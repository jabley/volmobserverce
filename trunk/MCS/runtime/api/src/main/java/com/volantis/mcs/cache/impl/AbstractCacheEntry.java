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


package com.volantis.mcs.cache.impl;

import com.volantis.mcs.cache.CacheEntry;

/**
 * This class provide basic default behaviour for a @link CacheEntry.
 * <p>All classes that wish to implement @link CacheEntry should extend this
 * class.</p>
 */
public abstract class AbstractCacheEntry implements CacheEntry {

    /**
     * The object to be stored.
     */
    protected final Object cachedObject;
    
    /**
     * Time to live attribute.
     */
    protected final long timeToLive;

    /**
     * Create a new {@link CacheEntry} that will wrap the cached object.
     *
     * @param cachedObject The object to cache.
     */
    public AbstractCacheEntry(Object cachedObject) {
        this(cachedObject, -1);
    }

    /**
     * Create a new {@link CacheEntry} that will wrap the cached object.
     *
     * @param cachedObject The object to cache.
     * @param timeToLive The time to live attribute.
     */
    public AbstractCacheEntry(Object cachedObject, long timeToLive) {
        this.cachedObject = cachedObject;
        this.timeToLive = timeToLive;
    }

    // Javadoc inherited
    public Object getCachedObject() {
        return cachedObject;
    }

    // Javadoc inherited
    public long getTimeToLive() {
        return timeToLive;
    }
}
