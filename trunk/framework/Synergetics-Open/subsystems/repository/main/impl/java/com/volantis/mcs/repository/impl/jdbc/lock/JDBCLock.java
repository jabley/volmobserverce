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

import com.volantis.mcs.repository.lock.Lock;
import com.volantis.mcs.repository.lock.LockException;

import java.security.Principal;

/**
 * A lock that is stored in a JDBC database.
 */
public class JDBCLock
        implements Lock {

    /**
     * The manager of the locks within the database.
     */
    private final JDBCLockManager manager;

    /**
     * The resource that this lock protects.
     */
    private final String resourceIdentifier;

    /**
     * The owner of the lock.
     */
    private Principal owner;

    /**
     * The time (on the database server) that the lock was acquired.
     */
    private long acquisitionTime;

    /**
     * Initialise.
     *
     * @param manager            The manager to use to access the database.
     * @param resourceIdentifier The resource that this lock protects.
     * @param principal          The owner of the lock, may be null.
     * @param acquisitionTime
     */
    public JDBCLock(
            JDBCLockManager manager, String resourceIdentifier,
            Principal principal, long acquisitionTime) {
        this.manager = manager;
        this.resourceIdentifier = resourceIdentifier;
        this.owner = principal;
        this.acquisitionTime = acquisitionTime;
    }

    // Javadoc inherited.
    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    // Javadoc inherited.
    public Principal getOwner() {
        return owner;
    }

    // Javadoc inherited.
    public long getAcquisitionTime() {
        return acquisitionTime;
    }

    // Javadoc inherited.
    public boolean isLocked() {
        return owner != null;
    }

    // Javadoc inherited.
    public boolean acquire(Principal principal)
            throws LockException {

        if (principal == null) {
            throw new IllegalArgumentException("principal cannot be null");
        }

        if (this.owner == null) {
            // Ask the manager to acquire the lock for the principal. If it
            // does not fail then the returned principal is the owner, which
            // may or may not be the requested principal.
            JDBCLockInfo lockInfo =
                    manager.acquireLock(resourceIdentifier, principal);
            this.owner = lockInfo.getOwner();
            this.acquisitionTime = lockInfo.getAcquisitionTime();
            if (owner.equals(principal)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new IllegalStateException("Lock cannot be acquired as it is already owned by " +
                    owner.getName());
        }
    }

    // Javadoc inherited.
    public void release(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("principal cannot be null");
        }

        if (principal.equals(owner)) {
            if (!manager.releaseLock(resourceIdentifier, principal)) {
                throw new LockException("Lock for " + resourceIdentifier +
                        " is not owned by " + principal.getName());
            }
        } else if (owner == null) {
            throw new IllegalStateException(
                    "Lock cannot be released as it is not owned");
        } else {
            throw new IllegalStateException("Lock cannot be released as it is owned by " +
                    owner.getName());
        }
    }

    /**
     * Update the internal state of the lock.
     */
    public void updateState() {
        JDBCLockInfo info = manager.queryLockInfo(resourceIdentifier);
        if (info != null) {
            owner = info.getOwner();
            acquisitionTime = info.getAcquisitionTime();
        } else {
            owner = null;
            acquisitionTime = 0;
        }
    }

    /**
     * Update the internal state of the lock.
     *
     * @param principal       The owner.
     * @param acquisitionTime The acquisition time.
     */
    public void updateState(Principal principal, long acquisitionTime) {
        this.owner = principal;
        this.acquisitionTime = acquisitionTime;
    }
}
