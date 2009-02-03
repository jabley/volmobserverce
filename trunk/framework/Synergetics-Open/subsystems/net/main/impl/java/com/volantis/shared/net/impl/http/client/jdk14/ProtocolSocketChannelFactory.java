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

import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.shared.time.Comparator;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.params.HttpConnectionParams;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * A {@link ProtocolSocketFactory} that creates sockets based on the new I/O
 * socket channels.
 *
 * <p>This uses the non blocking capability of the channels to implement a
 * connection timeout without using additional threads.</p>
 */
public class ProtocolSocketChannelFactory
        implements ProtocolSocketFactory {

    /**
     * The clock, used to detect connection timeouts.
     */
    private final SystemClock clock;

    /**
     * The connection timeout.
     */
    private final Period connectionTimeout;

    /**
     * Initialise.
     *
     * @param connectionTimeout The connection timeout.
     */
    public ProtocolSocketChannelFactory(Period connectionTimeout) {
        clock = SystemClock.getDefaultInstance();
        this.connectionTimeout = connectionTimeout;
    }

    // Javadoc inherited.
    public Socket createSocket(
            String host, int port, InetAddress clientHost, int clientPort)
            throws IOException, UnknownHostException {

        return createSocket(host, port, clientHost, clientPort, null);    
    }

    public Socket createSocket(
            String host, int port, InetAddress clientHost, int clientPort, HttpConnectionParams params)
            throws IOException, UnknownHostException {
        SocketAddress local = new InetSocketAddress(clientHost, clientPort);
        InetSocketAddress remote = new InetSocketAddress(host, port);

        SocketChannel channel = SocketChannel.open();
        Socket socket;
        boolean close = true;
        try {
            socket = channel.socket();
            socket.bind(local);

            connect(channel, remote);
            close = false;
        } finally {
            // Make sure that the channel is closed in the case of an error.
            if (close) {
                channel.close();
            }
        }

        return socket;
    }

    /**
     * Connects the socket.
     *
     * <p>This implements the connection timeout by placing the socket channel
     * into non blocking mode and waiting on it until the connection timeout
     * has been exceeded, or the socket is ready to connect.</p>
     *
     * @param channel The channel being connected.
     * @param remote  The remote address.
     * @throws IOException If there was a problem.
     */
    private void connect(SocketChannel channel, InetSocketAddress remote)
            throws IOException {

        // Check whether the remote address is resolved before connecting as
        // if it isn't then the connect will throw a RuntimeException rather
        // than an IOException which is what the old sockets used to do and
        // is therefore expected by callers.
        if (remote.isUnresolved()) {
            throw new UnknownHostException(remote.getHostName());
        }

        channel.configureBlocking(false);
        boolean connected = channel.connect(remote);
        if (!connected) {
            waitForConnectToComplete(channel, remote);
        }

        // Reconfigure the channel to block so that the normal socket I/O
        // calls work correctly.
        channel.configureBlocking(true);
    }

    /**
     * Wait for the connect to complete.
     *
     * @param channel The channel on which the connect has been started.
     * @param remote The address to which the channel is attempting to connect.
     * @throws IOException If there was a problem.
     */
    private void waitForConnectToComplete(
            SocketChannel channel, InetSocketAddress remote)
            throws IOException {
        Selector selector = Selector.open();
        try {
            SelectionKey key = channel.register(selector,
                    SelectionKey.OP_CONNECT);

            Period timeRemaining = connectionTimeout;
            Time startTime = clock.getCurrentTime();

            boolean connected = false;
            while (!connected) {
                long timeout = timeRemaining.inMillisTreatIndefinitelyAsZero();
                int keys = selector.select(timeout);
                if (keys == 0) {
                    // Selector woken up, check to see whether it has timed out.
                    Time now = clock.getCurrentTime();
                    Period elapsed = now.getPeriodSince(startTime);
                    if (Comparator.GE.compare(
                            elapsed, timeRemaining)) {
                        // Timed out so fail.
                        throw new ConnectException("Timed out");
                    } else {
                        // Reduce the time remaining by the elapsed time and
                        timeRemaining = timeRemaining.subtract(elapsed);
                        startTime = now;
                    }
                } else if (keys == 1) {
                    Set selectedKeys = selector.selectedKeys();
                    if (selectedKeys.contains(key)) {
                        connected = true;
                    } else {
                        throw new IllegalStateException(
                                "Expected selected keys to contain " + key +
                                " but was " + selectedKeys);
                    }
                } else {
                    throw new IllegalStateException(
                            "Expected 0 or 1 from selector.select but got " +
                            keys);
                }
            }

            // Finish the connection, this may result in a ConnectException,
            // if it does then augment it with the remote address as it is
            // useless otherwise.
            try {
                channel.finishConnect();
            } catch (ConnectException e) {
                ConnectException augmented = new ConnectException(
                        e.getMessage() + " when connecting to " + remote);
                augmented.setStackTrace(e.getStackTrace());
                throw augmented;
            }
        } finally {
            // Make sure that the selector is always closed, as otherwise it
            // can leak file handles.
            selector.close();
        }
    }

    // Javadoc inherited.
    public Socket createSocket(String host, int port) throws IOException,
            UnknownHostException {
        InetSocketAddress remote = new InetSocketAddress(host, port);
        SocketChannel channel = SocketChannel.open();
        Socket socket;
        boolean close = true;
        try {
            socket = channel.socket();

            connect(channel, remote);
            close = false;
        } finally {
            // Make sure that the channel is closed in the case of an error.
            if (close) {
                channel.close();
            }
        }

        return socket;
    }
}
