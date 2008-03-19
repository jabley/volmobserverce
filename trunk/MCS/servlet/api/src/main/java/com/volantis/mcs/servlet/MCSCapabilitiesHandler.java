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
package com.volantis.mcs.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Helper class for handling requests that are asking for MCS capabilities
 * information.
 */
public class MCSCapabilitiesHandler {
    /**
     * The header containing queries for MCS.
     */
    private static final String MCS_QUERY_HEADER = "x-mcs-query";

    /**
     * The MCS query value indicating a request for MCS capabilities.
     */
    private static final String MCS_QUERY_CAPABILITIES = "capabilities";

    /**
     * The MCS capabilities response header.
     */
    private static final String MCS_CAPABILITIES_HEADER = "x-mcs-capabilities";

    /**
     * The capabilities of this version of MCS.
     */
    private static final String MCS_CAPABILITIES = "MCS, xdime, xdime cp";

    /**
     * The request associated with this handler.
     */
    private HttpServletRequest request;

    /**
     * The response associated with this handler.
     */
    private HttpServletResponse response;

    /**
     * Construct an MCSCapabilitiesHandler for a given request and response.
     *
     * @param request The request for this handler
     * @param response The response for this handler
     */
    public MCSCapabilitiesHandler(HttpServletRequest request,
                                  HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * Checks the request associated with this handler to see whether it
     * contains a request for MCS capability information.
     *
     * @return True if the request is an MCS capabilities request
     */
    public boolean isMCSCapabilitiesRequest() {
        boolean capabilitiesRequest = false;
        if (request != null) {
            Enumeration mcsQueries = request.getHeaders(MCS_QUERY_HEADER);
            while (mcsQueries != null && !capabilitiesRequest &&
                    mcsQueries.hasMoreElements()) {
                if (MCS_QUERY_CAPABILITIES.equals(mcsQueries.nextElement())) {
                    capabilitiesRequest = true;
                }
            }
        }
        return capabilitiesRequest;
    }

    /**
     * Send information to the response associated with this handler that
     * identifies the capabilities of this MCS installation.
     */
    public void processMCSCapabilitiesRequest() {
        response.reset();
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setHeader(MCS_CAPABILITIES_HEADER, MCS_CAPABILITIES);
        response.setContentLength(0);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9616/3	adrianj	VBM:2005092817 Fix for JSP problems with x-mcs-query

 28-Sep-05	9616/1	adrianj	VBM:2005092610 Allow querying of MCS capabilities

 ===========================================================================
*/
