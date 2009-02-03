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

package com.volantis.shared.net.http.client;

import com.volantis.shared.time.Period;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpState;

/**
 * Builder for a {@link HttpClient}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface HttpClientBuilder {

    /**
     * Get the connection timeout.
     *
     * @return The connection timeout.
     */
    Period getConnectionTimeout();

    /**
     * Set the connection timeout.
     *
     * @param connectionTimeout The connection timeout.
     */
    void setConnectionTimeout(Period connectionTimeout);

    /**
     * Get the round trip timeout.
     *
     * @return The round trip timeout.
     */
    Period getRoundTripTimeout();

    /**
     * Set the round trip timeout.
     *
     * @param roundTripTimeout The round trip timeout.
     */
    void setRoundTripTimeout(Period roundTripTimeout);

    /**
     * Get the connection manager.
     *
     * @return The connection manager.
     */
    HttpConnectionManager getConnectionManager();

    /**
     * Set the connection manager.
     *
     * @param connectionManager The connection manager.
     */
    void setConnectionManager(HttpConnectionManager connectionManager);

    /**
     * Get the state.
     *
     * @return The state.
     */
    HttpState getState();

    /**
     * Set the state.
     *
     * @param state The state.
     */
    void setState(HttpState state);

    /**
     * Build the {@link HttpClient}.
     *
     * @return The newly built {@link HttpClient}.
     */
    HttpClient buildHttpClient();
}
