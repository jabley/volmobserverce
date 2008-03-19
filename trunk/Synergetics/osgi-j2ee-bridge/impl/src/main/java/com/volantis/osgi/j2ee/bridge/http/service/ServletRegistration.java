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

import org.osgi.framework.Bundle;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A servlet registration.
 */
public class ServletRegistration
        extends Registration {

    /**
     * The servlet that was registered.
     */
    private final Servlet servlet;

    /**
     * The configuration for the servlet.
     */
    private final ServletConfig servletConfig;

    /**
     * Initialise.
     *
     * @param bundle         The bundle.
     * @param servletContext The servlet context.
     * @param alias          The alias.
     * @param servlet        The servlet that is registered.
     * @param servletConfig  The servlet configuration.
     */
    public ServletRegistration(
            Bundle bundle,
            InternalServletContext servletContext,
            String alias,
            Servlet servlet,
            ServletConfig servletConfig) {
        super(bundle, servletContext, alias);

        this.servlet = servlet;
        this.servletConfig = servletConfig;
    }

    /**
     * Initialise the servlet.
     *
     * @throws ServletException If there was a problem initialising the
     *                          servlet.
     */
    public void initialise()
            throws ServletException {

        servlet.init(servletConfig);

        // Update the usage count for the servlet context.
        servletContext.incrementUseCount();

        initialised = true;
    }

    // Javadoc inherited.
    protected boolean doRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpServletRequest req = new HttpRequest(request, this);

        servlet.service(req, response);

        return true;
    }

    /**
     * Shutdown the servlet.
     */
    protected void doShutDown() {
        try {
            servlet.destroy();
        } finally {
            servletContext.decrementUseCount();
        }
    }
}
