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

package com.volantis.mcs.widgets.services.googlemaps;

import java.util.Map;

import com.volantis.mcs.googlemaps.OperationHelper;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.widgets.services.MapOperation;
import com.volantis.mcs.widgets.services.WidgetServiceHelper;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

public class SearchOperation implements MapOperation {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SearchOperation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(ZoomOperation.class);    
    
    
    // javadoc inherited
    public String perform(Map params) throws IllegalArgumentException,Exception {
        String mode = WidgetServiceHelper.getParameter(params, P_MODE);
        Integer zoom =  WidgetServiceHelper.getIntParameter(params, P_ZOOM);
        String query =  WidgetServiceHelper.getParameter(params, P_QUERY);
        
        if (null == mode) {
            throw new IllegalArgumentException(exceptionLocalizer.format(
                    "widget-missing-mandatory-parameter",P_MODE));
        }
        if (null == query) {
            throw new IllegalArgumentException(exceptionLocalizer.format(
                    "widget-missing-mandatory-parameter",P_QUERY));
        }
        if (MODE_PHOTO.equalsIgnoreCase(mode)) {
            return searchPhoto(query, zoom);
        } else if (MODE_MAP.equalsIgnoreCase(mode)) {
            return searchMap(query, zoom);
        }
        throw new IllegalArgumentException(
                exceptionLocalizer.format("widet-invalid-search-operation-param", 
                        new Object []{P_MODE,P_QUERY}));
    }

    private String searchMap(String query, Integer zoom) throws Exception {        
        if (logger.isDebugEnabled()) {
            StringBuffer logMsg = new StringBuffer(
                    "Executing searchMap(query=" + query);
            if (zoom != null) {
                logMsg.append(", zoom=" + zoom.intValue());
            }
            logMsg.append(")");
            logger.debug(logMsg);
        }
        return OperationHelper.getInstance().performSearchMap(query, zoom);
    }

    private String searchPhoto(String query, Integer zoom) throws Exception {
        if (logger.isDebugEnabled()) {
            StringBuffer logMsg = new StringBuffer(
                    "Executing searchMap(query=" + query);
            if (zoom != null) {
                logMsg.append(", zoom=" + zoom.intValue());
            }
            logMsg.append(")");
            logger.debug(logMsg);
        }
        return OperationHelper.getInstance().performSearchPhoto(query, zoom);
    }
}
