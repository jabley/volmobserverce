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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.project;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link StringURLBasedPath}.
 */
public class StringURLBasedPathTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that getParentPath() works correctly.
     */
    public void testGetParentPath() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StringURLBasedPath path = new StringURLBasedPath(
                "http://host/path/path.xml");
        Path ancestor;

        ancestor = path.getParentPath();
        assertEquals("http://host/path", ancestor.toExternalForm());

        ancestor = ancestor.getParentPath();
        assertEquals("http://host", ancestor.toExternalForm());

        // The path stops at the host.
        ancestor = ancestor.getParentPath();
        assertNull(ancestor);
    }

    /**
     * Ensure that getParentPath() works correctly with a JAR like URL.
     */
    public void testGetParentPathWithJar() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StringURLBasedPath path = new StringURLBasedPath(
                "file:/a/b/foo.jar!/com/blah.xml");
        Path ancestor;

        ancestor = path.getParentPath();
        assertEquals("file:/a/b/foo.jar!/com", ancestor.toExternalForm());

        ancestor = ancestor.getParentPath();
        assertEquals("file:/a/b/foo.jar!", ancestor.toExternalForm());

        // The path stops at the host.
        ancestor = ancestor.getParentPath();
        assertNull(ancestor);
    }
}
