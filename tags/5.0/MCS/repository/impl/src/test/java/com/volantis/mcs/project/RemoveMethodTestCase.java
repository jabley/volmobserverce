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

import com.volantis.mcs.project.impl.ValidatingPolicyBuilderAccessor;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.policies.PolicyType;

/**
 * Test the {@link ValidatingPolicyBuilderAccessor#removePolicyBuilder} method.
 */
public class RemoveMethodTestCase
        extends ValidateNameTestAbstract {

    // Javadoc inherited.
    protected void invokeFailingMethod(
            ValidatingPolicyBuilderAccessor accessor, final String name,
            PolicyType policyType)
            throws RepositoryException {

        accessor.removePolicyBuilder(connectionMock, projectMock,
                name);
    }

    // Javadoc inherited.
    protected void invokeOkMethod(
            ValidatingPolicyBuilderAccessor accessor, final String name,
            PolicyType policyType)
            throws RepositoryException {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        accessorMock.expects
                .removePolicyBuilder(connectionMock, projectMock, name)
                .returns(true);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        boolean removed = accessor.removePolicyBuilder(connectionMock,
                projectMock, name);
        assertTrue(removed);
    }
}
