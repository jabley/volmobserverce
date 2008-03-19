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

import com.volantis.mcs.accessors.PolicyBuilderAccessorMock;
import com.volantis.mcs.policies.PolicyBuilderMock;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.impl.ValidatingPolicyBuilderAccessor;
import com.volantis.mcs.repository.RepositoryConnectionMock;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for tests of specific methods in the
 * {@link ValidatingPolicyBuilderAccessor}
 */
public abstract class ValidateNameTestAbstract
        extends TestCaseAbstract {

    protected PolicyBuilderAccessorMock accessorMock;
    protected RepositoryConnectionMock connectionMock;
    protected ProjectMock projectMock;
    protected PolicyBuilderMock policyBuilderMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        accessorMock = new PolicyBuilderAccessorMock("accessorMock",
                expectations);

        connectionMock = new RepositoryConnectionMock("connectionMock",
                expectations);

        projectMock = new ProjectMock("projectMock", expectations);

        policyBuilderMock = new PolicyBuilderMock("policyBuilderMock",
                expectations);
    }

    /**
     * Ensure that the method being tested checks for invalid extensions.
     */
    public void testInvalidExtension() throws Exception {

        ValidatingPolicyBuilderAccessor accessor =
                new ValidatingPolicyBuilderAccessor(accessorMock);

        try {
            invokeFailingMethod(accessor, "/invalid", null);
            fail("Did not detect invalid extension");
        } catch (IllegalArgumentException expected) {
            assertEquals(
                    "Policy name '/invalid' has an unknown file extension",
                    expected.getMessage());
        }
    }

    /**
     * Invokes method that will fail.
     *
     * @param accessor The accessor on which the method is invoked.
     * @param name     The name of the policy.
     * @param policyType
     */
    protected abstract void invokeFailingMethod(
            ValidatingPolicyBuilderAccessor accessor, String name,
            PolicyType policyType)
            throws RepositoryException;

    /**
     * Ensure that the method being tested checks for no leading slash.
     */
    public void testNoLeadingSlash() throws Exception {

        ValidatingPolicyBuilderAccessor accessor =
                new ValidatingPolicyBuilderAccessor(accessorMock);

        try {
            invokeFailingMethod(accessor, "invalid.mimg", PolicyType.IMAGE);
            fail("Did not detect invalid extension");
        } catch (IllegalArgumentException expected) {
            assertEquals("Policy name 'invalid.mimg' must start with a /",
                    expected.getMessage());
        }
    }

    /**
     * Ensure that the method being tested delegates properly.
     */
    public void testOk() throws Exception {

        ValidatingPolicyBuilderAccessor accessor =
                new ValidatingPolicyBuilderAccessor(accessorMock);

        invokeOkMethod(accessor, "/valid.mimg", PolicyType.IMAGE);
    }

    /**
     * Invokes method that will succeed.
     *
     * @param accessor The accessor on which the method is invoked.
     * @param name     The name of the policy.
     * @param policyType
     */
    protected abstract void invokeOkMethod(
            ValidatingPolicyBuilderAccessor accessor, String name,
            PolicyType policyType)
            throws RepositoryException;
}
