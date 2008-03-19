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

package com.volantis.mcs.ibm.websphere.mcsi.expression.functions;

import com.volantis.mcs.expression.functions.PolicyIdentityFunctionTestAbstract;
import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.StringValueMock;

/**
 * Test cases for {@link PolicyFunction}.
 */
public class PolicyFunctionTestCase
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

        final StringValueMock projectValueMock =
                new StringValueMock("projectValueMock", expectations);

        final StringValueMock componentValueMock =
                new StringValueMock("componentValueMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the values.
        projectValueMock.expects.stringValue()
                .returns(projectValueMock).any();
        projectValueMock.expects.asJavaString().returns(PROJECT_NAME).any();
        componentValueMock.expects.stringValue()
                .returns(componentValueMock).any();
        componentValueMock.expects.asJavaString().returns(AUDIO_COMPONENT_NAME).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Function function = new PolicyFunction();

        doTestInvokeCreateIdentity(function, new Value[] {
            projectValueMock, componentValueMock
        }, new PolicyIdentity(AUDIO_COMPONENT_NAME, PolicyType.AUDIO));

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
