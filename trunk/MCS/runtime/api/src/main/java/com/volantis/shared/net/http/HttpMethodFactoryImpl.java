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

package com.volantis.shared.net.http;

import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.http.client.HttpClientFactory;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.shared.time.Period;
import our.apache.commons.httpclient.HttpMethod;
import our.apache.commons.httpclient.methods.GetMethod;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default implementation of {@link HttpMethodFactory}.
 */
public class HttpMethodFactoryImpl
        implements HttpMethodFactory {

    /**
     * The system clock.
     */
    private final SystemClock clock;

    /**
     * The timeout for connections. A timeout of 0 means there is no timeout.
     */
    private final Period connectionTimeout;

    /**
     * Creates a new instance of RemoteRepositoryConnection
     */
    public HttpMethodFactoryImpl(
            Period connectionTimeout, final SystemClock clock) {
        this.connectionTimeout = connectionTimeout;
        this.clock = clock;
    }

    /**
     * Add HTTP headers to the request
     */
    private void createHeaders(URL url, HttpMethod method) {

        // We need to send the hostname and port as a header otherwise the
        // response sets the hostname to localhost.
        if (url.getPort() == -1) {
            method.addRequestHeader("Host", url.getHost());
        } else {
            method.addRequestHeader("Host", url.getHost() + ":" +
                    url.getPort());
        }
    }

    public HttpGetMethod createGetMethod(String url) {

        GetMethod method = new GetMethod(url);
        method.setHttp11(false);

        HttpClientFactory factory = HttpClientFactory.getDefaultInstance();
        HttpClientBuilder builder = factory.createClientBuilder();
        builder.setConnectionTimeout(connectionTimeout);
        builder.setRoundTripTimeout(connectionTimeout);
        HttpClient httpClient = builder.buildHttpClient();

        URL netURL = null;
        try {
            netURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new ExtendedRuntimeException("Invalid netURL: " + url, e);
        }
        createHeaders(netURL, method);

        MethodExecuter executer = new MethodExecuterImpl(httpClient);

        return new HttpGetMethodImpl(clock, executer,
                method, url, connectionTimeout);
    }
}
