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

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.attributes.ButtonAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DismissAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;

/**
 * Renderer for dismiss button 
 */
public class DismissRenderer extends WidgetDefaultRenderer {

    /**
     * Attributes for the internally rendered button 
     */
    private ButtonAttributes buttonAttributes;

    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES = {
        EventName.PRESSED,
    };

    /**
     * Open element for widget:dismiss
     * @throws ProtocolException 
     */    
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // If dismiss id was not specified, generate it automatically.
        if(null == attributes.getId()) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }        

        require(WidgetScriptModules.BASE_BB_DISMISS, protocol, attributes);

        // Render internal button
        buttonAttributes = new ButtonAttributes();       
        buttonAttributes.copy(attributes);
        buttonAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        renderWidgetOpen(protocol, buttonAttributes);
    }

    /**
     * Close element for widget:dismiss    
     * @throws ProtocolException 
     */    
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        
                
        // Render internal button
        renderWidgetClose(protocol, buttonAttributes);
        addUsedWidgetId(buttonAttributes.getId());

        // We need to access our attributes
        DismissAttributes dismissAttrs = (DismissAttributes)attributes;
        
        // If this happens, it is our bug, because all Dismissable widgets
        // must ensure that they have an id
        if (null == dismissAttrs.getDismissableId()) {
            throw new IllegalStateException("Id of widgets to dismiss not set");
        }
        addUsedWidgetId(dismissAttrs.getDismissableId());
        
        // Create the widget
        StringBuffer textBuffer 
            = new StringBuffer(createJavaScriptWidgetRegistrationOpening(attributes.getId()))
            .append("new Widget.Dismiss(")
            .append(createJavaScriptString(dismissAttrs.getId()))
            .append(", ").append(createJavaScriptString(dismissAttrs.getType()))
            .append(", ").append(createJavaScriptWidgetReference(buttonAttributes.getId()))
            .append(", ").append(createJavaScriptWidgetReference(dismissAttrs.getDismissableId()))
            .append(")")
            .append(createJavaScriptWidgetRegistrationClosure());            
                
        writeJavaScript(textBuffer.toString());        
        addCreatedWidgetId(dismissAttrs.getId());        
    }

    // Javadoc inherited
    protected EventName[] getSupportedEventNames() {
        return SUPPORTED_EVENT_NAMES;
    }
}
