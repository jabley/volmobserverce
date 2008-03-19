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
 * $Header: /src/voyager/com/volantis/mcs/papi/PAPIAttributes.java,v 1.5 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 22-Nov-02    Geoff           VBM:2002111504 - Added getElementName method.                               
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

import java.util.Iterator;

/**
 * The base class of all the PAPI attribute classes.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate
*/
public interface PAPIAttributes {


  /**
   * Resets the internal state so it is equivalent (not necessarily identical)
   * to a new instance.
   */                                                                       
  void reset ();
    
    /**
     * Returns the name of the element.
     *
     * @return the name of the element.
     */
    String getElementName();

    /**
     * Return a generic representation of the the attributes, accessible via
     * generic getter and setter methods.
     *
     * @return the representation of the attributes with generic getter and
     * setter methods
     */
    PAPIAttributes getGenericAttributes();

    /**
     * Get the value of the attribute.
     *
     * <p>It is an error to use an attribute name (including namespace)
     * that is not valid for the current element. Currently this error is
     * ignored but users must not rely on this behaviour as additional
     * validation may be added in future to detect and report these errors.</p>
     *
     * @param namespace The namespace of the attribute, null if the attribute
     *                  does not belong in a namespace, i.e. it belongs to
     *                  the element.
     * @param localName The local name of the attribute, may not be null.
     * @return The value of the attribute, or null if the attribute does not
     *         exist.
     */
    String getAttributeValue(String namespace, String localName);

    /**
     * Add a particular attribute to the list.
     *
     * <p>It is an error to use an attribute name (including namespace)
     * that is not valid for the current element. Currently this error is
     * ignored but users must not rely on this behaviour as additional
     * validation may be added in future to detect and report these errors.</p>
     *
     * @param namespace of the attribute to add
     * @param localName of the attribute to add
     * @param value of the attribute to add
     */
    void setAttributeValue(String namespace, String localName, String value);

    /**
     * Returns an <code>Iterator</code> that provides access to the
     * names of the attributes stored
     * @param namespace The namespace whose attributes you want or null if
     *                  you wish those attributes that don't belong to a
     *                  namespace
     * @return an Iterator
     */
    Iterator getAttributeNames(String namespace);
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

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
