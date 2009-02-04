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
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.PropertyReference;
import com.volantis.mcs.protocols.widgets.attributes.DisplayAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;

/**
 * Renderer for PropertyDisplayElement 
 */
public class DisplayDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.CLEAR_CONTENT,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.CONTENT,
        };

    /**
     * The SpanAttributes used.
     */
    private SpanAttributes spanAttributes;
    
    // javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        require(WidgetScriptModules.BASE_BB_DISPLAY, protocol, attributes);

        spanAttributes = new SpanAttributes();
        
        spanAttributes.copy(attributes);
        
        if (spanAttributes.getId() == null) {
            spanAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }

        protocol.writeOpenSpan(spanAttributes);
    }

    // javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        
        
        protocol.writeCloseSpan(spanAttributes);

        // Render the Property Display controller.
        DisplayAttributes propertyDisplayAttributes = (DisplayAttributes) attributes;
        
        PropertyReference propertyReference = propertyDisplayAttributes.getPropertyReference();
        
        StringBuffer scriptBuffer = new StringBuffer();
        
        // Finally, render the JavaScript part.
        if (attributes.getId() != null) {
            scriptBuffer.append("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }

        scriptBuffer.append("new Widget.Display(")
            .append(createJavaScriptString(spanAttributes.getId()))
            .append(",{");
        
        if (propertyReference != null) {
            scriptBuffer.append("property:")
                .append(createJavaScriptExpression(propertyReference));
            
            addUsedWidgetId(propertyReference.getWidgetId());
        }
        
        String content = propertyDisplayAttributes.getContent();
        
        if (content != null){
            scriptBuffer.append("content:")
            .append(createJavaScriptString(content));
        
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
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        return isWidgetSupported(protocol);
    }
}
