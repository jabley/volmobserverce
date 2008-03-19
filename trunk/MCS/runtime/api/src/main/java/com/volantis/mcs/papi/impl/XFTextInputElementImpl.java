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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFTextInputElement.java,v 1.11 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 07-Dec-01    Paul            VBM:2001120703 - Use the default type of
 *                              "text" if it has not been set.
 * 11-Feb-02    Paul            VBM:2001122105 - Added call to initialise
 *                              the field event attributes.
 * 19-Feb-02    Paul            VBM:2001100102 - Create and initialise a
 *                              FieldDescriptor object.
 * 04-Mar-02    Paul            VBM:2001101803 - Moved initialisation of the
 *                              field descriptor into a separate method.
 * 14-Mar-02    Steve           VBM:2002021119 - Handle the inputMode attribute
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 15-Aug-02    Paul            VBM:2002081421 - Updated to match changes made
 *                              in XFFormFieldElement.
 * 03-Mar-03    Byron           VBM:2003022813 - Modified elementStartImpl call
 *                              handleMarinerExpression before setting the
 *                              initial value.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for 
 *                              ProtocolException that throws PAPIException.
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false 
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.TextInputFieldType;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.properties.StyleProperty;

/**
 * The xftextinput element.
 */
public class XFTextInputElementImpl
        extends XFFormFieldElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XFTextInputElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(XFTextInputElementImpl.class);


    private static final StyleProperty MCS_INPUT_FORMAT =
            StylePropertyDetails.MCS_INPUT_FORMAT;

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    /**
     * Create a new <code>XFTextInputElement</code>.
     */
    public XFTextInputElementImpl() {
        super(false);
    }

    // Javadoc inherited from super class.
    String getInitialValue(
            MarinerPageContext pageContext,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        XFTextInputAttributes attributes =
                (XFTextInputAttributes) papiAttributes;

        return attributes.getInitial();
    }

    // Javadoc inherited from super class.
    FieldType getFieldType(PAPIAttributes papiAttributes) {

        return TextInputFieldType.getSingleton();
    }

    protected int elementStartImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        XFTextInputAttributes attributes =
                (XFTextInputAttributes) papiAttributes;

        // Create a new protocol attributes object every time, as this element
        // could be reused before the attributes have actually been finished with
        // by the protocol.
        com.volantis.mcs.protocols.XFTextInputAttributes pattributes
                = new com.volantis.mcs.protocols.XFTextInputAttributes();

        // Initialise the attributes specific to this field.
        TextAssetReference object;
        String value;

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Process the errmsg as a mariner expression.
        object = resolver.resolveQuotedTextExpression(
                attributes.getErrmsg());
        pattributes.setErrmsg(object);

        // Set the initial value attribute.
        object = resolver.resolveQuotedTextExpression(attributes.getInitial());
        pattributes.setInitial(object);

        // Set the max length attribute.
        value = attributes.getMaxLength();
        if (value != null) {
            pattributes.setMaxLength(Integer.parseInt(value));
        }

        // Set the type attribute, the default is "text".
        value = attributes.getType();
        if (value == null) {
            value = "text";
        }
        pattributes.setType(value);

        // Set the inputMode attribute
        value = attributes.getInputMode();
        if (value != null) {
            pattributes.setInputMode(value);
        }

        // Process the validate value as a mariner expression and store the
        // resulting object in the styles. The value if specified always
        // overrides the styles.
        StyleValue validate = resolver.resolveQuotedTextExpressionAsStyleValue(
                attributes.getValidate());
        if (validate != null) {
            MutablePropertyValues propertyValues = styles.getPropertyValues();
            propertyValues.setComputedAndSpecifiedValue(
                    MCS_INPUT_FORMAT, validate);
        }

        // Initialise form field event attributes.
        PAPIInternals.initialiseFieldEventAttributes(pageContext, attributes,
                pattributes);

        // Do the rest of the field processing.
        try {
            doField(pageContext, attributes, pattributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", pattributes.getTagName(), e);
            throw new PAPIException(
                    exceptionLocalizer.format("rendering-error",
                            pattributes.getTagName()),
                    e);
        }

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Feb-04	2789/5	tony	VBM:2004012601 rework changes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
