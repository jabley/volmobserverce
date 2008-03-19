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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 Parameter element object
 */
public class ParametersElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ParametersElement.class);

    /**
     * A filter that selects an object element.
     */
    private static final ElementUtils.Filter OBJECT_ELEMENT_FILTER = 
            new ElementUtils.Filter() {
                public boolean matches(XDIMEElementInternal element) {
                    return element instanceof ObjectElement;
                }
            };

    // Javadoc inherited.
    public ParametersElement(XDIMEContextInternal context) {
        super(XHTML2Elements.PARAM, UnstyledStrategy.STRATEGY, context);
    }

    /**
     * Validate that this element is being used correctly, but don't
     * bother to style it.
     * Don't call the super as this element is a no-op and thus has no
     * protocol attribute object, which styling requres.
     *
     * @param context       The XDIMEContext within which this element is
     *                      being processed.
     * @param attributes    The implementation of XDIMEAttributes which
     *                      contains the attributes specific to the
     *                      element.
     * @return PROCESS_ELEMENT_BODY
     * @throws XDIMEException if validation failed
     */
    public XDIMEResult exprElementStart(XDIMEContextInternal context,
                                        XDIMEAttributes attributes)
        throws XDIMEException {

        XDIMEResult result = super.exprElementStart(context, attributes);
        if (result != XDIMEResult.PROCESS_ELEMENT_BODY) {
            return result;
        }

        ObjectElement object = getContainingObject();

        String name = getValidName(attributes);

        Object value = getValidValue(attributes, name);

        object.addParameter(name, value);

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    /**
     * Find the object element that contains this parameter.
     * This is done by searching the element stack for the most recent
     * element of type ObjectElement.
     *
     * @return an istance of ObjectElement, will not be null
     * @throws XDIMEException if element not found
     */
    private ObjectElement getContainingObject()
        throws XDIMEException {
        ObjectElement foundObject = (ObjectElement)
                ElementUtils.getContainingElement(parent,
                        OBJECT_ELEMENT_FILTER);

        if (foundObject == null) {
            logger.error("internal-error-while-processing-element", getTagName());
            dumpElementStack(logger);

            throw new XDIMEException(exceptionLocalizer.format(
                "internal-error-while-processing-element", getTagName()));

        }
        return foundObject;
    }

    /**
     * Get the name attribute from the attribute set and validate it.
     *
     * @param attributes set of supplied attributes
     * @return the value of the name attribute
     * @throws XDIMEException if attribute is missing or contains an invalid
     *                        value
     */
    private String getValidName(XDIMEAttributes attributes)
        throws XDIMEException {

        String name = getRequiredAttribute(XDIMEAttribute.NAME, attributes);

        if (ObjectParameter.isNameValid(name) == false) {
            logger.error("invalid-parameter-name", name);
            throw new XDIMEException(exceptionLocalizer.format(
                "invalid-parameter-name", name));
        }

        return name;
    }

    /**
     * Given the name of the parameter get the value attribute and validate it.
     *
     * @param attributes set of supplied attributes
     * @param name valid paramter name, obtained from "name" attribute
     * @return Object representing the value of a validated "value" attribute
     * @throws XDIMEException if the value attribute was missing or invalid
     */
    private Object getValidValue(XDIMEAttributes attributes, String name)
        throws XDIMEException {

        String value = getRequiredAttribute(XDIMEAttribute.VALUE, attributes);

        Object valueObject = ObjectParameter.parseValue(name, value);
        if (valueObject == null) {
             logger.error("invalid-parameter-value", new Object[] {name, value});
             throw new XDIMEException(exceptionLocalizer.format(
                 "invalid-parameter-value", new Object[] {name, value}));
         }

        return valueObject;
    }



    /**
     * Get the named attribute from the XDIMEAttributes object. Throwing
     * an exception if it is not found.
     *
     * @param attributeName  Name of attribute to get
     * @param attributes Attribute set to get it from
     * @return the attribute
     * @throws XDIMEException thrown if attribute not found
     */
    private String getRequiredAttribute(XDIMEAttribute attributeName,
                                      XDIMEAttributes attributes)
        throws XDIMEException {
        String name = getAttribute(attributeName, attributes);
        if (name == null) {
           logger.error("missing-attribute",
                         new Object[] {getTagName(), attributeName});

            throw new XDIMEException(exceptionLocalizer.format(
                "missing-attribute",
                new Object[] {getTagName(), attributeName}));
        }

        return name;
    }

    // Javadoc inherited.
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
        //TODO Fill in body.
        return XDIMEResult.PROCESS_ELEMENT_BODY;        
    }

    // Javadoc inherited.
    protected void callCloseOnProtocol(XDIMEContextInternal context)
        throws XDIMEException {
        //TODO Fill in body.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 30-Sep-05	9562/3	pabbott	VBM:2005092011 Add XHTML2 Object element

 22-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
