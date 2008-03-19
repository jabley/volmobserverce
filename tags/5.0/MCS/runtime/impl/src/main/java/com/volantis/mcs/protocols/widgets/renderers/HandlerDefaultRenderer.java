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

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionReference;
import com.volantis.mcs.protocols.widgets.EventReference;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.HandlerAttributes;

/**
 * Renderer for PropertyHandlerElement 
 */
public class HandlerDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        new PropertyName[] {
            PropertyName.IS_ENABLED,
        };
    
    // javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        
    }

    // javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        
        
        // Render the Property Handler controller.
        HandlerAttributes handlerAttributes = (HandlerAttributes) attributes;
        
        EventReference eventReference = handlerAttributes.getEventReference();
        
        ActionReference actionReference = handlerAttributes.getActionReference();
        
        StringBuffer scriptBuffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            scriptBuffer.append("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }

        scriptBuffer.append("new Widget.Handler(")
            .append(createJavaScriptExpression(eventReference))
            .append(",")
            .append(createJavaScriptExpression(actionReference));
        
        if (!handlerAttributes.isEnabled()) {
            scriptBuffer.append(",{isEnabled:false}");
        }
            
        scriptBuffer.append(")");
        
        if (attributes.getId() != null) {
            scriptBuffer.append(")");
        }

        addUsedWidgetId(eventReference.getWidgetId());
        addUsedWidgetId(actionReference.getWidgetId());
        
        // Write JavaScript content to DOM.
        try {
            getJavaScriptWriter().write(scriptBuffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }
    
    // Javadoc inherited
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }
}
