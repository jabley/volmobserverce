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
import com.volantis.shared.security.acl.immutable.ImmutableACL;
import com.volantis.shared.security.acl.mutable.MutableACL;

import java.security.Principal;
import java.security.acl.Permission;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents an Access Control List.
 *
 * <p>This is a reworking of the standard {@link java.security.acl.Acl}
 * interface to support the inhibitor pattern.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ACL
        extends Inhibitor {

    /**
     * Create an equivalent yet immutable ACL.
     *
     * @return The immutable ACL.
     */
    ImmutableACL createImmutableACL();

    /**
     * Create an equivalent yet mutable ACL.
     *
     * @return The mutable ACL.
     */
    MutableACL createMutableACL();

    /**
     * Returns true if the given principal is an owner of the ACL.
     *
     * @param owner the principal to be checked to determine whether or not
     *              it is an owner.
     * @return true if the passed principal is in the list of owners, false
     *         if not.
     */
    boolean isOwner(Principal owner);

    /**
     * Returns the name of this ACL.
     *
     * @return the name of this ACL.
     */
    String getName();

    /**
     * Returns an enumeration for the set of allowed permissions for the
     * specified principal (representing an entity such as an individual or
     * a group). This set of allowed permissions is calculated as
     * follows:<p>
     *
     * <ul>
     *
     * <li>If there is no entry in this Access Control List for the
     * specified principal, an empty permission set is returned.<p>
     *
     * <li>Otherwise, the principal's group permission sets are determined.
     * (A principal can belong to one or more groups, where a group is a
     * group of principals, represented by the Group interface.)
     * The group positive permission set is the union of all
     * the positive permissions of each group that the principal belongs to.
     * The group negative permission set is the union of all
     * the negative permissions of each group that the principal belongs to.
     * If there is a specific permission that occurs in both
     * the positive permission set and the negative permission set,
     * it is removed from both.<p>
     *
     * The individual positive and negative permission sets are also
     * determined. The positive permission set contains the permissions
     * specified in the positive ACL entry (if any) for the principal.
     * Similarly, the negative permission set contains the permissions
     * specified in the negative ACL entry (if any) for the principal.
     * The individual positive (or negative) permission set is considered
     * to be null if there is not a positive (negative) ACL entry for the
     * principal in this ACL.<p>
     *
     * The set of permissions granted to the principal is then calculated
     * using the simple rule that individual permissions always override
     * the group permissions. That is, the principal's individual negative
     * permission set (specific denial of permissions) overrides the group
     * positive permission set, and the principal's individual positive
     * permission set overrides the group negative permission set.
     *
     * </ul>
     *
     * @param user the principal whose permission set is to be returned.
     * @return the permission set specifying the permissions the principal
     *         is allowed.
     */
    Set permissions(Principal user);

    /**
     * Returns an enumeration of the entries in this ACL. Each element in
     * the enumeration is of type ACLEntry.
     *
     * @return an enumeration of the entries in this ACL.
     */
    Iterator entries();

    /**
     * Checks whether or not the specified principal has the specified
     * permission. If it does, true is returned, otherwise false is returned.
     *
     * More specifically, this method checks whether the passed permission
     * is a member of the allowed permission set of the specified principal.
     * The allowed permission set is determined by the same algorithm as is
     * used by the <code>getPermissions</code> method.
     *
     * @param principal  the principal, assumed to be a valid authenticated
     *                   Principal.
     * @param permission the permission to be checked for.
     * @return true if the principal has the specified permission, false
     *         otherwise.
     * @see #permissions(java.security.Principal)
     */
    boolean checkPermission(Principal principal, Permission permission);

    /**
     * Returns a string representation of the
     * ACL contents.
     *
     * @return a string representation of the ACL contents.
     */
    String toString();
}
