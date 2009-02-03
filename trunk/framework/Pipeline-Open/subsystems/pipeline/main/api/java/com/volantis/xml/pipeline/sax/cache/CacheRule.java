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
import com.volantis.cache.group.Group;
import com.volantis.shared.time.Period;
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
 * Rule for cache element
 */
public class CacheRule extends DynamicElementRuleImpl {


    // javadoc inherited
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName element,
                               Attributes attributes) throws SAXException {

        XMLPipelineContext context = dynamicProcess.getPipelineContext();

        EndElementAction action = EndElementAction.DO_NOTHING;

        final CacheProcessConfiguration cpc =
                (CacheProcessConfiguration) context.getPipelineConfiguration().
                        retrieveConfiguration(CacheProcessConfiguration.class);

        final String cacheName = attributes.getValue("name");
        final String cacheKeyString = attributes.getValue("key");
        // set max wait time
        final Period maxWaitTime = calculateMaxWaitTime(attributes.getValue("max-wait-time"));
        
        // set the expiry mode
        final String expiryMode = attributes.getValue("expiry-mode");
        final boolean fixedExpiryMode;
        if (expiryMode == null || expiryMode.length() == 0) {
            // get the global expiry mode
            fixedExpiryMode = cpc.isFixedExpiryMode();
        } else {
            fixedExpiryMode = !"auto".equals(expiryMode);
        }

        if (cacheName == null || cacheName.length() == 0) {
            forwardError(dynamicProcess, "Cache name must be specified");
        }


        final Cache cache = cpc.getCache(cacheName);
        if (cache != null) {

            if (cacheKeyString != null) {
                // <cache name="cacheName" key="cacheKey">...
                // cacheInfo element is not expected
                // <cacheBody>? is optional


                // Need to populate the cache by doing the OperationProcess
                // at least once.
                CacheBodyOperationProcess operation =
                        new CacheBodyOperationProcess();

                final CacheControl cacheControl = new CacheControl();
                cacheControl.setTimeToLive(cpc.getDefaultTimeToLive(cacheName));
                operation.setCacheControl(cacheControl);

                operation.setCacheName(cacheName);
                operation.setCache(cache);
                CacheKey cacheKey = new CacheKey();
                cacheKey.addKey(cacheKeyString);
                operation.setCacheKey(cacheKey);
                XMLPipeline pipeline = dynamicProcess.getPipeline();
                operation.setPipeline(pipeline);
                operation.setFixedExpiryMode(fixedExpiryMode);
                operation.setMaxWaitTime(maxWaitTime);

                try {
                    CacheBodyOperationProcessState state =
                            operation.initializeProcessState();

                    // If there is a cached recorder to playback from
                    // then we want to suppress all of the content.
                    if (state == CacheBodyOperationProcessState.
                            PLAYBACK_AND_SUPPRESS) {
                        // supress all content
                        context.getFlowControlManager().
                                exitCurrentElement();
                    }

                    dynamicProcess.addProcess(operation);
                    action = new EndElementAction() {
                        public void doAction(DynamicProcess dynamicProcess)
                                throws SAXException {
                            // Remove the process from the head, assume
                            // that it is the one that we added,
                            // should probably check.
                            dynamicProcess.removeProcess();
                        }
                    };

                } catch (SAXException exc) {
                    forwardFatalError(dynamicProcess, "A problem was" +
                            " encountered starting the caching process.");
                }

            } else {
                CacheProperties properties = new CacheProperties();
                properties.setCacheName(cacheName);
                properties.setFixedExpiryMode(fixedExpiryMode);
                properties.setMaxWaitTime(maxWaitTime);
                context.pushObject(properties, false);

                action = new EndElementAction() {
                    public void doAction(DynamicProcess dynamicProcess)
                            throws SAXException {

                        XMLPipelineContext context = dynamicProcess.
                                getPipelineContext();

                        Object properties = context.popObject();
                        if (!(properties instanceof CacheProperties)) {
                            forwardFatalError(dynamicProcess,
                                    "Expected properties to be instance of " +
                                            "CacheProperties, but was " +
                                            properties.getClass());
                        }
                    }
                };

            }
        } else {
            // Send a warning down the pipeline that the cache could not be
            // found.
            forwardFatalError(dynamicProcess, "The requested cache " +
                    cacheName + " could not be found.");
        }
        return action;
    }

    //  javadoc inherited from interface
    public void endElement(DynamicProcess dynamicProcess, ExpandedName element,
                           Object object) throws SAXException {

        // Perform the action.
        EndElementAction action = (EndElementAction) object;
        action.doAction(dynamicProcess);
    }
    
    /**
     * Return a Period based on the specified timeve value based on
     * following algorithm:
     * <ul>
     * <li>If it is null, then
     * {@link CacheBodyOperationProcess.DEFAULT_MAX_WAIT_TIME} is returned.</li>
     * <li>If it is {@link CacheControl.LIVE_FOREVER}, then
     * {@link Period.INDEFINITELY} is returned.</li>
     * <li>If it cannot be parsed as an integer, then a NumberFormatException
     * is thrown.</li>
     * <li>If it is less than zero, an IllegalArgumentException is thrown.</li>
     * </ul>
     *
     * @param time the string to be converted to period - may be null.
     * @return a Period - not null.
     * @throws NumberFormatException    if the time isn't
     *                                  CacheControl.LIVE_FOREVER, null
     *                                  or an integer greater than -1.
     * @throws IllegalArgumentException if the string represents an integer
     *                                  which is less than 0
     */
    static Period calculateMaxWaitTime(String time) {

        Period result;

        if (time == null ) {
            result = CacheBodyOperationProcess.DEFAULT_MAX_WAIT_TIME;
        } else if (CacheControl.LIVE_FOREVER.equals(time)) {
            result = Period.INDEFINITELY;
        } else {
            int timeInt = Integer.parseInt(time);
            if (timeInt < 0) {
                throw new IllegalArgumentException("timeToLive must be >-1, " +
                        "null or the LIVE_FOREVER " +
                        "constant; was: " + time);
            }
            result = Period.inSeconds(timeInt);
        }
        return result;
    }    
}
