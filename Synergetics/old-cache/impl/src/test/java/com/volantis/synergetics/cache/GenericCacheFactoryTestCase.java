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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-May-03    Adrian          VBM:2003051901 - Added this to test
 *                              GenericCacheFactory.
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;


import junit.framework.TestCase;

/**
 * This class tests GenericCacheFactory
 */
public class GenericCacheFactoryTestCase extends TestCase {

    /**
     * Construct a new instance of GenericCacheFactoryTestCase
     */
    public GenericCacheFactoryTestCase() {
        super();
    }

    /**
     * Construct a new named instance of GenericCacheFactoryTestCase
     */
    public GenericCacheFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Test the createCache(String strategy, int maxEntries, int timeout)
     */
    public void testCreateCache() throws Exception {
        GenericCache cache = GenericCacheFactory.createCache(null, null, -1,
                                                             -1);
        assertTrue("Expected a BasicCache.",
                   cache.getClass() == BasicCache.class);

        cache = GenericCacheFactory.createCache(null, null, -1, 20);
        assertTrue("Expected a RefreshingCache.",
                   cache.getClass() == RefreshingCache.class);

        cache = GenericCacheFactory.createCache(null,
                                                GenericCacheConfiguration.LEAST_USED,
                                                100, -1);
        assertTrue("Expected a LimitedCache",
                   cache.getClass() == LimitedCache.class);
        assertTrue("Expected LimitedCache to have LeastUsedCacheStrategy.",
                   ((LimitedCache) cache).cacheStrategy.getClass() ==
                   LeastUsedCacheStrategy.class);

        cache = GenericCacheFactory.createCache(null,
                                                GenericCacheConfiguration.LEAST_RECENTLY_USED,
                                                100, -1);
        assertTrue("Expected a LimitedCache",
                   cache.getClass() == LimitedCache.class);
        assertTrue("Expected LimitedCache to have " +
                   "LeastRecentlyUsedCacheStrategy.",
                   ((LimitedCache) cache).cacheStrategy.getClass() ==
                   LeastRecentlyUsedCacheStrategy.class);

        cache = GenericCacheFactory.createCache(null,
                                                GenericCacheConfiguration.LEAST_USED,
                                                100, 20);
        assertTrue("Expected a ManagedCache",
                   cache.getClass() == ManagedCache.class);
        assertTrue("Expected ManagedCache to have LeastUsedCacheStrategy.",
                   ((ManagedCache) cache).cacheStrategy.getClass() ==
                   LeastUsedCacheStrategy.class);

        cache = GenericCacheFactory.createCache(null,
                                                GenericCacheConfiguration.LEAST_RECENTLY_USED,
                                                100, 20);
        assertTrue("Expected a ManagedCache",
                   cache.getClass() == ManagedCache.class);
        assertTrue("Expected ManagedCache to have " +
                   "LeastRecentlyUsedCacheStrategy.",
                   ((ManagedCache) cache).cacheStrategy.getClass() ==
                   LeastRecentlyUsedCacheStrategy.class);
    }

    /**
     * This tests the createCache(GenericCacheConfiguration config) method.  It
     * does this in a variety of ways by constructing the necessary cache
     * configurations.
     */
    public void testCreateCacheWithCacheConfiguration() throws Exception {
        // Test creating a BasicCache - this can use the default values of
        // the configuration object.
        GenericCacheConfiguration config = new GenericCacheConfiguration();
        GenericCache cache = GenericCacheFactory.createCache(null, config);
        assertTrue("Expected a BasicCache (Config).",
                   cache.getClass() == BasicCache.class);

        // Test creating a RefreshingCache
        config.setTimeout(20);
        cache = GenericCacheFactory.createCache(null, config);
        assertTrue("Expected a RefreshingCache (Config).",
                   cache.getClass() == RefreshingCache.class);

        // Test creating a LimitedCache with least-used strategy
        config.setTimeout(-1);
        config.setMaxEntries(100);
        config.setStrategy(GenericCacheConfiguration.LEAST_USED);
        cache = GenericCacheFactory.createCache(null, config);
        assertTrue("Expected a LimitedCache (Config #1).",
                   cache.getClass() == LimitedCache.class);
        assertTrue("Expected LimitedCache (Config) to have LeastUsedStrtagey",
                   ((LimitedCache) cache).cacheStrategy.getClass() ==
                   LeastUsedCacheStrategy.class);

        // Test creating a LimitedCache with least-recently-used strategy
        config.setStrategy(GenericCacheConfiguration.LEAST_RECENTLY_USED);
        cache = GenericCacheFactory.createCache(null, config);
        assertTrue("Expected a LimitedCache (Config #2).",
                   cache.getClass() == LimitedCache.class);
        assertTrue("Expected LimitedCache (Config) to have " +
                   "LeastRecentlyUsedStrtagey",
                   ((LimitedCache) cache).cacheStrategy.getClass() ==
                   LeastRecentlyUsedCacheStrategy.class);

        // Test creating a ManagedCache with least-used strategy
        config.setTimeout(20);
        config.setStrategy(GenericCacheConfiguration.LEAST_USED);
        cache = GenericCacheFactory.createCache(null, config);
        assertTrue("Expected a ManagedCache (Config #1).",
                   cache.getClass() == ManagedCache.class);
        assertTrue("Expected ManagedCache (Config) to have LeastUsedStrtagey",
                   ((LimitedCache) cache).cacheStrategy.getClass() ==
                   LeastUsedCacheStrategy.class);

        // Test creating a ManagedCache with least-recently-used strategy
        config.setStrategy(GenericCacheConfiguration.LEAST_RECENTLY_USED);
        cache = GenericCacheFactory.createCache(null, config);
        assertTrue("Expected a ManagedCache (Config #2).",
                   cache.getClass() == ManagedCache.class);
        assertTrue("Expected ManagedCache (Config)to have " +
                   "LeastRecentlyUsedStrtagey",
                   ((LimitedCache) cache).cacheStrategy.getClass() ==
                   LeastRecentlyUsedCacheStrategy.class);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 25-Jun-04	259/7	claire	VBM:2004060803 Refactored location of cache config related constants

 24-Jun-04	259/5	claire	VBM:2004060803 Renamed CacheConfiguration to GenericCacheConfiguration to avoid name clashes with a runtime cache configuration object

 21-Jun-04	259/3	claire	VBM:2004060803 CacheConfiguration implementation and integration

 ===========================================================================
*/
