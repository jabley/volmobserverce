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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.forms.ActionFieldType;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.ActionReference;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.ButtonScriptHandlerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WizardAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.WizardRenderer;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.events.XFormsFocusEvent;
import com.volantis.mcs.xdime.widgets.WizardElement;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xdime.xforms.model.XFormModel;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @mock.generate
 */
public class XFSubmitElementImpl extends XFormsControlElement {

    /**
     * The WizardElement enclosing for this submit element. 
     */
    private WizardElement wizard = null;    
    
    public static final String TAG_NAME = "submit";
    
    public static final String DEFAULT_SUBMIT_CAPTION = "OK";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    XFSubmitElementImpl.class);

    /**
     * Initialize a new instance.
     * @param context
     */
    public XFSubmitElementImpl(XDIMEContextInternal context) {
        super(XFormElements.SUBMIT, context);

        protocolAttributes = new XFActionAttributes();

        // Add xfsubmit specific events.
        new XFormsFocusEvent().registerEvents(eventMapper);
    }

    // javadoc inherited
    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        // Iterate the stack of XDIME elements from top to bottom,
        // looking for first wizard element. 
        Stack stack = ((XDIMEContextImpl)context).getStack();
        
        // Start from the top of the stack.
        int elementIndex = stack.size() - 1;
        
        while (elementIndex >= 0) {
            Object element = stack.elementAt(elementIndex);
           
            // Check, if we got launchable element.
            if (element instanceof WizardElement) {
                wizard = (WizardElement)element;
                
                // We found so break
                break;
            }
            
            elementIndex--;
        }
        // if we found wizard it means that this submit button should be 
        // wrapped (on client-side) with javascritp that controls button
        // behaviour. In this way standard submit might be used by widgets.
        if(null != wizard){
            ButtonScriptHandlerAttributes buttonAttributes = new ButtonScriptHandlerAttributes();
            
            VolantisProtocol protocol = getProtocol(context);
            WidgetModule widgetModule = protocol.getWidgetModule();
            
            try {
                WidgetRenderer renderer =
                    widgetModule.getWidgetRenderer(buttonAttributes);
                renderer.renderOpen(protocol, buttonAttributes);
            } catch (ProtocolException e) {
                throw new XDIMEException(e);
            }
        } 
        return super.callOpenOnProtocol(context, attributes);
    }



    // javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {

        // Iterate the stack of XDIME elements from top to bottom,
        // looking for first wizard element. 
        Stack stack = ((XDIMEContextImpl)context).getStack();
        
        // Start from the top of the stack.
        int elementIndex = stack.size() - 1;
        
        while (elementIndex >= 0) {
            Object element = stack.elementAt(elementIndex);
           
            // Check, if we got launchable element.
            if (element instanceof WizardElement) {
                wizard = (WizardElement)element;
                
                // We found so break
                break;
            }
            
            elementIndex--;
        }

        TextAssetReference caption =
            ((XFFormFieldAttributes)protocolAttributes).getCaption();

        // when we found wizard we have to associate this submit button
        // with complete action on wizard. 
        if(null != wizard){
            VolantisProtocol protocol = getProtocol(context);
            ButtonScriptHandlerAttributes buttonAttributes = new ButtonScriptHandlerAttributes();
            
            String submitId = protocolAttributes.getId();
            if(null == submitId){
                submitId = protocol.getMarinerPageContext().generateUniqueFCID();
                protocolAttributes.setId(submitId);
            }
            
            buttonAttributes.setId(submitId);

            //if xf:label element was not specified for xf:submit element
            //then set label to the default value
            if (null == caption) {
                ((XFFormFieldAttributes)protocolAttributes)
                    .setCaption(new LiteralTextAssetReference(
                            DEFAULT_SUBMIT_CAPTION));
            }
            
            // wizard is embracing this element
            // wizard id is needed for proper action reference desciption
            WizardAttributes wizardAttrs = (WizardAttributes)wizard.getProtocolAttributes();
            final String wizardId = wizardAttrs.getId();
            
            // associate button with complete action
            final List memberList = new ArrayList();
            memberList.add(ActionName.COMPLETE);
            buttonAttributes.setActionReference(new ActionReference() {
                public String getWidgetId() {return wizardId;}
                public List getMemberNames() {return memberList;}
            });
            
            WidgetModule widgetModule = protocol.getWidgetModule();
            
            try {
                WidgetRenderer renderer =
                    widgetModule.getWidgetRenderer(buttonAttributes);
                renderer.renderClose(protocol, buttonAttributes);
                
                WizardRenderer wizardRenderer = (WizardRenderer)
                    widgetModule.getWidgetRenderer(wizardAttrs);
                              
                StringBuffer validatorsBuffer = wizardRenderer.getValidatorsBuffer();
                if(null != validatorsBuffer) {
                    validatorsBuffer.append("$('" + submitId + "').type='button';");
                }                
            } catch (ProtocolException e) {
                throw new XDIMEException(e);
            }
        }

        // If an action field is tagged to indicate that its query parameters
        // should contain an extra name value pair (used to indicate things
        // like which of two submit buttons was pressed to trigger the
        // submission) then HTMLActionFieldHandler appends this name value pair
        // using the name and initial value specified in the field descriptor.
        // If this submit element had a child setvalue element, then it
        // requires this name value pair and we should set the flag. We set the
        // flag to the caption string (for backwards compatibility with XDIME1
        // - there is no other reason to set it to this string).
        if (caption != null) {
            // We only want to map fields that have an initial value (i.e. had
            // a child setvalue element with either a value attribute or some
            // body content).
            if (fieldDescriptor.getInitialValue() != null) {
                String captionString =
                        caption.getText(TextEncoding.PLAIN);
                fieldDescriptor.setFieldTag(captionString);
                ((XFFormFieldAttributes)protocolAttributes).
                        setFieldDescriptor(fieldDescriptor);
            }
        }

        super.callCloseOnProtocol(context);
    }

    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // set the type of the XFAction to submit
        final String submitType = "submit";
        ((XFActionAttributes)protocolAttributes).setType(submitType);
    }

    // Javadoc inherited.
    protected FieldType getFieldType() {
        return ActionFieldType.getSingleton();
    }

    // Javadoc inherited.
    protected void setNameAttribute(XDIMEAttributes attributes)
            throws XDIMEException {

        // do nothing - submit elements do not specify the name 
//        final String submission = getAttribute(XDIMEAttribute.SUBMISSION,
//                attributes);
//
//        if (submission == null) {
//            throw new XDIMEException(exceptionLocalizer.format(
//                    "xdime-attribute-value-invalid",
//                    new String[]{submission, "submission"}));
//        }
//
//        ((XFFormFieldAttributes)protocolAttributes).setName(submission);
//        fieldDescriptor.setName(submission);
    }

    // Javadoc inherited.
    protected XFormModel register(XFormBuilder builder,
            XDIMEAttributes attributes) throws XDIMEException {

        final String submissionRef = getAttribute(XDIMEAttribute.SUBMISSION,
                attributes);
        String modelID = getAttribute(XDIMEAttribute.MODEL, attributes);

        // ensure that the submission being referenced exists.
        modelID = builder.getSubmission(submissionRef, modelID).
                getEnclosingModelID();
        return builder.getModel(modelID);
    }

    // Javadoc inherited.
    protected void setInitialValue(XFormBuilder builder, String ref,
            String modelID) throws XDIMEException {
        // do nothing - submit does not have an initial value
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/4	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
