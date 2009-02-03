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

package com.volantis.synergetics.testtools.mock.libraries.javax.servlet;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.javax.servlet.FilterChainMock;
import mock.javax.servlet.ServletConfigMock;
import mock.javax.servlet.ServletContextMock;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;
import mock.javax.servlet.http.HttpSessionMock;

/**
 * Tests for servlet related mock objects.
 */
public class ServletTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new HttpSessionMock("httpSession", expectations);
        new ServletConfigMock("servletConfig", expectations);
        new ServletContextMock("servletContext", expectations);
        new HttpServletRequestMock("httpServletRequest", expectations);
        new HttpServletResponseMock("httpServletResponse", expectations);
        new FilterChainMock("filterChain", expectations);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 ===========================================================================
*/
