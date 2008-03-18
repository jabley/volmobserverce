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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.impl.url.http;

import com.volantis.shared.net.http.HTTPAuthorizationScheme;
import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.http.client.HttpClientFactory;
import com.volantis.shared.net.impl.url.InternalURLContentManager;
import com.volantis.shared.net.proxy.Proxy;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.net.url.http.HttpUrlConfiguration;
import com.volantis.shared.time.Period;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.HttpMethod;
import our.apache.commons.httpclient.methods.GetMethod;

/**
 * Base implementation of HttpContentRetriever. Creates the HttpClient and
 * GetMethod, sets the proxy authorization header, if necessary and copies the
 * headers from the HttpUrlConfiguration.
 *
 * <p>Allows subclasses to add additional headers, and perform other operations
 * before and after executing the get. Leaves the HttpContent generation to the
 * implementation.</p>
 */
public abstract class AbstractHttpContentRetriever
        implements HttpContentRetriever {

    /**
     * Content manager to retrieve proxy info.
     */
    private final InternalURLContentManager manager;

    public AbstractHttpContentRetriever(
            final InternalURLContentManager manager) {
        this.manager = manager;
    }

    // javadoc inherited
    public HttpContent retrieve(final URL url, final Period timeout,
                               final HttpUrlConfiguration httpConfig)
            throws IOException {
        // Create a HTTP Client which will timeout connections and round trips
        // in the specified period.
        final HttpClientFactory factory = HttpClientFactory.getDefaultInstance();
        final HttpClientBuilder builder = factory.createClientBuilder();
        builder.setConnectionTimeout(timeout);
        builder.setRoundTripTimeout(timeout);
        final HttpClient httpClient = builder.buildHttpClient();

        final GetMethod method = new GetMethod(url.toExternalForm());
        final HostConfiguration configuration = new HostConfiguration();
        configuration.setHost(url.getHost(), url.getPort(), url.getProtocol());
        method.setHostConfiguration(configuration);
        method.setFollowRedirects(true);

        // Get the proxy config to use for the host.
        final Proxy proxy = manager.getProxy(url.getHost());
        if (proxy != null) {
            configuration.setProxy(proxy.getHost(), proxy.getPort());

            if (proxy.useAuthorization()) {
                method.setDoAuthentication(true);

                // mock up a authentication challenge so we can get
                // the response (which can be sent before the
                // challenge if we want to save time and effort)
                method.setRequestHeader(
                        HTTPAuthorizationScheme.PROXY_AUTHORIZATION_HEADER,
                        HTTPAuthorizationScheme.BASIC.
                        createResponseForChallenge(
                                HTTPAuthorizationScheme.MOCK_CHALLENGE_BASIC,
                                proxy));
            }
        }

        // set the additional headers
        if (httpConfig != null) {
            for (Iterator iter = httpConfig.getHeaderNames(); iter.hasNext();) {

                final String headerName = (String) iter.next();
                final Iterator headerValues =
                    httpConfig.getHeaderValues(headerName);
                while (headerValues.hasNext()) {
                    final String headerValue = (String) headerValues.next();
                    method.addRequestHeader(headerName, headerValue);
                }
            }
        }

        // allow subclasses to add things to the method and to register the
        // start of the method execution
        preExecute(method);
        httpClient.executeMethod(method);
        return createHttpContent(url, method);
    }

    /**
     * Called immediately before executing the get method. Lets subclasses add
     * extra things (headers, cookies, etc.) to the method or register
     * pre-execution details.
     *
     * @param method the method that is to be executed
     */
    protected void preExecute(final HttpMethod method) {
        // do nothing
    }

    /**
     * Creates the {@link HttpContent} object from the executed method.
     *
     * @param url the URL for which the content was requested 
     * @param method the executed method
     * @return the created URLContent
     * @throws IOException if there is an error reading the HTTP method
     */
    protected abstract HttpContent createHttpContent(URL url, HttpMethod method)
        throws IOException;
}
