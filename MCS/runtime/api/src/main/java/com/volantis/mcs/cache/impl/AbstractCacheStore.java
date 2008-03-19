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

import com.volantis.mcs.cache.CacheStore;
import com.volantis.mcs.cache.CacheTime;
import com.volantis.mcs.cache.CacheIdentity;

/**
 * This class provides some default behaviour for a @link CacheStore.
 * <p>Any classes wishing to implement the @link CacheStore interface should
 * extend this class</p>
 */
public abstract class AbstractCacheStore implements CacheStore {

    /**
     * The time to live for an entry in the cache. Any entry older than this
     * time should be considered as eligable for removal. 
     * Value Long.MAX_VALUE means live-forever.
     */
    protected long timeToLive;

    /**
     * The cacheTime object which will be used to derive any time sensitive
     * data for the cache.
     */
    protected CacheTime cacheTime;

   /**
    * The sequence number which should be incremented on storage of an entry in
    * the cache.
    */
    protected short sequenceNumber;

    /**
     * Protect our default constructor as we need a @link CacheTime object and
     * a TTL as a minimum.
     */
    private AbstractCacheStore() {

    }

    /**
     * Create a new {@link CacheStore}.
     * @param cacheTime The {@link CacheTime} used to derive all Cache timings.
     * @param timeToLive The amount of time in milliseconds that a entry can
     * remain in cache and be conidered as current.
     */
    protected AbstractCacheStore(CacheTime cacheTime, long timeToLive) {

        this.cacheTime = cacheTime;
        this.timeToLive = timeToLive;

        sequenceNumber = 0;
    }

    //Javadoc inherited
    public synchronized short getNextSequenceNumber() {
        return sequenceNumber++;
    }

    //Javadoc inherited
    public boolean isExpired(CacheIdentity identity) {
        return (cacheTime.getTimeInMillis() >= identity.getExpiresTime());
    }

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
