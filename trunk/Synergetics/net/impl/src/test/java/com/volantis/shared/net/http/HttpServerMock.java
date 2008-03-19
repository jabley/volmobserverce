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

package com.volantis.shared.net.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A mock server that can be used to test socket based code.
 */
public class HttpServerMock
        implements Runnable {

    /**
     * The socket.
     */
    private final ServerSocket serverSocket;

    /**
     * The port for the server, this is assigned automatically by the operating
     * system so will not run into problems with binding exceptions.
     */
    private final int serverPort;

    /**
     * The thread on which this server runs.
     */
    private final Thread thread;

    /**
     * An exception that was thrown when this server was running.
     *
     * <p>This is thrown in the {@link #close()} method.</p>
     */
    private Exception exception;

    /**
     * A list of transactions.
     */
    private final List transactions;

    /**
     * Initialise.
     *
     * <p>Creates a server socket and a daemon thread to process requests.
     * Also starts the thread.</p>
     *
     * @throws IOException
     */
    public HttpServerMock() throws IOException {
        serverSocket = new ServerSocket(0);
        serverPort = serverSocket.getLocalPort();
        System.out.println("Bound to port " + serverPort);

        transactions = new ArrayList();

        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    // Javadoc inherited.
    public void run() {
        Socket socket = null;
        try {
            while (true) {
                try {
                    socket = serverSocket.accept();
                    if (transactions.size() > 0) {
                        HttpTransaction transaction = (HttpTransaction)
                                transactions.remove(0);

                        transaction.process(socket);
                    }

                } finally {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
                }
            }

        } catch (Exception e) {
            if (!serverSocket.isClosed()) {
                e.printStackTrace();
                exception = e;
            }
        }
    }

    /**
     * Get the server port.
     *
     * @return The server port.
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Close the server.
     *
     * @throws Exception If there was a problem during the running of the
     *                   server.
     */
    public void close() throws Exception {

        Exception exception = this.exception;

        try {
            serverSocket.close();
        } catch (Exception e) {
        }

        try {
            thread.join();
        } catch (Exception e) {
        }

        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Add a {@link SendResponseTransaction}.
     *
     * @param expectedRequest The expected request, if null the request content
     *                        is consumed but ignored.
     * @param responseContent The response to send back.
     */
    public void addTransaction(
            String[] expectedRequest, String[] responseContent) {

        if (responseContent == null) {
            throw new IllegalArgumentException(
                    "responseContent cannot be null");
        }

        HttpTransaction t = new SendResponseTransaction(expectedRequest,
                responseContent);
        addTransaction(t);
    }

    /**
     * Add a general {@link HttpTransaction}.
     *
     * @param transaction The transaction to add.
     */
    public void addTransaction(HttpTransaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Get a URL to the server.
     *
     * @param path The path.
     * @return The URL.
     * @throws MalformedURLException If there was a problem creating the URL.
     */
    public URL getURL(final String path) throws MalformedURLException {
        return new URL("http", "localhost", serverPort, path);
    }

    /**
     * Get a URL to the server as a string.
     *
     * @param path The path.
     * @return The URL as a string.
     * @throws MalformedURLException If there was a problem creating the URL.
     */
    public String getURLAsString(final String path)
            throws MalformedURLException {
        return getURL(path).toExternalForm();
    }

    /**
     * Get the server address, host + port.
     *
     * @return The server address.
     */
    public String getServerAddress() {
        return "localhost:" + serverPort;
    }

    public boolean hasTransactions() {
        return !transactions.isEmpty();
    }
}
