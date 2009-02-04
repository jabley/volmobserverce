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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/FieldDescriptor.java,v 1.5 2002/04/05 15:01:22 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to encapsulate
 *                              information about the fields used in a form.
 * 04-Mar-02    Paul            VBM:2001101803 - Removed the handler property.
 * 08-Mar-02    Paul            VBM:2002030607 - Removed commented code.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 05-Apr-02    Ian             VBM:2002030606 - Added fieldTag property and
 *                              setFieldTag() and getFieldTag() methods.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.synergetics.ObjectHelper;

/**
 * This class encapsulates the information about a form field which is needed
 * to ensure consistency of behaviour across the different protocols. It does
 * not contain any protocol specific information however.
 */
public class FieldDescriptor {

    /**
     * The name of this field.
     */
    private String name;

    /**
     * The field type.
     */
    private FieldType type;

    /**
     * The initial value.
     */
    private String initialValue;

    /**
     * General purpose field tag
     */
    private String fieldTag;

    /**
     * Create a new <code>FieldDescriptor</code>.
     */
    public FieldDescriptor() {
    }

    /**
     * Set the value of the name property.
     *
     * @param name The new value of the name property.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of the name property.
     *
     * @return The value of the name property.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of the type property.
     *
     * @param type The new value of the type property.
     */
    public void setType(FieldType type) {
        this.type = type;
    }

    /**
     * Get the value of the type property.
     *
     * @return The value of the type property.
     */
    public FieldType getType() {
        return type;
    }

    /**
     * Set the value of the initial value property.
     *
     * @param initialValue The new value of the initial value property.
     */
    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * Get the value of the initial value property.
     *
     * @return The value of the initial value property.
     */
    public String getInitialValue() {
        return initialValue;
    }

    // Javadoc inherited from super class.
    public boolean equals(Object object) {
        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        FieldDescriptor o = (FieldDescriptor) object;

        return ObjectHelper.equals(name, o.name)
                && ObjectHelper.equals(type, o.type)
                && ObjectHelper.equals(initialValue, o.initialValue);
    }

    // Javadoc inherited from super class.
    public int hashCode() {
        throw new UnsupportedOperationException
                ("This object cannot be added to a hash map");
    }

    // Javadoc inherited from super class.
    public String toString() {
        return getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(this))
                + " [" + paramString() + "]";
    }

    /**
     * Set the general purpose field tag.
     *
     * @param value The value for the field tag.
     */
    public void setFieldTag(String value) {
        fieldTag = value;
    }

    /**
     * Get the value of the field tag..
     *
     * @return The value for the field tag.
     */
    public String getFieldTag() {
        return fieldTag;
    }

    /**
     * Return a String representation of the state of the object.
     *
     * @return The String representation of the state of the object.
     */
    private String paramString() {
        return name + "," + type + "," + initialValue;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
