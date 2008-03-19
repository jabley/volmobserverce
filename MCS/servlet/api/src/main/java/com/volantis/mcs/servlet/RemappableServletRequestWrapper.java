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

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * ServletRequestWrapper which allows the request URL to appear to be
 * changed.
 */
public class RemappableServletRequestWrapper extends MCSRequestWrapper {

    private URL url;
    private String contextPath;
    private String servletPath;
    private String uri;
    private StringBuffer urlString;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param httpServletRequest
     */
    public RemappableServletRequestWrapper(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    public void resetRequestURL(URL remappedURL) {
        this.url = remappedURL;
        // Remove the webapp component.
        String path = url.getPath();
        char separator = '/';

        // Remove the webapp name.
        final int index = path.indexOf(separator);
        final int second = path.indexOf(separator, index + 1);
        if (index != -1 && second != -1) {
            contextPath = path.substring(0, second);
            // We only remap up to the context path, so anything beyond that
            // can still come from the original request.
            String pathInfo = super.getPathInfo();
            int pathInfoIndex = path.length();
            if (pathInfo != null) {
                pathInfoIndex = path.indexOf(pathInfo, second);
            }

            servletPath = path.substring(second, pathInfoIndex);
        }
        urlString = new StringBuffer(url.getAuthority());
        if (url.getAuthority().endsWith("/")) {
            urlString.deleteCharAt(urlString.length());
        }
        if (url.getPath().charAt(0) != separator) {
            urlString.append('/');
        }
        urlString.append(url.getPath());
        uri = urlString.toString();
        urlString.insert(0, "://");
        urlString.insert(0, url.getProtocol());
    }

    // Javadoc inherited.
    public String getPathInfo() {
        return super.getPathInfo();
    }

    // Javadoc inherited.
    public String getPathTranslated() {
        return super.getPathInfo();
    }

    // Javadoc inherited.
    public String getContextPath() {
        if (contextPath == null) {
            return super.getContextPath();
        }
        return contextPath;
    }

    // Javadoc inherited.
    public String getQueryString() {
        if (url == null) {
            return super.getQueryString();
        }
        return url.getQuery();
    }

    // Javadoc inherited.
    public String getServletPath() {
        if (servletPath == null) {
            return super.getServletPath();
        }
        return servletPath;
    }

    // Javadoc inherited.
    public String getScheme() {
        if (url == null) {
            return super.getScheme();
        }
        return url.getProtocol();

    }

    // Javadoc inherited.
    public String getServerName() {
        if (url == null) {
            return super.getServerName();
        }
        return url.getHost();
    }

    // Javadoc inherited.
    public int getServerPort() {
        if (url == null) {
            return super.getServerPort();
        }
        int port = url.getPort();
        if (port == -1) {
            port = 80;
        }
        return port;
    }

    // Javadoc inherited.
    public String getRequestURI() {
        if (uri == null) {
            return super.getRequestURI();
        }
        return uri;
    }

    // Javadoc inherited.
    public StringBuffer getRequestURL() {
        if (urlString == null) {
            return super.getRequestURL();
        }
        return urlString;
    }
}
