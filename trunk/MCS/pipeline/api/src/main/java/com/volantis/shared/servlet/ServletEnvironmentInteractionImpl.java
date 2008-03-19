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

package com.volantis.shared.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.volantis.shared.environment.AbstractEnvironmentInteraction;
import com.volantis.shared.environment.Environment;
import com.volantis.shared.environment.EnvironmentInteraction;

/**
 * ServletEnvironmentInteractionImpl.
 * Data store for the servlet environment interaction.
 *
 * @author steve
 *
 */
public class ServletEnvironmentInteractionImpl
        extends AbstractEnvironmentInteraction
        implements ServletEnvironmentInteraction {

    private ServletEnvironment environment;

    private Servlet servlet;

    private ServletConfig config;

    private ServletRequest request;

    private ServletResponse response;

    private EnvironmentInteraction parent;

    /**
     * Create a ServletEnvironmentInteraction implementation
     */
    public ServletEnvironmentInteractionImpl(
            ServletEnvironment servletEnvironment, Servlet servlet,
            ServletConfig servletConfig, ServletRequest servletRequest,
            ServletResponse servletResponse) {
        super();

        environment = servletEnvironment;
        this.servlet = servlet;
        config = servletConfig;
        request = servletRequest;
        response = servletResponse;
    }

    /**
     * Return the servlet configuration set in the constructor
     * @return the servlet configuration
     */
    public ServletConfig getServletConfig() {
        return config;
    }

    /**
     * Return the environment set in the constructor
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Return the servlet request set in the constructor
     * @return the servlet request
     */
    public ServletRequest getServletRequest() {
        return request;
    }

    /**
     * Return the servlet response set in the constructor
     * @return the servlet response
     */
    public ServletResponse getServletResponse() {
        return response;
    }

    /**
     * Return the servlet
     * @return the servlet
     */
    public Servlet getServlet() {
        return servlet;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 11-Jul-03	181/3	steve	VBM:2003070802 rework fixes

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/
