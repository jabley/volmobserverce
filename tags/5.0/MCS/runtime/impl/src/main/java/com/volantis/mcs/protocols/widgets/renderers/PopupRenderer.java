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
 * (c) Volantis Systems Ltd 2006 - 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.internal.attributes.EffectBlockAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.VisibilityKeywords;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

public class PopupRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(PopupRenderer.class);
        
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES = {
        ActionName.DISMISS, 
        ActionName.SHOW
    };
    
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        
        if(!isWidgetSupported(protocol)) {
            return;
        }

        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr",protocol);
        requireLibrary("/vfc-popup.mscr",protocol);

        // If popup ID was not specified, generate it automatically.
        if(attributes.getId() == null) {
            attributes.setId(
                   protocol.getMarinerPageContext().generateUniqueFCID());
        }        
                
        // Popup internally renders a block
        EffectBlockAttributes blockAttributes = new EffectBlockAttributes();
        blockAttributes.copy(attributes);
        blockAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        pushMCSAttributes(blockAttributes);
        renderWidgetOpen(protocol, blockAttributes);
    }

    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        
        if(!isWidgetSupported(protocol)) {
            return;
        }
        
        // Retrieve block attributes stored in doRenderOpen
        EffectBlockAttributes blockAttributes = (EffectBlockAttributes)popMCSAttributes();
        renderWidgetClose(protocol, blockAttributes);
        addUsedWidgetId(blockAttributes.getId());
                
        // Detect full screen popup. This will be used on the client side 
        // to apply ugly workarounds for scrolling.  
        Styles styleTable = attributes.getStyles();
        StyleValue heightValue = styleTable.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.HEIGHT);
        StyleValue widthValue = styleTable.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.WIDTH);
        StyleValue positionValue = styleTable.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.POSITION);
        StyleValue overflowValue = styleTable.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.OVERFLOW);

        String fullScreen;
        if (heightValue != null && positionValue != null && widthValue!=null && overflowValue!=null) {
            if (heightValue.getStandardCSS().equals("100%")
                    && widthValue.getStandardCSS().equals("100%")
                    && (overflowValue.getStandardCSS().equals("scroll") || overflowValue.getStandardCSS().equals("auto"))
                    && positionValue.getStandardCSS().equals("fixed")) {
                fullScreen = new String(" fullScreen: true");                
            } else {
                fullScreen = new String(" fullScreen: false");
            }
        } else {
            fullScreen = new String(" fullScreen: false");
        }
                
        // Create the widget
        StringBuffer textBuffer = new StringBuffer(createJavaScriptWidgetRegistrationOpening(attributes.getId()))
            .append("new Widget.Popup(")
            .append(createJavaScriptString(attributes.getId()))
            .append(", ").append(createJavaScriptString(blockAttributes.getId()))
            .append(", {")
            .append(fullScreen)
            .append(", ").append(getDisappearableOptions(attributes))
            .append(", ").append(getAppearableOptions(attributes))
            .append("})")
            .append(createJavaScriptWidgetRegistrationClosure());
                
        writeJavaScript(textBuffer.toString());
        addCreatedWidgetId(attributes.getId());

        attributes.getStyles().removeNestedStyles(StatefulPseudoClasses.MCS_CONCEALED);
    }

    // check initial state (visible/invisible) for fallback generation markup
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {

        // In normal situation the contents of popup is of course needed 
        if(isWidgetSupported(protocol)) {
            return true;
        }
            
        // In case of fallback, we don't render the contents if the popup
        // was initialy hidden. In case of uninitialized attributes or styles
        // (may happen if this renderer is called from some other widget renderer)
        // we also assume the popup content's should not be rendered in fallback
        if (null == attributes) {
            return false;
        }
        
        Styles styles = attributes.getStyles();
        if (null == styles) {
            return false;
        }      
        return isInitiallyVisible(styles);
    }

    /**
     * @return Returns true if this popup is supposed to be initially visible
     */
    protected boolean isInitiallyVisible(Styles styles) {
        StyleValue visibilityValue 
            = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.VISIBILITY);                
        if (VisibilityKeywords.HIDDEN.equals(visibilityValue)) {
           return false;
        }
        
        StyleValue displayValue 
            = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.DISPLAY);        
        if (DisplayKeywords.NONE.equals(displayValue)) {
            return false;
        }
        
        return true;
    }

    // Javadoc inherited
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }
}
