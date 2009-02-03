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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.security.acl;

import com.volantis.shared.security.acl.mutable.MutableACL;
import com.volantis.shared.security.acl.mutable.MutableACLEntry;

import java.security.Principal;

/**
 * An object for creating instances of Access Control List classes.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User extensions of this class are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class ACLFactory {

    /**
     * Create a mutable ACL.
     *
     * @param owner The owner of the ACL.
     * @return The mutable ACL.
     */
    public abstract MutableACL createACL(Principal owner);

    /**
     * Create a mutable ACL entry
     *
     * @param principal The principal to which the permissions apply.
     * @param negative  True if the entry removes permissions, false if it gives
     *                  them.
     * @return The mutable ACL.
     */
    public abstract MutableACLEntry createACLEntry(
            Principal principal, boolean negative);
}
