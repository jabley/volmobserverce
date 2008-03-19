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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.javax.servlet.http.HttpServletRequestMock;

/**
 * Verify that {@link RemappingFilter} behaves as expected.
 */
public class RemappingFilterTestCase extends MockTestCaseAbstract {

    /**
     * Different app servers interpret the servlet spec differently when it
     * comes to what servletPath and pathInfo mean when processing filters.
     * This tests the Tomcat behaviour described below.
     * * <p/>
     * For example, if MCS is running on http://localhost:8080/volantis, and
     * a request is made to http://localhost:8080/volantis/welcome/welcome.xdime
     * in the {@link RemappingFilter} HttpServletRequest#getServletPath returns
     * '/welcome/welcome.xdime' and #getPathInfo returns null.
     */
    public void testGetFilterPathForTomcatLikeServer() {
        // Create test objects.
        RemappingFilter filter = new RemappingFilter();
        HttpServletRequestMock servletRequest =
                new HttpServletRequestMock("servletRequest", expectations);

        // Set expectations.
        final String servletPath = "/welcome/welcome.xdime";
        servletRequest.expects.getServletPath().returns(servletPath);
        servletRequest.expects.getPathInfo().returns(null);

        // Run test.
        String filterPath = filter.getFilterPath(servletRequest);
        assertEquals(servletPath, filterPath);
    }

    /**
     * Different app servers interpret the servlet spec differently when it
     * comes to what servletPath and pathInfo mean when processing filters.
     * This tests the Weblogic behaviour described below.
     * <p/>
     * For example, if MCS is running on http://localhost:8080/volantis, and
     * a request is made to http://localhost:8080/volantis/welcome/welcome.xdime
     * in the {@link RemappingFilter} HttpServletRequest#getServletPath returns
     * '' and #getPathInfo returns '/welcome/welcome.xdime'.
     */
    public void testGetFilterPathForWeblogicLikeServer() {
        // Create test objects.
        RemappingFilter filter = new RemappingFilter();
        HttpServletRequestMock servletRequest =
                new HttpServletRequestMock("servletRequest", expectations);

        // Set expectations.
        final String servletPath = "";
        final String pathInfo = "/welcome/welcome.xdime";
        servletRequest.expects.getServletPath().returns(servletPath);
        servletRequest.expects.getPathInfo().returns(pathInfo);

        // Run test.
        String filterPath = filter.getFilterPath(servletRequest);
        assertEquals(pathInfo, filterPath);
    }

    public void testGetFilterPathWhenBothPathInfoAndServletPathNotEmpty() {
        // Create test objects.
        RemappingFilter filter = new RemappingFilter();
        HttpServletRequestMock servletRequest =
                new HttpServletRequestMock("servletRequest", expectations);

        // Set expectations.
        final String servletPath = "/volantis";
        final String pathInfo = "/welcome/welcome.xdime";
        servletRequest.expects.getServletPath().returns(servletPath);
        servletRequest.expects.getPathInfo().returns(pathInfo);

        // Run test.
        String filterPath = filter.getFilterPath(servletRequest);
        assertEquals(servletPath + pathInfo, filterPath);
    }

    public void testGetFilterPathWhenBothPathInfoAndServletPathEmpty() {
        // Create test objects.
        RemappingFilter filter = new RemappingFilter();
        HttpServletRequestMock servletRequest =
                new HttpServletRequestMock("servletRequest", expectations);

        // Set expectations.
        final String servletPath = "";
        final String pathInfo = null;
        servletRequest.expects.getServletPath().returns(servletPath);
        servletRequest.expects.getPathInfo().returns(pathInfo);

        // Run test.
        String filterPath = filter.getFilterPath(servletRequest);
        assertEquals(servletPath, filterPath);
    }
}
