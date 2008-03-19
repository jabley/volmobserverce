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
import com.volantis.shared.security.acl.ACL;
import com.volantis.shared.security.acl.ACLEntry;

import java.security.Principal;
import java.security.acl.LastOwnerException;
import java.security.acl.NotOwnerException;

/**
 * A mutable {@link ACL}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface MutableACL
        extends ACL, MutableInhibitor {

    /**
     * Adds an owner. Only owners can modify ACL contents. The caller
     * principal must be an owner of the ACL in order to invoke this method.
     * That is, only an owner can add another owner. The initial owner is
     * configured at ACL construction time.
     *
     * @param caller the principal invoking this method. It must be an owner
     *               of the ACL.
     * @param owner  the owner that should be added to the list of owners.
     * @return true if successful, false if owner is already an owner.
     * @throws java.security.acl.NotOwnerException
     *          if the caller principal is not an owner
     *          of the ACL.
     */
    boolean addOwner(Principal caller, Principal owner)
            throws NotOwnerException;

    /**
     * Deletes an owner. If this is the last owner in the ACL, an exception is
     * raised.<p>
     *
     * The caller principal must be an owner of the ACL in order to invoke
     * this method.
     *
     * @param caller the principal invoking this method. It must be an owner
     *               of the ACL.
     * @param owner  the owner to be removed from the list of owners.
     * @return true if the owner is removed, false if the owner is not part
     *         of the list of owners.
     * @throws NotOwnerException if the caller principal is not an owner
     *                           of the ACL.
     * @throws java.security.acl.LastOwnerException
     *                           if there is only one owner left, so that
     *                           deleteOwner would leave the ACL owner-less.
     */
    boolean deleteOwner(Principal caller, Principal owner)
            throws NotOwnerException, LastOwnerException;

    /**
     * Sets the name of this ACL.
     *
     * @param caller the principal invoking this method. It must be an
     *               owner of this ACL.
     * @param name   the name to be given to this ACL.
     * @throws java.security.acl.NotOwnerException
     *          if the caller principal
     *          is not an owner of this ACL.
     * @see #getName
     */
    void setName(Principal caller, String name)
            throws NotOwnerException;

    /**
     * Adds an ACL entry to this ACL. An entry associates a principal
     * (e.g., an individual or a group) with a set of
     * permissions. Each principal can have at most one positive ACL
     * entry (specifying permissions to be granted to the principal)
     * and one negative ACL entry (specifying permissions to be
     * denied). If there is already an ACL entry of the same type
     * (negative or positive) already in the ACL, false is returned.
     *
     * @param caller the principal invoking this method. It must be an
     *               owner of this ACL.
     * @param entry  the ACL entry to be added to this ACL.
     * @return true on success, false if an entry of the same type
     *         (positive or negative) for the same principal is already
     *         present in this ACL.
     * @throws NotOwnerException if the caller principal
     *                           is not an owner of this ACL.
     */
    boolean addEntry(Principal caller, ACLEntry entry)
            throws NotOwnerException;

    /**
     * Removes an ACL entry from this ACL.
     *
     * @param caller the principal invoking this method. It must be an
     *               owner of this ACL.
     * @param entry  the ACL entry to be removed from this ACL.
     * @return true on success, false if the entry is not part of this ACL.
     * @throws NotOwnerException if the caller principal is not
     *                           an owner of this Acl.
     */
    boolean removeEntry(Principal caller, ACLEntry entry)
            throws NotOwnerException;

}
