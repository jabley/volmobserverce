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
 * $Header: /src/voyager/com/volantis/mcs/servlet/FormFragmentationServlet.java,v 1.7 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Feb-02    Steve           VBM:2001101803 Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Steve           VBM:2001101803 - Now throws a ServletException
 *                              rather than trying to load a probably
 *                              non-existant error.jsp
 * 03-Jul-02    Chris W         VBM:2002061901 processRequest method writes the
 *                              parameters in the HttpServletRequest object to
 *                              the debug log.
 * 22-Nov-02    Paul            VBM:2002091806 - Replaced references to
 *                              ServletConfig with ServletContext.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.forms.SessionFormData;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ServletFilterConfiguration;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Servlet to intercept form fragmentation buttons, store the parameters in the
 * session context and then route the calling page back to the correct fragment
 * or action page.
 */
public class FormFragmentationServlet extends HttpServlet {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(FormFragmentationServlet.class);

    /**
     * The object that actually handles XDIME request processing. Will be
     * null when the servlet is operating in "pass-through mode".
     */
    private XDIMERequestProcessor xdimeRequestProcessor = null;

    // javadoc inherited
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        MarinerServletApplication application = MarinerServletApplication.
                getInstance(servletConfig.getServletContext());

        Volantis volantis = ApplicationInternals.getVolantisBean(application);

        // Get the servlet configuration from the mcs-config.xml file. This
        // is an optional configuration. If not present no special handling of
        // XDIME responses can be performed.
        ServletFilterConfiguration config =
                volantis.getServletFilterConfiguration();

        if (config != null) {
            xdimeRequestProcessor = new SimpleXDIMERequestProcessor(
                    servletConfig.getServletContext(), config.getMimeTypes());
        }
    }

    // javadoc inherited
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // javadoc inherited
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Perform processing for both GET and POST service requests.
     *
     * @param request the request to be processed
     * @param response the response to be populated
     */
    public void processRequest(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {
        // Log all HttpServletRequest parameters
        Enumeration paramEnum = request.getParameterNames();
        if (logger.isDebugEnabled()) {
            logger.debug("HttpServletRequest parameters are:");

            while (paramEnum.hasMoreElements()) {
                String paramName = (String) paramEnum.nextElement();
                logger.debug(paramName + ": " +
                             request.getParameter(paramName));
            }
        }

        // Get all the mariner stuff from the request
        ServletContext servletContext = getServletContext();
        MarinerRequestContext requestContext;
        Enumeration en;

        requestContext = MarinerServletRequestContext.getCurrent(request);

        if (requestContext == null) {
            try {
                requestContext = new MarinerServletRequestContext(
                        servletContext, request, response);
            } catch (Exception ex) {
                logger.error("servlet-context-creation-error");
                throw new ServletException("Unable to create new " +
                                           "MarinerServletRequestContext");
            }
        }

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(requestContext);

        // Get the name of the form from the vform parameter
        String formSpecifier = (String) request.getParameter("vform");

        if (formSpecifier == null) {
            logger.error("form-name-missing");
            requestContext.release();
            throw new ServletException("No vform parameter passed.");
        }

        // Determine which URL to go to from which submission
        // button was pressed to get here
        String targetURL = null;

        SessionFormData formData = pageContext.getFormDataManager().
                getSessionFormData(formSpecifier);

        if (request.getParameter(URLConstants.NEXT_FORM_FRAGMENT) != null) {
            targetURL = formData.getFieldValue(URLConstants.NEXT_FORM_FRAGMENT);
        } else if (request.getParameter(URLConstants.PREV_FORM_FRAGMENT) !=
                null) {
            targetURL = formData.getFieldValue(URLConstants.PREV_FORM_FRAGMENT);
        } else if (request.getParameter(URLConstants.RESET_FORM_FRAGMENT) !=
                null) {
            targetURL = formData.getFieldValue(URLConstants.RESET_FORM_FRAGMENT);
        } else {
            targetURL = formData.getFieldValue(URLConstants.ACTION_FORM_FRAGMENT);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Target URL is " + targetURL);
        }

        if (targetURL == null) {
            requestContext.release();
            throw new ServletException("No target URL for dispatch.");
        }

        // Strip the context path from the front of the target URL if it is
        // there
        targetURL = removeContextPath(targetURL, request);

        // Strip the session id from the URL if there is one
        targetURL = removeSession(targetURL);


        // Make sure the target URL starts with a / (required by dispatcher).
        if (!targetURL.startsWith("/")) {
            targetURL = "/" + targetURL;
        }

        // Add the passed form parameters into the session
        en = request.getParameterNames();

        while (en.hasMoreElements()) {
            String pName = (String) en.nextElement();
            if (!(pName.equals(URLConstants.FORM_PARAMETER)) &&
                    !(pName.equals(URLConstants.NEXT_FORM_FRAGMENT)) &&
                    !(pName.equals(URLConstants.PREV_FORM_FRAGMENT)) &&
                    !(pName.equals(URLConstants.ACTION_FORM_FRAGMENT)) &&
                    !(pName.equals(URLConstants.
                                   FORM_FRAGMENTATION_PARAMETER)) &&
                    !(pName.equals(URLConstants.RESET_FORM_FRAGMENT))) {
                String pValue = (String) request.getParameter(pName);

                if (logger.isDebugEnabled()) {
                    logger.debug("Writing attribute " + pName + "=" +
                                 pValue + " to the session context.");
                }

                formData.setFieldValue(pName, pValue);
            }
        }

        // Forward to the appropriate destination
        if (logger.isDebugEnabled()) {
            logger.debug("Dispatching to " + targetURL);
        }

        RequestDispatcher rd = servletContext.getRequestDispatcher(targetURL);
        HttpServletResponse forwardResponse = response;
        CachingResponseWrapper responseWrapper = null;

        if (xdimeRequestProcessor != null) {
            // The response from the forwarded request must be wrapped so that
            // we can perform additional processing if raw XDIME is returned
            responseWrapper = new CachingResponseWrapper(response);
            forwardResponse = responseWrapper;
        }

        // Wrap the request - the wrapper removes IF headers which were causing
        // the dispatcher to report that the resource hadn't changed. However
        // we still need to process it to produce a different result!
        MCSRequestWrapper requestWrapper =  new MCSRequestWrapper(request);
        // Dispatch the request to the "final destination"
        rd.forward(requestWrapper, forwardResponse);

        // Release the request context so that it is not detected as the
        // enclosing request context when processing the XDIME. If it was, it
        // would use the original requets URL
        requestContext.release();
        // See if we need to perform additional processing or not
        if (xdimeRequestProcessor != null) {
            if (xdimeRequestProcessor.isXDIME(
                    responseWrapper.getMimeTypeFromContentType())) {
                // We have found raw XDIME which must be converted into the
                // channelized response, probably because the container doesn't
                // apply filters when invoking RequestDispatcher#forward - this
                // because of an ambiguity in the 2.3 Servlet API spec
                processXDIME(servletContext,
                             request,
                             responseWrapper,
                             targetURL);
            } else {
                // We have found an already channelized response so we simply
                // ensure that the real response is populated from the wrapper
                responseWrapper.writeTo(response);
            }
        }

        requestContext.release();
    }

    /**
     * Processes the raw XDIME in the wrapped response through MCS in order to
     * generate the required channelized final response.
     *
     * @param servletContext  the context in which the servlet is operating
     * @param request         the original request that obtained the raw XDIME
     * @param responseWrapper the wrapped response containing the raw XDIME
     * @param targetURL       the URL that was forwarded to in order to obtain
     *                        the raw XDIME. This may include query patameters
     * @throws IOException      if there is a problem processing the response
     * @throws ServletException if there is a problem processing the response
     */
    private void processXDIME(final ServletContext servletContext,
                              final HttpServletRequest request,
                              final CachingResponseWrapper responseWrapper,
                              final String targetURL)
            throws IOException, ServletException {
        // We must get MCS to process the XDIME, but we need it to understand
        // the request is associated with an alternative URI (the URL that
        // was forwarded to in order to obtain the raw XDIME)

        // De-construct the request URI information in order to generate
        // a spoof request that indicates that it was for the target URL
        // rather than this servlet
        final String contextPath = request.getContextPath();
        final String pathInfo;
        final String queryString;
        final Hashtable parameters = new Hashtable(request.getParameterMap());
        int query = targetURL.indexOf('?');

        if (query >= 0) {
            pathInfo = targetURL.substring(0, query);
            queryString = targetURL.substring(query + 1);
            Hashtable extras = HttpUtils.parseQueryString(queryString);

            mergeParameters(parameters, extras);
        } else {
            pathInfo = targetURL;
            queryString = null;
        }

        // Create the spoof request that can be used to dupe MCS into thinking
        // that the request was for the target URL (which it would have been
        // had the container implemented filters on RequestDispatcher#forward
        // invocations)
        HttpServletRequestWrapper requestWrapper =
                new HttpServletRequestWrapper(request) {
                    // javadoc inherited
                    public String getRequestURI() {
                        // Includes the query string
                        return contextPath + targetURL;
                    }

                    // javadoc inherited
                    public StringBuffer getRequestURL() {
                        // Includes the query string
                        StringBuffer sb = request.getRequestURL();

                        // Removing the servlet's name from the string buffer,
                        // replacing it with the target URL (remembering that
                        // the targetURL is guaranteed to start with a slash
                        int i = sb.lastIndexOf("/");

                        sb.replace(i, sb.length(), targetURL);

                        return sb;
                    }

                    // javadoc inherited
                    public String getContextPath() {
                        return contextPath;
                    }

                    // javadoc inherited
                    public String getServletPath() {
                        // We pretend that the servlet was selected by the
                        // "/*" url-pattern (as described in the Servlet 2.4
                        // API documentation, as a clarification of the 2.3
                        // API specification)
                        return "";
                    }

                    // javadoc inherited
                    public String getPathInfo() {
                        // Excludes the query string
                        return pathInfo;
                    }

                    // javadoc inherited
                    public String getQueryString() {
                        return queryString;
                    }

                    // javadoc inherited
                    public String getParameter(String s) {
                        String[] values = getParameterValues(s);

                        return (values != null ? values[0] : null);
                    }

                    // javadoc inherited
                    public Map getParameterMap() {
                        return parameters;
                    }

                    // javadoc inherited
                    public Enumeration getParameterNames() {
                        return parameters.keys();
                    }

                    // javadoc inherited
                    public String[] getParameterValues(String s) {
                        return (String[]) parameters.get(s);
                    }
                };

        // Allow this spoof request and the wrapped response to be processed by
        // MCS to generate the channelized response
        xdimeRequestProcessor.processXDIME(servletContext, requestWrapper,
            responseWrapper.getResponse(), responseWrapper,
            responseWrapper.getCharacterEncoding(), true);
    }

    /**
     * Merges the extra parameters into the given parameters such that the
     * extra parameter values appear before the original parameters when both
     * parameter sets have the same named parameter in them. Otherwise the set
     * of named parameters is a simple union of the two sets.
     *
     * @param parameters the parameters into which the extra parameters are to
     *                   be merged
     * @param extras     the extra parameters to be merged in
     */
    private void mergeParameters(Map parameters, Map extras) {
        Iterator i = extras.keySet().iterator();

        // Go through all of the extra parameter keys
        while (i.hasNext()) {
            Object key = i.next();
            String[] extraValues = (String[]) extras.get(key);
            String[] values = (String[]) parameters.get(key);

            if (extraValues != null) {
                if (values == null) {
                    // This key doesn't exist in the target parameters so
                    // simply add it
                    parameters.put(key, extraValues);
                } else {
                    // This key exists in both sets so generate a merged value
                    // with the extra values first in the set of values
                    String[] mergedValues = new String[values.length +
                            extraValues.length];

                    System.arraycopy(extraValues, 0,
                                     mergedValues, 0,
                                     extraValues.length);
                    System.arraycopy(values, 0,
                                     mergedValues, extraValues.length,
                                     values.length);

                    parameters.put(key, mergedValues);
                }
            }
        }
    }

    /**
     * Removes the session information from the end of a URL.
     *
     * @param original The original URL
     * @return String holding the URL with any session parameters removed
     */
    private String removeSession(String original) {
        String noSession;

        // Strip the session id from the URL if there is one
        int sessionStart = original.indexOf(';');
        if (sessionStart > -1) {
            int sessionEnd = original.indexOf('?', sessionStart);
            if (sessionEnd == -1) {
                noSession = original.substring(0, sessionStart);
            } else {
                noSession = original.substring(0, sessionStart) +
                        original.substring(sessionEnd);
            }
        } else {
            noSession = original;
        }

        return noSession;
    }

    /**
     * Removes the context path from the start of a URL.
     *
     * @param original The original URL
     * @param request  The servlet request containing the context path
     * @return String containing the original URL with the context path removed
     *         from the front if it was present
     */
    private String removeContextPath(String original,
                                     HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String noContext;

        if (original.startsWith(contextPath)) {
            noContext = original.substring(contextPath.length());
        } else {
            noContext = original;
        }
        return noContext;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10727/6	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 09-Dec-05	10677/5	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 08-Dec-05	10677/3	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 20-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 13-May-05	8230/1	philws	VBM:2005051009 Port FormFragmentationServlet XDIME XML changes from 3.3 (removes XDIME page caching to be added back by Doug under 2005041916)

 13-May-05	8165/1	philws	VBM:2005051009 Ensure that the FormFragmentationServlet works with XDIME XML pages

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
