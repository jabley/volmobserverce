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
package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkersAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

import java.util.List;

public class MapLocationMarkerElement extends WidgetElement {
    
    private final static int MIN_MARKER_ZOOM = 0;
    
    private final static int MAX_MARKER_ZOOM = 17;
    /**
     * Creates and initialises new instance of Map Location Marker element.
     * @param context
     */
    public MapLocationMarkerElement(XDIMEContextInternal context) {
        // Initialise superclass.
        super(WidgetElements.MAP_LOCATION_MARKER, context);

        // Create an instance of MapLocationMarker attributes.
        // It'll be initialised later in initialiseAttributes() method.
        protocolAttributes = new MapLocationMarkerAttributes();
    }
    
    
    
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {

        int minZoom = getMinZoomAttributeValue(context, attributes);
        int maxZoom = getMaxZoomAttributeValue(context, attributes);

        if (maxZoom < minZoom) {
            throw new XDIMEException("Value of max-zoom attribute of "
                    + "map-location-marker element must not be lower than "
                    + "min-zoom attibute");            
        } else {
            ((MapLocationMarkerAttributes)protocolAttributes)
                    .setMinZoom(minZoom);
            ((MapLocationMarkerAttributes)protocolAttributes)
                    .setMaxZoom(maxZoom);
        }
        
        double latitude = getLatitudeAttributeValue(context, attributes);
        if (!Double.isNaN(latitude)) {
            ((MapLocationMarkerAttributes)protocolAttributes)
                    .setLatitude(latitude);
        }

        double longitude = getLongitudeAttributeValue(context, attributes);
        if (!Double.isNaN(longitude)) {
            ((MapLocationMarkerAttributes) protocolAttributes)
                    .setLongitude(longitude);
        }

        String src = getSrcAttributeValue(context, attributes);

        ((MapLocationMarkerAttributes) protocolAttributes).setLongitude(longitude);
        ((MapLocationMarkerAttributes) protocolAttributes).setLatitude(latitude);
        ((MapLocationMarkerAttributes) protocolAttributes).setSrc(src);
        ((MapLocationMarkerAttributes) protocolAttributes).setMinZoom(minZoom);
        ((MapLocationMarkerAttributes) protocolAttributes).setMaxZoom(maxZoom);
    }


    /**
     * Retrieves the value of the 'latitude' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'latitude' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected double getLatitudeAttributeValue(XDIMEContextInternal context, 
            XDIMEAttributes attributes) throws XDIMEException {
        
        double latitude = Double.NaN;
        String attrVal = attributes.getValue("", "latitude");
        if (attrVal != null) {
            try {
                latitude = Double.parseDouble(attrVal);
            } catch (NumberFormatException nfe) {
                throw new XDIMEException("\"latitude\" attribute of "
                        + "map-location-marker element must be double");
            }
        } else {
            throw new XDIMEException("\"latitude\" attribute of map-location-"
                    + "marker element must be specified.");
        }
        return latitude;
    }
    
    /**
     * Retrieves the value of the 'longitude' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'longitude' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected double getLongitudeAttributeValue(XDIMEContextInternal context, 
            XDIMEAttributes attributes) throws XDIMEException {

        double longitude = Double.NaN;
        String attrVal = attributes.getValue("","longitude");
        if (attrVal != null) {
            try {
                longitude = Double.parseDouble(attrVal);
            } catch (NumberFormatException nfe) {
                throw new XDIMEException("\"longitude\" attribute of " 
                        + "map-location-marker element must be double");
            }
        } else {
            throw new XDIMEException("\"longitude\" attribute of map-location-"
                    + "marker element must be specified.");
        }
        return longitude;
    }
    
    protected String getSrcAttributeValue(XDIMEContextInternal context, 
            XDIMEAttributes attributes) throws XDIMEException {

        String src = attributes.getValue("","src");
        if (src != null) {
            src = rewriteURLWithPageURLRewriter(context, src, PageURLType.WIDGET);
        } else {
            throw new XDIMEException("\"src\" attribute of map-location-marker"
                    + " element must be specified.");

        }
        return src;
    }
    
    /**
     * Retrieves the value of the 'min-zoom' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'min-zoom' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     *         or -1 if the value was not specified by XDIME page author
     * @throws XDIMEException
     */
    protected int getMinZoomAttributeValue(XDIMEContextInternal context, 
            XDIMEAttributes attributes) throws XDIMEException {

        int minZoom = -1;
        String attrVal = attributes.getValue("","min-zoom");
        if (attrVal != null) {
            try {
                minZoom = Integer.parseInt(attrVal);
            } catch (NumberFormatException nfe) {
                throw new XDIMEException("\"min-zoom\" attribute of " 
                        + "map-location-marker element must be Integer");
            }
            if (minZoom < MIN_MARKER_ZOOM || minZoom > MAX_MARKER_ZOOM) {
                throw new XDIMEException("\"min-zoom\" attribute of " 
                        + "map-location-marker element must belong to [" +
                        MIN_MARKER_ZOOM + "," +
                        MAX_MARKER_ZOOM +"]");
            }
        }
        return minZoom;
    }

    /**
     * Retrieves the value of the 'max-zoom' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'max-zoom' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     *         or -1 if the value was not specified by XDIME page author
     * @throws XDIMEException
     */
    protected int getMaxZoomAttributeValue(XDIMEContextInternal context, 
            XDIMEAttributes attributes) throws XDIMEException {

        int maxZoom = MAX_MARKER_ZOOM;
        String attrVal = attributes.getValue("","max-zoom");
        if (attrVal != null) {
            try {
                maxZoom = Integer.parseInt(attrVal);
            } catch (NumberFormatException nfe) {
                throw new XDIMEException("\"max-zoom\" attribute of " 
                        + "map-location-marker element must be Integer");
            }
            if (maxZoom < MIN_MARKER_ZOOM 
                    || maxZoom > MAX_MARKER_ZOOM) {
                throw new XDIMEException("\"max-zoom\" attribute of " 
                        + "map-location-marker element must belong to [" +
                        MIN_MARKER_ZOOM + "," +
                        MAX_MARKER_ZOOM +"]");
            }
        }
        return maxZoom;
    }



    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        
        WidgetAttributes parentWidgetAttributes = 
            (WidgetAttributes)((WidgetElement)parent).getProtocolAttributes();
        
        if (parentWidgetAttributes instanceof MapLocationMarkersAttributes) {
            List list = ((MapLocationMarkersAttributes)parentWidgetAttributes)
                .getContentAttributes();

            //add attributes to list of content attributes 
            list.add(getProtocolAttributes());
        } 
        //else not possible if correct validation
                
        return super.callOpenOnProtocol(context,attributes);  
        
    }
    
    
    
}
