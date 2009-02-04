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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/servlet/ServletEnvironmentContextTestCase.java,v 1.6 2003/02/20 13:02:45 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Dec-02    Phil W-S        VBM:2002121001 - Created.
 * 17-Dec-02    Allan           VBM:2002121711 - Testcase for
 *                              ServletEnvironmentContext. Just tests
 *                              setContentType() at the moment. Rewritten
 *                              quite a lot of the previous change so that
 *                              reponse stub classes are used and
 *                              setUp/tearDown
 * 20-Jan-03    Allan           VBM:2002121901 - Modified contentTypeCharSet
 *                              property renamed to characterEncoding with its
 *                              get/set methods.
 * 19-Feb-03    Phil W-S        VBM:2003021707 - Add test for
 *                              compositeContentType (removing redundant
 *                              "notest" methods).
 * 20-Feb-03    Phil W-S        VBM:2003021707 - Rework to correct behaviour to
 *                              match that identified by decompiling the
 *                              (broken) WLS class that handles the charset
 *                              definition on ContentType.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.testtools.stubs.HttpServletResponseStub;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.shared.time.Time;
import com.volantis.shared.time.Period;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Testcase for ServletEnvironmentContext.
 */
public class ServletEnvironmentContextTestCase extends TestCase {
    ServletEnvironmentContext envContext;
    MyMarinerServletRequestContext requestContext;
    TestMarinerPageContext pageContext;
    MyHttpServletResponse response;

    /**
     * Set up the layout for this testcase.
     */
    public void setUp() {
        response = new MyHttpServletResponse();
        requestContext = new MyMarinerServletRequestContext(response);
        pageContext = new TestMarinerPageContext();
        pageContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        envContext = new ServletEnvironmentContext(requestContext);
    }

    /**
     * Tear down everything that was set up.
     */
    public void tearDown() {
        requestContext = null;
        response = null;
        envContext = null;
    }

    public ServletEnvironmentContextTestCase(String name) {
        super(name);
    }

    /**
     * Test the setContentType() method.
     */
    public void testSetContentType() throws Exception {
        requestContext.setCharacterEncoding("charset");
        envContext.setContentType("mimeType");
        assertEquals("mimeType;charset=charset", response.getContentType());
    }

    public void testCompositeContentType() throws Exception {
        assertEquals("no charset simple ContentType not as",
                     "mimeType",
                     envContext.compositeContentType("mimeType", null));
        assertEquals("charset simple ContentType not as",
                     "mimeType;charset=charset",
                     envContext.compositeContentType("mimeType", "charset"));
        assertEquals("no charset parameterized ContentType not as",
                     "mimeType;foo=bar",
                     envContext.compositeContentType("mimeType;foo=bar", null));
        assertEquals("charset parameterized ContentType not as",
                     "mimeType;foo=bar;charset=charset",
                     envContext.compositeContentType("mimeType;foo=bar",
                                                  "charset"));
    }

    public void testApplyCachingDirectivesCachingNotSet() {
        envContext.applyCachingDirectives();
        assertEquals("no-cache", response.getHeader("Pragma"));
        assertEquals("no-cache", response.getHeader("Cache-Control"));
        final Calendar calendar =
            new GregorianCalendar(1990, Calendar.JANUARY, 1);
        assertEquals(calendar.getTimeInMillis(),
            response.getDateHeader("Expires"));
    }

    public void testApplyCachingDirectivesNoExpires() {
        final ResponseCachingDirectives cachingDirectives =
            envContext.getCachingDirectives();
        cachingDirectives.enable();
        envContext.applyCachingDirectives();
        assertEquals("no-cache", response.getHeader("Pragma"));
        assertEquals("no-cache", response.getHeader("Cache-Control"));
        final Calendar calendar =
            new GregorianCalendar(1990, Calendar.JANUARY, 1);
        assertEquals(calendar.getTimeInMillis(),
            response.getDateHeader("Expires"));
    }

    public void testApplyCachingDirectivesWithExpires() {
        final ResponseCachingDirectives cachingDirectives =
            envContext.getCachingDirectives();
        cachingDirectives.enable();
        final long expiresInMilliSeconds = System.currentTimeMillis() + 50000;
        cachingDirectives.setExpires(Time.inMilliSeconds(expiresInMilliSeconds),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        envContext.applyCachingDirectives();

        final String maxAgeValue = response.getHeader("Cache-Control");
        assertTrue(maxAgeValue.startsWith("max-age="));
        final int maxAgeInSeconds =
            Integer.parseInt(maxAgeValue.substring("max-age=".length()));
        assertTrue(maxAgeInSeconds >=48 && maxAgeInSeconds <= 50);

        assertEquals(expiresInMilliSeconds, response.getDateHeader("Expires"));

        final String headerValue = response.getHeader("Vary");
        final Set headerNames = new HashSet();
        final StringTokenizer tokenizer = new StringTokenizer(headerValue, ",");
        while (tokenizer.hasMoreTokens()) {
            headerNames.add(tokenizer.nextToken());
        }
        assertEquals(ServletEnvironmentContext.VARY_HEADER_NAMES, headerNames);
    }

    public void testApplyCachingDirectivesWithMaxAge() {
        final ResponseCachingDirectives cachingDirectives =
            envContext.getCachingDirectives();
        cachingDirectives.enable();
        cachingDirectives.setMaxAge(Period.inSeconds(50),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        envContext.applyCachingDirectives();

        final String maxAgeValue = response.getHeader("Cache-Control");
        assertTrue(maxAgeValue.startsWith("max-age="));
        final int maxAgeInSeconds =
            Integer.parseInt(maxAgeValue.substring("max-age=".length()));
        assertTrue(maxAgeInSeconds >=48 && maxAgeInSeconds <= 50);

        assertTrue((System.currentTimeMillis() + 50000) >=
            response.getDateHeader("Expires"));

        final String headerValue = response.getHeader("Vary");
        final Set headerNames = new HashSet();
        final StringTokenizer tokenizer = new StringTokenizer(headerValue, ",");
        while (tokenizer.hasMoreTokens()) {
            headerNames.add(tokenizer.nextToken());
        }
        assertEquals(ServletEnvironmentContext.VARY_HEADER_NAMES, headerNames);
    }

    public void testApplyCachingDirectivesDisabled() {
        final ResponseCachingDirectives cachingDirectives =
            envContext.getCachingDirectives();
        cachingDirectives.enable();
        cachingDirectives.setMaxAge(Period.inSeconds(50),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        cachingDirectives.disable();

        envContext.applyCachingDirectives();
        assertEquals("no-cache", response.getHeader("Pragma"));
        assertEquals("no-cache", response.getHeader("Cache-Control"));
        final Calendar calendar =
            new GregorianCalendar(1990, Calendar.JANUARY, 1);
        assertEquals(calendar.getTimeInMillis(),
            response.getDateHeader("Expires"));
    }

    /**
     * Specialization that can be used to unit testing. This avoids issues
     * with actual sending of HTTP responses.
     */
    protected class MyHttpServletResponse extends HttpServletResponseStub {
        private Map headers = new HashMap();

        private String contentType;

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }

        /**
         * Special method for test purposes.
         *
         * @param s the key for the header to retrieve
         * @return the header value for the given key
         */
        public String getHeader(String s) {
            return (String)headers.get(s);
        }

        /**
         * Special method for test purposes.
         *
         * @param s the key for the header to retrieve
         * @return the header value for the given key
         */
        public int getIntHeader(String s) {
            return ((Integer)headers.get(s)).intValue();
        }

        /**
         * Special method for test purposes.
         *
         * @param s the key for the header to retrieve
         * @return the header value for the given key
         */
        public long getDateHeader(String s) {
            return ((Long)headers.get(s)).longValue();
        }

        public boolean containsHeader(String s) {
            return headers.containsKey(s);
        }

        public void setDateHeader(String s, long l) {
            headers.put(s, new Long(l));
        }

        public void addDateHeader(String s, long l) {
            setDateHeader(s, l);
        }

        public void setHeader(String s, String s1) {
            headers.put(s, s1);
        }

        public void addHeader(String s, String s1) {
            setHeader(s, s1);
        }

        public void setIntHeader(String s, int i) {
            headers.put(s, new Integer(i));
        }

        public void addIntHeader(String s, int i) {
            setIntHeader(s, i);
        }

    }

    /**
     * Allows unit testing without the need to fully initialize the
     * request context.
     */
    protected class MyMarinerServletRequestContext
        extends MarinerServletRequestContext {
        HttpServletResponse response;

        MyMarinerServletRequestContext(HttpServletResponse response) {
            this.response = response;
        }

        public ServletResponse getResponse() {
            return response;
        }

        public HttpServletResponse getHttpResponse() {
            return response;
        }
    }
}
