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
 * This class provides a generalised cache module.
 * <p>A cache is an amalgamation of a CacheStore, which handles the storage and
 * retrieval of entries, and a CacheManager which is responsible for the
 * automation of cache entry expiration.</p>
 */
public class Cache {

    /**
     * The {@link CacheStore} that will act as the backing store for this cache.
     */
    protected CacheStore cacheStore;

    /**
     * The {@link CacheManager} that will provide automation of expiration for
     * this cache.
     */
    protected CacheManager cacheManager;

    /**
     * Instanciate a new Cache.
     *
     * @param cacheStore The {@link CacheStore} to use for storage of
     * {@link CacheEntry} objects.
     * @param cacheManager The manager used to handle expiration of
     * expired {@link CacheIdentity} objects.
     */
    public Cache (CacheStore cacheStore, CacheManager cacheManager) {
        this.cacheStore = cacheStore;
        this.cacheManager = cacheManager;
        cacheManager.scheduleFlush(cacheStore);

    }

    /**
     * Stores a {@link CacheEntry} into the @link CacheStore.
     *
     * @param cacheEntry The object to store
     * @return The {@link CacheIdentity} that can be used to retrieve this entry.
     */
    public CacheIdentity store (CacheEntry cacheEntry) {
        cacheManager.preStoreFlush(cacheStore, cacheEntry);
        return cacheStore.store(cacheEntry);
    }

    /**
     * Retrieve a {@link CacheEntry} from the cache.
     * <p>This method will retrieve the entry from the {@link CacheStore} and
     * execute the {@link CacheManager#postRetrieveFlush(CacheStore,
     * CacheIdentity)}
     * method.</p>
     *
     * @param cacheIdentity The {@link CacheIdentity} of the {@link CacheEntry}
     * to retrieve.
     *
     * @return The @linkCacheEntry or null if not found.
     */
    public CacheEntry retrieve(CacheIdentity cacheIdentity) {
        CacheEntry entry = cacheStore.retrieve(cacheIdentity);
        cacheManager.postRetrieveFlush(cacheStore, cacheIdentity);
        return entry;
    }

    /**
     * Clears all {@link CacheEntry} objects from the {@link CacheStore}.
     */
    public void clear() {
        cacheStore.clear();
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
