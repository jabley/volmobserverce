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
package com.volantis.resource;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.javax.servlet.FilterChainMock;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;

/**
 * Test the project file filter
 */
public class ProjectFileFilterTestCase extends TestCaseAbstract {
    /**
     * mock request object
     */
    private HttpServletRequestMock httpServletRequestMock;

    /**
     * mock responce object
     */
    private HttpServletResponseMock httpServletResponseMock;

    /**
     * Mock filter chain.
     */
    private FilterChainMock filterChainMock;

    protected void setUp() throws Exception {
        super.setUp();

        httpServletRequestMock =
                new HttpServletRequestMock("httpServletRequestMock",
                        expectations);

        httpServletResponseMock =
                new HttpServletResponseMock("httpServletResponseMock",
                        expectations);

        filterChainMock = new FilterChainMock("filterChainMock", expectations);
    }

    /**
     * Test that the ProjectFileFilter returns a 403 error if the URL
     * ends with mcs-project.xml and does not contain a request header of
     * "x-mcs-project-config".
     */
    public void testFilterWithProjectRequest()
            throws Exception {

        httpServletRequestMock.expects.getRequestURI()
                .returns("com/volantis/resource/mcs-project.xml");

        httpServletRequestMock.expects.getHeader("x-mcs-project-config")
                .returns(null);

        httpServletResponseMock.expects.sendError(403);

        ProjectFileFilter filter = new ProjectFileFilter();

        filter.doFilter(httpServletRequestMock,
                httpServletResponseMock, filterChainMock);
    }

    /**
     * Test that the ProjectFileFilter does not send a 403 error if the URL
     * ends with mcs-project.xml but contains a request header of
     * "x-mcs-project-config".
     */
    public void testFilterWithProjectRequestAndHeader()
            throws Exception {

        httpServletRequestMock.expects.getHeader("x-mcs-project-config")
                .returns("com/volantis/resource/mcs-project.xml");

        filterChainMock.expects.doFilter(httpServletRequestMock,
                httpServletResponseMock);

        ProjectFileFilter filter = new ProjectFileFilter();

        filter.doFilter(httpServletRequestMock,
                httpServletResponseMock, filterChainMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	9990/1	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 ===========================================================================
*/
