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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/FormDescriptor.java,v 1.2 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to encapsulate the
 *                              information about a form.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.synergetics.ObjectHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the information about a form which is needed
 * to ensure consistency of behaviour across the different protocols.
 *
 * @mock.generate
 */
public class FormDescriptor {

    /**
     * The name of the form.
     */
    private String name;

    /**
     * The list of the field descriptors.
     */
    private final List fields;

    /**
     * Create a new <code>FormDescriptor</code>.
     */
    public FormDescriptor() {
        fields = new ArrayList();
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
     * Add a field to the list.
     *
     * @param field The field to add.
     */
    public void addField(FieldDescriptor field) {
        fields.add(field);
    }

    /**
     * Return the list of <code>FieldDescriptor</code> objects.
     *
     * @return The list of <code>FieldDescriptor</code> objects.
     */
    public List getFields() {
        return fields;
    }

    // Javadoc inherited from super class.
    public boolean equals(Object object) {
        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        FormDescriptor o = (FormDescriptor) object;

        return ObjectHelper.equals(name, o.name)/*
      && ObjectHelper.equals (fields, o.fields)*/;
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
     * Return a String representation of the state of the object.
     *
     * @return The String representation of the state of the object.
     */
    private String paramString() {
        return name + "," + fields;
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
