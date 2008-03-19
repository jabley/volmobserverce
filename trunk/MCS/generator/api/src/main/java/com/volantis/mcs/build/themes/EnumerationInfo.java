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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/EnumerationInfo.java,v 1.1 2002/04/27 16:10:54 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created to encapsulate the 
 *                              data required to generate CSS KeywordMapper
 *                              classes
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.build.themes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class to enmcapsulate KeywordMapper data
 *
 */
public class EnumerationInfo {
  
  /**
   * List of Elements that make up the enumeration
   */
  protected List elements;

  /**
   * The name of this enumeration
   */
  protected String name;

  /**
   * Get the value of name.
   * @return value of name.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Set the value of name.
   * @param v  Value to assign to name.
   */
  public void setName(String  v) {
    this.name = v;
  }
  
  /**
   * Creates a new EnumerationInfo instance.
   */
  public EnumerationInfo() {
    elements = new ArrayList();
  }

  /**
   * Add an Element to this Enumeration
   * @param element an Element object
   */
  public void addElement(Element element) {
    if(elements.contains(element)) {
      throw new IllegalArgumentException("EnumarationType cannot contain a " +
					 "duplicate entry " + element);
    }
    elements.add(element);
  }

  /**
   * Get an Iterator that iterates over this enumerations elements
   * @return an
   */
  public Iterator elementIterator() {
    return elements.iterator();
  }


    /**
   * Class that represents an Enumeration Element
   */
  public static class Element extends AbstractThemeVersionInfo {
    
    /**
     * Elements label
     */
    private String label;

        /**
     * Creates a new Element instance.
     * @param label the label of this Enumeration element
     */
    public Element(String label) {
        this.label = label;
    }

    /**
     * Get the value of label.
     * @return value of label.
     */
    public String getLabel() {
      return label;
    }

      // Javadoc inherited from super class
    public boolean equals(Object o) {

      // Call the super class to check whether this object and the other object
      // are of the same type and have the same name.
      if (!super.equals (o)) {
	return false;
      }
      Element e = (Element)o;

      return label.equals(e.label);
    }
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

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
