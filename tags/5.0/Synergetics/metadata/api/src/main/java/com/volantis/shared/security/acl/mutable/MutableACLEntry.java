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

package com.volantis.shared.security.acl.mutable;

import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.security.acl.ACLEntry;

import java.security.acl.Permission;

/**
 * A mutable {@link ACLEntry}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface MutableACLEntry
        extends ACLEntry, MutableInhibitor {

    /**
     * Adds the specified permission to this ACL entry. Note: An entry can
     * have multiple permissions.
     *
     * @param permission the permission to be associated with
     *                   the principal in this entry.
     * @return true if the permission was added, false if the
     *         permission was already part of this entry's permission set.
     */
    boolean addPermission(Permission permission);

    /**
     * Removes the specified permission from this ACL entry.
     *
     * @param permission the permission to be removed from this entry.
     * @return true if the permission is removed, false if the
     *         permission was not part of this entry's permission set.
     */
    boolean removePermission(Permission permission);

}
