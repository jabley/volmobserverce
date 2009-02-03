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

package com.volantis.shared.net.impl.http.client;

import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.time.Period;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpState;

/**
 * The implementation of {@link HttpClientBuilder}.
 */
public class HttpClientBuilderImpl
        implements HttpClientBuilder {

    /**
     * The factory to use to create the {@link HttpClient}.
     */
    private final AbstractHttpClientFactory factory;

    /**
     * The connection timeout.
     */
    private Period connectionTimeout = Period.INDEFINITELY;

    /**
     * The round trip timeout.
     */
    private Period roundTripTimeout = Period.INDEFINITELY;

    /**
     * The connection manager.
     */
    private HttpConnectionManager connectionManager;

    /**
     * The state.
     */
    private HttpState state;

    /**
     * Initialise.
     *
     * @param factory The factory to use to create the {@link HttpClient}.
     */
    public HttpClientBuilderImpl(AbstractHttpClientFactory factory) {
        this.factory = factory;
    }

    // Javadoc inherited.
    public Period getConnectionTimeout() {
        return connectionTimeout;
    }

    // Javadoc inherited.
    public void setConnectionTimeout(Period connectionTimeout) {
        if (connectionTimeout == null) {
            throw new IllegalArgumentException("connectionTimeout cannot be null");
        }
        this.connectionTimeout = connectionTimeout;
    }

    // Javadoc inherited.
    public Period getRoundTripTimeout() {
        return roundTripTimeout;
    }

    // Javadoc inherited.
    public void setRoundTripTimeout(Period roundTripTimeout) {
        if (roundTripTimeout == null) {
            throw new IllegalArgumentException("roundTripTimeout cannot be null");
        }
        this.roundTripTimeout = roundTripTimeout;
    }

    // Javadoc inherited.
    public HttpConnectionManager getConnectionManager() {
        return connectionManager;
    }

    // Javadoc inherited.
    public void setConnectionManager(HttpConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    // Javadoc inherited.
    public HttpState getState() {
        return state;
    }

    // Javadoc inherited.
    public void setState(HttpState state) {
        this.state = state;
    }

    // Javadoc inherited.
    public HttpClient buildHttpClient() {
        return factory.createClient(this);
    }
}
