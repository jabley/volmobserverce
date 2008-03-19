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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.agent.impl;

import com.volantis.map.agent.Request;
import com.volantis.map.agent.RequestFactory;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.imageprocessor.parameters.ICSParamBuilder;
import com.volantis.map.ics.imageprocessor.parameters.ParameterBuilderException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

/**
 * Default implementation of the {@link RequestFactory} interface.
 */
public class DefaultRequestFactory extends RequestFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultRequestFactory.class);

     /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    DefaultRequestFactory.class);

    private final ICSParamBuilder builder = new ICSParamBuilder();

    /**
     * Create a request object for the specified resource type, srcUrl and
     * the input parameters in the supplied map. This Request object is
     * immutable.
     *
     * @param resourceType
     * @param srcUrl
     * @param inputParameters
     * @return
     */
    public Request createRequest(String resourceType, URI srcUrl, Map inputParameters) {
        if (srcUrl != null) {
            inputParameters.put(ParameterNames.SOURCE_URL, srcUrl.toASCIIString());
        }
        DefaultRequest req =
            new DefaultRequest(resourceType, ParameterNames.SOURCE_URL);
        DefaultAgentParameters daf = (DefaultAgentParameters) req.getInputParams();
        Iterator it = inputParameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            daf.setParameterValue((String)entry.getKey(), (String) entry.getValue());
        }
        return req;
    }

    // javadoc inherited
    public Request createRequestFromICSURI(URI uri) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("creating media agent request for ICS URL '" +
                         uri.toString() + "'");
        }
        DefaultRequest request =
                new DefaultRequest("image.", ParameterNames.SOURCE_URL);
        try {
            builder.build(uri, (MutableParameters)request.getInputParams());
        } catch (ParameterBuilderException e) {
            throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format(
                            "agent-request-creation-failure", uri));
        }
        return request;
    }
}
