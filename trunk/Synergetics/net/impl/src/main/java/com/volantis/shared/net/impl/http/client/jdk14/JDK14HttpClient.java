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

package com.volantis.shared.net.impl.http.client.jdk14;

import com.volantis.shared.net.impl.ThreadInterruptingTimingOutTask;
import com.volantis.shared.net.impl.TimingOutTask;
import com.volantis.shared.net.impl.http.client.AbstractHttpClient;
import com.volantis.shared.net.impl.ThreadInterruptingTimingOutTask;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.time.Period;
import our.apache.commons.httpclient.HttpConnection;
import our.apache.commons.httpclient.protocol.Protocol;
import our.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import our.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import java.io.InterruptedIOException;
import java.io.IOException;

/**
 * A JDK 1.4 specific {@link AbstractHttpClient}.
 *
 * <p>This makes use of the new I/O socket channels to support round trip and
 * connection timeouts properly. The connection timeout is managed by the
 * implementations of {@link ProtocolSocketFactory} that are responsible for
 * creating and connecting the sockets. The round trip timeout is handled
 * by this class itself and relies on the fact that socket channels are
 * interruptible by {@link Thread#interrupt()}.</p> 
 */
public class JDK14HttpClient
        extends AbstractHttpClient {

    /**
     * Initialise.
     *
     * @param builder The client builder.
     */
    public JDK14HttpClient(HttpClientBuilder builder) {
        super(builder);
    }

    // Javadoc inherited.
    protected TimingOutTask createTimingOutTask(
            HttpConnection connection) {
        return new ThreadInterruptingTimingOutTask();
    }

    // Javadoc inherited.
    protected Protocol updateProtocol(Protocol protocol) {

        // Create a protocol that encapsulates a socket factory that will
        // create socket wrappers around socket channels.
        ProtocolSocketFactory socketFactory = protocol.getSocketFactory();
        if (socketFactory instanceof SecureProtocolSocketFactory) {
            SecureProtocolSocketFactory secureSocketFactory =
                    (SecureProtocolSocketFactory) socketFactory;
            socketFactory = new SecureProtocolSocketChannelFactory(
                    secureSocketFactory, connectionTimeout);
        } else {
            socketFactory =
                    new ProtocolSocketChannelFactory(connectionTimeout);
        }

        protocol = new Protocol(protocol.getScheme(),
                socketFactory, protocol.getDefaultPort());
        
        return protocol;
    }

    // Javadoc inherited.
    protected void prepareConnection(HttpConnection connection)
            throws IOException {
        // Do nothing. Doesn't use the connection timeout or socket read
        // timeout as they are handled separately.
    }

    // Javadoc inherited.
    protected InterruptedIOException wrapException(final String message,
                                                   Exception cause) {

        InterruptedIOException exception = new InterruptedIOException(message);
        exception.initCause(cause);
        return exception;
    }
}
