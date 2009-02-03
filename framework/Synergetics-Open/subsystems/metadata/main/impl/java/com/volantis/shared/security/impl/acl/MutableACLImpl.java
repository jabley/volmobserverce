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

import com.volantis.shared.security.acl.ACLEntry;
import com.volantis.shared.security.acl.immutable.ImmutableACL;
import com.volantis.shared.security.acl.mutable.MutableACL;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

import java.security.Principal;
import java.security.acl.Group;
import java.security.acl.LastOwnerException;
import java.security.acl.NotOwnerException;
import java.util.Map;

/**
 * Implementation of {@link MutableACL}.
 */
public class MutableACLImpl
        extends AbstractACL
        implements MutableACL {

    /**
     * Copy constructor.
     *
     * @param acl The object to copy.
     */
    public MutableACLImpl(AbstractACL acl) {
        super(acl);
    }

    /**
     * Initialise.
     *
     * @param owner The original owner of the ACL.
     */
    public MutableACLImpl(Principal owner) {
        super(owner);
    }

    /**
     * For Persistence mechanisms only
     */
    protected MutableACLImpl() {
        // don't configure the parent. This is only for persistence mechanisms
    }

    // Javadoc inherited.
    public ImmutableACL createImmutableACL() {
        return new ImmutableACLImpl(this);
    }

    // Javadoc inherited.
    public boolean addOwner(Principal caller, Principal owner)
            throws NotOwnerException {

        ensureOwner(caller);

        boolean added = owners.add(owner);
        if (added && owner instanceof Group) {
            groups.add(owner);
        }

        return added;
    }

    // Javadoc inherited.
    public boolean deleteOwner(Principal caller, Principal owner)
            throws NotOwnerException, LastOwnerException {

        ensureOwner(caller);

        if (owners.size() == 1 && owners.contains(owner)) {
            throw new LastOwnerException();
        }

        boolean removed = owners.remove(owner);
        if (removed && owner instanceof Group) {
            groups.remove(owner);
        }

        return removed;
    }

    // Javadoc inherited.
    public void setName(Principal caller, String name)
            throws NotOwnerException {

        ensureOwner(caller);

        this.name = name;
    }

    // Javadoc inherited.
    public boolean addEntry(Principal caller, ACLEntry entry)
            throws NotOwnerException {

        ensureOwner(caller);

        if (entry == null) {
            throw new IllegalArgumentException("entry cannot be null");
        }

        Map map = findMap(entry, true);
        Principal principal = entry.getPrincipal();
        boolean added = false;
        if (!map.containsKey(principal)) {
            map.put(principal, entry.createImmutableACLEntry());
            added = true;
        }

        return added;
    }

    // Javadoc inherited.
    public boolean removeEntry(Principal caller, ACLEntry entry)
            throws NotOwnerException {

        ensureOwner(caller);

        if (entry == null) {
            throw new IllegalArgumentException("entry cannot be null");
        }

        Map map = findMap(entry, false);
        boolean removed = false;
        if (map != null) {
            removed = map.remove(entry) != null;
        }

        return removed;
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_ACL;
    }
}
