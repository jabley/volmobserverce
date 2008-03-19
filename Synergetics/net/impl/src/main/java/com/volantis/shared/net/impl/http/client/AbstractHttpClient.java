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

import com.volantis.shared.net.http.HttpStatusCode;
import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.impl.RunnableTimerTask;
import com.volantis.shared.net.impl.TimingOutTask;
import com.volantis.shared.time.Period;
import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.HttpConnection;
import our.apache.commons.httpclient.HttpConnectionManager;
import our.apache.commons.httpclient.HttpMethod;
import our.apache.commons.httpclient.HttpState;
import our.apache.commons.httpclient.protocol.Protocol;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import java.util.Timer;

/**
 * Implementation of {@link HttpClient}.
 */
public abstract class AbstractHttpClient
        implements HttpClient {

    /**
     * The timer with which all the tasks will be registered.
     *
     * <p>This will create a single daemon thread to manage a queue of
     * tasks.</p>
     */
    private static final Timer TIMER = new Timer(true);

    /**
     * The Apache HttpConnectionManager that is responsible for managing the
     * connections.
     */
    private final HttpConnectionManager manager;

    /**
     * The round trip timeout.
     */
    protected final Period roundTripTimeout;

    /**
     * The connection timeout.
     */
    protected final Period connectionTimeout;

    /**
     * The state associated with this client.
     */
    private final HttpState httpState;

    public AbstractHttpClient(HttpClientBuilder builder) {
        this.roundTripTimeout = builder.getRoundTripTimeout();
        this.connectionTimeout = builder.getConnectionTimeout();

        // If no manager is specified then create a simple one that is only
        // used by this client.
        HttpConnectionManager manager = builder.getConnectionManager();
        if (manager == null) {
            // If no external manager is configured then use a default one
            // that simply creates and closes connections on demand.
            manager = new DefaultHttpConnectionManager();
        }
        this.manager = manager;

        // If no state is provided then create an empty one that is only
        // used by this instance.
        HttpState state = builder.getState();
        if (state == null) {
            state = new HttpState();
        }
        httpState = state;
    }

    // Javadoc inherited.
    public HttpStatusCode executeMethod(HttpMethod method)
            throws IOException {

        HostConfiguration configuration = method.getHostConfiguration();
        if (configuration == null) {
            throw new IllegalStateException("No configuration set on method");
        }

        Protocol originalProtocol = configuration.getProtocol();
        Protocol protocol = updateProtocol(originalProtocol);
        if (protocol != originalProtocol) {
            configuration.setHost(configuration.getHost(),
                    configuration.getVirtualHost(),
                    configuration.getPort(),
                    protocol);
        }

        // Get the connection from the manager, it is the responsibility
        // of the caller to release this connection by calling
        // HttpMethod#releaseConnection.
        HttpConnection connection = manager.getConnection(configuration);
        prepareConnection(connection);

        TimingOutTask task = null;
        int statusCode;
        URI uri = null;
        try {
            // Get the URI now so that it can be used to report errors if
            // necessary.
            uri = new URI(connection.getProtocol().getScheme(), null,
                    configuration.getHost(), connection.getPort(),
                    method.getPath(), method.getQueryString(), null);

            if (roundTripTimeout != Period.INDEFINITELY) {
                task = createTimingOutTask(connection);
                TIMER.schedule(new RunnableTimerTask(task),
                        roundTripTimeout.inMillis());
            }

            statusCode = method.execute(httpState, connection);
        } catch (Exception e) {
            if (task != null && task.timedOut()) {

                // Assume that the exception occurred because of the
                // timeout.
                // todo create a more specific exception that encapsulates the
                // todo URL, the timeout and the actual elapsed time.
                InterruptedIOException exception =
                        wrapException("Request to '" + uri +
                        "' timed out after " + roundTripTimeout, e);

                throw exception;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new UndeclaredThrowableException(e);
            }
        } finally {
            if (task != null) {
                // Make sure that any existing timeouts are ignored.
                task.deactivate();
            }
        }

        return HttpStatusCode.getStatusCode(statusCode);
    }

    /**
     * Create the task that will timeout the connection.
     *
     * @param connection The connection to timeout.
     * @return The timing out task.
     */
    protected abstract TimingOutTask createTimingOutTask(
            HttpConnection connection);

    /**
     * Update the protocol.
     *
     * <p>If the specialization of this client needs to change the protocol,
     * e.g. the socket factory, then it must implement this to create and
     * return a new protocol. Otherwise, the specialization must just return
     * the protocol supplied.</p>
     *
     * @param protocol          The original protocol.
     * @return The protocol.
     */
    protected abstract Protocol updateProtocol(Protocol protocol);

    /**
     * Prepare the connection for use.
     *
     * @param connection The connection to prepare.
     */
    protected abstract void prepareConnection(HttpConnection connection)
            throws IOException;

    /**
     * Create a {@link InterruptedIOException} that contains the specified
     * message and wraps the specified exception.
     *
     * @param message The message to store in the exception.
     * @param cause   The root cause of the exception.
     * @return The newly created {@link InterruptedIOException}.
     */
    protected abstract InterruptedIOException wrapException(
            final String message, Exception cause);
}
