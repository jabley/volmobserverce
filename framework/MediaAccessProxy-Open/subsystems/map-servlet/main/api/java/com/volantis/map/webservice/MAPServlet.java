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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.webservice;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.OperationEngine;
import com.volantis.map.operation.OperationNotFoundException;
import com.volantis.map.operation.ResourceDescriptorNotFoundException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.path.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of base REST webservice for characteristic access.
 */
public class MAPServlet
    extends HttpServlet {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(MAPServlet.class);

    /**
     * The operation engine to use.
     */
    private OperationEngine opEngine;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (null != pathInfo) {
            Path path = Path.parse(pathInfo);
            if (path.getNumberOfFragments() > 0) {
                String externalID = path.getFragment(path.getNumberOfFragments()-1);
                externalID = externalID.replaceAll("-", "/");
               
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Servicing request " + pathInfo);
                }

                try {
                    opEngine.processRequest(externalID, request, response);

                } catch (ResourceDescriptorNotFoundException x) {
                    sendError(response, externalID, HttpServletResponse.SC_NOT_FOUND, x);
                } catch (OperationNotFoundException x) {
                    sendError(response, externalID, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, x);
                } catch (Throwable t) {
                    sendError(response, externalID, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t);
                }
            } else {
                sendBadRequest(request, response, pathInfo);
            }
        } else {
            sendBadRequest(request, response, "");
        }
    }

    private void sendError(HttpServletResponse response, String param, int code, Throwable x)
        throws IOException {

        if (code < 500 ) {
            // Code less then 500 means we handled the problem gracefully
            LOGGER.info("processing-error", param, x);
        } else {
            // This in most cases means the server error
            LOGGER.error("processing-error", param, x);
        }
        response.sendError(code, x.getMessage());
    }

    private void sendBadRequest(
            HttpServletRequest request, HttpServletResponse response, String param)
        throws IOException {

        LOGGER.info("invalid-request", param);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Set the operation engine this servlet will use
     *
     * @param opEngine the operation engine to use
     */
    public void setOperationEngine(OperationEngine opEngine) {
        this.opEngine = opEngine;
    }
}
