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

/**
 * Test cases for {@link URLProjectLoader}.
 */
public abstract class PathProjectLoaderTestAbstract
        extends TestCaseAbstract {

    private ProjectLoadingOptimizer optimizer;

    protected void setUp() throws Exception {
        super.setUp();

        optimizer = null;
    }

    /**
     * Create the loader to test.
     *
     * @return The loader to test.
     */
    protected abstract ProjectLoader createLoader();

    /**
     * Test to make sure that the URL project loader can load a project
     * properly.
     */
    public void testLoadProject() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ProjectLoader loader = createLoader();

        String urlAsString =
                getClassRelativeResourceURLAsString("a/b/blah.txt");

        RuntimeProjectConfiguration configuration =
                loader.loadProjectConfiguration(urlAsString, optimizer);
        assertNotNull("Should find a configuration", configuration);
    }
}
