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

package com.volantis.shared.security.impl.acl;

import com.volantis.shared.security.acl.immutable.ImmutableACLEntry;
import com.volantis.shared.security.acl.mutable.MutableACLEntry;

import java.security.Principal;
import java.security.acl.Permission;
import java.util.HashSet;

/**
 * Implementation of {@link MutableACLEntry}.
 */
public class MutableACLEntryImpl
        extends AbstractACLEntry
        implements MutableACLEntry {

    /**
     * Copy constructor.
     *
     * @param entry The object to copy.
     */
    public MutableACLEntryImpl(AbstractACLEntry entry) {
        super(entry);
    }

    /**
     * Initialise.
     *
     * @param principal The principal for which the permissions apply.
     * @param negative  True if the permission is being taken away, false if
     *                  it is being given.
     */
    public MutableACLEntryImpl(Principal principal, boolean negative) {
        super(principal, negative);
    }

    // Javadoc inherited.
    public ImmutableACLEntry createImmutableACLEntry() {
        return new ImmutableACLEntryImpl(this);
    }

    // Javadoc inherited.
    public boolean addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new HashSet();
        }

        return permissions.add(permission);
    }

    // Javadoc inherited.
    public boolean removePermission(Permission permission) {
        return permissions == null ? false : permissions.remove(permission);
    }
}
