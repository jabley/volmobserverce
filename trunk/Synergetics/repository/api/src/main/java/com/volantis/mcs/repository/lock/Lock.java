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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.lock;

import java.security.Principal;

/**
 * A lock in the JDBC repository.
 *
 * <p>A lock is used to protect access to a particular resource within a JDBC
 * repository. It can be in one of two states, locked or unlocked. When it is
 * locked there is a row in a lock table that contains the identifier of the
 * resource that is locked and the name of the principal that owns the lock. If
 * no such row exists then the lock is not locked.</p>
 *
 * <p>A lock can be created in an unlocked state using
 * {@link LockManager#getLock(String)}. When it is locked a row is created in
 * the lock table and when it is unlocked the entry is removed. If concurrent
 * requests are made to lock the same lock then one request will succeed and
 * the rest will fail.</p>
 */
public interface Lock {

    /**
     * Get the identifier of the resource that is locked.
     *
     * @return The identifier of the resource that is locked.
     */
    String getResourceIdentifier();

    /**
     * The owner of the lock.
     *
     * @return The owner of the lock, or null if the lock is not locked.
     */
    Principal getOwner();

    /**
     * Get the time at which the lock was acquired.
     *
     * <p>This is the time on the server.</p>
     *
     * @return The time the lock was acquired.
     */
    long getAcquisitionTime();

    /**
     * Check the state of the lock.
     *
     * @return True if the lock is locked and false if it is not.
     */
    boolean isLocked();

    /**
     * Acquire the lock.
     *
     * <p>This will attempt to acquire the lock on behalf of the specified
     * principal. One of three things will occur.</p>
     *
     * <ul>
     * <li>The lock was successfully acquired for the principal in which case
     * this method returns true and the owner is set to this principal.</li>
     * <li>The lock was acquired by another principal in which case this
     * method returns false and the owner is set to the other principal.</li>
     * <li>An error occurred while attempting to access the database. In this
     * case an exception is thrown and the state of the lock is not
     * changed.</li>
     * </ul>
     *
     * @param principal The principal that wants to own the lock.
     * @return True if the lock was acquired for the specified principal, false
     * if it was not.
     * @throws IllegalStateException if the lock is already locked.
     */
    boolean acquire(Principal principal) throws LockException;

    /**
     * Release the lock.
     *
     * @param principal The principal that owns the lock.
     *
     * @throws IllegalStateException if the lock is not locked by the specified
     * principal.
     */
    void release(Principal principal);
}
