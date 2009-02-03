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
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.shared.security.acl.ACL;
import com.volantis.shared.security.acl.ACLEntry;
import com.volantis.shared.security.acl.mutable.MutableACL;

import java.security.Principal;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Base for implementations of {@link ACL}.
 */
public abstract class AbstractACL extends InhibitorImpl
    implements ACL {

    /**
     * Increment to map index for groups.
     */
    private static final int GROUP = 1;

    /**
     * Increment to map index for individuals.
     */
    private static final int INDIVIDUAL = 0;

    /**
     * Increment to map index for positive entries.
     */
    private static final int POSITIVE = 0;

    /**
     * Increment to map index for negative entries.
     */
    private static final int NEGATIVE = 2;

    /**
     * Index of positive entries for groups.
     */
    private static final int GROUP_POSITIVE = GROUP + POSITIVE;

    /**
     * Index of negative entries for groups.
     */
    private static final int GROUP_NEGATIVE = GROUP + NEGATIVE;

    /**
     * Index of positive entries for individuals.
     */
    private static final int INDIVIDUAL_POSITIVE = INDIVIDUAL + POSITIVE;

    /**
     * Index of negative entries for individuals.
     */
    private static final int INDIVIDUAL_NEGATIVE = INDIVIDUAL + NEGATIVE;

    /**
     * An empty set.
     */
    private static final Set EMPTY_SET = Collections.EMPTY_SET;

    /**
     * An empty iterator.
     */
    public static final Iterator EMPTY_ITERATOR = new Iterator() {
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }
    };

    /**
     * The set of owners. (Principle objects)
     */
    protected Set /*<Principle>*/ owners;

    /**
     * The Set of owners that are also Groups. (Group extends Principle)
     */
    protected Set /*<Group>*/ groups;

    /**
     * The name of the ACL.
     */
    protected String name;

    /**
     * Maps from principal to ACLEntry
     *
     * <p>There are 4 maps:</p>
     * <ul>
     * <li>m[0] = Group - Positive</li>
     * <li>m[1] = Group - Negative</li>
     * <li>m[2] = Individual - Positive</li>
     * <li>m[3] = Individual - Negative</li>
     * </ul>
     */
    private Map[] maps;

    /**
     * This should only be called by subclasses being used by a persistence
     * mechanism. Otherwise it leaves the object incorrectly initialized
     */
    protected AbstractACL() {
        groups = new HashSet();
        owners = new HashSet();
    }

    /**
     * Copy constructor.
     *
     * @param acl The object to copy.
     */
    protected AbstractACL(AbstractACL acl) {
        if (acl == null) {
            throw new IllegalArgumentException("owner cannot be null");
        }

        groups = new HashSet(acl.groups);
        owners = new HashSet(acl.owners);

        maps = new Map[4];
        for (int i = 0; i < maps.length; i++) {
            Map map = acl.maps[i];
            if (map != null) {
                maps[i] = new HashMap(map);
            }
        }
    }

    /**
     * Initialise.
     *
     * @param owner The initial owner.
     */
    protected AbstractACL(Principal owner) {
        if (owner == null) {
            throw new IllegalArgumentException("owner cannot be null");
        }

        groups = new HashSet();
        owners = new HashSet();

        owners.add(owner);
        if (owner instanceof Group) {
            groups.add(owner);
        }

        maps = new Map[4];
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return createImmutableACL();
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return createMutableACL();
    }

    // Javadoc inherited.
    public MutableACL createMutableACL() {
        return new MutableACLImpl(this);
    }

    // Javadoc inherited.
    public boolean isOwner(Principal owner) {
        boolean found = owners.contains(owner);
        if (!found) {
            for (Iterator i = groups.iterator(); !found && i.hasNext();) {
                Group group = (Group) i.next();
                if (group.isMember(owner)) {
                    found = true;
                }
            }
        }

        return found;
    }

    /**
     * Ensure that the caller is an owner.
     *
     * @param caller The caller.
     * @throws NotOwnerException If the caller is not an owner.
     */
    protected void ensureOwner(Principal caller) throws NotOwnerException {
        if (!isOwner(caller)) {
            throw new NotOwnerException();
        }
    }

    // Javadoc inherited.
    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public Iterator entries() {
        return new ACLIterator(maps);
    }

    // Javadoc inherited.
    public boolean checkPermission(Principal principal, Permission permission) {
        Set permissions = getPermissionSet(principal);
        return permissions.contains(permission);
    }

    // Javadoc inherited.
    public Set permissions(Principal principal) {
        Set permissions = getPermissionSet(principal);
        return permissions;
    }

    /**
     * Get the set of permissions that are granted to the specified principal.
     *
     * @param principal The principal whose permissions are required.
     * @return The possibly empty set of permissions.
     */
    private Set getPermissionSet(Principal principal) {
        Set permissions = EMPTY_SET;

        permissions = updateGroupPermissions(maps[GROUP_POSITIVE], principal,
                permissions);
        permissions = updateGroupPermissions(maps[GROUP_NEGATIVE], principal,
                permissions);
        permissions = updateIndividualPermissions(maps[INDIVIDUAL_POSITIVE],
                principal, permissions);
        permissions = updateIndividualPermissions(maps[INDIVIDUAL_NEGATIVE],
                principal, permissions);
        return permissions;
    }

    /**
     * Update the set of permissions with the permissions that are granted or
     * denied for any groups of which the principal belongs.
     *
     * @param map         The map from group to ACLEntry.
     * @param principal   The principal whose permissions are required.
     * @param permissions The set of permissions to update.
     * @return The possibly changed set of permissions.
     */
    private Set updateGroupPermissions(
            Map map, Principal principal, Set permissions) {

        if (map != null) {
            for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                Group group = (Group) entry.getKey();
                if (group.isMember(principal)) {
                    ACLEntry aclEntry = (ACLEntry) entry.getValue();
                    permissions = updatePermissions(permissions, aclEntry);
                }
            }
        }

        return permissions;
    }

    /**
     * Update the set of permissions with the permissions that are granted or
     * denied for the principal themselves..
     *
     * @param map         The map from group to ACLEntry.
     * @param principal   The principal whose permissions are required.
     * @param permissions The set of permissions to update.
     * @return The possibly changed set of permissions.
     */
    private Set updateIndividualPermissions(
            Map map, Principal principal, Set permissions) {

        if (map != null) {
            ACLEntry entry = (ACLEntry) map.get(principal);
            if (entry != null) {
                permissions = updatePermissions(permissions, entry);
            }
        }
        return permissions;
    }

    /**
     * Update the permissions with the permissions in the entry.
     *
     * @param permissions The set of permissions to update.
     * @param aclEntry    The entry whose permissions are to be granted, or denied.
     * @return The possibly changed set of permissions.
     */
    private Set updatePermissions(Set permissions, ACLEntry aclEntry) {
        boolean grant = !aclEntry.isNegative();
        if (grant) {
            if (permissions == EMPTY_SET) {
                permissions = new HashSet();
            }
            Iterator iterator = aclEntry.permissions();
            while (iterator.hasNext()) {
                Permission p = (Permission) iterator.next();
                permissions.add(p);
            }
        } else {
            if (permissions != EMPTY_SET) {
                Iterator iterator = aclEntry.permissions();
                while (iterator.hasNext()) {
                    Permission p = (Permission) iterator.next();
                    permissions.remove(p);
                }
            }
        }
        return permissions;
    }

    /**
     * Find the map in the array for the specified entry.
     *
     * @param entry  The entry whose map is required.
     * @param create True if the map should be created if it does not already
     *               exist.
     * @return The map, may be null.
     */
    protected Map findMap(ACLEntry entry, boolean create) {
        Principal principal = entry.getPrincipal();
        int index;
        if (principal instanceof Group) {
            index = GROUP;
        } else {
            index = INDIVIDUAL;
        }
        if (entry.isNegative()) {
            index += NEGATIVE;
        } else {
            index += INDIVIDUAL;
        }
        Map map = maps[index];
        if (map == null && create) {
            map = new HashMap();
            maps[index] = map;
        }

        return map;
    }

    // Javadoc inherited.
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = entries();
        while (iterator.hasNext()) {
            ACLEntry entry = (ACLEntry) iterator.next();
            buffer.append(entry).append("\n");
        }
        return buffer.toString();
    }
}
