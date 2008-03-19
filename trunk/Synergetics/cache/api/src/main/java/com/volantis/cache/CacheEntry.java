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

package com.volantis.cache;

import com.volantis.cache.provider.CacheableObjectProvider;

/**
 * An entry in the cache.
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
public interface CacheEntry {

    /**
     * Get the cache containing this entry.
     *
     * <p>This can never change so it is safe to invoke this method at any
     * time.</p>
     *
     * @return The containing cache.
     */
    Cache getCache();

    /**
     * Get the key for the entry.
     *
     * <p>This can never change so it is safe to invoke this method at any
     * time.</p>
     *
     * @return The key of the entry.
     */
    Object getKey();

    /**
     * Get the value.
     *
     * <p>This can change when the entry is in the pending state and
     * so must not normally be accessed unless the caller has the entry mutex
     * and the entry is not in the pending state. The exception to this are
     * implementations of {@link CacheableObjectProvider} as they are invoked
     * as part of the code that updates the entry.</p>
     *
     * @return The value of the entry.
     */
    Object getValue();

    /**
     * Get the object that the user of the cache has associated with this
     * entry.
     *
     * <p>This can change when the entry is in the pending state and
     * so must not normally be accessed unless the caller has the entry mutex
     * and the entry is not in the pending state. The exception to this are
     * implementations of {@link CacheableObjectProvider} as they are invoked
     * as part of the code that updates the entry.</p>
     *
     * @return The extension object.
     */
    Object getExtensionObject();
}
