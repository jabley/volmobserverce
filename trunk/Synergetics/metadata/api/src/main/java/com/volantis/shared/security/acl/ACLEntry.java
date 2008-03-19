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

import com.volantis.shared.inhibitor.Inhibitor;
import com.volantis.shared.security.acl.immutable.ImmutableACLEntry;
import com.volantis.shared.security.acl.mutable.MutableACLEntry;

import java.security.Principal;
import java.security.acl.Permission;
import java.util.Iterator;

/**
 * An entry within an {@link ACL}.
 *
 * <p>This is a reworking of the standard {@link java.security.acl.AclEntry}
 * interface to support the inhibitor pattern.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface ACLEntry
        extends Inhibitor {

    /**
     * Create an immutable ACLEntry that is equivalent to this.
     *
     * @return The immutable ACLEntry.
     */
    ImmutableACLEntry createImmutableACLEntry();

    /**
     * Create a mutable ACLEntry that is equivalent to this.
     *
     * @return The mutable ACLEntry.
     */
    MutableACLEntry createMutableACLEntry();

    /**
     * Returns the principal for which permissions are granted or denied by
     * this ACL entry. Returns null if there is no principal set for this
     * entry yet.
     *
     * @return the principal associated with this entry.
     */
    Principal getPrincipal();

    /**
     * Returns true if this is a negative ACL entry (one denying the
     * associated principal the set of permissions in the entry), false
     * otherwise.
     *
     * @return true if this is a negative ACL entry, false if it's not.
     */
    boolean isNegative();

    /**
     * Checks if the specified permission is part of the
     * permission set in this entry.
     *
     * @param permission the permission to be checked for.
     * @return true if the permission is part of the
     *         permission set in this entry, false otherwise.
     */
    boolean checkPermission(Permission permission);

    /**
     * Returns an enumeration of the permissions in this ACL entry.
     *
     * @return an enumeration of the permissions in this ACL entry.
     */
    Iterator permissions();

    /**
     * Returns a string representation of the contents of this ACL entry.
     *
     * @return a string representation of the contents.
     */
    String toString();
}
