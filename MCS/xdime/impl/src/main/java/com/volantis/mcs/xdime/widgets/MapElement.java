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

package com.volantis.mcs.xdime.widgets;


import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.attributes.InputAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MapAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WidgetRenderer;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.PackagedURLEncoder;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;


public class MapElement extends WidgetElement implements InputContainer{

    //must be the same as com.volantis.mcs.googlemaps.GMapConst.MIN/MAX_ZOOM
    private final static int MIN_ZOOM = 0;
    
    private final static int MAX_ZOOM = 17;
	
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(MapElement.class);
    
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(MapElement.class);
    
    
    public MapElement(XDIMEContextInternal context){
        super(WidgetElements.MAP, context);
        protocolAttributes = new MapAttributes();
    }

    public void addInput(InputAttributes inputAttributes) {
        ((MapAttributes)protocolAttributes).addInput(inputAttributes);
    }
    

    // javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {
        try {
            WidgetRenderer widgetRenderer = getWidgetRenderer(context);
            if (null == widgetRenderer){
                // Do fallback if widget is not supported by the protocol
                doFallbackClose(context);
                return;       
            }
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
    
            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }
        resolveFakeImageURL(context, (MapAttributes)protocolAttributes);
        
        super.callCloseOnProtocol(context);
    }
    
    /**
     * Resolve ICS address for fake image. It is performed to get ICS 
     * address for further image transformations. 
     * 
     * @param context
     * @param attributes
     * @return
     * @throws XDIMEException
     */
    private boolean resolveFakeImageURL(XDIMEContextInternal context,
            MapAttributes attributes)
    throws XDIMEException{
        
        boolean written = false;

        MarinerRequestContext requestContext =
        context.getInitialRequestContext();
        
        
        ImageAttributes imageAttributes = attributes.getImageAttributes();
        if(imageAttributes == null){
            // widget is not supported therefore image attributes were not set. 
            written = false;
        } else {
            String url = imageAttributes.getSrc();
            // transcode the image.
            try {
                url = ContextInternals.constructImageURL(
                           requestContext, url);
            } catch (RepositoryException e) {
                throw new XDIMEException(e);
            }
      
            createFakeAssetURLMapEntry(requestContext, url);
                    
            // Rewrite the URL with PageURLRewriter.
            url = getPageContext(context)
                    .getAssetResolver()
                    .rewriteURLWithPageURLRewriter(url, PageURLType.IMAGE);
                    
            imageAttributes.setSrc(url);
            written = true;
        }
        return written;
    }
    
    /**
     * Create a fake asset URL map entry using the context and url.
     *
     * @param context the mariner request context.
     * @param url     the url use to create and add a fake asset URL map
     *                entry.
     *                
     *  @todo - this might be temporary - check how does it works without. 
     */
    private void createFakeAssetURLMapEntry(MarinerRequestContext context,
                                            String url) {
        // copied from ObjectElement implementation        
        ApplicationContext ac = ContextInternals.getApplicationContext(context);
        PackageResources pr = ac.getPackageResources();
        if (pr != null) {
            PackagedURLEncoder packagedURLEncoder = ac.getPackagedURLEncoder();
            if (packagedURLEncoder != null) {
                String encoded = packagedURLEncoder.getEncodedURI(url);
                PackageResources.Asset prAsset = new PackageResources.Asset(
                        url, false);
                pr.addAssetURLMapping(encoded, prAsset);
            }
        }
    }  
    
    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        int x = 0;
        
        double latitude = getLatitudeAttributeValue(context, attributes);
        if (!Double.isNaN(latitude)) {
            x += 1;
            ((MapAttributes) protocolAttributes).setLatitude(latitude);
        }
        
        double longitude = getLongitudeAttributeValue(context, attributes);
        if (!Double.isNaN(longitude)) {
            x += 1;
            ((MapAttributes) protocolAttributes).setLongitude(longitude);
        }
        
        if (x == 1) {
            throw new XDIMEException(
                    "Both or none of longitude and latitude "
                            + "attributes must be specified");
        }
        
        
        String query = getQueryAttributeValue(context, attributes);
        if (query != null) {
            if (x > 0) {
                throw new XDIMEException("longitude and/or latitude "
                                + "attributes must not be specified while " 
                                + "query attribute is specified");
            }
            x++;
            ((MapAttributes) protocolAttributes).setQuery(query);
            
        }
        
        int zoom = getZoomAttributeValue(context, attributes);
        
        if (zoom > -1) {
        	if (zoom < MIN_ZOOM || zoom > MAX_ZOOM) {
               	throw new XDIMEException("\"zoom\" attribute of " 
                        + "map element must belong to ["
                        + MIN_ZOOM + ","
                        + MAX_ZOOM + "]");
        	} else if (x == 0) {
        		throw new XDIMEException("zoom attribute can be specified "
        				+ "only when initial location is");
        	}
        	zoom = MAX_ZOOM - zoom;
            ((MapAttributes) protocolAttributes).setZoom(zoom);
        }
 

        
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
                throw new XDIMEException("\"latitude\" attribute of map "
                        + "element must be double");
            }
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
                throw new XDIMEException("\"longitude\" attribute of map "
                        + "element must be double");
            }
        }
        return longitude;
    }
    
    /**
     * Retrieves the value of the 'zoom' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'zoom' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected int getZoomAttributeValue(XDIMEContextInternal context, 
            XDIMEAttributes attributes) throws XDIMEException {
        
    	int zoom = -1;
        String attrVal = attributes.getValue("", "zoom");
        if (attrVal != null) {
            try {
                zoom = Integer.parseInt(attrVal);
            } catch (NumberFormatException nfe) {
                throw new XDIMEException("\"zoom\" attribute of map "
                        + "element must be integer");
            }
        }
        
        return zoom;
    }
    
    /**
     * Retrieves the value of the 'query' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'query' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getQueryAttributeValue(XDIMEContextInternal context, 
            XDIMEAttributes attributes) throws XDIMEException {
        String query = attributes.getValue("","query");
        
        if (query != null) {
            query = rewriteURLWithPageURLRewriter(context, query, PageURLType.WIDGET);
        }
        
        return query;
    }
    
    
}
