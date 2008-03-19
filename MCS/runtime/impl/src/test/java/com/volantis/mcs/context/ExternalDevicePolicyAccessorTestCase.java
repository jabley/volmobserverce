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

package com.volantis.mcs.context;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link ExternalDevicePolicyAccessor}.
 */
public class ExternalDevicePolicyAccessorTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that experimental policies are not visible but normal ones are. 
     */
    public void testGetPolicyValue()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MarinerPageContextMock contextMock =
                new MarinerPageContextMock("contextMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getDevicePolicyValue("normal").returns("value");

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DevicePolicyAccessor accessor =
                new ExternalDevicePolicyAccessor(contextMock);
        String value = accessor.getDevicePolicyValue("x-experimental");
        assertNull(value);

        value = accessor.getDevicePolicyValue("normal");
        assertEquals("value", value);
    }

}
