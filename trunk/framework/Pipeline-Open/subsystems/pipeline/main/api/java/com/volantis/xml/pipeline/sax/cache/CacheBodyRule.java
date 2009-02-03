/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Â  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.Â  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.cache.Cache;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.cache.body.CacheBodyOperationProcess;
import com.volantis.xml.pipeline.sax.cache.body.CacheBodyOperationProcessState;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule for cacheBody element
 */
public class CacheBodyRule extends DynamicElementRuleImpl {


    // javadoc inherited from interface
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName element, Attributes attributes) throws SAXException {

        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        EndElementAction action = EndElementAction.DO_NOTHING;

        CacheProperties properties = (CacheProperties) context.
                findObject(CacheProperties.class);

        if (properties != null) {


            CacheProcessConfiguration cpc =
                    (CacheProcessConfiguration) dynamicProcess.getPipelineContext().
                            getPipelineConfiguration().
                            retrieveConfiguration(CacheProcessConfiguration.class);

            final String cacheName = properties.getCacheName();
            final Cache cache = cpc.getCache(cacheName);

            // if properties is null then cacheBody element is used outside
            // cache element or cache element has key attribute set, so 
            // recording process has been already started.
            CacheBodyOperationProcess operation =
                    new CacheBodyOperationProcess();

            XMLPipeline pipeline = dynamicProcess.getPipeline();

            operation.setCacheName(cacheName);
            operation.setCacheKey(properties.getCacheKey());
            final CacheControl cacheControl = properties.getCacheControl();
            if (cacheControl != null && cacheControl.getTimeToLive() == null) {
                // if no TTL period has been set, use the default value for this
                // cache
                cacheControl.setTimeToLive(cpc.getDefaultTimeToLive(cacheName));
            }
            operation.setCacheControl(cacheControl);
            operation.setPipeline(pipeline);
            operation.setCache(cache);
            operation.setFixedExpiryMode(properties.isFixedExpiryMode());
            operation.setMaxWaitTime(properties.getMaxWaitTime());

            try {
                CacheBodyOperationProcessState state =
                        operation.initializeProcessState();

                // If there is a cached recorder to playback from then we want
                // to suppress all of the content.
                if (state == CacheBodyOperationProcessState.
                        PLAYBACK_AND_SUPPRESS) {
                    // supress all content
                    context.getFlowControlManager().exitCurrentElement();
                }

                dynamicProcess.addProcess(operation);
                action = new EndElementAction() {
                    public void doAction(DynamicProcess dynamicProcess)
                            throws SAXException {
                        // Remove the process from the head, assume that it is
                        // the one that we added, should probably check.
                        dynamicProcess.removeProcess();
                    }
                };

            } catch (SAXException exc) {
                forwardFatalError(dynamicProcess, "A problem was encountered "
                        + "starting the caching process.");
            }
        }
        return action;
    }

    // javadoc inherited from interface
    public void endElement(DynamicProcess dynamicProcess, ExpandedName element,
                           Object object) throws SAXException {
        // Perform the action.
        EndElementAction action = (EndElementAction) object;
        action.doAction(dynamicProcess);
    }
}
