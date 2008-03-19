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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet.http;

import com.volantis.shared.throwable.ExtendedRuntimeException;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Wrapper around HttpServletRequest to ensure that it behaves according to the
 * standard.
 *
 * <p>Note, this does not implement HttpServletRequest because it would not
 * necessarily be safe to pass back to the servlet API as the implementation
 * may rely on any non standard behaviour.</p>
 */
public class HttpRequestStandardizer {

    private final HttpServletRequest request;
    private static final String URL_SEPARATOR = "/";

    public HttpRequestStandardizer(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Ensures that the returned value is either an empty string for the root
     * context, or starts with a / but does not end with a / for other contexts.
     *
     * @return The standard context path.
     */
    public String getContextPath() {
        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        } else if (!contextPath.equals("")) {
            contextPath = ensureSeparatorAtStartNotEnd(contextPath);
        }
        return contextPath;
    }

    /**
     * Get the request URI.
     *
     * @return The standard request URI.
     */
    public String getRequestURI() {
        return request.getRequestURI();
    }

    /**
     * Get the servlet relative path info.
     *
     * <p>The returned path is either null, or a non empty string that does
     * not start with a /.</p>
     *
     * @return The servlet relative path info.
     */
    public String getPathInfo() {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith(URL_SEPARATOR)) {
                pathInfo = pathInfo.substring(1);
            }

            if (pathInfo.length() == 0) {
                pathInfo = null;
            }
        }
        return pathInfo;
    }

    /**
     * Get the context relative path info.
     *
     * <p>The returned path contains server path. It may be empty if there
     * is no server path.</p>
     *
     * @return The context relative path.
     */
    public String getContextRelativePathInfo() {
        String pathInfo = getPathInfo();
        if (pathInfo == null) {
            return getServletPath();
        } else {
            return getServletPath() + URL_SEPARATOR + pathInfo;
        }
    }

    /**
     * Ensures that the returned value is either an empty string if there is
     * no servlet path, or starts with a / but does not end with a /.
     *
     * @return The normalized servlet path.
     */
    public String getServletPath() {
        String servletPath = request.getServletPath();
        if (servletPath == null) {
            servletPath = "";
        } else if (!servletPath.equals("")) {
            servletPath = ensureSeparatorAtStartNotEnd(servletPath);
        }
        return servletPath;
    }

    /**
     * Ensure that the path starts with a separator but does not end with one.
     *
     * @param path The path.
     * @return The normalized path.
     */
    private String ensureSeparatorAtStartNotEnd(String path) {
        if (!path.startsWith(URL_SEPARATOR)) {
            path = URL_SEPARATOR + path;
        }
        if (path.endsWith(URL_SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * Get the host URL.
     *
     * <p>Consists of the scheme, server name and port and ends with a /.</p>
     *
     * @return The host URL.
     */
    public URL getHostURL() {
        try {
            URL url = new URL(request.getScheme(),
                    request.getServerName(),
                    request.getServerPort(), "/");
            return url;
        } catch (MalformedURLException e) {
            throw new ExtendedRuntimeException("Could not get host URI", e);
        }
    }
}
