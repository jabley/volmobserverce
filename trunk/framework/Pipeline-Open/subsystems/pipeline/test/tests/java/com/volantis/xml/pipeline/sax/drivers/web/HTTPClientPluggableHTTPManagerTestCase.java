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
import com.volantis.shared.net.http.cookies.CookieImpl;
import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.shared.net.url.http.utils.HttpClientUtils;
import com.volantis.shared.time.Period;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import junitx.util.PrivateAccessor;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.cookie.CookieSpecBase;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Unit test for the {@link HTTPClientPluggableHTTPManager} class
 */
public class HTTPClientPluggableHTTPManagerTestCase
            extends AbstractPluggableHTTPManagerTestAbstract {

    // javadoc inherited
    protected PluggableHTTPManager createTestablePluggableHTTPManager() {
        return new HTTPClientPluggableHTTPManager();
    }

    // JavaDoc inherited
    protected WebDriverConfiguration createTestWebDriverConfig() {
        return new WebDriverConfigurationImpl();
    }

    /**
     * Test the {@link HttpClientUtils#createCookieArray}
     * method
     */
    public void testCreateCookieArray() throws Throwable {
        String name = "Set-Cookie";
        String value = "name1=value1,name2=value2";
        org.apache.commons.httpclient.Header header =
                    new org.apache.commons.httpclient.Header();
        header.setName(name);
        header.setValue(value);

        HttpMethod httpMethod =
                    new GetMethod("https://wwww.volantis.com/index.jsp");

        String method = "createCookieArray";
        Class paramTypes [] = {org.apache.commons.httpclient.HttpMethod.class,
                               org.apache.commons.httpclient.Header.class};
        Object args [] = {httpMethod, header};

        org.apache.commons.httpclient.Cookie cookies [] =
                    (org.apache.commons.httpclient.Cookie[])
                    PrivateAccessor.invoke(HttpClientUtils.class, method, paramTypes, args);

        assertEquals("Should be 2 cookies", 2, cookies.length);

        // I'm assuming the order the cookies went in are the order
        // they come out which seems to be true at the time of writing.
        assertEquals("Checking cookie 0 name", "name1",
                     cookies[0].getName());
        assertEquals("Checking cookie 0 value", "value1",
                     cookies[0].getValue());
        assertEquals("Checking cookie 1 name", "name2",
                     cookies[1].getName());
        assertEquals("Checking cookie 0 value", "value2",
                     cookies[1].getValue());
    }

    /**
     * Test the method addHttpClientCookiesToWebDriverCookies
     */
    public void testAddHttpClientCookiesToWebDriverCookies() throws Throwable {
        HTTPClientPluggableHTTPManager manager =
                    (HTTPClientPluggableHTTPManager)
                    createTestablePluggableHTTPManager();

        CookieSpecBase cookieSpecBase = new CookieSpecBase();
        org.apache.commons.httpclient.Cookie[] cookiesArray =
                    cookieSpecBase.parse("www.volantis.com", 843, "/index", true,
                                         "name1=value1,name2=value2");

        HTTPMessageEntities responseCookies = new SimpleHTTPMessageEntities();

        String method = "addHttpClientCookiesToWebDriverCookies";
        Class paramTypes [] = {org.apache.commons.httpclient.Cookie[].class,
                               HTTPMessageEntities.class};
        Object args [] = {cookiesArray, responseCookies};
        PrivateAccessor.invoke(manager, method, paramTypes, args);


        assertTrue("Expected two cookies.", responseCookies.size() == 2);

        CookieImpl[] cookies = new CookieImpl[2];
        int count = 0;
        Iterator cookiesIterator = responseCookies.iterator();
        while (cookiesIterator.hasNext()) {
            CookieImpl cookie = (CookieImpl) cookiesIterator.next();
            cookies[count] = cookie;
            count++;
        }

        assertEquals("Checking cookie 1 name", "name1",
                     cookies[0].getName());
        assertEquals("Checking cookie 1 value", "value1",
                     cookies[0].getValue());
        assertEquals("Checking cookie 2 name", "name2",
                     cookies[1].getName());
        assertEquals("Checking cookie 2 value", "value2",
                     cookies[1].getValue());
    }

    /**
     * Test the method addHttpClientHeaderToWebDriverHeaders
     */
    public void testAddHttpClientHeaderToWebDriverHeaders() throws Throwable {
        addHeaderTestImpl("header", "h-value", true);
        addHeaderTestImpl("Location", "http://www.turkcell.com.tr/index/0,1028,400,00.html", false);
    }

    private void addHeaderTestImpl(String headerName, String headerValue,
                                   boolean list)
            throws Throwable {
        HTTPClientPluggableHTTPManager manager =
                    (HTTPClientPluggableHTTPManager)
                    createTestablePluggableHTTPManager();

        final org.apache.commons.httpclient.Header header =
                    new org.apache.commons.httpclient.Header();
        header.setName(headerName);
        header.setValue(headerValue);

        HTTPMessageEntities responseHeader = new SimpleHTTPMessageEntities();

        String method = "addHttpClientHeaderToWebDriverHeaders";
        Class paramTypes [] = {org.apache.commons.httpclient.Header.class,
                               HTTPMessageEntities.class,
                               boolean.class };
        Object args [] = {header, responseHeader, Boolean.valueOf(list)};
        PrivateAccessor.invoke(manager, method, paramTypes, args);

        Iterator headers = responseHeader.iterator();
        while (headers.hasNext()) {
            HeaderImpl headerImpl = (HeaderImpl) headers.next();
            assertEquals("Unexpected header name.",
                         headerName, headerImpl.getName());
            assertEquals("Unexpected header value.",
                         headerValue, headerImpl.getValue());
        }
    }


    /**
     * Test the calculateResponseCookieMaxAge method.
     */
    public void testCalculateResponseCookieMaxAge() throws Throwable {
        org.apache.commons.httpclient.Cookie cookie =
                    new org.apache.commons.httpclient.Cookie();
        Calendar calendar = Calendar.getInstance();
        Date expiryDate = calendar.getTime();
        cookie.setExpiryDate(expiryDate);

        String method = "calculateResponseCookieMaxAge";
        Class paramTypes [] = {org.apache.commons.httpclient.Cookie.class};
        Object args [] = {cookie};

        Integer maxAge = (Integer) PrivateAccessor.invoke(
            HttpClientUtils.class, method, paramTypes, args);

        assertNotNull(maxAge);

        // maxAge should be negative and close to 0 i.e. the length of time
        // it has taken to get from this method to System.currentTimeMillis
        // in calculateResponseCookiesMaxAge.
        assertEquals("maxAge should be between 0: " + maxAge.intValue(), 0,
                     maxAge.intValue());

        // Testing missing expiry date
        cookie.setExpiryDate(null);
        maxAge = (Integer) PrivateAccessor.invoke(
            HttpClientUtils.class, method, paramTypes, args);

        assertNotNull(maxAge);
        assertEquals("maxAge should be -1", -1, maxAge.intValue());
    }


    /**
     * Tests that the configuration has a default timeout of -1 and that the
     * accessors deal with the timeout correctly.
     */
    public void testTimeoutConfiguration() throws Throwable {
        final long timeout = 3000;
        HTTPClientPluggableHTTPManager manager = (HTTPClientPluggableHTTPManager)
                createTestablePluggableHTTPManager();

        WebDriverConfiguration webdConfig = new WebDriverConfigurationImpl();

        assertEquals(-1, webdConfig.getTimeoutInMillis());

        webdConfig.setTimeout(timeout);
        manager.initialize(webdConfig, Period.INDEFINITELY);

        assertEquals(timeout, manager.configuration.getTimeoutInMillis());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 04-May-05	8022/2	pcameron	VBM:2005042704 Fixed configuration of SSL providers

 04-May-05	7988/1	pcameron	VBM:2005042704 Fixed configuration of SSL providers

 24-Mar-05	7501/1	matthew	VBM:2005031708 Make CachingPluggableHTTPManager use request parameters a part of the cache key and refactor AbstractPluggableHTTPManager

 23-Mar-05	7443/1	matthew	VBM:2005031708 allow urls with parameters to be cached

 22-Mar-05	7474/1	pcameron	VBM:2005031018 Added timeout to web driver configuration

 22-Mar-05	7472/1	pcameron	VBM:2005031018 Added timeout to web driver configuration

 02-Mar-05	7224/1	matthew	VBM:2005012108 Allow cookies to be set on HttpClient requests

 02-Mar-05	7222/1	matthew	VBM:2005012108 Allow cookies to be set on HttpClient requests

 29-Nov-04	6302/3	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 29-Nov-04	6302/1	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jul-04	786/1	claire	VBM:2004071304 Refactor web driver to support HTTPS

 16-Jul-04	767/1	claire	VBM:2004070101 Provide Jigsaw implemention of PluggableHTTPManager

 12-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
