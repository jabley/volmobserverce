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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/AttributesStructure.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
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

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the information needed to keep track of the
 * attributes which are owned by by an element, or an attribute group.
 */
public class AttributesStructure {

  /**
   * The <code>SchemaObject</code> which owns this attribute structure.
   */
  private SchemaObject owner;

  /**
   * The list of attributes.
   */
  private List attributes;

  /**
   * The list of attribute groups.
   */
  private List attributeGroups;

  /**
   * Create a new <code>AttributesStructure</code>.
   * @param owner The owner of these attributes.
   */
  public AttributesStructure (SchemaObject owner) {
    this.owner = owner;
  }

  /**
   * Get the list of <code>SchemaAttribute</code> objects.
   * @return The list of <code>SchemaAttribute</code> objects.
   */
  public List getAttributes () {
    if (attributes == null) {
      attributes = new ArrayList ();
    }
    
    return attributes;
  }

  /**
   * Get the list of <code>AttributeGroup</code> objects.
   * @return The list of <code>AttributeGroup</code> objects.
   */
  public List getAttributeGroups () {
    if (attributeGroups == null) {
      attributeGroups = new ArrayList ();
    }

    return attributeGroups;
  }

  /**
   * Get the owner of this object.
   * @return The owner of this object.
   */
  public SchemaObject getOwner () {
    return owner;
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
    return "attributes=" + attributes
      + ", attributeGroups=" + attributeGroups;
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
