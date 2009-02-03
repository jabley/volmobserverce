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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Feb-02    Geoff           VBM:2003010904 - Added this change history,
 *                              made property names public for ease of testing.
 * 07-May-03    Allan           VBM:2003050605 - Moved from MCS to Synergetics.
 * 20-May-03    Adrian          VBM:2003051901 - Added LEAST_RECENTLY_USED
 *                              strategy name member field.
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * The parent class for all Volantis internal cache classes.
 *
 * Decendents of this class <b>MUST</b> provide a Thread Safe map
 * implementation if they expect to be thread safe.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public abstract class GenericCache {

    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2001. ";

    /**
     * Default cache initial size setting.
     */
    protected static final int DEFAULT_INITIAL_CACHE_SIZE = 32;

    /**
     * Strategy property with regard to the order in which objects should be
     * removed from a cache when the maximum number of elements are reached.
     */
    protected String strategy;

    /**
     * Maximum number of elements allowed in this cache.
     */
    protected int maxEntries;

    /**
     * The default factory to use if one is not specified.
     */
    protected FutureResultFactory DEFAULT_FACTORY =
        new DefaultFutureResultFactory();

    /**
     * The cache. Setup in the constructors.
     */
    protected ReadThroughCache cache;

    /**
     * Create a new cache that uses the default factory. The default factory
     * causes the future result objects to return null when get is called on
     * them just like the original cache would
     */
    protected GenericCache() {
        this.cache = new ReadThroughCache(DEFAULT_FACTORY);
    }

    /**
     * Constructor that uses default value initial size and loadFactory.
     *
     * @param factory the factory to use. If this is null then a default
     *                factory is used that returns null. This is useful for
     *                backward compatibility.
     */
    protected GenericCache(FutureResultFactory factory) {
        if (factory == null) {
            factory = DEFAULT_FACTORY;
        }
        this.cache = new ReadThroughCache(factory);
    }

    /**
     * Construct a Generic Cache using the specified FutureResultFactory and
     * other parameters.
     *
     * @param factory         the factory to use. If this is null then a
     *                        default factory is used that returns null. This
     *                        is useful for backward compatibility.
     * @param initialCapacity the initial capacity of the cache
     * @param loadFactor      the loadfactor of the cache.
     */
    protected GenericCache(FutureResultFactory factory,
                           int initialCapacity,
                           float loadFactor) {
        if (factory == null) {
            factory = DEFAULT_FACTORY;
        }
        this.cache =
            new ReadThroughCache(factory, initialCapacity, loadFactor);
    }

    // javadoc unnecessary
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    // javadoc unnecessary
    public String getStrategy() {
        return strategy;
    }

    // javadoc unnecessary
    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    // javadoc unnecessary
    public int getMaxEntries() {
        return maxEntries;
    }

    /**
     * Set the timeout in seconds for objects in this cache. Note that
     * timeToLive on individual objects will override this value. A timeout of
     * -1 will unset this property.
     *
     * @param seconds The timeout in seconds or -1 to unset.
     */
    public void setTimeout(int seconds) {
        cache.getFutureResultFactory().setTimeout(seconds);
    }

    /**
     * Get the timeout in seconds for cached objects. A value of -1 indicates
     * that the timeout is not set. The timeToLive property on individual
     * cached objects overrides timeout.
     *
     * @return timeout in seconds of -1 if not set.
     */
    public int getTimeout() {
        return cache.getFutureResultFactory().getTimeout();
    }

    /**
     * Clear the cache. This is expensive so try not to use it.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * @return true if the cache is empty. This is expensive so try not to use
     *         it.
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * Remove an entry from the cache.
     *
     * @param key the key of the key/value pair to remove.
     * @return The value that was removed.
     */
    public Object remove(Object key) {
        return cache.remove(key);
    }

    /**
     * @return the approximate size of the cache
     */
    public int size() {
        return cache.approximateSize();
    }

    /**
     * Return the underlying ReadThroughFutureResult. This method should only
     * be called by clients that know what they are doing and cannot achieve
     * the results they desire through the normal mechanism of supplying a
     * FutureResultFactoy.
     *
     * Locking on the ReadThroughFutureResult provides item level locking
     * without hindering the performance of the cache. Note that locking on the
     * ReadThroughFutureResult does not mean that it cannot be removed from the
     * cache concurrently.
     *
     * @param key        The key that maps to this future result object
     * @param timeToLive
     * @return
     */
    final ReadThroughFutureResult getFutureResult(Object key, int timeToLive) {
        ReadThroughFutureResult result = null;
        if (preGet(key)) {
            result = cache.get(key, timeToLive);
            result.setHitCount(result.getHitCount() + 1);
            result.setLastHitTime(System.currentTimeMillis());
        }
        return result;
    }

    /**
     * Get a specified object from the cache. This method is final because it
     * is generic. It will update all the necessary properties of the cache
     * object that is retrieved such as hitCount and lastHitTime and will
     * consider timeToLive when doing the retrieve. Caches that need to do
     * additional processing should override preGet() - called before the
     * object is retrieved from the cache - and/or postGet() called after the
     * object is retreived from the cache.
     *
     * @param key        The key of the object to retreive from the cahce.
     * @param timeToLive the period of time this object should remain valid
     *                   for.
     * @return The cached object or null if not found or the object timed out.
     *
     * @throws UndeclaredThrowableException on error.
     */
    public final Object get(Object key, int timeToLive) {

        Object returnValue = null;
        try {
            ReadThroughFutureResult cacheObject = null;
            if (preGet(key)) {
                cacheObject = cache.get(key, timeToLive);
                if (cacheObject == null) {
                    return null;
                } else {
                    cacheObject.setHitCount(cacheObject.getHitCount() + 1);
                    cacheObject.setLastHitTime(System.currentTimeMillis());
                    returnValue = cacheObject.getValue(key);
                }
            }

            postGet(key, cacheObject);
        } catch (InterruptedException ie) {
            throw new UndeclaredThrowableException(ie);
        } catch (InvocationTargetException ite) {
            ite.getTargetException().printStackTrace();
            throw new UndeclaredThrowableException(ite);
        }
        return returnValue;
    }

    /**
     * Get a specified object from the cache. This method is final because it
     * is generic. It will update all the necessary properties of the cache
     * object that is retrieved such as hitCount and lastHitTime and will
     * consider timeToLive when doing the retrieve. Caches that need to do
     * additional processing should override preGet() - called before the
     * object is retrieved from the cache - and/or postGet() called after the
     * object is retreived from the cache.
     *
     * Get the value of the specified mapping from the cache. This method will
     * use DataAccessors obtained from the internal FutureResultFactory
     * implementation.
     *
     * @param key the key used to obtain the object.
     * @return the object mapped to the key.
     *
     * @throws UndeclaredThrowableException on error.
     */
    public final Object get(Object key) {
        return get(key, getTimeout());
    }

    /**
     * Put the specified object into the cache. Caches that need to do
     * additional processing should override prePut() - called before the
     * object goes into the cache - and/or postPut() called after the object
     * goes into the cache.
     *
     * @param key   The key for the value.
     * @param value The value.
     * @return The Object replaced by the put operation or null if none were
     *         replaced.
     *
     * @throws UndeclaredThrowableException on error.
     * @deprecated This should no longer be used. It is only here for backwards
     *             compatibility. The get methods are now used to populate the
     *             cache via DataAccessor objects created by a
     *             FutureResultFactory instance.
     */
    public final Object put(Object key, Object value) {
        return put(key, value, -1);
    }

    /**
     * Put the specified object into the cache. Caches that need to do
     * additional processing should override prePut() - called before the
     * object goes into the cache - and/or postPut() called after the object
     * goes into the cache.
     *
     * @param key        The key for the value.
     * @param value      The value.
     * @param timeToLive The time that the object should be cached for in
     *                   seconds. A value of -1 indicates that this property is
     *                   not set.
     * @return The Object replaced by the put operation or null if none were
     *         replaced.
     *
     * @throws UndeclaredThrowableException on error.
     * @deprecated This should no longer be used. The get methods are now used
     *             to populate the cache via DataAccessor objects created by a
     *             FutureResultFactory instance.
     */
    public final Object put(Object key, Object value, int timeToLive) {

        Object replacedValue = null;
        ReadThroughFutureResult replaced = null;
        // create an object to put in the cache with a trivial DataAccessor object
        ReadThroughFutureResult cacheObject =
            cache.getFutureResultFactory().
            createDirectValueFutureResult(key, value, timeToLive);
        try {
            if (prePut(cacheObject)) {
                replaced = cache.put(key, cacheObject);
                // call getValue to ensure that the entity is populated.
                cacheObject.getValue(key);
            }
            postPut(cacheObject, replaced);

            if (replaced != null) {
                replacedValue = replaced.getValue(key);
            }
        } catch (InterruptedException ie) {
            throw new UndeclaredThrowableException(ie);
        } catch (InvocationTargetException ite) {
            throw new UndeclaredThrowableException(ite);
        }
        return replacedValue;
    }

    /**
     * Called by get() prior to getting the object from cache.
     *
     * @param key The key of the object to be retrieved.
     * @return true If the get operation should continue and actually do the
     *         get; false otherwise.
     */
    protected boolean preGet(Object key) {
        return true;
    }

    /**
     * Called by get() after getting the object from the cache.
     *
     * @param key         The key used to get the object.
     * @param cacheObject The object that has just been got from the cache, may
     *                    be null.
     */
    protected void postGet(Object key, ReadThroughFutureResult cacheObject) {
    }

    /**
     * Called by put() prior to putting the object into the cache.
     *
     * @param cacheObject The object about to be put into the cache.
     * @return true If the put operation should continue and actually do the
     *         put; false otherwise.
     *
     * @deprecated This is only here because put is still around. Remove this
     *             when put is finally removed.
     */
    protected boolean prePut(ReadThroughFutureResult cacheObject) {
        return true;
    }

    /**
     * Called by put() immeadiate after putting the object into the cache.
     *
     * @param cacheObject    The object that has just been put into the cache.
     * @param replacedObject The object that was replaced by the put operation,
     *                       may be null.
     * @deprecated This is only here because put is still around. Remove this
     *             when put is finally removed.
     */
    protected void postPut(ReadThroughFutureResult cacheObject,
                           ReadThroughFutureResult replacedObject) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	397/1	matthew	VBM:2005020308 Implement CachingPluggableHTTPManager

 14-Feb-05	391/3	matthew	VBM:2005020308 More minor changes

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 03-Feb-05	379/16	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/14	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/11	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/9	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/7	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/5	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 25-Jun-04	259/3	claire	VBM:2004060803 Refactored location of cache config related constants

 24-Jun-04	259/1	claire	VBM:2004060803 Updated cache configuration to handle unlimited strings from the config due to xsd change

 20-May-04	209/1	geoff	VBM:2004051809 pre populate policy caches

 20-May-04	207/1	geoff	VBM:2004051809 pre populate policy caches (commit for proteus visibility)

 19-Dec-03	129/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements (update GenericCache.LEAST_USED)

 26-Sep-03	87/1	steve	VBM:2003091604 Single Clock thread

 05-Sep-03	53/3	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored - update internal variables, logging and comments

 21-Aug-03	53/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 07-Aug-03	40/1	allan	VBM:2003080501 Add FOREVER constant

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/
