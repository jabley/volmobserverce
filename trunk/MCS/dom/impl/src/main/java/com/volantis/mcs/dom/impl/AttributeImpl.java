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
 * $Header: /src/voyager/com/volantis/mcs/dom/Attribute.java,v 1.2 2002/03/22 18:24:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.Attribute;

/**
 * This class represents an attribute of an element in the dom.
 * <p>
 * It is only singly linked because they will mostly be added to a list and
 * hardly ever removed. The extra link would therefore hardly ever be needed
 * and would simply add to the size of the objects and the cost of managing the
 * lists.
 * </p>
 */
public class AttributeImpl
  implements Attribute {

  /**
   * The next attribute, is null if this is the last attribute in the list.
   */
  AttributeImpl next;

  /**
   * The name of the attribute.
   */
  String name;

  /**
   * The value associated with the attribute.
   */
  String value;

  /**
   * Create a new <code>Attribute</code>.
   */
  public AttributeImpl () {
  }

  /**
   * Set the next attribute.
   * @param next The next attribute.
   */
  public void setNext (AttributeImpl next) {
    this.next = next;
  }

  public Attribute getNext () {
    return next;
  }

  /**
   * Set the name.
   * @param name The name.
   */
  public void setName (String name) {
    this.name = name;
  }

  public String getName () {
    return name;
  }

  /**
   * Set the value.
   * @param value The value.
   */
  public void setValue (String value) {
    this.value = value;
  }

  public String getValue () {
    return value;
  }

  /**
   * Copy the name and value from another attribute.
   * @param attribute The attribute whose name and value should be copied from.
   */
  public void copy (AttributeImpl attribute) {
    name = attribute.name;
    value = attribute.value;
  }

  void reset () {
    next = null;
    name = null;
    value = null;
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * end:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
