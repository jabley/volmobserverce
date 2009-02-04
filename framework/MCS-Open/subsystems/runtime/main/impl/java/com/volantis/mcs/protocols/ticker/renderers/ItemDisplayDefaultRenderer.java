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

package com.volantis.mcs.protocols.ticker.renderers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.attributes.ItemDisplayAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemPropertyAttributes;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.DismissAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PopupAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WidgetRenderer;

/**
 * Renderer for ItemDisplay element 
 */
public class ItemDisplayDefaultRenderer extends ElementDefaultRenderer {
    /**
     * Attributes for rendered popup widget.
     */
    private PopupAttributes popupAttributes;
    
    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require script module
        require(WIDGET_TICKER, protocol, attributes);

        // Generate an ID for this widget, if not already generated.
        // It'll be used to register this widget.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }

        // Render open for Popup widget, which will be used internally 
        // to implement behaviour of this ItemDisplay widget.
        // Generate a unique ID for the popup, so it does not clash
        // with the ID of this widget.
        popupAttributes = new PopupAttributes();
        
        popupAttributes.copy(attributes);
        
        popupAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        // Eventually, render Popup opening.
        getWidgetDefaultModule(protocol).getPopupRenderer().renderOpen(protocol, popupAttributes);
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        ItemDisplayAttributes itemDisplayAttributes = (ItemDisplayAttributes) attributes;
         
        // Render popup closure.
        getWidgetDefaultModule(protocol).getPopupRenderer().renderClose(protocol, popupAttributes);

        // Prepare Javascript content.
        StringWriter scriptWriter = new StringWriter();
        
        // Finally, render the JavaScript part.
        if (itemDisplayAttributes.getId() != null) {
            scriptWriter.write(createJavaScriptWidgetRegistrationOpening(itemDisplayAttributes.getId()));
            
            addCreatedWidgetId(itemDisplayAttributes.getId());
        }
        
        scriptWriter.write("Ticker.createItemDisplay({");      
        
        scriptWriter.write("popupId:" + createJavaScriptString(popupAttributes.getId()));
        
        addUsedWidgetId(popupAttributes.getId());
        
        StringWriter propertyIdWriter = new StringWriter();
        
        Iterator itemPropertyIterator = itemDisplayAttributes.getItemProperties().iterator();
        
        boolean firstItemProperty = true;
        
        while (itemPropertyIterator.hasNext()) {
            ItemPropertyAttributes itemPropertyAttributes = 
                    (ItemPropertyAttributes) itemPropertyIterator.next();
            
            if (firstItemProperty) {
                firstItemProperty = false;
            } else {
                propertyIdWriter.write(",");
            }
            
            propertyIdWriter.write(itemPropertyAttributes.getId());
        }
        
        scriptWriter.write(",itemPropertyId:" + createJavaScriptString(propertyIdWriter.toString()));
        
        scriptWriter.write("})");

        if (itemDisplayAttributes.getId() != null) {
            scriptWriter.write(createJavaScriptWidgetRegistrationClosure());                  
        }
        
        // Write JavaScript content to DOM.
        writeJavaScript(scriptWriter.toString());
    }   
    
    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
                
        // First, check if the content of this widget should be rendered.
        boolean shouldRenderContent = isWidgetSupported(protocol);
        
        // If it should, check if the content for the popup should be rendered.
        if (shouldRenderContent) {
            try {
                WidgetModule module = getWidgetDefaultModule(protocol);
                
                if (module != null) {
                    WidgetRenderer popupRenderer = module.getPopupRenderer();
                    
                    if (popupRenderer != null) {
                        shouldRenderContent = popupRenderer.shouldRenderContents(protocol, popupAttributes);
                    }
                }
            } catch (ProtocolException e) {
                return false;
            }
        }
        
        return shouldRenderContent;
    }
}
