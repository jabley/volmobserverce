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

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.ActionReference;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.ButtonScriptHandlerAttributes;
import com.volantis.styling.StatefulPseudoClasses;

public class ButtonScriptHandlerRenderer extends WidgetDefaultRenderer {

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        new ActionName[] {
            ActionName.PRESS,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        new PropertyName[] {
            PropertyName.IS_ENABLED,
        };
    
    
    // javadoc inherited
    protected void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireStandardLibraries(protocol);

    }

    // javadoc inherited
    protected void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        if(!isWidgetSupported(protocol)) {
            return;
        }      
        
        
        // Render only javascript responsible for handling button 
        ButtonScriptHandlerAttributes actionButtonAttributes = 
            (ButtonScriptHandlerAttributes) attributes;
        
        ActionReference actionReference = actionButtonAttributes.getActionReference();
        
        // Get disabled styles.
        StylesExtractor stylesExtractor = 
            createStylesExtractor(protocol, attributes.getStyles());

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
            .append(createJavaScriptString(actionButtonAttributes.getId()))
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
