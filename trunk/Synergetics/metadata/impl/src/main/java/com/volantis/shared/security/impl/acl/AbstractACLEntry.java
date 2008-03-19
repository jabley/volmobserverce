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

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.security.acl.ACLEntry;
import com.volantis.shared.security.acl.mutable.MutableACLEntry;

import java.security.Principal;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Base for implementations of {@link ACLEntry}.
 */
public abstract class AbstractACLEntry
        implements ACLEntry {

    /**
     * The principal to who the permissions are being granted or denied.
     */
    protected final Principal principal;

    /**
     * True if the permissions are being denied.
     */
    protected final boolean negative;

    /**
     * The set of permissions to grant or deny.
     */
    protected Set permissions;

    /**
     * Copy constructor.
     * @param entry
     */
    protected AbstractACLEntry(AbstractACLEntry entry) {
        this.principal = entry.principal;
        this.negative = entry.negative;
        Set permissions = entry.permissions;
        if (permissions == null || permissions.isEmpty()) {
            this.permissions = null;
        } else {
            this.permissions = new HashSet(permissions);
        }
    }

    /**
     * Initialise.
     *
     * @param principal The principal for which the permissions apply.
     * @param negative  True if the permission is being taken away, false if
     *                  it is being given.
     */
    public AbstractACLEntry(Principal principal, boolean negative) {
        this.principal = principal;
        this.negative = negative;
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return createImmutableACLEntry();
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return createMutableACLEntry();
    }

    // Javadoc inherited.
    public MutableACLEntry createMutableACLEntry() {
        return new MutableACLEntryImpl(this);
    }

    // Javadoc inherited.
    public boolean isNegative() {
        return negative;
    }

    // Javadoc inherited.
    public Principal getPrincipal() {
        return principal;
    }

    // Javadoc inherited.
    public boolean checkPermission(Permission permission) {
        return permissions == null ? false : permissions.contains(permission);
    }

    // Javadoc inherited.
    public Iterator permissions() {
        if (permissions == null || permissions.isEmpty()) {
            return AbstractACL.EMPTY_ITERATOR;
        } else {
            return permissions.iterator();
        }
    }

    // Javadoc inherited.
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (negative) {
            buffer.append("-");
        } else {
            buffer.append("+");
        }
        if (principal instanceof Group) {
            buffer.append("group ");
        } else {
            buffer.append("user ");
        }
        buffer.append(principal).append("=");
        if (permissions != null && !permissions.isEmpty()) {
            String separator = "";
            for (Iterator i = permissions.iterator(); i.hasNext();) {
                Permission p = (Permission) i.next();
                buffer.append(separator).append(p);
                separator = ",";
            }
        }

        return buffer.toString();
    }
}
