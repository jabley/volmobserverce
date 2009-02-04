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

package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkerAttributes;
import com.volantis.mcs.googlemaps.GImage;
import com.volantis.mcs.googlemaps.GLatLng;
import com.volantis.mcs.googlemaps.GPoint;
import com.volantis.mcs.googlemaps.GoogleCalculator;
import com.volantis.mcs.googlemaps.GoogleCalculatorExtended;

public class MapLocationMarkerDefaultRenderer extends WidgetDefaultRenderer {

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.SELECT, ActionName.DESELECT
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {};
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {
            EventName.SELECTED, EventName.DESELECTED
        };
    
    protected void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        String id = attributes.getId();
        
        if (id == null) {
            id = protocol.getMarinerPageContext().generateUniqueFCID();
            attributes.setId(id);
        }
    }

    protected void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        
        if(!isWidgetSupported(protocol)) {
            return;
        }
        
        MapLocationMarkerAttributes attr = (MapLocationMarkerAttributes)attributes;
        double lng = attr.getLongitude();
        double lat = attr.getLatitude();
        String src = attr.getSrc();
        int minZoom = attr.getMinZoom();
        int maxZoom = attr.getMaxZoom();
        
        GoogleCalculator calc = GoogleCalculatorExtended.getInstance();
        GPoint gPoint = calc.fromLatLngToPixel(new GLatLng(lat, lng), 0);
        GImage gImage = calc.fromGPixelToGImage(gPoint);
        
        StringBuffer textBuffer = new StringBuffer();
        
        textBuffer.append(
                createJavaScriptWidgetRegistrationOpening(
                        attr.getId()))
                .append("new Widget.MapLocationMarker(")
                .append(createJavaScriptString(attr.getId()))
                .append(", {")
                .append("lng: ").append(gImage.getImgX()).append(", ")
                .append("lat: ").append(gImage.getImgY()).append(", ")
                .append("src: ").append(createJavaScriptString(src)).append(", ")
                .append("minZoom: ").append(minZoom).append(", ")
                .append("maxZoom: ").append(maxZoom)
                .append("})")
                .append(createJavaScriptWidgetRegistrationClosure());
        
        addCreatedWidgetId(attr.getId());
        writeJavaScript(textBuffer.toString());

    }

    // Javadoc inherited
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }

    // Javadoc inherited
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }    
    
    // Javadoc inherited
    protected EventName[] getSupportedEventNames() {
        return SUPPORTED_EVENT_NAMES;
    }
    
}
