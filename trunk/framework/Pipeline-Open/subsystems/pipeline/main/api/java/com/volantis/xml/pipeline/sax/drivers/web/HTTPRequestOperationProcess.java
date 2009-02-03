/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Â  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.Â  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.net.http.headers.HeaderUtils;
import com.volantis.shared.net.url.http.RuntimeHttpException;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.performance.MonitoredTransaction;
import com.volantis.synergetics.url.URLIntrospector;
import com.volantis.synergetics.url.URLPrefixRewriteManager;
import com.volantis.synergetics.url.URLPrefixRewriteOperation;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterTuple;
import com.volantis.xml.pipeline.sax.convert.URLRewriteProcess;
import com.volantis.xml.pipeline.sax.convert.URLRewriteProcessConfiguration;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import com.volantis.xml.pipeline.sax.performance.MonitoringConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The operation process for GET and POST Web Driver requests.
 */
public class HTTPRequestOperationProcess extends AbstractOperationProcess {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(HTTPRequestOperationProcess.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    HTTPRequestOperationProcess.class);

    /**
     * The TUPLES that are required for the URL rewriting.
     */
    private static final ConverterTuple[] TUPLES = new ConverterTuple[]{
        new ConverterTuple(null, "a", "href"),
        new ConverterTuple(null, "form", "action"),
        new ConverterTuple(null, "img", "src"),
        new ConverterTuple(null, "frame", "src"),
        new ConverterTuple(null, "link", "href"),
        new ConverterTuple(null, "xfform", "action")

        // This is commented out because we should really be adding this but
        // cannot due to a limitation in the current framework. The is a bug
        // since we cannot associate the 'base' attribute with 'any' element.
        // See URLRewriteProcess#startProcess. todo fix this.
        /*, new ConverterTuple(NamespaceSupport.XMLNS, "",
                ContextManagerProcess.BASE_ATTRIBUTE)
        */
    };

    /**
     * This value is used in a number of places. Its put here to help
     * locate it. It is the status code returned by a http server when a
     * conditional get is performed but no changes have been made.
     * The returned page will have no content.
     */
    private static final int NO_CHANGE_REPONSE = 304;

    /**
     * The type of the request for this operation.
     */
    private HTTPRequestType requestType;

    /**
     * The id for the request.
     */
    private String id;

    /**
     * The String representation of the url to use for this request.
     */
    private String urlString;

    /**
     * The string representation of the protocol to use for this request.  This
     * is extracted from {@link #urlString} and not explicitly set.
     */
    private String protocolString;

    /**
     * The version of http to use for the request.
     */
    private HTTPVersion httpVersion = HTTPVersion.HTTP_1_1; // default to 1.1.

    /**
     * This flag is used to determine whether this process silently follows
     * HTTP 302 response codes.
     */
    private Boolean followsRedirect;

    /**
     * This flag determines how errored content is handled in the pipeline.
     * If true the content is ignored and stored in an ignored content buffer
     * in the response.  If it is false the content is passed through the
     * pipeline.
     */
    private Boolean ignoreErroredContent;

    /**
     * The configuration for this operation process.
     */
    private WebDriverConfiguration configuration;

    /**
     * The input stream factory used to create alternative input streams
     * from an existing one and the value of the content encoding itself.
     */
    protected InputStreamFactory inputStreamFactory = null;

    /**
     * The timeout to be applied to connections made with the HTTP manager.
     * Measured in milliseconds. A zero or negative value means no timeout.
     */
    private Period timeout = Period.INDEFINITELY;

    /**
     * The pipeline configuration.
     */
    private XMLPipelineConfiguration pipelineConfiguration;

    /**
     * Construct a new RequestOperationProcess.
     */
    public HTTPRequestOperationProcess() {
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        // get hold of the pipeline context
        XMLPipelineContext context = getPipelineContext();

        // get hold of the pipeline configuration
        pipelineConfiguration = context.getPipelineConfiguration();

        configuration = (WebDriverConfiguration) pipelineConfiguration.
                retrieveConfiguration(WebDriverConfiguration.class);
    }

    // javadoc inherited from interface
    public void stopProcess() throws SAXException {
        XMLPipelineContext context = getPipelineContext();
        if (!context.inErrorRecoveryMode()) {

            MonitoringConfiguration monitoringConfiguration =
                    (MonitoringConfiguration) context.getPipelineConfiguration()
                    .retrieveConfiguration(MonitoringConfiguration.class);


            MonitoredTransaction webdTransaction =
                    monitoringConfiguration.getTransaction("webd");

            webdTransaction.start();
            PluggableHTTPManager httpManager =
                    ((WebDriverConfigurationImpl) configuration).
                    getPluggableHTTPManager(protocolString, pipelineConfiguration);
            httpManager.initialize(configuration, timeout);
            try {
                context.pushBaseURI(getUrlString());
                httpManager.sendRequest(createRequestDetails(), context);
                webdTransaction.stop(MonitoredTransaction.SUCCESSFUL, getUrlString());
            } catch (HTTPException e) {
                webdTransaction.stop(MonitoredTransaction.FAILED, getUrlString());
                fatalError(new XMLPipelineException(
                        exceptionLocalizer.format(
                                "http-request-process-failure",
                                urlString),
                        context.getCurrentLocator(),
                        e));
            } catch (RuntimeHttpException e) {
                webdTransaction.stop(MonitoredTransaction.FAILED, getUrlString());
                fatalError(new XMLPipelineException(
                        exceptionLocalizer.format(
                                "http-request-process-failure",
                                urlString),
                        context.getCurrentLocator(),
                        e));
            } catch (MalformedURLException e) {
                webdTransaction.stop(MonitoredTransaction.FAILED, getUrlString());
                fatalError(new XMLPipelineException(
                        "base uri attribute is malformed",
                        context.getCurrentLocator(),
                        e));
            } finally {
                context.popBaseURI();
            }
        }
    }

    /**
     * Creates a {@link HTTPResponseProcessor} instance that can be used to
     * process the response of a HTTP Request
     * @return a <code>HTTPResponseProcessor</code> instance
     */
    private HTTPResponseProcessor createHTTPResponseProcessor() {
        return new HTTPResponseProcessor() {
            // javadoc inherited
            public void processHTTPResponse(String redirectURL,
                                            InputStream responseStream,
                                            int statusCode,
                                            String contentType,
                                            String contentEncoding)
                    throws HTTPException {
                try {
                    processResponse(redirectURL,
                                    responseStream,
                                    statusCode,
                                    contentType,
                                    contentEncoding);
                } catch (SAXException e) {
                     XMLPipelineContext context = getPipelineContext();
                    // If the pipeline is already handling this error,
                     if (context.inErrorRecoveryMode()) {
                         // Then we assume that there is a containing try operation that
                         // needs help to avoid the pipeline crashing, so we need to
                         // mask the exception. So just continue on and hope the
                         // pipeline error handling will save us. Note that we cannot
                         // re-report it via fatalError or the flow control manager
                         // objects.
                         if (logger.isDebugEnabled()) {
                             logger.debug("WEBD encountered XML parsing exception " +
                                     "while error recovery is in progress, ignoring", e);
                         }
                     } else {
                         // Else we assume that there is no containing try operation.
                         // In this case just let the exception propogate up and cause
                         // the pipeline to die.
                         if (logger.isDebugEnabled()) {
                             logger.debug("WEBD encountered XML parsing exception " +
                                     "while no error recovery is in progress, rethrowing",
                                     e);
                         }
                         throw new HTTPException(e);
                     }

                } catch (IOException e) {
                    throw new HTTPException(e);
                }
            }
        };
    }

    /**
     * Determine if a url has a resource part.
     * @param url the url represented as a String
     * @return true if url has a resource part; false otherwise.
     */
    private boolean hasResource(String url) {
        List resourceSuffixes =
                configuration.getContextChangingResourceSuffixes();
        boolean hasResource = false;
        int size = resourceSuffixes.size();
        for (int i = 0; i < size && !hasResource; i++) {
            hasResource = url.endsWith((String) resourceSuffixes.get(i));
        }

        return hasResource;
    }

    /**
     * Create the XML URL rewrite process and return it. The process created is
     * based on whether or not URL redirection processing is necessary or not.
     * If it isn't required this method will return null.
     *
     * @return the newly created URL rewrite process, null if not required.
     */
    private XMLProcess createURLRewriterProcess(String redirectURL) {
        XMLProcess process = null;
        if (redirectURL != null && getFollowRedirects()) {
            URLRewriteProcessConfiguration urlRewriteConfig =
                    new URLRewriteProcessConfiguration();

            ConverterConfiguration convertConfig =
                    urlRewriteConfig.getConverterConfiguration();
            convertConfig.setTuples(TUPLES);

            URLPrefixRewriteManager rewriteManager =
                    urlRewriteConfig.getURLPrefixRewriteManager();

            rewriteManager.addRewritableURLPrefix(null, redirectURL,
                                                  URLPrefixRewriteOperation.ADD_PREFIX);

            process = new URLRewriteProcess(urlRewriteConfig);
        }
        return process;
    }

    /**
     * Get the request type of this operation process
     * @return requestType.
     */
    public HTTPRequestType getRequestType() {
        return requestType;
    }

    /**
     * Set the request type of this operation process.
     * @param requestType The request type.
     */
    public void setRequestType(HTTPRequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * Get the id for this request.
     * @return The id for this request.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id for this request.
     * @param id The id for this request.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the String representation of the url for this request.
     * @return The String representation of the url for this request.
     */
    public String getUrlString() {
        return urlString;
    }

    /**
     * Set the String representation of the url for this request.
     * @param urlString The String representation of the url for this request.
     */
    public void setUrlString(String urlString) {
        this.urlString = urlString;
        URLIntrospector urlHandler = new URLIntrospector(urlString);
        protocolString = urlHandler.getProtocol();
    }

    /**
     * Get the version of http this request should specify.
     * @return The http version.
     */
    public HTTPVersion getHTTPVersion() {
        return httpVersion;
    }

    /**
     * Set the version of http this request should specify.
     * @param httpVersion The http version.
     */
    public void setHTTPVersion(HTTPVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    /**
     * Set the value that determines if {@link HTTPRequestOperationProcess}
     * automatically and silently follows HTTP response 302 redirects.
     *
     * @param follows If "true" then the process automatically redirects.  If
     * it is "false" then the redirect is not followed and it is up to the
     * client to perform the redirect.
     */
    public void setFollowRedirects(String follows) {
        this.followsRedirect = Boolean.valueOf(follows);
    }

    /**
     * Get the value that determines if {@link HTTPRequestOperationProcess}
     * automatically and silently follows HTTP response 302 redirects.
     *
     * This first checks whether it was specified as an attribute on the
     * markup.  If not the value is retrieved from the configuration.
     *
     * @return true if the process automatically redirects otherwise false
     * perform the redirect.
     */
    public boolean getFollowRedirects() {
        boolean result = true;
        if (followsRedirect != null) {
            result = followsRedirect.booleanValue();
        } else if (configuration != null) {
            result = configuration.getFollowRedirects();
        }
        return result;
    }

    /**
     * Set the value of the flag that determines how errored content is
     * handled. If "true" the content is ignored.  If
     * {@link WebDriverConfiguration#setIgnoreContentEnabled} has been called
     * with a true parameter then the ignored content will be stored in an
     * ignored content buffer in the response.  If it is "false" the content is
     * passed through the pipeline.
     * @param ignore The value that determines whether errored content is
     * ignored or processed.
     */
    public void setIgnoreErroredContent(String ignore) {
        ignoreErroredContent = Boolean.valueOf(ignore);
    }

    /**
     * Get the value of the flag that determines whether errored content is
     * either ignored or processed. This value may have been set explicitly on
     * the markup attributes or on the {@link WebDriverConfiguration}.  A value
     * set on the attributes takes precedence over values in the configuration.
     * As such this method will return the markup value first.  If no value
     * was set in the markup it returns the configuration value.  The
     * configuration defines a default value of true.
     * @return true if errored content should be ignored, otherwise false.
     */
    public boolean getIgnoreErroredContent() {
        boolean result;
        if (ignoreErroredContent != null) {
            result = ignoreErroredContent.booleanValue();
        } else {
            result = configuration.getIgnoreErroredContent();
        }
        return result;
    }

    /**
     * Check whether content should be ignored given the specified http
     * response status code.  The method will return true (the content should
     * be ignored) if the following conditions are met:
     * <ul>
     * <li> The status code is not 200
     * <li> The status code is not 304 (conditional get)
     * <li> The status code is not a 3XX code (redirects are handled separately)
     * <li> The markup or process configuration states that we should ignore
     *      content where the status code indicates an error.
     * </ul>
     * @param statusCode The HTTP response status code.
     * @return true if the listed conditions are met, otherwise false.
     */
    protected boolean shouldIgnoreContent(int statusCode) {
        boolean result = false;
        if ((statusCode != 200 && (statusCode < 300 || statusCode > 399)
                && getIgnoreErroredContent()) || statusCode == NO_CHANGE_REPONSE) {
            result = true;
        }
        return result;
    }

    /**
     * Get the input stream factory.
     *
     * @return the input stream factory.
     */
    protected InputStreamFactory getInputStreamFactory() {
        if (inputStreamFactory == null) {
            inputStreamFactory = new InputStreamFactory();
        }
        return inputStreamFactory;
    }

    /**
     * Process the response ensuring that it is conditioned as appropriate and
     * passed to an associated script if one exists.  The content may be stored
     * in the ignoredContent buffer of the response if {@link
     * WebDriverConfiguration#setIgnoreContentEnabled} has been called with a
     * true parameter AND EITHER the ignoreContent flag is true OR the content
     * type is one that we have been asked to ignore.
     *
     * @param redirectURL
     *                   if a redirect was followed this parameter will
     *                   reference the URL that was followed. Will be null if a
     *                   redirect did not occur.
     * @param response   an InputStream that can be used to retrieve the actual
     *                   response body.
     * @param statusCode the status of the response.
     * @param contentType
     *                   the content type of the response.
     * @param contentEncoding
     *                   the content encoding of the response.
     */
    protected void processResponse(String redirectURL,
                                   InputStream response,
                                   int statusCode,
                                   String contentType,
                                   String contentEncoding)

            throws IOException, SAXException {

        // The user might have told us to ignore responses of this content type
        boolean ignoreContent = shouldIgnoreContent(statusCode);
        boolean ignoreThisContentType =
                ContentAction.IGNORE == retrieveContentAction(contentType);
        if (!ignoreContent && !ignoreThisContentType) {
            if (response != null) {
                // Consume the response.
                InputStream stream = getInputStreamFactory().getInputStream(
                        response, contentEncoding);

                // allows us to push back the "<" character when it is found
                PushbackInputStream pbis = new PushbackInputStream(stream);
                // if we find an opening demlimiter then assume there is some content
                if(findStartDelimiter('<', pbis)) {
                    consumeResponse(redirectURL, pbis, contentType);
                } // disregard content otherwise.
            }
        } else {
            // Ignore the response. If the response stream is null then there
            // is no response so do nothing. This normally occurs when a http
            // server returns a 304 status code.
            WebDriverResponse webdriver = retrieveWebDriverResponse();
            // we save away the ignored content if the ignore content flag is
            // true or the mime type is to be ignore and the configuration
            // indicates that we should save ignoreable content.
            boolean saveIgnoredContent =
                    ignoreContent ||
                    (ignoreThisContentType &&
                    configuration.isIgnoreContentEnabled());
            if (webdriver != null && saveIgnoredContent && response != null) {

                // Copy the stream to pass to the WebDriverResponse. This
                // copy is necessary because the response stream will be closed
                // after this method call making its content unavailable to
                // callers.
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int readBytes = response.read(buffer);
                while (readBytes != -1) {
                    out.write(buffer, 0, readBytes);
                    readBytes = response.read(buffer);
                }
                webdriver.setIgnoredContent(
                        new ByteArrayInputStream(out.toByteArray()));
            }
        }
    }

    /**
     * Reads the pbis until the specified delimiter is found. That delimiter is
     * then pushed back onto the stream and this method returns true. If the
     * delimiter is not found then this method returns false and the push back
     * input stream will be empty.
     *
     * @param delim the delimiter to search for
     * @param pbis the PushbackInputStream to search for the delimiter
     * @return true if the delimiter was found, false otherwise
     */
    private boolean findStartDelimiter(char delim, PushbackInputStream pbis) {

        boolean result = false;
        try{
            int c = pbis.read();
            while (c != -1 && !result) {
                if (c == delim) {
                    // push the read character back into the stream
                    result = true;
                    pbis.unread(c);
                } else {
                    c = pbis.read();
                }
            }
        }catch(IOException ioe) {
            result = false;
        }
        return result;
    }

    /**
     * Consume the response.
     * @param response The response.
     * @param contentType The content type of the response.
     * @throws IOException thrown by condition(..) call.
     * @throws SAXException thrown by condition(..) call.
     */
    private void consumeResponse(String redirectURL,
                                 InputStream response,
                                 String contentType)
            throws IOException, SAXException {
        XMLFilter responseFilter = retrieveResponseFilter(contentType);
        ContentConditioner conditioner =
                createContentConditioner(contentType, responseFilter);
        InputSource source = new InputSource(response);

        String charEncoding;

        // Get the charset from the content type first as that has priority
        // over any other setting.
        charEncoding = HeaderUtils.getCharSetFromContentType(contentType);

        // todo If no charset was found and the content type is text/html then
        // todo process the <meta> tags in the content to see whether there is
        // todo a content-type property that has a charset parameter.

        // If no character encoding could be found then use the default
        // configured one.
        if (charEncoding == null) {
            charEncoding = configuration.getCharacterEncoding();
        }

        // Set the character encoding in the input source.
        if (charEncoding != null) {
            source.setEncoding(charEncoding);
        }

        // We aim to have the following process(es) in the pipeline
        // WDProcess -> CUP -> [[URLRewriteProcess] | [Pipeline]] -> next
        // where URLRewriteProcess is chained to its own CUP setup in
        // its setPipeline() method.
        XMLProcess nextProcess = next;

        // If the configuration has the the 'responseContainsPipelineMarkup'
        // flag set, then we need to create a pipeline and insert it before
        // the next process (which could be the URLRewriteProcess or next
        if (configuration.getResponseContainsPipelineMarkup()) {
            XMLPipeline pipeline = getPipelineContext().getPipelineFactory().
                    createDynamicPipeline(getPipelineContext());
            XMLProcess pipelineProcess = pipeline.getPipelineProcess();
            pipelineProcess.setNextProcess(nextProcess);
            nextProcess = pipelineProcess;
        }

        if (redirectURL != null) {
            if (redirectURL.indexOf("://") == -1) {
                // We have been redirected to a relative url. In
                // this case there is no need to rewrite relative urls
                redirectURL = null;
            } else {
                // We need to set redirectURL to its prefex i.e.
                // remove the resource part if there is one.
                if (hasResource(redirectURL)) {
                    int index = redirectURL.lastIndexOf('/');
                    redirectURL = redirectURL.substring(0, index);
                }
            }
        }
        // We want the URLRewrite process to appear immediately after the
        // CUP so that
        XMLProcess urlRewriteProcess = createURLRewriterProcess(redirectURL);
        if (urlRewriteProcess != null) {
            // Only add the url rewriter process to the pipeline if
            // the process returned is not null. This process is chained
            // to the cup process so that its ouput will be returned
            // to the cup.
            urlRewriteProcess.setNextProcess(nextProcess);
            urlRewriteProcess.setPipeline(getPipeline());
            nextProcess = urlRewriteProcess;
        }
        // Always create the cup process. Note that this won't always be
        // the case. See the comments in URLRewriteProcess.
        XMLProcess cup = getPipelineContext().
                getPipelineFactory().createContextUpdatingProcess();
        cup.setPipeline(getPipeline());

        // Chain the cup to the next process (which may be the URLRewrite
        // process OR Pipeline OR next in that order).
        cup.setNextProcess(nextProcess);
        setNextProcess(cup);

        source.setSystemId(getUrlString());
        conditioner.condition(source, cup);
    }

    /**
     * Create the right kind of conditioner for the specified content type.
     * This is a factory method that may be moved to its own class in future
     * if/when conditioner creation becomes more complicated. There may need
     * to be something done about the XMLFilter also if in future the thing
     * that provides the conditioner does not know about the filter.
     * @param contentType The content type.
     * @param filter The XMLFilter for use by the created conditioner.
     * @return A conditioner that can condition the specified content type.
     */
    private ContentConditioner createContentConditioner(String contentType,
                                                        XMLFilter filter) {
        ContentConditioner conditioner = null;
        if (contentType != null) {
            // ContentConditioners should only ever be mapped against the short
            // form of content type strings.  As such we must extract the
            // content type up to the ';' separator.
            int pos = contentType.indexOf(';');
            if (pos != -1) {
                contentType = contentType.substring(0, pos);
            }
            WebDriverConditionerFactory factory =
                    configuration.getWebDriverConditionerFactory(contentType);
            if (factory != null) {
                conditioner = factory.createConditioner(filter);
            }

            if (conditioner == null) {
                conditioner = HeaderUtils.
                        createContentTypeConditioner(contentType, filter);
            }
        }
        return conditioner;
    }

    /**
     * <P>Retrieve the ContentAction for a specified contentType.</P>
     * <P>If the content type specified is null, then
     * {@link ContentAction#IGNORE} is returned.</P>
     * <P>If a content type is specified for which no ContentAction is found
     * then the following applies:
     * <ul>
     * <li>If only {@link ContentAction#CONSUME} are found then anything not
     * specified is IGNORED</li>
     * <li>IF only {@link ContentAction#IGNORE} are found then anything not
     * specified is CONSUMED</li>
     * <li>IF both are found then the content is CONSUMED either explicitly or
     * because it is not IGNORED</li>
     * </ul>
     * @param contentType The content type.
     * @return The ContentAction associated with the specified content type.
     */
    private ContentAction retrieveContentAction(String contentType) {
        ContentAction action = ContentAction.CONSUME;

        // If the content type is null, we should ignore the body - it is
        // likely that there is no body and we're engaged in a redirect or
        // similar process. If there is a body, then without a content type
        // there is no way to select the appropriate conditioner and other
        // configuration options.
        if (contentType == null) {
            action = ContentAction.IGNORE;
            if (logger.isDebugEnabled()) {
                String message = "Content type is null - ignoring content.";
                logger.debug(message);
            }
        } else {
            XMLPipelineContext context = getPipelineContext();
            Map contents = (Map) context.getProperty(Content.class);
            if (contents != null) {
                Content content = (Content) contents.get(contentType);
                if (content != null) {
                    action = content.getAction();
                } else {
                    // The user might have been quite general in their
                    // content type specification which means that we might
                    // have failed to match at this point. For example the
                    // user might have specified that we ignore "text/html"
                    // but the contentType param might be
                    // 'text/html; charset="utf8"'. We need to look for
                    // generalizations.
                    Iterator iterator = contents.keySet().iterator();
                    boolean ignoreExists = false;
                    boolean consumeExists = false;
                    boolean found = false;
                    while (iterator.hasNext() && !found) {
                        String key = (String) iterator.next();
                        content = (Content) contents.get(key);
                        action = content.getAction();
                        if (contentType.startsWith(key)) {
                            found = true;
                        } else {
                            // todo - this looks wrong, consumeExists is not used see 2004011501
                            if (action == ContentAction.CONSUME) {
                                consumeExists = true;
                            } else if (action == ContentAction.IGNORE) {
                                // Extra if clause rather than just the else
                                // in case ContentAction may have more options
                                // added later.
                                ignoreExists = true;
                            }
                        }
                    }
                    if (found == false) {
                        // todo - investigate since this looks like a bug see 2004011501
                        if (ignoreExists) {
                            action = ContentAction.CONSUME;
                        } else {
                            action = ContentAction.IGNORE;
                        }

                    }
                }
            }
        }
        return action;
    }

    /**
     * If there is a Script associated with this operation for the content type
     * of the response then retreive the XMLFilter for this Script. If no Script
     * XMLFilter can be found for this operation and content type then the given
     * then adapt the consumer XMLProcess into an XMLFilter and return this.
     * @param contentType The content type.
     */
    private XMLFilter retrieveResponseFilter(String contentType) {
        XMLPipelineContext context = getPipelineContext();
        Script script = (Script) context.getProperty(Script.class);
        XMLFilter responseFilter = null;
        if (script != null) {
            responseFilter =
                    configuration.retrieveScriptFilter(script.getRef(),
                                                       contentType);
        } else {
            responseFilter = new XMLFilterImpl();
        }

        return responseFilter;
    }

    /**
     * Get the real request associated with this HTTPRequestOperationProcess
     * if there is one.
     * @return The WebDriverRequest associated with this
     * HTTPRequestOperationProcess.
     */
    private WebDriverRequest retrieveWebDriverRequest() {
        XMLPipelineContext context = getPipelineContext();
        WebDriverAccessor accessor =
                (WebDriverAccessor) context.getProperty(WebDriverAccessor.class);
        WebDriverRequest request = null;
        if (accessor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No WebDriverAccessor available");
            }
        } else {
            request = accessor.getRequest(context);
        }

        return request;
    }

    /**
     * Get the real response associated with this HTTPRequestOperationProcess
     * if there is one.
     * @return The WebDriverResponse associated with this
     * HTTPRequestOperationProcess.
     */
    private WebDriverResponse retrieveWebDriverResponse() {
        XMLPipelineContext context = getPipelineContext();
        WebDriverAccessor accessor =
                (WebDriverAccessor) context.getProperty(WebDriverAccessor.class);
        WebDriverResponse response = null;
        if (accessor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No WebDriverAccessor available");
            }
        } else {
            response = accessor.getResponse(context, id);
        }

        return response;
    }

    /**
     * Factory method that creates a {@link RequestDetails} instance
     * @return
     */
    private RequestDetails createRequestDetails() {
        return new RequestDetails(
                getUrlString(),
                getPipelineContext().getCurrentBaseURI().toExternalForm(),
                getRequestType(),
                retrieveWebDriverRequest(),
                retrieveWebDriverResponse(),
                getFollowRedirects(),
                getHTTPVersion(),
                (HTTPRequestPreprocessor)
                getPipelineContext().getProperty(
                        HTTPRequestPreprocessor.class),
                (HTTPResponsePreprocessor)
                getPipelineContext().getProperty(
                        HTTPResponsePreprocessor.class),
                createHTTPResponseProcessor()

        );
    }

    /**
     * Allows the timeout to be specified.
     *
     * @param timeout the timeout.
     */
    public void setTimeout(Period timeout) {
        this.timeout = timeout;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 09-Jun-05	8708/3	matthew	VBM:2005060713 remove infinite loop

 09-Jun-05	8708/1	matthew	VBM:2005060713 scan input stream for body content before sending to sax parser

 09-Jun-05	8706/1	matthew	VBM:2005060713 scan input stream for body content before sending to sax parser

 22-Apr-05	7828/1	byron	VBM:2005040611 HTTP code 302 not followed to new location in transcoder

 22-Apr-05	7801/3	byron	VBM:2005040611 HTTP code 302 not followed to new location in transcoder

 22-Apr-05	7801/1	byron	VBM:2005040611 HTTP code 302 not followed to new location in transcoder

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 04-Jan-05	6569/5	pduffin	VBM:2004123101 Fixed subtle bug introduced by removing the break.

 04-Jan-05	6569/3	pduffin	VBM:2004123101 Removed break from loop as our coding standards do not allow them except in switches. Added java doc to private test case helper method.

 31-Dec-04	6569/1	pduffin	VBM:2004123101 Modified webd to use the character set specified in the Content-Type header if available, otherwise falling back to previous behaviour

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	6302/3	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 29-Nov-04	6302/1	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 11-Nov-04	6170/1	doug	VBM:2004102516 Synchronized HTTP Requests due to Jigsaw deadlock

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Sep-04	890/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 23-Sep-04	888/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 10-Sep-04	872/1	matthew	VBM:2004083107 add JSessionID proxy handling

 08-Sep-04	854/1	matthew	VBM:2004083107 allow httpProcessor to obtain pre and post request processors from Pipline context

 08-Sep-04	869/1	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	865/2	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	858/1	doug	VBM:2004090610 Added preprocessing of response capability

 01-Sep-04	740/3	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 16-Aug-04	822/3	adrianj	VBM:2004081214 Handle null content-type and empty response (rework)

 16-Aug-04	822/1	adrianj	VBM:2004081214 Handle null content-type and empty response

 20-Jul-04	786/1	claire	VBM:2004071304 Refactor web driver to support HTTPS

 12-Jul-04	751/11	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/6	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/4	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 09-Jul-04	769/2	doug	VBM:2004070502 Improved integration tests for the Web Driver

 24-Jun-04	759/1	matthew	VBM:2004061101 small change to allow setIgnoreContentEnabled to work with 304 status responses

 16-Jun-04	753/1	byron	VBM:2004061401 Refactor pipeline WEBDriver to optionally process response as pipeline markup

 03-Jun-04	736/10	allan	VBM:2004060201 Merged with 2004060101

 02-Jun-04	736/7	allan	VBM:2004060201 Add xfform to list of rewritten urls

 02-Jun-04	736/5	allan	VBM:2004060201 Add xfform to list of rewritten urls

 02-Jun-04	736/3	allan	VBM:2004060201 Fixes for dci redirection and remapping

 02-Jun-04	738/1	matthew	VBM:2004060101 Changed HTTPRequestOperationProcess so that it handles 304 status codes correctly

 01-Jun-04	725/4	allan	VBM:2004052106 Allow redirect urls to be remapped.

 01-Jun-04	720/3	byron	VBM:2004052802 DCI: Have web driver track cookies set on redirects

 01-Jun-04	720/1	byron	VBM:2004052802 DCI: Have web driver track cookies set on redirects

 28-May-04	715/7	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 28-May-04	715/5	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 27-May-04	715/3	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 27-May-04	715/1	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 31-Mar-04	656/1	adrian	VBM:2004031602 Updated pipeline with DSB enhancement requests

 26-Mar-04	644/1	adrian	VBM:2004031907 Add support to WebDriver to provide a character encoding

 25-Mar-04	640/1	adrian	VBM:2004031906 Added mechanism to allow custom conditioners to be used in WebDriver

 24-Mar-04	629/6	adrian	VBM:2004031901 enable web driver to ignore errored content

 23-Mar-04	606/6	adrian	VBM:2004031609 Fixed merge problems

 18-Mar-04	606/1	adrian	VBM:2004031609 Update web driver to make response to redirects configurable

 22-Mar-04	619/1	adrian	VBM:2004031804 Set header and cookie values correctly in web driver

 23-Mar-04	631/1	allan	VBM:2004032205 Patch performance fixes from Pipeline/MCS 3.0GA

 22-Mar-04	626/1	allan	VBM:2004032205 Pipeline performance enhancements.

 10-Feb-04	525/1	adrian	VBM:2004011902 fixed bug setting the baseuri on included content within template bindings

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 19-Jan-04	537/1	claire	VBM:2004011919 Enhanced processResponse test cases and fixed byte array sizing

 19-Jan-04	514/5	claire	VBM:2004011514 processResponse updated and test cases changed

 16-Jan-04	514/1	claire	VBM:2004011514 WebResponseDriver updated with appropriate content

 16-Jan-04	518/1	claire	VBM:2004011515 Removed RuntimeException with null cookie expiry date

 15-Jan-04	508/3	claire	VBM:2004011501 Improved the behaviour of retrieveContentType based on content preferences

 15-Jan-04	508/1	claire	VBM:2004011501 Improved the behaviour of retrieveContentType based on content preferences

 19-Dec-03	489/1	doug	VBM:2003120807 Ensured that our current xml processes are recoverable when inside a try op

 20-Aug-03	294/4	allan	VBM:2003070709 Update log and error message to improve clarity.

 06-Aug-03	301/3	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 05-Aug-03	294/1	allan	VBM:2003070709 Ensure nested anchor filter is before the user filter in the chain

 04-Aug-03	217/29	allan	VBM:2003071702 Tidied two lines and fix merge conflicts

 04-Aug-03	217/27	allan	VBM:2003071702 Fixed missing exception in logger.error call.

 04-Aug-03	217/25	allan	VBM:2003071702 Filter nested anchors.

 01-Aug-03	217/23	allan	VBM:2003071702 Added more tests. Fixed a couple of bugs

 31-Jul-03	238/6	byron	VBM:2003072309 Create the adapter process for parent task v4

 31-Jul-03	217/21	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/17	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build

 31-Jul-03	217/15	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 30-Jul-03	238/3	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit v3

 30-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit

 30-Jul-03	217/11	allan	VBM:2003071702 Separated WebDriverAccessor from configuration. Updated type safe enums. Updated conditioners

 29-Jul-03	217/9	allan	VBM:2003071702 Code complete. Not tested

 28-Jul-03	217/7	allan	VBM:2003071702 Intermediate group level changes

 28-Jul-03	217/5	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 ===========================================================================
*/