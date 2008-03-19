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
import com.volantis.mcs.project.impl.ValidatingPolicyBuilderAccessor;
import com.volantis.mcs.repository.RepositoryException;

/**
 * Test the {@link ValidatingPolicyBuilderAccessor#addPolicyBuilder} method.
 */
public class AddMethodTestCase
        extends ValidateBuilderTestAbstract {

    // Javadoc inherited.
    protected void invokeFailingMethod(
            ValidatingPolicyBuilderAccessor accessor, String name,
            PolicyType policyType)
            throws RepositoryException {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        policyBuilderMock.expects.getName().returns(name).any();
        policyBuilderMock.expects.getPolicyType()
                .returns(policyType).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        accessor.addPolicyBuilder(connectionMock, projectMock,
                policyBuilderMock);
    }

    // Javadoc inherited.
    protected void invokeOkMethod(
            ValidatingPolicyBuilderAccessor accessor, String name,
            PolicyType policyType)
            throws RepositoryException {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        policyBuilderMock.expects.getName().returns(name).any();
        policyBuilderMock.expects.getPolicyType()
                .returns(policyType).any();

        accessorMock.expects
                .addPolicyBuilder(connectionMock, projectMock,
                        policyBuilderMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        accessor.addPolicyBuilder(connectionMock, projectMock,
                policyBuilderMock);
    }
}
