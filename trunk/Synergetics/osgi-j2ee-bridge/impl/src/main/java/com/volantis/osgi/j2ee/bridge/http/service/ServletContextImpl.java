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

package com.volantis.osgi.j2ee.bridge.http.service;

import org.osgi.service.http.HttpContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * The servlet context that wraps a {@link HttpContext} and is given to any
 * registered servlets.
 */
public class ServletContextImpl
        implements InternalServletContext {

    /**
     * The servlet context from the outermost container.
     */
    private final ServletContext containerContext;

    /**
     * The context supplied when registered.
     */
    private final HttpContext httpContext;

    /**
     * The set of attributes associated with this servlet context.
     */
    private final Hashtable attributes;

    /**
     * The count of the number of usages.
     */
    private int useCount;

    /**
     * Initialise.
     *
     * @param containerContext The servlet context from the outermost container.
     * @param httpContext      The context supplied when registered.
     */
    public ServletContextImpl(
            ServletContext containerContext,
            HttpContext httpContext) {
        this.containerContext = containerContext;
        this.httpContext = httpContext;
        attributes = new Hashtable();
    }

    // Javadoc inherited.
    public ServletContext getContext(String name) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public int getMajorVersion() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public int getMinorVersion() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public String getMimeType(String name) {
        String mimeType = httpContext.getMimeType(name);
        if (mimeType == null) {
            mimeType = containerContext.getMimeType(name);
        }
        return mimeType;
    }

    // Javadoc inherited.
    public Set getResourcePaths(String name) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public URL getResource(String name) throws MalformedURLException {
        return httpContext.getResource(name);
    }

    // Javadoc inherited.
    public InputStream getResourceAsStream(String name) {
        InputStream is = null;
        try {
            URL url = getResource(name);
            if (url != null) {
                is = url.openStream();
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return is;
    }

    // Javadoc inherited.
    public RequestDispatcher getRequestDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public Enumeration getServlets() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public Enumeration getServletNames() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public void log(String message) {
    }

    // Javadoc inherited.
    public void log(Exception exception, String message) {
    }

    // Javadoc inherited.
    public void log(String message, Throwable throwable) {
    }

    // Javadoc inherited.
    public String getRealPath(String name) {
        return null;
    }

    // Javadoc inherited.
    public String getServerInfo() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public String getInitParameter(String name) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public Enumeration getInitParameterNames() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    // Javadoc inherited.
    public Enumeration getAttributeNames() {
        return attributes.keys();
    }

    // Javadoc inherited.
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    // Javadoc inherited.
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    // Javadoc inherited.
    public String getServletContextName() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public synchronized void incrementUseCount() {
        useCount += 1;
    }

    // Javadoc inherited.
    public synchronized boolean decrementUseCount() {
        useCount -= 1;
        return useCount == 0;
    }

    // Javadoc inherited.
    public HttpContext getHttpContext() {
        return httpContext;
    }
}
