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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.DeckAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.BlockContainerAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptArrayAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Deck widget suitable for HTML protocols.
 */
public class DeckDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.NEXT_PAGE,
            ActionName.PREVIOUS_PAGE,
            ActionName.FIRST_PAGE,
            ActionName.LAST_PAGE,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.CURRENT_PAGE_NUMBER,
            PropertyName.DISPLAYED_PAGE_NUMBER,
            PropertyName.PAGES_COUNT,
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {};

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(DeckDefaultRenderer.class);

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Inform of required JavaScript libraries.
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/vfc-deck.mscr", protocol);

        JavaScriptArrayAttributes listAttributes = new JavaScriptArrayAttributes();
        
        listAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        renderWidgetOpen(protocol, listAttributes);
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        JavaScriptArrayAttributes listAttributes = (JavaScriptArrayAttributes) getCurrentAttributes(protocol);
        
        renderWidgetClose(protocol, listAttributes);
        
        BlockContainerAttributes blockContainerAttributes = new BlockContainerAttributes();
        
        blockContainerAttributes.copy(attributes);
        
        blockContainerAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        renderWidgetOpen(protocol, blockContainerAttributes);
        
        renderWidgetClose(protocol, blockContainerAttributes);
        
        DeckAttributes deckAttributes = (DeckAttributes) attributes;

        // Prepare Javascript content.
        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId()));

            addCreatedWidgetId(attributes.getId());
        }
        
        buffer.append("new Widget.Deck(")
            .append(createJavaScriptWidgetReference(blockContainerAttributes.getId()))
            .append(",{");
        
        // Render display mode.
        buffer.append("mode:")
            .append(createJavaScriptString(deckAttributes.getStyles()
                    .getPropertyValues().getComputedValue(StylePropertyDetails.MCS_DECK_MODE)
                    .getStandardCSS()));

        // Render deck pages: loaded or embeeded in static way
        LoadAttributes loadAttributes = deckAttributes.getLoadAttributes();
        
        if (loadAttributes != null) {
            String when = loadAttributes.getWhen();
            if (when == null) when = "onload";
            
            buffer.append(",load: new Widget.DeckLoad({src:")
                .append(createJavaScriptString(loadAttributes.getSrc()))
                .append(",when:")
                .append(createJavaScriptString(when))
                .append("})");                        
        } else {
            buffer.append(",pages:")
                .append(createJavaScriptWidgetReference(listAttributes.getId()));
        }
        
        buffer.append("})");
        
        addUsedWidgetId(blockContainerAttributes.getId());

        addUsedWidgetId(listAttributes.getId());
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        writeJavaScript(buffer.toString());
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
