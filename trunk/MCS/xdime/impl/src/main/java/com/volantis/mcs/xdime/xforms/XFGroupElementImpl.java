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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.renderers.FieldExpanderWidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.WizardRenderer;
import com.volantis.mcs.xdime.ElementOutputState;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.FieldExpanderElement;
import com.volantis.mcs.xdime.widgets.WizardElement;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xdime.xforms.model.XFormModel;
import com.volantis.styling.Styles;

/**
 * Describes how an XForm group element should be processed.
 * 
 * @mock.generate
 */
public class XFGroupElementImpl extends StyledXFormsElement {

    /**
     * The model in which the controls contained within this group should
     * appear.
     */
    protected XFormModel containingModel;
    
    /**
     * Wizard Renderer suitable for the current protocol 
     * This attribute is not-null only, if this XFormGroup 
     * is rendered inside Wizard Widget.
     */
    private WizardRenderer wizardRenderer = null;

    /**
     * Field Expander renderer suitable for the current protocol.
     * Not null if this group is under control of Field Expander
     */
    private FieldExpanderWidgetRenderer fieldExpanderRenderer = null;
    
    /**
     * Initialize a new instance.
     * @param context
     */
    public XFGroupElementImpl(XDIMEContextInternal context) {
        super(XFormElements.GROUP, context);
    }    
    
    // Javadoc inherited
    public XDIMEResult exprElementStart(XDIMEContextInternal context,
            com.volantis.mcs.xdime.XDIMEAttributes attributes) throws XDIMEException {

        XDIMEResult result = super.exprElementStart(context, attributes);

        // Figure out which model this group refers to.
        XFormBuilder xFormBuilder = context.getXFormBuilder();
        containingModel = xFormBuilder.registerGroup(attributes);

        // Update the form fragmentation state if required.
        // Get the styles which determine how this group should be displayed.
        final Styles styles = getStylingEngine(context).getStyles();
        containingModel.pushGroup(styles, getPageContext(context));

        // Suppress the group output if the containing model is currently
        // inactive.
        boolean suppressed = !containingModel.isActive();

        // Update the current (group) element's output state and store it in
        // the model (so that we can update the state if necessary).
        ElementOutputState state = getOutputState();
        state.setIsInactiveGroup(suppressed);
        containingModel.pushElementOutputState(state);

        return result;
    }

    // Javadoc inherited
    public XDIMEResult exprElementEnd(XDIMEContextInternal context)
            throws XDIMEException {

        // Get the state before popping the current group.
        boolean active = containingModel.isActive();

        // Return the model to the state before processing this group.
        containingModel.popElementOutputState();
        containingModel.popGroup();

        // Check if the state has changed - can only happen at most once each
        // way per parse.
        boolean activeAfter = containingModel.isActive();

        if (active && !activeAfter) {
            // The rest of this group should not be output.
            containingModel.updateAllGroups(true);
        } else if (!active && activeAfter) {
            // The rest of this group should be output.
            containingModel.updateAllGroups(false);
        }

        return super.exprElementEnd(context);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, com.volantis.mcs.xdime.XDIMEAttributes attributes)
            throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);
            
        // If this group is controlled by Field Expander, 
        // render the markup required by the widget
        if (parent instanceof FieldExpanderElement) {
            try {
                fieldExpanderRenderer = getFieldExpanderRenderer(context);
                if (null != fieldExpanderRenderer) {
                    fieldExpanderRenderer.renderXFGroupOpen(getProtocol(context), protocolAttributes);
                }
            } catch (ProtocolException e) {
                throw new XDIMEException(e);
            }        
            
            // Following code is part of Wizard Widget handling.
            // First, check whether this element is directly enclosed 
            // within the Wizard element.
        } else if (parent instanceof WizardElement) {
            // This element is directly enclosed by 'wizard' element. 
            // Get the Widget module from protocol...
            
            WidgetModule widgetModule = protocol.getWidgetModule();
            
            if (widgetModule != null) {
                // Convert all ProtocolException exceptions to XDIMEException.
                try {
                    // Get Wizard renderer instance...
                wizardRenderer = (WizardRenderer) widgetModule.getWizardRenderer();
                    
                    if (wizardRenderer != null) {
                        // Get styles values for this element...
                        Styles styles = getStylingEngine(context).getStyles();
                        
                        //  Now, render opening of the wizard step.
                        wizardRenderer.renderOpenStep(protocol, styles);
                    }
                } catch (ProtocolException e) {
                    throw new XDIMEException(e);
                }
            }
        }        
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);
        // If this group is controlled by Field Expander, 
        // render the markup required by the widget
        if (null != fieldExpanderRenderer) {
            try {
                fieldExpanderRenderer.renderXFGroupClose(getProtocol(context), protocolAttributes);                
            } catch (ProtocolException e) {
                throw new XDIMEException(e);
                }
            // Following code is part of Wizard Widget handling.
        } else if (null != wizardRenderer) {
                try {
                    wizardRenderer.renderCloseStep(protocol);
                } catch (ProtocolException e) {
                    throw new XDIMEException(e);
                }
        }    
    }
}
