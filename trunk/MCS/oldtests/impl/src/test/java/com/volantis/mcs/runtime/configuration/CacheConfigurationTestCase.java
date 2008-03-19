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

package com.volantis.mcs.runtime.configuration;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the holding of configuration information about caches.
 */
public class CacheConfigurationTestCase extends TestCaseAbstract {

    /**
     * Initialise a new instance of this test case.
     */
    public CacheConfigurationTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public CacheConfigurationTestCase(String s) {
        super(s);
    }

    /**
     * Test the set/get name methods.
     */
    public void testName() throws Exception {
        CacheConfiguration config = new CacheConfiguration();
        String value = "name";
        config.setName(value);
        String result = config.getName();
        assertNotNull("Result should not be null", result);
        assertEquals("Initial value and result should match", value, result);
    }

    /**
     * Test the set/get strategy methods.
     */
    public void testStrategy() throws Exception {
        CacheConfiguration config = new CacheConfiguration();
        String value = "strategy";
        config.setStrategy(value);
        String result = config.getStrategy();
        assertNotNull("Result should not be null", result);
        assertEquals("Initial value and result should match", value, result);
    }

    /**
     * Test the set/get max entries methods.
     */
    public void testMaxEntries() throws Exception {
        CacheConfiguration config = new CacheConfiguration();
        config.setMaxEntries(CacheConfiguration.UNLIMITED);
        String result = config.getMaxEntries();
        assertNotNull("Result should not be null", result);
        assertEquals("Initial value and result should match",
                CacheConfiguration.UNLIMITED, result);
    }

    /**
     * Test the set/get max age methods.
     */
    public void testMaxAge() throws Exception {
        CacheConfiguration config = new CacheConfiguration();
        config.setMaxAge(CacheConfiguration.UNLIMITED);
        String result = config.getMaxAge();
        assertNotNull("Result should not be null", result);
        assertEquals("Initial value and result should match",
                CacheConfiguration.UNLIMITED, result);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 ===========================================================================
*/
