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

import javax.servlet.ServletException;

public class Checker {
    /**
     * Make sure that the path info is either empty, or starts with a /.
     *
     * @param pathInfo The path info to check.
     */
    public static void checkPathInfo(String pathInfo)
            throws ServletException {

        if (pathInfo.equals("")) {
            // Root servlet is ok.
        } else if (!pathInfo.startsWith("/")) {
            // Unless it is for the root servlet the servlet path must start
            // with a '/'.
            throw new ServletException("Non empty path info '" + pathInfo +
                    "' must start with a /");
        }
    }

    /**
     * Make sure that the servlet path is either empty, or starts with a / but
     * does not end with a /.
     *
     * @param servletPath The servlet path to check.
     */
    public static void checkServletPath(String servletPath)
            throws ServletException {

        if (servletPath.equals("")) {
            // Root servlet is ok.
        } else if (!servletPath.startsWith("/")) {
            // Unless it is for the root servlet the servlet path must start
            // with a '/'.
            throw new ServletException("Non empty servlet path '" +
                    servletPath + "' must start with a /");
        } else if (servletPath.endsWith("/")) {
            // The servlet path must not end with a '/'.
            throw new ServletException("Servlet path '" + servletPath +
                    "' must not end with a /");
        }
    }

    /**
     * Make sure that the context path is either empty, or starts with a / but
     * does not end with a /.
     *
     * @param contextPath The context path to check.
     */
    public static void checkContextPath(String contextPath)
            throws ServletException {

        if (contextPath.equals("")) {
            // Root context is ok.
        } else if (!contextPath.startsWith("/")) {
            // Unless it is for the root context the context path must start
            // with a '/'.
            throw new ServletException("Non empty context path '" +
                    contextPath + "' must start with a /");
        } else if (contextPath.endsWith("/")) {
            // The context path must not end with a '/'.
            throw new ServletException("Context path '" + contextPath +
                    "' must not end with a /");
        }
    }

    /**
     * Make sure that the alias is either '/', or starts with a '/' but does
     * not end with one.
     *
     * @param alias The alias.
     */
    public static void checkAlias(String alias) {
        // Make sure that the alias is valid.
        if (alias == null) {
            throw new IllegalArgumentException("alias cannot be null");
        } else if (!alias.startsWith("/")) {
            throw new IllegalArgumentException(
                    "'" + alias + "' does not start with '/'");
        } else if (alias.endsWith("/") && !alias.equals("/")) {
            throw new IllegalArgumentException(
                    "'" + alias + "' ends with '/' but is not '/'");
        }
    }
}
