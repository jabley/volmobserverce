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
 * $Header: /src/voyager/com/volantis/mcs/objects/RepositoryObjectIdentity.java,v 1.10 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Oct-01    Paul            VBM:2001092608 - Created.
 * 23-May-02    Allan           VBM:2002030615 - Added visit(). 
 * 12-Jun-02    Allan           VBM:2002030615 - Made visit() return an Object.
 * 31-Jan-02    Doug            VBM:2002120405 - Modified the visit() methods
 *                              javadoc to state that the method is for 
 *                              volantis internal use only.
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.objects;

import com.volantis.mcs.project.Project;

/**
 * Defines the common methods which all objects which uniquely identity a
 * RepositoryObject must implement.
 * <p>
 * All objects which implement this must be immutable, that is they must
 * not be modifiable once they have been created. This means that they can be
 * shared and don't need to keep being cloned.
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public interface RepositoryObjectIdentity
        extends Comparable {

    /**
     * Get the name of the object which this identifies.
     *
     * @return The name of the object which this identifies.
     */
    public String getName();

    /**
     * Get the project to which the object identified by this object belongs.
     *
     * @return The project to which the object identified by this object belongs.
     *         If this is null then the object belongs to the default project.
     */
    public Project getProject();

    /**
     * Get the class of objects which this identifies.
     *
     * @return The class of objects which this identifies.
     */
    public Class getObjectClass();

    /**
     * Compare this object with another object.
     * <p>
     * This method must only return true if the other object is of exactly the
     * same class and each member variable in this object is equal to the member
     * variable in the other object.
     *
     * @param object The other object to compare against.
     * @return True if the objects are equivalent, false otherwise.
     */
    public boolean equals(Object object);

    /**
     * Calculate a hashCode for this object.
     * <p>
     * The hashCode for this object must be calculated from all its members
     * and from its type to try and prevent two objects with the same identity
     * but different types from colliding in a hash table.
     *
     * @return The hashCode.
     */
    public int hashCode();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Aug-04	5081/1	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (make it simpler)

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 ===========================================================================
*/
