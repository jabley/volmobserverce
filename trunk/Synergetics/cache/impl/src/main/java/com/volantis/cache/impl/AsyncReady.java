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

package com.volantis.cache.impl;

import com.volantis.cache.CacheEntry;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.time.Period;

import java.util.Timer;

/**
 * The result of an asynchronous query on an entry that was ready.
 *
 * <p>If the entry state changes after this has been created then it must have
 * no impact on this object.</p>
 *
 * <p>A single instance of this is created each time that an entry's value
 * changes and is stored in the entry until it is overridden by another update.
 * Therefore, a single instance of this class may be accessed by multiple
 * threads and must be thread safe.</p>
 */
class AsyncReady
        implements InternalAsyncResult {

    /**
     * The key for the entry.
     */
    private final Object key;

    /**
     * The value of the entry.
     */
    private final Object value;

    /**
     * Initialise.
     *
     * @param entry The entry.
     */
    public AsyncReady(CacheEntry entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    // Javadoc inherited.
    public boolean isReady() {
        return true;
    }

    // Javadoc inherited.
    public Object update(ProviderResult result) {
        throw new IllegalStateException(
                "Cannot update as entry " + key + " is ready");
    }

    // Javadoc inherited.
    public Object getValue() {
        return value;
    }

    // Javadoc inherited.
    public void failed(Throwable throwable) {
        throw new IllegalStateException(
                "Cannot fail as entry " + key + " is ready");
    }

    // Javadoc inherited.
    public CacheEntry getEntry() {
        throw new IllegalStateException(
                "Cannot get entry as entry " + key + " is ready");
    }

    // Javadoc inherited.
    public void schedule(Timer timer, Period period) {
        // Do nothing.
    }
}
