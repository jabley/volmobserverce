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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/ComplexType.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
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
 * This class contains the definition of a complex type.
 */
public class ComplexType
  implements SchemaObject {

  /**
   * Flag which specifies whether this complex type allows mixed content,
   * which is elements and character data.
   */
  private boolean mixed;

  /**
   * Flag which specifies whether this complex type is empty or not.
   */
  private boolean empty;

    private Scope scope;

    private String name;

    /**
     * The structure of attributes associated with this element.
     */
    private AttributesStructure attributesStructure;

    public ComplexType(Scope scope, String name) {
        this.scope = scope;
        this.name = name;
    }

    /**
   * Set the empty property.
   * @param empty The new value of the empty property.
   */
  public void setEmpty (boolean empty) {
    this.empty = empty;
  }

  /**
   * Check to see whether this complex type is empty or not.
   * @return True if this complex type is empty and false otherwise.
   */
  public boolean isEmpty () {
    return empty;
  }

  /**
   * Set the mixed property.
   * @param mixed The new value of the mixed property.
   */
  public void setMixed (boolean mixed) {
    this.mixed = mixed;
  }

  /**
   * Check to see whether this complex type allows mixed content or not.
   * @return True if this complex type allows mixed content and false
   * otherwise.
   */
  public boolean isMixed () {
    return mixed;
  }

  // Javadoc inherited from super class.
  public Scope getScope () {
    return scope;
  }

    public String getName() {
        return name;
    }

    /**
     * Get the structure of the attributes associated with this element.
     * @return The structure of the attributes associated with this element.
     */
    public AttributesStructure getAttributesStructure () {
      if (attributesStructure == null) {
        attributesStructure = createAttributesStructure ();
      }

      return attributesStructure;
    }

    /**
     * Create a new <code>AttributesStructure</code> which will be associated
     * with this element.
     * @return The new <code>AttributesStructure</code>.
     */
    protected AttributesStructure createAttributesStructure () {
      return new AttributesStructure (this);
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

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
