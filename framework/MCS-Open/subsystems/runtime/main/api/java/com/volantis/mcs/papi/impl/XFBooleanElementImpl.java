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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFBooleanElement.java,v 1.8 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 11-Feb-02    Paul            VBM:2001122105 - Added call to initialise
 *                              the field event attributes.
 * 19-Feb-02    Paul            VBM:2001100102 - Create and initialise a
 *                              FieldDescriptor object.
 * 04-Mar-02    Paul            VBM:2001101803 - Moved initialisation of the
 *                              field descriptor into a separate method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 15-Aug-02    Paul            VBM:2002081421 - Updated to match changes made
 *                              in XFFormFieldElement.
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
import com.volantis.mcs.papi.XFBooleanAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.BooleanFieldType;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The xfboolean element.
 */
public class XFBooleanElementImpl
        extends XFFormFieldElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XFBooleanElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(XFBooleanElementImpl.class);

    /**
     * Create a new <code>XFBooleanElement</code>.
     */
    public XFBooleanElementImpl() {
        super(false);
    }

    // Javadoc inherited from super class.
    String getInitialValue(
            MarinerPageContext pageContext,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        XFBooleanAttributes attributes = (XFBooleanAttributes) papiAttributes;

        return attributes.getInitial();
    }

    // Javadoc inherited from super class.
    FieldType getFieldType(PAPIAttributes papiAttributes) {
        return BooleanFieldType.getSingleton();
    }

    // Javadoc inherited from super class.
    protected int elementStartImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        XFBooleanAttributes attributes = (XFBooleanAttributes) papiAttributes;

        // Create a new protocol attributes object every time, as this element
        // could be reused before the attributes have actually been finished with
        // by the protocol.
        com.volantis.mcs.protocols.XFBooleanAttributes pattributes
                = new com.volantis.mcs.protocols.XFBooleanAttributes();

        // Initialise the attributes specific to this field.
        TextAssetReference object;

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Process the errmsg as a mariner expression.
        object = resolver.resolveQuotedTextExpression(attributes.getErrmsg());
        pattributes.setErrmsg(object);

        // Process the false value as a mariner expression.
        object = resolver.resolveQuotedTextExpression(
                attributes.getFalseValue());
        pattributes.setFalseValues(object);

        // Set the initial value attribute.
        pattributes.setInitial(attributes.getInitial());

        // Process the true value as a mariner expression.
        object =
                resolver.resolveQuotedTextExpression(attributes.getTrueValue());
        pattributes.setTrueValues(object);

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

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
