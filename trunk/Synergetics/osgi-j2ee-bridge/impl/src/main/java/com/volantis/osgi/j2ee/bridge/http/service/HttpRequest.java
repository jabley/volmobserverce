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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.ServletException;

/**
 * The request that is passed to a servlet registered with the HttpService.
 *
 * <p>This fixes up various paths and ensures that they adhere to the servlet
 * 2.4 specification.</p>
 */
public class HttpRequest
        extends HttpServletRequestWrapper {

    /**
     * The fixed context path.
     */
    private final String contextPath;

    /**
     * The fixed servlet path.
     */
    private final String servletPath;

    /**
     * The fixed path info.
     */
    private final String pathInfo;

    /**
     * Initialise.
     * @param request The underlying request.
     * @param registration The servlet registration.
     */
    public HttpRequest(HttpServletRequest request,
                       ServletRegistration registration)
            throws ServletException {

        super(request);

        String alias = registration.getAlias();

        String outerContextPath = request.getContextPath();
        Checker.checkContextPath(outerContextPath);
        contextPath = outerContextPath;

        // The servlet path is the outermost servlet path prepended to the
        // alias. Make sure that both servlet paths adhere to the
        // specification.
        String outerServletPath = request.getServletPath();
        Checker.checkServletPath(outerServletPath);

        servletPath = (outerServletPath + alias);
        Checker.checkServletPath(servletPath);

        String outerPathInfo = request.getPathInfo();
        Checker.checkPathInfo(outerPathInfo);

        if (!outerPathInfo.startsWith(alias)) {
            throw new IllegalStateException("Outer path info '" +
                    outerPathInfo + "' must start with '" + alias + "'");
        }

        pathInfo = outerPathInfo.substring(alias.length());
        Checker.checkPathInfo(pathInfo);
    }

    // Javadoc inherited.
    public String getPathInfo() {
        return pathInfo;
    }

    // Javadoc inherited.
    public String getPathTranslated() {
        return null;
    }

    // Javadoc inherited.
    public String getContextPath() {
        return contextPath;
    }

    // Javadoc inherited.
    public String getServletPath() {
        return servletPath;
    }
}
