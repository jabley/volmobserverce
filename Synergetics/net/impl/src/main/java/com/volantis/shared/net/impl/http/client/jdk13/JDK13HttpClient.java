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

package com.volantis.shared.net.impl.http.client.jdk13;

import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.impl.TimingOutTask;
import com.volantis.shared.net.impl.http.client.AbstractHttpClient;
import com.volantis.shared.time.Period;
import our.apache.commons.httpclient.HttpConnection;
import our.apache.commons.httpclient.protocol.Protocol;

import java.io.InterruptedIOException;
import java.io.IOException;

/**
 * A JDK 1.3 specific {@link AbstractHttpClient}.
 *
 * <p>This does not work brilliantly well but it should suffice. It creates a
 * timer task that closes the connection. The problem with this is that the
 * connection was not designed to be closed asynchronously so this can lead to
 * spurious errors, however they are caught by the client and wrapped in an
 * {@link InterruptedIOException} exception. It also sets the socket read
 * timeout and the Apache HttpConnection connection timeout to try and cause
 * the non blocking I/O to exit as soon as possible.</p>
 */
public class JDK13HttpClient
        extends AbstractHttpClient {

    /**
     * Initialise.
     *
     * @param builder The client builder.
     */
    public JDK13HttpClient(HttpClientBuilder builder) {
        super(builder);
    }

    // Javadoc inherited.
    protected TimingOutTask createTimingOutTask(
            HttpConnection connection) {
        return new HttpConnectionTimingOutTask(connection);
    }

    // Javadoc inherited.
    protected Protocol updateProtocol(Protocol protocol) {
        // This does not change the underlying socket factory and so does not
        // require changing the protocol so just return the one that was passed
        // in.
        return protocol;
    }

    // Javadoc inherited.
    protected void prepareConnection(HttpConnection connection)
            throws IOException {
        // Use the built in connection timeout in HTTP Client. This should be
        // avoided as it can leave a large number of threads lying around but
        // it is the best that there is in JDK 1.3.
        if (connectionTimeout != Period.INDEFINITELY) {
            connection.setConnectionTimeout((int)
                    connectionTimeout.inMillis());
        }

        // Set the socket read timeout so that socket reads do not wait
        // indefinitely.
        if (roundTripTimeout != Period.INDEFINITELY) {
            connection.setSoTimeout((int) roundTripTimeout.inMillis());
        }
    }

    // Javadoc inherited.
    protected InterruptedIOException wrapException(final String message,
                                                   Exception cause) {
        // JDK 1.3 does not support causes so create our own exception that
        // does.
        return new ExtendedInterruptedIOException(message, cause);
    }
}
