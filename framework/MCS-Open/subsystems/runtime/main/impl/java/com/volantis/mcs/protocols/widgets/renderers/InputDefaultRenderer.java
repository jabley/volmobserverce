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
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.PropertyReference;
import com.volantis.mcs.protocols.widgets.attributes.InputAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;

/**
 * Renderer for input element from widgets namespace 
 */
public class InputDefaultRenderer extends WidgetDefaultRenderer {
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
            PropertyName.PARTIAL_VALUE,
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES = {};
    
    private Element inputElement;

    /**
     * Open element for widget:input and ensure unique value for id attribute
     *    
     * @throws ProtocolException 
     */    
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        require(WidgetScriptModules.BASE_BB_CONTROLS_INPUT, protocol, attributes);
        
        InputAttributes inputAttributes = (InputAttributes)attributes;
        
        // Render input element.
        DOMOutputBuffer buffer = getCurrentBuffer(protocol);
        
        inputElement = buffer.openStyledElement("input", attributes.getStyles());
        
        if (inputAttributes.getName() != null) {
            inputElement.setAttribute("name", inputAttributes.getName());
        }

        if (inputAttributes.getValue() != null) {
            inputElement.setAttribute("value", inputAttributes.getValue());
        }        

        if (inputAttributes.getType() != null) {
            inputElement.setAttribute("type", inputAttributes.getType());
        }                
        
        if (inputAttributes.getId() != null) {
            inputElement.setAttribute("id", inputAttributes.getId());
        } else {
            inputElement.setAttribute("id", protocol.getMarinerPageContext().generateUniqueFCID());            
        }           
    }

    /**
     * Close element for widget:input
     * 
     * @throws ProtocolException 
     */    
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        
        
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        currentBuffer.closeElement("input");
        
        // Get widget attributes.
        InputAttributes inputAttributes = (InputAttributes) attributes;
        
        PropertyReference propertyReference = inputAttributes.getPropertyReference();
        
        // Finally, render the JavaScript part.
        StringBuffer scriptBuffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            scriptBuffer.append("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }

        scriptBuffer.append("new Widget.Input(")
            .append(createJavaScriptString(inputElement.getAttributeValue("id")))
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
