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

import com.volantis.shared.environment.EnvironmentInteraction;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Encapsulates a servlet request and response.
 *
 * <strong>This interface is a facade provided for use by user code and as such
 * must not be implemented by user code.</strong>
 */
public interface ServletEnvironmentInteraction
        extends EnvironmentInteraction {

    /**
     * Return the servlet that this is interaction refers to
     * @return the servlet
     */
    public Servlet getServlet();

    /**
     * Return the configuration object for the servlet
     * @return the servlet configuration
     */
    public ServletConfig getServletConfig();

    /**
     * Return the request sent to the servlet
     * @return the servlet request
     */
    public ServletRequest getServletRequest();

    /**
     * Return the response sent by the servlet
     * @return the servlet response
     */
    public ServletResponse getServletResponse();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 15-Jul-03	197/1	steve	VBM:2003071501 Cookie and environment implementation

 11-Jul-03	181/1	steve	VBM:2003070802 Environment implementation

 ===========================================================================
*/
