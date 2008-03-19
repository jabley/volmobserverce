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

package com.volantis.mcs.widgets.services;

import java.util.Map;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

public class WidgetServiceHelper {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(WidgetServiceHelper.class);

    /**
     * Returns a parameter from a servlet parameter map 
     * 
     * @param params is a parameter map obtained from the servlet 
     * via getParameterMap
     */
    public static String getParameter(Map params, String key) {
        // The parameters map contains arrays of strings as values
        String[] values = (String[])params.get(key);
        if (null != values && values.length > 0) {
            return values[0];
        }
        return null;
    }

    /**
     * Returns an integer parameter from a servlet parameter map 
     * 
     * @param params is a parameter map obtained from the servlet 
     * via getParameterMap
     */
    public static Integer getIntParameter(Map params, String key) {
        
        Integer value = null;
        // Note: it's OK to pass null to valueOf(), 
        // as this results in NumberFormatException whoich we handle anyway 
        try {            
            value = Integer.valueOf(getParameter(params, key));
        } catch (NumberFormatException x) {
            if (logger.isDebugEnabled()) {
                logger.debug("Parameter " + key + " = " + getParameter(params, key) + " is not an integer");
            }
            // The contract is not to throw an exception, but just return null
        } 
        return value;
    }
}
