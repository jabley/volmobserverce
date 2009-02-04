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
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.ActionReference;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.ButtonAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.styling.StatefulPseudoClasses;

/**
 * Renderer for ActionButtonElement 
 */
public class ButtonDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.PRESS,
            ActionName.ENABLE,
            ActionName.DISABLE,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.IS_ENABLED,
        };

    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {
            EventName.PRESSED,
        };


    /**
     * The ButtonAttributes used.
     */
    private SpanAttributes spanAttributes;
    
    private String elementId;
    
    // javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        require(WidgetScriptModules.BASE_BB_BUTTON, protocol, attributes);
        
        elementId = attributes.getId();
        
        if (elementId == null) {
            elementId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        
        if (usesNativeRendering(protocol, attributes)) {
            DOMOutputBuffer domBuffer = getCurrentBuffer(protocol);            
            
            Element element = domBuffer.openStyledElement("button", attributes.getStyles());
 
            element.setAttribute("id", elementId);
            // buttons alre always rendered as type="button"
            // default value is submit which leads to unexpected behaviour when button is 
            // rendered in form (like in wizard)
            element.setAttribute("type","button"); 
        } else {
            spanAttributes = new SpanAttributes();

            spanAttributes.copy(attributes);

            if (spanAttributes.getId() == null) {
                spanAttributes.setId(elementId);
            }

            protocol.writeOpenSpan(spanAttributes);
        }
    }

    // javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        
        
        if (usesNativeRendering(protocol, attributes)) {
            DOMOutputBuffer domBuffer = getCurrentBuffer(protocol);

            domBuffer.closeElement("button");            
        } else {
            protocol.writeCloseSpan(spanAttributes);
        }

        // Render the action button controller.
        ButtonAttributes actionButtonAttributes = (ButtonAttributes) attributes;
        
        ActionReference actionReference = actionButtonAttributes.getActionReference();
        
        // Get disabled styles.
        StylesExtractor stylesExtractor = createStylesExtractor(protocol, attributes.getStyles());

        stylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_DISABLED);
        
        StringBuffer scriptBuffer = new StringBuffer();
        
        // Finally, render the JavaScript part.
        if (attributes.getId() != null) {
            scriptBuffer.append("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }

        scriptBuffer.append("new Widget.Button(")
            .append(createJavaScriptString(elementId))
            .append(",{")
            .append("disabledStyle:" + stylesExtractor.getJavaScriptStyles());
        
        if (actionReference != null) {
            scriptBuffer.append(",action:")
                .append(createJavaScriptExpression(actionReference));
            
            addUsedWidgetId(actionReference.getWidgetId());
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
    
    /**
     * Returns true, if button should be rendered using native device styling, false otherwise.
     * 
     * @param protocol The protocol used.
     * @param attributes The button attributes.
     * @return 
     */
    private boolean usesNativeRendering(VolantisProtocol protocol, MCSAttributes attributes) {
        return attributes.getStyles().getPropertyValues()
            .getComputedValue(StylePropertyDetails.MCS_BUTTON_STYLE) == StyleKeywords.NATIVE;
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

    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        return isWidgetSupported(protocol);
    }
}
