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
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.widgets.renderers.FieldExpanderWidgetRenderer;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.FieldExpanderElement;
import com.volantis.mcs.xdime.xforms.model.SIItem;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xdime.xforms.model.XFormModel;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.xdime.XDIMEAttributes;

/**
 * Abstract superclass for all XForms control elements.
 *
 * @mock.generate 
 */
public abstract class XFormsControlElement extends StyledXFormsElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(XFormsControlElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    XFormsControlElement.class);

    /**
     * The descriptor of the field.
     */
    protected FieldDescriptor fieldDescriptor = new FieldDescriptor();

    /**
     * Initialise.
     *
     * @param type The element type.
     * @param context
     */
    protected XFormsControlElement(
            ElementType type, XDIMEContextInternal context) {
        super(type, context);
    }

    /**
     * If this xforms element references a model item, then the value of the
     * item should be used as the initial value of the control.
     *
     * @param builder
     * @param ref
     * @param modelID
     */
    protected void setInitialValue(XFormBuilder builder, String ref,
            String modelID) throws XDIMEException {

        // deal with any referenced items
        SIItem item = builder.getItem(ref, modelID);
        // the item could be null because the control could legitimately
        // refer to an non-existent model item.
        if (item != null) {
            item.setIsReferenced();

            // the default implementation uses only the unprocessed value
            final String initialValue = item.getUnprocessedValue();

            if (initialValue != null) {
                ((XFFormFieldAttributes) protocolAttributes).setInitial(
                        initialValue);
                fieldDescriptor.setInitialValue(initialValue);
            }
        }
    }

    // Javadoc inherited.    
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
    

        XFFormFieldAttributes pattributes =
            (XFFormFieldAttributes) protocolAttributes;

        pattributes.setEntryContainerInstance(
                getPageContext(context).getCurrentContainerInstance());

        // If this element is controlled by Field Expander,
        // render the markup required by the widget
        if (parent instanceof FieldExpanderElement) {
            try {
                FieldExpanderWidgetRenderer renderer =
                        getFieldExpanderRenderer(context);
                if (null != renderer) {
                    renderer.renderXFControlOpen(getProtocol(context), protocolAttributes);
                }
            } catch (ProtocolException e) {
                throw new XDIMEException(e);
            }    
        }        
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }





    // Javadoc inherited.
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        // update the form and field descriptors
        XFormBuilder builder = context.getXFormBuilder();
        final XFFormFieldAttributes fieldAttributes =
                (XFFormFieldAttributes) protocolAttributes;
        builder.updateControl(fieldDescriptor, fieldAttributes);

        // effectively all xform controls are inline, so render it immediately
        FieldType fieldType = getFieldType();
        try {
            fieldType.doField(getProtocol(context), fieldAttributes);
        } catch (ProtocolException e) {
            throw new XDIMEException(e);
        }
        
        // If this element is controlled by Field Expander, 
        // render the markup required by the widget
        if (context.getCurrentElement() instanceof FieldExpanderElement) {
            try {
                FieldExpanderWidgetRenderer renderer = getFieldExpanderRenderer(context);
                if (null != renderer) {
                    renderer.renderXFControlClose(getProtocol(context), protocolAttributes);
                }
            } catch (ProtocolException e) {
                throw new XDIMEException(e);
            }    
        }        
    }

    /**
     * Get the field type of this xforms element.
     *
     * @return The {@link FieldType} of this xforms element.
     */
    protected abstract FieldType getFieldType();

    /**
     * Do any attribute initialisation that is common to all form controls.
     *
     * @param context
     * @param attributes xdime attributes of the xforms control
     */
    protected void initialiseAttributes(XDIMEContextInternal context,
            com.volantis.mcs.xdime.XDIMEAttributes attributes) throws XDIMEException {

        // initialise the attributes common to all XDIME elements
        initialiseCommonAttributes(context, attributes);

        // initialise the attributes common to all xforms control elements
        XFFormFieldAttributes pattributes =
                (XFFormFieldAttributes) protocolAttributes;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Saving partially complete form control");
        }

        // the title attribute is used as the prompt

        String title = getAttribute(com.volantis.mcs.xdime.XDIMEAttribute.TITLE, attributes);
        pattributes.setPrompt(new LiteralTextAssetReference(title));

        // Set the entry container (caption will be set by XFLabelElementImpl).
        pattributes.setEntryContainerInstance(
                getPageContext(context).getCurrentContainerInstance());

        // Set the initial value.
        final String ref = getAttribute(com.volantis.mcs.xdime.XDIMEAttribute.REF, attributes);
        final String modelID = getAttribute(com.volantis.mcs.xdime.XDIMEAttribute.MODEL, attributes);


        // Set the name attribute (use the value of the ref or submission
        // attribute depending on the element type)
        setNameAttribute(attributes);

        final XFormBuilder builder = context.getXFormBuilder();

        // determine the model that this control should appear in
        XFormModel model = builder.registerControl(attributes);
        pattributes.setContainingXFFormName(model.getID());

        setInitialValue(builder, ref, modelID);

        // populate the field descriptor

        fieldDescriptor.setType(getFieldType());
        // Add a reference to the field descriptor into the attributes.
        pattributes.setFieldDescriptor(fieldDescriptor);
        // Add a reference to the (incomplete) form data into the attributes.
        pattributes.setFormAttributes(model.getXFFormAttributes());
        pattributes.setFormData(model.getFormData());

        // do any element specific initialisation
        initialiseElementSpecificAttributes(context, attributes);
    }

    /**
     * Sets the name attribute to the value of the ref attribute. Other
     * controls may override this to set the name to the value of another
     * attribute (e.g. submit uses the value from a child setvalue element's
     * ref attribute).
     *
     * @param attributes    to use to determine the value of the name attribute
     * @throws XDIMEException if there was a problem setting the name attribute
     */
    protected void setNameAttribute(com.volantis.mcs.xdime.XDIMEAttributes attributes)
            throws XDIMEException {

        final String ref = getAttribute(com.volantis.mcs.xdime.XDIMEAttribute.REF, attributes);

        if (ref == null) {
            throw new XDIMEException(exceptionLocalizer.format(
                    "xdime-attribute-value-invalid", new String[]{ref, "ref"}));
        }

        ((XFFormFieldAttributes)protocolAttributes).setName(ref);
        fieldDescriptor.setName(ref);
    }

    // javadoc inherited
    public boolean isElementAtomic() {
        return true;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 10-Oct-05	9673/9	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/6	emma	VBM:2005092807 Adding tests for XForms emulation

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/4	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 09-Sep-05	9415/2	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
