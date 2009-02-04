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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.BlockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.BlockContentAttributes;
import com.volantis.mcs.protocols.widgets.internal.renderers.EffectBlockDefaultRenderer;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Container widget suitable for HTML protocols.
 */
public class BlockContentDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {};
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {};
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {};
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(EffectBlockDefaultRenderer.class);

    private com.volantis.mcs.protocols.widgets.internal.attributes.BlockContentAttributes blockContentAttributes;

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        BlockContentAttributes myAttributes = (BlockContentAttributes) attributes;

        require(WidgetScriptModules.BASE_BB_CONTENT, protocol, attributes);

        // Open internal BlockContent element, which would contain the content
        // for this BlockContent widget.
        blockContentAttributes = new com.volantis.mcs.protocols.widgets.internal.attributes.BlockContentAttributes();
        
        blockContentAttributes.copy(attributes);
        
        blockContentAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        if (myAttributes.isForResponse()) {
            if (myAttributes.getId() == null) {
                myAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
            }
        }
        
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockContentAttributes)
            .renderOpen(protocol, blockContentAttributes);
        
        openingBlockContent(protocol);
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        String parentBlockId = closingBlockContent(protocol);

        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockContentAttributes)
            .renderClose(protocol, blockContentAttributes);
        
        // Render JavaScript
        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }
        
        StylesExtractor baseStylesExtractor = createStylesExtractor(protocol, attributes.getStyles());
        StylesExtractor concealedStylesExtractor = createStylesExtractor(protocol, attributes.getStyles());
        concealedStylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_CONCEALED);
        
        buffer.append("new Widget.BlockContent(")
            .append(createJavaScriptWidgetReference(blockContentAttributes.getId()))
            .append(",{")
            .append(getAppearableOptions(baseStylesExtractor))
            .append(",")
            .append(getDisappearableOptions(concealedStylesExtractor));
        
        if (parentBlockId != null) {
            buffer.append(",parentBlock:")
                .append(createJavaScriptWidgetReference(parentBlockId));

            addUsedWidgetId(parentBlockId);
        }
    
        buffer.append("})");
        
        addUsedWidgetId(blockContentAttributes.getId());
        
        // Render closing parentheses for Widget.Register invokation
        if (attributes.getId() != null) {
            buffer.append(")");                  
        }

        // if widget:block-content inside response:body element
        BlockContentAttributes myAttributes = (BlockContentAttributes) attributes;
        
        if(myAttributes.isForResponse()) {
            buffer.append("; this.blockContent = Widget.getInstance('" + myAttributes.getId() + "')");                    
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
    
    protected BlockDefaultRenderer getBlockDefaultRenderer(VolantisProtocol protocol) throws ProtocolException {
        return (BlockDefaultRenderer) getWidgetDefaultModule(protocol)
             .getWidgetRenderer(BlockAttributes.class);
    }
    
    private void openingBlockContent(VolantisProtocol protocol) throws ProtocolException {
        getBlockDefaultRenderer(protocol).openingBlockContent();
    }

    private String closingBlockContent(VolantisProtocol protocol) throws ProtocolException {
        return getBlockDefaultRenderer(protocol).closingBlockContent();
    }

    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        return isWidgetSupported(protocol);
    }
}
