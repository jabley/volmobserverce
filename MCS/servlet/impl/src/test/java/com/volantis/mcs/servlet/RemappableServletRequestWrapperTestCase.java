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
package com.volantis.mcs.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.javax.servlet.http.HttpServletRequestMock;

import java.net.URL;

/**
 * Verifies that {@link RemappableServletRequestWrapper} behaves as expected
 * i.e. just passes the request onto the servlet request until the URL has been
 * reset, after which point it returns the modified information.
 */
public class RemappableServletRequestWrapperTestCase
        extends TestCaseAbstract {

    private RemappableServletRequestWrapper wrapper;
    private HttpServletRequestMock request;
    private static final String placeHolder = "placeHolder";
    private static final StringBuffer placeHolderBuffer =
            new StringBuffer(placeHolder);
    private URL url;


    private static final String externalForm =
            "http://server:8080/context/servlet/path/pathInfo?queryString=qs";

    private static final String scheme = "http";
    private static final String schemeSuffix = "://";

    private static final String server = "server";
    private static final String authoritySeparator = ":";
    private static int port = 8080;
    private static final String context = "/context";
    private static final String servletPath = "/servlet/path";
    private static final String pathInfo = "/pathInfo";
    private static final String queryString = "queryString=qs";

    private static final String requestURI = server + authoritySeparator +
            port + context + servletPath + pathInfo;
    private static final String requestURL = scheme  + schemeSuffix + requestURI;

    protected void setUp() throws Exception {
        super.setUp();
        request = new HttpServletRequestMock("request", expectations);
        wrapper = new RemappableServletRequestWrapper(request);
        url = new URL(externalForm);
        request.expects.getPathInfo().returns(pathInfo);
    }

    public void testGetRequestURL() {
        request.expects.getRequestURL().returns(placeHolderBuffer);
        assertEquals(placeHolderBuffer, wrapper.getRequestURL());
        
        wrapper.resetRequestURL(url);
        assertEquals(requestURL, wrapper.getRequestURL().toString());
    }

    public void testGetRequestURI() {
        request.expects.getRequestURI().returns(placeHolder);
        assertEquals(placeHolder, wrapper.getRequestURI());

        wrapper.resetRequestURL(url);
        assertEquals(requestURI, wrapper.getRequestURI());
    }

    public void testGetContextPath() {
        request.expects.getContextPath().returns(placeHolder);
        assertEquals(placeHolder, wrapper.getContextPath());

        wrapper.resetRequestURL(url);
        assertEquals(context, wrapper.getContextPath());
    }

    public void testGetServletPath() {
        request.expects.getServletPath().returns(placeHolder);
        assertEquals(placeHolder, wrapper.getServletPath());

        wrapper.resetRequestURL(url);
        assertEquals(servletPath, wrapper.getServletPath());
    }

    public void testGetPathInfo() {
        request.expects.getPathInfo().returns(pathInfo).fixed(2);
        assertEquals(pathInfo, wrapper.getPathInfo());

        wrapper.resetRequestURL(url);
        assertEquals(pathInfo, wrapper.getPathInfo());
    }

     public void testGetPathTranslated() {
        request.expects.getPathInfo().returns(pathInfo).fixed(2);
        assertEquals(pathInfo, wrapper.getPathTranslated());

        wrapper.resetRequestURL(url);
        assertEquals(pathInfo, wrapper.getPathTranslated());
    }

    public void testGetQueryString() {
        request.expects.getQueryString().returns(placeHolder);
        assertEquals(placeHolder, wrapper.getQueryString());

        wrapper.resetRequestURL(url);
        assertEquals(queryString, wrapper.getQueryString());
    }

    public void testGetScheme() {
        request.expects.getScheme().returns(placeHolder);
        assertEquals(placeHolder, wrapper.getScheme());

        wrapper.resetRequestURL(url);
        assertEquals(scheme, wrapper.getScheme());
    }

     public void testGetServerName() {
        request.expects.getServerName().returns(placeHolder);
        assertEquals(placeHolder, wrapper.getServerName());

        wrapper.resetRequestURL(url);
        assertEquals(server, wrapper.getServerName());
    }

    public void testGetPort() {
        request.expects.getServerPort().returns(10);
        assertEquals(10, wrapper.getServerPort());

        wrapper.resetRequestURL(url);
        assertEquals(port, wrapper.getServerPort());
    }

    public void testGetProtocol() {
        request.expects.getProtocol().returns(placeHolder).fixed(2);
        assertEquals(placeHolder, wrapper.getProtocol());

        wrapper.resetRequestURL(url);
        assertEquals(placeHolder, wrapper.getProtocol());
    }
}
