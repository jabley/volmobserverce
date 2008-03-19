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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.ActionReferenceImpl;
import com.volantis.mcs.protocols.widgets.attributes.ButtonAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DismissAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PopupAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WizardAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

public class WizardDefaultRenderer extends WidgetDefaultRenderer implements
        WizardRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(WizardRenderer.class);

    /**
     * Buffer for validators. Validators must be register in wizard after his 
     * registration so buffer which will be rendered afrer registration wizard 
     * is needed
     */
    private StringBuffer validatorsBuffer;
    
    /**
     * Stack of wizard step renderers. The renderer on the top of the stack is
     * current.
     */
    private Stack stepRenderersStack;

    /**
     * is true if wizard is has at least one mcs-cancelable
     */
    private boolean isCancelable;

    /**
     * Used to retrieve localized messages.
     */
    private static final MessageLocalizer messageLocalizer = LocalizationFactory
            .createMessageLocalizer(WizardRenderer.class);

    private static final ActionName[] WIZARD_ACTIONS = new ActionName[] {
            ActionName.NEXT, ActionName.PREVIOUS, ActionName.CANCEL,
            ActionName.COMPLETE, ActionName.LAUNCH };

    private static final ActionName[] INTERNAL_ACTIONS = new ActionName[] {
            ActionName.NEXT, ActionName.PREVIOUS, ActionName.CANCEL };

    private Map actionsToInternalButtonsMap = new HashMap();

    private Stack stepsIdStack = new Stack();

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }

        setCancelable(false);

        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/vfc-popup.mscr", protocol);
        requireLibrary("/validate.mscr", protocol);
        requireLibrary("/vfc-validator.mscr", protocol);
        requireLibrary("/vfc-wizard.mscr", protocol);

        // If there's no ID attribute, generate it automatically.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }
        // id of widget is initialized so we can initialize internal actions
        initializeActionsToInternalButtonsMap(attributes.getId());

        Styles styles = attributes.getStyles();
        if(styles == null){
        	styles =  StylingFactory.getDefaultInstance()
            .createInheritedStyles(protocol.getMarinerPageContext().getStylingEngine().getStyles(), 
                    DisplayKeywords.BLOCK);
        	attributes.setStyles(styles);
        }
        styles.getPropertyValues().setComputedValue(StylePropertyDetails.VISIBILITY, StyleKeywords.HIDDEN);        
        styles.getPropertyValues().setComputedValue(StylePropertyDetails.DISPLAY, StyleKeywords.BLOCK);        
        
        validatorsBuffer = new StringBuffer();
        
        // Open 'div' element representing the wizard,
        // copying ID attribute and styles from the wizard element.
        Element divElement = openDivElement(protocol,attributes);

        // Now, create a renderer to render enclosed wizard steps.
        // Push it onto the stack, so that it could be read while
        // processing inner XForm Group elements.
        pushStepRenderer(createStepRenderer(protocol, divElement,
                this.actionsToInternalButtonsMap));
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }

        // Perform post-action, after all steps have been rendered.
        getCurrentStepRenderer(false).postRender();

        // Now, so that wizard steps have already been rendered,
        // pop step renderer from the stack.
        popStepRenderer();

        // Close 'div' element representing the wizard.
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        Element wizardElement = currentBuffer.getCurrentElement();
        closeDivElement(protocol);

        String stepsIdJSArray = createStepsIdJSArray();

        // check if user define dialog popup
        String dialog = ((WizardAttributes) attributes).getCancelDialog();

        // generate javascript with initialize function for widget
        StringBuffer scriptBuffer = new StringBuffer("Widget.register(")
                .append(createJavaScriptString(attributes.getId())).append(
                        ", new Widget.Wizard(").append(
                        createJavaScriptString(attributes.getId()))
                .append(",{").append(getAppearableOptions(attributes)).append(
                        ", ").append(getDisappearableOptions(attributes))
                .append(", stepsIdsArray : ").append(stepsIdJSArray).append(
                        "}));");

        // add registration for all step's validators
        scriptBuffer.append(validatorsBuffer);
        
        if (isCancelable() && dialog == null) {

            if (logger.isDebugEnabled()) {
                logger.debug("Creating default cancel dialog for cancellable wizard " +
                        "with no user-defined cancel dialog");
            }

            dialog = createCancelDialog(protocol);            
        }

        if (dialog != null) {

            if (logger.isDebugEnabled()) {
                logger.debug("Using user-defined dialog " + dialog 
                        + " as cancel dialog for wizard.");
        }
            // register popup to be used as cancel dialog by wizard
            scriptBuffer.append("$W("
                    + createJavaScriptString(attributes.getId())
                    + ").registerPopup(" + createJavaScriptString(dialog)
                    + ");");
            addUsedWidgetId(dialog);

        }

        addCreatedWidgetId(attributes.getId());
        
        try {
            getJavaScriptWriter().write(scriptBuffer.toString());
        } catch (IOException e) {
            throw new ProtocolException();
        }

        // save launch button if exists
        if (currentBuffer.hasInsertionPoint()) {
            currentBuffer.restoreInsertionPoint();
            Element launchElement = currentBuffer.getCurrentElement();
            launchElement.remove();
            launchElement.insertAfter(wizardElement);
        }
    }

    /**
     * @return Returns the isCancelable.
     */
    protected boolean isCancelable() {
        return isCancelable;
    }

    /**
     * @param isCancelable
     *            The isCancelable to set.
     */
    protected void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    /**
     * Renders opening of wizard step.
     * 
     * @param xfGroupStyles
     *            styles of xf:group element.
     */
    public void renderOpenStep(VolantisProtocol protocol, Styles xfGroupStyles)
            throws ProtocolException {

        MutablePropertyValues styleValues = xfGroupStyles.getPropertyValues();

        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            styleValues.setSpecifiedValue(StylePropertyDetails.MCS_BREAK_AFTER,
                    StyleKeywords.ALWAYS);
            return;
        } else {
            // Because form fragmentation and wizard rendering are two
            // mutually exclusive features, disable form fragmentation
            // by clearing the 'mcs-break-after' style.
            styleValues.setSpecifiedValue(StylePropertyDetails.MCS_BREAK_AFTER,
                    null);
        }

        getCurrentStepRenderer(false).renderOpen(xfGroupStyles);
    }

    /**
     * Renders closing of current wizard step.
     */
    public void renderCloseStep(VolantisProtocol protocol)
            throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            // Fallback behaviour
            return;
        }
        getCurrentStepRenderer(false).renderClose();
    }

    /**
     * Renders registration of the simple validator enclosed within current
     * wizard step. If we are not currently processing wizard step, this method
     * does nothing.
     * 
     * @param protocol
     *            The protocol to use for rendering
     * @param id
     *            The ID of the simple validator to register
     * @throws ProtocolException
     */
    public void renderRegisterSimpleValidator(VolantisProtocol protocol,
            String id) throws ProtocolException {
        WizardStepRenderer stepRenderer = getCurrentStepRenderer(true);

        if (stepRenderer != null) {
            stepRenderer.renderRegisterSimpleValidator(id);
        }
    }

    /**
     * Renders registration of the multiple validator enclosed within current
     * wizard step. If we are not currently processing wizard step, this method
     * does nothing.
     * 
     * @param protocol
     *            The protocol to use for rendering
     * @param id
     *            The ID of the simple validator to register
     * @throws ProtocolException
     */
    public void renderRegisterMultipleValidator(VolantisProtocol protocol,
            String id) throws ProtocolException {
        WizardStepRenderer stepRenderer = getCurrentStepRenderer(true);

        if (stepRenderer != null) {
            stepRenderer.renderRegisterMultipleValidator(id);
        }
    }

    /**
     * Returns stack with step renderers. If stack does not exist yet, it's
     * created.
     * 
     * @return stack with step renderers
     */
    private Stack getStepRenderersStack() {
        if (stepRenderersStack == null) {
            stepRenderersStack = new Stack();
        }
        return stepRenderersStack;
    }

    /**
     * Creates new instance of WizardStepRenderer.
     * 
     * @param protocol
     *            protocol to use for rendering
     * @param xfGroupAttributes
     *            attributes of 'xf:group' element, rendered as wizard step
     * @param wizardElement
     *            rendered element enclosing the wizard step.
     * @return created wizard step renderer
     */
    private WizardStepRenderer createStepRenderer(VolantisProtocol protocol,
            Element wizardElement, Map actionsButtonsMapping) {
        return new WizardStepRenderer(this, protocol, wizardElement,
                actionsButtonsMapping);
    }

    /**
     * Pushes the wizard step renderer, so that it becomes current.
     * 
     * @param renderer
     *            the wizard step renderer to become current
     */
    private void pushStepRenderer(WizardStepRenderer renderer) {
        getStepRenderersStack().push(renderer);
    }

    /**
     * Pops current wizard step renderer.
     * 
     * @throws IllegalStateException
     *             if there's no renderer to pop.
     */
    private void popStepRenderer() {
        try {
            getStepRenderersStack().pop();
        } catch (EmptyStackException e) {
            throw new IllegalStateException("No current step renderer.");
        }
    }

    /**
     * Returns renderer used to render current wizard step.
     * 
     * @param safe
     *            If true, then the exception is not thrown.
     * @return the wizard step renderer
     * @throws IllegalStateException,
     *             if there's no current renderer, unless <code>safe</code> is
     *             true.
     */
    private WizardStepRenderer getCurrentStepRenderer(boolean safe) {
        WizardStepRenderer stepRenderer = null;

        try {
            stepRenderer = (WizardStepRenderer) getStepRenderersStack().peek();
        } catch (EmptyStackException e) {
            if (!safe) {
                throw new IllegalStateException("No current step renderer.");
            }
        }

        return stepRenderer;
    }

    // javadoc inherited
    protected ActionName[] getSupportedActionNames() {
        return WIZARD_ACTIONS;
    }

    /**
     * Return internalButtons map.
     * 
     * @return
     */
    protected Map getActionsToInternalButtonsMap() {
        return this.actionsToInternalButtonsMap;
    }

    /**
     * For each internal button create buttonAttribute that contains only
     * actionReference that is associated with this button.
     * 
     * @param widgetId
     */
    private void initializeActionsToInternalButtonsMap(String widgetId) {
        for (int i = 0; i < INTERNAL_ACTIONS.length; i++) {
            ActionName action = INTERNAL_ACTIONS[i];
            List actionList = new ArrayList();
            actionList.add(action);
            ButtonAttributes buttonAttributes = new ButtonAttributes();
            buttonAttributes.setActionReference(new ActionReferenceImpl(widgetId, actionList));
            actionsToInternalButtonsMap.put(action, buttonAttributes);
        }
    }

    /**
     * Method for providing steps Id to Wizard renderer. Now steps list will be
     * provided as array of steps instead of invoking registerStep on Wizard
     * class every time we start to render wizard step.
     */
    public void pushStepIdOnStack(String stepId) {
        this.stepsIdStack.push(stepId);
    }

    /**
     * Collect all ids from stepsIdStack and transforms it into JavaScript array
     * 
     * @return
     */
    private String createStepsIdJSArray() {
        StringBuffer stepsJSArray = new StringBuffer();

        // creating array from the end
        stepsJSArray.append("]");
        while (!this.stepsIdStack.isEmpty()) {
            String stepId = "'" + (String) this.stepsIdStack.pop() + "'";
            stepsJSArray.insert(0, stepId);
            if (!this.stepsIdStack.isEmpty()) {
                stepsJSArray.insert(0, ",");
            }
        }
        // enclosing brace
        stepsJSArray.insert(0, "[");
        return stepsJSArray.toString();
    }

    private String createCancelDialog(VolantisProtocol protocol) throws ProtocolException {
        // prepare renderer and attributes for popup widget
        PopupAttributes popupAttributes = new PopupAttributes();
        // set default styles for popup
        popupAttributes.setStyles(createCancelDialogStyles(protocol));
        // open popup
        renderWidgetOpen(protocol, popupAttributes);

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        // set default message content            
        openDivElement(protocol);
        currentBuffer.writeText(messageLocalizer
                .format("widget-wizard-cancel-dialog"));
        closeDivElement(protocol);

        // Open div for buttons
        DivAttributes divAttrs = new DivAttributes();
        divAttrs.setStyles(createCancelDialogButtonsDivStyles(protocol));
        openDivElement(protocol, divAttrs);
        
        // add first dialog button - yes
        DismissAttributes dismissAttributes = new DismissAttributes();
        dismissAttributes.setType("yes");
        dismissAttributes.setDismissableId(popupAttributes.getId());            
        // set default styles for dismis type yes
        dismissAttributes.setStyles(createCancelDialogButtonStyles(protocol));

        renderWidgetOpen(protocol, dismissAttributes);
        // TODO: should be localized
        currentBuffer.writeText("yes");
        renderWidgetClose(protocol, dismissAttributes);
        addUsedWidgetId(dismissAttributes.getId());

        // add second dialog button - no
        dismissAttributes = new DismissAttributes();
        dismissAttributes.setType("no");
        dismissAttributes.setDismissableId(popupAttributes.getId());
        // set default styles for dismis type no
        dismissAttributes.setStyles(createCancelDialogButtonStyles(protocol));

        renderWidgetOpen(protocol, dismissAttributes);
        // TODO: should be localized
        currentBuffer.writeText("no");
        renderWidgetClose(protocol, dismissAttributes);
        addUsedWidgetId(dismissAttributes.getId());

        closeDivElement(protocol);
        
        // close popup
        renderWidgetClose(protocol, popupAttributes);        
        
        return popupAttributes.getId();
    }
    
    private Styles createCancelDialogStyles(VolantisProtocol protocol) {

        Styles styles = StylingFactory.getDefaultInstance()
            .createInheritedStyles(
                    protocol.getMarinerPageContext()
                            .getStylingEngine().getStyles(),
                    DisplayKeywords.NONE);

        MutablePropertyValues values = styles.getPropertyValues();
        StyleValueFactory svf = StyleValueFactory.getDefaultInstance();
        
        values.setComputedAndSpecifiedValue(
                StylePropertyDetails.POSITION, StyleKeywords.FIXED);
        values.setComputedAndSpecifiedValue(
                StylePropertyDetails.TOP, svf.getLength(null, 0, LengthUnit.PX));
        values.setComputedAndSpecifiedValue(
                StylePropertyDetails.LEFT, svf.getLength(null, 0, LengthUnit.PX));
        values.setComputedAndSpecifiedValue(
                StylePropertyDetails.BACKGROUND_COLOR, StyleColorNames.WHITE);        
        
        Iterator i = Arrays.asList(StyleShorthands.MARGIN.getStandardProperties()).iterator();
        while (i.hasNext()) {
            StyleProperty property = (StyleProperty)i.next();
            values.setComputedAndSpecifiedValue(property, svf.getLength(null, 5, LengthUnit.PX));
        }
        i = Arrays.asList(StyleShorthands.PADDING.getStandardProperties()).iterator();
        while (i.hasNext()) {
            StyleProperty property = (StyleProperty)i.next();
            values.setComputedAndSpecifiedValue(property, svf.getLength(null, 0.5, LengthUnit.EM));
        }
        i = Arrays.asList(StyleShorthands.BORDER_WIDTH.getStandardProperties()).iterator();
        while (i.hasNext()) {
            StyleProperty property = (StyleProperty)i.next();
            values.setComputedAndSpecifiedValue(property, svf.getLength(null, 1, LengthUnit.PX));
        }
        i = Arrays.asList(StyleShorthands.BORDER_STYLE.getStandardProperties()).iterator();
        while (i.hasNext()) {
            StyleProperty property = (StyleProperty)i.next();
            values.setComputedAndSpecifiedValue(property, StyleKeywords.SOLID);
        }
        i = Arrays.asList(StyleShorthands.BORDER_COLOR.getStandardProperties()).iterator();
        while (i.hasNext()) {
            StyleProperty property = (StyleProperty)i.next();
            values.setComputedAndSpecifiedValue(property, StyleColorNames.BLACK);
        }
        
        return styles;
    }

    private Styles createCancelDialogButtonStyles(VolantisProtocol protocol) {
        Styles styles = StylingFactory.getDefaultInstance()
            .createInheritedStyles(
                protocol.getMarinerPageContext()
                        .getStylingEngine().getStyles(),
                DisplayKeywords.INLINE);

        styles.getPropertyValues().setComputedAndSpecifiedValue(
            StylePropertyDetails.MCS_BUTTON_STYLE,
            StyleKeywords.NATIVE);
        
        return styles;
    }

    private Styles createCancelDialogButtonsDivStyles(VolantisProtocol protocol) {
        Styles styles = StylingFactory.getDefaultInstance()
            .createInheritedStyles(
                protocol.getMarinerPageContext()
                        .getStylingEngine().getStyles(),
                DisplayKeywords.BLOCK);

        styles.getPropertyValues().setComputedAndSpecifiedValue(
            StylePropertyDetails.TEXT_ALIGN,
            StyleKeywords.CENTER);
        
        return styles;
    }

	/**
	 * @return the validatorsBuffer
	 */
	public StringBuffer getValidatorsBuffer() {
		return validatorsBuffer;
	}
}
