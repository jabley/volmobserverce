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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.shared.time.Period;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


/**
 * Rule for cacheControl element
 */
public class CacheControlRule extends DynamicElementRuleImpl {


    // javadoc inherited from interface
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName element, Attributes attributes) throws SAXException {

        XMLPipelineContext context = dynamicProcess.getPipelineContext();

        CacheProperties properties = (CacheProperties) context.
                findObject(CacheProperties.class);

        if (properties == null) {
            forwardError(dynamicProcess, "Cache properties can not be " +
                    "found. Unexpected cacheControl element");
        } else {
            CacheControl cacheControl = createCacheControl(attributes);
            if (cacheControl != null) {
                properties.setCacheControl(cacheControl);
            }
        }
        return null;
    }


    /**
     * Create a CacheControl from some Attributes that contain cacheControl
     * properties.
     *
     * @param attributes Attributes containing CacheControl properties.
     */
    protected CacheControl createCacheControl(Attributes attributes) {

        CacheControl cacheControl = new CacheControl();

        if (attributes != null && attributes.getLength() > 0) {
            final String timeToLive = attributes.getValue("timeToLive");
            cacheControl.setTimeToLive(calculateTimeToLive(timeToLive));
        }

        return cacheControl;
    }

    /**
     * Return a Period based on the specified timeToLive value based on
     * following algorithm:
     * <ul>
     * <li>If it is {@link CacheControl.LIVE_FOREVER} or null, then
     * {@link Period.INDEFINITELY} is returned.</li>
     * <li>If it cannot be parsed as an integer, then a NumberFormatException
     * is thrown.</li>
     * <li>If it is less than zero, an IllegalArgumentException is thrown.</li>
     * </ul>
     *
     * @param timeToLive the string time to live value - may be null.
     * @return a Period - not null.
     * @throws NumberFormatException    if the timeToLive isn't
     *                                  CacheControl.LIVE_FOREVER, null
     *                                  or an integer greater than -1.
     * @throws IllegalArgumentException if the string represents an integer
     *                                  which is less than 0
     */
    static Period calculateTimeToLive(String timeToLive) {

        // Made this method static since it doesn't use an instance variables.
        // Made this method have default visibility to allow for testing.

        // @todo potentially this should be moved onto the CacheControl class.
        Period result;

        if (timeToLive == null ||
                CacheControl.LIVE_FOREVER.equals(timeToLive)) {
            result = Period.INDEFINITELY;
        } else {
            int timeToLiveInt = Integer.parseInt(timeToLive);
            if (timeToLiveInt < 0) {
                throw new IllegalArgumentException("timeToLive must be >-1, " +
                        "null or the LIVE_FOREVER " +
                        "constant; was: " + timeToLive);
            }
            result = Period.inSeconds(timeToLiveInt);
        }
        return result;
    }

    // javadoc inherited from interface
    public void endElement(DynamicProcess dynamicProcess,
                           ExpandedName element, Object object) throws SAXException {
        // nothing to do
    }
}
