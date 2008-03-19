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

package com.volantis.mcs.xdime.integration;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.servlet.MarinerServletSessionContext;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import junit.framework.AssertionFailedError;
import mock.javax.servlet.http.HttpServletRequestMock;
import mock.javax.servlet.http.HttpServletResponseMock;
import mock.javax.servlet.http.HttpSessionMock;
import mock.org.xml.sax.ErrorHandlerMock;
import org.apache.commons.collections.IteratorEnumeration;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * Test cases for XDIME (1 and 2) integration.
 */
public class XDIMEIntegrationTest
        extends TestCaseAbstract {

    private final MarinerServletApplication servletApplication;
    private final String xdimePath;
    private final String expectedResultPath;
    private final String deviceName;
    private final boolean isXML;
    private String expectedContentType;

    public XDIMEIntegrationTest(
            MarinerServletApplication servletApplication, String deviceName,
            String xdimePath,
            String expectedResultPath, boolean isXML) {

        setName(xdimePath + " for " + deviceName);

        this.servletApplication = servletApplication;
        this.xdimePath = xdimePath;
        this.expectedResultPath = expectedResultPath;
        this.deviceName = deviceName;
        this.isXML = isXML;
        expectedContentType = "text/html;charset=ISO-8859-1";
    }

    public void setExpectedContentType(String expectedContentType) {
        this.expectedContentType = expectedContentType;
    }

    protected void runTest() throws Throwable {

        final HttpServletRequestMock requestMock =
                new HttpServletRequestMock("requestMock",
                        expectations);
        final HttpServletResponseMock responseMock =
                new HttpServletResponseMock("responseMock",
                        expectations);
        final ErrorHandlerMock errorHandlerMock =
                new ErrorHandlerMock("errorHandlerMock", expectations);

        final HttpSessionMock httpSessionMock =
                new HttpSessionMock("httpSessionMock", expectations);

        requestMock.expects.getHeader("Mariner-Application").returns(null)
                .any();

        // The following is only needed to ensure that errors are trapped correctly.
        requestMock.expects
                .setAttribute(MarinerRequestContext.class.getName(), null)
                .optional();

        requestMock.expects.getScheme().returns("http").any();
        requestMock.expects.getServerName().returns("host").any();
        requestMock.expects.getServerPort().returns(-1).any();
        requestMock.expects.getContextPath().returns("/volantis").any();
        requestMock.expects.getServletPath().returns("").any();
        requestMock.expects.getPathInfo()
                .returns(xdimePath)
                .any();
        requestMock.expects.getParameterNames().returns(new Enumeration() {

            public boolean hasMoreElements() {
                return false;
            }

            public Object nextElement() {
                throw new NoSuchElementException();
            }
        }).any();

        requestMock.expects.getAttribute(MarinerRequestContext.class.getName())
                .returns(null);
        requestMock.fuzzy.setAttribute(MarinerRequestContext.class.getName(),
                mockFactory.expectsInstanceOf(
                        MarinerServletRequestContext.class));

        requestMock.expects.getSession(false).returns(httpSessionMock).any();
        requestMock.expects.getSession(true).returns(httpSessionMock).any();

        requestMock.fuzzy.setAttribute("cacheScope", mockFactory.expectsAny())
                .returns().any();

        httpSessionMock.expects
                .getAttribute(MarinerServletSessionContext.class.getName())
                .returns(null);
        RetrieverAction sessionRetriever = new RetrieverAction();
        httpSessionMock.fuzzy
                .setAttribute(MarinerServletSessionContext.class.getName(),
                        mockFactory.expectsInstanceOf(
                                MarinerServletSessionContext.class))
                .does(sessionRetriever);

        Device device = servletApplication.getRuntimeDeviceRepository()
                .getDevice(deviceName);

        httpSessionMock.expects.getAttribute(Device.class.getName())
                .returns(device).any();

        final Map headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        headers.put("User-Agent", deviceName);

        requestMock.fuzzy.getHeader(mockFactory.expectsInstanceOf(String.class))
                .does(new MethodAction() {

                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        String header =
                                (String) event.getArgument(String.class);
                        return headers.get(header);
                    }
                }).any();
        requestMock.fuzzy
                .getHeaders(mockFactory.expectsInstanceOf(String.class))
                .does(new MethodAction() {

                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        String header =
                                (String) event.getArgument(String.class);
                        if (header == null) {
                            return null;
                        } else {
                            return new IteratorEnumeration(Collections
                                    .singletonList(header).iterator());
                        }
                    }
                }).any();
//        requestMock.expects.getHeaders("accept").returns(null).any();
        requestMock.expects.getHeaderNames()
                .returns(new IteratorEnumeration(headers.keySet().iterator()))
                .any();

        StringWriter writer = new StringWriter();

        // todo Find out why WML sets this twice and fix it.
        responseMock.expects.setContentType(expectedContentType).atLeast(1);
        responseMock.expects.getWriter().returns(new PrintWriter(writer));

        // todo add proper expectations.
        responseMock.fuzzy
                .addHeader(mockFactory.expectsAny(), mockFactory.expectsAny())
                .returns().any();
        responseMock.fuzzy
                .addDateHeader(mockFactory.expectsAny(),
                        mockFactory.expectsAny())
                .returns().any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MarinerRequestContext requestContext = new MarinerServletRequestContext(
                servletApplication.getServletContext(),
                requestMock, responseMock);

        httpSessionMock.expects
                .getAttribute(MarinerServletSessionContext.class.getName())
                .returns((MarinerServletSessionContext) sessionRetriever
                        .getObject()).any();

        requestMock.expects.getAttribute(MarinerRequestContext.class.getName())
                .returns(requestContext).any();

        // Nested canvases temporarily change the MarinerRequestContext bound
        // to the request. However, that information is not used internally so
        // just ignore it.

        requestMock.fuzzy
                .setAttribute(MarinerRequestContext.class.getName(),
                        mockFactory.expectsAny()).returns().any();

        requestMock.expects.setAttribute(
                MarinerRequestContext.class.getName(), requestContext).any();

        URL url = getClass().getResource("webapp/" + xdimePath);

        XMLReader reader =
                MarlinSAXHelper.getXMLReader(requestContext, errorHandlerMock);
        InputSource source = new InputSource(url.toExternalForm());
        reader.parse(source);

        URL resultURL = getClass().getResource("webapp/" + expectedResultPath);
        String expectedResult;
        if (resultURL == null) {
            expectedResult = "could not find '" + expectedResultPath + "'";
        } else {
            Reader resultReader = new InputStreamReader(resultURL.openStream());
            StringBuffer buffer = new StringBuffer();
            char[] chars = new char[1024];
            int read;
            while ((read = resultReader.read(chars)) > -1) {
                buffer.append(chars, 0, read);
            }

            expectedResult = buffer.toString();

            expectedResult =
                    expectedResult.replaceAll("<\\?xml-stylesheet[^?]+\\?>\n", "");
//            if (expectedResult.startsWith("<?xml-stylesheet")) {
//                expectedResult = expectedResult.substring(
//                        expectedResult.indexOf("?>\n") + 3);
//            }
        }

        String actualResult = writer.getBuffer().toString();
        if (isXML) {
            boolean failed = true;
            try {
                assertXMLEquals("", expectedResult, actualResult);
                failed = false;
            } catch(AssertionFailedError e) {
                e.printStackTrace();
            } finally {
                if (failed) {
                    assertEquals(expectedResult, actualResult);
                }
            }
        } else {
            assertEquals(expectedResult, actualResult);
        }
    }

    private static class RetrieverAction
            implements MethodAction {

        private Object object;

        public Object perform(MethodActionEvent methodActionEvent)
                throws Throwable {
            object = methodActionEvent.getArgument(Object.class);
            return null;
        }

        public Object getObject() {
            return object;
        }
    }
}
