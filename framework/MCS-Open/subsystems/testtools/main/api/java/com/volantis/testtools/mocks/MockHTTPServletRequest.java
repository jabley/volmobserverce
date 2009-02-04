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
package com.volantis.testtools.mocks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.security.Principal;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;

/**
 * Supporting class for the expression unit tests*
 */
public class MockHTTPServletRequest implements HttpServletRequest {
    private Map headers = new HashMap();

    private Map parameters = new HashMap();

    public String getAuthType() {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public void setCharacterEncoding(String s)
            throws UnsupportedEncodingException {
    }

    public Map getParameterMap() {
        return null;
    }

    /**
     * Adds the given value to the set of values for the given header.
     *
     * @param headerName name of the header
     * @param headerValue value to add to the header
     */
    public void addHeader(final String headerName, final String headerValue) {
        addEntity(headers, headerName, headerValue);
    }

    /**
     * Adds the given value to the set of values for the given header.
     *
     * @param s name of the header
     * @param v value to add to the header
     */
    public void addParameter(String s, String v) {
        addEntity(parameters, s, v);
    }

    /**
     * Add a name/value pair to a map making a list for the values if no
     * mapping already exists to allow multiple values for each name.
     * @param map The map
     * @param name
     * @param value
     */
    protected void addEntity(Map map, String name, String value) {
        List values = (List) map.get(name);
        if (values == null) {
            values = new ArrayList();

            map.put(name, values);
        }

        values.add(value);
    }


    public long getDateHeader(String s) {
        return 0;
    }

    public String getHeader(String s) {
        return getEntity(headers, s);
    }

    public Enumeration getHeaderNames() {
        return new Enumeration() {
            Iterator i = headers.keySet().iterator();

            public boolean hasMoreElements() {
                return i.hasNext();
            }

            public Object nextElement() {
                return i.next();
            }
        };
    }

    public Enumeration getHeaders(final String s) {
        if (headers.get(s) == null) {
            return null;
        } else {
            return new Enumeration() {
                Iterator i = ((List) headers.get(s)).iterator();

                public boolean hasMoreElements() {
                    return i.hasNext();
                }

                public Object nextElement() {
                    return i.next();
                }
            };
        }
    }

    public int getIntHeader(String s) {
        return 0;
    }

    public String getMethod() {
        return null;
    }

    //javadoc inherited
    public String getParameter(String s) {
        return getEntity(parameters, s);

    }

    /**
     * Given a Map of Lists and a key. Retrieve the list keyed on the key and
     * and return the first value in the List.
     * @param map The Map
     * @param key The key
     * @return The first value in the keyed list.
     */
    protected String getEntity(Map map, String key) {
        List values = (List) map.get(key);

        if (values == null) {
            return null;
        } else {
            return (String) values.get(0);
        }
    }

    public Enumeration getParameterNames() {
        return null;
    }

    // javadoc inherited
    public String[] getParameterValues(String s) {
        String values [] = null;

        List list = (List) parameters.get(s);

        if (list != null) {
            String container [] = new String[list.size()];
            values = (String[]) list.toArray(container);
        }

        return values;
    }

    public String getPathInfo() {
        return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getQueryString() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public String getRequestURI() {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getServletPath() {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public HttpSession getSession(boolean flag) {
        return null;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isUserInRole(String s) {
        return false;
    }

    public Object getAttribute(String s) {
        return null;
    }

    public Enumeration getAttributeNames() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public ServletInputStream getInputStream()
            throws IOException {
        return null;
    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration getLocales() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public BufferedReader getReader()
            throws IOException {
        return null;
    }

    public String getRealPath(String s) {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    public String getScheme() {
        return null;
    }

    public String getServerName() {
        return null;
    }

    public int getServerPort() {
        return 0;
    }

    public boolean isSecure() {
        return false;
    }

    public void removeAttribute(String s) {
    }

    public void setAttribute(String s, Object obj) {
    }

    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    public String getLocalName() {
        throw new UnsupportedOperationException();
    }

    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    public int getLocalPort() {
        throw new UnsupportedOperationException();
    }
}
