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

package com.volantis.mcs.expression.functions.policy;

import com.volantis.mcs.expression.functions.PolicyIdentityFunctionTestAbstract;
import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.StringValueMock;

/**
 * Test cases for {@link GetUnbrandedPolicyNameFunction}.
 */
public class GetUnbrandedPolicyNameFunctionTestCase
        extends PolicyIdentityFunctionTestAbstract {

    /**
     * Test that when invoked the function creates the correct identity and
     * wraps it in a value.
     */
    public void testInvoke()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final StringValueMock valueMock =
                new StringValueMock("valueMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the value.
        valueMock.expects.stringValue().returns(valueMock).any();
        valueMock.expects.asJavaString()
                .returns(AUDIO_COMPONENT_NAME).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Function function = new GetUnbrandedPolicyNameFunction();

        doTestInvokeCreateIdentity(function, new Value[] {
            valueMock
        }, new PolicyIdentity(projectMock, "^" + AUDIO_COMPONENT_NAME, PolicyType.AUDIO));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/2	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 ===========================================================================
*/
