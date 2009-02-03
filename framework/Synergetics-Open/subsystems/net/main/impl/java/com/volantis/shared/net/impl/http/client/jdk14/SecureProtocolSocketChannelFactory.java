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

import com.volantis.shared.time.Period;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.ConnectTimeoutException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A {@link SecureProtocolSocketFactory} that creates sockets based on the new I/O
 * socket channels.
 *
 * <p>This relies on the fact that SSL is a layer added on top of normal
 * sockets. It extends the {@link ProtocolSocketChannelFactory} to create and
 * connect the sockets using the socket channels and then uses the delegate to
 * create a secure socket on top of that.</p>
 */
public class SecureProtocolSocketChannelFactory
        extends ProtocolSocketChannelFactory
        implements SecureProtocolSocketFactory {

    /**
     * The underlying factory that will create the secure sockets.
     */
    private final SecureProtocolSocketFactory delegate;

    /**
     * Initialise.
     *
     * @param delegate The underlying
     * @param connectionTimeout The connection timeout.
     */
    public SecureProtocolSocketChannelFactory(
            SecureProtocolSocketFactory delegate,
            Period connectionTimeout) {
        super(connectionTimeout);

        this.delegate = delegate;
    }

    // Javadoc inherited.
    public Socket createSocket(
            String host, int port, InetAddress clientHost, int clientPort)
            throws IOException {

        return createSocket(host, port, clientHost, clientPort, null); 
    }

    public Socket createSocket(
            String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
            throws IOException, ConnectTimeoutException {

        // Create and connect a socket to the specified host and port from
        // the specified local host and port.
        Socket socket = super.createSocket(host, port, localAddress, localPort);

        // Decorate the socket with a secure socket that will automatically
        // close the underlying one.
        return createSocket(socket, host, port, true);
    }

    // Javadoc inherited.
    public Socket createSocket(String host, int port) throws IOException {

        // Create and connect a socket to the specified host and port.
        Socket socket = super.createSocket(host, port);

        // Decorate the socket with a secure socket that will automatically
        // close the underlying one.
        return createSocket(socket, host, port, true);
    }

    // Javadoc inherited.
    public Socket createSocket(
            Socket socket, String host, int port,
            boolean autoClose)
            throws IOException {

        // Delegate the request to create a secure socket wrapper around an
        // existing and already connected socket.
        Socket secureSocket = delegate.createSocket(socket, host, port,
                autoClose);
        return secureSocket;
    }
}
