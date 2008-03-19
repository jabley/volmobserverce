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

package com.volantis.mcs.project;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.project.impl.ValidatingPolicyBuilderAccessor;

/**
 * Base for testing those methods that accept a {@link PolicyBuilder}.
 */ 
public abstract class ValidateBuilderTestAbstract
        extends ValidateNameTestAbstract {

    /**
     * Ensure that the method being tested checks for mismatch between builder
     * policy type and name.
     */
    public void testPolicyTypeMismatch() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ValidatingPolicyBuilderAccessor accessor =
                new ValidatingPolicyBuilderAccessor(accessorMock);

        try {
            invokeFailingMethod(accessor, "/valid.mimg", PolicyType.AUDIO);
            fail("Did not detect invalid extension");
        } catch (IllegalArgumentException expected) {
            assertEquals(
                    "Expected builder to have a policy type of Image based " +
                    "on the name of '/valid.mimg' but was Audio",
                    expected.getMessage());
        }
    }
}
