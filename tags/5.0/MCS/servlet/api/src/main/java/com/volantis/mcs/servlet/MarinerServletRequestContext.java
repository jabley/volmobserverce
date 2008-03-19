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
 * $Header: /src/voyager/com/volantis/mcs/servlet/MarinerServletRequestContext.java,v 1.22 2003/04/28 15:27:11 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Dec-01    Paul            VBM:2001121702 - Created.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - Create a RequestHeaders object
 *                              and pass it into the initialisePage method
 *                              instead of the request.
 * 24-Jan-02    Doug            VBM:2002011406 - Modified the initialise method
 *                              so the requestURI is calculate using
 *                              the HttpServletRequest methods getContextPath()
 *                              getServletPath() and getPathInfo(). This is
 *                              because the bluestone getRequestURI() does
 *                              return the correct URI
 * 31-Jan-02    Paul            VBM:2001122105 - Made sure that the correct
 *                              EnvironmentContext was created.
 * 21-Feb-02    Allan           VBM:2002022007 - Modified uri StringBuffer
 *                              in initialise() to provide an initial capacity
 *                              of 40 - to reduce StringBuffer expansion.
 * 25-Feb-02    Paul            VBM:2002022204 - Moved the code which
 *                              retrieves the writer for the response into the
 *                              ServletEnvironmentContext as it should only be
 *                              retrieved if it is actually going to be used.
 * 08-Mar-02    Paul            VBM:2002030607 - Wrapped any internal
 *                              exceptions in a MarinerContextException.
 * 15-Mar-02    Adrian          VBM:2002031501 - Updated url generation in
 *                              initialise to cope with empty path data to
 *                              prevent malformed urls.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 08-Apr-02    Allan           VBM:2002040401 - Ensured that uri.length() > 0
 *                              in intialise() before references charAt(length
 *                              -1.
 * 09-Apr-02    Ian             VBM:2002032002 - Created createNestedRequest() 
 *                              method to remove servlet dependencies in MAML.
 * 17-Oct-02    Ian             VBM:2002091806 - Updated initialise to create
 *                              an ApplicationContext prior to initialising 
 *                              the MarinerPageContext. Moved session 
 *                              initialisation to happen here instead of in
 *                              MarinerPageContext.
 * 11-Nov-02    Mat             VBM:2002103010 - Changed the application context
 *                              code to support ApplicationRegistryContainer.
 * 15-Nov-02    Mat             VBM:2002103010 - Changed to call checkLicense
 *                              on the ApplicationContextFactory.
 * 11-Nov-02    Mat             VBM:2002103010 - Changed the application
 *                              context code to support
 *                              ApplicationRegistryContainer.
 * 13-Nov-02    Ian             VBM:2002103010 - Disabled license as we are 
 *                              getting a class cast for the
 *                              ApplicationContext.
 * 13-Nov-02    Paul            VBM:2002091806 - Changed throws declaration to
 *                              throw MarinerContextException instead of
 *                              RepositoryException.
 * 19-Nov-02    Mat             VBM:2002103010 - Renable the licensing.
 * 22-Nov-02    Paul            VBM:2002091806 - Replaced references to
 *                              ServletConfig with ServletContext where
 *                              possible and deprecated other uses of it.
 * 26-Nov-02    Paul            VBM:2002091806 - Undid some of the work above
 *                              in order to maintain backwards compatability.
 * 29-Jan-03    Ian             VBM:2003011609 - Changed allocation of 
 *                              ApplicationContext to only vreate a new instance
 *                              if there is no enclosing request context.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 13-Feb-03    Byron           VBM:2003021204 - Modified initialise to use the
 *                              new request headers class.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Mar-03    Steve           VBM:2003022403 - Removed non existant javadoc Link
 * 25-Apr-03    Mat             VBM:2003033108 - Added code to initialise()
 *                              to interpret the accept-charset header.
 * 13-May-03    Mat             VBM:2003033108 - Amended the code that 
 *                              parses the accept-charset to deal with 
 *                              headers that don't end with a ';'
 * 13-May-03    Chris W         VBM:2003041105 - isResolveCharacterReferences()
 *                              and isWriteDirect() added to support public api
 *                              methods for jsp character references.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.application.ApplicationContextFactory;
import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.ApplicationRegistry;
import com.volantis.mcs.application.ApplicationRegistryContainer;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.ExpressionSupport;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.servlet.HttpServletFactory;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RequestHeaders;
import com.volantis.mcs.runtime.ServletRequestHeaders;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * This class contains all the servlet specific information which needs to be
 * used while processing the request.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class MarinerServletRequestContext
        extends MarinerRequestContext {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    MarinerServletRequestContext.class);

    /**
     * The key to use in the ServletRequest to store and retrieve the
     * current MarinerPageContext object.
     */
    private static final String REQUEST_ATTRIBUTE_NAME
            = MarinerRequestContext.class.getName();

    /**
     * The volantis bean.
     */
    private Volantis volantisBean;

    /**
     * The servlet config.
     */
    private ServletConfig servletConfig;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * The request.
     */
    private HttpServletRequest request;

    /**
     * The response.
     */
    private HttpServletResponse response;


    /**
     * Create a new <code>MarinerServletRequestContext</code>.
     * <p/>
     * This constructor has been deprecated as the ServletConfig should not
     * be passed into the request. The invoking servlet should use the
     * information in the ServletConfig object to initialise itself. If it needs
     * to pass extra information through to included servlets then it should use
     * the ServletRequest.setAttribute() method.
     * </p>
     *
     * @param config   The <code>ServletConfig</code>
     * @param request  The <code>ServletRequest</code>.
     * @param response The <code>ServletResponse</code>.
     * @throws IOException If there was a problem clearing the
     *                     output.
     * @throws MarinerContextException If there was a problem initialising the
     *                                 object.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @deprecated Use {@link #MarinerServletRequestContext (ServletContext,
     *             ServletRequest, ServletResponse response)} instead
     */
    public MarinerServletRequestContext(ServletConfig config,
                                        ServletRequest request,
                                        ServletResponse response)
            throws IOException,
            MarinerContextException {

        EnvironmentContext environmentContext
                = new ServletEnvironmentContext(this);

        initialise(config, config.getServletContext(),
                (HttpServletRequest) request, (HttpServletResponse) response,
                environmentContext);
    }

    /**
     * Create a new <code>MarinerServletRequestContext</code>.
     * <p/>
     * Using this constructor will cause the {@link #getServletConfig} method
     * to return null.
     * </p>
     *
     * @param context  The <code>ServletContext</code>
     * @param request  The <code>ServletRequest</code>.
     * @param response The <code>ServletResponse</code>.
     * @throws IOException             If there was a problem clearing the
     * output.
     * @throws MarinerContextException If there was a problem initialising the
     *                                 object.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public MarinerServletRequestContext(ServletContext context,
                                        ServletRequest request,
                                        ServletResponse response)
            throws IOException,
            MarinerContextException {

        this(context, request, response, null);
    }

    /**
     * Create a new <code>MarinerServletRequestContext</code>.
     * <p/>
     * Using this constructor will cause the {@link #getServletConfig} method
     * to return null.
     * </p>
     *
     * @param context    The <code>ServletContext</code>
     * @param request    The <code>ServletRequest</code>.
     * @param response   The <code>ServletResponse</code>.
     * @param envContext The <code>EnvironmentContext</code>. If not null then
     *                   an <code>ExpressionContext</code> is copied from the
     *                   environment context into the new
     *                   <code>MarinerServletRequestContext</code>. This allows
     *                   any sub-requests to access the environment context so
     *                   that expression contexts and variables can be shared
     *                   across all sub-requests.
     * @throws IOException  If there was a problem clearing the
     *                      output.
     * @throws MarinerContextException If there was a problem initialising the
     *                                 object.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public MarinerServletRequestContext(ServletContext context,
                                        ServletRequest request,
                                        ServletResponse response,
                                        EnvironmentContext envContext)
            throws IOException,
            MarinerContextException {

        EnvironmentContext newContext = new ServletEnvironmentContext(this);
        if (envContext != null) {
            newContext.setExpressionContext(envContext.getExpressionContext());
        }

        initialise(null, context, (HttpServletRequest) request,
                (HttpServletResponse) response, newContext);
    }

    /**
     * Create a new <code>MarinerServletRequestContext</code>.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    protected MarinerServletRequestContext() {
    }

    /**
     * Initialise this <code>MarinerServletRequestContext</code>.
     * <h2>
     * You MUST NOT change the protection level on this method.
     * </h2>
     *
     * @param servletConfig      The <code>ServletConfig</code>, this may be
     *                           null and should not be used internally at all.
     *                           It is kept here simply to maintain backwards
     *                           compatability at the API level and will be
     *                           removed in a future release.
     * @param servletContext     The <code>ServletContext</code>
     * @param request            The <code>ServletRequest</code>.
     * @param response           The <code>ServletResponse</code>.
     * @param environmentContext The <code>EnvironmentContext</code>.
     * @throws IOException       If there was a problem clearing the output.
     * @throws MarinerContextException If there was a problem initializing
     *                                 the context.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    void initialise(ServletConfig servletConfig,
                    ServletContext servletContext,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    EnvironmentContext environmentContext)
            throws IOException,
            MarinerContextException {

        boolean initialised = false;
        try {
            this.servletConfig = servletConfig;
            this.servletContext = servletContext;
            this.request = request;
            this.response = response;

            // Get the MarinerApplication, this may return null.
            //
            // This does not create and initialize the MarinerServletApplication if
            // it does not exist. This is purely for backwards compatability. In
            // future we can change this so we no longer need to use jsp:useBean.
            // Before we do that however we need to make sure that it will not have
            // an adverse effect on anything.
            //
            // Calling the following method still causes two extra synchronization
            // points which may affect performance. If jsp:useBean was not used then
            // this would simply use the same as before.
            //
            MarinerServletApplication application
                    = MarinerServletApplication.getInstance(servletContext,
                            false);

            // store a reference to the application
            ContextInternals.setMarinerApplication(this, application);

            if (application == null) {
                throw new IllegalStateException
                        ("Mariner application has not been initialised");
            }

            // Get the Volantis bean, this never returns null as long as the
            // MarinerServletApplication has been correctly initialised which will
            // be checked above.
            volantisBean = ApplicationInternals.getVolantisBean(application);

            // Create a RequestHeaders object and populate it with the requests
            // headers.
            RequestHeaders requestHeaders = new ServletRequestHeaders(
                    HttpServletFactory.getDefaultInstance().getHTTPHeaders(
                            request));

            // Get the application name for this request.
            String applicationName = requestHeaders.getHeader(
                    "Mariner-Application");

            // Get the application Registry to enable application specific
            // initialisation.
            ApplicationRegistry applicationRegistry =
                    ApplicationRegistry.getSingleton();

            // Get the factory method to enable the application context
            // to be initialised.
            ApplicationRegistryContainer applicationRegistryContainer =
                    applicationRegistry.getApplicationRegistryContainer(
                            applicationName);

            // We did not match our appication so we are either mariner
            // or an unknown application, either way we will use the
            // default application behaviour.
            if (applicationRegistryContainer == null) {
                applicationRegistryContainer =
                        applicationRegistry.getApplicationRegistryContainer(
                                ApplicationRegistry.DEFAULT_APPLICATION_NAME);

                // Still cannot find anything, give in.
                if (applicationRegistryContainer == null) {
                    throw new IllegalStateException(
                            "No application registered");
                }
            }

            MarinerURL requestURL = createRequestURL(request);
            requestURL = volantisBean.getClientAccessibleURL(requestURL);

            ApplicationContext appContext;
            ApplicationContextFactory acf =
                    applicationRegistryContainer.getServletApplicationContextFactory();


            // Save away a reference to the enclosing MarinerRequestContext
            // if any.
            enclosingRequestContext = getCurrent(request);
            if (enclosingRequestContext != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Saving reference to "
                            + enclosingRequestContext
                            + " from " + request);
                }
            }

            // Get a MarinerPageContext object.
            final MarinerPageContext context;

            if (enclosingRequestContext != null) {
                context =
                        ContextInternals.getMarinerPageContext(
                                enclosingRequestContext);
            } else {
                context = volantisBean.createMarinerPageContext();
            }

            // Create and initialise the EnvironmentContext, do this after the
            // MarinerPageContext has been allocated but before it has been
            // initialised as this is needed during the initialisation.
            environmentContext.initialise(context);
            environmentContext.initialiseSession();

            ExpressionContext exprContext =
                    environmentContext.getExpressionContext();

            // If there is an expression context then it must have been associated
            // with an enclosing request context since we are still initialising
            // this instance of MarinerServletRequestContext. We now must associate
            // the expression context with this MarinerServletRequestContext.
            //
            // An expression context can be associated with more than one request
            // context, although not concurrently. When the request context is no
            // longer needed, the expression context (if any) should be reset to
            // the enclosing request context.
            if (exprContext == null) {
                ExpressionSupport.createExpressionEnv(this,
                        volantisBean, environmentContext,
                        context.getDevicePolicyAccessor());
            } else {
                exprContext.setProperty(MarinerRequestContext.class, this,
                        false);
            }

            ContextInternals.setEnvironmentContext(this, environmentContext);

            if (enclosingRequestContext != null) {
                // Copy our application context, as nothing has changed from an
                // application point of view.
                appContext =
                        ContextInternals.getApplicationContext(
                                enclosingRequestContext);
                if (logger.isDebugEnabled()) {
                    logger.debug("Retrieved applicationContext " + appContext);
                }
            } else {
                // We are the top level request so initialise an application context
                // for this application
                appContext = acf.createApplicationContext(this);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Created new applicationContext " + appContext);
                }
            }

            // Store the application context away as it is required to
            // initialise the page context.
            ContextInternals.setApplicationContext(this, appContext);

            // Save the reference away immediately so if a problem occurs during the
            // initialisation it is cleaned up properly.
            ContextInternals.setMarinerPageContext(this, context);

            // Initialise the page context.
            if (enclosingRequestContext == null) {
                context.initialisePage(volantisBean, this,
                        enclosingRequestContext,
                        requestURL, requestHeaders);
            } else {
                // Make this the current request context
                context.pushRequestContext(this);
            }


            // Select the best charset that we could find from the accept headers.
            EncodingManager encodingManager = appContext.getEncodingManager();
            AcceptCharsetSelector selector = new AcceptCharsetSelector(
                    encodingManager);
            HttpHeaders rHeaders = HttpServletFactory.
                    getDefaultInstance().getHTTPHeaders(request);
            String charset = selector.selectCharset(rHeaders,
                (DefaultDevice) context.getDevice().getDevice());
            if (charset != null) {
                // Use the selected charset.
                // This should always succeed since we already validated it.
                setCharacterEncoding(charset);
            } else {
                // For now, allow the user the change to set it themselves before
                // we fail.
                // todo: generate a 406 if we ever try and use a null Encoding
                // rather than a 500, as per the spec.
                logger.warn("charset-must-be-set-externally");
            }

            // Store a reference to this object in the request so that it can be
            // found by included pages.
            if (logger.isDebugEnabled()) {
                logger.debug("XXX Storing reference to " + this
                        + " in " + request);
            }
            request.setAttribute(REQUEST_ATTRIBUTE_NAME, this);



            // This must be the last thing which is done in this catch block.
            initialised = true;
        } catch (RepositoryException e) {
            throw new MarinerContextException(e);
        } catch (ServletException e) {
            throw new MarinerContextException(e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Error e) {
            throw e;
        } finally {
            if (!initialised) {
                // The only way we could have reached here is if there was an
                // exception in the body of the catch which caused the initialised
                // flag to not be set.
                logger.error("exception-in-msrc-init");

                // Clean up anything we can.
                release();
            }
        }
    }

    /**
     * Create a MarinerURL representation of the URL to which the request was
     * made, ensuring that the components have the correct separator characters
     * with no duplicates.
     *
     * @param request   HttpServletRequest that was received
     * @return MarinerURL representation of the request URL
     */
    protected MarinerURL createRequestURL(HttpServletRequest request) {

        final String protocolSeparator = "://";
        final String authoritySeparator = ":";

        // Retrieve the URL components from the servlet request
        String protocol = request.getScheme();
        String host = request.getServerName();
        String port = null;
        if (!(request.getServerPort() == -1)) {
            port = Integer.toString(request.getServerPort());
        }
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();

        // Work out the maximum length that the URL could be.
        int uriLength = (protocol == null) ? 0 :
                protocol.length() + protocolSeparator.length();
        uriLength += (host == null) ? 0 : host.length() +
                ((port == null) ? 0 :authoritySeparator.length() + port.length());
        uriLength += (contextPath == null) ? 0 : contextPath.length();
        uriLength += (servletPath == null) ? 0 : servletPath.length();
        uriLength += (pathInfo == null) ? 0 : pathInfo.length();
        // 3 /'s could be added.
        uriLength += 3;


        StringBuffer uri = new StringBuffer(uriLength);
        boolean uriHasEndSlash = false;

        if (protocol != null) {
            uri.append(protocol).append(protocolSeparator);
            if (host != null) {
                uri.append(host);
                if (port != null) {
                    uri.append(authoritySeparator).append(port);
                }
            }
        }

        if (contextPath != null && !contextPath.equals("")) {
            if (!contextPath.startsWith("/")) {
                uri.append("/");
            }
            uri.append(contextPath);
        }

        if (servletPath != null && !servletPath.equals("")) {
            uriHasEndSlash = uri.length() == 0 ? false :
                    uri.charAt(uri.length() - 1) == '/';
            if (!uriHasEndSlash && !servletPath.startsWith("/")) {
                uri.append("/");
            }
            if (uriHasEndSlash && servletPath.startsWith("/")) {
                servletPath = servletPath.substring(1);
            }
            uri.append(servletPath);
        }

        if (pathInfo != null && !pathInfo.equals("")) {
            uriHasEndSlash = uri.length() == 0 ? false :
                    uri.charAt(uri.length() - 1) == '/';
            if (!uriHasEndSlash && !pathInfo.startsWith("/")) {
                uri.append("/");
            }
            if (uriHasEndSlash && pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            uri.append(pathInfo);
        }

        // Create a MarinerURL which contains the URL which could have been
        // used by the client to generate this request.

        return new MarinerURL(uri.toString(), getParameterMap(request));
    }

    /**
     * Get the Map which contains the parameters passed on this request.
     *
     * @param request The request which is currently being processed.
     * @return The Map of parameters, the keys are the names of the parameters
     *         and the values are String arrays.
     * @volantis-api-exclude-from PublicAPI
     */
    protected Map getParameterMap(ServletRequest request) {

        Map parameters = new HashMap();

        for (Enumeration e = request.getParameterNames();
             e.hasMoreElements();) {
            String name = (String) e.nextElement();

            String[] values = request.getParameterValues(name);

            parameters.put(name, values);
        }

        return parameters;
    }

    /**
     * Get the <code>HttpServletRequest</code>.
     *
     * @return The <code>HttpServletRequest</code>.
     * @volantis-api-exclude-from PublicAPI
     */
    public HttpServletRequest getHttpRequest() {
        return request;
    }

    /**
     * Get the <code>HttpServletResponse</code>.
     *
     * @return The <code>HttpServletResponse</code>.
     * @volantis-api-exclude-from PublicAPI
     */
    public HttpServletResponse getHttpResponse() {
        return response;
    }

    /**
     * Get the <code>ServletRequest</code>.
     *
     * @return The <code>ServletRequest</code>.
     * @volantis-api-exclude-from PublicAPI
     */
    public ServletRequest getRequest() {
        return request;
    }

    /**
     * Get the <code>ServletResponse</code>.
     *
     * @return The <code>ServletResponse</code>.
     * @volantis-api-exclude-from PublicAPI
     */
    public ServletResponse getResponse() {
        return response;
    }

    /**
     * Get the <code>ServletConfig</code>.
     *
     * @return The <code>ServletConfig</code>, this may be null.
     * @volantis-api-exclude-from PublicAPI
     * @deprecated The ServletConfig should not be passed into the request.
     */
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    /**
     * Get the <code>ServletContext</code>.
     *
     * @return The <code>ServletContext</code>.
     * @volantis-api-exclude-from PublicAPI
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Get the current MarinerServletRequestContext.
     *
     * @param request The <code>ServletRequest</code> which is being processed.
     * @return The current MarinerServletRequestContext, or null if a
     *         MarinerServletRequestContext has not yet been created, or found for
     *         the current request.
     * @deprecated Use {@link #findInstance(javax.servlet.ServletRequest)}.
     */
    public static
    MarinerRequestContext getCurrent(ServletRequest request) {

        // Look and see if the current request has had a
        // MarinerServletRequestContext created for it.
        MarinerRequestContext context = (MarinerRequestContext)
                request.getAttribute(REQUEST_ATTRIBUTE_NAME);

        if (logger.isDebugEnabled()) {
            logger.debug("Retrieved reference to " + context
                    + " from " + request);
        }

        return context;
    }

    /**
     * Find the current MarinerServletRequestContext instance.
     *
     * @param request The <code>ServletRequest</code> which is being processed.
     * @return The current MarinerServletRequestContext, or null if a
     *         MarinerServletRequestContext has not yet been created, or found for
     *         the current request.
     */
    public static
    MarinerServletRequestContext findInstance(ServletRequest request) {

        // Look and see if the current request has had a
        // MarinerServletRequestContext created for it.
        MarinerServletRequestContext context = (MarinerServletRequestContext)
                request.getAttribute(REQUEST_ATTRIBUTE_NAME);

        if (logger.isDebugEnabled()) {
            logger.debug("Retrieved reference to " + context
                    + " from " + request);
        }

        return context;
    }

    /**
     * Javadoc inherited from super class.
     *
     * @volantis-api-exclude-from PublicAPI
     */
    public MarinerRequestContext createNestedContext()
            throws IOException,
            RepositoryException,
            MarinerContextException {

        // Retrieve the environment context so that it may be shared with the
        // new nested context.
        EnvironmentContext envContext =
                ContextInternals.getEnvironmentContext(this);
        return new MarinerServletRequestContext(servletContext,
                request, response,
                envContext);
    }

    // javadoc inherited
    public void release() {

        // Clear the setting in the request.
        if (request != null) {
            if (enclosingRequestContext == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Clearing reference to " + this
                            + " from " + request);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Restoring reference to "
                            + enclosingRequestContext
                            + " in " + request);
                }
            }

            // If there is an expression context associated with this request
            // context then it must be reset so that it is re-associated with the
            // enclosing request context from which it originally came.
            ExpressionContext exprContext =
                    MCSExpressionHelper.getExpressionContext(this);
            if (exprContext != null) {
                exprContext.
                        setProperty(MarinerRequestContext.class,
                                enclosingRequestContext,
                                false);
            }

            request.setAttribute(REQUEST_ATTRIBUTE_NAME,
                    enclosingRequestContext);
        }

        enclosingRequestContext = null;
        request = null;
        response = null;
        servletContext = null;
        volantisBean = null;

        // Release any resources managed by the base class after we have released
        // all the resources managed by this class.
        super.release();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 29-Jun-05	8913/1	rgreenall	VBM:2005062802 Merge from 331: NullPointerException when rendering pages on WML devices.

 29-Jun-05	8903/1	rgreenall	VBM:2005062802 NullPointerException when rendering pages on WML devices.

 17-May-05	8284/2	allan	VBM:2005042707 Move all smart server code into smartserver subsystem

 11-Apr-05	7376/2	allan	VBM:2005031101 SmartClient bundler - commit for testing

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 07-Jan-05	6609/1	matthew	VBM:2005010404 Remove MarinerPageContext pooling from Volantis bean

 23-Dec-04	6518/3	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 07-Oct-04	5239/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 07-Oct-04	5237/8	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 02-Aug-04	5017/1	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 23-Jul-04	4937/3	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 19-Feb-04	3090/4	ianw	VBM:2004021716 Merged

 19-Feb-04	3090/1	ianw	VBM:2004021716 Changed project stack to check enclosing request context first

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 03-Feb-04	2838/1	claire	VBM:2004011914 Handling current and different projects

 19-Dec-03	2246/3	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements (add export jars)

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 29-Jul-03	898/1	geoff	VBM:2003072906 port argh from metis

 29-Jul-03	893/1	geoff	VBM:2003072906 argh

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/8	geoff	VBM:2003071405 use fallbacks more often and allow user to set it themselves if we can't

 23-Jul-03	807/6	geoff	VBM:2003071405 works and tested but no design review yet

 25-Jun-03	544/1	geoff	VBM:2003061007 Allow JSPs to create binary output

 17-Jun-03	432/1	philws	VBM:2003061705 Fix charset extraction from accept-charset HTTP header

 ===========================================================================
*/
