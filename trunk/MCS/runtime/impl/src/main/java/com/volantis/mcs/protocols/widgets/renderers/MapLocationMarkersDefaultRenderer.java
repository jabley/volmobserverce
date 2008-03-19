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

import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.MapLocationMarkerContextHandler;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkersAttributes;

public class MapLocationMarkersDefaultRenderer extends WidgetDefaultRenderer {

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.SHOW, ActionName.HIDE
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.CURRENT
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {};
    
    
    protected void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-map.mscr",protocol);
        
        
        // Generate an ID for widget, if not specified.
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

        String mapId = protocol.getWidgetModule().getMapRenderer().getCurrentMapId();
        
        if (mapId == null) {
            throw new ProtocolException("widget:map-location-markers element "
                    + "placed outside widget:map element");
        }
        
        MapLocationMarkersAttributes mapLocationMarkersAttributes 
                = (MapLocationMarkersAttributes)attributes;
        
        // retrive list of attributes of <map-location-marker> element which are
        // within the <w:map-location-markers> element.
        List mapLocationMarkerAttributes = mapLocationMarkersAttributes.getContentAttributes();
        
        // create ClockContentHandler to merge contents
        MapLocationMarkerContextHandler mapLocationMarkerHandler 
                = new MapLocationMarkerContextHandler();
        
        mapLocationMarkerHandler.mergeMapLocationMarkerAttributes(mapLocationMarkerAttributes);
        
        List markersList = mapLocationMarkerHandler.getMarkersList();
        String markers = getMarkersIds(markersList);
        
        StringBuffer textBuffer = new StringBuffer();
        
        textBuffer.append(
                createJavaScriptWidgetRegistrationOpening(
                        mapLocationMarkersAttributes.getId()))
                .append("new Widget.MapLocationMarkers(")
                .append(createJavaScriptString(mapLocationMarkersAttributes.getId()))
                .append(", {")
                .append("mapId: ").append(createJavaScriptString(mapId))
                .append(", markers: [").append(markers).append("]")
                .append("})")
                .append(createJavaScriptWidgetRegistrationClosure());
        
        addCreatedWidgetId(mapLocationMarkersAttributes.getId());
        addUsedWidgetIds(markersList);
        addUsedWidgetId(mapId);
        
        writeJavaScript(textBuffer.toString());
    }
    
    
    private String getMarkersIds(List markers) throws ProtocolException {
        Iterator i = markers.iterator();
        StringBuffer sb = new StringBuffer();
        if (i.hasNext()) {
            sb.append(createJavaScriptString((String)i.next()));
        }
        while (i.hasNext()){
            sb.append(", ");
            sb.append(createJavaScriptString((String)i.next()));
        }
        return sb.toString();
    }
        
    
    private void addUsedWidgetIds(List markers) throws ProtocolException {
        Iterator i = markers.iterator();
        while (i.hasNext()) {
            addUsedWidgetId((String) i.next());
        }
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
