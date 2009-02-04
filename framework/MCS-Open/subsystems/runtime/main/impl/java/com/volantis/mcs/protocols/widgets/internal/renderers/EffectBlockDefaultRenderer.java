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

package com.volantis.mcs.protocols.widgets.internal.renderers;

import java.io.IOException;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.renderers.StylesExtractor;
import com.volantis.mcs.protocols.widgets.renderers.WidgetDefaultRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Container widget suitable for HTML protocols.
 */
public class EffectBlockDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.SHOW,
            ActionName.HIDE,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.STATUS,
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {
            EventName.HIDING,
            EventName.HIDDEN,
            EventName.SHOWING,
            EventName.SHOWN,
        };
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(EffectBlockDefaultRenderer.class);

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        require(WidgetScriptModules.BASE_BB_EFFECT, protocol, attributes);        
        
        Element element = openDivElement(protocol, attributes, true);

        if (attributes.getStyles()
                .getPropertyValues()
                .getComputedValue(StylePropertyDetails.DISPLAY) == DisplayKeywords.NONE) {
            element.setAttribute("style", "display:none");
        }
        
        openDivElement(protocol);
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        closeDivElement(protocol);
        
        Element outerDiv = closeDivElement(protocol);
        
        // Prepare Javascript Container.
        StringBuffer buffer = new StringBuffer();
        
        // Finally, render the JavaScript part.
        if (attributes.getId() != null) {
            buffer.append("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }
        
        StylesExtractor baseStylesExtractor = createStylesExtractor(protocol, attributes.getStyles());
        StylesExtractor concealedStylesExtractor = createStylesExtractor(protocol, attributes.getStyles());
        concealedStylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_CONCEALED);

        buffer.append("new Widget.Internal.EffectBlock(")
            .append(createJavaScriptString(outerDiv.getAttributeValue("id")))
            .append(",{")
            .append(getAppearableOptions(baseStylesExtractor))
            .append(",")
            .append(getDisappearableOptions(concealedStylesExtractor));
        
        buffer.append("})");
        
        // Render closing parentheses for Widget.Register invokation
        if (attributes.getId() != null) {
            buffer.append(")");                  
        }
        
        // Write JavaScript Container to DOM.
        try {
            getJavaScriptWriter().write(buffer.toString());
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
