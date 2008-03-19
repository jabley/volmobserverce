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
 * This interface describe a CacheEntry which can reside in a
 * {@link CacheStore}.
 * 
 * @mock.generate
 */

public interface CacheEntry {

    /**
     * Generate a new {@link CacheIdentity} with the given {@link CacheTime}
     * object, time-to-live value and sequence number.
     * <p>Given that the {@link CacheTime} object can potentialy return time
     * dependent data, it is highly likely that the @link CacheIdentity will
     * differ on each call</p>
     *
     * @param cacheTime The CacheTime object supplied by the {@link Cache} and
     * used for all Cache timings.
     * @param timeToLive number of milliseconds, that returned identity must be
     * considered as valid
     * @param sequenceNo The incrementing sequence number supplied by the
     * @link CacheStore.
     * @return a {@link CacheIdentity} that can be used as a key for this
     */
    public CacheIdentity generateIdentity(CacheTime cacheTime,
                                          long timeToLive,
                                          short sequenceNo);
    
    /**
     * Returns the Cached object.
     *
     * @return The cached object.
     */
    public Object getCachedObject();
    
    /**
     * Returns number of milliseconds, that this CacheEntry must be considered 
     * as valid (as not expired). Value Long.MAX_VALUE indicates, 
     * that this entry should be valid forever (should never expire).
     * Value -1 means, that the exact expiration moment is not important.
     * 
     * @return time to live.
     */
    public long getTimeToLive();
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
