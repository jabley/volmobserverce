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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/ElementGroup.java,v 1.1 2003/03/10 11:51:09 byron Exp $
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
 * This class contains the definition of an Group within the schema.
 *
 * @author Byron Wild
 */
public class ElementGroup implements SchemaObject {

    /**
     * The <code>Scope</code> within which this definition is valid.
     */
    private Scope enclosingScope;

    /**
     * The structure of attributes associated with this ElementGroup.
     */
    private AttributesStructure attributesStructure;

    /**
     * The name of this ElementGroup.
     */
    private String name;

    /**
     * The ComplexType associated with this ElementGroup.
     */
    private ComplexType complexType;

    /**
     * The ChoiceInfo  associated with this ElementGroup.
     */
    private ChoiceInfo choice;


    /**
     * Create a new <code>ElementGroup</code>.
     *
     * @param enclosingScope The <code>Scope</code> within which this object is
     *                       valid.
     * @param name           The name of the ElementGroup.
     */
    public ElementGroup(Scope enclosingScope, String name) {
        this.name = name;
        this.enclosingScope = enclosingScope;
    }

    /**
     * Set the <code>ComplexType</code> associated with this ElementGroup.
     *
     * @param complexType The <code>ComplexType</code> to associate with this
     *                    ElementGroup.
     */
    public void setComplexType(ComplexType complexType) {
        this.complexType = complexType;
    }

    /**
     * Get the <code>ComplexType</code> associated with this ElementGroup.
     *
     * @return      The <code>ComplexType</code> associated with this
     *              ElementGroup.
     */
    public ComplexType getComplexType() {
        return complexType;
    }

    /**
     * Set the <code>ChoiceInfo</code> associated with this ElementGroup.
     *
     * @param choice The <code>ChoiceInfo</code> to associate with this
     *               ElementGroup.
     */
    public void setChoice(ChoiceInfo choice) {
        this.choice = choice;
    }

    /**
     * Get the <code>ChoiceInfo</code> associated with this ElementGroup.
     *
     * @return      The <code>Choice</code> associated with this ElementGroup.
     */
    public ChoiceInfo getChoice() {
        return choice;
    }

    /**
     * Return the name of this ElementGroup
     *
     * @return      the name of this ElementGroup
     */
    public String getName() {
        return name;
    }

    // Javadoc inherited from super class.
    public Scope getScope() {
        return enclosingScope;
    }

    /**
     * Get the structure of the attributes associated with this ElementGroup.
     *
     * @return      The structure of the attributes associated with this
     *              ElementGroup.
     */
    public AttributesStructure getAttributesStructure() {
        if (attributesStructure == null) {
            attributesStructure = createAttributesStructure();
        }
        return attributesStructure;
    }

    /**
     * Create a new <code>AttributesStructure</code> which will be associated
     * with this ElementGroup.
     *
     * @return      The new <code>AttributesStructure</code>.
     */
    protected AttributesStructure createAttributesStructure() {
        return new AttributesStructure(this);
    }

    // Javadoc inherited from super class.
    public String toString() {
        return super.toString() + " [" + paramString() + "]";
    }

    /**
     * Return a String representation of the state of the object.
     *
     * @return      The String representation of the state of the object.
     */
    protected String paramString() {
        return name + ", attributesStructure=" + attributesStructure;
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
