/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.agent.impl;

import com.volantis.map.agent.AgentRequestId;
import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.MediaAgentException;
import com.volantis.map.agent.Request;
import com.volantis.map.agent.ResponseCallback;
import com.volantis.map.common.encryption.Encrypter;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.MissingParameterException;
import com.volantis.synergetics.descriptorstore.ParameterNames;
import com.volantis.synergetics.descriptorstore.Parameters;
import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStore;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreException;
import com.volantis.synergetics.log.LogDispatcher;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class DefaultMediaAgent implements MediaAgent {

    /**
     * Default time to live value for the data store items.
     */
    private static int DEFAULT_TIME_TO_LIVE = 600;

    /**
    * Used for logging
    */
   private static final LogDispatcher LOGGER =
       LocalizationFactory.createLogger(DefaultMediaAgent.class);

    /**
     * Queue of agent requests.
     */
    private List requestQueue = new LinkedList();

    /**
     * Map associating agent request IDs with agent requests.
     */
    private Map requestMap = new HashMap();

    private final ResourceDescriptorStore descriptorStore;

    /**
     * URL prefix for the resource external URL
     */
    private final String urlPrefix;

    private final Object lock;

    private long timeToLive = DEFAULT_TIME_TO_LIVE;

    private Collection<String> externalIds = new LinkedList<String>();

    public DefaultMediaAgent(final ResourceDescriptorStore descriptorStore,
                             final String urlPrefix) {
        this.descriptorStore = descriptorStore;
        this.urlPrefix = urlPrefix;
        lock = new Object();
    }

    // javadoc inherited
    public AgentRequestId requestURL(Request request, ResponseCallback callback) {
        AgentRequestId requestId = new DefaultAgentRequestId();
        AgentRequest agentRequest = new AgentRequest(request, callback);
        synchronized (this) {
            requestQueue.add(requestId);
            requestMap.put(requestId, agentRequest);
        }
        return requestId;
    }

    // javadoc inherited
    public void waitForComplete() throws MediaAgentException {
        synchronized (lock) {
            for (Iterator iter = requestQueue.iterator(); iter.hasNext(); ) {
                final AgentRequestId requestId = (AgentRequestId) iter.next();
                final AgentRequest agentRequest =
                    (AgentRequest) requestMap.get(requestId);
                if (agentRequest.getState().equals(AgentRequestState.PENDING)) {
                    waitForComplete(requestId);
                }
            }
        }
    }

    // javadoc inherited
    public void waitForComplete(final AgentRequestId requestId)
            throws MediaAgentException {
        synchronized (lock) {
            final AgentRequest agentRequest =
                (AgentRequest) requestMap.get(requestId);
            final boolean succeed = agentRequest.beginProcessing();
            if (succeed) {
                processRequest(agentRequest);
                agentRequest.completeProcessing();
            }
        }
    }

    // javadoc inherited
    public void waitForComplete(final Set requestIds) throws MediaAgentException {
        synchronized (lock) {
            for (Iterator iter = requestIds.iterator(); iter.hasNext(); ) {
                final AgentRequestId requestId = (AgentRequestId) iter.next();
                final AgentRequest agentRequest =
                    (AgentRequest) requestMap.get(requestId);
                if (agentRequest.getState().equals(AgentRequestState.PENDING)) {
                    waitForComplete(requestId);
                }
            }
        }
    }

    /**
     * Internal method for processing a single request.
     *
     * @param agentRequest The request to process.
     */
    private void processRequest(AgentRequest agentRequest) {
        Parameters inputParameters = descriptorStore.createParameters();
        ParameterNames outputParameters = descriptorStore.createParameterNames();

        final Request request = agentRequest.getRequest();
        final com.volantis.map.common.param.Parameters inputParams =
            request.getInputParams();
        final Iterator requestInputParams = inputParams.getParameterNames();
        try {
            while (requestInputParams.hasNext()) {
                String paramName = (String) requestInputParams.next();
                inputParameters.setParameterValue(
                    paramName, inputParams.getParameterValue(paramName));
            }
        } catch (MissingParameterException e) {
            // this is not expected as we iterate through the parameter names
            throw new UndeclaredThrowableException(e);
        }

        Iterator requestOutputParams = request.getOutputParams().iterator();
        while (requestOutputParams.hasNext()) {
            String paramName = (String) requestOutputParams.next();
            outputParameters.setName(paramName);
        }

        // TODO later If there's an appropriate plugin, allow it to do pre-processing of the parameters.
        // plugin.preProcess(inputParameters, outputParameters, device);

        ResourceDescriptor descriptor = descriptorStore.createDescriptor(
            request.getResourceType(), inputParameters, outputParameters,
            timeToLive);

        // TODO later If there's an appropriate plugin, allow it to do post-processing of the parameters.
        // plugin.postProcess(outputParameters, device);

        DefaultAgentParameters callbackParameters = new DefaultAgentParameters();
        Iterator processedParameters = descriptor.getOutputParameters().iterator();
        while (processedParameters.hasNext()) {
            Parameters.Entry entry = (Parameters.Entry) processedParameters.next();
            callbackParameters.setParameterValue(entry.getName(), entry.getValue());
        }
        String externalId = descriptor.getExternalID();
        externalIds.add(externalId);
        externalId = Encrypter.defaultEncrypt(externalId);
        // base 64 can contain "/"
        externalId = externalId.replaceAll("/", "-");
        callbackParameters.setParameterValue(
            OUTPUT_URL_PARAMETER_NAME, urlPrefix + externalId);

        try {
            agentRequest.getResponseCallback().execute(callbackParameters);
        } catch (Exception e) {
            LOGGER.warn("error-executing-response-callback", request.getSourceURL(),e);
        }
    }

    // javadoc inherited
    public void ensureMinTimeToLive(final long timeToLive) {
        synchronized (lock) {
            if (this.timeToLive < timeToLive) {
                this.timeToLive = timeToLive;
                for (String externalId: externalIds) {
                    try {
                        descriptorStore.updateDescriptorTimeToLive(externalId, timeToLive);
                    } catch (ResourceDescriptorStoreException e) {
                        // no matching item was found
                        LOGGER.error("invalid-resource-id", externalId, e);
                    }
                }
            }
        }
    }
}
