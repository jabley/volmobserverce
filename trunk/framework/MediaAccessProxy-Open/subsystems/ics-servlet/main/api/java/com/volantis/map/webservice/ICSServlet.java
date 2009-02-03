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

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterBuilderException;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.ImageConstants;
import com.volantis.map.ics.imageprocessor.parameters.ICSParamBuilder;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.operation.Operation;
import com.volantis.map.operation.OperationNotFoundException;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.ResourceDescriptorFactory;
import com.volantis.map.operation.Result;
import com.volantis.map.retriever.ResourceRetrieverException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
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
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ICSServlet.class);

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


        ResourceDescriptor descriptor =
            ResourceDescriptorFactory.getInstance()
            .createDescriptor("ICS", "image");


        try {
            // build up the parameters based on the ICS request
            ICSParamBuilder builder = new ICSParamBuilder();
            StringBuffer uri = new StringBuffer();
            uri.append(getStandardisedPathInfo(request));
            String queryString = request.getQueryString();

            if (queryString != null && !"".equals(queryString)) {
                uri.append('?').append(queryString);
            }

            builder.build(new URI(uri.toString()),
                          descriptor.getInputParameters());
            
            // todo: delegate to the Operation Engine here, which does exactly the same
            ServiceReference[] references =
                context.getBundleContext().getServiceReferences(
                    Operation.class.getName(), "(operationType=ics)");
            if (null == references || references.length < 1) {
                throw new OperationNotFoundException(
                    "operation-type-not-found",
                    descriptor.getResourceType());
            } else {
                Result result = Result.UNSUPPORTED;
                for (int i=0; i< references.length &&
                    result == Result.UNSUPPORTED; i++) {

                    ServiceReference ref = references[i];
                    Object service =
                        context.getBundleContext().getService(ref);
                    if (null == service) {
                        Object[] params = new Object[] {
                            ref.getProperty("service.pid"),
                            ref.getBundle().getSymbolicName()
                        };
                        LOGGER.error("service-has-been-unregistered", params);
                    } else {
                        Operation operation =
                            (Operation) service;
                        result  = operation.execute(
                            descriptor, request, response);
                    }
                }
                if (result == Result.UNSUPPORTED) {
                    throw new OperationNotFoundException(
                        "no-plugin-available",
                            new String[] { descriptor.getExternalID(), descriptor.getResourceType() });
                }
            }
        } catch (OperationNotFoundException x) {
            sendError(response, descriptor, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, x);
        } catch (ParameterBuilderException x) {
            sendError(response, descriptor, HttpServletResponse.SC_BAD_REQUEST, x);
        } catch (ResourceRetrieverException x) {
            sendError(response, descriptor, HttpServletResponse.SC_BAD_GATEWAY, x);
        } catch (Throwable t) {
            sendError(response, descriptor, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t);
        }
    }

     /**
      * Prepare properly encoded PathInfo string
      */
     private static String getStandardisedPathInfo(HttpServletRequest request) {
        String servletContext = request.getContextPath()
            + request.getServletPath();
        String requestPath = request.getRequestURI();
        int index = requestPath.indexOf("?");
        if (index != -1) {
            requestPath = requestPath.substring(0, index);
        }
        // remove the servletContext from the begining of the request URI.
        requestPath = requestPath.substring(servletContext.length());

        return requestPath;
    }

    private void sendError(HttpServletResponse response,
                           ResourceDescriptor descriptor,
                           int code, Throwable x)
            throws IOException {

        String param;
        ObjectParameters params = descriptor.getInputParameters();
        try {
            param = params.getParameterValue(ParameterNames.SOURCE_URL);
        } catch (MissingParameterException e) {
            /* OK, will use external id */
            param = descriptor.getExternalID();
        }

        if (code < 500 ) {
             // Code less then 500 means we handled the problem gracefully
             LOGGER.info("processing-error", param, x);
         } else {
             // This in most cases means the server error
             LOGGER.error("processing-error", param, x);
         }
        response.sendError(code, x.getMessage());
    }
}
