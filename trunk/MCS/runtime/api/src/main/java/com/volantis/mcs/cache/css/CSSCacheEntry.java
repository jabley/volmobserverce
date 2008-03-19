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


package com.volantis.mcs.cache.css;

import com.volantis.mcs.cache.impl.AbstractCacheEntry;
import com.volantis.mcs.cache.CacheIdentity;
import com.volantis.mcs.cache.CacheTime;
import com.volantis.mcs.runtime.css.WritableCSSEntity;

/**
 * This class provides a specialisation of a @link CacheEntry which may be used
 * to store {@link WritableCSSEntity} objects.
 */
public class CSSCacheEntry extends AbstractCacheEntry {

    /**
     * Create a new CSSCacheEntry containing the {@link WritableCSSEntity}.
     *
     * @param cachedObject The {@link WritableCSSEntity} to cache.
     */
    public CSSCacheEntry(WritableCSSEntity cachedObject) {
        super(cachedObject);
    }

    /**
     * Create a new CSSCacheEntry containing the {@link WritableCSSEntity}.
     *
     * @param cachedObject The {@link WritableCSSEntity} to cache.
     * @param timeToLive The time to live attribute of an entry.
     */
    public CSSCacheEntry(WritableCSSEntity cachedObject, long timeToLive) {
        super(cachedObject, timeToLive);
    }

    // Javadoc inherited
    public CacheIdentity generateIdentity(CacheTime cacheTime, long timeToLive, short sequenceNo) {
        // Get cacheTime for new cache identity
        long identityCacheTime = cacheTime.getTimeInMillis();
        
        // Calculate expires time of cache identity.
        long identityExpiresTime = (timeToLive != Long.MAX_VALUE) ? 
                identityCacheTime + timeToLive : Long.MAX_VALUE;
        
        // Create and return new cache identity.
        return new CSSCacheIdentity(identityCacheTime, identityExpiresTime, sequenceNo);
    }

    /**
     * Get the {@link WritableCSSEntity} wrapped by this
     * {@link com.volantis.mcs.cache.CacheEntry}.
     *
     * @return The {@link WritableCSSEntity} object.
     */
    public WritableCSSEntity getCacheObjectAsWritableCSSEntity() {
        return (WritableCSSEntity)getCachedObject();
    }
}
