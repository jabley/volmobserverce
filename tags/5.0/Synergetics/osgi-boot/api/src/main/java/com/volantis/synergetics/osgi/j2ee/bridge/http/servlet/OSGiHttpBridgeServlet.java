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

package com.volantis.synergetics.osgi.j2ee.bridge.http.servlet;

import com.volantis.synergetics.osgi.OSGiManager;
import com.volantis.synergetics.osgi.j2ee.bridge.http.HttpBridge;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The servlet that dispatches requests across the {@link HttpBridge} into the
 * OSGi framework.
 */
public class OSGiHttpBridgeServlet
        implements Servlet {

    /**
     * The servlet config.
     */
    private ServletConfig servletConfig;

    /**
     * The manager, retrieved from the servlet context after being initialised
     * by the OSGiBootListener.
     */
    private OSGiManager manager;

    /**
     * Initialise the servlet.
     * @param servletConfig
     * @throws ServletException
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig = servletConfig;
        ServletContext servletContext = servletConfig.getServletContext();
        this.manager = (OSGiManager)
                servletContext.getAttribute(OSGiManager.class.getName());
    }

    // Javadoc inherited.
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    /**
     * Dispatch the request across the bridge.
     */
    public void service(
            ServletRequest genericRequest, ServletResponse genericResponse)
            throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) genericRequest;
        HttpServletResponse response = (HttpServletResponse) genericResponse;

        // Attempt to retrieve the service from OSGi that will dispath requests
        // to servlets registered with the HttpService.
        HttpBridge bridge = (HttpBridge)
                manager.getExportedBridgeService(HttpBridge.class);
        if (bridge == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Requested resource '" + request.getRequestURI() +
                    "'could not be found");
            return;
        }

        try {
            // Dispatch the request to the bridge.
            bridge.dispatch(request, response);
        } catch(ServletException e) {
            e.printStackTrace();
            throw e;
        } catch(IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Javadoc inherited.
    public String getServletInfo() {
        return "Servlet Bridge Between J2EE and OSGi Framework v1.0\n" +
                "(c) Volantis Systems Ltd";
    }

    // Javadoc inherited.
    public void destroy() {
    }
}
