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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/ElementReference.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.parser;

/**
 * This class is a reference to a SchemaElement and once the schema has
 * been parsed behaves just like the element to which it refers. It is not
 * valid to call any of the ElementDefinition methods until the schema has been
 * parsed as the element to which it refers may not have been defined yet.
 */
public class ElementReference
  implements SchemaElement {

  /**
   * The <code>Scope</code> within which this reference is valid.
   */
  private Scope enclosingScope;

  /**
   * The name of this element.
   */
  private String name;

  /**
   * The definition of this element.
   */
  private ElementDefinition definition;

  /**
   * Create a new <code>ElementReference</code>.
   * @param name The name of the element.
   * @param enclosingScope The <code>Scope</code> within which this object
   * is valid.
   */
  public ElementReference (String name, Scope enclosingScope) {
    this.name = name;
    this.enclosingScope = enclosingScope;
  }

  // Javadoc inherited from super class.
  public ElementDefinition getDefinition () {
    if (definition == null) {
      definition = enclosingScope.getElementDefinition (name);
    }

    return definition;
  }

  // Javadoc inherited from super class.
  public String getName () {
    return name;
  }

  // Javadoc inherited from super class.
  public Scope getScope () {
    return enclosingScope;
  }

  // Javadoc inherited from super class.
  public String toString () {
    return super.toString () + " [" + paramString () + "]";
  }

  /**
   * Return a String representation of the state of the object.
   * @return The String representation of the state of the object.
   */
  protected String paramString () {
    return name
      + "," + definition;
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
