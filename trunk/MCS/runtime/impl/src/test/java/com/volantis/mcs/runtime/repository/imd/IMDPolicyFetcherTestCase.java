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

package com.volantis.mcs.runtime.repository.imd;

import com.volantis.mcs.context.CurrentProjectProviderMock;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilderMock;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.policies.PolicyActivatorMock;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.policies.ActivatedPolicyMock;
import com.volantis.mcs.runtime.policies.NormalizedPolicyReferenceImpl;
import com.volantis.mcs.runtime.policies.PolicyFetcherMock;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.PolicyActivatorMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link IMDPolicyFetcher}.
 */
public class IMDPolicyFetcherTestCase
        extends TestCaseAbstract {

    private PolicyFetcherMock fetcherMock;
    private PolicyActivatorMock activatorMock;
    private CurrentProjectProviderMock projectProviderMock;
    private ActivatedPolicyMock activatedPolicyMock;
    private RuntimeProjectMock projectMock;
    private String policyName;
    private RuntimePolicyReference reference;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        fetcherMock = new PolicyFetcherMock("fetcherMock", expectations);

        activatorMock = new PolicyActivatorMock("activatorMock", expectations);

        projectProviderMock = new CurrentProjectProviderMock(
                "projectProviderMock", expectations);

        activatedPolicyMock = new ActivatedPolicyMock("activatedPolicyMock",
                expectations);

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        policyName = "/fred";

        reference = new NormalizedPolicyReferenceImpl(projectMock, policyName,
                PolicyType.AUDIO);
    }

    /**
     * Ensure that when not policy is added to the IMD fetcher that it
     * delegates the request.
     */
    public void testCanNotFindPolicy() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        fetcherMock.expects.fetchPolicy(reference).returns(activatedPolicyMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        IMDPolicyFetcher fetcher = new IMDPolicyFetcher(fetcherMock,
                activatorMock, projectProviderMock);

        Policy actualPolicy = fetcher.fetchPolicy(reference);
        assertSame(activatedPolicyMock, actualPolicy);
    }

    /**
     * Ensure that when a policy is added to the IMD fetcher that it can be
     * retrieved.
     */
    public void testCanFindPolicy() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final PolicyBuilderMock builderMock =
                new PolicyBuilderMock("builderMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The inline policy will be stored using its name as a key.
        builderMock.expects.getName().returns(policyName).any();

        // The activation will use the current project.
        projectProviderMock.expects.getCurrentProject()
                .returns(projectMock).any();

        // The activator will be called.
        activatorMock.expects.activate(projectMock, builderMock, projectMock)
                .returns(activatedPolicyMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new NormalizedPolicyReferenceImpl(
                projectMock, policyName, PolicyType.AUDIO);

        IMDPolicyFetcher fetcher = new IMDPolicyFetcher(fetcherMock,
                activatorMock, projectProviderMock);

        // Add the inline policy.
        fetcher.addInlinePolicyBuilder(builderMock);

        // Fetch the policy, this should return the activated version of the
        // inline policy that was added.
        Policy actualPolicy = fetcher.fetchPolicy(reference);
        assertSame(activatedPolicyMock, actualPolicy);
    }
}
