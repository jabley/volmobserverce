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

package com.volantis.mcs.repository.impl.jdbc.lock;

import com.volantis.mcs.repository.lock.LockException;
import com.volantis.mcs.repository.lock.LockManager;

import java.security.Principal;

/**
 * A lock manager that stores the locks into a JDBC database.
 *
 * @mock.generate base="LockManager"
 */
public interface JDBCLockManager
        extends LockManager {

    /**
     * Query the lock information for the specified resource.
     *
     * @param resourceIdentifier The identifier of the resource to query.
     * @return A new {@link JDBCLockInfo}, or null if it could not be found.
     */
    JDBCLockInfo queryLockInfo(String resourceIdentifier);

    /**
     * Attempt to acquire the lock for the identified resource on behalf of the
     * specified principal.
     *
     * @param resourceIdentifier The identifier of the resource to lock.
     * @param principal          The principal that requires the lock.
     * @return The owner of the lock, this may be either the specified
     *         principal, or another principal.
     * @throws LockException If there was a problem accessing the database.
     */
    JDBCLockInfo acquireLock(String resourceIdentifier, Principal principal)
            throws LockException;

    /**
     * Release the lock for the identified resource.
     *
     * @param resourceIdentifier The identifier of the resource to lock.
     * @param principal          The principal that owns the lock.
     * @return True if the lock could be released and false otherwise.
     * @throws LockException If there was a problem accessing the database.
     */
    boolean releaseLock(String resourceIdentifier, Principal principal);
}
