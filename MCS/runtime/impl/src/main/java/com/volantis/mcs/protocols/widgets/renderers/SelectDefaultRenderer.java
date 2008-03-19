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

import java.io.IOException;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.PropertyReference;
import com.volantis.mcs.protocols.widgets.attributes.SelectAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;

/**
 * Renderer for select element from widgets namespace 
 */
public class SelectDefaultRenderer extends WidgetDefaultRenderer {

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES = {};
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.VALUE,
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES = {};
    
    private Element selectElement;    
    
    /**
     * Open element for widget:select and ensure unique value for id attribute
     *    
     * @throws ProtocolException 
     */        
    protected void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Required libraries
        requireStandardLibraries(protocol);

        SelectAttributes selectAttributes = (SelectAttributes)attributes;
        
        // Render input element.
        DOMOutputBuffer buffer = getCurrentBuffer(protocol);
        
        selectElement = buffer.openStyledElement("select", attributes.getStyles());
        
        if (selectAttributes.getId() != null) {
            selectElement.setAttribute("id", selectAttributes.getId());
        } else {
            selectElement.setAttribute("id", protocol.getMarinerPageContext().generateUniqueFCID());            
        }        

        String mode = selectAttributes.getMode();
        if (mode != null && mode.equals("multiple")) {
            selectElement.setAttribute("multiple", "multiple");
        }
        
        // set default size=1
        Styles styles = attributes.getStyles();
        StyleValue styleValue = styles.getPropertyValues().getSpecifiedValue(
                StylePropertyDetails.MCS_ROWS);
        if (styleValue != null) {
            selectElement.setAttribute("size", styleValue
                    .getStandardCSS());
        } else {
            selectElement.setAttribute("size", "1");
        }
    }

    /**
     * Close element for widget:select
     * 
     * @throws ProtocolException 
     */    
    protected void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        
        if(!isWidgetSupported(protocol)) {
            return;
        }        
        
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        currentBuffer.closeElement("select");
        
        // Get widget attributes.
        SelectAttributes selectAttributes = (SelectAttributes) attributes;
        
        PropertyReference propertyReference = selectAttributes.getPropertyReference();
        
        // Finally, render the JavaScript part.
        StringBuffer scriptBuffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            scriptBuffer.append("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }

        scriptBuffer.append("new Widget.Select(")
            .append(createJavaScriptString(selectElement.getAttributeValue("id")))
            .append(",{");
        
        if (propertyReference != null) {
            scriptBuffer.append("property:")
                .append(createJavaScriptExpression(propertyReference));
            
            addUsedWidgetId(propertyReference.getWidgetId());
        }
        
        scriptBuffer.append("})");
        
        if (attributes.getId() != null) {
            scriptBuffer.append(")");
        }
        
        // Write JavaScript content to DOM.
        try {
            getJavaScriptWriter().write(scriptBuffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }
}
