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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.JavaScriptContainer;
import com.volantis.mcs.protocols.widgets.AccessibleJavaScriptContainer;
import com.volantis.mcs.protocols.widgets.RemovableJavaScriptContainerFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.mcs.protocols.widgets.attributes.ButtonAttributes;
/**
 * Renderer for rendering wizard steps.
 * <p>
 * This step-renderer differs from WizardRenderer, so that
 * for each wizard element is rendered by separate step-renderer 
 * instance.
 */
public final class WizardStepRenderer {
    
    private final WizardDefaultRenderer renderer;

    /**
     * Protocol used to render wizard step.
     */
    private final VolantisProtocol protocol;

    /**
     * Rendered element enclosing all the wizard content.
     */
    private Element wizardElement;

    /**
     * Rendered element enclosing current wizard step.
     */
    private Element stepElement;

    /**
     * Styles of the xf:group element enclosing step element.
     */
    private Styles xfGroupStyles;
    
    /**
     * Last rendered 'Next' element. It's stored, because it needs to be removed
     * for the last wizard step.
     */
    private Element lastNextElement;

    /**
     * Currently processed wizard step number (indexed from 1).
     */
    private int stepNumber;
    
    /**
     * Mapping betwen actions and buttons responsible for triggering this actions. Provided by outer widget. 
     * 
     */
    private Map actionsButtonsMapping;
    
    /**
     * Internal button java script containers list. Used for storing javascripts
     * associated to button with given ID. When button is removed javascript also
     * will be removed.
     */
    private Map internalButtonsJavascriptContainer = new HashMap();
    
    
    /**
     * Map used to retrieve names of JavaScript functions to
     * invoke on button click.
     */
    static final private Map pseudoElementsActions = new HashMap();

    static {
        pseudoElementsActions.put(PseudoElements.MCS_NEXT,ActionName.NEXT);
        pseudoElementsActions.put(PseudoElements.MCS_PREVIOUS,ActionName.PREVIOUS);
        pseudoElementsActions.put(PseudoElements.MCS_CANCEL,ActionName.CANCEL);
        // complete element will be added by user so it can not
        // be inserted internally
    }

    /**
     * Map used to retrieve default button labels.
     */
    static final private Map buttonCaptions = new HashMap();

    static {
        buttonCaptions.put(PseudoElements.MCS_NEXT, ">>");
        buttonCaptions.put(PseudoElements.MCS_PREVIOUS, "<<");
        buttonCaptions.put(PseudoElements.MCS_CANCEL, "X");
        // complete element will be added by user so it can not
        // be inserted internally
    }

    /**
     * Map used to retrieve default button colors.
     */
    static final private Map buttonColors = new HashMap();

    static {
        buttonColors.put(PseudoElements.MCS_CANCEL, StyleColorNames.RED);
        buttonColors.put(PseudoElements.MCS_COMPLETE, StyleColorNames.GREEN);
    }

    /**
     * Creates and returns new instance of wizard step renderer.
     *
     * @param renderer parent wizard renderer 
     * @param protocol protocol to use for rendering
     * @param wizardElement rendered element enclosing the wizard step.
     * @return created wizard step renderer
     */
    public WizardStepRenderer(WizardDefaultRenderer renderer,
            VolantisProtocol protocol, Element wizardElement,Map actionButtonsMapping) {
        this.renderer = renderer;
        this.protocol = protocol;
        this.wizardElement = wizardElement;
        this.actionsButtonsMapping = actionButtonsMapping;

        lastNextElement = null;
        stepNumber = 0;
    }

    
    /**
     * Renders opening of wizard step.
     * 
     * @param xfGroupStyles styles of the xf:group element.
     */
    public void renderOpen(Styles xfGroupStyles) throws ProtocolException {
                    
        // Store styles of the xf:group element for future use.
        this.xfGroupStyles = xfGroupStyles;
        
        // Increment stepNumber, since we are starting to process
        // the next wizard step.
        stepNumber++;

        // Create styles for wizard-step element.
        // Initially, all wizard steps are hidden.
        // They'll be shown on client-side, by JavaScript code.
        Styles stepStyles = StylingFactory.getDefaultInstance()
            .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(), 
                    DisplayKeywords.BLOCK);

        stepStyles.getPropertyValues().setComputedValue(StylePropertyDetails.VISIBILITY, StyleKeywords.HIDDEN);        

        // Open 'div' element representing the wizard,
        DivAttributes outerDivAttributes = new DivAttributes();
        outerDivAttributes.setStyles(stepStyles);
        // and generate unique ID for it - last parameter to true
        renderer.openDivElement(this.protocol,outerDivAttributes,true);
        
        // generate inner DIV element for support slide effect on steps
        DivAttributes innerDivAttributes = new DivAttributes();
        Styles innerDivStyles = StylingFactory.getDefaultInstance()
        .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(), 
                DisplayKeywords.BLOCK);
        innerDivAttributes.setStyles(innerDivStyles);
        renderer.openDivElement(this.protocol,innerDivAttributes);
    }

    /**
     * Renders closure of wizard step.
     */
    public void renderClose() throws ProtocolException {
                
        // Render buttons within the wizard step 'div' element.
        renderButtons();

        // After buttons are rendered, it's OK to close the 'div' element.
        DOMOutputBuffer currentBuffer = getCurrentBuffer();
        
        //close inner DIV element 
        this.renderer.closeDivElement(this.protocol);
        
        //close outer DIV element - step element
        stepElement = this.renderer.closeDivElement(this.protocol);

        // Render step registration code.
        renderer.pushStepIdOnStack((String)stepElement.getAttributeValue("id"));
        // register step is no longer necessary
        // it is moved to wizard initialization

        // Update state of the renderer.
        stepElement = null;
    }

    /**
     * Post rendering. 
     */
    public void postRender() throws ProtocolException {
        
        if(!protocol.getProtocolConfiguration().isFrameworkClientSupported()) {
            return;
        }        
        

        // Button is now associated with javascript that hadles it
        // so removing buttons must be followed by removing it's scripts
        // therevore way of removing script must be provided
        // it is realized by creating special button renderer that create 
        // javascript contaniers that will be removed from map when button
        // is removed. All containers that left after removing process
        // will be rewrited to main javascript container and rendered on result page
        // here is alghoritm for rewriting used javascript containers into 
        // main javascript containers container.
        removeElement(lastNextElement);
        // rewrite used buttons into right script. 
        Iterator iter = this.internalButtonsJavascriptContainer.keySet().iterator();
        while(iter.hasNext()){
            AccessibleJavaScriptContainer jsContainer = 
                (AccessibleJavaScriptContainer)this.internalButtonsJavascriptContainer.get(iter.next());
            JavaScriptContainer newContainer = 
                renderer.createJavaScriptContainer(protocol);
            List usedId = jsContainer.getUsedWidgetsIdList();
            for(Iterator i = usedId.iterator();i.hasNext();){
                newContainer.addUsedWidgetId((String)i.next());
            }
            List createdId = jsContainer.getCreatedWidgetsIdList();
            for(Iterator i = createdId.iterator();i.hasNext();){
                newContainer.addCreatedWidgetId((String)i.next());
            }
            try {
                newContainer.getWriter().write(jsContainer.getJavaScriptContent());
            } catch (IOException e) {
                throw new ProtocolException();
            }
        }
        
    }
    
    /**
     * Remove element from rendering. Javascript containers for internal buttons
     * are stored in map and when button is removed associated javascript container
     * is also removed. 
     * 
     * <p>Therefore there is two-stage process of removing button
     * First remove button, later remove javascritp for it.</p>  
     * @param element
     */
    private void removeElement(Element element){
        if(null != element){
            String elementId = element.getAttributeValue("id");
            this.internalButtonsJavascriptContainer.remove(elementId);
            element.remove();
        }
    }

    /**
     * Renders a button of given type and returns rendered element.
     * 
     * @param pseudoElement button type (expressed as style pseudo-element).
     * @return rendered button element.
     */
    private Element renderButton(PseudoElement pseudoElement) throws ProtocolException {
        // get button attributes for given pseudo elements
        ActionName actionName = (ActionName)pseudoElementsActions.get(pseudoElement);
        
        // buttonAttributes contains only action reference for given button
        // action is  used for creating fresh button attributes with
        // unique id. 
        // buttonAttributes can contains also more data than action reference 
        ButtonAttributes refButtonAttributes = (ButtonAttributes)
            this.actionsButtonsMapping.get(actionName);
        // new attributes created to avoid duplicated ID.
        // ButtondefaultRenderer.renderOpen create id if don't exist
        // so reusing of the same attriubtes leads to duplicated ID. 
        final ButtonAttributes buttonAttributes = new ButtonAttributes();
        buttonAttributes.setActionReference(refButtonAttributes.getActionReference());
        
        if(null == buttonAttributes){
            throw new ProtocolException();            
        }
        
        DOMOutputBuffer buffer = getCurrentBuffer();

        
        //  Take styles for button element from the pseudo-element.
        Styles buttonStyles = xfGroupStyles.removeNestedStyles(pseudoElement);

        //  If they does not exist, create styles inheriting from parent ones.
        if (buttonStyles == null) {
            buttonStyles = StylingFactory.getDefaultInstance()
                .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(),
                        DisplayKeywords.INLINE);
        }

        //  Set default button colour.
        if (buttonStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.COLOR) == null) {
            StyleValue color = (StyleValue) buttonColors.get(pseudoElement);
            if (color != null) {
                buttonStyles.getPropertyValues().setComputedValue(StylePropertyDetails.COLOR, color);
            }
        }
        // styles calculated so set it on buttonAttriubutes
        buttonAttributes.setStyles(buttonStyles);
        String id = buttonAttributes.getId();
        if(null == id){
            buttonAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        // Get and clear the content property for button styles.
        // The content will be inserted after element is created.
        StyleValue contentValue = buttonStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.CONTENT);
            
        if (contentValue != null) {
            buttonStyles.getPropertyValues().clearPropertyValue(StylePropertyDetails.CONTENT);
        }
        
        // Special button renderer is created that use RemovableJavascriptContainer 
        // RemovableJavascriptContainer is removed when associated buutton is removed
        // Otherwise there will be erros on result page because of attempts to access
        // not-existing buttons. 
        ButtonDefaultRenderer internalButtonRenderer = new ButtonDefaultRenderer(){
            
            private JavaScriptContainer lastCreated;

            protected JavaScriptContainer createJavaScriptContainer(VolantisProtocol protocol) throws ProtocolException {
                // RemovableJavaScriptContainerFactory used because it creates containers
                // without storing them internally without removing. 
                JavaScriptContainer container =  
                    RemovableJavaScriptContainerFactory.getInstance().createJavaScriptContainer();
                internalButtonsJavascriptContainer.put(buttonAttributes.getId(), 
                        container);
                lastCreated = container;
                return container;
            }

            /**
             * Returns last crated container. 
             * <p>This will work only because buttons doesn't have any buttons inside
             * Otherwise implementation must be changed to more robust way of creating
             * current JavascriptContainer for rendered button</p>
             */
            protected JavaScriptContainer getCurrentJavaScriptContainer() {
                return lastCreated;
            }
            
        };
        
        // using new button renderer into rendering internal button
        internalButtonRenderer.renderOpen(protocol, buttonAttributes);

        Element element = buffer.getCurrentElement();
        // Write default button content.
        buffer.writeText((String) buttonCaptions.get(pseudoElement));

        internalButtonRenderer.renderClose(protocol, buttonAttributes);
            
        if (contentValue != null) {
            // Insert content for button element...
            ((DOMProtocol) protocol).getInserter().insert(element, contentValue);
        }
        
        return element;
       
    }

    /**
     * Renders buttons for current wizard step.
     */
    private void renderButtons() throws ProtocolException {
                
        // Do not render 'previous' button for first wizard step.
        if (stepNumber != 1) {
            renderButton(PseudoElements.MCS_PREVIOUS);
        }

        // Always render 'next' button - it will be removed 
        // from the last step in the postRender() method.
        lastNextElement = renderButton(PseudoElements.MCS_NEXT);

        // last element is no longer necessary as provided by user
        
        // Render cancel button only if the xf:group element has 
        // 'mcs-cancelable' set to 'always'.
        if (xfGroupStyles.getPropertyValues().getComputedValue(
                StylePropertyDetails.MCS_CANCELABLE) == StyleKeywords.ALWAYS) {
            renderButton(PseudoElements.MCS_CANCEL);
            
            // Inform the renderer, that there exists at least one
            // cancel button, so that renderer will create an instance
            // of cancel confirmation popup.
            renderer.setCancelable(true); 
        }
    }

    /**
     * Returns current output buffer to render to.
     * 
     * @return current output buffer.
     */
    private DOMOutputBuffer getCurrentBuffer() {
        return (DOMOutputBuffer) protocol.getMarinerPageContext()
                .getCurrentOutputBuffer();
    }
    
    /**
     * Renders registration of the Simple Validator widget.
     * 
     * @param id The ID of the validator to register.
     * @throws ProtocolException
     */
    protected void renderRegisterSimpleValidator(String id) throws ProtocolException {
        renderRegisterValidator(id, true);
    }

    /**
     * Renders registration of the Multiple Validator widget.
     * 
     * @param id The ID of the validator to register.
     * @throws ProtocolException
     */
    protected void renderRegisterMultipleValidator(String id) throws ProtocolException {
        renderRegisterValidator(id, false);
    }

    /**
     * Renders registration of the Validator widget.
     * 
     * @param id The ID of the validator to register.
     * @param simple True for simple or false for multiple validator.
     * @throws ProtocolException
     */
    private void renderRegisterValidator(String id, boolean simple) throws ProtocolException {
        
    	StringBuffer validatorsBuffer = renderer.getValidatorsBuffer();

    	// Render JavaScript content.
   		validatorsBuffer.append("$W('"
    				+ wizardElement.getAttributeValue("id") + "').");
          
		if (simple) {
			validatorsBuffer.append("registerSimpleValidator");
		} else {
			validatorsBuffer.append("registerMultipleValidator");
		}	          
		validatorsBuffer.append("(" + (stepNumber - 1)
		      + ", '" + id + "');");
    }
}
