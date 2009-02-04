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

package com.volantis.mcs.runtime;

import com.volantis.mcs.project.PolicySourceMock;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.policies.cache.SeparateCacheControlConstraintsMap;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link DynamicProjectKey}.
 */
public class DynamicProjectKeyTestCase
        extends TestCaseAbstract {

    public void testEquals() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PolicySourceMock policySourceMock =
                new PolicySourceMock("policySourceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        final SeparateCacheControlConstraintsMap cacheControlConstraintsMap =
                new SeparateCacheControlConstraintsMap(); 
        DynamicProjectKey key1 = new DynamicProjectKey(
                policySourceMock,
                new AssetsConfiguration(), "", cacheControlConstraintsMap);
        DynamicProjectKey key2 = new DynamicProjectKey(
                policySourceMock,
                new AssetsConfiguration(), "", cacheControlConstraintsMap);
        assertEquals(key1, key2);
    }

}
