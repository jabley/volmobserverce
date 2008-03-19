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
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.CacheScopeConstant;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.cache.CacheManager;
import com.volantis.mcs.runtime.configuration.RenderedPageCacheConfiguration;
import com.volantis.shared.content.ContentStyle;
import com.volantis.synergetics.cache.FutureResultFactory;
import com.volantis.synergetics.cache.GenericCache;
import com.volantis.synergetics.cache.GenericCacheFactory;
import com.volantis.synergetics.cache.ReadThroughFutureResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;
import com.volantis.xml.utilities.sax.TeeContentHandler;
import com.volantis.xml.xml.serialize.XMLSerializer;
import org.apache.regexp.RE;
import org.apache.regexp.REProgram;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * A {@link XDIMERequestProcessor} that caches the rendered page keyed on the
 * hascode of the XDIME markup. If the cache is operating in SAFE mode any
 * pipeline markup in the XDIME will be evaluated before the hashcode is
 * evaluated. If operating in optimistic mode then the pipeline markup will not
 * be evaluated.
 */
public class CachingXDIMERequestProcessor extends SimpleXDIMERequestProcessor {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    CachingXDIMERequestProcessor.class);

    /**
     * The exception localizer used by this class.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    CachingXDIMERequestProcessor.class);

    /**
     * The default session ID name.
     */
    private static final String DEFAULT_SESSION_ID_NAME = "jsessionid";

    /**
     * A regular expression for matching a session identifier. Before use,
     * the session name will be prepended.
     * <p>This is taken from a subset of what is legal in path parameters
     * according to RFC 2396, with some special characters that would not
     * be legal as parameters on a URL removed. This should almost certainly
     * be extracted as a separate configurable value in the future.</p>
     *
     * @todo better Make this configurable through mcs-config.xml
     */
    private static final String RE_SESSION_IDENTIFIER =
            "=([0-9!A-Za-z-_.~\\*\\(\\)\\+]*)";

    /**
     * The placeholder for the replaced session identifiers.
     */
    private static final String SESSION_PLACEHOLDER = "VOL_MCS_SESSID";

    /**
     * A regular expression for matching a canvas tag (with or without a
     * namespace) and extracting the attributes.
     */
    private static final String RE_FIND_CANVAS =
            "<\\s*([A-Za-z]+\\s*:)?\\s*canvas\\s*([^>]*)>";

    /**
     * A regular expression for matching an attribute indicating that caching
     * is not valid for the given canvas.
     */
    private static final String RE_FIND_SCOPE =
            "cacheScope\\s*=\\s*\"(none|safe|optimistic)\"";

    /**
     * A regular expression for matching a meta tag (with or without a
     * namespace) and extracting the attributes.
     */
    private static final String RE_FIND_META_CACHE_SCOPE =
            "<\\s*([A-Za-z]+\\s*:)?\\s*meta\\s*" +
            "property=\"mcs:cache-scope\"\\s*>(none|safe|optimistic)";


    /**
     * A reusable regular expression program for finding the canvas tag.
     */
    private REProgram findCanvas;

    /**
     * A reusable regular expression program for finding a no-caching
     * attribute.
     */
    private REProgram findCacheScope;

    
    /**
     * A reusable regular expression program for finding a no-caching
     * attribute.
     */
    private REProgram findMetaCacheScope;
    
    /**
     * A reusable regular expression program for finding the session ID.
     */
    private REProgram findSession;

    /**
     * This is the cache that will be used to cache rendered pages
     */
    private GenericCache renderedPageCache;

    /**
     * Configuration for the rendered page cache.
     */
    private RenderedPageCacheConfiguration cacheConfig;

    /**
     * The application
     */
    private MarinerServletApplication application;

    /**
     * Initializes a new <code>CachingXDIMERequestProcessor</code> instance
     * @param servletContext the servlet context
     * @param xdimeMIMETypes list of mime type for XDIME documents
     * @param cacheConfig the cache configuration
     * @param sessionName the indentifier used by the app server to add session
     * parameters to URLs
     * @throws ServletException if an error occurs
     */
    public CachingXDIMERequestProcessor(
            ServletContext servletContext,
            List xdimeMIMETypes,
            RenderedPageCacheConfiguration cacheConfig,
            String sessionName)
            throws ServletException {
        super(servletContext, xdimeMIMETypes);
        
        this.cacheConfig = cacheConfig;

        FutureResultFactory resultFactory =
                    new FutureResultFactory() {
                        // javadoc inherited
                        protected ReadThroughFutureResult
                                    createCustomFutureResult(Object key,
                                                             int timeToLive) {
                            return new PageCacheFutureResult(key,
                                                             timeToLive);
                        }
                    };

        renderedPageCache = GenericCacheFactory.createCache(
                    resultFactory,
                    cacheConfig.getStrategy(),
                    cacheConfig.getMaxEntries().intValue(),
                    cacheConfig.getTimeout().intValue());

        initApplication(servletContext);

        initVolantis();

        // Initialise reusable regular expression programs.
        if (sessionName == null) {
            sessionName = DEFAULT_SESSION_ID_NAME;
        }
        findSession = createSessionRegExp(sessionName);
        findCanvas = new RE(RE_FIND_CANVAS).getProgram();
        findCacheScope = new RE(RE_FIND_SCOPE).getProgram();
        findMetaCacheScope = new RE(RE_FIND_META_CACHE_SCOPE).getProgram();
    }

    /**
     * initialise Volantis by setting the RenderedPageCacheManager to a method
     * which clears the renderedPageCache
     */
    protected void initVolantis() {
        Volantis volantis = ApplicationInternals.getVolantisBean(application);
        // allow the cache to be flushed from the volantis bean.
        volantis.setRenderedPageCacheManager(new CacheManager() {
            public void flushCache() {
                renderedPageCache.clear();
            }
        });
    }

    /**
     * initialise the mariner servlet application
     * @param servletContext
     * @throws ServletException
     */
    protected void initApplication(ServletContext servletContext)
            throws ServletException {
        application = MarinerServletApplication.getInstance(servletContext);
    }

    /**
     * Initializes a new <code>CachingXDIMERequestProcessor</code> instance
     * which uses the provided requestProcessorHelper to delegate functionality
     * instead of the defautl helper. This method is intended to be used for
     * testing
     * @param servletContext the servlet context
     * @param xdimeMIMETypes list of mime type for XDIME documents
     * @param cacheConfig the cache configuration
     * @param sessionName the indentifier used by the app server to add session
     * parameters to URLs
     * @param requestProcessorHelper
     * @throws ServletException if an error occurs
     */
    public CachingXDIMERequestProcessor(
            ServletContext servletContext,
            List xdimeMIMETypes,
            RenderedPageCacheConfiguration cacheConfig,
            String sessionName, 
            XDIMERequestProcessorHelper requestProcessorHelper)
            throws ServletException {
        this(servletContext, xdimeMIMETypes, cacheConfig, sessionName);
        xdimeRequestProcessorHelper = requestProcessorHelper;
    }

    // javadoc inherited
    public void processXDIME(MarinerServletRequestContext marinerRequestContext,
                             CachedContent xdimeContent,
                             String characterSet)
            throws IOException, ServletException, SAXException {

        String xdimeString;
        xdimeString = xdimeRequestProcessorHelper.getXDIMEString(xdimeContent);

        // Caching takes place if this page does not explicitly opt out or the
        // device is a wmlc device
        RenderedPageCacheScope cacheScope = getPageCacheScope(xdimeString);

        if (cacheScope == RenderedPageCacheScope.NONE ||
            willGenerateBinaryOutput(marinerRequestContext)) {
            // we are not caching so just delegate to the
            // default strategy
            if (logger.isDebugEnabled()) {
                logger.debug("Page is not cacheable");
            }
            super.processXDIME(marinerRequestContext,
                               xdimeContent,
                               characterSet);
        } else {
            processCachedXDIME(xdimeContent, xdimeString, characterSet, cacheScope,
                    marinerRequestContext);

        }
    }

    

    /**
     * Method that either returns the rendered page from the cache. Or if it
     * has not been cached calls into MCS to render that page and adds the
     * rendered page to the cache.
     * @param xdimeContent
     * @param xdimeString the XDIME that is being rendered
     * @param characterSet the character encoding
     * @param cacheScope controls how the cache key is generated
     * @param marinerRequestContext the request context
     * @throws SAXException if an error occurs
     * @throws IOException if an error occurs
     * @throws ServletException if an error occurs
     */
    private void processCachedXDIME(CachedContent xdimeContent,
            String xdimeString, String characterSet,
                                    RenderedPageCacheScope cacheScope,
                                    MarinerServletRequestContext marinerRequestContext)
            throws SAXException, IOException, ServletException {

        String sessionId;
        // We may have cached this page so we need to determine
        // if it is in the cache.
        if (logger.isDebugEnabled()) {
            logger.debug("Page is cacheable");
        }

        // Identify and strip out the session ID if possible (the
        // stripped version of the XDIME will be used for
        // generating the cache key, and the non-stripped version
        // will be processed).
        RE stripXDIMERE = new RE(findSession);
        String strippedXDIME =
                    stripXDIMERE.subst(xdimeString, SESSION_PLACEHOLDER);

        sessionId = stripXDIMERE.getParen(1);
        byte[] sessionIdBytes = null;

        if (sessionId != null) {
            sessionIdBytes = sessionId.getBytes(characterSet);
        }
        // if the cache scope is secure then we need to be process any
        // pipeline markup in the XDIME.
        PipelineRecording recording = null;
        if (cacheScope == RenderedPageCacheScope.SAFE) {
            PipelineRecorder recorder = XMLPipelineFactory.getDefaultInstance()
                    .createPipelineRecorder();
            recorder.startRecording();
            strippedXDIME = processPipelineMarkupInXDIME(
                        strippedXDIME,
                        marinerRequestContext,
                        recorder);
            recording = recorder.stopRecording();
        }

        // get hold of the cache entry object. If the page has not
        // been cached this method will add an empty cache entry.
        RenderedPageWrapper result =
                    getPageFromCache(strippedXDIME,
                                     marinerRequestContext,
                                     characterSet);
        // flag that will be used to minimize the length of time
        // that we hold the lock for. We only need to lock if the
        // page is not cached and we have to populate the cache.
        boolean handledResponse = false;
        // lock the result and check to see if it is cached.
        synchronized (result) {
            if (result.getPage() == null ||
                    result.getPageValidForCaching() ==
                        CacheScopeConstant.CAN_NOT_CACHE_PAGE) {

                if (logger.isDebugEnabled()) {
                    if (result.getPage() == null) {
                        logger.debug("Cache miss: " +
                                 "adding rendered page to cache");
                    } else {
                        logger.debug("Page marked as not cachable");
                    }
                }

                //a copy of the original repsonse must be kept so that the
                //cached result can be copied to it. The response can not be
                //retrived from the marinerRequestContext because when the
                //release is called in the finally clause the response is set
                //to null
                HttpServletResponse httpResponse =
                        marinerRequestContext.getHttpResponse();

                HttpServletRequest httpRequest =
                        marinerRequestContext.getHttpRequest();

                // Create a cached response wrapper to pass to MCS.
                // This allows us to retrieve a copy of the results
                // sent back to the client.
                CachingResponseWrapper cachingResponse =
                            new CachingResponseWrapper(httpResponse);

                // this is unfortunate but we need to create a new
                // RequestContext that writes to CachedResposneWrapper when
                // populating the response
                MarinerServletRequestContext cachedRequestContext = null;

                try {
                    cachedRequestContext = xdimeRequestProcessorHelper.
                        createServletRequestContext(
                            marinerRequestContext.getServletContext(),
                            marinerRequestContext.getHttpRequest(),
                            cachingResponse);

                    if (recording != null) {
                        // We have already parsed the XDIME in order
                        // to process the pipeline markup and have the
                        // SAX events recorded. In order to avoid the
                        // overhead of another parse we will use the
                        // recorder to replay these events into MCS.
                        xdimeRequestProcessorHelper.
                                replayEventsIntoMCS(recording,
                                            cachedRequestContext);
                    } else {
                        // we haven't already parsed the XDIME so do it
                        // now, calling into MCS to convert to device
                        // specific markup.
                        super.processXDIME(cachedRequestContext,
                                           xdimeContent,
                                           characterSet);

                    }
                } catch (MarinerContextException e) {
                    logger.error("mariner-context-exception", e);
                    throw new ServletException(
                            exceptionLocalizer.format(
                                    "mariner-context-exception"),
                            e);
                } finally {
                    if (marinerRequestContext != null) {
                        marinerRequestContext.release();
                    }
                }

                processCacheScopeState(result, httpRequest);


                handledResponse = true;

                // Split the result where the session ID should be
                // inserted, and store it in the rendered page wrapper.
                byte[] cacheContent;
                ContentStyle contentStyle = cachingResponse.getContentStyle();
                if (contentStyle == ContentStyle.TEXT) {
                    // content is text, convert to bytes for storage
                    // NOTE: this is not 100% efficient but we don't care as
                    // the caching is broken at the moment anyway.
                    cacheContent = new String(cachingResponse.getAsCharArray()).
                            getBytes(characterSet);

                } else if (contentStyle == ContentStyle.BINARY) {
                    cacheContent = cachingResponse.getAsByteArray();
                } else {
                    throw new IllegalStateException("no content available");
                }
                ByteArrayTokenizer tokenizer = new ByteArrayTokenizer(
                        cacheContent, sessionIdBytes);
                byte[][] wrappedBytes = tokenizer.getAllTokens();
                final CachedRenderedPage cachedPage = new CachedRenderedPage(
                        wrappedBytes, cachingResponse.getContentType());

                result.setPage(cachedPage);

                // now that we have cached the page, copy the cached response
                // content to the real response for this page.
                cachingResponse.writeTo(httpResponse);
            }
        }

        if (!handledResponse) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cache hit: outputting rendered page");
            }
            outputCachedResult(result.getPage(),
                               sessionIdBytes,
                               marinerRequestContext.getResponse());
        }
    }

    /**
     * retrieve the cache scope state from the request and copy it into the
     * RenderedPageWrapper - result.
     * @param result
     * @param httpRequest
     */
    private void processCacheScopeState(RenderedPageWrapper result,
                                        HttpServletRequest httpRequest) {
        CacheScopeConstant cacheScope =
                 (CacheScopeConstant) httpRequest.
                    getAttribute(CacheScopeConstant.CACHE_SCOPE_ATTRIBUTE);
        if (cacheScope != null) {
            result.setPageValidForCaching(cacheScope);
        }
    }

    /**
     * Obtains the object that is cached
     * @param xdime the XDIME whose rendered document may be in the cache
     * @param requestContext the request context
     * @param characterSet the character encoding
     * @return a RenderedPageWrapper object
     */
    private RenderedPageWrapper getPageFromCache(
                                  String xdime,
                                  MarinerServletRequestContext requestContext,
                                  String characterSet) {
        // Generate the key for carrying out cache lookups
        HttpServletRequest request = requestContext.getHttpRequest();
        String requestURI =request.getRequestURI();
        String fragState = request.getParameter("vfrag");
        String deviceName = requestContext.getDeviceName();
        RenderedPageCacheKey key =
                new RenderedPageCacheKey(xdime,
                                         deviceName,
                                         characterSet,
                                         requestURI,
                                         fragState == null ? "" : fragState);

        // not contain an actual CachedRenderedPage instance. This extra
        // level of indirection is required because the cache can not
        // automatically retrieve the resource to be stored in the cache
        // based on its key because we use the hash of the input document
        // rather than the document itself. It is necessary to synchronize
        // on the wrapped object and either retrieve its contents or
        // populate it as appropriate. This should ensure that only one
        // thread ever runs the XDIME through MCS.
        return (RenderedPageWrapper) renderedPageCache.get(key);
    }

    /**
     * Evalutes any pipeline markup in the given XDIME
     * @param xdime the XDIME
     * @param requestContext a request context
     * @param recorder an event recorder that will contain the SAXEvents
     * for the evaluated XDIME (means we do not need to parse the document
     * again if the rendered page is not in the cache).
     * @return the XDIME document with pipeline tags evaluated.
     * @throws org.xml.sax.SAXException if an error occurs
     * @throws java.io.IOException if an error occurs
     * @throws javax.servlet.ServletException if an error occurs
     */
    private String processPipelineMarkupInXDIME(
            final String xdime,
            MarinerServletRequestContext requestContext,
            PipelineRecorder recorder)
            throws SAXException, IOException, ServletException {

        StringWriter writer = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(writer, null);
        // Create a ContentHandler the Tee's the events to both a
        // SAXEventRecorder and an XMLSerializer. The serializer will
        // serialize the XDIME after any pipeline markup has been
        // processed. The SAXEventRecorder will allow the SAX events
        // for this post pipeline XDIME to be replayed into MCS later.
        TeeContentHandler teeContentHandler = new TeeContentHandler(
                recorder.getRecordingHandler(), serializer);
        xdimeRequestProcessorHelper.
                parseXDIME(requestContext, new StringCachedContent(xdime),
                   teeContentHandler);
        // Return the post pipeline XDIME
        return writer.toString();
    }

    /**
     * Writes a cached response back to the client, substituting in the
     * session ID where necessary.
     *
     * @param result    The cached page to write out
     * @param sessionId The session ID, converted to bytes using the
     *                  appropriate character encoding
     * @param resp      The response to which the cached response should be
     *                  written
     * @throws java.io.IOException if there is an error writing to the response
     */
    private void outputCachedResult(CachedRenderedPage result,
                                    byte[] sessionId,
                                    ServletResponse resp)
            throws IOException {
        final String contentType = result.getContentType();
        if (contentType != null) {
            resp.setContentType(contentType);
        }
        OutputStream out = resp.getOutputStream();
        byte[][] cached = result.getBodyDataParts();
        for (int i = 0; i < cached.length; i++) {
            out.write(cached[i]);
            if (sessionId != null && i < (cached.length - 1)) {
                out.write(sessionId);
            }
        }
    }

    /**
     * Determine tha cache scope
     * @param page the XDIME page
     */
    private RenderedPageCacheScope getPageCacheScope(String page) {
        RenderedPageCacheScope scope = null;
        RE regExpression = new RE(findCanvas);
        if (regExpression.match(page)) {
            String canvasAttributes = regExpression.getParen(2);
            regExpression.setProgram(findCacheScope);
            if (regExpression.match(canvasAttributes)) {
                scope = RenderedPageCacheScope.get(regExpression.getParen(1));
            }
        }
        if (scope == null) {
            // lets look for an XDIME 2 cache directive
            regExpression.setProgram(findMetaCacheScope);
            if (regExpression.match(page)) {
                scope = RenderedPageCacheScope.get(regExpression.getParen(2));
            }
        }
        if (scope == null) {
            // Canvas element did not specify a cache scope then we select the
            // default from the configuration. If that is null then default to
            //  none
            RenderedPageCacheScope defaultScope =
                    RenderedPageCacheScope.get(cacheConfig.getDefaultScope());
            scope = (defaultScope != null) ? defaultScope
                                           : RenderedPageCacheScope.NONE;
        }
        return scope;
    }

    /**
     * Determines whether MCS is going to generate binary output for this
     * request.
     * <p>
     * If it is we do not attempt to cache anything.
     *
     * @param context the request context
     * @return true iff MCS will generate binary output for this request.
     */
    protected boolean willGenerateBinaryOutput(
            MarinerServletRequestContext context) {

        final ApplicationContext applicationContext =
                ContextInternals.getApplicationContext(context);

        return applicationContext.getProtocol().getOutputStyle() ==
                ContentStyle.BINARY;
    }

    /**
     * Create a compiled regular expression program.
     *
     * @param sessionName The name of the session
     * @return A compiled regular expression program that matches instances of
     *         the session with the specified name.
     */
    private REProgram createSessionRegExp(String sessionName) {
        String expr = sessionName + RE_SESSION_IDENTIFIER;
        return new RE(expr).getProgram();
    }

    /**
     * A ReadThroughFutureResult implementation that always creates a
     * rendered page wrapper as its object. This is required because the
     * actual rendered page can not be created from the key since we are
     * using a lossy conversion from the input XDIME to the key value.
     */
    private class PageCacheFutureResult extends ReadThroughFutureResult {

        /**
         * Pass constructor parameters through to parent class.
         *
         * @param key        The key for this future result
         * @param timeToLive The time for this future result to live
         */
        private PageCacheFutureResult(Object key, int timeToLive) {
            super(key, timeToLive);
        }

        // Javadoc inherited
        protected Object performUpdate(Object key) throws Exception {
            return new RenderedPageWrapper();
        }
    }

    private static class StringCachedContent implements CachedContent {
        private final String xdime;

        public StringCachedContent(String content) {
            this.xdime = content;
        }

        public ContentStyle getContentStyle() {
            return ContentStyle.TEXT;
        }

        public byte[] getAsByteArray() throws IOException {
            throw new IllegalStateException();
        }

        public char[] getAsCharArray() throws IOException {
            return xdime.toCharArray();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/4	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10727/3	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 08-Dec-05	10677/6	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 08-Dec-05	10677/4	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 25-May-05	7762/4	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 20-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 ===========================================================================
*/
