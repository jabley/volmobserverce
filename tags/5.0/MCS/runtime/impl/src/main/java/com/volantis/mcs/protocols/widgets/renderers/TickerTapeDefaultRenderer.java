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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.attributes.FeedAttributes;
import com.volantis.mcs.protocols.ticker.renderers.ElementDefaultRenderer;
import com.volantis.mcs.protocols.widgets.attributes.RefreshAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TickerTapeAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for TickerTape widget suitable for HTML protocols.
 */
public class TickerTapeDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(TickerTapeDefaultRenderer.class);
    private String itemTemplateId;
    private OutputBuffer itemTemplateOutputBuffer;

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Inform of required JavaScript libraries.
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-tickertape.mscr", protocol);

        // If ticker element does not have an ID, generate it,
        // because it'll be needed later.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        // Eventually, open the <div> element with given ID.
        openDivElement(protocol, attributes);
/*
        // Open divs with specified style. This was initialy done on client site,
        // but to preserve from css optimalisation it is moved here.
        DivAttributes divAttributes = createDivAttributes(protocol, null, false);
        MutablePropertyValues styleValues = divAttributes.getStyles().getPropertyValues();
        styleValues.setComputedAndSpecifiedValue(StylePropertyDetails.OVERFLOW, StyleKeywords.HIDDEN);
        styleValues.setComputedAndSpecifiedValue(StylePropertyDetails.TEXT_ALIGN, StyleKeywords.LEFT);
        styleValues.setComputedAndSpecifiedValue(StylePropertyDetails.WIDTH, new StylePercentage(100));
        styleValues.setComputedAndSpecifiedValue(StylePropertyDetails.HEIGHT, new StylePercentage(100));
        openDivElement(protocol, divAttributes);
        
        divAttributes = createDivAttributes(protocol, null, false);
        styleValues = divAttributes.getStyles().getPropertyValues();
        styleValues.setComputedAndSpecifiedValue(StylePropertyDetails.POSITION, StyleKeywords.RELATIVE);
        styleValues.setComputedAndSpecifiedValue(StylePropertyDetails.LEFT, StyleLength(0, LengthUnit.PX));
        openDivElement(protocol, divAttributes);
*/
        
        itemTemplateId = null;
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        // 
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        // Close DIV elements, which ware open in renderOpen().
        closeDivElement(protocol);
        //closeDivElement(protocol);
        //closeDivElement(protocol);

        // If this widget contains feed, render the controller.
        FeedAttributes feedAttributes = ((TickerTapeAttributes) attributes).getFeedAttributes();
        
        String separatorId = null;

        if (feedAttributes != null) {
            // Apply the separator
            Styles separatorStyles = feedAttributes.getStyles();
            
            if (separatorStyles != null) {
                separatorStyles = separatorStyles.getNestedStyles(PseudoElements.MCS_ITEM);
                
                if (separatorStyles != null) {
                    separatorStyles = separatorStyles.getNestedStyles(PseudoElements.MCS_BETWEEN);                    
                    
                    if (separatorStyles != null) {
                        /* If the <feed> element contains styles specified for the 
                         * ::mcs-item::mcs-between pseudo-element, render the separator.
                         *                        
                         * The rendered markup will look like:
                         * <span id="xxx">
                         *     <span style="xxx"> *** </span>
                         * </span>
                         */
                        
                        // First, create a span element which would act as a placeholder
                        // for the actual content of the separator.
                        Element placeholderSpan = getCurrentBuffer(protocol).openStyledElement("span",
                                StylingFactory.getDefaultInstance()
                                    .createInheritedStyles(
                                        protocol.getMarinerPageContext().getStylingEngine()
                                        .getStyles(), DisplayKeywords.NONE));

                        separatorId = protocol.getMarinerPageContext().generateUniqueFCID();
                        
                        placeholderSpan.setAttribute("id", separatorId);
                        
                        // Now, create a span element which would hold styles, and the actual
                        // separator content.
                        Element contentSpan = getCurrentBuffer(protocol).openStyledElement("span",
                                separatorStyles);

                        // Copy the content of the separator, if there's any.
                        StyleValue separatorContent = separatorStyles.getPropertyValues()
                                .getSpecifiedValue(StylePropertyDetails.CONTENT);
                
                        if (separatorContent != null) {
                            ((DOMProtocol)protocol).getInserter().insert(contentSpan, separatorContent);
                        }   
                        
                        // Close the content span.
                        getCurrentBuffer(protocol).closeElement("span");

                        // Close the placeholder span.
                        getCurrentBuffer(protocol).closeElement("span");
                    }
                }
            }            
        }
        
        RefreshAttributes refreshAttributes = ((TickerTapeAttributes) attributes)
                .getRefreshAttributes();
        
        // Read ticker-tape properties out of style attributes.
        StylesExtractor styles = createStylesExtractor(protocol, attributes.getStyles());
        
        // Prepare Javascript content.
        StringWriter scriptWriter = new StringWriter();
        scriptWriter.write(createJavaScriptWidgetRegistrationOpening(attributes.getId()));
        
        addCreatedWidgetId(attributes.getId());
        
        scriptWriter.write("new Widget.TickerTape(" + createJavaScriptString(attributes.getId()) + ", {");

        // Render scroll type: scroll, slide or alternating.
        scriptWriter.write("style:" + createJavaScriptString(styles.getMarqueeStyle()) + ",");
        scriptWriter.write("focusable:" + createJavaScriptString(styles.getFocusStyle()) + ",");

        // Render scroll attributes: direction, FPS & CPS.
        scriptWriter.write("scroll:{");
        scriptWriter.write("direction:" + createJavaScriptString(styles.getMarqueeDirection()) + ",");
        scriptWriter.write("framesPerSecond:" + styles.getFrameRate() + ",");
        scriptWriter.write("charsPerSecond:" + styles.getMarqueeSpeed());
        scriptWriter.write("}");    

        // Render refresh attributes: URL & interval.
        if (refreshAttributes != null) {
            scriptWriter.write(",refresh:{");
            scriptWriter.write("url:" + createJavaScriptString(refreshAttributes.getSrc()) + ",");
            scriptWriter.write("interval:" + createJavaScriptString(refreshAttributes.getInterval()));
            scriptWriter.write("}");
        }

        int repetitions = styles.getMarqueeRepetitions();
        scriptWriter.write(",repetitions:" + ((repetitions != Integer.MAX_VALUE) ? 
                        Integer.toString(repetitions) : createJavaScriptString("infinite")));

        // Render closing parentheses.
        scriptWriter.write("})");
        
        scriptWriter.write(createJavaScriptWidgetRegistrationClosure());

        scriptWriter.write(";");
        
        // Render the item template outside Carousel content.
        if (itemTemplateOutputBuffer != null) {
            getCurrentBuffer(protocol).transferContentsFrom(itemTemplateOutputBuffer);
            
            itemTemplateOutputBuffer = null;
        }
        
        // If this widget contains feed, render the controller.
        if (feedAttributes != null) {
            // The controller class is defined in the vfc-ticker JavaScript
            // library, so we need to add the requirement here.
            requireLibrary("/vfc-ticker.mscr", protocol);
            
            scriptWriter.write("Ticker.createTickerTapeController({tickerTape:Widget.getInstance(" + 
                    createJavaScriptString(attributes.getId()) + ")");

            if (feedAttributes.getChannel() != null) {
                scriptWriter.write(", channel:" + createJavaScriptString(feedAttributes.getChannel()));
            }

            if (feedAttributes.getItemDisplay() != null) {
                scriptWriter.write(", itemDisplayId:" + createJavaScriptString(feedAttributes.getItemDisplay()));
            }
            
            if (separatorId != null) {
                scriptWriter.write(", separatorId:" + createJavaScriptString(separatorId));
            }
            
            if (itemTemplateId != null) {
                scriptWriter.write(", itemTemplate:" + createJavaScriptWidgetReference(itemTemplateId));

                addUsedWidgetId(itemTemplateId);
            }
            
            scriptWriter.write("});");

            addUsedWidgetId(protocol.getMarinerPageContext()
                    .generateFCID(ElementDefaultRenderer.FEED_POLLER_ID_SUFFIX));
        }
        
        // Write JavaScript content to DOM.
        writeJavaScript(scriptWriter.toString());
    }
    
    public void setItemTemplateId(String id) {
        itemTemplateId = id;
    }
    
    public void setItemTemplateOutputBuffer(OutputBuffer buffer) {
        itemTemplateOutputBuffer = buffer;
    }
}
