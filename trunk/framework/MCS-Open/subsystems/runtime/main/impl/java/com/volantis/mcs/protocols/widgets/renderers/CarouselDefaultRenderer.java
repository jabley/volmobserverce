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

import java.io.StringWriter;
import java.util.Set;
import java.util.HashSet;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.attributes.FeedAttributes;
import com.volantis.mcs.protocols.ticker.renderers.ElementDefaultRenderer;
import com.volantis.mcs.protocols.widgets.attributes.CarouselAttributes;
import com.volantis.mcs.protocols.widgets.attributes.RefreshAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renderer for Carousel widget suitable for HTML protocol
 */
public class CarouselDefaultRenderer extends WidgetDefaultRenderer {

    public static final ScriptModule MODULE = createAndRegisterModule();

    private static ScriptModule createAndRegisterModule() {
        Set dependencies = new HashSet();
        // always needed directly dependend module
        dependencies.add(WidgetScriptModules.EFFECT_BASE);
        dependencies.add(WidgetScriptModules.BASE_BB_EFFECT);
        dependencies.add(WidgetScriptModules.BASE_REFRESHABLE);

        // conditionals effects modules
        dependencies.add(WidgetScriptModules.CE_APPEAR);
        dependencies.add(WidgetScriptModules.CE_BLINDDOWN);
        dependencies.add(WidgetScriptModules.CE_BLINDUP);
        dependencies.add(WidgetScriptModules.CE_DROPOUT);
        dependencies.add(WidgetScriptModules.CE_FADE);
        dependencies.add(WidgetScriptModules.CE_FOLD);
        dependencies.add(WidgetScriptModules.CE_GROW);
        dependencies.add(WidgetScriptModules.CE_PUFF);
        dependencies.add(WidgetScriptModules.CE_SHAKE);
        dependencies.add(WidgetScriptModules.CE_SHRINK);
        dependencies.add(WidgetScriptModules.CE_SLIDE);
        dependencies.add(WidgetScriptModules.CE_PULSATE);

        ScriptModule module = new ScriptModule("/vfc-carousel.mscr", dependencies,
                19700, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }
        
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(CarouselDefaultRenderer.class);
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private String itemTemplateId;
    private OutputBuffer itemTemplateOutputBuffer;


    /**
     * Set internal div styles.
     * 
     * Internal div is a container for content and is needed only for effects. 
     * To work properly its padding and bottom must be set to 0. Additionally
     * for NetFront 3.4 the background-color needs to be set explicitely (VBM 2006091317)
     */
    private void setInternalDivStyles(Styles style, Styles parent) {
        MutablePropertyValues props = style.getPropertyValues(); 
        
        props.setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_BOTTOM,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        props.setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_LEFT,
        	STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        props.setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_RIGHT,
        	STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        props.setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_TOP,
        	STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));

        props.setComputedAndSpecifiedValue(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        props.setComputedAndSpecifiedValue(StylePropertyDetails.BORDER_LEFT_WIDTH,
        	STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        props.setComputedAndSpecifiedValue(StylePropertyDetails.BORDER_RIGHT_WIDTH,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        props.setComputedAndSpecifiedValue(StylePropertyDetails.BORDER_TOP_WIDTH,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        
        // See VBM 2006091317
        props.setComputedAndSpecifiedValue(StylePropertyDetails.BACKGROUND_COLOR,
                parent.getPropertyValues().getComputedValue(StylePropertyDetails.BACKGROUND_COLOR));
    }
    
    /**
     * Set padding and margin of UL element to 0. 
     * 
     * <p>These properties should be 0 to force UL behave
     * like div. Without this there were different size carousel content and carousel
     *  itself observed on opera Mobile Browser.</p>
     * 
     * @param style
     */
    private void setULStyles(Styles style){
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_BOTTOM,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_LEFT,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_RIGHT,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_TOP,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
          
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.MARGIN_BOTTOM,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.MARGIN_LEFT,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.MARGIN_RIGHT,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
        style.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.MARGIN_TOP,
            STYLE_VALUE_FACTORY.getLength(null, 0,LengthUnit.PX));
    }
    
    /**
     * Called on open of carousel element
     */
    public void doRenderOpen(VolantisProtocol protocol,
    	MCSAttributes attributes) throws ProtocolException {

    	if (!isWidgetSupported(protocol)) {
            // Fallback behaviour - if the device does not support the carousel
            // then we simply display all the content.
    	    return;
    	}

        require(MODULE, protocol, attributes);
        
        if(attributes.getId() == null){
            attributes.setId(
                   protocol.getMarinerPageContext().generateUniqueFCID());
        }	     
        
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        // External DIV gets the styles specified for the carousel, such
        // as background, border etc. This is not subject to effects,
        // as the effects are applied to content, not to the whole widget.
        // In this regards Carousel differs from other widgets.
        Element divElement =
            openDivElement(attributes.getStyles(), currentBuffer);
        divElement.setAttribute("id", attributes.getId());

        // Internal div. That's the one to which effects are applied. 
        Styles intDivStyle = StylingFactory.getDefaultInstance().createInheritedStyles(
                attributes.getStyles(),DisplayKeywords.BLOCK);
        setInternalDivStyles(intDivStyle, attributes.getStyles());        
        openDivElement(intDivStyle, currentBuffer); 

        Styles ulStyle = StylingFactory.getDefaultInstance().createInheritedStyles(
                attributes.getStyles(),DisplayKeywords.BLOCK);
        setULStyles(ulStyle);
        currentBuffer.openStyledElement("ul",ulStyle);
        
        itemTemplateId = null;
        itemTemplateOutputBuffer = null;
    }
    
    /**
     * Called on close of carousel element
     */
    public void doRenderClose(VolantisProtocol protocol,
    		MCSAttributes attributes) throws ProtocolException {
        
        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour - if the device does not support the carousel 
            // then we simply display all the content. 
            return;
        }

        // require AJAX script module
        if ( ((CarouselAttributes)attributes).getRefreshAttributes() != null ) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        currentBuffer.closeElement("ul");
        closeDivElement(currentBuffer);  
        // close external div element
        closeDivElement(currentBuffer);
                    
        RefreshAttributes refreshAttributes = ((CarouselAttributes)attributes).getRefreshAttributes();
        String textRefreshAttr = "";
        if(refreshAttributes != null) {               
            textRefreshAttr = ", refreshURL: "+ createJavaScriptString(refreshAttributes.getSrc()) +
            ", refreshInterval: "+ refreshAttributes.getInterval();
        }
        
        StylesExtractor styles = createStylesExtractor(protocol, attributes.getStyles());
        
        StylesExtractor disappearStyles = createStylesExtractor(protocol, attributes.getStyles());
        disappearStyles.setPseudoClass(StatefulPseudoClasses.MCS_CONCEALED);
        
        StringBuffer textToScriptBuffer = new StringBuffer();
        textToScriptBuffer.append("Widget.register(")
                          .append(createJavaScriptString(attributes.getId()))
                          .append(",new Widget.Carousel(")
                          .append(createJavaScriptString(attributes.getId()))
                          .append(", {")
                          .append("delay: ")
                          .append(styles.getTransitionInterval())
                          .append(", ").append(getAppearableOptions(styles))
                          .append(", ").append(getDisappearableOptions(disappearStyles))
                          .append(textRefreshAttr)
                          .append(", focusable: ")
                          .append(createJavaScriptString(styles.getFocusStyle()))
                          .append("}));");
                    
        addCreatedWidgetId(attributes.getId());
        
        String textToScript = textToScriptBuffer.toString();
            
        // Render the item template outside Carousel content.
        if (itemTemplateOutputBuffer != null) {
            getCurrentBuffer(protocol).transferContentsFrom(itemTemplateOutputBuffer);
            
            itemTemplateOutputBuffer = null;
        }
        
        // If this widget contains feed element, render the controller.
        FeedAttributes feedAttributes = ((CarouselAttributes)attributes).getFeedAttributes();
        
        if (feedAttributes != null) {

            // The controller class is defined in the vfc-ticker JavaScript
            // library, so we need to add the requirement here.
            require(ElementDefaultRenderer.WIDGET_TICKER, protocol, attributes);            

            StringWriter scriptWriter = new StringWriter();

            scriptWriter.write("Ticker.createCarouselController({");
            scriptWriter.write("carousel:Widget.getInstance(" + createJavaScriptString(attributes.getId()) + ")");
            
            if (feedAttributes.getChannel() != null) {
                scriptWriter.write(", channel:" + createJavaScriptString(feedAttributes.getChannel()));
            }

            if (feedAttributes.getItemDisplay() != null) {
                scriptWriter.write(", itemDisplayId:" + createJavaScriptString(feedAttributes.getItemDisplay()));
            }
            
            if (itemTemplateId != null) {
                scriptWriter.write(", itemTemplate:" + createJavaScriptWidgetReference(itemTemplateId));
                
                addUsedWidgetId(itemTemplateId);
                
                itemTemplateId = null;
            }
            
            scriptWriter.write("});");
            
            textToScript = textToScript + scriptWriter.toString();
            
            addUsedWidgetId(protocol.getMarinerPageContext()
                    .generateFCID(ElementDefaultRenderer.FEED_POLLER_ID_SUFFIX));
        }

        writeJavaScript(textToScript);
    }
    
    public void setItemTemplateId(String id) {
        itemTemplateId = id;
    }
    
    public void setItemTemplateOutputBuffer(OutputBuffer buffer) {
        itemTemplateOutputBuffer = buffer;
    }
}
