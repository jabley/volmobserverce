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
 * $Header: /src/voyager/com/volantis/mcs/repository/policycaches/GenericCacheFactory.java,v 1.7 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Aug-01     Doug           VBM:2001072503 - If using the Sun JVM we now
 *                              specify explicitly that we do not want the
 *                              policy caches to store object as
 *                              softReferences. This is because there is a
 *                              bug in the Sun jvm that causes SoftReferences
 *                              to be garbage collected ever time the GC runs.
 *                              Any other JVM vendor and we use Soft caching
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 07-May-03    Allan           VBM:2003050605 - Moved from MCS to Synergetics.
 * 20-May-03    Adrian          VBM:2003051901 - Updated createCache(String,
 *                              int, int) to construct limited caches with
 *                              CacheStrategy
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Map;

/**
 * Factory class for creating GenericCache objects based upon the strategy,
 * maxEntries and timeout properties.
 *
 * @author Allan Boyd
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class GenericCacheFactory {

    /**
     * Volantis copyright object.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2001. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(GenericCacheFactory.class);


    /**
     * Comaptibility method see createCache(FutureResultFactory, String, int,
     * int) where params are (null, null, -1, -1)
     */
    public static GenericCache createCache() {
        return createCache(null, null, -1, -1);
    }

    /**
     * Comaptibility method see createCache(FutureResultFactory, String, int,
     * int)
     */
    public static GenericCache createCache(String strategy,
                                           int maxEntries,
                                           int timeout) {
        return createCache(null, null, maxEntries, timeout);
    }

    /**
     * Compatibility method. see createCache(FutureResultFactory, Map)
     */
    public static GenericCache createCache(Map map) {
        return createCache(null, map);
    }

    /**
     * Compatibility method. see createCache(FutureResultFactory,
     * GenericCahceConfiguration)
     */
    public static GenericCache createCache(GenericCacheConfiguration config) {
        return createCache(null, config);
    }

    /**
     * Create a new GenericCache.
     *
     * @param factory    a future result factory to use. If null then a default
     *                   future result factory will be used.
     * @param strategy   the strategy required for the cache - -1 is no
     *                   strategy
     * @param maxEntries the maxEntries allowed in the cache - -1 is no limit
     *                   on maxEntries
     * @param timeout    the timeout required for the cache - -1 is no timeout
     *                   to store the cache entries if the underlying JVM
     *                   supports them.
     * @return GenericCache created according to the specified attributes
     */
    public static GenericCache createCache(FutureResultFactory factory,
                                           String strategy,
                                           int maxEntries,
                                           int timeout) {

        if (maxEntries == -1 && timeout == -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Created a BasicCache");
            }
            return new BasicCache(factory);
        }

        if (maxEntries == -1 && timeout != -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Created a RefreshingCache with timeout"
                             + " of " + timeout);
            }
            return new RefreshingCache(factory, timeout);
        }

        CacheStrategy cacheStrategy =
            CacheStrategyFactory.getCacheStrategy(strategy);

        if (maxEntries != -1 && timeout == -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Created a LimitedCache with maxEntries"
                             + " of " + maxEntries + " and a strategy of "
                             + strategy);
            }

            return new LimitedCache(factory, maxEntries, cacheStrategy);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Created a ManagedCache with timeout"
                         + " of " + timeout + ", a maxEntries of "
                         + maxEntries + " and a strategy of " + strategy);
        }

        return new ManagedCache(factory, maxEntries, cacheStrategy, timeout);
    }

    /**
     * Create a new GenericCache using attributes from a Map. If attributes is
     * null, null is returned. If attributes has some missing attribute, those
     * attributes are assigned default values.
     *
     * @param factory    a future result factory to use. If null then a default
     *                   future result factory will be used.
     * @param attributes a Map of of attributes for the cache
     * @return a GenericCache with the given attributes
     */
    public static GenericCache createCache(FutureResultFactory factory,
                                           Map attributes) {
        String value, strategy = "-1";
        int maxEntries = -1, timeout = -1;

        if (attributes == null) {
            return null;
        }

        strategy =
            (String) attributes.get(GenericCacheConfiguration.STRATEGY_NAME);

        value =
            (String) attributes.get(GenericCacheConfiguration.MAXENTRIES_NAME);
        if (value != null) {
            maxEntries = Integer.parseInt(value);
        }

        value =
            (String) attributes.get(GenericCacheConfiguration.TIMEOUT_NAME);
        if (value != null) {
            timeout = Integer.parseInt(value);
        }

        return createCache(factory, strategy, maxEntries, timeout);
    }


    /**
     * Create a new GenericCache using the configuration options provided in
     * the given cache configuration.  If this configuration provided is null
     * then null is returned.  Any missing values within the configuration will
     * revert to default values as defined within the cache configuration
     * object.
     *
     * @param factory a future result factory to use. If null then a default
     *                future result factory will be used.
     * @param config  The cache configuration to use as a basis on which a
     *                GenericCache is returned.  The values provided here
     *                influence the cache implementation chosen.
     * @return A GenericCache based on the given configuration.
     */
    public static GenericCache createCache(FutureResultFactory factory,
                                           GenericCacheConfiguration config) {
        // Check whether the configuration provided is null and return
        // a null cache if so.  This is also logged to a debug log.
        if (config == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Null cache configuration provided so a null" +
                             " cache returned has been returned.");
            }
        }

        // Obtain the maximum entries, strategy and timout values to use to
        // decide on the cache to create.
        String strategy = config.getStrategy();
        int maxEntries = config.getMaxEntries();
        int timeout = config.getTimeout();

        // Create a basic cache as unlimited entries and timeout
        if (maxEntries == -1 && timeout == -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Created a BasicCache");
            }
            return new BasicCache(factory);
        }

        // Unlimited entries, but a limit on the timeout so need to create a
        // refreshing cache
        if (maxEntries == -1 && timeout != -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Created a RefreshingCache with timeout"
                             + " of " + timeout);
            }
            return new RefreshingCache(factory, timeout);
        }

        // Obtain a strategy based on the value in the configuration
        CacheStrategy cacheStrategy =
            CacheStrategyFactory.getCacheStrategy(strategy);

        // Finite number of entries, but with no timeout on the contents of
        // the cache so create a LimitedCache
        if (maxEntries != -1 && timeout == -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Created a LimitedCache with maxEntries"
                             + " of " + maxEntries + " and a strategy of "
                             + strategy);
            }

            return new LimitedCache(factory, maxEntries, cacheStrategy);
        }

        // The default case of a managed cache if all of the other tests
        // are not true.
        if (logger.isDebugEnabled()) {
            logger.debug("Created a ManagedCache with timeout"
                         + " of " + timeout + ", a maxEntries of "
                         + maxEntries + " and a strategy of " + strategy);
        }

        return new ManagedCache(factory, maxEntries, cacheStrategy, timeout);

    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Feb-05	379/9	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/7	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/5	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/3	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 29-Nov-04	343/1	doug	VBM:2004111702 Refactored Logging framework

 25-Jun-04	259/9	claire	VBM:2004060803 Refactored location of cache config related constants

 24-Jun-04	259/7	claire	VBM:2004060803 Renamed CacheConfiguration to GenericCacheConfiguration to avoid name clashes with a runtime cache configuration object

 22-Jun-04	259/5	claire	VBM:2004060803 CacheConfiguration - update JavaDoc

 21-Jun-04	259/3	claire	VBM:2004060803 CacheConfiguration implementation and integration

 20-May-04	209/1	geoff	VBM:2004051809 pre populate policy caches

 05-Sep-03	53/3	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored - update internal variables, logging and comments

 21-Aug-03	53/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 ===========================================================================
*/
