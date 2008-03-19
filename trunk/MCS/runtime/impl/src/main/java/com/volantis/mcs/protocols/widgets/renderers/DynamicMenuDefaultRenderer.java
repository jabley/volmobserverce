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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.widgets.renderers;

import java.io.IOException;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.NavigationListAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.OverflowKeywords;
import com.volantis.mcs.themes.properties.PositionKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.mcs.themes.values.StyleColorNames;


/**
 * Renderer for dynamic menu widget
 */

public class DynamicMenuDefaultRenderer extends WidgetDefaultRenderer
    implements DynamicMenuWidgetRenderer {

    private Styles styleTopMenu;

    /**
     * level - actual level in dynamic menu for render element
     */
    private int level = 0;

    /**
     * Render for Navigation List element on open
     */
    public void renderNlOpen(VolantisProtocol protocol, NavigationListAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/vfc-dynamicmenu.mscr", protocol);

        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        Styles styles = attributes.getStyles();

        level++;

        Element divElement = null;

        if(level == 1) {
            Styles positionStyles = StylingFactory.getDefaultInstance()
                .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(),
                DisplayKeywords.BLOCK);

            // save only positioning style property, others like borders, paddings, etc. will be saved in innerDIV element
            if(styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.POSITION) != null) {
                positionStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.POSITION, styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.POSITION));
                styles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.POSITION, PositionKeywords.ABSOLUTE);
            }
            if(styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.LEFT) != null) {
                positionStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.LEFT, styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.LEFT));
                styles.getPropertyValues().clearPropertyValue(StylePropertyDetails.LEFT);
            }
            if(styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.TOP) != null) {
                positionStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.TOP, styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.TOP));
                styles.getPropertyValues().clearPropertyValue(StylePropertyDetails.TOP);
            }
            if(styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.BOTTOM) != null) {
                positionStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.BOTTOM, styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.BOTTOM));
                styles.getPropertyValues().clearPropertyValue(StylePropertyDetails.BOTTOM);
            }
            if(styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.RIGHT) != null) {
                positionStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.RIGHT, styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.RIGHT));
                styles.getPropertyValues().clearPropertyValue(StylePropertyDetails.RIGHT);
            }
            if(styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.WIDTH) != null) {
                positionStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.WIDTH, styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.WIDTH));
            }
            if(styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.HEIGHT) != null) {
                positionStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.HEIGHT, styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.HEIGHT));
            }
            divElement = openDivElement(positionStyles, getCurrentBuffer(protocol));
        } else {
            Styles divStyles = StylingFactory.getDefaultInstance()
            .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(),
            DisplayKeywords.BLOCK);

            divElement = openDivElement(divStyles, getCurrentBuffer(protocol));
        }

        divElement.setAttribute("id", attributes.getId());
        StyleValue styleValue = styles.getPropertyValues().getSpecifiedValue(
                StylePropertyDetails.MCS_MENU_ORIENTATION);
        if (styleValue != null) {
            divElement.setAttribute("vfc-orientation", styleValue
                    .getStandardCSS());
        } else {
            divElement.setAttribute("vfc-orientation", "vertical");
        }

        styleValue = styles.getPropertyValues().getSpecifiedValue(
                StylePropertyDetails.MCS_TOGGLE_EVENT);
        if (styleValue != null) {
            divElement.setAttribute("vfc-openEvent", styleValue
                    .getStandardCSS());
        } else {
            divElement.setAttribute("vfc-openEvent", "focus");
        }

        styleTopMenu = styles;

        if(styleTopMenu.getPropertyValues().getSpecifiedValue(StylePropertyDetails.BACKGROUND_COLOR) == null){
            styleTopMenu.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.BACKGROUND_COLOR, StyleColorNames.WHITE);
        }

        if(styleTopMenu.getPropertyValues().getSpecifiedValue(StylePropertyDetails.WHITE_SPACE) == null){
            styleTopMenu.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.WHITE_SPACE, WhiteSpaceKeywords.NOWRAP);
        }
    }

    /**
     * Render for Navigation List element on Close
     */
    public void renderNlClose(VolantisProtocol protocol,
            NavigationListAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        // close innerDiv
        closeDivElement(currentBuffer);
        // close outerDiv
        closeDivElement(currentBuffer);
        closeDivElement(currentBuffer);

        level--;

        // only for top level menu generate javascript body
        if (level == 0) {

            StringBuffer textToScript = new StringBuffer("Widget.register(")
                .append(createJavaScriptString(attributes.getId()))
                .append(", new Widget.DynamicMenu(")
                .append(createJavaScriptString(attributes.getId()))
                .append(", {")
                .append(getAppearableOptions(attributes))
                .append(", ").append(getDisappearableOptions(attributes))
                .append(", objName: Widget.getInstance(").append(createJavaScriptString(attributes.getId()))
                .append(")")
                .append(" }));");

            try {
                writeStartupScriptElement(currentBuffer, textToScript.toString());
            } catch (IOException e) {
                throw new ProtocolException();
            }
        }

    }

    /**
     * Render for Label element on Open
     */
    public boolean renderLabelOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // if top level menu - no label on output
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        Element divElement = openDivElement(attributes.getStyles(),
                currentBuffer);

        Styles styles = attributes.getStyles();

        Styles unfoldedStyles = styles
                .findNestedStyles(StatefulPseudoClasses.MCS_UNFOLDED);
        StylesExtractor extractor = createStylesExtractor(protocol, unfoldedStyles);

        if (unfoldedStyles != null) {
            divElement.setAttribute("vfc-unfoldedstyle", extractor
                    .getJavaScriptStyles());
        }

        // top level menu - with empty label
        if (level == 1) {
            closeDivElement(currentBuffer);

            Styles divStyles = StylingFactory.getDefaultInstance()
            .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(),
            DisplayKeywords.BLOCK);

            divElement = openDivElement(divStyles, currentBuffer); // outerDiv
            divElement = openDivElement(styleTopMenu, currentBuffer); // innerDiv
            divElement.setAttribute("style", "display:block;");

            //remove pseudo element Marker style if were applied to label element on level 1
            clearMarkerContent(attributes);

            return false;
        } else {
            // put label content in anchor
            styles = StylingFactory.getDefaultInstance()
            .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(),
            DisplayKeywords.INLINE);

            Element link = currentBuffer.openStyledElement("a", styles);
            link.setAttribute("href","javascript:void(0)");
            return true;
        }
    }

    /**
     * Render for Label element on Close
     */
    public void renderLabelClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        if (level > 1) {
        	// common mechanism for rendering markers used
            // empty default values for backward compatibility
            StyleValue markerFoldedValue = getFoldedMarkerValue(protocol, attributes);
            String foldedMarkerId = renderFoldedMarker(protocol, markerFoldedValue,"");         

            StyleValue markerUnfoldedValue = getUnfoldedMarkerValue(protocol, attributes);
            String unfoldedMarkerId = renderUnfoldedMarker(protocol, markerUnfoldedValue,""); 
        }
        //close anchor
        currentBuffer.closeElement("a");

        closeDivElement(currentBuffer);
        Element divElement = null;

        // outerDiv
        Styles divStyles = StylingFactory.getDefaultInstance()
        .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(),
        DisplayKeywords.BLOCK);

        divElement = openDivElement(divStyles, currentBuffer);
        if (level > 1) {
            divElement.setAttribute("style", "display:none;");
        }

        // innerDiv
        divElement = openDivElement(styleTopMenu, currentBuffer);

        // top level menu
        if (level == 1) {
            divElement.setAttribute("style",
                    "position:relative; display:block;");
        }
    }

    /**
     * Render for li element on Open open div element
     *
     * @param protocol
     *            VolantisProtocol
     * @param attributes
     *            MCSAttributes
     */
    public void renderLiOpen(VolantisProtocol protocol, ListItemAttributes attributes)
            throws ProtocolException {
        // The default 'display' style for 'li' element is 'list-item'.
        // The rendered 'div' element should have it set to 'block'.
        attributes.getStyles().getPropertyValues().setComputedValue(StylePropertyDetails.DISPLAY,
                DisplayKeywords.BLOCK);

        // Open the 'div' element.
        Element div = openDivElement(attributes.getStyles(), getCurrentBuffer(protocol));

        //get from styleTopMenu nad set inline style
        String styleItemDiv = "";

        StyleValue valueWhiteSpace = styleTopMenu.getPropertyValues().getSpecifiedValue(StylePropertyDetails.WHITE_SPACE);
        styleItemDiv = "white-space: " + valueWhiteSpace.getStandardCSS();

        div.setAttribute("style", styleItemDiv);

        // We are rendering li as div, so must take extra care to support li-specific attributes.
        // This unfortunatelly duplicates code from  XHTMLBasic.addListItemAttributes(),
        // but we cannot delegate to protocol as this method is protected.
        LinkAssetReference url = attributes.getHref();
        if (url != null && url.getURL() != null) {
            div.setAttribute("href", url.getURL());
        }
    }

    /**
     * Render for li element on Close
     */
    public void renderLiClose(VolantisProtocol protocol,
            ListItemAttributes attributes) throws ProtocolException {
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        closeDivElement(currentBuffer);
    }

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        return;
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        return;
    }
}
