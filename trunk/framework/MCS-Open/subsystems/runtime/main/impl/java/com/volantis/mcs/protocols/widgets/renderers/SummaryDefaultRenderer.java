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
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.gallery.GalleryDefaultModule;
import com.volantis.mcs.protocols.gallery.attributes.ItemAttributes;
import com.volantis.mcs.protocols.gallery.renderers.ItemDefaultRenderer;
import com.volantis.mcs.protocols.widgets.FoldingItemContext;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.BlockContentAttributes;
import com.volantis.mcs.protocols.widgets.attributes.SummaryAttributes;
import com.volantis.styling.Styles;
import com.volantis.styling.StatefulPseudoClasses;

/**
 * Widget renderer for XDIME2 SummaryElement suitable for HTML protocols
 */
public class SummaryDefaultRenderer extends WidgetDefaultRenderer {
    private BlockContentAttributes blockContentAttributes;

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        SummaryAttributes summaryAttributes = (SummaryAttributes) attributes;
        
        // Handle the widget:summary element enclosed within the gallery:item element.
        if (summaryAttributes.isInsideGalleryItem()) {
            renderOpenForGalleryItem(protocol, attributes);
            
            return;
        }
        
        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        FoldingItemContext foldingItemContext = 
            ((FoldingItemDefaultRenderer)getWidgetDefaultModule(protocol).getFoldingItemRenderer())
                .getCurrentContext();
        
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        
        // Render "summary" element
        Element element = openDivElement(attributes.getStyles(), currentBuffer);

        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }

        element.setAttribute("id", attributes.getId());
        
        setElementLocked(protocol, element);
        
        foldingItemContext.setSummaryElementId(attributes.getId());

        // each marker (folded,unfolded) will be rendered separately
        // because marker could be also defined for folding-item 
        // so its value is stored in folding-item context
        // if undefined for summary - value from folding-item is used (if defined)
        // if defined for summary - used no matter if defined for folding-item also

        Styles foldedMarkerStyles = getFoldedMarkerStyles(protocol, attributes);

        String foldedMarkerId = renderFoldedMarker(protocol, DEFAULT_FOLDED_MARKER, foldedMarkerStyles, foldingItemContext.getFoldedMarkerStyles());

        // the same scenario for unfolded
        Styles unfoldedMarkerStyles = getUnfoldedMarkerStyles(protocol, attributes);

        String unfoldedMarkerId = renderUnfoldedMarker(protocol, DEFAULT_UNFOLDED_MARKER, unfoldedMarkerStyles, foldingItemContext.getUnfoldedMarkerStyles());

        
        foldingItemContext.setFoldedMarkerId(foldedMarkerId);        
        foldingItemContext.setUnfoldedMarkerId(unfoldedMarkerId);
        
        // Retrieve unfolded styles for further rendering
        StylesExtractor stylesExtractor = createStylesExtractor(protocol, attributes.getStyles());
        
        stylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_UNFOLDED);
        
        String unfoldedStyles = stylesExtractor.getJavaScriptStyles();
        
        foldingItemContext.setSummaryUnfoldedStyles(unfoldedStyles);
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        SummaryAttributes summaryAttributes = (SummaryAttributes) attributes;
        
        // Handle the widget:summary element enclosed within the gallery:item element.
        if (summaryAttributes.isInsideGalleryItem()) {
            renderCloseForGalleryItem(protocol, attributes);
            
            return;
        }
         
        if (!isWidgetSupported(protocol)) {
            return;
        }

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        closeDivElement(currentBuffer);
    }
    
    // javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        SummaryAttributes summaryAttributes = (SummaryAttributes) attributes;

        if (summaryAttributes.isInsideGalleryItem()) {
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
        
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockContentAttributes)
            .renderOpen(protocol, blockContentAttributes);
    }
    
    private void renderCloseForGalleryItem(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }

        getWidgetDefaultModule(protocol).getWidgetRenderer(blockContentAttributes)
            .renderClose(protocol, blockContentAttributes);
        
        String slideId = protocol.getMarinerPageContext().generateUniqueFCID();
        
        GalleryDefaultModule galleryModule = 
            (GalleryDefaultModule) protocol.getGalleryModule();
        ItemDefaultRenderer itemRenderer = 
            (ItemDefaultRenderer) galleryModule.getElementRenderer(ItemAttributes.class);
        itemRenderer.setSummarySlideId(slideId);
        
        StringBuffer buffer = new StringBuffer();

        buffer.append("Widget.register(")
            .append(createJavaScriptString(slideId))
            .append(", new Gallery.Slide(")
            .append(createJavaScriptWidgetReference(blockContentAttributes.getId()))
            .append("))");

        addCreatedWidgetId(slideId);
        
        addUsedWidgetId(blockContentAttributes.getId());
        
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }  
    }
}
