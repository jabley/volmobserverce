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
 * (c) Volantis Systems Ltd 20065. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.cache;

import com.volantis.mcs.runtime.policies.cache.DeprecatedPolicyCacheFlusherImpl;
import com.volantis.mcs.runtime.policies.cache.PolicyCacheMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Unit test case for PolicyCacheMapImpl
 */
public class DeprecatedPolicyCacheFlusherImplTestCase
        extends TestCaseAbstract {
    
    private DeprecatedPolicyCacheFlusherImpl testee;
    private PolicyCacheMock policyCacheMock;

    protected void setUp() throws Exception {
        super.setUp();

        policyCacheMock = new PolicyCacheMock("policyCacheMock", expectations);

        // Prepare an object to test
        testee = new DeprecatedPolicyCacheFlusherImpl(policyCacheMock);
    }
    
    /**
     * Ensure that flush calls the correct method on the underlying cache.
     */
    public void testFlush() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================
        policyCacheMock.expects.flush(null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        testee.flush(null);
    }
}
