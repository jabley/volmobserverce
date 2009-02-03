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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.cache;

import com.volantis.cache.impl.CacheBuilderImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link CacheBuilder}.
 */
public class CacheBuilderTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that max count must be set.
     */
    public void testMaxCountNotSet() throws Exception {

        CacheBuilder builder = new CacheBuilderImpl();
        try {
            builder.buildCache();
            fail("Did not detect maxCount was not set.");
        } catch (IllegalStateException expected) {
            assertEquals("maxCount must be set", expected.getMessage());
        }
    }

    /**
     * Ensure that max count cannot be set to an invalid value.
     */
    public void testMaxCountSetInvalid() throws Exception {

        CacheBuilder builder = new CacheBuilderImpl();
        try {
            builder.setMaxCount(-1);
            fail("Did not detect maxCount was not set.");
        } catch (IllegalArgumentException expected) {
            assertEquals("maxCount must be > 0 but is -1", expected.getMessage());
        }
    }
}
