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

import com.volantis.mcs.policies.PolicyBuilderReaderMock;
import com.volantis.mcs.repository.LocalRepositoryMock;
import com.volantis.mcs.runtime.RepositoryContainer;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.runtime.repository.remote.xml.RemoteReadersFactoryMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Collections;

/**
 * Test cases for {@link ProjectManager}.
 */
public class ProjectManagerTestCase
        extends TestCaseAbstract {

    private ProjectLoaderMock projectLoaderMock;
    private RuntimeProjectMock projectMock;
    private RepositoryContainer repositoryContainer;
    private RuntimeProjectConfiguration configuration;
    private ProjectLoadingOptimizer optimizer;
    private RemoteReadersFactoryMock remoteReadersFactoryMock;
    private RuntimeProjectConfiguratorMock configuratorMock;
    private RuntimeProjectMock globalProjectMock;

    protected void setUp() throws Exception {
        super.setUp();

        configuration = new RuntimeProjectConfiguration();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        projectLoaderMock = new ProjectLoaderMock("projectLoaderMock",
                expectations);

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        globalProjectMock = new RuntimeProjectMock(
                "globalProjectMock", expectations);

        final LocalRepositoryMock jdbcRepositoryMock =
                new LocalRepositoryMock("jdbcRepositoryMock",
                        expectations);

        final LocalRepositoryMock xmlRepositoryMock =
                new LocalRepositoryMock("xmlRepositoryMock",
                        expectations);

        repositoryContainer = new RepositoryContainer(jdbcRepositoryMock,
                xmlRepositoryMock);

        remoteReadersFactoryMock = new RemoteReadersFactoryMock(
                "remoteReadersFactoryMock", expectations);

        PolicyBuilderReaderMock remoteReaderMock = new PolicyBuilderReaderMock(
                "remoteReaderMock", expectations);

        configuratorMock = new RuntimeProjectConfiguratorMock(
                "configuratorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        remoteReadersFactoryMock.fuzzy.createPolicyBuilderReader(
                mockFactory.expectsAny()).returns(remoteReaderMock);

        configuratorMock.fuzzy.createGlobalProject(mockFactory.expectsAny())
                .returns(globalProjectMock);

        optimizer = null;
    }

    /**
     * Make sure that the global project has the correct characteristics.
     */
    public void testGlobal() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ProjectManager manager = new ProjectManagerImpl(null,
                configuratorMock, repositoryContainer,
                remoteReadersFactoryMock);
        RuntimeProject project = manager.getGlobalProject();
        assertSame(globalProjectMock, project);
    }

    /**
     * Ensure that getting a policy that is in the best guess project returns
     * immediately and does not try and load the project.
     */
    public void testGetProjectInBestGuess() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final RuntimeProjectMock bestGuessProjectMock =
                new RuntimeProjectMock("bestGuessProjectMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        bestGuessProjectMock.expects.getContainsOrphans().returns(false).any();
        bestGuessProjectMock.expects
                .containsPolicy("http://host/fred/image.mimg")
                .returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ProjectManager manager = new ProjectManagerImpl(projectLoaderMock,
                configuratorMock, repositoryContainer,
                remoteReadersFactoryMock);

        RuntimeProject actualProject =
                manager.getProject("http://host/fred/image.mimg",
                        bestGuessProjectMock);
        assertSame(bestGuessProjectMock, actualProject);
    }

    /**
     * Ensure that getting an unknown project invokes the loader once and is
     * then cached away.
     */
    public void testGetProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectLoaderMock.expects
                .loadProjectConfiguration("http://host/fred/image.mimg",
                        optimizer)
                .returns(configuration);

        projectMock.expects.getPolicyRootAliases()
                .returns(Collections.singleton("http://host/fred/")).any();
        projectMock.expects.containsPolicy("http://host/fred/image.mimg")
                .returns(true);

        configuratorMock.fuzzy
                .buildProject(configuration, null, mockFactory.expectsAny())
                .returns(projectMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ProjectManager manager = new ProjectManagerImpl(projectLoaderMock,
                configuratorMock, repositoryContainer,
                optimizer, remoteReadersFactoryMock);
        RuntimeProject actualProject;
        actualProject =
                manager.getProject("http://host/fred/image.mimg", null);
        assertSame(actualProject, projectMock);

        // Try again, should not require another call to load.
        actualProject =
                manager.getProject("http://host/fred/image.mimg", null);
        assertSame(actualProject, projectMock);
    }

    /**
     * Ensure that getting an unknown project invokes the loader once and is
     * then cached away.
     */
    public void testGetOverlappingProject() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        projectLoaderMock.expects
                .loadProjectConfiguration(
                        "http://host/fred/flintstone/image.mimg", optimizer)
                .returns(configuration);

        projectMock.expects.getPolicyRootAliases()
            .returns(Collections.singleton("http://host/fred/flintstone/")).any();
        projectMock.expects.containsPolicy("http://host/fred/image.mimg")
                .returns(false);

        configuratorMock.fuzzy
                .buildProject(configuration, null, mockFactory.expectsAny())
                .returns(projectMock);

        RuntimeProjectConfiguration configuration2 = new RuntimeProjectConfiguration();
        projectLoaderMock.expects
                .loadProjectConfiguration("http://host/fred/image.mimg",
                        optimizer)
                .returns(configuration2);

        final RuntimeProjectMock otherProjectMock =
                new RuntimeProjectMock("otherProjectMock", expectations);

        configuratorMock.fuzzy
                .buildProject(configuration2, null, mockFactory.expectsAny())
                .returns(otherProjectMock);
        otherProjectMock.expects.getPolicyRootAliases()
                .returns(Collections.singleton("http://host/fred/")).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ProjectManager manager = new ProjectManagerImpl(projectLoaderMock,
                configuratorMock, repositoryContainer,
                optimizer, remoteReadersFactoryMock);
        RuntimeProject actualProject;
        actualProject =
                manager.getProject("http://host/fred/flintstone/image.mimg",
                        null);
        assertSame(actualProject, projectMock);

        // Try again, should fail with an overlap.
        try {
            actualProject =
                    manager.getProject("http://host/fred/image.mimg", null);
            fail("Did not detect overlap");
        } catch (IllegalStateException e) {
            assertEquals("Project 'http://host/fred/' overlaps project " +
                    "'http://host/fred/flintstone/'", e.getMessage());
        }
        assertSame(actualProject, projectMock);
    }
}
