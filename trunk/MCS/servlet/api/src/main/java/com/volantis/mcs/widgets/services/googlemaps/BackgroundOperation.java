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

/**
 * Implements background operation when cache is enabled.
 * Background operations needs separate class because both satellite and map mode
 * needs background operations. 
 */
public class BackgroundOperation implements MapOperation {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ZoomOperation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(BackgroundOperation.class);    
    
    
    public String perform(Map params) throws IllegalArgumentException {
        Integer x = WidgetServiceHelper.getIntParameter(params, P_X);
        Integer y = WidgetServiceHelper.getIntParameter(params, P_Y);
        String t =  WidgetServiceHelper.getParameter(params, P_T);
        Integer offx =  WidgetServiceHelper.getIntParameter(params, P_OFFX);
        Integer offy =  WidgetServiceHelper.getIntParameter(params, P_OFFY);         
        
        Integer zoom = WidgetServiceHelper.getIntParameter(params, P_ZOOM);
        
        
        if (null == zoom) {
            throw new IllegalArgumentException(exceptionLocalizer.format(
                    "widget-missing-mandatory-parameter","'zoom'"));
        }
        if ((null != t) &&  (null != offx) && (null != offy)) {
            return getPhotoBackground(zoom.intValue(), t,offx.intValue(),offy.intValue());
        } else if ((null != x) && (null != y)) {
            return getMapBackground(zoom.intValue(), x.intValue(), y.intValue());
        }
        throw new IllegalArgumentException(
                exceptionLocalizer.format("widget-invalid-bg-operation-params", 
                new Object []{"zoom"}));
    }

    /**
     * Get photo images from background operation - images normally invisible
     * @param currentZoom
     * @param t
     * @param offx
     * @param offy
     * @return
     */
    private String getPhotoBackground(int currentZoom, String t,int offx,int offy) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Executing zoomInPhoto(zoom=" + currentZoom + ", t=" + t + ")");
        }

        return OperationHelper.getInstance().getBgPhotoImagesList(t,offx,offy);
    }

    /**
     * get map images from background operation - images normally invisible.  
     * @param zoom
     * @param x
     * @param y
     * @return
     */
    private String getMapBackground(int zoom,int x,int y) {

        if (logger.isDebugEnabled()) {
            logger.debug("Executing zoomOutPhoto(zoom=" + zoom + ", x=" + x + ", y="+y+")");
        }
        
        return OperationHelper.getInstance().getBgMapImagesList(x, y, zoom);
    }
}
