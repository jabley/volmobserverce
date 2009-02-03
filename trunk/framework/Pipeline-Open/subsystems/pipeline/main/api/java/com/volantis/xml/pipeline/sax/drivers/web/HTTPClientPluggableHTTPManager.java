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


import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.net.http.HTTPAuthorizationScheme;
import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HttpStatusCode;
import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.http.client.HttpClientFactory;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.cookies.CookieVersion;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.net.proxy.Proxy;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.net.url.http.utils.HttpClientUtils;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.UncacheableDependency;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.MalformedCookieException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;


/**
 * An {@link AbstractPluggableHTTPManager} implementation that uses
 * {@link HttpClient} to perform the request.
 */
public class HTTPClientPluggableHTTPManager
        extends AbstractPluggableHTTPManager {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(
                    HTTPClientPluggableHTTPManager.class);

    /**
     * The set of headers to exclude from the transfer.
     */
    private static final Set HEADERS_EXCLUDED_FROM_TRANSFER;
    static {
        Set set = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        set.add("host");
        HEADERS_EXCLUDED_FROM_TRANSFER = set;
    }


    /**
     * Creates a new <code>HTTPClientPluggableHTTPManager</code> instance
     */
    public HTTPClientPluggableHTTPManager() {

    }

    // javadoc inherited
    public HTTPRequestExecutor createHTTPRequestExecutor(
            final String url,
            final HTTPRequestType requestType,
            final HTTPVersion version,
            final ProxyManager proxyManager,
            final XMLPipelineContext xmlPipelineContext) throws HTTPException {

        final HttpState state = new HttpState();
        state.setCookiePolicy(CookiePolicy.COMPATIBILITY);

        HttpClientFactory factory = HttpClientFactory.getDefaultInstance();
        HttpClientBuilder builder = factory.createClientBuilder();
        builder.setState(state);
        builder.setConnectionTimeout(timeout);
        builder.setRoundTripTimeout(timeout);

        // create the HTTPClient instance.
        final HttpClient httpClient = builder.buildHttpClient();

        // return an HTTPRequestExecutor that uses HttpClient to perform
        // the request
        return new HTTPRequestExecutor() {

            /**
             * The request method to use.
             */
            HttpMethod method = null;

            /**
             * The list of request parameters
             */
            List requestParameters = null;

            /**
             * A list containing the request headers.
             */
            List requestHeaders = null;

            // javadoc inherited
            public HTTPResponseAccessor execute() throws HTTPException {

                // create the request method this will be POST or GET depending
                // on the requestType argument. This will copy the request
                // parameters to the appropraite place.
                method = createMethod(url, requestType, requestParameters);

                transferRequestHeaders(method, requestHeaders);

                // set the http version for the request
                ((HttpMethodBase) method).setHttp11(
                        version == HTTPVersion.HTTP_1_1);

                if (proxyManager != null) {
                    try {
                        URL realUrl = new URL(url);

                        // Get the proxy config to use for the host.
                        Proxy proxy = proxyManager.getProxyForHost(
                                realUrl.getHost());
                        if (proxy != null) {
                            method.getHostConfiguration().
                                setProxy(proxy.getHost(), proxy.getPort());

                        if (proxy.useAuthorization()) {
                            method.setDoAuthentication(true);

                            // mock up a authentication challenge so we can get
                            // the response (which can be sent before the
                            // challenge if we want to save time and effort)
                            method.setRequestHeader(
                                HTTPAuthorizationScheme.PROXY_AUTHORIZATION_HEADER,
                                HTTPAuthorizationScheme.BASIC.
                                        createResponseForChallenge(HTTPAuthorizationScheme.MOCK_CHALLENGE_BASIC,
                                    proxy));
                        }
                        }
                    } catch (MalformedURLException mue) {
                        LOGGER.error(mue);
                    }
                }

                try {
                    HttpStatusCode statusCode = httpClient.executeMethod(method);
                    if (xmlPipelineContext != null) {
                        final DependencyContext dependencyContext =
                            xmlPipelineContext.getDependencyContext();
                        if (dependencyContext != null &&
                                dependencyContext.isTrackingDependencies()) {
                            dependencyContext.addDependency(
                                UncacheableDependency.getInstance());
                        }
                    }
                    return new HTTPClientResponseAccessor(method, statusCode);
                } catch (IOException e) {
                    throw new HTTPException(e);
                }
            }

            // javadoc inherited
            public void release() {
                if (method != null) {
                    method.releaseConnection();
                }
            }

            // javadoc inherited
            public void addRequestParameter(RequestParameter parameter) {

                // if no list of request parameters exists then create one and
                // add the request parameter
                if (parameter != null) {
                    if (requestParameters == null) {
                        requestParameters = new ArrayList();
                    }
                    requestParameters.add(parameter);
                }
            }

            // javadoc inherited
            public void addRequestHeader(Header header) {
                if (header != null) {
                    if (requestHeaders == null) {
                        requestHeaders = new ArrayList();
                    }
                    requestHeaders.add(header);
                }
            }

            // javadoc inherited
            public void addRequestCookie(Cookie cookie) {
                // For each cookie we need to create an equivalent HttpClient
                // cookie

                org.apache.commons.httpclient.Cookie httpClientCookie =
                        cookieToHTTPClientCookie(cookie);

                // Now add the cookie header to the HttpState. The state object
                // is needed as cookies set directly as headers on the request
                // are ignored.
                state.addCookie(httpClientCookie);
            }

            /**
             * Method that transfers the list of request headers to the
             * HttpMethod supplied.
             *
             * @param method the method in which the headers whould be placed.
             * @param requestHeaders the list of request headers to transfer to
             * the HttpMethod. May be null.
             */
            private void transferRequestHeaders(HttpMethod method,
                                                List requestHeaders) {
                if (requestHeaders != null) {
                    for (int i = 0; i < requestHeaders.size(); i++) {
                        Header header = (Header) requestHeaders.get(i);
                        String name = header.getName();
                        if (!HEADERS_EXCLUDED_FROM_TRANSFER.contains(name)) {
                            method.addRequestHeader(name, header.getValue());
                        }
                    }
                }
            }
        };
    }

    // javadoc inherited
    public HTTPCache getHTTPCache() {
        return null;
    }

    //  javadoc inherited
    public String encodeWithinQuery(String entity)
            throws HTTPException {
        return encodeWithinQuery(entity, null);
    }

    // javadoc inherited
    public String encodeWithinQuery(String entity, String encoding) throws HTTPException {
        try {
            return encoding != null ?
                    URIUtil.encodeWithinQuery(entity, encoding):
                    URIUtil.encodeWithinQuery(entity);
        } catch (URIException e) {
            throw new HTTPException(e);
        }
    }

    /**
     * Creates a HttpMethod to perform an HTTP request
     *
     * @param url     the url of the request
     * @param requestType
     *                the type of request i.e POST or GET
     * @param requestParameters the list of HTTPMessageEntity objects that
     * represent the request parameters.
     * @return an HttpMethod instance
     * @throws HTTPException if an error occurs.
     */
    private HttpMethod createMethod(String url,
                                    HTTPRequestType requestType,
                                    List requestParameters) throws HTTPException {
        // Either create a POST or GET method
        HttpMethod method = (requestType == HTTPRequestType.POST)
                ? createPostMethod(url, requestParameters)
                : createGetMethod(url, requestParameters);

        // Do not follow redirects as we need to process them ourselves
        method.setFollowRedirects(false);
        return method;
    }

    /**
     * Create a POST type of request method for this request and populate it
     * with the request parameters supplied.
     *
     * @param url the url to get
     * @param requestParameters a list containing the request parameters to
     * use for this POST. May be null.
     * @return The HttpMethdd for performing the requested post
     * @throws HTTPException if an error occurs.
     */
    private HttpMethod createPostMethod(String url, List requestParameters) throws HTTPException {

        // list of the query parameters
        List queryParameters = new ArrayList();
        // list of the body parameters
        List bodyParameters = new ArrayList();
        // set of the body parameter names
        Set bodyParametersName = new HashSet();

        // The webd expects that all parameters will be passed via
        // webd:parameter element with  suituable attributes/elements
        // which define how to pass the parameter in the http method
        // but we also have to handle the situation when
        // url contains the parameters already appended to the query string. In this
        // particular case we shouldn't touch parameters which are in the query string and
        // if there are parameters with query target we should
        // append them at the end of the current query string
        // this is only to keep track of the source of query string parameters
        Set queryParametersName = getQueryParameterNames(url);

        // check parameters target value and add them to the suituable list
        if (requestParameters != null) {
            for (int i = 0; i < requestParameters.size(); i++) {
                WebRequestParameter param = (WebRequestParameter)
                        requestParameters.get(i);
                String target = param.getTarget();
                String name = param.getName();
                if (target != null) {
                    if (target.equalsIgnoreCase("body") || target.equalsIgnoreCase("default")) {
                        // add it to the body
                        if (queryParametersName.contains(name)) {
                            throw new HTTPException("Parameter with name: '" + name +
                                    "' already exists as a query parameter. " +
                                    "You can't add parameters with the same name and an oposite target.");
                        } else {
                            bodyParameters.add(param);
                            bodyParametersName.add(name);
                        }
                    } else if (target.equalsIgnoreCase("query")) {
                        if (bodyParametersName.contains(name)) {
                            throw new HTTPException("Parameter with name: '" + name +
                                    "' already exists as a body parameter. " +
                                    "You can't add parameters with the same name and oposite target.");
                        } else {
                            // add it to the query string
                            queryParameters.add(param);
                            queryParametersName.add(name);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Adding parameter with name: '" +
                                        param.getName() + "' and value: '" +
                                        param.getValue() + "' to the query string.");
                            }
                        }

                    } else {
                        // invalid target value
                        throw new HTTPException("Target value: '" + target + "'is invalid. " +
                                "Possible target values on a POST method are: 'body', 'query', 'default'.");
                    }
                } else {
                    // means that we have to put it in the location that
                    // is the default for the POST method
                    bodyParameters.add(param);
                    bodyParametersName.add(name);
                }
            }
        }

        String encoding = configuration != null ?
                configuration.getCharacterEncoding() :
                null;

        // creates POST method and adds query parameters
        PostMethod method = new PostMethod(HTTPManagerUtilities.createQueryURL(
                        url, queryParameters, this, true, encoding));

        // adds remaining parameters to the body
        if (bodyParameters != null) {
            for (int i = 0; i < bodyParameters.size(); i++) {
                RequestParameter param = (RequestParameter)
                        bodyParameters.get(i);
                method.addParameter(param.getName(),param.getValue());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Adding parameter with name: '" +
                            param.getName() + "' and value: '" +
                            param.getValue() + "' to the body.");
                }
            }
        }

        return method;
    }

    private Set getQueryParameterNames(final String url) throws HTTPException {
        // set of the query parameter names
        final Set queryParameterNames = new HashSet();
        final URI newUrl;
        try {
            newUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new HTTPException(e);
        }
        final String query = newUrl.getQuery();
        if (query != null) {
            final StringTokenizer apmTokenizer = new StringTokenizer(query, "&");
            while (apmTokenizer.hasMoreTokens()) {
                final String parameter = apmTokenizer.nextToken();
                final int eqPos = parameter.indexOf('=');
                String parameterName = null;
                if (eqPos == -1) {
                    parameterName = parameter;
                } else if (eqPos > 0) {
                    parameterName = parameter.substring(0, eqPos);
                }
                if (parameterName != null) {
                    queryParameterNames.add(parameterName);
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Leaving parameter with the name: '" +
                            parameterName + "' in the query string.");
                }
            }
        }
        return queryParameterNames;
    }

    /**
     * Create a GET type of request method for this request.
     *
     * @param url     the url to get
     * @param requestParameters the list containing the HTTPMessageEntity
     * obejcts representing the request parameters. May be null.
     * @return The HttpMethdd for performing the requested get
     * @throws HTTPException if an error occurs.
     */
    private HttpMethod createGetMethod(String url,
                                       List requestParameters) throws HTTPException {

        // validation
        if (requestParameters != null) {

            for (int i = 0; i < requestParameters.size(); i++) {

                // validation only if request is from web driver (webd:get)
                // it use then WebRequestParameter which can be validate
            	if(! (requestParameters.get(0) instanceof WebRequestParameter)) {
            		break;
            	}
                WebRequestParameter param = (WebRequestParameter)
                        requestParameters.get(i);
                String target = param.getTarget();
                if (target != null) {
                    if (!target.equalsIgnoreCase("query") && !target.equalsIgnoreCase("default")) {
                        // invalid target value or target equals body
                        throw new HTTPException("'body' target can't be specified on a GET method or target value is invalid. " +
                                        "Possible target values on a GET method are: 'query', 'default'.");
                    }
                }
            }
        }

        String encoding = configuration != null ?
                configuration.getCharacterEncoding() :
                null;

        return new GetMethod(HTTPManagerUtilities.createQueryURL(
                url, requestParameters, this, false, encoding));
    }


    /**
     * Given a Volantis Cookie return the equivalent HTTP Client cookie.
     * @param cookie A Volantis Cookie.
     * @return The org.apache.commons.httpclient.Cookie version of the given
     * Volantis Cookie.
     */
    private org.apache.commons.httpclient.Cookie
            cookieToHTTPClientCookie(Cookie cookie) {

        org.apache.commons.httpclient.Cookie httpClientCookie =
                new org.apache.commons.httpclient.Cookie(
                        cookie.getDomain(),
                        cookie.getName(),
                        cookie.getValue(),
                        cookie.getPath(),
                        cookie.getMaxAge(),
                        cookie.isSecure());
        // no constructor that allows this to be set
        httpClientCookie.setVersion(cookie.getVersion().getNumber());

        return httpClientCookie;
    }

    /**
     * Add an HttpClient header into an HTTPMessageEntities as a
     * com.volantis.shared.set.http.Header.
     * @param httpClientHeader The HttpClient header.
     * @param headers The HTTPMessageEntities container for headers.
     * @param valueList True if the header's value can contain a
     *                  comma-separated list of values
     */
    private void addHttpClientHeaderToWebDriverHeaders(
            org.apache.commons.httpclient.Header httpClientHeader,
            HTTPMessageEntities headers, boolean valueList) throws HttpException {

        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        final String headerName = httpClientHeader.getName();
        final List values = HttpClientUtils.getHeaderValueList(httpClientHeader);
        if (values.size() == 0) {
            Header header = factory.createHeader(headerName);
            headers.add(header);
        } else {
            if (valueList) {
                for (Iterator iter = values.iterator(); iter.hasNext(); ) {
                    final Header header = factory.createHeader(headerName);
                    final String value = (String) iter.next();
                    header.setValue(value);
                    headers.add(header);
                }
            } else {
                StringBuffer reconstitutedHeader = new StringBuffer();
                Iterator it = values.iterator();
                while (it.hasNext()) {
                    if (reconstitutedHeader.length() > 0) {
                        reconstitutedHeader.append(',');
                    }
                    reconstitutedHeader.append(it.next());
                }
                final Header header = factory.createHeader(headerName);
                header.setValue(reconstitutedHeader.toString());
                headers.add(header);
            }
        }
    }

    /**
     * Given an array of HttpClient cookies, create corresponding WebDriver
     * cookies (implementing com.volantis.shared.set.http.Cookie) and put these
     * into a HTTPMessageEntities.
     * @param httpClientCookies The array of HttpClient cookies. Must be non-
     * null.
     * @param webDriverCookies The HTTPMessageEntities into which to put the
     * WebDriver cookies corresponding to the cookies in httpClientCookies.
     * Must be non-null.
     */
    private void addHttpClientCookiesToWebDriverCookies(
            org.apache.commons.httpclient.Cookie httpClientCookies [],
            HTTPMessageEntities webDriverCookies) {
        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        for (int i = 0; i < httpClientCookies.length; i++) {
            org.apache.commons.httpclient.Cookie httpClientCookie =
                    httpClientCookies[i];
            Cookie cookie = factory.createCookie(httpClientCookie.getName(),
                    httpClientCookie.getDomain(),
                    httpClientCookie.getPath());
            cookie.setComment(httpClientCookie.getComment());

            // Need to work out max age from the HttpClient expiry date and
            // the current time on the client.
            int maxAge =
                HttpClientUtils.calculateResponseCookieMaxAge(httpClientCookie);
            cookie.setMaxAge(maxAge);

            cookie.setSecure(httpClientCookie.getSecure());
            cookie.setValue(httpClientCookie.getValue());
            cookie.setVersion(CookieVersion.getCookieVersion(
                    httpClientCookie.getVersion()));

            // Use add since we do not currently distinguish between cookies
            // with the same name but different path/domain - though we might
            // expect HttpClient to not provide us with duplicate cookies so
            // adding should not cause real duplicates to be created.
            webDriverCookies.add(cookie);
        }
    }

    /**
     * A {@link HTTPResponseAccessor} allows the response to be extracted from
     * an {@link HttpMethod} instance. The HttpMethod must have been executed
     */
    private class HTTPClientResponseAccessor
            implements HTTPResponseAccessor {

        /**
         * Cookie jar
         */
        private HTTPMessageEntities cookies;

        /**
         * Headers
         */
        private HTTPMessageEntities headers;

        /**
         * The status code
         */
        private final HttpStatusCode statusCode;

        /**
         * The HttpMethod that is being adapted
         */
        private final HttpMethod method;

        /**
         * The Http version returned by the server.
         */
        private HTTPVersion httpVersion = null;

        /**
         * Creates a <code>HTTPClientResponseAccessor</code> instance
         * @param method the underlying HttpMethod instance that has been
         * executed in order to perform the request.
         * @param statusCode the statusCode that the request returned
         */
        public HTTPClientResponseAccessor(HttpMethod method,
                                          HttpStatusCode statusCode) {
            this.method = method;
            this.statusCode = statusCode;
            this.httpVersion = HTTPVersion.httpVersion(method.getStatusLine()
                    .getHttpVersion());
        }

        /**
         * Get the HTTP version returned by the server.
         * @return The HTTP version returned by the server.
         */
        public HTTPVersion getHTTPVersion() {
            return this.httpVersion;
        }

        // javadoc inherited
        public HTTPMessageEntities getCookies() throws HTTPException {
            if (cookies == null) {
                try {
                    populateMessageEntities();
                } catch (HttpException e) {
                    throw new HTTPException(e);
                }
            }
            return cookies;
        }

        // javadoc inherited
        public HTTPMessageEntities getHeaders() throws HTTPException {
            if (headers == null) {
                try {
                    populateMessageEntities();
                } catch (HttpException e) {
                    throw new HTTPException(e);
                }
            }
            return headers;
        }

        // javadoc inherited
        public InputStream getResponseStream() throws HTTPException {
            try {
                return method.getResponseBodyAsStream();
            } catch (IOException e) {
                throw new HTTPException(e);
            }
        }

        // javadoc inherited
        public int getStatusCode() {
            return statusCode.getAsInt();
        }

        /**
         * Creates and populates the cookie and header HTTPMessageEnities
         * members
         * @throws MalformedCookieException if an error occurs
         */
        private void populateMessageEntities() throws HttpException {
            // extract the headers and cookies from the the HttpMethod
            HTTPFactory factory = HTTPFactory.getDefaultInstance();
            cookies = factory.createHTTPMessageEntities();
            headers = factory.createHTTPMessageEntities();
            if (method != null) {
                org.apache.commons.httpclient.Header responseHeaders [] =
                        method.getResponseHeaders();
                if (responseHeaders != null && responseHeaders.length > 0) {

                    for (int i = 0; i < responseHeaders.length; i++) {
                        org.apache.commons.httpclient.Header header =
                                responseHeaders[i];
                        if (COOKIE_HEADER.equalsIgnoreCase(header.getName())) {
                            org.apache.commons.httpclient.Cookie httpCookies []
                                = HttpClientUtils.createCookieArray(method, header);
                            if (httpCookies != null &&
                                    httpCookies.length > 0) {
                                addHttpClientCookiesToWebDriverCookies(
                                        httpCookies, cookies);
                            }
                        } else if (LOCATION_HEADER.equalsIgnoreCase(header.getName())) {
                            // Location headers contain single URIs rather than
                            // a comma-separated list of values. In order to
                            // handle this we need to treat them as a special
                            // case. If there are other headers that can only
                            // contain a single value (which may contain
                            // commas) then we should check for them here as
                            // well.
                            addHttpClientHeaderToWebDriverHeaders(header,
                                    headers, false);
                        } else {
                            addHttpClientHeaderToWebDriverHeaders(header,
                                    headers, true);
                        }
                    }
                }
            }
        }
    }
}
