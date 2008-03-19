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
 * $Header: /src/voyager/com/volantis/mcs/repository/policycaches/LimitedCache.java,v 1.8 2002/08/07 16:47:36 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Aug-01    Doug            VBM:2001072503 - Storing SoftReferences to 
 *                              objects in the cache is now optional.The 
 *                              constructor takes a boolean 
 *                              - useReferenceCaching. If true the map is 
 *                              wrapped in a SoftMap which manages the storing
 *                              of objects as SoftReferences, if false objects
 *                              are stored as normal references.
 * 02-Jan-02    Paul            VBM:2002010201 - Fixed up header.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 28-Mar-02    Allan           VBM:2002022007 - Remove System.out.println in
 *                              put.
 * 07-Aug-02    Allan           VBM:2002080702 - Added a nullObjects ArrayList
 *                              and used this in pruneCache() to remove such
 *                              objects from the cache after the iteration 
 *                              over the cache is complete.
 * 07-May-03    Allan           VBM:2003050605 - Moved from MCS to Synergetics.
 * 20-May-03    Adrian          VBM:2003051901 - Construct with CacheStrategy 
 *                              rather than strategy name. Updated get and put 
 *                              to update the last hit time of the CacheObject.
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * A LimitedCache is a GenericCache that has a known limit on the number of
 * elements that are allowed in that cache. This limit is controlled by the
 * maxEntries property. When the limit is reached there has to be a strategy
 * for removing objects so as to make room for new objects in the cache. This
 * is the purpose of the strategy property. Objects in a LimitedCache do not
 * timeout.
 *
 * The current design of this cache allows for optimal performance of the put
 * and get operations. Where the limit is reached often increasing the freeup
 * size or the maxEntries might be a good idea. Alternatively this class could
 * be re-designed to emphasize performance for caches that do often reach their
 * limit - thereby reducing performance of the put and get operations.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class LimitedCache extends GenericCache {

    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2001. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(LimitedCache.class);

    /**
     * Default value for <code>freeupSize</code>.
     */
    private static final double DEFAULT_FREEUP_SIZE = 10.0;

    /**
     * The percentage of the cache to free-up each time the cache reaches its
     * maximum number of elements. For example a value of 1.0 would remove 1
     * percent of the objects in the cache each time the cache reached its
     * limit.
     */
    volatile private double freeupSize;

    /**
     * The {@link CacheStrategy} to use in this cache. volatile so that changes
     * to the strategy will be seen in a timely manner
     */
    volatile CacheStrategy cacheStrategy;

    /**
     * This is the lock that needs to be obtained before attempting to prune
     * the cache. It is not related to the cache or the synchronization
     * mechanisms used by the cache. It is used solely to ensure that only one
     * thread attempts to prune at a single time.
     */
    protected Object pruneLock = new Object();

    /**
     * Create a default Limited cache for backwards compatibility.
     *
     * @param maxEntries value for the maxEntries property of this cache
     * @param strategy   value for the strategy property of this cache
     */
    public LimitedCache(int maxEntries, CacheStrategy strategy) {
        setMaxEntries(maxEntries);
        setCacheStrategy(strategy);
    }

    /**
     * Construct a new LimitedCache with a default freeupSize
     *
     * @param factory    the factory used to produce the FutureResult objects
     *                   used by this cache.
     * @param maxEntries value for the maxEntries property of this cache
     * @param strategy   value for the strategy property of this cache
     */
    public LimitedCache(FutureResultFactory factory,
                        int maxEntries, CacheStrategy strategy) {
        this(factory, maxEntries, strategy, DEFAULT_FREEUP_SIZE);
    }

    /**
     * Construct a new LimitedCache
     *
     * @param factory    the factory used to produce the FutureResult objects
     *                   used by this cache.
     * @param maxEntries value for the maxEntries property of this cache
     * @param strategy   value for the strategy property of this cache
     * @param freeupSize the percentage of maxEntries number of object to
     *                   remove from the cache when the cache reaches its
     *                   maxEntries limit.
     */
    public LimitedCache(FutureResultFactory factory,
                        int maxEntries, CacheStrategy strategy,
                        double freeupSize) {
        super(factory);
        setMaxEntries(maxEntries);
        setFreeupSize(freeupSize);
        // Create the cache using a LeastUsedCacheStrategy to describe
        // how to sort the cache object.
        setCacheStrategy(strategy);

    }

    /**
     * Remove <code>freeupSize</code> objects from the cache.
     *
     * The current implementation will traverse the whole of the cache once and
     * store all the objects to remove. Then it will go through this collection
     * of objects and remove them. This is slow. The alternative is to maintain
     * the collection of objects to remove as objects are put into and got from
     * the cache. This method will make prunceCache quicker but it will make
     * put and get slower. If prune is not called very often - which hopefully
     * it won't be and if it is then maybe maxEntries or freeupSize should be
     * increased - the current implementation of pruneCache is probably better
     * for overall cache performance.
     *
     * @param keyNotToDelete
     */
    protected void pruneCache(Object keyNotToDelete) {
        int removeCount = (int) ((getFreeupSize() / 100.0) *
            (double) getMaxEntries());

        if (removeCount < 1) {
            removeCount = 1;
        }

        // this could probably be done better (using wait() rather then
        // spin locks. This lock does not stop reads or writes occuring to
        // the cache. It removes entries from the cache concurrent to reads
        // and writes.
        synchronized (pruneLock) {
            if (logger.isDebugEnabled()) {
                logger.debug("Pruning the cache: timestamp: "
                            + System.currentTimeMillis());
            }
            // cache.size() potentially produces more accurate result then
            // approximateSize so check approximate first then go for
            // the expensive size() method.
            if (size() >= getMaxEntries() &&
                cache.size() >= getMaxEntries()) {

                TreeSet deadObjects = new TreeSet(cacheStrategy);
                Iterator iterator = cache.keys();
                while (iterator.hasNext() && removeCount > 0) {
                    Object key = iterator.next();
                    if (!key.equals(keyNotToDelete)) {
                        // cacheObject cannot be null as the underlying Concurrent
                        // map does not support null values (or keys for that
                        // matter)
                        ReadThroughFutureResult co = cache.get(key, -1);

                        if (deadObjects.size() < removeCount) {
                            deadObjects.add(co);
                        } else {
                            ReadThroughFutureResult lastDead =
                                (ReadThroughFutureResult) deadObjects.last();

                            if (cacheStrategy.compare(lastDead, co) > 0) {
                                // lastDead is better than the current object so
                                // bring it back to life
                                deadObjects.remove(lastDead);

                                // ... and kill the current object
                                deadObjects.add(co);
                            }
                        }
                    }
                }

                // At this point deadObjects should hold all the objects
                // that we want to remove from the cache. So, remove them from the
                // cache.
                iterator = deadObjects.iterator();
                while (iterator.hasNext()) {
                    ReadThroughFutureResult co =
                        (ReadThroughFutureResult) iterator.next();
                    cache.remove(co.getKey());
                }
            }
            // ensure the approximate size is as accurate as possible. Calling
            // size() updates the approximateSize value.
            cache.size();
        } // exit the synch block

    }

    /**
     * Prune the cache if necessary. This occurs on the preGet as the get
     * method is now used to populate the cache.
     *
     * @param key The key of the object to be retrieved.
     * @return true If the get operation should continue and actually do the
     *         get; false otherwise.
     */
    public boolean preGet(Object key) {
        if (size() >= getMaxEntries()) {
            // Cache has probably reached its limit so prune it.
            pruneCache(key);
        }
        return true;
    }

    /**
     * Prune the cache if necessary. This performs a quick check to see if the
     * cache is probably over sized. If it thinks so then it goes on to perform
     * more rigorous and accurate tests.
     */
    // rest of javadoc inherited.
    public boolean prePut(ReadThroughFutureResult cacheObject) {
        if (size() >= getMaxEntries()) {
            // Cache has probably reached its limit so prune it.
            pruneCache(null);
        }
        return true;
    }

    /**
     * Get freeupSize.
     *
     * @return freeupSize.
     */
    protected double getFreeupSize() {
        return freeupSize;
    }

    /**
     * Set freeupSize
     *
     * @param freeupSize
     */
    protected void setFreeupSize(double freeupSize) {
        this.freeupSize = freeupSize;
    }

    /**
     * Get cacheStrategy.
     *
     * @return cacheStrategy.
     */
    protected CacheStrategy getCacheStrategy() {
        return cacheStrategy;
    }

    /**
     * Set cacheStrategy. If the new cacheStrategy is different from the
     * current then the deadObjects TreeSet is recreated using the new
     * cacheStrategy.
     *
     * @param cacheStrategy
     */
    protected void setCacheStrategy(CacheStrategy cacheStrategy) {
        if (cacheStrategy != null && !cacheStrategy.equals(getCacheStrategy())) {
            this.cacheStrategy = cacheStrategy;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Feb-05	379/4	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/2	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 05-Sep-03	53/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored - update internal variables, logging and comments

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/
