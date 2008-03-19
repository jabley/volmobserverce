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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/MarlinSAXHelper.java,v 1.3 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Paul            VBM:2002112214 - Created to provide some helper
 *                              methods to make it easy to process SAX 2 events
 *                              into PAPI.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 24-Apr-03    Chris W         VBM:2003030404 - getContentHandler() returns
 *                              MarlinContentHandler and is overloaded to
 *                              support native markup.
 * 30-Apr-03    Sumit           VBM:2003041502 - Added get method for the
 *                              MarlinPipelineFilter class
 * 16-May-03    Sumit           VBM:2003041502 - Added get method for the
 *                              DynamicPipelineProcessConfiguration class
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.pipeline.PipelineInitialization;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineFilter;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class provides various static methods to help use Marlin with SAX 2.
 * <p>
 * These methods are supplied purely for convenience and do not do anything
 * which cannot be done using other methods and classes directly.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class MarlinSAXHelper {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MarlinSAXHelper.class);

    /**
     * The default error handler.
     */
    private static ErrorHandler defaultErrorHandler =
        new DefaultErrorHandler();

    /**
     * Get a basic XMLReader. The Content and Error Handlers should be
     * assigned before using this reader.
     *
     * @return a basic XMLReader.
     */
    public static XMLReader getXMLReader() {

        // TODO: this should be using VolantisXMLReaderFactory
        return new com.volantis.xml.xerces.parsers.SAXParser();
    }

    /**
     * Get a default, pipeline-enabled XMLReader. The Content and Error
     * Handlers should be assigned before using this .
     *
     * @param requestContext The MarinerRequestContext to use to obtain the
     *                       pipeline configuration
     * @return a default XMLReader
     */
    public static XMLReader getXMLReader(MarinerRequestContext requestContext) {

        MarinerPageContext pageContext =
            ContextInternals.getMarinerPageContext(requestContext);

        // retrive the PipelineInitialization instance that the volantis
        // bean manages.
        PipelineInitialization pipelineInitialization =
            pageContext.getVolantisBean().getPipelineInitialization();

        // get hold of the Adapter process factory
        XMLPipelineFactory factory =
            pipelineInitialization.getPipelineFactory();

        // create the pipeline context
        XMLPipelineContext pipelineContext = null;

       ExpressionContext expressionContext = null;

        // We might already have a valid expression context set up
        expressionContext =
            MCSExpressionHelper.getExpressionContext(requestContext);

        // create the pipeline configuration. This particular factory will
        // create a configuration based on the MCS config file.
        XMLPipelineConfiguration pipelineConfiguration =
                    factory.createPipelineConfiguration();

        if (expressionContext != null) {
            // We have a valid expression context already set up for us so
            // use this when creating a new pipeline context
            pipelineContext =
                    factory.createPipelineContext(pipelineConfiguration,
                                                  expressionContext);
        } else {
            throw new IllegalStateException("No expression context set up");
//            // create a new pipeline context
//            EnvironmentInteraction rootInteraction =
//                    createRootInteraction(requestContext);
//            pipelineContext =
//                    factory.createPipelineContext(pipelineConfiguration,
//                                                  rootInteraction);
//
//            // If we are running within the context of a servlet or JSP then we
//            // must set up the expression context correctly with the servlet
//            // request and register all the template extension functions
//            pipelineContext.getExpressionContext().setProperty(
//                MarinerRequestContext.class, requestContext, false);
//
//            ExpressionSupport.registerFunctions(
//                pipelineContext.getExpressionContext());
//
//            // Store the expression context in the request context's
//            // environment context, so that any sub-requests can share the
//            // expression context.
//            MCSExpressionHelper.setExpressionContext(
//                    requestContext, pipelineContext.getExpressionContext());
        }

                // get hold of the environment context associated with the request
        EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(requestContext);

        environmentContext.initalisePipelineContextEnvironment(pipelineContext);

	    XMLPipeline pipeline = factory.createDynamicPipeline(pipelineContext);
	    // create the pipeline fiter
	    XMLPipelineFilter pipelineFilter = null;
        try {
            pipelineFilter =
		        factory.createPipelineFilter(pipeline);
        } catch (SAXException se) {
            throw new ExtendedRuntimeException(se);
        }


        // Get hold of a basic parser and set it as the parent of the pipeline
        XMLReader reader = getXMLReader();

        pipelineFilter.setParent(reader);

        // store the Pipeline Context away in the environment context. This allows other
        // objects access to the current pipeline context
        setPipelineContext(pipelineContext, requestContext);

        if (logger.isDebugEnabled()) {
            logger.debug("Constructed marlin pipeline reader");
        }

        return pipelineFilter;
    }

    /**
     * Return the default ErrorHandler
     *
     * @return an ErrorHandler
     */
    public static ErrorHandler getDefaultErrorHandler() {
        return defaultErrorHandler;
    }

    /**
     * Get an XMLReader configured with a MarlinContentHandler and the
     * specified error handler.
     *
     * @param requestContext The initial MarinerRequestContext to use for the
     *                       MarlinContentHandler
     * @param errorHandler   The ErrorHandler to associate with the XMLReader,
     *                       a default one is used if null is specified
     * @return an XMLReader
     */
    public static XMLReader getXMLReader(MarinerRequestContext requestContext,
                                         ErrorHandler errorHandler) {
        XMLReader reader = getXMLReader(requestContext);
        ContentHandler contentHandler = getContentHandler(requestContext);

        reader.setContentHandler(contentHandler);

        if (errorHandler == null) {
            errorHandler = defaultErrorHandler;
        }

        reader.setErrorHandler(errorHandler);

        return reader;
    }

    /**
     * Create and initialise a MCSInternalContentHandler for use in MCS.
     *
     * @param requestContext    The initial MarinerRequestContext to use to
     *                          initialise the MCSInternalContentHandler
     * @return MCSInternalContentHandler that supports the nativemarkup element
     */
    public static MCSInternalContentHandler getContentHandler(
        MarinerRequestContext requestContext) {
        return getContentHandler(requestContext, true);
    }

    /**
     * Create and initialise a MCSInternalContentHandler. If the
     * supportsNativeMarkup parameter is true then the returned handler will
     * support the nativemarkup element, otherwise it does not. In the latter
     * case it will throw a SAXException if it encounters a nativemarkup element.
     *
     * @param requestContext       The initial MarinerRequestContext to use to
     *                             initialise the MCSInternalContentHandler.
     * @param supportsNativeMarkup This boolean specifies if the returned
     *                             MCSInternalContentHandler supports native
     *                             markup.
     * @return MCSInternalContentHandler which may or may not support native
     * markup (depending on the value of the supportsNativeMarkup parameter).
     */
    public static MCSInternalContentHandler getContentHandler(
            MarinerRequestContext requestContext,
            boolean supportsNativeMarkup) {

        // Retrieve the pipeline context associated with the request
        XMLPipelineContext pipelineContext =
                getPipelineContext(requestContext);

        // obtain the flow control manager from the pipeline context.
        // ok for this to be null
        FlowControlManager flowControlManager = (pipelineContext != null) ?
                pipelineContext.getFlowControlManager() : null;

        MCSInternalContentHandler defaultHandler;
        if (supportsNativeMarkup) {
            defaultHandler =
                    new SwitchablePAPIContentHandler(flowControlManager);
        } else {
            defaultHandler = new PAPIContentHandler(flowControlManager);
        }

        NamespaceSwitchContentHandler switchingHandler =
                new NamespaceSwitchContentHandler(defaultHandler);

        switchingHandler.setInitialRequestContext(requestContext);

        return switchingHandler;
    }

    /**
     * Create and initialise a MarlinContentHandler, associate it with a newly
     * created XMLReader along with the error handler and then use it to parse
     * the InputSource.
     * The base URI for the popelineContext will be set to the absolute URL
     * of the request URL
     * 
     * @param requestContext The initial MarinerRequestContext to use for the
     *                       MarlinContentHandler.
     * @param errorHandler   The ErrorHandler to associate with the XMLReader,
     *                       a default one is used if this has not been
     *                       specified.
     * @param inputSource    The InputSource to parse.
     * @throws SAXException If there was a problem parsing the input source.
     * @throws IOException  If there was a problem reading from the input
     *                      source.
     */
    public static void parse(MarinerRequestContext requestContext,
                             ErrorHandler errorHandler,
                             InputSource inputSource)
        throws SAXException, IOException {
        XMLReader reader = getXMLReader(requestContext, errorHandler);
        
         MarinerPageContext pageContext =
            ContextInternals.getMarinerPageContext(requestContext);
             
         XMLPipelineContext pipelineContext = getPipelineContext(requestContext);
         // set the Base URI in the pipeline's context
         try {
             URL baseURI = pageContext.getAbsoluteURL(
                pageContext.getRequestURL(false));
             if (logger.isDebugEnabled()) {
                 logger.debug("Setting Base URI " + baseURI);
             }
             pipelineContext.pushBaseURI(baseURI.toExternalForm());
         } catch (MalformedURLException e) {
             throw new ExtendedSAXException(e);
         }

        reader.parse(inputSource);
    }

    /**
     * Wrap the specified systemId in an InputSource and invoke
     * {@link #parse(MarinerRequestContext, ErrorHandler, InputSource)}.
     *
     * @param requestContext The initial MarinerRequestContext to use for the
     *                       MarlinContentHandler
     * @param errorHandler   The ErrorHandler to associate with the XMLReader,
     *                       a default one is used if null is specified
     * @param systemId       The systemId which identifies the resource to
     *                       parse
     * @throws SAXException If there was a problem parsing the input source
     * @throws IOException  If there was a problem reading from the input
     *                      source
     */
    public static void parse(MarinerRequestContext requestContext,
                             ErrorHandler errorHandler,
                             String systemId) throws SAXException,
        IOException {
        parse(requestContext, errorHandler, new InputSource(systemId));
    }

    /**
     * Factory method for factoring a root <code>EnvironmentInteraction</code>
     * instance
     * @return a EnvironmentInteraction instance or null
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @todo later where can we obtain a Servlet and ServletConfig from
     */
    private static EnvironmentInteraction createRootInteraction(
            MarinerRequestContext requestContext) {

        // get hold of the environment context associated with the request
        EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(requestContext);
        return environmentContext.createRootEnvironmentInteraction();
    }

    /**
     * Saves an <code>XMLPipelineContext</code> away. The same
     * XMLPipelineContext can be retrievied via the
     * {@link #getPipelineContext}.
     * @param pipelineContext the XMLPipelineContext to set
     * @param requestContext the associated MarinerRequestContext
     */
    private static void setPipelineContext(
            XMLPipelineContext pipelineContext,
            MarinerRequestContext requestContext) {

        // get hold of the environment context associated with the request
        EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(requestContext);

        // save the pipeline context away in the environment context
        environmentContext.setPipelineContext(pipelineContext);
    }

    /**
     * Retrieves the <code>XMLPipelineContext</code> associated with the
     * MarinerRequestContext.
     * @param requestContext the MarinerRequestContext
     * @return an XMLPipelineContext or null if one has not been set
     */
    private static XMLPipelineContext getPipelineContext(
            MarinerRequestContext requestContext) {

        // get hold of the environment context associated with the request
        EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(requestContext);

        return environmentContext.getPipelineContext();
    }

    /**
     * Private class which is used if an error handler is not specified.
     */
    private static class DefaultErrorHandler implements ErrorHandler {
        /**
         * Called by the sax parser when a non-serious error is encountered in
         * the XML file.
         *
         * @param exception A SAXParseException describing the problem
         */
        public void warning(SAXParseException exception) {
            logger.warn("warning", exception);
        }

        /**
         * Called by the sax parser when a serious error is encountered in the
         * XML file.
         *
         * @param exception A SAXParseException describing the problem
         */
        public void error(SAXParseException exception) {
            logger.error("error", exception);
        }

        /**
         * Called by the sax parser when a fatal error is encountered in the
         * XML file.
         *
         * @param exception A SAXParseException describing the problem
         */
        public void fatalError(SAXParseException exception)
            throws SAXException {
            logger.error("fatal-error", exception);

            throw new ExtendedSAXException(exception);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/2	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 07-Oct-04	5239/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 07-Oct-04	5237/4	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/6	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 19-Feb-04	3133/1	adrian	VBM:2004011902 fixed bug caused by pipeline api change

 02-Feb-04	2802/1	ianw	VBM:2004011921 Added mechanism to enable AppServer interfaces to configure NamespaceSwitchContentHandler

 15-Oct-03	1439/3	mat	VBM:2003091805 Rework changes

 13-Oct-03	1439/1	mat	VBM:2003091805 Added WebSphere PortletFilter

 15-Aug-03	1115/1	philws	VBM:2003071704 Fix includeJSP to work from XML file

 14-Aug-03	1096/1	adrian	VBM:2003070805 updated usages of XMLPipelineContext and PropertyContainer to match pipeline api changes

 13-Aug-03	1048/3	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 13-Aug-03	776/4	chrisw	VBM:2003071005 Fix supermerge conflicts

 10-Jul-03	776/1	chrisw	VBM:2003071005 Remove MarlinContentHandler.setInitialRequestContext()

 11-Aug-03	1013/1	chrisw	VBM:2003080806 Refactored expressions to be jsp independent

 06-Aug-03	954/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 01-Aug-03	880/3	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 31-Jul-03	828/3	philws	VBM:2003071802 Update MCS against new Volantis Expression API from the Pipeline depot

 22-Jul-03	833/2	adrian	VBM:2003071902 added marlin support for invocation elements

 30-Jun-03	552/4	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 27-Jun-03	503/2	sumit	VBM:2003061906 ServletRequestFunction fixed to take MarinerRequestContext as source of parameters

 17-Jun-03	416/3	doug	VBM:2003061601 Intergrated XML pipeline with marlin

 16-Jun-03	366/9	doug	VBM:2003041502 Integration with pipeline JSPs

 13-Jun-03	316/1	byron	VBM:2003060403 Addressed some rework issues

 ===========================================================================
*/
