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

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.marlin.sax.MCSInternalContentHandler;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.pipeline.PipelineInitialization;
import com.volantis.shared.content.ContentStyle;
import com.volantis.shared.resource.DefaultResourceLoader;
import com.volantis.shared.dependency.Tracking;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineFilter;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import com.volantis.xml.pipeline.sax.url.URLConfigurationFactory;
import com.volantis.xml.sax.DtdEntityResolver;
import com.volantis.xml.sax.LocalExternalEntity;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Default implementation of the XDIMERequestProcessorHelper.
 */
public class DefaultXDIMERequestProcessorHelper implements 
        XDIMERequestProcessorHelper {

    /**
     * An entity resolver to locally resolve the XHTML 2 DTD if the user
     * specifies it in the DOCTYPE of the input document.
     * <p>
     * This allows our customers to pick up the standard extra character
     * entities (eg &euro;) that are "normally" available without clobbering
     * the real www.w3.org website.
     * <p>
     * Note that this DTD contains *only* character entities, and no validation
     * rules.
     */
    private static final DtdEntityResolver xhtmlDtdResolver =
            new DtdEntityResolver(new DefaultResourceLoader());
    static {
        // Add the definitions of the external entities that this resolver
        // can resolve locally. Note that the .ent files are used both here
        // and by the remote repository parsing.
        xhtmlDtdResolver.addExternalEntity(new LocalExternalEntity(
                "-//W3C//DTD XHTML 2.0//EN",
                "http://www.w3.org/MarkUp/DTD/xhtml2.dtd",
                "com/volantis/mcs/servlet/dtd/xhtml2.dtd"));
        xhtmlDtdResolver.addExternalEntity(new LocalExternalEntity(
                "-//W3C//ENTITIES Latin 1 for XHTML//EN",
                "http://www.w3.org/MarkUp/DTD/xhtml-lat1.ent",
                "com/volantis/mcs/dtd/xhtml-lat1.ent"));
        xhtmlDtdResolver.addExternalEntity(new LocalExternalEntity(
                "-//W3C//ENTITIES Symbols for XHTML//EN",
                "http://www.w3.org/MarkUp/DTD/xhtml-symbol.ent",
                "com/volantis/mcs/dtd/xhtml-symbol.ent"));
        xhtmlDtdResolver.addExternalEntity(new LocalExternalEntity(
                "-//W3C//ENTITIES Special for XHTML//EN",
                "http://www.w3.org/MarkUp/DTD/xhtml-special.ent",
                "com/volantis/mcs/dtd/xhtml-special.ent"));
    }

    private final ServletResourceMapper resourceMapper;

    public DefaultXDIMERequestProcessorHelper(ServletContext servletContext) {
        resourceMapper = new ServletResourceMapperImpl(servletContext);
    }

    //javadoc inherited
    public MarinerServletRequestContext createServletRequestContext(
                ServletContext context,
                ServletRequest request,
                ServletResponse response)
                throws IOException, MarinerContextException {
        return new MarinerServletRequestContext(context,
                                                request,
                                                response);
    }

    //javadoc inherited
    public void parseXDIME(
        final MarinerServletRequestContext servletRequestContext,
        final CachedContent xdimeContent,
        final ContentHandler contentHandler)
                throws IOException, ServletException, SAXException {

        // TODO: enable validation of input XDIME. See VBM:2006051821.

        XMLReader xmlReader =
                    MarlinSAXHelper.getXMLReader(servletRequestContext);

        storeVisitedIdsInPipelineContext(servletRequestContext);

        // enable dependency tracking
        final MarinerPageContext pageContext =
            ContextInternals.getMarinerPageContext(servletRequestContext);
        final XMLPipelineContext pipelineContext =
            pageContext.getEnvironmentContext().getPipelineContext();
        pipelineContext.getDependencyContext().pushDependencyTracker(
            Tracking.ENABLED);

        // connect the given content handler to the parser
        xmlReader.setContentHandler(contentHandler);
        // connect a default error handler to the parser
        xmlReader.setErrorHandler(MarlinSAXHelper.getDefaultErrorHandler());
        // Tell the parser to locally resolve the XHTML 2 DTD if specified.
        xmlReader.setEntityResolver(xhtmlDtdResolver);

        // Treat the response wrapper as an InputSource for the XML parser.
        InputSource inputSource;
        ContentStyle contentStyle = xdimeContent.getContentStyle();
        if (contentStyle == ContentStyle.BINARY) {
            inputSource = new InputSource(new ByteArrayInputStream(
                    xdimeContent.getAsByteArray()));
        } else if (contentStyle == ContentStyle.TEXT) {
            inputSource = new InputSource(new CharArrayReader(
                    xdimeContent.getAsCharArray()));
        } else {
            throw new IllegalStateException("no xdime content available");
        }

        // Ensure that the initial base URI for the XML processing is
        // correctly set via the use of a system ID on the input source
        // to enable processing of relative URLs within XDIME/DCI markup
        final String systemIdForRequest =
                getSystemIDForRequest(servletRequestContext);

        if (systemIdForRequest != null) {
            inputSource.setSystemId(systemIdForRequest);
        }

        // Parse the response and process it as XDIME.
        xmlReader.parse(inputSource);

        pipelineContext.getDependencyContext().popDependencyTracker();
    }

    /**
     * Reads the list of visited MCS instance IDs from the servlet request
     * context and stores it in the current pipeline context. If the list of
     * visited IDs doesn't contain the ID of the current MCS instance, it is
     * appended to the list stored in the pipeline context.
     *
     * @param requestContext the servlet request context to access the headers
     * of the incoming request
     */
    private void storeVisitedIdsInPipelineContext(
            final MarinerServletRequestContext requestContext) {

        final EnvironmentContext environmentContext =
            ContextInternals.getEnvironmentContext(requestContext);
        final XMLPipelineContext pipelineContext =
            environmentContext.getPipelineContext();

        // store the ID's of the visited MCS instances adding the ID of the
        // current instance.
        String instances = requestContext.getHttpRequest().getHeader(
            URLConfigurationFactory.VISITED_MCS_INSTANCES_HEADER_NAME);
        if (instances == null || instances.trim().length() == 0) {
            instances = "";
        } else {
            instances += ",";
        }
        final Volantis volantis = ApplicationInternals.getVolantisBean(
            requestContext.getMarinerApplication());
        if (instances.indexOf(volantis.getMCSInstanceIdentifier()) < 0) {
            instances += volantis.getMCSInstanceIdentifier();
            pipelineContext.setProperty(
                URLConfigurationFactory.VISITED_MCS_INSTANCES_HEADER_NAME,
                instances, false);
        }
    }

    /**
     * Returns a system ID appropriate to the given request.
     *
     * @param requestContext the request context for which a system ID is
     * required.
     * @return a system ID representing the given request
     * @throws java.net.MalformedURLException if the ID cannot be resolved
     * correctly
     */
    private String getSystemIDForRequest(
            final MarinerServletRequestContext requestContext)
            throws MalformedURLException {
        // The system ID is a URL of some form that is representative of the
        // current request. Using the external request URL may cause problems
        // (such as servlet re-entrancy problems) on certain containers and
        // therefore should only be used as a last resort.
        //
        // The first way to generate a URL is to resolve the path for the
        // current request relative to the servlet, to obtain this path as a
        // resource URL and use the resource URL's external form as the system
        // ID. However, the container may return a null URL if the resource is
        // not accessible.
        HttpServletRequest request = requestContext.getHttpRequest();

        return resourceMapper.getSystemIDForRequest(request);
    }

    //javadoc inherited
    public String getXDIMEString(CachedContent xdimeContent)
            throws IOException {
        String xdimeString;
        final ContentStyle contentStyle = xdimeContent.getContentStyle();
        if (contentStyle == ContentStyle.BINARY) {
            // Content is binary. We know it is XDIME but we do not know the
            // character set as that is buried in the XML declaration.
            //
            // So, we should scan the declaration to find the charset.
            // However, that would be non-trivial, we have a release to get out
            // and the caching is broken anyway so for now we just assume it is
            // the platform default charset.
            // TODO: find the real XML charset of the input XDIME when caching is fixed.
            xdimeString = new String(xdimeContent.getAsByteArray());

        } else if (contentStyle == ContentStyle.TEXT) {
            // Content is already text.
            xdimeString = new String(xdimeContent.getAsCharArray());
        } else {
            throw new IllegalStateException("no content available");
        }
        return xdimeString;
    }

    //javadoc inherited
    public MCSInternalContentHandler getContentHandler(
            MarinerRequestContext marinerRequestContext) {
        return MarlinSAXHelper.getContentHandler(marinerRequestContext);
    }

    //javadoc inherited
    public void replayEventsIntoMCS(
            PipelineRecording recording,
            MarinerServletRequestContext requestContext)

            throws SAXException {

        // Get the MCS content handler which will process the XDIME into
        // the target markup.
        ContentHandler mcsContentHandler =
                MarlinSAXHelper.getContentHandler(requestContext);

        XMLPipelineFactory factory = getPipelineFactory(requestContext);

        // Create a pipeline for the pipeline filter.
        XMLPipeline pipeline = createPipeline(requestContext);

        // get the ContextUpdatingProcess, this will make sure that the
        // name spaces are pushed onto the namespace tracker during
        // the replay.
        XMLProcess contextUpdatingProcess = factory.createContextUpdatingProcess();

        pipeline.addHeadProcess(contextUpdatingProcess);

        XMLPipelineFilter mcsFilter = factory.createPipelineFilter(pipeline);

        mcsFilter.setContentHandler(mcsContentHandler);

        // Replay the SAX events into the pipeline.
        PipelinePlayer player = recording.createPlayer();
        player.play(pipeline.getPipelineProcess());
    }

    /**
     * Create a new pipeline for use in replaying sax events into MCS.
     *
     * @param context used to get the pipeline initilization
     * @return a new pipeline, should not be null.
     */
    private XMLPipeline createPipeline(MarinerRequestContext context) {
        XMLPipelineFactory factory = getPipelineFactory(context);

        ExpressionContext expressionContext =
            MCSExpressionHelper.getExpressionContext(context);

            // create the pipeline configuration. This particular factory will
            // create a configuration based on the MCS config file.
            XMLPipelineConfiguration pipelineConfiguration =
                factory.createPipelineConfiguration();

            XMLPipelineContext pipelineContext =
                    factory.createPipelineContext(pipelineConfiguration,
                                                  expressionContext);

        return factory.createPipeline(pipelineContext);
    }

    /**
     * Get hold of a pre existing pipeline factory.
     *
     * @param context used to get the pipeline initialiseation.
     * @return the pipeline factory, not expected to be null.
     */
    private XMLPipelineFactory getPipelineFactory(
        MarinerRequestContext context) {
        MarinerPageContext marinerPageContext =
            ContextInternals.getMarinerPageContext(context);

        // initialize the pipelineInitialization so that we can
        // create a pipeline.
        PipelineInitialization pipelineInitialization =
            marinerPageContext.getVolantisBean().getPipelineInitialization();

        // get hold of the pipeline factory
        return pipelineInitialization.getPipelineFactory();
    }
}
