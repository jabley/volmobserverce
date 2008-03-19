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
import com.volantis.mcs.protocols.widgets.internal.attributes.BlockContentAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.EffectBlockAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for DeckPage widget suitable for HTML protocols.
 */
public class DeckPageDefaultRenderer extends WidgetDefaultRenderer {
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

        // Render internal BlockContent widget which will contain a content of
        // the page.
        BlockContentAttributes blockContentAttributes = new BlockContentAttributes();
        
        blockContentAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        renderWidgetOpen(protocol, blockContentAttributes);
        
        // Render internal EffectBlock widget, which would be used to show/hide
        // content of a page. Make the page initially invisible by setting
        // display:none style.
        EffectBlockAttributes effectBlockAttributes = new EffectBlockAttributes();
        
        effectBlockAttributes.copy(attributes);
        
        effectBlockAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        effectBlockAttributes.getStyles().getPropertyValues()
            .setComputedAndSpecifiedValue(StylePropertyDetails.DISPLAY, DisplayKeywords.NONE);
        
        renderWidgetOpen(protocol, effectBlockAttributes);
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        // Render closure of the EffectBlock widget.
        EffectBlockAttributes effectBlockAttributes = 
            (EffectBlockAttributes) getCurrentAttributes(protocol);
        
        renderWidgetClose(protocol, effectBlockAttributes);

        // Render closure of the BlockContent widget.
        BlockContentAttributes blockContentAttributes = 
            (BlockContentAttributes) getCurrentAttributes(protocol);

        renderWidgetClose(protocol, blockContentAttributes);

        // Prepare Javascript content to render.
        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationOpening(attributes.getId()));

            addCreatedWidgetId(attributes.getId());
        }
        
        buffer.append("new Widget.DeckPage(")
            .append(createJavaScriptWidgetReference(blockContentAttributes.getId()))
            .append(", ")
            .append(createJavaScriptString(effectBlockAttributes.getId()))
            .append(",{})");
        
        addUsedWidgetId(blockContentAttributes.getId());
        
        if (attributes.getId() != null) {
            buffer.append(createJavaScriptWidgetRegistrationClosure());
        }
        
        // Render the JavaScript content.
        writeJavaScript(buffer.toString());
    }
}
