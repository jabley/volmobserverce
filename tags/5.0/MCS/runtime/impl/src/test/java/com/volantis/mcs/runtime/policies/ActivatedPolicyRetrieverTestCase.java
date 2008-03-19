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

import com.volantis.impl.mcs.runtime.policies.ActivatedPolicyRetrieverImpl;
import com.volantis.mcs.policies.PolicyBuilderMock;
import com.volantis.mcs.policies.PolicyBuilderReaderMock;
import com.volantis.mcs.policies.PolicyBuilderResponse;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link ActivatedPolicyRetrieverImpl}.
 */
public class ActivatedPolicyRetrieverTestCase
        extends TestCaseAbstract {

    private final String name = "/foo.foo";
    private final String absoluteName = "http://www.volantis.com/foo.foo";
    private RuntimeProjectMock projectMock;
    private ActivatedPolicyMock policyMock;
    private RuntimeProjectMock actualProjectMock;
    private PolicyActivatorMock activatorMock;
    private ProjectManagerMock projectManagerMock;
    private RuntimeProjectMock globalProjectMock;
    private PolicyBuilderMock builderMock;
    private PolicyBuilderReaderMock readerMock;
    private PolicyBuilderReaderMock actualReaderMock;
    private PolicyBuilderReaderMock globalReaderMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        readerMock = new PolicyBuilderReaderMock("readerMock",
                expectations);

        actualProjectMock =
                new RuntimeProjectMock("actualProjectMock", expectations);

        actualReaderMock = new PolicyBuilderReaderMock("actualReaderMock",
                expectations);

        activatorMock = new PolicyActivatorMock("activatorMock", expectations);

        projectManagerMock =
                new ProjectManagerMock("projectManagerMock", expectations);

        globalProjectMock =
                new RuntimeProjectMock("globalProjectMock", expectations);

        globalReaderMock = new PolicyBuilderReaderMock("globalReaderMock",
                expectations);

        builderMock = new PolicyBuilderMock("builderMock", expectations);

        policyMock = new ActivatedPolicyMock("policyMock", expectations);

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectManagerMock.expects.getGlobalProject()
                .returns(globalProjectMock).any();

        projectMock.expects.getPolicyBuilderReader()
                .returns(readerMock).any();

        actualProjectMock.expects.getPolicyBuilderReader()
                .returns(actualReaderMock).any();

        globalProjectMock.expects.getPolicyBuilderReader()
                .returns(globalReaderMock).any();
    }

    /**
     * Ensure that retrieving a policy causes it to be activated correctly.
     */
    public void testRetrieve() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        readerMock.expects.getPolicyBuilder(projectMock, name)
                .returns(new PolicyBuilderResponse(projectMock,
                        builderMock));

        activatorMock.expects.activate(projectMock,
                builderMock, projectMock)
                .returns(policyMock);

        // No base project.
        projectMock.expects.getBaseProject().returns(null).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        ActivatedPolicyRetriever retriever = new ActivatedPolicyRetrieverImpl(
                activatorMock, projectManagerMock);

        ActivatedPolicy policy = retriever.retrievePolicy(projectMock, name);
        assertSame(policyMock, policy);
    }

    /**
     * Ensure that failing to retrieve a policy causes null to be returned.
     */
    public void testRetrieveFailure() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        readerMock.expects.getPolicyBuilder(projectMock, name)
                .returns(new PolicyBuilderResponse(projectMock, null));

        // No base project.
        projectMock.expects.getBaseProject().returns(null).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ActivatedPolicyRetriever retriever = new ActivatedPolicyRetrieverImpl(
                activatorMock, projectManagerMock);

        ActivatedPolicy policy = retriever.retrievePolicy(projectMock, name);
        assertNull(policy);
    }

    /**
     * Ensure that if the reader indicates that the project is different from
     * the requesting one (which must be the global project) then it is used to
     * activate the project.
     */
    public void testProjectUpdate() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        globalReaderMock.expects.getPolicyBuilder(globalProjectMock, name)
                .returns(new PolicyBuilderResponse(actualProjectMock,
                        builderMock));

        activatorMock.expects.activate(actualProjectMock,
                builderMock, actualProjectMock)
                .returns(policyMock);

        // No base project.
        actualProjectMock.expects.getBaseProject().returns(null).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ActivatedPolicyRetriever retriever = new ActivatedPolicyRetrieverImpl(
                activatorMock, projectManagerMock);

        ActivatedPolicy policy = retriever.retrievePolicy(globalProjectMock,
                name);
        assertSame(policyMock, policy);
    }

    /**
     * Ensure that if the reader indicates that the project is different from
     * the requesting one but the requesting one is not the global one than it
     * throws an exception.
     */
    public void testProjectInvalidActualProject() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // If any reader other then the one for the global project returns a
        // different project in the response than the requested one then it is
        // an error.
        readerMock.expects.getPolicyBuilder(projectMock, name)
                .returns(new PolicyBuilderResponse(actualProjectMock,
                        builderMock));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ActivatedPolicyRetriever retriever = new ActivatedPolicyRetrieverImpl(
                activatorMock, projectManagerMock);

        try {
            retriever.retrievePolicy(projectMock, name);
            fail("Did not detect invalid project in response");
        } catch (IllegalStateException e) {
            assertEquals("Response indicates policy is in " +
                    "{actualProjectMock} instead of {projectMock} but " +
                    "either it, or {projectMock} is not the global " +
                    "project {globalProjectMock}", e.getMessage());
        }
    }

    /**
     * Ensure that if the reader indicates that the project is different from
     * the requesting one but the requesting one is not the global one than it
     * throws an exception.
     */
    public void testProjectInvalidLogicalProject() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // If any reader other then the one for the global project returns a
        // different project in the response than the requested one then it is
        // an error.
        readerMock.expects.getPolicyBuilder(projectMock, name)
                .returns(null);

        projectMock.expects.getBaseProject().returns(globalProjectMock).any();

        globalReaderMock.expects.getPolicyBuilder(globalProjectMock, name)
                .returns(new PolicyBuilderResponse(actualProjectMock,
                        builderMock));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ActivatedPolicyRetriever retriever = new ActivatedPolicyRetrieverImpl(
                activatorMock, projectManagerMock);

        try {
            retriever.retrievePolicy(projectMock, name);
            fail("Did not detect invalid project in response");
        } catch (IllegalStateException e) {
            assertEquals("Response indicates policy is in " +
                    "{actualProjectMock} instead of {globalProjectMock} but " +
                    "either it, or {projectMock} is not the global " +
                    "project {globalProjectMock}", e.getMessage());
        }
    }

    /**
     * Ensure that if a policy could not be found in one project that the base
     * project is checked.
     */
    public void testFallback()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        readerMock.expects.getPolicyBuilder(projectMock, name)
                .returns(null);

        // Try base project.
        projectMock.expects.getBaseProject().returns(actualProjectMock).any();

        actualReaderMock.expects.getPolicyBuilder(actualProjectMock, name)
                .returns(new PolicyBuilderResponse(actualProjectMock,
                        builderMock));

        activatorMock.expects.activate(actualProjectMock,
                builderMock, projectMock)
                .returns(policyMock);

        // No more base projects.
        actualProjectMock.expects.getBaseProject().returns(null).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        ActivatedPolicyRetriever retriever = new ActivatedPolicyRetrieverImpl(
                activatorMock, projectManagerMock);

        ActivatedPolicy policy = retriever.retrievePolicy(projectMock, name);
        assertSame(policyMock, policy);
    }

    /**
     * Ensure that if a policy name is not project relative that even if it
     * could not be found in one project that it does not fallback to the base
     * project.
     */
    public void testAbsoluteDontFallback()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        readerMock.expects.getPolicyBuilder(projectMock, absoluteName)
                .returns(null);

        projectMock.expects.getBaseProject().returns(actualProjectMock).any();
        
        // =====================================================================
        //   Test Expectations
        // =====================================================================


        ActivatedPolicyRetriever retriever = new ActivatedPolicyRetrieverImpl(
                activatorMock, projectManagerMock);

        ActivatedPolicy policy =
                retriever.retrievePolicy(projectMock, absoluteName);
        assertNull(policy);
    }
}
