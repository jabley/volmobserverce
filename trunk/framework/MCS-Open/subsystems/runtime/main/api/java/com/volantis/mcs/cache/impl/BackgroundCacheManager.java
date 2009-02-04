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

import com.volantis.mcs.cache.CacheManager;
import com.volantis.mcs.cache.CacheStore;
import com.volantis.mcs.cache.CacheIdentity;

import java.util.Set;
import java.util.Iterator;

/**
 * This @link CacheManager provides a simple background scheduled flush of
 * expired cache entries on a  selected interval and after an entry has been
 * retrieved.
 *  */
public class BackgroundCacheManager extends CacheManager {

    /**
     * Create a new {@link BackgroundCacheManager} with a default monitoring
     * interval of 60 seconds.
     */
    public BackgroundCacheManager() {
        super();
    }

    /**
     * Create a new {@link BackgroundCacheManager} with the supplied monitoring
     * interval.
     *
     * @param interval The interval on which a background thread should wakeup.
     */
    public BackgroundCacheManager(long interval) {
        super(interval);
    }

    /**
     * Setup a schedule for flushes of the {@link CacheStore} (usually on a
     * background thread). All expired entries will be flushed on the interval.
     *
     * @param cacheStore The {@link CacheStore} to flush.
     */
    public void scheduleFlush(final CacheStore cacheStore) {
        Thread backGroundThread = new Thread() {
            public void run() {
                while (true) {
                    cacheStore.removeExpiredIdentities();
                    synchronized(this) {
                        try {
                                wait(interval);
                        } catch (InterruptedException e) {
                            //We don't care if we got interupted
                        }
                    }
                }
            }
        };
        backGroundThread.start();
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Nov-05	10208/1	ianw	VBM:2005110410 Partial backout of performance fix as not needed

 10-Nov-05	10210/3	ianw	VBM:2005110410 Partial backout of performance fix as not needed

 08-Nov-05	10210/1	ianw	VBM:2005110410 Improve performance issues with CSS cache

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
