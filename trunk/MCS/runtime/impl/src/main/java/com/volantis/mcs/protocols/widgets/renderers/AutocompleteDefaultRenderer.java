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
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.AutocompleteAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;

/**
 * Autocomplete Renderer Include renderer methods for widget:autocomplete and
 * for LI elements from response:autocomplete
 */
public class AutocompleteDefaultRenderer extends WidgetDefaultRenderer
        implements AutocompleteRenderer {

//    private AnchorAttributes anchorAttr = null;
    
    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/controls.mscr", protocol);
        requireLibrary("/vfc-autocomplete.mscr", protocol);

        // If autocomplete ID was not specified, generate it automatically.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }
                
        Element divElement = openDivElement(attributes.getStyles(),
                getCurrentBuffer(protocol));        
        
        divElement.setAttribute("id", attributes.getId());
        divElement.setAttribute("style", "display:none");
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        closeDivElement(currentBuffer);

        StringBuffer textToScript;

        StylesExtractor appearStyles = new StylesExtractor(attributes
                .getStyles(), false);
        StylesExtractor disappearStyles = new StylesExtractor(attributes
                .getStyles(), true);

        String inputId = ((AutocompleteAttributes) attributes).getInputId();

        textToScript = new StringBuffer("Widget.addStartupItem(function(){Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(", new Widget.Autocompleter(")
                .append(createJavaScriptString(inputId))
                .append(",")
                .append(createJavaScriptString(attributes.getId()))
                .append(",{")
                .append("lagTime: ")
                .append(appearStyles.getDelay())
                .append(", itemLimit: ")
                .append(appearStyles.getItemLimit())
                .append(", url: ")
                .append(createJavaScriptString(((AutocompleteAttributes) attributes).getSrc()))
                .append(", ").append(getAppearableOptions(appearStyles))
                .append(", ").append(getDisappearableOptions(disappearStyles))
                .append("}));})");

        try {
            writeScriptElement(currentBuffer, textToScript.toString());
        } catch (IOException e) {
            throw new ProtocolException();
        }
    }

    // Javadoc inherited.
    public void renderLiOpen(VolantisProtocol protocol,
            ListItemAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        // set unique attribute id if is not set
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }
        Styles liStyles = attributes.getStyles();
                
        // default list-type-style set to none value
        if (liStyles.getPropertyValues().getSpecifiedValue(
                StylePropertyDetails.LIST_STYLE_TYPE) == null) {
            liStyles.getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.LIST_STYLE_TYPE,
                    ListStyleTypeKeywords.NONE);
        }
        // default white-space set to nowrap value
        if (liStyles.getPropertyValues().getSpecifiedValue(
                StylePropertyDetails.WHITE_SPACE) == null) {
            liStyles.getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.WHITE_SPACE,
                    WhiteSpaceKeywords.NOWRAP);
        }
        // write open LI tag
        protocol.writeOpenListItem(attributes);       
        
    }

    // Javadoc inherited.
    public void renderLiClose(VolantisProtocol protocol,
            ListItemAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        // close LI element
        protocol.writeCloseListItem(attributes);      
        
        Styles liStyles = attributes.getStyles();
        Styles activeStyles = liStyles
                .findNestedStyles(StatefulPseudoClasses.ACTIVE);

        // for each styles in active pseudo class will be generated javascript
        // method registerActiveStyles in order to register styles in javascript
        // objects.
        // It is doing in such way because it is not possible pasing pseudo
        // elements or class in response:response body

        if (activeStyles != null) {
            DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
            ScriptAttributes sa = new ScriptAttributes();

            sa.setLanguage("JavaScript");
            sa.setType("text/javascript");
            openScriptElement(sa, currentBuffer);

            StylesExtractor extractor = createStylesExtractor(protocol,
                    activeStyles);
            
            String textToScript = "registerActiveStyle('" + attributes.getId()
                    + "', ";
            if (activeStyles != null) {
                textToScript += extractor.getJavaScriptStyles();
            }
            textToScript += ")";

            try {
                writeJavaScript(currentBuffer, textToScript);
            } catch (IOException e) {
                throw new ProtocolException();
            }
            closeScriptElement(currentBuffer);
        }
    }
}
