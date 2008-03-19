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

public class ZoomOperation implements MapOperation {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ZoomOperation.class);
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(ZoomOperation.class);    
        
    // javadoc inherited
    public String perform(Map params) throws IllegalArgumentException,Exception {
        String direction = WidgetServiceHelper.getParameter(params, P_DIR);
        Integer x = WidgetServiceHelper.getIntParameter(params, P_X);
        Integer y = WidgetServiceHelper.getIntParameter(params, P_Y);
        String t =  WidgetServiceHelper.getParameter(params, P_T);
        Integer offx =  WidgetServiceHelper.getIntParameter(params, P_OFFX);
        Integer offy =  WidgetServiceHelper.getIntParameter(params, P_OFFY);        
        Integer zoom = WidgetServiceHelper.getIntParameter(params, P_ZOOM);
        
        if (null == direction) {
            throw new IllegalArgumentException(
                    exceptionLocalizer.format("widget-missing-dir-parameter"));
        }
        if (null == zoom) {
            throw new IllegalArgumentException(
                    exceptionLocalizer.format("widget-missing-zoom-parameter"));
        }        
        
        if (null != t && null != offx && null != offy && DIR_IN.equals(direction)) {
            return zoomInPhoto(zoom.intValue(), t,offx.intValue(),offy.intValue());
        } if (null != t && null != offx && null != offy && DIR_OUT.equals(direction)) {
            return zoomOutPhoto(zoom.intValue(), t,offx.intValue(),offy.intValue());
        } else if ((null != x) && (null != y) && DIR_IN.equals(direction)) {
            return zoomInMap(zoom.intValue(), x.intValue(), y.intValue());
        } else if ((null != x) && (null != y) && DIR_OUT.equals(direction)) {
            return zoomOutMap(zoom.intValue(), x.intValue(), y.intValue());
        }
        throw new IllegalArgumentException(
                exceptionLocalizer.format("widet-invalid-zoom-operation-params"));
    }

    private String zoomInPhoto(int currentZoom, String t,int offx,int offy) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Executing zoomInPhoto(zoom=" + currentZoom + ", t=" + t + ")");
        }

        return OperationHelper.getInstance().getSatZoomIn(t,offx,offy);
    }

    private String zoomOutPhoto(int currentZoom,String t,int offx,int offy) {

        if (logger.isDebugEnabled()) {
            logger.debug("Executing zoomOutPhoto(zoom=" + currentZoom + ", t=" + t + ")");
        }
        
        return OperationHelper.getInstance().getSatZoomOut(t,offx,offy);
    }

    private String zoomInMap(int currentZoom, int x, int y) {

        if (logger.isDebugEnabled()) {
            logger.debug("Executing zoomInMap(zoom=" + currentZoom + ", x=" + x + ", y=" + y + ")");
        }
        return OperationHelper.getInstance().getMapZoomIn(x, y, currentZoom);
    }

    private String zoomOutMap(int currentZoom, int x, int y) {

        if (logger.isDebugEnabled()) {
            logger.debug("Executing zoomOutMap(zoom=" + currentZoom + ", x=" + x + ", y=" + y + ")");
        }
        return OperationHelper.getInstance().getMapZoomOut(x, y, currentZoom);
    }
}
