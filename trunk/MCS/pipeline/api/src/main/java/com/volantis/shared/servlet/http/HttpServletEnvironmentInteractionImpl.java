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

package com.volantis.shared.servlet.http;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.volantis.shared.servlet.ServletEnvironment;
import com.volantis.shared.servlet.ServletEnvironmentInteractionImpl;

/**
 * HttpServletEnvironmentInteractionImpl.
 * Data store for the servlet environment interaction.
 *
 * @author steve
 *
 */
public class HttpServletEnvironmentInteractionImpl
        extends ServletEnvironmentInteractionImpl
        implements HttpServletEnvironmentInteraction {

    /** The request and response */
    private HttpServletRequest request;

    private HttpServletResponse response;

    /**
     * Create a ServletEnvironmentInteraction implementation
     */
    public HttpServletEnvironmentInteractionImpl(
            ServletEnvironment servletEnvironment,
            HttpServlet servlet,
            ServletConfig servletConfig,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        super(servletEnvironment, servlet, servletConfig, servletRequest,
              servletResponse);

        request = servletRequest;
        response = servletResponse;
    }

    /**
     * Get the HTTP servlet request
     * @return the servlet request
     */
    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    /**
     * Get the HTTP servlet response
     * @return the servlet response
     */
    public HttpServletResponse getHttpServletResponse() {
        return response;
    }

    /**
     * Get the current session from the request. If there is no session, one is
     * created.
     * @return the current session
     */
    public HttpSession getHttpSession() {
        return request.getSession(true);
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
