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
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.FoldingItemContext;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.protocols.widgets.attributes.FoldingItemAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;

/**
 * The renderer for folding-item xdime2 element
 */
public class FoldingItemDefaultRenderer extends WidgetDefaultRenderer {

    public static final ScriptModule MODULE = createAndRegisterModule();
    
    private static ScriptModule createAndRegisterModule() {
        Set dependencies = new HashSet();
        // always needed directly dependend module
        dependencies.add(WidgetScriptModules.EFFECT_BASE);
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

        ScriptModule module = new ScriptModule("/vfc-foldingitem.mscr", dependencies,
                6100, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }


    /**
     * Stack with FoldingItemContext instances.
     * The context on the top of the stack is the current one.
     */
    private Stack contextsStack;
 
    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Push new folding item context, making it a current one.
        pushContext();

        FoldingItemContext foldingItemContext = getCurrentContext();
        
        // different way of rendering markers - now each of them
        // rendered separately, moreover markers should be set both
        // for folding-item and for summary element
        Styles foldedMarkerStyles = getFoldedMarkerStyles(protocol, attributes);
        if(foldedMarkerStyles != null) {
            foldingItemContext.setFoldedMarkerStyles(foldedMarkerStyles);
        }

        Styles unfoldedMarkerStyles = getUnfoldedMarkerStyles(protocol, attributes);
        if(unfoldedMarkerStyles != null) {
            foldingItemContext.setUnfoldedMarkerStyles(unfoldedMarkerStyles);
        }

        require(MODULE, protocol, attributes);
        
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        openDivElement(protocol, attributes);
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if(!isWidgetSupported(protocol)){
            return; 
        }

        // require AJAX script module
        if ( ((FoldingItemAttributes)attributes).getLoadAttributes() != null ) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }

        closeDivElement(protocol);
        
        FoldingItemContext foldingItemContext = getCurrentContext();
                        
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        String textToScript;
        String textLoadAttr = "";
            LoadAttributes loadAttributes = ((FoldingItemAttributes) attributes)
                    .getLoadAttributes();
            if (loadAttributes != null) {
                textLoadAttr = ", load_src: " + createJavaScriptString(loadAttributes.getSrc())
                        + ", load_when: " + createJavaScriptString(loadAttributes.getWhen());
            }

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
            
            stylesExtractor.getJavaScriptStyles();
            unfoldedStylesExtractor.getJavaScriptStyles();

            StringBuffer textBuffer = new StringBuffer("Widget.register(")
                    .append(createJavaScriptString(attributes.getId()))
                    .append(",new Widget.FoldingItem(")
                    .append(createJavaScriptString(attributes.getId()))
                    .append(", {")
                    .append("delay: ").append(stylesExtractor.getTransitionInterval())
                    .append(", ").append(getDisappearableOptions(disappearStylesExtractor))
                    .append(", ").append(getAppearableOptions(stylesExtractor))
                    
                    // This part of code is used to apply unfolded style for the widget:folding-item
                    // element. As this is not required in the functional spec, the code is commented.
                    // In the future, if there'll be a requirement to specify unfolded styles
                    // for the widget:folding-item element, just uncomment this line and it'll work.
                    //+ ", unfoldedElementStyle: " + unfoldedStylesExtractor.getJavaScriptStyles()
                    
                    .append(", unfoldedFtElementStyle: ").append(foldingItemContext.getSummaryUnfoldedStyles())
                    .append(", ftElementId: ")
                    .append(createJavaScriptString(foldingItemContext.getSummaryElementId()))
                    .append(", fdElementId: ")
                    .append(createJavaScriptString(foldingItemContext.getDetailsElementId()))
                    .append(", foldedSpanId: ")
                    .append(createJavaScriptString(foldingItemContext.getFoldedMarkerId()))
                    .append(", unfoldedSpanId: ")
                    .append(createJavaScriptString(foldingItemContext.getUnfoldedMarkerId()))
                    .append(((unfoldon == null) ? ""
                            : ", unfoldon: "+ createJavaScriptString(unfoldon.getStandardCSS())))
                    .append(((initial_state == null) ? ""
                            : ", initial_state: "+ createJavaScriptString(initial_state.getStandardCSS())))
                    .append(textLoadAttr).append("}));");
            
            styles.removeNestedStyles(StatefulPseudoClasses.MCS_CONCEALED);   
        
        try {
            writeScriptElement(currentBuffer, textBuffer.toString());
        } catch (IOException e) {
            throw new ProtocolException();
        }        

        // After the rendering is finished, pop current context.
        popContext();
    }
    
    /**
     * Creates and returns new instance of FoldingItemContext.
     * 
     * @return The FoldingItemContext.
     */
    private FoldingItemContext createContext() {
        return new FoldingItemContext();
    }
    
    /**
     * Returns stack of FoldingItemContext instances,
     * creating it if not created yet.
     * 
     * @return The stack of FoldingItemContext instanes.
     */
    private Stack getContextsStack() {
        if (contextsStack == null) {
            contextsStack = new Stack();
        }
        
        return contextsStack;
    }
    
    /**
     * Pushes new FoldingItemContext onto the stack, making it 
     * the current one, and returns it.
     * 
     * @return The current FoldingItemContext.
     */
    private FoldingItemContext pushContext() {
        return (FoldingItemContext) getContextsStack().push(createContext());
    }
    
    /**
     * Pops current instance of FoldingItemContext from the stack.
     * 
     * @throws EmptyStackException if stack is empty.
     */
    private void popContext() {
        getContextsStack().pop();
    }
    
    /**
     * Returns the current FoldingItemContext.
     * 
     * @throws EmptyStackException if stack is empty.
     */
    public FoldingItemContext getCurrentContext() {
        return (FoldingItemContext) getContextsStack().peek();
    }
}
