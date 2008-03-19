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

import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.parameters.ICSParamBuilder;
import com.volantis.map.ics.imageprocessor.parameters.ParameterBuilderException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.Operation;
import com.volantis.map.operation.OperationNotFoundException;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.ResourceDescriptorFactory;
import com.volantis.map.operation.Result;
import com.volantis.synergetics.log.LogDispatcher;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class ICSServlet extends HttpServlet {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ICSServlet.class);

    /**
     * The OSGI component context
     */
    private ComponentContext context;


    /**
     * Valid file extensions for the SVG file format
     */
    List SVG_EXTENSIONS = Arrays.asList(new String[]{
        ImageConstants.SVG_EXTENSION.toLowerCase(),
        ImageConstants.SVG_EXTENSION.toUpperCase()});

    /**
     * Initialise the servlet.
     *
     * @throws javax.servlet.ServletException
     */
    public void init() throws ServletException {
        super.init();
    }

    /**
     * Process a GET request from a client
     *
     * @param request  request from client
     * @param response response to client
     * @throws ServletException if an error occurs processing the request
     * @throws IOException      if an error occurs when reading/writing images
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Process a POST request from a client
     *
     * @param request  request from client
     * @param response response to client
     * @throws ServletException if an error occurs processing the request
     * @throws IOException      if an error occurs when reading/writing images
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Called by ODGi framework to set the component context
     *
     * @param context the context to use
     */
    public void setComponentContext(ComponentContext context) {
        this.context = context;
    }


    /**
     * Read a request and respond with an image.
     *
     * @param request  request from client
     * @param response response to client
     * @throws ServletException if an error occurs processing the request
     * @throws IOException      if an error occurs when reading/writing images
     */
    public void processRequest(HttpServletRequest request,
                               HttpServletResponse response)
        throws ServletException, IOException {

        try{

            ResourceDescriptor descriptor =
                ResourceDescriptorFactory.getInstance()
                .createDescriptor("ICS", "image");


            // build up the parameters based on the ICS request
            ICSParamBuilder builder = new ICSParamBuilder();
            StringBuffer uri = new StringBuffer();
            uri.append(request.getPathInfo());
            String queryString = request.getQueryString();
            if (queryString != null && !"".equals(queryString)) {
                uri.append('?').append(queryString);
            }
            builder.build(new URI(uri.toString()),
                          descriptor.getInputParameters());

            ServiceReference[] references =
                context.getBundleContext().getServiceReferences(
                    Operation.class.getName(), null);
             if (null == references || references.length < 1) {
                throw new OperationNotFoundException(
                    "operation-type-not-found",
                    descriptor.getResourceType());
            } else {
                Result result = Result.UNSUPPORTED;
                for (int i = 0; i < references.length &&
                    result == Result.UNSUPPORTED; i++) {
                    try {
                        Object service =
                            context.getBundleContext().getService(references[i]);
                        if (null == service) {
                            LOGGER.error("service-has-been-unregistered", references[i]);
                        } else {
                            Operation operation =
                                (Operation) service;
                            result = operation.execute(descriptor, request, response);
                        }

                    } finally {
                        context.getBundleContext().ungetService(references[i]);
                    }
                }
            }
        } catch (InvalidSyntaxException e) {
            LOGGER.error("processing-error", e);
            throw new RuntimeException(e);
        } catch (OperationNotFoundException e) {
            LOGGER.error("processing-error", e);
            throw new RuntimeException(e);
        } catch (ParameterBuilderException e) {
            LOGGER.error("processing-error", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            LOGGER.error("processing-error", e);
            throw new RuntimeException(e);
        } 

    }

   

    private void sendError(HttpServletRequest request,
                           HttpServletResponse response)
        throws IOException {
        LOGGER.info("invalid-request", request.getRequestURL());
        response.sendError(500);
    }
}
