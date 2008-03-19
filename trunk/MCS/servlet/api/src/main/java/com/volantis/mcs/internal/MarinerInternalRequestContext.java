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
 * $Header: /src/voyager/com/volantis/mcs/internal/MarinerInternalRequestContext.java,v 1.4 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 13-Feb-03    Byron           VBM:2003021204 - Modified initialise to use the
 *                              new request headers class.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.internal;

import com.volantis.mcs.application.ApplicationContextFactory;
import com.volantis.mcs.application.ApplicationRegistry;
import com.volantis.mcs.application.ApplicationRegistryContainer;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerSessionContext;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.expression.ExpressionSupport;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.InternalRequestHeaders;
import com.volantis.mcs.runtime.RequestHeaders;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.servlet.AcceptCharsetSelector;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.HttpFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.charset.EncodingManager;

import java.io.IOException;


/**
 * @author ianw
 */
public class MarinerInternalRequestContext extends MarinerRequestContext {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MarinerInternalRequestContext.class);

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


    private InternalRequest request;
    private InternalResponse response;
    private InternalConfig config;


    /**
     * Creates a new instance of InternalRequestContext
     */
    protected MarinerInternalRequestContext() {
    }

    /**
     * Creates a new <code>MarinerInternalRequestContext</code>.
     *
     * @param config   The <code>InternalConfig</code>.
     * @param request  The <code>InternalRequest</code>.
     * @param response The <code>InternalResponse</code>.
     * @param context  The <code>EnvironmentContext</code>. If not null then an
     *                 <code>ExpressionContext</code> is copied from the environment context
     *                 into the new <code>MarinerInternalRequestContext</code>. This allows
     *                 any sub-requests to access the environment context so that expression
     *                 contexts and variables can be shared across all sub-requests.
     * @throws MarinerContextException if there was a problem constructing the
     *                                 new instance.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public MarinerInternalRequestContext(InternalConfig config,
                                         InternalRequest request,
                                         InternalResponse response,
                                         EnvironmentContext context)
            throws IOException, MarinerContextException {

        MarinerSessionContext sessionContext = null;
        ExpressionContext expressionContext = null;
        if (context != null) {
            sessionContext = context.getCurrentSessionContext();
            expressionContext = context.getExpressionContext();
        }
        try {
            EnvironmentContext environmentContext
                    = new InternalEnvironmentContext(this, sessionContext);
            environmentContext.setExpressionContext(expressionContext);

            initialise(config, request, response, environmentContext);
        } catch (RepositoryException e) {
            throw new MarinerContextException(e);
        }
    }

    /**
     * Initialise this <code>MarinerServletRequestContext</code>.
     * <h2>
     * You MUST NOT change the protection level on this method.
     * </h2>
     *
     * @param config             The <code>InternalConfig</code>
     * @param request            The <code>ServletRequest</code>.
     * @param response           The <code>ServletResponse</code>.
     * @param environmentContext The <code>EnvironmentContext</code>.
     * @throws IOException         If there was a problem clearing the output.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    void initialise(InternalConfig config,
                    InternalRequest request,
                    InternalResponse response,
                    EnvironmentContext environmentContext)
            throws IOException,
            RepositoryException {

        boolean initialised = false;
        try {
            // Save those parameters needed for the future.
            this.config = config;
            this.request = request;
            this.response = response;

            // Get the Volantis bean.
            volantisBean = Volantis.getInstance();
            if (volantisBean == null) {
                throw new IllegalStateException
                        ("Volantis bean has not been initialised");
            }

            RequestHeaders requestHeaders = new InternalRequestHeaders();

            // Get the application name for this request.
            String applicationName = request.getApplicationName();

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
                                applicationRegistry.DEFAULT_APPLICATION_NAME);

                // Still cannot find anything, give in.
                if (applicationRegistryContainer == null) {
                    throw new IllegalStateException(
                            "No application registered");
                }
            }

            MarinerURL requestURL =
                    volantisBean.getClientAccessibleURL(
                            new MarinerURL("internalRequest"));

            ApplicationContext appContext;
            ApplicationContextFactory acf =
                    applicationRegistryContainer
                    .getInternalApplicationContextFactory();


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
            HttpHeaders rHeaders = HttpFactory.getDefaultInstance()
                    .createHTTPHeaders();
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
            logger.error("unexpected-exception", e);
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

    // Javadoc inherited from super class.
    public MarinerRequestContext createNestedContext()
            throws IOException,
            RepositoryException,
            MarinerContextException {
        return new MarinerInternalRequestContext(config, request,
                response, ContextInternals.getEnvironmentContext(this));
    }

    /**
     * Get the internal request object.
     *
     * @return The InternalRequest.
     */
    public InternalRequest getRequest() {
        return request;
    }


    /**
     * Get the internal response object.
     *
     * @return The InternalResponse.
     */
    public InternalResponse getResponse() {
        return response;
    }

    /**
     * Get the current MarinerServletRequestContext.
     *
     * @param request The <code>ServletRequest</code> which is being processed.
     * @return The current MarinerServletRequestContext, or null if a
     *         MarinerServletRequestContext has not yet been created, or found for
     *         the current request.
     */
    public static MarinerRequestContext getCurrent(InternalRequest request) {
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

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 02-Mar-05	7199/1	emma	VBM:2005022804 mergevbm from MCS V3.3

 01-Mar-05	7161/1	emma	VBM:2005022804 Added exception logging to help track initialisation problems

 07-Jan-05	6609/1	matthew	VBM:2005010404 Remove MarinerPageContext pooling from Volantis bean

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 07-Oct-04	5239/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 07-Oct-04	5237/8	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 19-Feb-04	3090/6	ianw	VBM:2004021716 Merged

 19-Feb-04	3090/3	ianw	VBM:2004021716 Changed project stack to check enclosing request context first

 19-Feb-04	3090/1	ianw	VBM:2004021716 Changed project stack to check enclosing request context first

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 03-Feb-04	2838/1	claire	VBM:2004011914 Handling current and different projects

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 28-Jul-03	755/7	adrian	VBM:2003022801 Added mysteriously missing import statement for MarinerSessionContext

 28-Jul-03	755/5	adrian	VBM:2003022801 fixed merge problems with iapi implementation

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
