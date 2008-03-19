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
package com.volantis.mcs.xdime;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class represents an XDIMEAttribute.
 * <p/>
 * It also defines constant implementations of the common valid XDIME attributes.
 * This is enforced by the private constructor.
 */
public class XDIMEAttribute {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(XDIMEAttribute.class);

    /**
     * The string attribute name, as defined by the XDIME-CP schema.
     */
    private String name;

    /**
     * The type of attribute, either CORE, HTTP, DISELECT or BINDING. These
     * types are taken from the XDIME-CP specification.
     */
    private int type;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param name attribute name, as defined by the XDIME-CP schema
     * @param type int which corresponds to one of the valid attribute types
     * (defined as constants).
     */
    private XDIMEAttribute(String name, int type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Return the type of the attribute i.e. one of CORE, HTTP, DISELECT or
     * BINDING.
     * @return int type of the attribute
     */
    public int getType() {
        return type;
    }

    /**
     * Determine if the attribute is one of the common attributes (as defined
     * by the XDIME-CP specification).
     * @return true if the attribute is one of the common attributes, false
     * otherwise
     */
    public boolean isCommonAttribute() {
        return (type & (CORE | HTTP | DISELECT)) != 0;
    }

    /**
     * Determine if the attribute is one of the binding attributes (as defined
     * by the XDIME-CP specification).
     *
     * @return true if the attribute is one of the binding attributes, false
     *         otherwise
     */
    public boolean isBindingAttribute() {
        return (type & BINDING) != 0;
    }

    /**
     * Verify that the value is valid for the particular attribute, and return
     * the validated value.
     *
     * @param value     the attribute value to be validated
     * @return validated value
     */
    public Object getValidatedValue(String value) throws XDIMEException {
        // do no validation by default
        return value;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }

    /**
     * Returns an XDIMEAttribute object holding the value of the specified
     * String.
     *
     * @param attString the String to be parsed.
     * @return the XDIMEAttribute which maps to the specified String
     * @throws XDIMEException if the specified String did not map to a valid
     *                        XDIMEAttribute
     */
    public static XDIMEAttribute valueOf(String attString)
            throws XDIMEException {

        XDIMEAttribute attribute = null;

        int length = ATTRIBUTES.length;
        for (int i = 0; i < length; i++) {
            if (ATTRIBUTES[i].toString().equalsIgnoreCase(attString)) {
                attribute = ATTRIBUTES[i];
            }
        }

        if (attribute == null) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "xdime-attribute-invalid", attString));
        }
        return attribute;
    }

    /************************************************************************/
    /*   The constant definitions representing the valid XDIMEAttributes.   */
    /************************************************************************/

    /**
     * The possible values for attribute type, as defined in the XDIME-CP
     * specification.
     */
    private static final int CORE = 1;
    private static final int HTTP = 2;
    private static final int DISELECT = 4;
    private static final int BINDING = 8;
    private static final int SUBMIT = 16;
    private static final int EMBEDDING = 32;
    private static final int PARAM = 64;

    /**
     * The identifier of this xform entity. Should be unique within a document.
     */
    public static final XDIMEAttribute ID = new XDIMEAttribute("id", CORE);

    /**
     * This XDIMEAttribute represents the style class which should be applied
     * to this xforms control.
     */
    public static final XDIMEAttribute STYLECLASS =
            new XDIMEAttribute("class", CORE);

    /**
     * This XDIMEAttribute represents the title which describes this control.
     */
    public static final XDIMEAttribute TITLE =
            new XDIMEAttribute("title", CORE);

    /**
     * This XDIMEAttribute represents the name of the parameter in an xforms
     * model that should hold the value of this control when the form is
     * submitted.
     */
    public static final XDIMEAttribute REF =
            new XDIMEAttribute("ref", BINDING);

    /**
     * This XDIMEAttribute represents the identifier of the model that the
     * referenced parameter exists in.
     */
    public static final XDIMEAttribute MODEL =
            new XDIMEAttribute("model", BINDING);

    /**
     * This XDIMEAttribute defines a conditional expression which determines
     * whether or not the element to which it applies should be processed.
     */
    public static final XDIMEAttribute EXPR =
            new XDIMEAttribute("expr", DISELECT);

    /**
     * This XDIMEAttribute represents the id that will be associated with the
     * element on which it is used. It is different from a standard id, in that
     * multiple elements can have the same value. This is valid because it
     * assumes that only one of the elements will be selected to appear in the
     * output.
     */
    public static final XDIMEAttribute SELID =
            new XDIMEAttribute("selid", DISELECT);

    /**
     * All elements in XHTML2 can have an href attribute which is the URI that
     * is followed when the element is activated.
     */
    public static final XDIMEAttribute HREF =
            new XDIMEAttribute("href", HTTP) {

                public Object getValidatedValue(String value)
                        throws XDIMEException {
                    try {
                        // validate by attempting to create a URI with value
                        return new URI(value);

                    } catch (URISyntaxException e) {
                        throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                            "xdime-attribute-value-invalid",
                            new Object[]{value, HREF.toString()}));
                    }
                }
            };

    /**
     * This XDIMEAttribute represents the src URI of an object element.
     */
    public static final XDIMEAttribute SRC =
        new XDIMEAttribute("src", EMBEDDING) {

            public Object getValidatedValue(String value)
                    throws XDIMEException {
                try {
                    // validate by attempting to create a URI with value
                    return new URI(value);

                } catch (URISyntaxException e) {
                    throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                        "xdime-attribute-value-invalid",
                        new Object[]{value, HREF.toString()}));
                }
            }

        };

    /**
     * This XDIMEAttribute represents the src type of an object element.
     */
    public static final XDIMEAttribute SRC_TYPE =
        new XDIMEAttribute("srctype", EMBEDDING);

    /**
     * This XDIMEAttribute represents the name attribute of a param element.
     */
    public static final XDIMEAttribute NAME =
        new XDIMEAttribute("name", PARAM);

    /**
     * This XDIMEAttribute represents the value attribute of a param element.
     */
    public static final XDIMEAttribute VALUE =
        new XDIMEAttribute("value", PARAM);

    /**
     * This attribute represents the single character that can be used to
     * activate the element.
     */
    public static final XDIMEAttribute ACCESS =
            new XDIMEAttribute("access", HTTP) {

                public Object getValidatedValue(String value)
                        throws XDIMEException {

                    if (value.length() == 1) {
                        return new Character(value.charAt(0));

                    } else {
                        throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                                "xdime-attribute-value-invalid",
                                new Object[]{value, ACCESS.toString()}));
                    }
                }
            };

    /**
     * The value of this attribute is the id of the submission element that
     * defines how the form is submitted.
     */
    public static final XDIMEAttribute SUBMISSION =
            new XDIMEAttribute("submission", SUBMIT);


    public static final XDIMEAttribute[] ATTRIBUTES = {
        ID, TITLE, STYLECLASS, HREF, ACCESS, EXPR, SELID, REF, MODEL,
        SRC, NAME, VALUE
    };
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9562/2	pabbott	VBM:2005092011 Add XHTML2 Object element

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
