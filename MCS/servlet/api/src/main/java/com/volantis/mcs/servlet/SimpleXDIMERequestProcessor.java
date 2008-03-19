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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.marlin.sax.MCSInternalContentHandler;
import com.volantis.mcs.service.ServiceDefinition;
import com.volantis.mcs.service.ServiceDefinitionHelper;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Provides common XDIME processing facilities required by various servlets
 * and servlet filters.
 */
public class SimpleXDIMERequestProcessor implements XDIMERequestProcessor {
    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SimpleXDIMERequestProcessor.class);

    /**
     * The exception localizer used by this class.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    SimpleXDIMERequestProcessor.class);

    /**
     * The list of MIME types denoting XDIME markup.
     */
    private final List xdimeMIMETypes;

    /**
     * delegate helper object - this object is being used to enable this to
     * class to be tested.
     */
    protected XDIMERequestProcessorHelper xdimeRequestProcessorHelper;

    /**
     * Initializes the new instance using the given xdimeMIMETypes. This
     * constructor will initialise a default xdime request processor which
     * well the be delegated to inorder to perform the processing
     *
     * @param servletContext
     * @param xdimeMIMETypes the MIME types that represent XDIME
     */
    public SimpleXDIMERequestProcessor(
            ServletContext servletContext, List xdimeMIMETypes) {
        this.xdimeMIMETypes = xdimeMIMETypes;
        xdimeRequestProcessorHelper =
                new DefaultXDIMERequestProcessorHelper(servletContext);
    }

    /**
     * Initializes the new instance using the given xdimeMIMETypes. This
     * constructor will use the xdime request processor provided
     * inorder to perform the processing - this method is intended to be used
     * for testing.
     *
     * @param xdimeMIMETypes the MIME types that represent XDIME
     * @param helper the delegate object which handles most of the processing
     */
    public SimpleXDIMERequestProcessor(List xdimeMIMETypes,
                                       XDIMERequestProcessorHelper helper) {
        this.xdimeMIMETypes = xdimeMIMETypes;
        xdimeRequestProcessorHelper = helper;
    }

    // javadoc inherited
    public boolean isXDIME(String mimeType) {
        return xdimeMIMETypes.contains(mimeType);
    }

    /**
     * Configures the environment context with any PMSS service information
     * that is needed
     * @param marinerRequestContext the MarnierRequestContext
     * @param environmentContext the Environment Context
     */
    private void configureServiceDefintion(MarinerServletRequestContext marinerRequestContext,
                                           EnvironmentContext environmentContext) {
        HttpServletRequest request = marinerRequestContext.getHttpRequest();
        ServiceDefinition service =
                ServiceDefinitionHelper.retrieveService(request);

        if (service != null) {
            // we need to store this away in the Expression context
            ExpressionContext expressionContext =
                    environmentContext.getExpressionContext();
            expressionContext.setProperty(ServiceDefinition.class,
                                          service,
                                          false);
        }
    }

    // javadoc inherited
    public void processXDIME(final ServletContext servletContext,
                             final ServletRequest request,
                             final ServletResponse response,
                             final CachedContent xdimeContent,
                             final String characterSet,
                             final boolean disableResponseCaching)

            throws IOException, ServletException {

        MarinerServletRequestContext marinerRequestContext = null;
        try {
            marinerRequestContext = xdimeRequestProcessorHelper.
                createServletRequestContext(servletContext, request, response);

            final EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(marinerRequestContext);

            // we need to store the service definintion away in the
            // expression context
            configureServiceDefintion(marinerRequestContext,
                                      environmentContext);
            final ResponseCachingDirectives cachingDirectives =
                environmentContext.getCachingDirectives();
            if (cachingDirectives != null && disableResponseCaching) {
                cachingDirectives.disable();
            }
            processXDIME(marinerRequestContext, xdimeContent, characterSet);
        } catch (MarinerContextException e) {
            logger.error("mariner-context-exception", e);
            throw new ServletException(
                    exceptionLocalizer.format("mariner-context-exception"),
                    e);
        } catch (SAXException se) {
            Exception cause = se.getException();

            logger.error("sax-exception-caught", se);

            // Check the root cause of the SAXException, as it may not be
            // logged correctly.
            if (cause != null) {
                logger.error("root-cause", cause);

                throw new ServletException(
                        exceptionLocalizer.format("root-cause"),
                        cause);
            }

            throw new ServletException(
                    exceptionLocalizer.format("sax-exception-caught"),
                    se);
        } catch (Throwable t) {
            // Catch and log anything else to avoid losing the information.
            logger.error("unexpected-exception", t);
            throw new ServletException(t);
        } finally {
            if (marinerRequestContext != null) {
                marinerRequestContext.release();
            }
        }
    }

    /**
     * Processes the response as XDIME.
     * @param marinerRequestContext the mariner request context     
     * @param xdimeContent used to read out the XDIME
     * @param characterSet the character encoding
     * @throws IOException if an error occurs
     * @throws ServletException if an error occurs
     * @throws SAXException if an error occurs
     */
    protected void processXDIME(
            final MarinerServletRequestContext marinerRequestContext,
            final CachedContent xdimeContent,
            final String characterSet)
         throws IOException, ServletException, SAXException {
            // Parse the contents of the response as Marlin markup.
            // The processed page will be written to the response, so
            // we don't need to do anything further.

        MCSInternalContentHandler contentHandler =
                xdimeRequestProcessorHelper.getContentHandler(
                marinerRequestContext);
        xdimeRequestProcessorHelper.
                    parseXDIME(marinerRequestContext,
                       xdimeContent,
                            contentHandler);
    }
}
