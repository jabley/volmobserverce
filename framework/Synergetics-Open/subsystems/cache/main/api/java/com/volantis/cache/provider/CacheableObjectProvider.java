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

package com.volantis.cache.provider;

import com.volantis.cache.CacheEntry;
import com.volantis.shared.system.SystemClock;


/**
 * Provides objects that can be added into the cache.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface CacheableObjectProvider {

    /**
     * Retrieve the value for the entry.
     *
     * <p>This method is called without holding any monitors on either the
     * cache or the entry.</p>
     *
     * @param clock
     * @param key   The key for the entry being requested.
     * @param entry The entry that is being processed, this will be null if
     *              the entry was previously marked as being uncacheable.
     * @return An instance of {@link ProviderResult} that contains the object
     *         retrieved as well as other information necessary for the cache,
     *         may not be null.
     */
    ProviderResult retrieve(SystemClock clock, Object key, CacheEntry entry);
}
