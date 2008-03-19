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

import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link RuntimePolicyReference}.
 */
public class RuntimePolicyReferenceTestCase
        extends TestCaseAbstract {

    private RuntimeProjectMock projectMock;
    private ProjectManagerMock projectManagerMock;
    private RuntimeProjectMock locatedProjectMock;
    private MarinerURL absoluteBaseURL;
    private RuntimeProjectMock globalProjectMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        projectManagerMock = new ProjectManagerMock("projectManagerMock",
                expectations);

        locatedProjectMock = new RuntimeProjectMock("locatedProjectMock",
                expectations);

        absoluteBaseURL = new MarinerURL("file:/a/b/");

        globalProjectMock = new RuntimeProjectMock("globalProjectMock",
                expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Use this as the global project.
        globalProjectMock.expects.isRemote().returns(true).any();
        globalProjectMock.expects.getContainsOrphans().returns(true).any();
    }

    /**
     * Test that normalizing an absolute reference locates the project properly
     * and uses the result.
     */
    public void testNormalizeAbsoluteReference() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project manager expects to be called to locate the project for
        // the absolute URL and does so.
        projectManagerMock.expects
                .queryProject("file:/fred.mimg", projectMock)
                .returns(locatedProjectMock);

        // The located project is not the global one.
        locatedProjectMock.expects.isRemote().returns(false).any();
        locatedProjectMock.expects.getContainsOrphans().returns(false).any();

        // The located project expects to be asked to convert an absolute URL
        // into a project relative one.
        locatedProjectMock.expects
                .makeProjectRelativePath(new MarinerURL("file:/fred.mimg"), true)
                .returns("/fred.mimg").any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "file:/fred.mimg", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "/fred.mimg",
                reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", locatedProjectMock,
                reference.getProject());

        assertFalse("Must be unbrandable", reference.isBrandable());
    }

    /**
     * Test that normalizing an absolute reference within the global project
     * leaves the URL as absolute.
     */
    public void testNormalizeAbsoluteGlobal() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project manager expects to be called to locate the project for
        // the absolute URL and does so.
        projectManagerMock.expects
                .queryProject("file:/fred.mimg", projectMock)
                .returns(locatedProjectMock);

        // The located project is the global one.
        locatedProjectMock.expects.isRemote().returns(true).any();
        locatedProjectMock.expects.getContainsOrphans().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "file:/fred.mimg", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "file:/fred.mimg",
                reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", locatedProjectMock,
                reference.getProject());

        assertFalse("Must be unbrandable", reference.isBrandable());
    }

    /**
     * Test that normalizing an absolute reference within the global project
     * leaves the URL as absolute.
     */
    public void testNormalizeAbsoluteRemote() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project manager expects to be called to locate the project for
        // the absolute URL and does so.
        final String policyURL = "http://www.test.com/sample/fred.mimg";
        projectManagerMock.expects.queryProject(policyURL, projectMock).returns(
            locatedProjectMock);

        // The located project is a remote one.
        locatedProjectMock.expects.isRemote().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
            projectMock, absoluteBaseURL, policyURL, null, projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", policyURL, reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", locatedProjectMock,
                reference.getProject());
    }

    /**
     * Test that normalizing an absolute reference that does not have an
     * associated project defaults to the global .
     */
    public void testNormalizeAbsoluteUnknownProject() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project manager expects to be called to locate the project
        // for the absolute URL but fails to find it.
        projectManagerMock.expects
                .queryProject("file:/fred.mimg", projectMock)
                .returns(null).any();

        projectManagerMock.expects.getGlobalProject()
                .returns(globalProjectMock).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "file:/fred.mimg", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "file:/fred.mimg",
                reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", globalProjectMock,
                reference.getProject());

        assertFalse("Must be unbrandable", reference.isBrandable());
    }

    /**
     * Test that normalizing a project relative reference results in a
     * reference that has the same name as input and the same project.
     */
    public void testNormalizeLocalProjectRelativeReference() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The current project is not the global one.
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "/fred", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "/fred", reference.getName());

        // The normalized project is the same as the input.
        assertEquals("Normalize project mismatch", projectMock,
                reference.getProject());

        assertTrue("Must be brandable", reference.isBrandable());
    }

    /**
     * Test that normalizing a remote project relative reference results in a
     * project relative reference.
     */
    public void testNormalizeRemoteProjectRelativeReference() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The current project is not the global one.
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "/fred", null,
                projectManagerMock);

        // The normalized name is not the same as the input.
        assertEquals("Normalize name mismatch", "/fred", reference.getName());

        // The normalized project is the same as the input.
        assertEquals("Normalize project mismatch", projectMock,
                reference.getProject());

        assertTrue("Must be brandable", reference.isBrandable());
    }

    /**
     * Test that normalizing an unbrandable project relative reference works
     * the same as for a brandable one apart from the fact that the resulting
     * reference is unbrandable.
     */
    public void testNormalizeLocalUnbrandableProjectRelativeReference()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The current project is not the global one.
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "^/fred", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "/fred", reference.getName());

        // The normalized project is the same as the input.
        assertEquals("Normalize project mismatch", projectMock,
                reference.getProject());

        assertFalse("Must be unbrandable", reference.isBrandable());
    }

    /**
     * Test that normalizing a document relative reference resolves it against
     * the base URL, locates the project and then correctly creates a project
     * relative path.
     */
    public void testNormalizeDocumentRelative() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project is not the global project.
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();

        projectManagerMock.expects.getProject("file:/a/b/fred.mimg", null)
                .returns(projectMock);

        // The project expects to be asked to convert an absolute path into
        // a project relative one.
        projectMock.expects.makeProjectRelativePath(
                new MarinerURL("file:/a/b/fred.mimg"), true)
                .returns("/b/fred.mimg").atLeast(1);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "fred.mimg", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "/b/fred.mimg",
                reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", projectMock,
                reference.getProject());

        assertTrue("Must be brandable", reference.isBrandable());
    }

    /**
     * Test that normalizing a remote document relative reference resolves it
     * against the base URL, locates the project and then correctly creates a
     * project relative path.
     */
    public void testNormalizeRemoteDocumentRelative() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project is not the global project.
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(true).any();

        projectManagerMock.expects.getProject("file:/a/b/fred.mimg", null)
                .returns(projectMock);

        // The project expects to be asked to convert an absolute path into
        // a project relative one.
        projectMock.expects.makeProjectRelativePath(
                new MarinerURL("file:/a/b/fred.mimg"), true)
                .returns("/b/fred.mimg").atLeast(1);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "fred.mimg", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "/b/fred.mimg",
                reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", projectMock,
                reference.getProject());

        assertTrue("Must be brandable", reference.isBrandable());
    }

    /**
     * Test that normalizing a document relative reference resolves it against
     * the base URL, locates the project and then because the containing
     * project extends the located project uses the located project to
     * creates a project relative path.
     */
    public void testNormalizeDocumentRelativeExtends() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The project is not the global project.
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();

        projectManagerMock.expects.getProject("file:/a/b/fred.mimg", null)
                .returns(locatedProjectMock);

        // The located project is extended by the containing project.
        projectMock.expects.extendsProject(locatedProjectMock)
                .returns(true).any();

        // The project expects to be asked to convert an absolute path into
        // a project relative one.
        locatedProjectMock.expects.makeProjectRelativePath(
                new MarinerURL("file:/a/b/fred.mimg"), true)
                .returns("/b/fred.mimg").atLeast(1);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "fred.mimg", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "/b/fred.mimg",
                reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", projectMock,
                reference.getProject());

        assertTrue("Must be brandable", reference.isBrandable());
    }

    /**
     * Test that normalizing a document relative reference resolves it against
     * the base URL, locates the project and then correctly creates a project
     * relative path.
     */
    public void testNormalizeDocumentRelativeOutsideProject() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The located project is not the global project.
        projectMock.expects.getContainsOrphans().returns(false).any();

        projectManagerMock.expects.getProject("file:/a/b/fred.mimg", null)
                .returns(locatedProjectMock);

        // The project expects to be asked whether it extends the located
        // project but it doesn't.
        projectMock.expects.extendsProject(locatedProjectMock)
                .returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "fred.mimg", null,
                projectManagerMock);

        // The document relative path is outside the current project.
        try {
            reference.getName();
            fail("Did not detect document relative path outside project");
        }
        catch(IllegalStateException expected) {
        }
    }

    /**
     * Test that normalizing an unbrandable document relative reference works
     * the same as for a brandable one apart from the fact that the resulting
     * reference is unbrandable.
     */
    public void testNormalizeUnbrandableDocumentRelativeReference()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // The located project is not the global project.
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();

        projectManagerMock.expects.getProject("file:/a/b/fred.mimg", null)
                .returns(projectMock);

        // The project expects to be asked to convert an absolute path into
        // a project relative one.
        projectMock.expects.makeProjectRelativePath(
                new MarinerURL("file:/a/b/fred.mimg"), true)
                .returns("/b/fred.mimg").atLeast(1);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RuntimePolicyReference reference = new RuntimePolicyReferenceImpl(
                projectMock, absoluteBaseURL, "^fred.mimg", null,
                projectManagerMock);

        // The normalized name is the same as the input.
        assertEquals("Normalize name mismatch", "/b/fred.mimg",
                reference.getName());

        // The normalized project is the one returned from the project manager.
        assertEquals("Normalize project mismatch", projectMock,
                reference.getProject());

        assertFalse("Must be unbrandable", reference.isBrandable());
    }
}
