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

import java.util.Set;

/**
 * This interface describes a generic Cache Store. The basic requirements are
 * to be able to store and retrieve items and to be able to flush entries
 * from the cache.
 *
 * @mock.generate
 */
public interface CacheStore {

    /**
     * Store the entry in the cache.
     * @param entry The entry to store in the cache.
     * @return The {@link CacheIdentity} that may be used to retrieve the
     * entry.
     */
    public CacheIdentity store(CacheEntry entry);

    /**
     * Retieve a previously stored entry from the cache.
     *
     * @param identity The {@link CacheIdentity} to retrieve .
     * @return The stored {@link CacheEntry} from the cache.
     */
    public CacheEntry retrieve(CacheIdentity identity);

    /**
     * This utility method provides the {@link CacheIdentity} of the oldest
     * object in the cache.
     * <p>This method would normally be used by a @link CacheManager to manage
     * flushing of the cache on a FIFO basis</p>
     *
     * @return The {@link CacheIdentity} of the oldest entry in the cache.
     */
    public  CacheIdentity retrieveOldestExpiredIdentity();

    /**
     * Get the next available sequence number for generating a cache key.
     * @return the next sequnce number
     */
    public short getNextSequenceNumber();

    /**
     * Removes the given entry from the cache if it exists.
     *
     * @param identity The {@link CacheIdentity} for the {@link CacheEntry}.
     */
    public void remove(CacheIdentity identity);

    /**
     * Indiscrimently remove all expired entries from the cache.
     */
    public void removeExpiredIdentities();

    /**
     * Indiscrimently clears the entire cache.
     */
    public void clear();

    /**
     * Indicates if the specified {@link CacheIdentity} is older than the
     * TimeToLive.
     *
     * @param identity The identity whose create time should be tested.
     * @return true if the identity is older than the time to live.
     */
    public boolean isExpired(CacheIdentity identity);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
