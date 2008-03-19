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
 * $Header: /src/voyager/com/volantis/mcs/objects/RepositoryObject.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Oct-01    Paul            VBM:2001092608 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.objects;

import com.volantis.mcs.project.Project;

/**
 * This interface defines those methods which are supported by objects which
 * are stored in the repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public interface RepositoryObject {

  /**
   * Get the name of the object.
   * @return The name of the object.
   */
  public String getName ();

  /**
   * Get the project to which this repository object belongs.
   * @return The project to which this repository object belongs. If this
   * is null then the object belongs to the default project.
   */
  public Project getProject();

  /**
   * Get the identity of the object.
   * @return The identity of the object.
   */
  public RepositoryObjectIdentity getIdentity ();

  /**
   * Create a clone of this object.
   * @return The clone of this object.
   */
  public Object clone ();

  /**
   * Compare this object with another object.
   * <p>
   * This method must only return true if the other object is of exactly the
   * same class and each member variable in this object is equal to the member
   * variable in the other object.
   * @param other The other object to compare against.
   * @return True if the objects are equivalent, false otherwise.
   */
  public boolean equals (Object object);

  /**
   * Calculate a hashCode for this object.
   * <p>
   * The hashCode for this object must be calculated from all its members
   * and from its type to try and prevent two objects with the same name
   * but different types from colliding in a hash table.
   * @return The hashCode.
   */
  public int hashCode ();
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

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 ===========================================================================
*/
