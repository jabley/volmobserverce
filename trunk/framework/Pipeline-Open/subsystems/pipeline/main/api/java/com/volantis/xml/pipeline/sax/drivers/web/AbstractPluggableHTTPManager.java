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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.net.proxy.ProxyFactory;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.url.URLPrefixRewriteManager;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Abstract implementation of the {@link PluggableHTTPManager} interface.
 */
public abstract class AbstractPluggableHTTPManager
        implements PluggableHTTPManager {

    /**
     * The name of the location header in redirect responses.
     */
    protected static final String LOCATION_HEADER = "location";

    /**
     * The name of the HTTP Header that contains the cookies in a response.
     */
    protected static String COOKIE_HEADER = "Set-Cookie";

    /**
     * The name of the HTTP Header that specified the content type of a
     * response.
     */
    private static String CONTENT_TYPE = "Content-Type";

    /**
     * The name of the HTTP Header that specified the content encoding of a
     * response.
     */
    private static final String CONTENT_ENCODING = "Content-Encoding";

    /**
     * The name of the HTTP Header that contains the date in a response.
     */
    protected static String DATE_HEADER = "Date";

    /**
     * The maximum number of redirects to follow. redirect cycles are detected
     * by other means.
     */
    private static final int MAX_REDIRECTS = 30;

    /**
     * WebDriverConfiguration instance
     */
    protected WebDriverConfiguration configuration;

    /**
     * The timeout, in milliseconds, to apply to connections. If negative or
     * zero, no timeout is applied.
     */
    protected Period timeout = Period.INDEFINITELY;

    // javadoc inherited
    public void initialize(WebDriverConfiguration configuration,
                           Period timeout) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration cannot be null");
        }

        this.configuration = configuration;
        this.timeout = timeout;
    }

    /**
     * Returns a {@link ProxyManager} instance if the configuration indicates
     * that the request should be made via a proxy.
     *
     * @param context the XML pipeline context.
     * @return A {@link ProxyManager} instance or null if a proxy is not
     *         configured.
     * @throws HTTPException if an error occurs.
     */
    protected ProxyManager retrieveProxyManager(XMLPipelineContext context)
            throws HTTPException {

        // use system properties if available otherwise use proxy settings
        // from configuration file.
        ProxyFactory factory = ProxyFactory.getDefaultInstance();
        ProxyManager manager = factory.getSystemProxyManager();
        if (manager == null) {
            // First see if a proxy has been specified.
            String proxyRef = (String) context.getProperty(ProxyManager.class);
            if (proxyRef != null) {
                // Now look for the referenced proxy in the configuration.
                manager = configuration.getProxyManager(proxyRef);
                if (manager == null) {
                    throw new HTTPException("Proxy \"" + proxyRef +
                                            "\" is not defined in the " +
                                            "WebDriverConfiguration");
                }
            }
        }

        return manager;
    }

    // javadoc inherited
    public int sendRequest(RequestDetails request,
                           XMLPipelineContext xmlPipelineContext)
            throws HTTPException {

        int statusCode;
        HTTPRequestExecutor requestExecutor = null;
        try {

            // Whenever a request is required to  follow redirects  we should
            // look for cookie headers in the redirect response (i.e. the
            // response that is telling us to redirect). If there are any
            // cookies we should store these so that when we eventually get the
            // real response (when we stop being redirected) we can add the
            // cookies we have found on redirects to the response we send to
            // the client.
            HTTPMessageEntities cookieJar =
                    HTTPFactory.getDefaultInstance().
                    createHTTPMessageEntities();

            // If redirection occurs this will reference the location of the
            // redirect URL
            String urlToFollow = request.getUrl();
            ProxyManager proxyManager = retrieveProxyManager(xmlPipelineContext);
            HTTPResponseAccessor responseAccessor;
            // used to store urls that I've been redirected too. This
            // can be probed to check for cyclic redirects.
            Set redirectCycles = new HashSet();
            do {
                // obtain the request parameters
                HTTPMessageEntities parameters = HTTPManagerUtilities.
                        retrieveRequestParameters(request.getRequest(),
                                xmlPipelineContext);
                // obtain the request headers
                HTTPMessageEntities headers = HTTPManagerUtilities.
                        retrieveRequestHeaders(request.getRequest(),
                                xmlPipelineContext);
                // obtain the request cookies
                HTTPMessageEntities cookies = HTTPManagerUtilities.
                            retrieveRequestCookies(request.getRequest(),
                                xmlPipelineContext);

                HTTPRequestPreprocessor requestPreprocessor =
                        request.getRequestPreprocessor();
                if (requestPreprocessor != null) {
                    urlToFollow = requestPreprocessor.preprocessRequest(
                            headers, cookies, parameters, urlToFollow);
                }

                // need to create the appropriate RequestExecutor
                requestExecutor = createHTTPRequestExecutor(urlToFollow,
                    request.getRequestType(), request.getVersion(),
                    proxyManager, xmlPipelineContext);

                // Add the request parameters to the executor for the original
                // request only, not any subsequent redirections. 
                // it is up to the executor to determine how to process them
                // ie add them as headers to POST or as query strings to GET
                if (redirectCycles.isEmpty()) {
                    for (Iterator i = parameters.iterator(); i.hasNext();) {
                        requestExecutor.addRequestParameter((RequestParameter) i.next());
                    }
                }

                // add the cookies to the request
                for (Iterator i = cookies.iterator(); i.hasNext();) {
                    requestExecutor.addRequestCookie((Cookie) i.next());
                }
                // add the headers to the request
                for (Iterator i = headers.iterator(); i.hasNext();) {
                    requestExecutor.addRequestHeader((Header) i.next());
                }

                // nexecute the request
                responseAccessor = requestExecutor.execute();

                HTTPResponsePreprocessor httpHeaderPreprocessor =
                        request.getResponsePreprocessor();
                if (httpHeaderPreprocessor != null) {
                    httpHeaderPreprocessor.preprocessResponse(
                            responseAccessor.getHeaders(),
                            responseAccessor.getCookies(),
                            responseAccessor.getStatusCode()
                    );
                }

                if (isRedirect(responseAccessor.getStatusCode())) {

                    // A redirect is required so copy the cookies out of the
                    // response so that they can be added to the final resposne
                    extractCookies(cookieJar, responseAccessor);

                    // extract the redirectURL from the response Header
                    String redirectURL = retrieveHeader(LOCATION_HEADER,
                            responseAccessor).getValue();

                    // If redirect URLs are to be remapped, then remap and
                    // set the location header
                    String remappedURL = null;
                    if (configuration.remapRedirects()) {
                        remappedURL = remapRedirectURL(redirectURL,
                                request.getContextURL());
                        // If the remap has failed then the URL will be null,
                        // so only modify Location header if the remap
                        // succeeded.
                        if (remappedURL != null) {
                            Header location =
                                    retrieveHeader(LOCATION_HEADER,
                                            responseAccessor);
                            location.setValue(remappedURL);
                        }
                    }

                    // We have now remapped the URL (either successfully or
                    // unsuccessfully). However, if we are not to follow
                    // redirects then ensure there is no URL to follow.
                    // (If the remap were successful then the Location header
                    // contains the remapped URL.)
                    if (request.isFollowRedirects()) {
                        if (configuration.remapRedirects()) {
                            urlToFollow = remappedURL;
                        } else {
                            urlToFollow = redirectURL;
                        }
                    } else {
                        urlToFollow = null;
                    }

                    if (urlToFollow != null) {
                        // release the existing executor
                        requestExecutor.release();

                        if (redirectCycles.contains(urlToFollow)) {
                            throw new HTTPException(
                                    "Cyclical redirect has been detected.");
                        } else {
                            redirectCycles.add(urlToFollow);
                        }
                        if (redirectCycles.size() >= MAX_REDIRECTS) {
                            throw new HTTPException(
                                    "Maximum number of redirects (" +
                                    MAX_REDIRECTS + ") has been exceeded.");
                        }
                    }

                } else {
                    // not a redirect so stop this variable from containing the
                    // request url. Only do so if a redirect was not followed
                    // to get this response.
                    if (redirectCycles.size() == 0) {
                        urlToFollow = null;
                    }
                }

            } while (isRedirect(responseAccessor.getStatusCode()) &&
                    urlToFollow != null);

            // populate the resoponse
            if (request.getResponse() != null) {
                if (urlToFollow != null) {
                    // We have been redirected but because urlToFollow is
                    // not null we know that the redirect has been remapped.
                    // So we need to set the location header to the remapped
                    // redirect url.
                    Header location = retrieveHeader(LOCATION_HEADER,
                            responseAccessor);
                    if (location != null) {
                        // update the header so that it points to the remapped
                        // redirect url
                        location.setValue(urlToFollow);
                    }

                }
                populateWebDriverResponse(request.getResponse(),
                        responseAccessor,
                        cookieJar);
            }
            // initalise the statusCode
            statusCode = responseAccessor.getStatusCode();

            // stream the output to the responseProcessor
            InputStream responseStream = null;
            try {
                responseStream = responseAccessor.getResponseStream();
                // process the response
                Header contentTypeHeader = retrieveHeader(CONTENT_TYPE,
                        responseAccessor);
                String contentType = (contentTypeHeader == null) ?
                        null : contentTypeHeader.getValue();
                Header header = retrieveHeader(CONTENT_ENCODING,
                        responseAccessor);
                String contentEncoding = (header != null) ?
                        header.getValue() : null;
                request.getResponseProcessor().processHTTPResponse(
                        urlToFollow,
                        responseStream,
                        statusCode,
                        contentType,
                        contentEncoding);
            } finally {
                if (responseStream != null) {
                    try {
                        // ensure that the stream is closed.
                        responseStream.close();
                    } catch (IOException e) {
                        throw new HTTPException(e);
                    }
                }
            }
        } finally {
            if (requestExecutor != null) {
                // ensure that we release the executor.
                requestExecutor.release();
            }
        }
        return statusCode;
    }

    /**
     * Calculate the remapped URL that should be redirected to if the response
     * indicates that a redirect should occur.
     * @param redirectURL the unmapped redirect URL from the response header
     * @param contextURL the current base URL. Can be null.
     * @return the redirect url.
     */
    private String remapRedirectURL(String redirectURL,
                                    String contextURL) {
        URLPrefixRewriteManager rewriteManager =
                configuration.getRedirectRewriteManager();
        if (rewriteManager != null) {
            String remappedRedirectURL = rewriteManager.
                    findAndExecuteRule(redirectURL, contextURL);

            if (remappedRedirectURL != null) {
                // There has been an attempt to remap the redirect
                // url. If the remap was successful then we either
                // need to follow the remap or set the location header
                // in the response. If on the other hand the remap is
                // unsuccessful we either have to follow the original
                // or not follow any redirect.
                if (remappedRedirectURL.equals(redirectURL)) {
                    // The remap has failed.
                    if (!configuration.
                            followUnsuccessfulRedirectRemaps()) {
                        redirectURL = null;
                    }
                } else {
                    redirectURL = remappedRedirectURL;
                }
            }
        }
        return redirectURL;
    }

    /**
     * Return true if this url should be redirected, false otherse.
     * @param statusCode the status code.
     * @return true if this url should be redirected, false otherse.
     */
    private boolean isRedirect(int statusCode) {
        return statusCode == 301 || statusCode == 302 || statusCode == 307;
    }





    /**
     * Populates a <code>WebDriverResponse</code> using the
     * <code>HTTPResponseAccessor</code> provided
     * @param response the <code>WebDriverResponse</code> that is to be
     * populated
     * @param accessor the <code>HTTPResponseAccessor</code> that will be used
     * to read the underlying response
     * @param cookieJar contains any extra cookies that should be added to the
     * response
     */
    private void populateWebDriverResponse(WebDriverResponse response,
                                           HTTPResponseAccessor accessor,
                                           HTTPMessageEntities cookieJar)
            throws HTTPException {
        // add the headers
        HTTPMessageEntities headers = accessor.getHeaders();
        if (headers != null) {
            response.setHeaders(headers);
        }

        // add the cookies
        extractCookies(cookieJar, accessor);
        if (cookieJar.size() > 0) {
            // if we have any cookies then add them to the response
            response.setCookies(cookieJar);
        }

        // add the status code
        response.setStatusCode(accessor.getStatusCode());

        response.setHTTPVersion(accessor.getHTTPVersion());

        // add the content type
        Header contentType = retrieveHeader(CONTENT_TYPE, accessor);
        if (contentType != null) {
            response.setContentType(contentType.getValue());
        }
    }

    /**
     * Extracts all the Cookie entities from the
     * <code>HTTPResponseAccessor</code> and stores them in the
     * <code>HTTPMessageEntities</code> instance provided
     * @param cookieJar the HTTPMessageEntities that will be populated with
     * {@link com.volantis.shared.net.http.cookies.Cookie} instances.
     * @param responseAccessor
     */
    private void extractCookies(HTTPMessageEntities cookieJar,
                                HTTPResponseAccessor responseAccessor)
            throws HTTPException {
        HTTPMessageEntities cookies = responseAccessor.getCookies();
        if (cookies != null) {
            for (Iterator i = cookies.iterator(); i.hasNext();) {
                cookieJar.add((HTTPMessageEntity)i.next());
            }
        }
    }

    /**
     * Retrieve the named header via the given <code>HTTPResponseAccessor</code>
     * instance
     * @param headerName the named header to retrieve
     * @param responseAccessor the <code>HTTPResponseAccessor</code> to use.
     * @return the the named header or null if the header did not exist
     * or more that one match was found.
     */
    private Header retrieveHeader(String headerName,
                                  HTTPResponseAccessor responseAccessor)
            throws HTTPException {
        HTTPMessageEntities headers = responseAccessor.getHeaders();
        Header header =
                HTTPFactory.getDefaultInstance().createHeader(headerName);
        HTTPMessageEntity[] matches = headers.retrieve(header.getIdentity());
        Header value = null;
        if (matches != null && matches.length > 0) {
            value = (Header)matches[0];
        }
        return value;
    }

    /**
     * Encodes the given entity for use in a URL query string.
     * @param entity the entity that is to be encoded
     * @throws HTTPException if an error occurs.
     */
    public abstract String encodeWithinQuery(String entity)
            throws HTTPException;

    /**
     * Encodes the given entity for use in a URL query string.
     * @param entity the entity that is to be encoded
     * @param encoding the encoding that is to be applied
     * @return the encoded entity
     * @throws HTTPException if an error occurs.
     */
    public abstract String encodeWithinQuery(String entity, String encoding)
            throws HTTPException;

    /**
     * Factory method for creating a HTTPRequestExecutor instance
     *
     * @param url     the url used to create the request executor.
     * @param requestType
     *                the request type (GET or POST).
     * @param version the HTTP version (1.0 or 1.1, etc.)
     * @param proxyManager   the Proxy instance.
     * @param xmlPipelineContext
     * @return a HTTPRequestExecutor instance
     */
    public abstract HTTPRequestExecutor createHTTPRequestExecutor(
        final String url,
        final HTTPRequestType requestType,
        final HTTPVersion version,
        final ProxyManager proxyManager,
        final XMLPipelineContext xmlPipelineContext) throws HTTPException;
}
