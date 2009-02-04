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

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Test cases for {@link ProjectLoadingOptimizer}.
 */
public class ProjectLoadingOptimizerTestCase
        extends TestCaseAbstract {

    /**
     * Test that the optimizer stores information about the project correctly.
     */
    public void testOptimizing() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ProjectLoadingOptimizer optimizer = new ProjectLoadingOptimizerImpl();

        assertEquals(ProjectLoadingOptimizer.MAYBE,
                optimizer.isProject("blah"));
        assertEquals(ProjectLoadingOptimizer.MAYBE,
                optimizer.isProject("foo"));

        List visited = new ArrayList();
        visited.add("blah");
        visited.add("foo");

        optimizer.notProject(visited);

        assertEquals(ProjectLoadingOptimizer.NO, optimizer.isProject("blah"));
    }
}
