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

import org.jdom.Attribute;
import org.jdom.Namespace;


/**
 * The LPDMODOMAttribute provides a specialisation of ODOMAttribute that does
 * not allow its value to be the empty string. If an attribute's value is set
 * to the empty string (which conceptually means that there is no value),
 * the attribute detaches itself from its parent.
 */
public class LPDMODOMAttribute extends ODOMAttribute {    
    /**
     * Initialises the new instance with default values.
     */
    protected LPDMODOMAttribute() {
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the attribute's name
     * @param value     the attribute's value
     * @param type      the attribute's type.
     */
    public LPDMODOMAttribute(String name, String value, int type) {
        super(name, value, type);
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the attribute's name
     * @param value     the attribute's value
     */
    public LPDMODOMAttribute(String name, String value) {
        super(name, value);
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the attribute's name
     * @param value     the attribute's value
     * @param type      the attribute's type. See {@link Attribute} for the set
     *                  of constants that should be used here
     * @param namespace the attribute's namespace
     */
    public LPDMODOMAttribute(String name,
                             String value,
                             int type,
                             Namespace namespace) {
        super(name, value, type, namespace);
    }

    /**
     * Initialises the new instance using the given parameters.
     *
     * @param name      the attribute's name
     * @param value     the attribute's value
     * @param namespace the attribute's namespace
     */
    public LPDMODOMAttribute(String name, String value, Namespace namespace) {
        super(name, value, namespace);
    }

    /**
     * Sets the value of this attribute. If the value is the empty string, this
     * attribute detaches itself from its parent and does not set its value.
     *
     * @note rest of javadoc inherited
     */
    public Attribute setValue(String value) {
        Attribute result = null;
        if (!"".equals(value)) {
            result = super.setValue(value);
        } else {
            this.detach();
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 08-Sep-04	5251/9	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 ===========================================================================
*/
