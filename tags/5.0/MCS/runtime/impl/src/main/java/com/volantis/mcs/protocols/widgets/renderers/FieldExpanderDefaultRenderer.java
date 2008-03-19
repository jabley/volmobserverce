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
import java.util.EmptyStackException;
import java.util.Stack;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.FieldExpanderContext;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renderer for field-expander widget suitable for HTML protocol
 */
public class FieldExpanderDefaultRenderer extends WidgetDefaultRenderer 
    implements FieldExpanderWidgetRenderer {
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(FieldExpanderDefaultRenderer.class);
 
    /**
     * Stack of currently processed FieldExpanders.
     */
    private Stack contextsStack;
    
    public void doRenderOpen(VolantisProtocol protocol,
    		MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }
        
        // Push new FieldExpander context, which will be used
        // to gather data from inner elements for further rendering.
        FieldExpanderContext context = pushContext();
        
        // Require all JavaScript libraries.
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr",protocol);
        requireLibrary("/vfc-fieldexpander.mscr",protocol);

        // Generate an ID, if it's not already there.
        if(attributes.getId() == null) {
            attributes.setId(
                   protocol.getMarinerPageContext().generateUniqueFCID());
        }	     
        
        // Open the <div> element enclosing FieldExpander content.
        openDivElement(protocol, attributes);
        
        // Set attributes, which will be used to create marker element.
        context.setMarkerAttributes(attributes);
    }
    
    public void doRenderClose(VolantisProtocol protocol,
    		MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }
    	
        FieldExpanderContext context = getCurrentContext();

        // Close outstanding <div> element.
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        
        closeDivElement(protocol);
        
        Styles styles = attributes.getStyles();
        StyleValue unfoldon = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_TOGGLE_EVENT);
        StyleValue initial_state = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_INITIAL_STATE);
                
        // Create instances of styles extractors.
        StylesExtractor stylesExtractor = 
            createStylesExtractor(protocol, attributes.getStyles());
        
        StylesExtractor disappearStylesExtractor = 
            createStylesExtractor(protocol, attributes.getStyles());
        disappearStylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_CONCEALED);
        
        StylesExtractor unfoldedStylesExtractor = 
            createStylesExtractor(protocol, attributes.getStyles());
        unfoldedStylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_UNFOLDED);
        
        stylesExtractor.setProtocol(protocol);
        unfoldedStylesExtractor.setProtocol(protocol);
        disappearStylesExtractor.setProtocol(protocol);
        
        stylesExtractor.getJavaScriptStyles();
        unfoldedStylesExtractor.getJavaScriptStyles();

        
        StringBuffer textToScript = new StringBuffer(150);
        textToScript.append("Widget.register(");
        textToScript.append(createJavaScriptString(attributes.getId()));
        textToScript.append(",new Widget.FieldExpander(");
        textToScript.append(createJavaScriptString(attributes.getId()));
        textToScript.append(", {");
        textToScript.append(" delay: ");
        textToScript.append(stylesExtractor.getTransitionInterval());
        textToScript.append(", ").append(getDisappearableOptions(disappearStylesExtractor));
        textToScript.append(", ").append(getAppearableOptions(stylesExtractor));
        textToScript.append(", unfoldedStyle: ");
        textToScript.append(unfoldedStylesExtractor.getJavaScriptStyles());
        if (unfoldon != null) {
            textToScript.append(", unfoldon: ");
            textToScript.append(createJavaScriptString(unfoldon.getStandardCSS()));
        }
        if (initial_state != null) {
            textToScript.append(", initial_state: ");
            textToScript.append(createJavaScriptString(initial_state.getStandardCSS()));
        }

        if (context.getSummaryElementId() != null) {
            textToScript.append(", ftElementId: ");
            textToScript.append(createJavaScriptString(context.getSummaryElementId()));
        }
        
        if (context.getDetailsElementId() != null) {
            textToScript.append(", fdElementId: ");
            textToScript.append(createJavaScriptString(context.getDetailsElementId()));
        }
        
        if (context.getSummaryFieldId() != null) {
            textToScript.append(", primFieldId: ");
            textToScript.append(createJavaScriptString(context.getSummaryFieldId()));
        }
        
        if (context.getFoldedMarkerId() != null) {
            textToScript.append(", foldedSpanId: ");
            textToScript.append(createJavaScriptString(context.getFoldedMarkerId()));
        }
        
        if (context.getUnfoldedMarkerId() != null) {
            textToScript.append(", unfoldedSpanId: ");
            textToScript.append(createJavaScriptString(context.getUnfoldedMarkerId()));
        }
        
        textToScript.append("}));");
     
           
        try {
            writeScriptElement(currentBuffer, textToScript.toString());
        } catch (IOException e) {
            throw new ProtocolException();
        }
        
        // After the FieldExpander is rendered, pop it from the stack.
        styles.removeNestedStyles(StatefulPseudoClasses.MCS_CONCEALED);     
        
        // Pop current context after rendering is finished.
        popContext();
    }

    /**
     * Generate Field Expander markup after opening an XFGroup 
     */
    public void renderXFGroupOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }
        
        // Create two <div> elements enclosing field expander content.
        // Generate an ID for the first one.
        Element outerDiv = openDivElement(protocol, attributes, true);
        
        getCurrentContext().setDetailsElementId(outerDiv.getAttributeValue("id"));
        
        openDivElement(protocol);
        
    }     

    /**
     * Generate Field Expander markup before closing an XFGroup  
     */
    public void renderXFGroupClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }

        // Close outstanding <div> elements.
        closeDivElement(protocol);
        closeDivElement(protocol);
    }

    /**
     * Generate Field Expander markup after opening an XFControl  
     */
    public void renderXFControlOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        
        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }

        // Make sure the summary input field contains ID, so that it can be
        // referenced from JavaScript code.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }

        Element div = openDivElement(protocol, null, true);
        
        getCurrentContext().setSummaryElementId(div.getAttributeValue("id"));
        
        getCurrentContext().setSummaryFieldId(attributes.getId());

        createMarker(protocol, getCurrentContext().getMarkerAttributes(), getCurrentBuffer(protocol));
    }

    /**
     * Generate Field Expander markup before closing an XFControl  
     */
    public void renderXFControlClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }

        closeDivElement(protocol);        
    }

    /**
     * Creates a marker indicating the state of the widged - folded or unfolded
     */
    private void createMarker(VolantisProtocol protocol, MCSAttributes attributes,
            DOMOutputBuffer currentBuffer) throws ProtocolException {
        
        FieldExpanderContext fieldExpander = getCurrentContext();
        
        // Generate ID for folded and unfolded markers,
        // so they can be passed to the Widget.FieldExpander() 
        // JavaScript constructor.
    	
        // common mechanism used for all marker-related scenarios
    	StyleValue markerFoldedValue = getFoldedMarkerValue(protocol, attributes);
        String foldedMarkerId = renderFoldedMarker(protocol, markerFoldedValue	, DEFAULT_FOLDED_MARKER);         
        fieldExpander.setFoldedMarkerId(foldedMarkerId);

    	StyleValue markerUnfoldedValue = getUnfoldedMarkerValue(protocol, attributes);
        String unfoldedMarkerId = renderUnfoldedMarker(protocol, markerUnfoldedValue	, DEFAULT_UNFOLDED_MARKER); 
        fieldExpander.setUnfoldedMarkerId(unfoldedMarkerId);
    }
    
    /**
     * Creates and returns new instance of FieldExpander.
     * 
     * @return The FieldExpander.
     */
    private FieldExpanderContext createContext() {
        return new FieldExpanderContext();
    }
    
    /**
     * Returns stack of FieldExpander contexts.
     * 
     * @return The stack of FieldExpander contexts.
     */
    private Stack getContextsStack() {
        if (contextsStack == null) {
            contextsStack = new Stack();
        }
        
        return contextsStack;
    }
    
    /**
     * Pushes and returns new FieldExpander context,
     * making it current.
     * 
     * @param fieldExpander The FieldExpander to push.
     * @returns The current context.
     */
    private FieldExpanderContext pushContext() {
        return (FieldExpanderContext) getContextsStack().push(createContext());
    }
    
    /**
     * Pops an instance of field expander context the stack.
     * 
     * @throws EmptyStackException if stack is empty.
     */
    private void popContext() {
        getContextsStack().pop();
    }
    
    /**
     * Returns an instance of field expander context 
     * from the top of the stack.
     * 
     * @throws EmptyStackException if stack is empty.
     */
    public FieldExpanderContext getCurrentContext() {
        return (FieldExpanderContext) getContextsStack().peek();
    }
}
