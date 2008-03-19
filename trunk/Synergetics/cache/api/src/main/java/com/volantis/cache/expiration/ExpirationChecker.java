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

package com.volantis.cache.expiration;

import com.volantis.cache.CacheEntry;
import com.volantis.shared.system.SystemClock;

/**
 * Determines whether an entry has expired.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * <p>An expired entry may be stale and so will require revalidation.</p>
 *
 * @mock.generate
 */
public interface ExpirationChecker {

    /**
     * Check to see whether the cache entry has expired.
     *
     * <p>This is invoked when the thread owns the mutex on the cache
     * entry.</p>
     *
     * @param clock
     * @param entry The entry to check.
     * @return True if the entry has expired and false otherwise.
     */
    boolean hasExpired(SystemClock clock, CacheEntry entry);
}
