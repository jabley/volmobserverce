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

public class SwitchModeOperation implements MapOperation {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SwitchModeOperation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(SwitchModeOperation.class);    
    
    
    public String perform(Map params) throws IllegalArgumentException {
        String mode = WidgetServiceHelper.getParameter(params, P_MODE);
        Integer x = WidgetServiceHelper.getIntParameter(params, P_X);
        Integer y = WidgetServiceHelper.getIntParameter(params, P_Y);
        String t =  WidgetServiceHelper.getParameter(params, P_T);
        Integer offx =  WidgetServiceHelper.getIntParameter(params, P_OFFX);
        Integer offy =  WidgetServiceHelper.getIntParameter(params, P_OFFY);
        Integer zoom = WidgetServiceHelper.getIntParameter(params, P_ZOOM);
        
        if (null == mode) {
            throw new IllegalArgumentException(exceptionLocalizer.format(
                    "widget-missing-mandatory-parameter",P_MODE));
        }
        if (null == zoom) {
            throw new IllegalArgumentException(exceptionLocalizer.format(
                    "widget-missing-mandatory-parameter",P_ZOOM));
        }
        if ((null != t) && (null != offx) && (null != offy) && MODE_MAP.equalsIgnoreCase(mode)) {
            return switchToMap( t,offx.intValue(),offy.intValue());
        }
        else if ((null != x) && (null != y) && MODE_PHOTO.equalsIgnoreCase(mode)) {
            return switchToPhoto(zoom.intValue(), x.intValue(), y.intValue());
        }
        else {
            throw new IllegalArgumentException(
                    exceptionLocalizer.format("widet-invalid-search-operation-param", 
                    new Object []{P_MODE,P_ZOOM}));
        }
    }

    private String switchToMap(String t,int offx, int offy)  {        
        
        if (logger.isDebugEnabled()) {
            logger.debug("Executing switchToMap(t=" + t + ")");
        }
        return OperationHelper.getInstance().getMapImagesListFromTxt(t,offx,offy);
    }

    private String switchToPhoto(int zoom, int x, int y)  {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Executing switchToPhoto(zoom=" + zoom + ", x=" + x + ", y=" + y +")");
        }

        return OperationHelper.getInstance().getPhotoImagesListFromCoord(x,y,zoom);
    }
}
