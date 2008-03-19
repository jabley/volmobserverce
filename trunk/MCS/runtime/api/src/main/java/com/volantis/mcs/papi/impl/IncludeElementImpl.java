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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.papi.IncludeAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.runtime.pipeline.PipelineIntegrationUtilities;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The include element. Allows an XML or plain text file to be processed,
 * causing the content to be incorporated into the page being generated.
 */
public class IncludeElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(IncludeElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(IncludeElementImpl.class);

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {
        // This tag specifically does not push itself onto the page context
        // element stack (it can't have children anyway). However, it
        // is important that there is an element in that stack in order to
        // allow the integration process to operate correctly.
        return SKIP_ELEMENT_BODY;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {
        IncludeAttributes attributes = (IncludeAttributes) papiAttributes;

        if (attributes.getHref() == null) {
            throw new PAPIException(
                    exceptionLocalizer.format("include-href-missing"));
        } else {
            // @todo this is a bit duff since it relies on markup naming not to change; do this a different way
            // Set up the markup that will be sent down the pipeline
            StringBuffer markup = new StringBuffer();
            InputSource inputSource;

            markup.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            markup.append("<urid:fetch xmlns:urid=\"").
                    append(Namespace.URID.getURI()).append("\" href=\"").
                    append(attributes.getHref()).
                    append("\"");

            if (attributes.getParse() != null) {
                markup.append(" parse=\"").
                        append(attributes.getParse()).
                        append("\"");
            }

            if (attributes.getEncoding() != null) {
                markup.append(" encoding=\"").
                        append(attributes.getEncoding()).
                        append("\"");
            }

            markup.append("/>");

            if (logger.isDebugEnabled()) {
                logger.debug("Set up inclusion command as: " +
                        markup.toString());
            }

            inputSource = new InputSource(new StringReader(markup.toString()));

            // Set up and execute the pipeline to perform the required
            // inclusion
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);
            XMLReader reader = MarlinSAXHelper.getXMLReader(context);
            reader.setContentHandler(MarlinSAXHelper.
                    getContentHandler(context));
            // @todo this is nasty: we assume that the reader is actually an XMLPipelineFilter
            XMLPipelineFilter filter = (XMLPipelineFilter) reader;
            XMLPipelineContext pipelineContext = filter.getPipelineContext();

            // set the Base URI in the pipeline's context
            try {
                URL baseURI = pageContext.getAbsoluteURL(
                        pageContext.getRequestURL(false));

                if (logger.isDebugEnabled()) {
                    logger.debug("Setting Base URI " + baseURI);
                }

                pipelineContext.pushBaseURI(baseURI.toExternalForm());
            } catch (MalformedURLException e) {
                throw new PAPIException(e);
            }

            PipelineIntegrationUtilities.setUpIntegration(
                    filter,
                    pageContext.getCurrentElement(),
                    context,
                    pageContext.getCurrentOutputBuffer());

            if (logger.isDebugEnabled()) {
                logger.debug("Executing inclusion");
            }

            // Execute the inclusion
            try {
                reader.parse(inputSource);
            } catch (IOException e) {
                throw new PAPIException(e);
            } catch (SAXException e) {
                throw new PAPIException(e);
            } finally {
                PipelineIntegrationUtilities.tearDownIntegration(
                        filter);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Successful execution of inclusion");
        }

        return CONTINUE_PROCESSING;
    }

    // javadoc inherited
    boolean hasMixedContent() {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 30-Jun-03	552/1	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 ===========================================================================
*/
