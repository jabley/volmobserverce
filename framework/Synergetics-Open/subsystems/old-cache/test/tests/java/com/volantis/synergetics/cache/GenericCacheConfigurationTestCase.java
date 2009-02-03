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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

import junit.framework.TestCase;

/**
 * This test GenericCacheConfiguration.
 */
public class GenericCacheConfigurationTestCase extends TestCase {

    /**
     * Construct a new instance of GenericCacheConfigurationTestCase
     */
    public GenericCacheConfigurationTestCase() {
        super();
    }

    /**
     * Construct a new named instance of GenericCacheConfigurationTestCase
     */
    public GenericCacheConfigurationTestCase(String name) {
        super(name);
    }

    /**
     * This tests the get/set of maxEntries
     */
    public void testMaxEntries() throws Exception {
        // Create a test instance
        GenericCacheConfiguration config = new GenericCacheConfiguration();

        // Test default
        assertTrue("Should be a default max entries of -1",
                   config.getMaxEntries() == -1);

        // Set a value
        int max = 20;
        config.setMaxEntries(max);

        // Test that the value has been set correctly
        assertTrue("Should be max entries of 20",
                   config.getMaxEntries() == 20);

        // Set a value
        max = 100;
        config.setMaxEntries(max);

        // Test that the value has been set correctly
        assertTrue("Should be max entries of 100",
                   config.getMaxEntries() == 100);

        // Test unlimited string
        config.setMaxEntries(GenericCacheConfiguration.UNLIMITED);

        // Test that the value has been set correctly
        assertTrue("Should be max entries of " +
                   GenericCacheConfiguration.UNLIMITED,
                   config.getMaxEntries() ==
                   GenericCacheConfiguration.FOREVER);
    }

    /**
     * This tests the get/set of strategy
     */
    public void testStrategy() throws Exception {
        // Create a test instance
        GenericCacheConfiguration config = new GenericCacheConfiguration();

        // Test default
        assertTrue("Should be a default strategy of -1",
                   config.getStrategy().equals("-1"));

        // Set a value
        String strategy = GenericCacheConfiguration.LEAST_RECENTLY_USED;
        config.setStrategy(strategy);

        // Test that the value has been set correctly
        assertTrue("Should be strategy of " +
                   GenericCacheConfiguration.LEAST_RECENTLY_USED,
                   config.getStrategy().equals(
                       GenericCacheConfiguration.LEAST_RECENTLY_USED));

        // Set a value
        strategy = GenericCacheConfiguration.LEAST_USED;
        config.setStrategy(strategy);

        // Test that the value has been set correctly
        assertTrue("Should be strategy of " +
                   GenericCacheConfiguration.LEAST_USED,
                   config.getStrategy().equals(
                       GenericCacheConfiguration.LEAST_USED));
    }

    /**
     * This tests the get/set of timeout
     */
    public void testTimeout() throws Exception {
        // Create a test instance
        GenericCacheConfiguration config = new GenericCacheConfiguration();

        // Test default
        assertTrue("Should be a default timeout of -1",
                   config.getTimeout() == -1);

        // Set a value
        int time = 20;
        config.setTimeout(time);

        // Test that the value has been set correctly
        assertTrue("Should be timeout of 20",
                   config.getTimeout() == 20);

        // Set a value
        time = 100;
        config.setTimeout(time);

        // Test that the value has been set correctly
        assertTrue("Should be timeout of 100",
                   config.getTimeout() == 100);

        // Test unlimited string
        config.setTimeout(GenericCacheConfiguration.UNLIMITED);

        // Test that the value has been set correctly
        assertTrue("Should be timeout of " +
                   GenericCacheConfiguration.UNLIMITED,
                   config.getTimeout() == GenericCacheConfiguration.FOREVER);
    }

    /**
     * This tests the is/set of requestReferenceCaching
     */
    public void testRequestsReferenceCaching() throws Exception {
        // Create a test instance
        GenericCacheConfiguration config = new GenericCacheConfiguration();

        // Test default
        assertFalse("Should be a default request reference caching of false",
                    config.isRequestReferenceCaching());

        // Set a value
        boolean reference = true;
        config.setRequestReferenceCaching(reference);

        // Test that the value has been set correctly
        assertTrue("Should be a default request reference caching of true",
                   config.isRequestReferenceCaching());

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jun-04	259/10	claire	VBM:2004060803 Refactored location of cache config related constants

 24-Jun-04	259/8	claire	VBM:2004060803 Renamed CacheConfiguration to GenericCacheConfiguration to avoid name clashes with a runtime cache configuration object

 24-Jun-04	259/3	claire	VBM:2004060803 Updated cache configuration to handle unlimited strings from the config due to xsd change

 21-Jun-04	259/1	claire	VBM:2004060803 CacheConfiguration implementation and integration

 ===========================================================================
*/
