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

import com.volantis.mcs.repository.RepositoryException;

import java.util.Collection;
import java.security.Principal;

/**
 * Provides support for managing locks.
 *
 * @mock.generate 
 */
public interface LockManager {

    /**
     * Get a principal with the specified name.
     *
     * @param name The name of the principal.
     * @return The {@link Principal}.
     */
    Principal getPrincipal(String name);

    /**
     * Get collection of all the locks in the repository.
     *
     * <p>The returned collection is a snapshot of the locks in the
     * repository.</p>
     *
     * @return A collection of {@link Lock}, if there are no locks then an
     * empty collection is returned.
     */
    Collection getLocks() throws LockException;

    /**
     * Get a lock for the specified resource.
     *
     * @param resourceIdentifier The identifier for the resource being locked.
     *
     * @return The lock.
     */
    Lock getLock(String resourceIdentifier);
}
