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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/SchemaFactory.java,v 1.3 2003/03/10 11:51:09 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 07-Mar-03    Byron           VBM:2003030527 - Added createElementGroup and
 *                              createElementGroupReference.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.parser;

/**
 * This interface defines methods which are used to create instances of
 * various schema related objects.
 */
public interface SchemaFactory {

  /**
   * Create a new <code>AttributeDefinition</code>.
   * @param scope The scope within which the attribute definition is to
   * be created.
   * @param name The name of the attribute.
   * @return The new <code>AttributeDefinition</code>.
   */
  public AttributeDefinition createAttributeDefinition (Scope scope,
                                                        String name);

  /**
   * Create a new <code>AttributeReference</code>.
   * @param scope The scope within which the attribute reference is to
   * be created.
   * @param name The name of the attribute.
   * @return The new <code>AttributeReference</code>.
   */
  public AttributeReference createAttributeReference (Scope scope,
                                                      String name);
  
  /**
   * Create a new <code>AttributeGroupDefinition</code>.
   * @param scope The scope within which the attribute group definition is to
   * be created.
   * @param name The name of the attribute group.
   * @return The new <code>AttributeGroupDefinition</code>.
   */
  public AttributeGroupDefinition createAttributeGroupDefinition (Scope scope,
                                                                  String name);

  /**
   * Create a new <code>AttributeGroupReference</code>.
   * @param scope The scope within which the attribute group reference is to
   * be created.
   * @param name The name of the attribute group.
   * @return The new <code>AttributeGroupReference</code>.
   */
  public AttributeGroupReference createAttributeGroupReference (Scope scope,
                                                                String name);

  /**
   * Create a new <code>AttributesStructure</code>.
   * @param owner The <code>SchemaObject</code> which owns the attributes.
   * @return The new <code>AttributesStructure</code>.
   */
  public AttributesStructure createAttributesStructure (SchemaObject owner);
  
  /**
   * Create a new <code>ElementDefinition</code>.
   * @param scope The scope within which the element definition is to
   * be created.
   * @param name The name of the element.
   * @return The new <code>ElementDefinition</code>.
   */
  public ElementDefinition createElementDefinition (Scope scope,
                                                    String name);

  /**
   * Create a new <code>ElementReference</code>.
   * @param scope The scope within which the element reference is to
   * be created.
   * @param name The name of the element.
   * @return The new <code>ElementReference</code>.
   */
  public ElementReference createElementReference (Scope scope,
                                                  String name);

    /**
     * Create a new <code>ElementGroup</code>.
     *
     * @param  scope The scope within which the group definition is to be
     *               created.
     * @param  name  The name of the element group.
     * @return       The new <code>ElementGroup</code>.
     */
    public ElementGroup createElementGroup(Scope scope, String name);

    /**
     * Create a new <code>ElementGroupReference</code>.
     *
     * @param  scope The scope within which the element reference is to be
     *               created.
     * @param  name  The name of the group.
     * @return       The new <code>ElementGroupReference</code>.
     */
    public ElementGroupReference createElementGroupReference(Scope scope,
                                                             String name);

    /**
     * Create a new <code>ComplexType</code>.
     * @param scope The scope within which the complex type is to
     * be created.
     * @param name The name of the type.
     * @return The new <code>ComplexType</code>.
     */
    public ComplexType createComplexType(Scope scope, String name);

}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
