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

import com.volantis.cache.provider.ProviderResult;
import com.volantis.cache.CacheEntry;
import com.volantis.shared.time.Period;

import java.util.Timer;

/**
 * The result of an asynchronous query on an entry that was previously marked
 * as uncacheable.
 *
 * <p>An instance of this is created for an entry that is marked as uncacheable
 * and stored in the entry itself. It will be returned every time an
 * asynchronous query is made against that entry and so must be thread
 * safe.</p>
 */
class AsyncUncacheable
        extends UpdatingAsyncResult {

    /**
     * Initialise.
     *
     * @param entry The entry to update.
     */
    public AsyncUncacheable(InternalCacheEntry entry) {
        super(entry);
    }

    // Javadoc inherited.
    public Object update(ProviderResult result) {

        Object value = result.getValue();

        // If the result is now cacheable then update the entry.
        if (result.isCacheable()) {

            // Only one thread can update each cache entry and the entry
            // must be in pending state. So synchronize on the entry to
            // move it into pending state, if it is not then change it
            // and remember to update the entry, otherwise do nothing.
            boolean update = false;
            synchronized(entry) {
                if (entry.getState() != EntryState.PENDING) {
                    entry.setState(EntryState.PENDING);
                    entry.setAsyncResult(null);
                    update = true;
                }
            }

            // If this thread was responsible for changing the entry
            // state then it must also update the entry.
            if (update) {
                value = updateCacheEntry(result, entry);
            }
        }

        return value;
    }

    // Javadoc inherited.
    public void failed(Throwable throwable) {
        // Failures must never be recorded in the entry as that is shared by
        // multiple threads.
    }

    // Javadoc inherited.
    public CacheEntry getEntry() {
        // The entry must not be exposed as it does not have any useful
        // information in and there may be multiple threads accessing the same
        // instance of this class and hence using the same entry.
        return null;
    }

    // Javadoc inherited.
    public void schedule(Timer timer, Period period) {
        // Nothing to do.
    }
}
