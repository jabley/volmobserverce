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
 * $Header: /src/voyager/com/volantis/mcs/pickle/PickleAttributes.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Paul            VBM:2002032105 - Created to be the base class
 *                              of all pickle attributes classes.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.pickle;

import com.volantis.mcs.papi.PAPIAttributes;

import java.util.Map;
import java.util.Iterator;

/**
 * The base class of all the pickle attribute classes.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class PickleAttributes
  implements PAPIAttributes {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2002.";

  /**
   * The name of the element.
   */
  private String elementName;

  /**
   * The attributes of the element.
   */
  private Map attributes;

  /**
   * Set the value of the element name property.
   * @param elementName The new value of the element name property.
   */
  public void setElementName (String elementName) {
    this.elementName = elementName;
  }

  /**
   * Get the value of the element name property.
   * @return The value of the element name property.
   */
  public String getElementName () {
    return elementName;
  }

  /**
   * Set the value of the attributes property.
   * @param attributes The new value of the attributes property.
   */
  public void setAttributes (Map attributes) {
      this.attributes = attributes;
  }

  /**
   * Get the value of the attributes property.
   * @return The value of the attributes property.
   */
  public Map getAttributes () {
    return attributes;
  }

  /**
   * Resets the internal state so it is equivalent (not necessarily identical)
   * to a new instance.
   */
  public void reset () {
    elementName = null;
    attributes = null;
  }

  // javadoc inherited
  public PAPIAttributes getGenericAttributes() {
      throw new UnsupportedOperationException(
              "PickleAttributes#getGenericAttributes is not supported");
  }

  // javadoc inherited
  public String getAttributeValue(String namespace, String localName) {
      throw new UnsupportedOperationException(
              "PickleAttributes#getAttributeValue is not supported");
  }

  // javadoc inherited
  public void setAttributeValue(String namespace, String localName, String value) {
      throw new UnsupportedOperationException(
              "PickleAttributes#setAttributeValue is not supported");
  }

    // javadoc inherited
    public Iterator getAttributeNames(String namespace) {
        throw new UnsupportedOperationException(
              "PickleAttributes#getAttributeNames is not supported");
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

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
