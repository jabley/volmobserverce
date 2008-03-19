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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * The LPDMODOMElement provides a specialisation of ODOMElement that uses
 * LPDMODOMAttributes.
 */
public class LPDMODOMElement extends ODOMElement {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(LPDMODOMElement.class);

    /**
     * Initialises the new instance with default values.
     */
    protected LPDMODOMElement() {
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the element's name
     */
    public LPDMODOMElement(String name) {
        super(name);
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the element's name
     * @param namespace the element's namespace
     */
    public LPDMODOMElement(String name, Namespace namespace) {
        super(name, namespace);
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the element's name
     * @param uri       the element's namespace URI
     */
    public LPDMODOMElement(String name, String uri) {
        super(name, uri);
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the element's name
     * @param prefix    the element's namespace prefix
     * @param uri       the element's namespace URI
     */
    public LPDMODOMElement(String name, String prefix, String uri) {
        super(name, prefix, uri);
    }

    /**
     * Replaces the superclass implementation to ensure that an {@link
     * LPDMODOMAttribute} is added. This overloading attempts to treat the
     * set request as a value update if possible, i.e. it checks first
     * for the existence of an attribute of the given name, and, if it
     * exists, updates its value to the given value. In this case, a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#ATTRIBUTE_VALUE}
     * event will be fired. If the attribute does not exist, then a new
     * attribute is created from the parameter data and stored in the element.
     * In this case, a
     * {@link com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY}
     * event will be fired for the new attribute.
     *
     * Setting the value to the empty string causes the attribute to detach
     * itself from its parent (this LPDMODOMElement).
     *
     * @param name the name of the attribute to set
     * @param value the value of the attribute to set
     *
     * @return the updated element
     */
    public Element setAttribute(String name, String value) {

        // Look for the attribute with the name in the parameter
        Element result = this;
        final LPDMODOMAttribute existingAttribute =
                (LPDMODOMAttribute) super.getAttribute(name);

        // If the attribute exists, just update its value; otherwise,
        // construct a new attribute and set it in the element if there is a
        // value to set.
        if (existingAttribute != null) {
            existingAttribute.setValue(value);
        } else {
            if (!"".equals(value)) {
                // Only create a new attribute if there is a value.
                result = super.setAttribute(new LPDMODOMAttribute(name, value));
            }
        }
        return result;
    }

    /**
     * Identical in all respects to {@link #setAttribute(String,String)}
     * except that the given namespace is applied to the check for the
     * pre-existence of the attribute and (if applicable) the creation of
     * the new attribute.
     *
     * @param name the name of the attribute to set
     * @param value the value of the attribute to set
     * @param ns the attribute namespace
     *
     * @return the updated element
     */
    public Element setAttribute(String name, String value, Namespace ns) {

        // Look for the attribute with the name in the parameter
        Element result = this;
        final LPDMODOMAttribute existingAttribute =
                (LPDMODOMAttribute) super.getAttribute(name, ns);

        // If the attribute exists, just update its value; otherwise,
        // construct a new attribute and set it in the element if there is a
        // value to set.
        if (existingAttribute != null) {
            existingAttribute.setValue(value);
        } else {
            if (!"".equals(value)) {
                // Only create a new attribute if there is a value.
                result = super.setAttribute(
                        new LPDMODOMAttribute(name, value, ns));
            }
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 08-Sep-04	5251/17	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 08-Sep-04	5251/9	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 ===========================================================================
*/
