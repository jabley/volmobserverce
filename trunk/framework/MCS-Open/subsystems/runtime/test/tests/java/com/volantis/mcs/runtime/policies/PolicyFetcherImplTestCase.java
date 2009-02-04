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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class PolicyFetcherImplTestCase
        extends TestCaseAbstract {

    private ActivatedPolicyRetrieverMock retrieverMock;
    private RuntimeProjectMock projectMock;
    private ActivatedPolicyMock policyMock;
    private PolicyFetcher fetcher;

    protected void setUp() throws Exception {
        super.setUp();

        retrieverMock = new ActivatedPolicyRetrieverMock("retrieverMock",
                expectations);
        projectMock = new RuntimeProjectMock("projectMock",
                expectations);
        policyMock = new ActivatedPolicyMock("policyMock", expectations);

        fetcher = new PolicyFetcherImpl(retrieverMock);
    }

    /**
     * Test that an unbranded project relative URL is passed straight
     * through to the accessor and the policy returned by the accessor
     * is returned by the fetcher if it matches the expected type.
     */
    public void testProjectRelative() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        retrieverMock.expects
                .retrievePolicy(projectMock, "/fred.foo")
                .returns(policyMock).any();

        policyMock.expects.getPolicyType().returns(PolicyType.AUDIO).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        RuntimePolicyReference runtime = new NormalizedPolicyReferenceImpl(
                projectMock, "/fred.foo", PolicyType.AUDIO);

        Policy policy = fetcher.fetchPolicy(runtime);
        assertSame("Policy", policyMock, policy);
    }

    /**
     * Test that an unbranded project relative URL is passed straight
     * through to the accessor and the policy returned by the accessor
     * is returned by the fetcher as there is no expected type.
     */
    public void testProjectRelativeNoExpectedType() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        retrieverMock.expects
                .retrievePolicy(projectMock, "/fred.foo")
                .returns(policyMock).any();

        policyMock.expects.getPolicyType().returns(PolicyType.AUDIO).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // No expected type.
        RuntimePolicyReference runtime = new NormalizedPolicyReferenceImpl(
                projectMock, "/fred.foo", null);

        Policy policy = fetcher.fetchPolicy(runtime);
        assertSame("Policy", policyMock, policy);
    }

    /**
     * Test that an unbranded project relative URL is passed straight
     * through to the accessor and the policy returned by the accessor
     * is not returned by the fetcher if it does not match the expected type.
     */
    public void testProjectRelativeUnexpectedType() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        retrieverMock.expects
                .retrievePolicy(projectMock, "/fred.foo")
                .returns(policyMock).any();

        policyMock.expects.getPolicyType().returns(PolicyType.AUDIO).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // No expected type.
        RuntimePolicyReference runtime = new NormalizedPolicyReferenceImpl(
                projectMock, "/fred.foo", PolicyType.IMAGE);

        Policy policy = fetcher.fetchPolicy(runtime);
        assertNull("Policy", policy);
    }

    /**
     * Test that an absolute URL is redirected to the global remote
     * project before being passed through to the accessor.
     */
    public void testAbsolutePathRedirected() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        retrieverMock.expects
                .retrievePolicy(projectMock,
                        "http://www.volantis.com/fred.foo")
                .returns(policyMock).any();

        policyMock.expects.getPolicyType().returns(PolicyType.AUDIO).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // No expected type.
        RuntimePolicyReference runtime = new NormalizedPolicyReferenceImpl(
                projectMock, "http://www.volantis.com/fred.foo",
                PolicyType.AUDIO);

        Policy policy = fetcher.fetchPolicy(runtime);
        assertSame("Policy", policyMock, policy);
    }
}
