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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.cookies.CookieImpl;
import com.volantis.shared.net.http.cookies.CookieVersion;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.PropertyContainer;
import com.volantis.xml.pipeline.sax.RecoverableComplexPropertyContainer;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineContextStub;
import junitx.util.PrivateAccessor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Unit test for the {@link AbstractPluggableHTTPManager} class
 */
public abstract class AbstractPluggableHTTPManagerTestAbstract
            extends TestCaseAbstract {

    /**
     * Create a new instance of this test case.
     */
    public AbstractPluggableHTTPManagerTestAbstract() {
    }

    /**
     * Create a new named instance of this test case.
     * 
     * @param s The name of the test case.
     */
    public AbstractPluggableHTTPManagerTestAbstract(String s) {
        super(s);
    }

    /**
     * Creates a <code>PluggableHTTPManager</code> instance for testing
     * @return a <code>PluggableHTTPManager</code> instance
     */
    protected abstract PluggableHTTPManager
                createTestablePluggableHTTPManager();

    /**
     * Creates a <code>WebDriverConfiguration</code> instance for building
     * a valid test environment.
     *
     * @return A <code>WebDriverConfiguration</code> instance
     */
    protected abstract WebDriverConfiguration createTestWebDriverConfig();





    /**
     * Test that the populateWebDriverResponse adds headers and cookies
     * correctly added to the WebDriverResponse.
     */
    public void testAddHeadersToWebDriverResponse() throws Throwable {
        PluggableHTTPManager manager = createTestablePluggableHTTPManager();
        PropertyContainer container = new RecoverableComplexPropertyContainer();

        WebDriverRequest request = new WebDriverRequestImpl();
        WebDriverResponseImpl response = new WebDriverResponseImpl();
        WebDriverAccessor accessor = createWebDriverAccessor(request, response);
        container.setProperty(WebDriverAccessor.class, accessor, false);

        String cookieName = "Set-Cookie";
        String cookieValue = "name1=value1";
        CookieVersion cookieVersion = CookieVersion.RFC2109;

        final Cookie cookieHeader =
                    new CookieImpl(cookieName, "localhost", ".");
        cookieHeader.setValue(cookieValue);
        cookieHeader.setVersion(cookieVersion);

        String headerName = "Other-Header";
        String headerValue = "o-h-value";
        final Header otherHeader = new HeaderImpl(headerName);
        otherHeader.setValue(headerValue);

        String method = "populateWebDriverResponse";
        Class paramTypes [] = {
            WebDriverResponse.class,
            HTTPResponseAccessor.class,
            HTTPMessageEntities.class};
        Object args [] = {
            response,
            new HTTPResponseAccessor() {
                public HTTPMessageEntities getCookies() {
                    HTTPMessageEntities cookies =
                                new SimpleHTTPMessageEntities();
                    cookies.add(cookieHeader);
                    return cookies;
                }

                public HTTPMessageEntities getHeaders() {
                    HTTPMessageEntities headers =
                                new SimpleHTTPMessageEntities();
                    headers.add(otherHeader);
                    return headers;
                }

                public InputStream getResponseStream() {
                    return null;
                }

                public int getStatusCode() {
                    return 200;
                }

                /**
                 * Get the HTTP version returned by the server.
                 * @return The HTTP version returned by the server.
                 */
                public HTTPVersion getHTTPVersion() {
                    return HTTPVersion.HTTP_1_1;
                }

            },
            new SimpleHTTPMessageEntities()
        };
        PrivateAccessor.invoke(manager, method, paramTypes, args);

        HTTPMessageEntities responseHeaders = response.getHeaders();
        HTTPMessageEntities responseCookies = response.getCookies();

        assertTrue("Expected one header.", responseHeaders.size() == 1);
        assertTrue("Expected one cookies.", responseCookies.size() == 1);

        Iterator headers = responseHeaders.iterator();
        while (headers.hasNext()) {
            HeaderImpl header = (HeaderImpl) headers.next();
            assertEquals("Unexpected header name.",
                         headerName, header.getName());
            assertEquals("Unexpected header value.",
                         headerValue, header.getValue());
        }

        Iterator cookiesIterator = responseCookies.iterator();
        while (cookiesIterator.hasNext()) {
            CookieImpl cookie = (CookieImpl) cookiesIterator.next();
            assertEquals("Checking cookie name",
                         cookieName,
                         cookie.getName());
            assertEquals("Checking cookie value",
                         cookieValue,
                         cookie.getValue());
            assertEquals("checking cookie version",
                         cookieVersion,
                         cookie.getVersion());
        }
    }

    /**
     * Test that the populateWebDriverResponse adds headers and cookies
     * correctly added to the WebDriverResponse with additional headers.
     */
    public void testAddHeadersToWebDriverResponseWithAdditionalHeaders()
                throws Throwable {

        PluggableHTTPManager manager = createTestablePluggableHTTPManager();
        PropertyContainer container = new RecoverableComplexPropertyContainer();

        WebDriverRequest request = new WebDriverRequestImpl();
        WebDriverResponseImpl response = new WebDriverResponseImpl();
        WebDriverAccessor accessor = createWebDriverAccessor(request, response);
        container.setProperty(WebDriverAccessor.class, accessor, false);

        String cookieName = "Set-Cookie";
        String cookieValue = "name1=value1";
        final Cookie cookie1 = new CookieImpl(cookieName, "localhost", "/");
        cookie1.setValue(cookieValue);

        String headerName = "Other-Header";
        String headerValue = "o-h-value";
        final Header otherHeader = new HeaderImpl(headerName);
        otherHeader.setValue(headerValue);

        HTTPMessageEntities additionalCookies = new SimpleHTTPMessageEntities();
        Cookie additionalCookie = new CookieImpl("additionalName",
                                                 "localhost",
                                                 "/");
        additionalCookie.setValue("additionalValue");
        additionalCookies.add(additionalCookie);

        String method = "populateWebDriverResponse";

        Class paramTypes [] = {
            WebDriverResponse.class,
            HTTPResponseAccessor.class,
            HTTPMessageEntities.class};

        Object[] args  = {
            response,
            new HTTPResponseAccessor() {
                public HTTPMessageEntities getCookies() {
                    HTTPMessageEntities cookies =
                                new SimpleHTTPMessageEntities();
                    cookies.add(cookie1);
                    return cookies;
                }

                public HTTPMessageEntities getHeaders() {
                    HTTPMessageEntities headers =
                                new SimpleHTTPMessageEntities();
                    headers.add(otherHeader);
                    return headers;
                }

                public InputStream getResponseStream() {
                    return null;
                }

                public int getStatusCode() {
                    return 200;
                }

                /**
                 * Get the HTTP version returned by the server.
                 * @return The HTTP version returned by the server.
                 */
                public HTTPVersion getHTTPVersion() {
                    return HTTPVersion.HTTP_1_1;
                }
            },
            additionalCookies
        };
        PrivateAccessor.invoke(manager, method, paramTypes, args);

        HTTPMessageEntities responseHeaders = response.getHeaders();
        HTTPMessageEntities responseCookies = response.getCookies();

        final int expectedCookies = 2;
        assertEquals("Expected one header.", 1, responseHeaders.size());
        assertEquals("Expected number of cookies to match", expectedCookies,
                     responseCookies.size());

        Iterator headers = responseHeaders.iterator();
        while (headers.hasNext()) {
            HeaderImpl header = (HeaderImpl) headers.next();
            assertEquals("Unexpected header name.",
                         headerName, header.getName());
            assertEquals("Unexpected header value.",
                         headerValue, header.getValue());
        }

        CookieImpl[] cookies = new CookieImpl[expectedCookies];

        int count = 0;
        Iterator cookiesIterator = responseCookies.iterator();
        while (cookiesIterator.hasNext()) {
            CookieImpl cookie = (CookieImpl) cookiesIterator.next();
            cookies[count] = cookie;
            count++;
        }

        assertEquals("Checking cookie 1 name",
                     "additionalName",
                     cookies[0].getName());
        assertEquals("Checking cookie 1 value",
                     "additionalValue",
                     cookies[0].getValue());
        assertEquals("Checking cookie 2 name",
                     cookieName,
                     cookies[1].getName());
        assertEquals("Checking cookie 2 value",
                     cookieValue,
                     cookies[1].getValue());
    }

    /**
     * Create a WebDriverAccessor for use by this testcase.
     * @param request The WebDriverRequest to associate with the created
     * WebDriverAccessor
     * @param response The WebDriverResponse to associate with the created
     * WebDriverAccessor.
     * @return a WebDriverAccessor
     */
    protected WebDriverAccessor
                createWebDriverAccessor(final WebDriverRequest request,
                                        final WebDriverResponse response) {

        return new WebDriverAccessor() {
            public WebDriverRequest
                        getRequest(XMLPipelineContext pipelineContext) {
                return request;
            }

            public WebDriverResponse
                        getResponse(XMLPipelineContext pipelineContext,
                                    String id) {
                return response;
            }
        };
    }

    /**
     * Mock implementation of XMLPipeline context that permits setting getting
     * of certain values.
     */
    protected static class MockXMLPipelineContext extends XMLPipelineContextStub {
        private Map properties = new HashMap();

        // javadoc inherited
        public void setProperty(Object key,
                                Object value,
                                boolean releaseOnChange) {
            properties.put(key, value);
        }

        // javadoc inherited
        public Object getProperty(Object key) {
            return properties.get(key);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9319/3	pcameron	VBM:2005081216 Fixed cookie propagation when caching

 24-Mar-05	7501/1	matthew	VBM:2005031708 Make CachingPluggableHTTPManager use request parameters a part of the cache key and refactor AbstractPluggableHTTPManager

 24-Mar-05	7443/1	matthew	VBM:2005031708 refactor AbstractPluggableHTTPManager

 09-Mar-05	7337/1	matthew	VBM:2005030809 Add a CookieVersion typesafe enum and use it in the Cookie interface and its implementations

 08-Mar-05	7331/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 08-Mar-05	7284/1	matthew	VBM:2005030212 Modify HTTPMessageEntity cookie implementations to allow a version number

 15-Feb-05	6976/1	matthew	VBM:2005020308 Add HTTP Caching mechanism

 29-Nov-04	6302/3	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 29-Nov-04	6302/1	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 08-Sep-04	869/1	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	865/2	doug	VBM:2004090707 Add web driver request preprocessing

 20-Jul-04	789/1	matthew	VBM:2004071602 Add HTTPVersion to WebDriverResponse

 16-Jul-04	767/1	claire	VBM:2004070101 Provide Jigsaw implemention of PluggableHTTPManager

 12-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
