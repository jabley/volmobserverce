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

package com.volantis.mcs.protocols.gallery.renderers;

import java.io.IOException;
import java.io.StringWriter;

import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.gallery.attributes.SlideshowAttributes;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.renderers.StylesExtractor;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.StyleKeywords;

/**
 * Renderer for SlideshowElement 
 */
public class SlideshowDefaultRenderer extends ElementDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        new ActionName[] {
            ActionName.NEXT,
            ActionName.PREVIOUS,
            ActionName.PLAY,
            ActionName.STOP,
            ActionName.PAUSE,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        new PropertyName[] {
            PropertyName.ITEM_NUMBER,
            PropertyName.ITEMS_COUNT,
        };

    /**
     * Attributes of the rendered div element of this element.
     */
    private DivAttributes slideshowDivAttributes;
    
    private StringWriter scriptWriter;
    
    private String slideshowId;
    
    private boolean isRendering = false;
    
    private String itemDisplayId;
    
    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireStandardLibraries(protocol);
        
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/vfc-gallery.mscr",protocol);
        
        // Generate an ID for gallery widget, if not specified.
        slideshowId = attributes.getId();
        
        if (slideshowId == null) {
            slideshowId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        
        slideshowDivAttributes = new DivAttributes();
        
        slideshowDivAttributes.copy(attributes);

        if (slideshowDivAttributes.getId() == null) {
            slideshowDivAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        protocol.writeOpenDiv(slideshowDivAttributes);

        // Prepare Javascript content for Slideshow widget.
        scriptWriter = new StringWriter();
        
        // Finally, render the JavaScript part.
        scriptWriter.write("Widget.register(" + createJavaScriptString(slideshowId) + ",");

        addCreatedWidgetId(slideshowId);
        
        scriptWriter.write("Gallery.createSlideshow({");
        
        scriptWriter.write("display:");
        
        itemDisplayId = null;
        
        isRendering = true;
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        SlideshowAttributes slideshowAttributes = (SlideshowAttributes) attributes;

        isRendering = false;
        
        protocol.writeCloseDiv(slideshowDivAttributes);

        if (itemDisplayId == null) {
            scriptWriter.write("null");
        } else {
            scriptWriter.write(createJavaScriptString(itemDisplayId));

            addUsedWidgetId(itemDisplayId);
            
            itemDisplayId = null;
        }
        
        
        // Render reference to gallery items
        String itemsId = slideshowAttributes.getItems();
        
        if (itemsId == null) {
            itemsId = protocol.getMarinerPageContext().generateFCID("GALLERY_ITEMS");
        }
        
        scriptWriter.write(",items:" + createJavaScriptString(itemsId));
            
        addUsedWidgetId(itemsId);

        // Render the autoPlay option. 
        // The default value for autoPlay option in JavaScript is true,
        // so there's no need to render anything if case the value of the 
        // style property is 'yes'.
        if (slideshowAttributes.getStyles().getPropertyValues().getComputedValue(StylePropertyDetails.MCS_AUTO_PLAY) == StyleKeywords.NO) {
            scriptWriter.write(",autoPlay:false");
        }
        
        // Extract style values and render them into JavaScript
        StylesExtractor stylesExtractor = createStylesExtractor(protocol, slideshowAttributes.getStyles());
        
        scriptWriter.write(",order:" + createJavaScriptString(stylesExtractor.getItemsOrder()));
        
        int repetitions = stylesExtractor.getRepetitions();      
        scriptWriter.write(",repetitions:" + ((repetitions == Integer.MAX_VALUE) 
                ? "'infinite'" : Integer.toString(repetitions)));  
          
        scriptWriter.write("}))");
        
        // Write JavaScript content to DOM.
        try {
            getJavaScriptWriter().write(scriptWriter.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        
        scriptWriter = null;
    }    
    
    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        // Render gallery contentonly if widget is supported.
        return isWidgetSupported(protocol);
    }
    
    public void renderItemDisplay(String id) {
        if (itemDisplayId == null) {
            itemDisplayId = id;
        }
    }
    
    public boolean isRendering() {
        return isRendering;
    }
    
    /**
     * @inheritDoc
     */
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }

    /**
     * @inheritDoc
     */
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }
}
