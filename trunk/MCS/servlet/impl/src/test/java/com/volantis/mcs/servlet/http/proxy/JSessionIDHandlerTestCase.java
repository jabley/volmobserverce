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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet.http.proxy;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.HttpServletRequestStub;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Collections;

/**
 * Test cases for JSessionIDHandler
 *
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public class JSessionIDHandlerTestCase extends TestCaseAbstract {

    /**
     * standard constructor
     * @param name the name of the test case
     */
    public JSessionIDHandlerTestCase(String name) {
        super(name);
    }

    /**
     * Check that path style JSessionID's are removed and no other
     * changes made.
     * @throws Exception
     */
    public void testRemoveSessionInfoFromPath() throws Exception {
        JSessionIDHandler handler = new JSessionIDHandler();

        // create a url with no session.
        String pathNoSession =
                "http://wibble.woo/index.html?param1=a&param2=b";
        StringBuffer sbPathNoSession = new StringBuffer(pathNoSession);
        String result = handler.removeSessionInfoFromPath(sbPathNoSession);
        assertNull("Nothing should have been removed from the url", result);
        assertEquals("url should not have been modified", pathNoSession,
                     sbPathNoSession.toString());

        // test that param sessions are left alone.
        String paramSession =
                "http://wibble.woo/index.html?JSessionid=342FSF342GHW&param2=b";
        StringBuffer sbParamSession = new StringBuffer(paramSession);
        result = handler.removeSessionInfoFromPath(sbParamSession);
        assertNull("Nothing should have been removed from the url", result);
        assertEquals("url should not have been modified", paramSession,
                     sbParamSession.toString());

        // test session url with params
        String pathSession =
                "http://wibble.woo/index.html;JSessionid=342FSF342GHW?param2=b";
        StringBuffer sbPathSession = new StringBuffer(pathSession);
        result = handler.removeSessionInfoFromPath(sbPathSession);
        assertEquals("Session should have been removed", result,
                     "342FSF342GHW");
        assertEquals("url have had session information removed",
                     "http://wibble.woo/index.html?param2=b",
                     sbPathSession.toString());

    }

    /**
     * Check that paramater style JSessionID's are removed and no other
     * changes made.
     * @throws Exception
     */
    public void testRemoveSessionInfoFromParams() throws Exception {
        JSessionIDHandler handler = new JSessionIDHandler();

        // create a url with no session.
        String paramNoSession =
                "http://wibble.woo/index.html?param1=a&param2=b";
        StringBuffer sbParamNoSession = new StringBuffer(paramNoSession);
        String result = handler.removeSessionInfoFromParams(sbParamNoSession);
        assertNull("Nothing should have been removed from the url", result);
        assertEquals("url should not have been modified",
                     paramNoSession, sbParamNoSession.toString());

        // test that param sessions are processed.
        String paramSession =
                "http://wibble.woo/index.html?JSessionid=342FSF342GHW&param2=b";
        StringBuffer sbParamSession = new StringBuffer(paramSession);
        result = handler.removeSessionInfoFromParams(sbParamSession);
        assertEquals("Session should have been removed from params",
                     "http://wibble.woo/index.html?param2=b",
                     sbParamSession.toString());
        assertEquals("url should have been modified",
                     "342FSF342GHW", result);

        // test that param sessions are processed even if no subsequent params
        // exist
        paramSession =
                "http://wibble.woo/index.html?JSessionid=342FSF342GHW";
        sbParamSession = new StringBuffer(paramSession);
        result = handler.removeSessionInfoFromParams(sbParamSession);
        assertEquals("Session should have been removed from params",
                     "http://wibble.woo/index.html",
                     sbParamSession.toString());
        assertEquals("removed session should be ",
                     "342FSF342GHW", result);

        // test that path sessions are not processed.
        String pathSession =
                "http://wibble.woo/index.html;JSessionid=342FSF342GHW?param2=b";
        StringBuffer sbPathSession = new StringBuffer(pathSession);
        result = handler.removeSessionInfoFromParams(sbPathSession);
        assertNull("Session should not have been removed", result);
        assertEquals("url should not have been modified",
                     "http://wibble.woo/index.html;JSessionid=342FSF342GHW?param2=b",
                     sbPathSession.toString());

    }

    /**
     * Test the getDomain() method
     * @throws Exception
     */
    public void testGetDomain() throws Exception {
        String url = "http://localhost:8080/wibble.html";
        StringBuffer urlBuffer = new StringBuffer(url);
        String domain = JSessionIDHandler.getDomain(urlBuffer);
        assertEquals("Domain should match ", "localhost", domain);

        // try url without port number
        url = "http://localhost/wibble.html";
        urlBuffer = new StringBuffer(url);
        domain = JSessionIDHandler.getDomain(urlBuffer);
        assertEquals("Domain should match ", "localhost", domain);
    }

    /**
     * Test the getProxySession() method
     * @throws Exception if an error occurs
     */
    public void testGetProxySession() throws Exception {
        HttpServletRequest req = new TestHttpServletRequest();
        HTTPProxySessionInterface serv1Proxy =
                JSessionIDHandler.getProxySession(req, "serv1");
        HTTPProxySessionInterface serv1ProxyDup =
                JSessionIDHandler.getProxySession(req, "serv1");
        HTTPProxySessionInterface serv2Proxy =
                JSessionIDHandler.getProxySession(req, "serv2");
        assertNotNull("Proxy session for serv1 should not be null",
                serv1Proxy);
        assertNotNull("Proxy session for serv2 should not be null",
                serv2Proxy);
        assertTrue("Proxy sessions for serv1 should be identical",
                serv1Proxy == serv1ProxyDup);
        assertTrue("Proxy sessions for serv1 and serv2 should be different",
                serv1Proxy != serv2Proxy);
    }

    /**
     * Mock HttpServletRequest with support for session data.
     */
    private class TestHttpServletRequest extends HttpServletRequestStub {
        private Map sessionData = new HashMap();

        private final Map attributes = new HashMap();

        // javadoc inherited
        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }

        // javadoc inherited
        public Object getAttribute(String name) {
            return attributes.get(name);
        }

        // Javadoc inherited
        public HttpSession getSession() {
            return getSession(true);
        }

        // Javadoc inherited
        public HttpSession getSession(boolean b) {
            return new HttpSession() {
                // Javadoc inherited
                public long getCreationTime() {
                    return 0;
                }

                // Javadoc inherited
                public String getId() {
                    return null;
                }

                // Javadoc inherited
                public long getLastAccessedTime() {
                    return 0;
                }

                // Javadoc inherited
                public ServletContext getServletContext() {
                    return null;
                }

                // Javadoc inherited
                public void setMaxInactiveInterval(int i) {
                }

                // Javadoc inherited
                public int getMaxInactiveInterval() {
                    return 0;
                }

                // Javadoc inherited
                public HttpSessionContext getSessionContext() {
                    return null;
                }

                // Javadoc inherited
                public Object getAttribute(String s) {
                    return sessionData.get(s);
                }

                // Javadoc inherited
                public Object getValue(String s) {
                    return getAttribute(s);
                }

                // Javadoc inherited
                public Enumeration getAttributeNames() {
                    return Collections.enumeration(sessionData.keySet());
                }

                // Javadoc inherited
                public String[] getValueNames() {
                    return new String[0];
                }

                // Javadoc inherited
                public void setAttribute(String s, Object o) {
                    sessionData.put(s, o);
                }

                // Javadoc inherited
                public void putValue(String s, Object o) {
                    setAttribute(s, o);
                }

                // Javadoc inherited
                public void removeAttribute(String s) {
                    sessionData.remove(s);
                }

                // Javadoc inherited
                public void removeValue(String s) {
                    removeAttribute(s);
                }

                // Javadoc inherited
                public void invalidate() {
                }

                // Javadoc inherited
                public boolean isNew() {
                    return false;
                }
            };
        }
    }
}
