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

import com.volantis.map.common.encryption.Encrypter;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.OperationEngine;
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
            if (path.getNumberOfFragments()>0) {
                String externalID = path.getFragment(path.getNumberOfFragments()-1);
                externalID = externalID.replaceAll("-", "/");
                externalID = Encrypter.defaultDecrypt(externalID);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Servicing request " + pathInfo);
                }

                try {
                    opEngine.processRequest(externalID, request, response);

                } catch (ResourceDescriptorNotFoundException rdnfe) {
                    sendNotFound(request, response);
                } catch (Throwable t) {
                    sendError(request, response, t);
                }
            } else {
                sendError(request, response, "invalid request url " + pathInfo);
            }
        } else {
            sendError(request, response, "invalid request url " + pathInfo);
        }
    }

    private void sendNotFound(
        HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        LOGGER.info("resource-definition-not-found", request.getRequestURL());
        response.sendError(404);
    }

    private void sendError(
        HttpServletRequest request, HttpServletResponse response, String message)
        throws IOException {
        LOGGER.info("invalid-request", request.getRequestURL());
        response.sendError(500);
    }

    private void sendError(HttpServletRequest request,
                           HttpServletResponse response,
                           Throwable t) throws IOException {
        LOGGER.error(t);
        sendError(request, response, t.getMessage());
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
