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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.parameters.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class MockHttpServletRequest implements HttpServletRequest {
    /* 
     * the follow methods are added for 2.4 servlet comatibilty
     */
    public int getLocalPort() {
     return 0;
    }
    public String getLocalAddr() {
     return null;
    }
    public String getLocalName() {
     return null;
    }
    public int getRemotePort(){
     return 0;
    }
    // The headers map so that headers can be stored/retrieved
    HashMap headers = new HashMap();

    // JavaDoc inherited
    public String getHeader(String s) {
        return (String) headers.get(s);
    }

    // JavaDoc inherited
    public Enumeration getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    // JavaDoc inherited
    public void setHeaderField(String header, String value) {
        headers.put(header, value);
    }

    // JavaDoc inherited
    public Enumeration getHeaders(String s) {
        return Collections.enumeration(headers.values());
    }

    // ************************************************************
    // All methods below this point have default implementations
    // ************************************************************

    // JavaDoc inherited
    public String getAuthType() {
        return null;
    }

    // JavaDoc inherited
    public String getContextPath() {
        return null;
    }

    // JavaDoc inherited
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    // JavaDoc inherited
    public long getDateHeader(String s) {
        return 0;
    }

    // JavaDoc inherited
    public int getIntHeader(String s) {
        return 0;
    }

    // JavaDoc inherited
    public String getMethod() {
        return null;
    }

    // JavaDoc inherited
    public String getPathInfo() {
        return null;
    }

    // JavaDoc inherited
    public String getPathTranslated() {
        return null;
    }

    // JavaDoc inherited
    public String getQueryString() {
        return null;
    }

    // JavaDoc inherited
    public String getRemoteUser() {
        return null;
    }

    // JavaDoc inherited
    public String getRequestedSessionId() {
        return null;
    }

    // JavaDoc inherited
    public String getRequestURI() {
        return null;
    }

    // JavaDoc inherited
    public StringBuffer getRequestURL() {
        return null;
    }

    // JavaDoc inherited
    public String getServletPath() {
        return null;
    }

    // JavaDoc inherited
    public HttpSession getSession() {
        return null;
    }

    // JavaDoc inherited
    public HttpSession getSession(boolean b) {
        return null;
    }

    // JavaDoc inherited
    public Principal getUserPrincipal() {
        return null;
    }

    // JavaDoc inherited
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    // JavaDoc inherited
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    // JavaDoc inherited
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    // JavaDoc inherited
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    // JavaDoc inherited
    public boolean isUserInRole(String s) {
        return false;
    }

    // JavaDoc inherited
    public Object getAttribute(String s) {
        return null;
    }

    // JavaDoc inherited
    public Enumeration getAttributeNames() {
        return null;
    }

    // JavaDoc inherited
    public String getCharacterEncoding() {
        return null;
    }

    // JavaDoc inherited
    public int getContentLength() {
        return 0;
    }

    // JavaDoc inherited
    public String getContentType() {
        return null;
    }

    // JavaDoc inherited
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    // JavaDoc inherited
    public Locale getLocale() {
        return null;
    }

    // JavaDoc inherited
    public Enumeration getLocales() {
        return null;
    }

    // JavaDoc inherited
    public String getParameter(String s) {
        return null;
    }

    // JavaDoc inherited
    public Map getParameterMap() {
        return null;
    }

    // JavaDoc inherited
    public Enumeration getParameterNames() {
        return null;
    }

    // JavaDoc inherited
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    // JavaDoc inherited
    public String getProtocol() {
        return null;
    }

    // JavaDoc inherited
    public BufferedReader getReader() throws IOException {
        return null;
    }

    // JavaDoc inherited
    public String getRealPath(String s) {
        return null;
    }

    // JavaDoc inherited
    public String getRemoteAddr() {
        return null;
    }

    // JavaDoc inherited
    public String getRemoteHost() {
        return null;
    }

    // JavaDoc inherited
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    // JavaDoc inherited
    public String getScheme() {
        return null;
    }

    // JavaDoc inherited
    public String getServerName() {
        return null;
    }

    // JavaDoc inherited
    public int getServerPort() {
        return 0;
    }

    // JavaDoc inherited
    public boolean isSecure() {
        return false;
    }

    // JavaDoc inherited
    public void removeAttribute(String s) {
    }

    // JavaDoc inherited
    public void setAttribute(String s, Object o) {
    }

    // JavaDoc inherited
    public void setCharacterEncoding(String s)
        throws UnsupportedEncodingException {
    }
}


