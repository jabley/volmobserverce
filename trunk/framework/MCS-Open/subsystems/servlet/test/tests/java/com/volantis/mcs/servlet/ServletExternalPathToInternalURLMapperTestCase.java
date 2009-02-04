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

package com.volantis.mcs.servlet;

import com.volantis.mcs.runtime.ExternalPathToInternalURLMapper;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.javax.servlet.ServletContextMock;

import java.net.URL;
import java.io.File;

/**
 * Test cases for {@link ServletExternalPathToInternalURLMapper}.
 */
public class ServletExternalPathToInternalURLMapperTestCase
        extends TestCaseAbstract {
    private ServletContextMock servletContextMock;

    protected void setUp() throws Exception {
        super.setUp();


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        servletContextMock = new ServletContextMock("servletContextMock",
                expectations);
    }

    /**
     * Test that mapping internal to external results in a path starting with a
     * /.
     */
    public void testMapInternalToExternal() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Try and find a real directory that can be used as the basis of the
        // test.
        URL url = getClass().getResource(".");
        String directory = url.toExternalForm();
        if (directory.startsWith("file:")) {
            directory = directory.substring(5);
        } else {
            File file = new File(".");
            if (!file.exists() || !file.isDirectory()) {
                throw new IllegalStateException("Test cannot complete because " +
                        "cannot get a path to a real directory");
            }
            directory = file.getCanonicalPath();
        }

        servletContextMock.expects.getRealPath("/")
                .returns(directory).any();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ExternalPathToInternalURLMapper mapper =
                new ServletExternalPathToInternalURLMapper(servletContextMock);
        String actualPath = mapper.mapInternalURLToExternalPath(
                "file:" + directory + "fred.gif");
        assertEquals("/fred.gif", actualPath);
    }
}
