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

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.context.ProjectStackMock;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link ProjectTracker}.
 */
public class ProjectTrackerTestCase
        extends TestCaseAbstract {

    private static final String BASE_A_B = "file:/a/b.xml";
    private BaseURLTrackerMock baseURLTrackerMock;
    private ProjectManagerMock projectManagerMock;
    private ProjectStackMock projectStackMock;
    private RuntimeProjectMock enclosingProjectMock;
    private RuntimeProjectMock newProjectMock;

    protected void setUp() throws Exception {
        super.setUp();

        baseURLTrackerMock =
                new BaseURLTrackerMock("baseURLTrackerMock", expectations,null);

        projectManagerMock =
                new ProjectManagerMock("projectManagerMock", expectations);

        projectStackMock =
                new ProjectStackMock("projectStackMock", expectations);

        enclosingProjectMock =
                new RuntimeProjectMock("enclosingProjectMock", expectations);

        newProjectMock =
                new RuntimeProjectMock("newProjectMock", expectations);
    }

    /**
     * Ensure that when the base URL has not project that the enclosing project
     * is pushed back onto the stack.
     */
    public void testBaseURLHasNoProject() throws Exception {
        doTest(null, enclosingProjectMock);
    }

    /**
     * Ensure that when the base URL has a project that it is pushed onto the
     * stack.
     */
    public void testBaseURLHasProject() throws Exception {

        enclosingProjectMock.expects.extendsProject(newProjectMock)
                .returns(false).any();

        doTest(newProjectMock, newProjectMock);
    }

    private void doTest(
            final RuntimeProject baseURLProject,
            final RuntimeProject pushedProject) {
        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Tracker gets the current project, this could actually occur
                // anytime before the ProjectManager#getProject method is
                // called but it is difficult to represent that. The mocks
                // would have to be able to have two ordered sets of
                // expectations that 'joined' back up. This could be done
                // implicitly by the fact that a mock could only have been
                // obtained by satisfying the expectation in the 'branch' that
                // is to be 'joined' back in.
                projectStackMock.expects.getCurrentProject()
                        .returns(enclosingProjectMock);

                // The base URL tracker reports that the base URL has changed.
                baseURLTrackerMock.expects.startElement(BASE_A_B)
                        .returns(true);

                // The base URL tracker provides the current base URL.
                baseURLTrackerMock.expects.getBaseURL()
                        .returns(new MarinerURL(BASE_A_B));

                // The project manager cannot find the project for the URL
                // supplied.
                projectManagerMock.expects
                        .getProject(BASE_A_B, enclosingProjectMock)
                        .returns(baseURLProject);

                // The enclosing project is pushed onto the stack so it has
                // not changed.
                projectStackMock.expects.pushProject(pushedProject);

                // The project is popped off the stack.
                projectStackMock.expects.popProject(null)
                        .returns(pushedProject);

                baseURLTrackerMock.expects.endElement(BASE_A_B);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ProjectTracker tracker = new ProjectTracker(baseURLTrackerMock,
                projectManagerMock, projectStackMock);

        tracker.startElement(BASE_A_B);
        tracker.endElement(BASE_A_B);
    }
}
