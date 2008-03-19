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
package com.volantis.mcs.protocols.widgets.renderers;

import java.io.IOException;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.gallery.GalleryDefaultModule;
import com.volantis.mcs.protocols.gallery.attributes.ItemAttributes;
import com.volantis.mcs.protocols.gallery.renderers.ItemDefaultRenderer;
import com.volantis.mcs.protocols.widgets.FoldingItemContext;
import com.volantis.mcs.protocols.widgets.attributes.BlockContentAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DetailAttributes;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;

/**
 * Widget renderer for DetailElement suitable for HTML protocol
 */
public class DetailDefaultRenderer extends WidgetDefaultRenderer {

    private BlockContentAttributes blockContentAttributes;

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol,
    		MCSAttributes attributes) throws ProtocolException {

        // Handle the widget:detail element enclosed within the gallery:item element.
        DetailAttributes detailAttributes = (DetailAttributes) attributes;
        
        if (detailAttributes.isInsideGalleryItem()) {
            renderOpenForGalleryItem(protocol, attributes);
            
            return;
        }
        
        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour            
            return;
        }

        // Retrieve current FoldingItem context.
        // This should not be null, since the Detail element
        // can only be inside the FoldingItem element.
        FoldingItemContext foldingItemContext = 
            ((FoldingItemDefaultRenderer)getWidgetDefaultModule(protocol).getFoldingItemRenderer())
                .getCurrentContext();
        
        // Open outer <div> element.
    	Element outerElement = openDivElement(attributes.getStyles(), getCurrentBuffer(protocol));
        
        // Generate an ID for it, if it's not already there.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        outerElement.setAttribute("id", attributes.getId());
            
        // Store an ID in the FoldingItem context, so it can be
        // retrieved later, while rendering JavaScript code.
        foldingItemContext.setDetailsElementId(attributes.getId());

        // Lock an element, so it'll not be removed from the
        // DOM by any transformer/optimiser.
        setElementLocked(protocol, outerElement);
        
        // set default styles for inner div element                               
        Styles stylesInner = StylingFactory.getDefaultInstance()
        .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(), 
        DisplayKeywords.BLOCK);                                       
        
        // Open inner <div> element.             
    	Element innerElement = openDivElement(stylesInner, getCurrentBuffer(protocol));

        // Lock inner element either.
        ((DOMProtocol)protocol).setElementLocked(innerElement);
    }
    
    // Javadoc inherited    
    public void doRenderClose(VolantisProtocol protocol,
    		MCSAttributes attributes) throws ProtocolException {
    	        
        // Handle the widget:detail element enclosed within the gallery:item element.
        DetailAttributes detailAttributes = (DetailAttributes) attributes;
        
        if (detailAttributes.isInsideGalleryItem()) {
            renderCloseForGalleryItem(protocol, attributes);
            
            return;
        }
        
        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour            
            return;
        }

        // Close all outstanding <div> elements.
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);		
    	closeDivElement(currentBuffer);
    	closeDivElement(currentBuffer);
    }

    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        DetailAttributes detailAttributes = (DetailAttributes) attributes;

        if (detailAttributes.isInsideGalleryItem()) {
            // The strategy for ItemGallery is not to render contents on fallback
            return isWidgetSupported(protocol);
        } else { 
            // in other cases use the default strategy            
            return super.shouldRenderContents(protocol, attributes);
        }
    }

    private void renderOpenForGalleryItem(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }

        blockContentAttributes = new BlockContentAttributes();

        blockContentAttributes.copy(attributes);
        
        if (blockContentAttributes.getId() == null) {
            blockContentAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());        
        }

        getWidgetDefaultModule(protocol).getWidgetRenderer(blockContentAttributes)
            .renderOpen(protocol, blockContentAttributes);
    }
    
    private void renderCloseForGalleryItem(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }

        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockContentAttributes)
            .renderClose(protocol, blockContentAttributes);
        
        String slideId = protocol.getMarinerPageContext().generateUniqueFCID();
        
        GalleryDefaultModule galleryModule = 
            (GalleryDefaultModule) protocol.getGalleryModule();
        ItemDefaultRenderer itemRenderer = 
            (ItemDefaultRenderer) galleryModule.getElementRenderer(ItemAttributes.class);
        itemRenderer.setDetailSlideId(slideId);
        
        DetailAttributes detailAttributes = (DetailAttributes) attributes;
        
        StringBuffer buffer = new StringBuffer();

        StylesExtractor detailStylesExtractor = 
            createStylesExtractor(protocol, detailAttributes.getStyles());
        
        double detailDuration = detailStylesExtractor.getDuration();

        buffer.append("Widget.register(")
            .append(createJavaScriptString(slideId))
            .append(", new Gallery.Slide(")
            .append(createJavaScriptWidgetReference(blockContentAttributes.getId()))
            .append(", {duration:")
            .append(Double.isNaN(detailDuration) ? "5" : Double.toString(detailDuration))
            .append("}))");

        addCreatedWidgetId(slideId);
        
        addUsedWidgetId(blockContentAttributes.getId());
        
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        } 
    }
}
