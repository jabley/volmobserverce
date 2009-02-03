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

import java.security.Principal;

/**
 * Encapsulates information about the lock.
 */
public class JDBCLockInfo {

    /**
     * The owner of the lock.
     */
    private final Principal owner;

    /**
     * The acquisition time.
     */
    private final long acquisitionTime;

    public JDBCLockInfo(Principal owner, long acquisitionTime) {
        if (owner == null) {
            throw new IllegalArgumentException("owner cannot be null");
        }
        this.owner = owner;
        this.acquisitionTime = acquisitionTime;
    }

    /**
     * Get the owner of the lock.
     *
     * @return The owner of the lock.
     */
    public Principal getOwner() {
        return owner;
    }

    /**
     * Get the lock acquisition time.
     *
     * @return The lock acquisition time.
     */
    public long getAcquisitionTime() {
        return acquisitionTime;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof JDBCLockInfo)) {
            return false;
        }

        JDBCLockInfo other = (JDBCLockInfo) obj;
        return owner.equals(other.owner) &&
                acquisitionTime == (other.acquisitionTime);
    }

    // Javadoc inherited.
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
