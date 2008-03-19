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

package com.volantis.mcs.cache;

/**
 * This class is the parent of all {@link CacheManager} implementations.
 * In itself this class does nothing to flush caches.
 *
 * @mock.generate
 */
public class CacheManager {


    /**
     * The monitoring interval for the schedular.
     */
    protected long interval;

    /**
     * Create a new {@link CacheManager} with a default monitoring interval of
     * 60 seconds.
     */
    public CacheManager() {
        this(60000);
    }

    /**
     * Create a new {@link CacheManager} with the supplied monitoring interval.
     *
     * @param interval The interval on which a background thread should wakeup.
     */
    public CacheManager(long interval) {
        this.interval = interval;
    }

    /**
     * Setup a schedule for flushes of the {@link CacheStore} (usually on a
     * background thread). The default behaviour is not to schedule flushing.
     *
     * @param cacheStore The {@link CacheStore} to flush.
     */
    public void scheduleFlush(final CacheStore cacheStore) {
    }

    /**
     * Perform flushing of the cache prior to storing a new entry into the
     * cache. The default behaviour is not to flush.
     *
     * @param cacheStore The {@link CacheStore} to flush.
     * @param cacheEntry The {@link CacheEntry} being stored.
     */
    public void preStoreFlush(CacheStore cacheStore,
                              CacheEntry cacheEntry) {
    }


    /**
     * Perform flushing of the cache after retrieving and entry from the Cache.
     * cache. The default behaviour is not to flush.
     *
     * @param cacheStore The {@link CacheStore} to flush.
     * @param cacheIdentity The {@link CacheIdentity} being retrieved.
     */
    public void postRetrieveFlush(CacheStore cacheStore, 
                                  CacheIdentity cacheIdentity) {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Nov-05	10208/1	ianw	VBM:2005110410 Partial backout of performance fix as not needed

 08-Nov-05	10210/1	ianw	VBM:2005110410 Improve performance issues with CSS cache

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
