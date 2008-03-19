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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFImplicitElement.java,v 1.6 2003/04/17 17:20:50 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 19-Feb-02    Paul            VBM:2001100102 - Create and initialise a
 *                              FieldDescriptor object.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 17-Apr-03    Chris W         VBM:2003031909 - elementStart now handles
 *                              clientVariableName attribute.
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
import com.volantis.mcs.papi.XFImplicitAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The xfimplicit element.
 */
public class XFImplicitElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    XFImplicitElementImpl.class);

    /**
     * Create a new <code>XFImplicitElement</code>.
     */
    public XFImplicitElementImpl() {
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        XFImplicitAttributes attributes = (XFImplicitAttributes) papiAttributes;

        // Create a new protocol attributes object every time, as this element
        // could be reused before the attributes have actually been finished with
        // by the protocol.
        com.volantis.mcs.protocols.XFImplicitAttributes pattributes
                = new com.volantis.mcs.protocols.XFImplicitAttributes();

        // Get the enclosing form element's attributes.
        XFFormElementImpl formElement
                = (XFFormElementImpl) pageContext.getCurrentElement();
        XFFormAttributes formAttributes = formElement.getProtocolAttributes();

        // Get the enclosing form element's descriptor.
        FormDescriptor formDescriptor = formElement.getFormDescriptor();

        // Add a reference back to the form attributes.
        pattributes.setFormAttributes(formAttributes);
        pattributes.setFormData(formAttributes.getFormData());

        // Set the name.
        String name = attributes.getName();
        pattributes.setName(name);

        // Set the value.
        String value = attributes.getValue();
        pattributes.setValue(value);

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Set the client variable name
        TextAssetReference reference = resolver.resolveQuotedTextExpression(
                attributes.getClientVariableName());
        String clientVariableName = getPlainText(reference);

        // One of value and clientVariableName must be specified.
        if (value == null && clientVariableName == null) {
            throw new PAPIException(exceptionLocalizer.format(
                    "xfimplicit-value-or-client-variable-name"));
        }

        pattributes.setClientVariableName(clientVariableName);

        // Create the field descriptor.
        FieldDescriptor fieldDescriptor = new FieldDescriptor();
        fieldDescriptor.setName(name);
        fieldDescriptor.setType(ImplicitFieldType.getSingleton());
        fieldDescriptor.setInitialValue(value);

        // Add a reference to the field descriptor into the attributes.
        pattributes.setFieldDescriptor(fieldDescriptor);

        // Add the attributes to the list.
        formAttributes.addField(pattributes);

        // Add the field descriptor to the list.
        formDescriptor.addField(fieldDescriptor);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return CONTINUE_PROCESSING;
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

 18-May-05	8196/3	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
