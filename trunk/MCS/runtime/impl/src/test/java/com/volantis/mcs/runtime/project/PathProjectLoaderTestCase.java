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

import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Test cases for {@link URLProjectLoader}.
 */
public class PathProjectLoaderTestCase
        extends TestCaseAbstract {

    private ProjectLoadingOptimizerMock optimizerMock;
    private PathMock a_b_projectPathMock;
    private PathMock a_projectPathMock;
    private PathMock a_b_blahPathMock;

    protected void setUp() throws Exception {
        super.setUp();

        optimizerMock = new ProjectLoadingOptimizerMock("optimizerMock",
                expectations);

        // Represents the path to a/
        final PathMock aPathMock = createPathMock("aPathMock", "a/", null);

        // Represents the path to a/mcs-project.xml
        a_projectPathMock = createPathMock("a_projectPathMock",
                "a/mcs-project.xml", aPathMock);

        // Represents the path to a/b/
        final PathMock a_bPathMock =
                createPathMock("a_bPathMock", "a/b/", aPathMock);

        // Represents the path to a/b/blah.txt
        a_b_blahPathMock = createPathMock("a_b_blahPathMock", "a/b/blah.txt",
                a_bPathMock);

        // Represents the path to a/b/mcs-project.xml
        a_b_projectPathMock = createPathMock("a_b_projectPathMock",
                "a/b/mcs-project.xml", a_bPathMock);

        aPathMock.expects.getChild("mcs-project.xml")
                .returns(a_projectPathMock).any();

        a_bPathMock.expects.getChild("mcs-project.xml")
                .returns(a_b_projectPathMock).any();

    }

    /**
     * Test to make sure that the URL project loader can load a project
     * properly.
     */
    public void testLoadProject() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        final URL projectURL = getClassRelativeResourceURL("a/mcs-project.xml");

        final InputStream stream = projectURL.openStream();
        expectations.add(new OrderedExpectations() {
            public void add() {

                optimizerMock.expects.isProject("a/b/")
                        .returns(ProjectLoadingOptimizer.MAYBE);

                a_b_projectPathMock.expects.openStream()
                        .fails(new IOException("Could not find resource"));

                optimizerMock.expects.isProject("a/")
                        .returns(ProjectLoadingOptimizer.MAYBE);

                a_projectPathMock.expects.openStream().returns(stream);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ProjectLoader loader = new PathProjectLoader() {
            public Path createPath(String url) {
                return a_b_blahPathMock;
            }
        };

        URL url = getClass().getResource("a/b/blah.txt");
        String urlAsString = url.toExternalForm();

        RuntimeProjectConfiguration configuration =
                loader.loadProjectConfiguration(urlAsString, optimizerMock);
        assertNotNull("Should find a configuration", configuration);
    }

    /**
     * Test to make sure that the URL project loader behaves properly when it
     * cannot load a project.
     */
    public void testLoadProjectFailure() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {

                optimizerMock.expects.isProject("a/b/")
                        .returns(ProjectLoadingOptimizer.MAYBE);

                a_b_projectPathMock.expects.openStream()
                        .fails(new IOException("Could not find resource"));

                optimizerMock.expects.isProject("a/")
                        .returns(ProjectLoadingOptimizer.MAYBE);

                a_projectPathMock.expects.openStream()
                        .fails(new IOException("Could not find resource"));

                optimizerMock.expects.notProject(Arrays.asList(new String[] {
                    "a/b/", "a/"
                }));
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ProjectLoader loader = new PathProjectLoader() {
            public Path createPath(String url) {
                return a_b_blahPathMock;
            }
        };

        URL url = getClass().getResource("a/b/blah.txt");
        String urlAsString = url.toExternalForm();

        RuntimeProjectConfiguration configuration =
                loader.loadProjectConfiguration(urlAsString, optimizerMock);
        assertNull("Should not find a configuration", configuration);
    }

    /**
     * Create a path mock.
     *
     * @param identifier The identifier of the mock.
     * @param relative   The relative path to the resource.
     * @param parent     The parent path.
     * @return The path mock with some standard expectations set.
     */
    private PathMock createPathMock(
            final String identifier, String relative, final Path parent) {

        final PathMock pathMock = new PathMock(identifier, expectations);
        pathMock.expects.toExternalForm().returns(relative).any();
        pathMock.expects.getParentPath().returns(parent).any();
        return pathMock;
    }

}
