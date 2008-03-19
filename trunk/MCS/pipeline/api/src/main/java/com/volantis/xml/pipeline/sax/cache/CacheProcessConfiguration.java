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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.cache.Cache;
import com.volantis.cache.CacheBuilder;
import com.volantis.cache.CacheEntry;
import com.volantis.cache.CacheFactory;
import com.volantis.cache.expiration.ExpirationChecker;
import com.volantis.cache.group.GroupBuilder;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides the configuration information for the
 * {@link com.volantis.xml.pipeline.sax.cache.body.CacheBodyOperationProcess}.
 */
public class CacheProcessConfiguration implements Configuration {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(CacheProcessConfiguration.class);

    /**
     * The cache factory.
     */
    private static final CacheFactory CACHE_FACTORY =
            CacheFactory.getDefaultInstance();

    /**
     * The expiration checker used by the created caches.
     */
    private static final ExpirationChecker EXPIRATION_CHECKER =
            new ExpirationChecker() {

                // javadoc inherited
                public boolean hasExpired(SystemClock clock, CacheEntry entry) {
                    PipelineCacheState pcs =
                            (PipelineCacheState) entry.getExtensionObject();
                    return pcs.hasExpired(clock);
                }
            };


    /**
     * The main cache.
     */
    private final Cache cache;

    /**
     * True if the global expiry mode was set to fixed-age.
     */
    private boolean fixedExpiryMode;

    /**
     * Map between cache names and their default TTL values.
     */
    private Map defaultTimeToLives;

    /**
     * Constructs a new CacheProcessConfiguration instance with a maximum
     * number of entries of {@link Integer.MAX_VALUE}.
     */
    public CacheProcessConfiguration() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Constructs a new CacheProcessConfiguration instance with the specified
     * maximum number of entries.
     *
     * @param maxNumberOfEntries the maximum number of entries allowed in the
     *                           Cache.
     */
    public CacheProcessConfiguration(int maxNumberOfEntries) {
        final CacheBuilder builder = CACHE_FACTORY.createCacheBuilder();
        builder.setExpirationChecker(EXPIRATION_CHECKER);
        builder.setMaxCount(maxNumberOfEntries);
        cache = builder.buildCache();
        defaultTimeToLives = new HashMap();
        fixedExpiryMode = true;
    }

    /**
     * Create a cache with the specified name and maximum entry level.
     *
     * @param name       The name of the new cache.
     * @param maxEntries The maximum number of entries in the new cache.
     * @return The newly created cache.
     */
    public Cache createCache(String name, String maxEntries, String maxAge) {
        int maxCount;
        if ("unlimited".equals(maxEntries)) {
            maxCount = Integer.MAX_VALUE;
        } else {
            try {
                maxCount = Integer.parseInt(maxEntries);
            } catch (NumberFormatException nfe) {
                LOGGER.error("pipeline-invalid-max-count-value",
                    maxEntries, nfe);
                maxCount = Integer.MAX_VALUE;
            }
        }

        GroupBuilder groupBuilder = CACHE_FACTORY.createGroupBuilder();
        groupBuilder.setMaxCount(maxCount);

        cache.getRootGroup().addGroup(name, groupBuilder);

        final Period ttl;
        if ("unlimited".equals(maxAge)) {
            ttl = Period.INDEFINITELY;
        } else {
            int defaultMaxAge = 0;
            try {
                defaultMaxAge = Integer.parseInt(maxAge);
            } catch (NumberFormatException e) {
                // leave it to 0;
                LOGGER.error("pipeline-invalid-max-age-value", maxAge, e);
            }
            if (defaultMaxAge < 0) {
                ttl = Period.INDEFINITELY;
            } else {
                ttl = Period.inSeconds(defaultMaxAge);
            }
        }
        defaultTimeToLives.put(name, ttl);

        return cache;
    }

    /**
     * Return the Cache managed by this CacheProcessConfiguration instance.
     *
     * @return a Cache. Not null.
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * Sets the expiry mode.
     *
     * @param fixedExpiryMode true to set the expiry mode to fixed-age, false to
     * set it to auto
     */
    public void setFixedExpiryMode(final boolean fixedExpiryMode) {

        this.fixedExpiryMode = fixedExpiryMode;
    }

    /**
     * Returns true iff the expiry mode is fixed-age.
     *
     * @return the expiry mode
     */
    public boolean isFixedExpiryMode() {
        return fixedExpiryMode;
    }

    /**
     * Returns the default TTL value for the specified cache (group).
     *
     * @param cacheName the name of the cache (group)
     * @return the default TTL of null if no TTL is set for that cache (group)
     */
    public Period getDefaultTimeToLive(final String cacheName) {
        return (Period) defaultTimeToLives.get(cacheName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 24-Jun-04	761/1	claire	VBM:2004060803 Updated createCache to use a cache configuration object

 04-Aug-03	285/1	doug	VBM:2003080402 Renamed XMLProcessConfiguration interface to Configuration

 09-Jun-03	49/3	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 09-Jun-03	49/1	adrian	VBM:2003060505 Updated xml caching process to include cacheBody element

 ===========================================================================
*/
