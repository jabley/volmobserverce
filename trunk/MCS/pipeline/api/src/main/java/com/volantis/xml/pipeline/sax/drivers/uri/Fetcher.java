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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.uri;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLContentManager;
import com.volantis.shared.net.url.http.RuntimeHttpException;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.performance.MonitoredTransaction;
import com.volantis.xml.pipeline.sax.ResourceNotFoundException;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import com.volantis.xml.pipeline.sax.performance.MonitoringConfiguration;
import com.volantis.xml.pipeline.sax.url.PipelineURLContentManager;
import com.volantis.xml.pipeline.sax.url.URLConfigurationFactory;
import com.volantis.xml.utilities.sax.XMLReaderFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Fetches content from a URL and inserts it into the pipeline.
 */
public class Fetcher {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(Fetcher.class);
    /**
     * Constant to identify the "text" value for the parse attribute.
     */
    public static final String TEXT_VALUE = "text";

    /**
     * Constant to identify the "xml" value for the parse attribute.
     */
    public static final String XML_VALUE = "xml";

    /**
     * The default encoding to use. We might want to move this out into a
     * Configuration object
     */
    public static final String DEFAULT_TEXT_ENCODING = "UTF-8";

    private final XMLPipelineContext context;
    private final XMLProcess target;

    /**
     * References the href that is to be included
     */
    private String href;

    /**
     * References the parse mode that will be used when including the contents
     * of the href
     */
    private String parse;

    /**
     * References the encoding that will be used if the href is being incuded
     * as text
     */
    private String encoding;

    /**
     * The timeout, measured in seconds, to be applied to the fetch operation.
     */
    private Period timeout;

    /**
     * The transaction for monitoring.
     */
    private MonitoredTransaction uridFetchTransaction;

    public Fetcher(XMLPipeline pipeline) {
        this.context = pipeline.getPipelineContext();
        this.target = getTargetProcess(pipeline);
    }

    /**
     * Get the process associated with this pipeline to which events should
     * be targeted.
     *
     * <p>If the pipeline is not empty then it returns the head process,
     * otherwise it returns the next process after the
     * {@link com.volantis.xml.pipeline.sax.XMLPipeline#getPipelineProcess()}. If that is null then it
     * throws a {@link org.xml.sax.SAXException}.</p>
     *
     * @param pipeline The pipeline whose target process is requested.
     * @return Target process.
     */
    public static XMLProcess getTargetProcess(XMLPipeline pipeline) {
        XMLProcess target = pipeline.getHeadProcess();
        if (target == null) {
            target = pipeline.getPipelineProcess().getNextProcess();
        }
        if (target == null) {
            throw new IllegalStateException("Cannot find target process");
        }
        return target;
    }

    /**
     * Set the href property that identifies the content to include
     * @param href String representation of the URL that references the
     * contents to include. Relative URLs will be resolved.
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Get the href property that identifies the content to include
     * @return String representation of the URL that references the
     * contents to include.
     */
    public String getHref() {
        return href;
    }

    /**
     * Set the method of parsing that will be used when including the content
     * of the href property
     * @param parse the parse method.
     */
    public void setParse(String parse) {
        this.parse = parse;
    }

    /**
     * Get the method of parsing that will be used when including the content
     * of the href property
     * @return the parse method.
     */
    public String getParse() {
        return parse;
    }

    /**
     * Set the method of encoding that will be used when including content with
     * a parse attribute of "text". If content is being included as "xml" the
     * encoding attribute is ignored.
     * @param encoding the encoding to use.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Get the method of encoding that will be used when including content with
     * a parse attribute of "text". If content is being included as "xml" the
     * encoding attribute is ignored.
     * @return String encoding to use.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Retrieve the timeout to be applied.
     *
     * @return the timeout to apply, null means use default timeout.
     */
    public Period getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout to be applied to the fetch operation.
     *
     * @param timeout the timeout to apply, or null if the default should be
     *                used.
     */
    public void setTimeout(Period timeout) {
        this.timeout = timeout;
    }

    // javadoc inherited from interface
    public void release() {
        // set the attributes to null
        href = null;
        parse = null;
        encoding = null;
    }

    /**
     * Method that determines if the attributes have been set correctly for
     * this.

     * @return True if the attributes are valid, false otherwise.
     */
    private boolean validateAttributes() throws SAXException {
        boolean isValid = true;

        // The href attribute is required
        if (null == href) {
            // need to send an error down the pipeline
            Locator locator = context.getCurrentLocator();
            ResourceNotFoundException rnfe =
                    new ResourceNotFoundException("URLConnector requires the " +
                                                  "href attribute to be set",
                                                  locator);
            target.fatalError(rnfe);
            isValid = false;
        }

        if (null != parse &&
                !(XML_VALUE.equals(parse) || TEXT_VALUE.equals(parse))) {
            // need to send an error down the pipeline
            Locator locator = context.getCurrentLocator();
            XMLProcessingException e =
                    new XMLProcessingException("parse attribute must either " +
                                               TEXT_VALUE + " or " + XML_VALUE,
                                               locator);
            target.fatalError(e);
            isValid = false;
        }
        return isValid;
    }                                                               

    /**
     * Include the document that has been specified via a call to the
     * setHref() method. Calling this method without calling setHref() first
     * will result in an error being reported.
     * @throws org.xml.sax.SAXException
     */
    public void doInclude() throws SAXException {

        MonitoringConfiguration monitoringConfiguration =
                (MonitoringConfiguration) context.getPipelineConfiguration()
                .retrieveConfiguration(MonitoringConfiguration.class);

       uridFetchTransaction =
                monitoringConfiguration.getTransaction("urid-fetch");

        uridFetchTransaction.start();
        // Make sure that that the data that has been provided is valid.
        String urlAsString = null;
        if (!context.inErrorRecoveryMode() && validateAttributes()) {
            try {
                // retrieve the URL that should be used to resolve any
                // relative URIS.
                URL base = context.getCurrentBaseURI();
                // ensure that we have a fully resolved URI.
                URL resolved =
                        (null == base) ? new URL(href) : new URL(base, href);
                urlAsString = resolved.toExternalForm();


                // incude the content
                if (TEXT_VALUE.equals(parse)) {
                    includeTextDocument(resolved);
                } else {
                    includeXMLDocument(resolved);
                }

            } catch (MalformedURLException e) {
                uridFetchTransaction.stop(
                    MonitoredTransaction.FAILED, urlAsString);
                // need to generate an error event.
                Locator locator = context.getCurrentLocator();
                ResourceNotFoundException error = new ResourceNotFoundException(
                    "The URI " + href + " could not be included", locator, e);
                target.fatalError(error);
            } catch (RuntimeHttpException e) {
                uridFetchTransaction.stop(
                    MonitoredTransaction.FAILED, urlAsString);
                // need to generate an error event.
                Locator locator = context.getCurrentLocator();
                ResourceNotFoundException error = new ResourceNotFoundException(
                    "The URI " + href + " could not be included", locator, e);
                target.fatalError(error);
            }
            uridFetchTransaction.stop(MonitoredTransaction.SUCCESSFUL, urlAsString);
        } else {
            uridFetchTransaction.stop(MonitoredTransaction.FAILED, urlAsString);
        }
    }

    /**
     * Includes and InputSource that references an XML document.
     * THe XML beind included will be paresed via an XMLReader
     * @throws org.xml.sax.SAXException if the next XMLProcess cannot handle any
     * error that it has recieved via its ErrorHandler interface.
     */
    private void includeXMLDocument(URL url)
            throws SAXException {

        String inclusionSystemId = url.toExternalForm();
        // check to see if the document that is about to be processed has
        // already been included. If it has then
        if (isCyclicInclusion(url)) {
            // cannot enter a cyclic inclusion
            Locator locator = context.getCurrentLocator();
            CyclicInclusionException ce =
                    new CyclicInclusionException("Cyclic Inclusion encountered " +
                                                 inclusionSystemId, locator);
            target.fatalError(ce);
            return;
        }

        // As this XML Document can contain pipeline mark-up we need
        // to ensure that the parsed document enters a pipeline so that
        // it is processed correctly.
        XMLPipelineFactory factory = context.getPipelineFactory();

        // create a new pipeline.
        XMLPipeline pipeline = factory.createDynamicPipeline(context);

        XMLProcess cup = factory.createContextUpdatingProcess();
        cup.setPipeline(target.getPipeline());
        XMLProcess pipelineProcess = pipeline.getPipelineProcess();
        cup.setNextProcess(pipelineProcess);

        // set up the XMLReader so that the inclusion can be read.
        XMLReader reader = createXMLReader();
        reader.setErrorHandler(cup);
        reader.setContentHandler(cup);

        // Chain the new pipeline process to the target process
        pipelineProcess.setNextProcess(target);

        URLContentManager manager = PipelineURLContentManager.retrieve(
                context);

        try {
            final URLConfiguration urlConfiguration =
                URLConfigurationFactory.getURLConfiguration(url, context);
            URLContent content =
                manager.getURLContent(url, timeout, urlConfiguration);

            // InputSource for content that we are trying to include.
            InputSource source = new InputSource(content.getInputStream());

            // Ensure that the systemID property is set for the
            // InputSource. The XMLReader will need this in order to
            // resolve relative URIs
            source.setSystemId(inclusionSystemId);

            // add dependency to the dependency context
            final DependencyContext dependencyContext =
                context.getDependencyContext();
            if (dependencyContext != null &&
                dependencyContext.isTrackingDependencies()) {

                dependencyContext.addDependency(content.getDependency());
            }

            // perform the inclusion
            reader.parse(source);
        } catch (SAXException e) {

            // If the parser encounters a truly fatal error (e.g. an entity it
            // does not understand), it will throw an exception even if the
            // pipeline try operation prevents the error handler from doing so.
            // This basically defeats our currently useless try operation and
            // allows the entire pipeline to crash. So we must catch any
            // exceptions here manually and deal with them ourselves. Sigh.
            //
            // If we fixed VBM:2004030305 then this code would hopefully not be
            // required.

            uridFetchTransaction.stop(MonitoredTransaction.FAILED,
                    url.toExternalForm());

            // If the pipeline is already handling this error,
            if (context.inErrorRecoveryMode()) {
                // Then we assume that there is a containing try operation that
                // needs help to avoid the pipeline crashing, so we need to
                // mask the exception. So just continue on and hope the
                // pipeline error handling will save us. Note that we cannot
                // re-report it via fatalError or the flow control manager
                // objects.
                if (logger.isDebugEnabled()) {
                    logger.debug("Fetch encountered XML parsing exception " +
                            "while error recovery is in progress, ignoring", e);
                }
            } else {
                // Else we assume that there is no containing try operation.
                // In this case just let the exception propogate up and cause
                // the pipeline to die.
                if (logger.isDebugEnabled()) {
                    logger.debug("Fetch encountered XML parsing exception " +
                            "while no error recovery is in progress, rethrowing",
                            e);
                }
                throw e;
            }

        } catch (IOException ioe) {
             uridFetchTransaction.stop(MonitoredTransaction.FAILED, url.toExternalForm());
            // get hold of the current locator
            Locator currentLocator = context.getCurrentLocator();

            // this is a streaming error as the part of the document might
            // have been included.
            ResourceNotFoundException se =
                    new ResourceNotFoundException(
                            "Could not find the document " +
                            url.toExternalForm(),
                            currentLocator, ioe);

            // report this as a fatal error.
            target.fatalError(se);
        }
    }

    /**
     * Determines whether an inclusion cycle would occur if the given
     * URI was included.
     * @param inclusion the URL
     * @return true if and only if including the data at the given URL
     * would result in a cyclic inclusion
     */
    private boolean isCyclicInclusion(URL inclusion) {
        String inclusionSystemId = inclusion.toExternalForm();
        boolean isCyclic = false;
        Iterator iter = context.getLocators();
        Locator locator;
        while (iter.hasNext() && !isCyclic) {
            locator = (Locator)iter.next();
            isCyclic = inclusionSystemId.equals(locator.getSystemId());
        }
        return isCyclic;
    }

    /**
     * Includes an external document without parsing as an XML document.
     * That is the content of the doucument is passed down the pipeline
     * via calls to the next processes <code>characters()</code>
     * method.
     * @throws org.xml.sax.SAXException  if error occurs
     */
    private void includeTextDocument(URL url)
            throws SAXException {
        String characterEncoding = null;

        URLContentManager manager = PipelineURLContentManager.retrieve(
                context);

        try {
            final URLConfiguration urlConfiguration =
                URLConfigurationFactory.getURLConfiguration(url, context);
            URLContent content =
                manager.getURLContent(url, timeout, urlConfiguration);

            // The encoding to use is determined as follows.
            //
            // 1) The document being included specifies an encoding in
            //    the header, otherwise
            // 2) the value of the encoding attribute if one extist,
            //    otherwise
            // 3) UTF-8

            // try to obtain the encoding using approach 1.
            characterEncoding = content.getCharacterEncoding();

            // try to obtain the encoding using approach 2.
            if (null == characterEncoding) {
                characterEncoding = encoding;
            }

            // try to obtain the encoding using approach 3.
            if (null == characterEncoding) {
                characterEncoding = DEFAULT_TEXT_ENCODING;
            }

            InputStream is =
                    new BufferedInputStream(content.getInputStream());
            InputStreamReader reader =
                    new InputStreamReader(is, characterEncoding);

            final int CHUNK_SIZE = 1024;
            final char[] chars = new char[CHUNK_SIZE];
            int charsRead = 0;

            while (-1 != charsRead) {
                // read a chunk of the text into the buffer
                charsRead = reader.read(chars, 0, CHUNK_SIZE);
                if (charsRead > 0) {
                    // pass the characters down the XML pipeline
                    target.characters(chars, 0, charsRead);
                }
            }
            // add dependency to the dependency context
            final DependencyContext dependencyContext =
                context.getDependencyContext();
            if (dependencyContext != null &&
                dependencyContext.isTrackingDependencies()) {

                dependencyContext.addDependency(content.getDependency());
            }
        } catch (UnsupportedEncodingException uee) {
            // get hold of the current locator
            Locator currentLocator =
                    context.getCurrentLocator();

            // this is a processing error as it will occur before we start
            // reading in the text
            XMLProcessingException se =
                    new XMLProcessingException(
                            "Inclusion failed due to " +
                            "unsupporteddocument character encoding " +
                            characterEncoding, currentLocator, uee);

            // report this fatal a error
            target.fatalError(se);
            uridFetchTransaction.stop(
                MonitoredTransaction.FAILED, url.toExternalForm());

        } catch (IOException ioe) {
            // get hold of the current locator
            Locator currentLocator =
                    context.getCurrentLocator();

            // this is a streaming error as the part of the document might
            // have been included.
            XMLStreamingException se =
                    new XMLStreamingException("Could not include text document " +
                                              url.toExternalForm(),
                                              currentLocator, ioe);

            // report this as a fatal error
            target.fatalError(se);
            uridFetchTransaction.stop(
                MonitoredTransaction.FAILED, url.toExternalForm());
        }
    }

    /**
     * Factory method to return the XMLReader to use for parsing an XML
     * inclusion.
     * @return an XMLReader
     */
    protected XMLReader createXMLReader() throws SAXException {
        return XMLReaderFactory.createXMLReader(false);
    }
}
