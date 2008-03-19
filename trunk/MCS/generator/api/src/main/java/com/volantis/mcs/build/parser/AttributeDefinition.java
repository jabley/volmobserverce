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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/AttributeDefinition.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
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
 * This class contains the definition of an Attribute within the schema.
 */
public class AttributeDefinition
  implements SchemaAttribute {

  /**
   * The <code>Scope</code> within which this definition is valid.
   */
  private Scope enclosingScope;

  /**
   * The default value of this attribute.
   */
  private String defaultValue;

  /**
   * The name of this attribute.
   */
  private String name;

  /**
   * The type of this attribute.
   */
  private String type;

  /**
   * The use of this attribute, may be "optional" or "required".
   */
  private String use;

  /**
   * Create a new <code>AttributeDefinition</code>.
   * @param name The name of the attribute.
   * @param enclosingScope The <code>Scope</code> within which this object
   * is valid.
   */
  public AttributeDefinition (String name, Scope enclosingScope) {
    this.name = name;
    this.enclosingScope = enclosingScope;
  }

  // Javadoc inherited from super class.
  public AttributeDefinition getDefinition () {
    return this;
  }

  /**
   * Set the default value of the attribute.
   * @param defaultValue The default value of the attribute.
   */
  public void setDefault (String defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * Get the default value of the attribute.
   * @return The default value of the attribute.
   */
  public String getDefault () {
    return defaultValue;
  }

  // Javadoc inherited from super class.
  public String getName () {
    return name;
  }

  // Javadoc inherited from super class.
  public Scope getScope () {
    return enclosingScope;
  }

  /**
   * Set the type of the attribute.
   * @param type The type of the attribute.
   */
  public void setType (String type) {
    this.type = type;
  }

  /**
   * Get the type of the attribute.
   * @return The type of the attribute.
   */
  public String getType () {
    return type;
  }

  /**
   * Set the use of the attribute.
   * @param use The use of the attribute.
   */
  public void setUse (String use) {
    this.use = use;
  }

  /**
   * Get the use of the attribute.
   * @return The use of the attribute.
   */
  public String getUse () {
    return use;
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
      + ", type=" + type
      + ", use=" + use
      + ", default=" + defaultValue;
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
