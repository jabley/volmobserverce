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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/ElementGroupReference.java,v 1.1 2003/03/10 11:51:09 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Byron           VBM:2003030527 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.parser;

/**
 * This class is a reference to an ElementGroup and once the schema has
 * been parsed behaves just like the group to which it refers. It is not
 * valid to call any of the Group methods until the schema has been
 * parsed as the group to which it refers may not have been defined yet.
 * @author Byron Wild
 */
public class ElementGroupReference implements SchemaObject {

    /**
     * The <code>Scope</code> within which this reference is valid.
     */
    private Scope enclosingScope;

    /**
     * The name of this group reference.
     */
    private String name;

    /**
     * Create a new <code>ElementGroupReference</code>.
     *
     * @param name           The name of the group reference.
     * @param enclosingScope The <code>Scope</code> within which this object is
     *                       valid.
     */
    public ElementGroupReference(Scope enclosingScope, String name) {
        this.name = name;
        this.enclosingScope = enclosingScope;
    }

    /**
     * The elementGroup of this group reference.
     */
    private ElementGroup elementGroup;

    /**
     * Return the ElementGroup for this GroupReference
     *
     * @return      the ElementGroup for this GroupReference
     */
    public ElementGroup getElementGroup() {
        if (elementGroup == null) {
            elementGroup = enclosingScope.getElementGroup(name);
        }
        return elementGroup;
    }

    /**
     * Return the name of this Group Reference
     *
     * @return      Return the name of this Group Reference.
     */
    public String getName() {
        return name;
    }

    // Javadoc inherited from super class.
    public Scope getScope() {
        return enclosingScope;
    }

    // Javadoc inherited from super class.
    public String toString () {
      return super.toString () + " [" + paramString () + "]";
    }

    /**
     * Return a String representation of the state of the object.
     *
     * @return      The String representation of the state of the object.
     */
    protected String paramString () {
      return name + "," + elementGroup;
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
