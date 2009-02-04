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

package com.volantis.mcs.servlet.http;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import javax.servlet.http.HttpServletRequest;

import mock.javax.servlet.http.HttpServletRequestMock;

import java.net.URL;

/**
 * Test cases for {@link HttpRequestStandardizer}.
 */
public class HttpRequestStandardizerTestCase
        extends TestCaseAbstract {
    private HttpServletRequestMock httpServletRequestMock;
    private HttpRequestStandardizer standardizer;

    protected void setUp() throws Exception {
        super.setUp();

        httpServletRequestMock = new HttpServletRequestMock(
                "httpServletRequestMock", expectations);

        standardizer = new HttpRequestStandardizer(httpServletRequestMock);
    }

    /**
     * Test context path.
     */
    public void testContextPath() throws Exception {
        doTestContextPath("/context", "/context");
        doTestContextPath("/context", "context");
        doTestContextPath("/context", "/context/");
        doTestContextPath("/context", "context/");
        doTestContextPath("", "/");
        doTestContextPath("", "");
        doTestContextPath("", null);
    }

    private void doTestContextPath(final String expected,
                                   final String contextPath) {

        httpServletRequestMock.expects.getContextPath().returns(contextPath);
        assertEquals(expected, standardizer.getContextPath());
    }

    /**
     * Test servlet path.
     */
    public void testServletPath() throws Exception {
        doTestServletPath("/servlet", "/servlet");
        doTestServletPath("/servlet", "servlet");
        doTestServletPath("/servlet", "/servlet/");
        doTestServletPath("/servlet", "servlet/");
        doTestServletPath("", "/");
        doTestServletPath("", "");
        doTestServletPath("", null);
    }

    private void doTestServletPath(final String expected,
                                   final String servletPath) {

        httpServletRequestMock.expects.getServletPath().returns(servletPath);
        assertEquals(expected, standardizer.getServletPath());
    }

    /**
     * Test path info.
     */
    public void testPathInfo() throws Exception {
        doTestPathInfo("pathInfo", "/pathInfo");
        doTestPathInfo("pathInfo", "pathInfo");
        doTestPathInfo("pathInfo/", "/pathInfo/");
        doTestPathInfo("pathInfo/", "pathInfo/");
        doTestPathInfo(null, "/");
        doTestPathInfo(null, "");
        doTestPathInfo(null, null);
    }

    private void doTestPathInfo(final String expected, final String pathInfo) {
        httpServletRequestMock.expects.getPathInfo().returns(pathInfo);
        assertEquals(expected, standardizer.getPathInfo());
    }

    /**
     * Test path info.
     */
    public void testContextRelativePathInfo() throws Exception {

        doTestContextRelativePathInfo("/servlet/pathInfo", "/servlet", "/pathInfo");
        doTestContextRelativePathInfo("/servlet", "/servlet", null);
        doTestContextRelativePathInfo("", "", null);
    }

    private void doTestContextRelativePathInfo(
            String expected, String servletPath, String pathInfo) {

        httpServletRequestMock.expects.getServletPath().returns(servletPath);
        httpServletRequestMock.expects.getPathInfo().returns(pathInfo);
        assertEquals(expected, standardizer.getContextRelativePathInfo());
    }

    /**
     * Test host URL.
     */ 
    public void testHostURL() throws Exception {

        httpServletRequestMock.expects.getServerName().returns("host");
        httpServletRequestMock.expects.getScheme().returns("http");
        httpServletRequestMock.expects.getServerPort().returns(98);

        URL url = standardizer.getHostURL();
        assertEquals("http://host:98/", url.toExternalForm());
    }
}
