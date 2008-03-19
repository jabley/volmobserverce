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
 * $Header: /src/voyager/com/volantis/mcs/utilities/AbstractNetworkClient.java,v 1.9 2003/02/07 10:39:18 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Apr-02    Steve           VBM:2002040812 Network client for extrnal
 *                              client communication
 * 31-May-02    Steve           VBM:2002052902 - If the passed port is -1 which
 *                              is the default if no port number is passed to
 *                              the URL class, then assume that port 80 is
 *                              required.
 * 02-Aug-02    Steve           VBM:2002052902 - If the
 *                              setPort() method is called with a port number
 *                              of -1 then the port is set to a default of 80.
 *                              The setPort() method was not there when the
 *                              previous fix was done.
 * 27-Aug-02    Steve           VBM:2002071604 - Added javadoc for methods
 * 23-Oct-02    Steve           VBM:2002071604 - Sets a 15 second timeout for
 *                              connection. connect() can now throw a
 *                              SocketException if the timeout triggers.
 * 09-Dec-02    Byron           VBM:2002120902 - Added logger and TIMEOUT
 *                              member variables. Added logging to connect()
 *                              method.
 * 10-Jan-03    Byron           VBM:2003010910 - Made TIMEOUT public, static.
 *                              Added connect(int), modified connect() to call
 *                              connect(int).
 * 06-Feb-03    Steve           VBM:2002071604 - Fixed up javadoc for methods
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.io.IOException;

import java.net.UnknownHostException;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class AbstractNetworkClient
 {
    /** The name of the host we are connecting to
     */
    protected String host;

    /** The port on the host we are connecting to
     */
    protected int port;

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractNetworkClient.class);

    /**
     * The timeout for a socket connection in milliseconds
     */
    public static final int TIMEOUT = 15000;

    /**
     * Create a new AbstractNetWorkClient.
     */
    public AbstractNetworkClient() {
    }

    /** Register host and port.
     * The connection will not be established until you call connect().
     * @param host  The name of the server host
     * @param port  The port to connect to the server on
     */
    public AbstractNetworkClient(String host, int port)
    {
        this.host = host;
        if (port == -1)
        {
            this.port = 80;
        } else {
            this.port = port;
        }
    }

    /**
     * Establishes the connection, then passes the socket to handleConnection.
     *
     * @throws UnknownHostException if the host is unknown
     * @throws SocketException      if there is a socket exception
     * @throws IOException          if there is an IO exception
     * @see #connect(int)
     */
    public void connect()
            throws UnknownHostException, SocketException, IOException {

        connect(TIMEOUT);
    }

    /**
     * Establishes the connection, then passes the socket to handleConnection.
     *
     * @param  timeout              the timeout value in milliseconds
     * @throws UnknownHostException if the host is unknown
     * @throws SocketException      if there is a socket exception
     * @throws IOException          if there is an IO exception
     * @see    #connect(int)
     */
    public void connect(int timeout)
            throws UnknownHostException, SocketException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Connecting to '" +
                         host + "' with port '" +
                         port + "' with timeout of " +
                         timeout + "ms");
        }
        // Throws an unknown host exception if the host does not exist
        InetAddress.getByName(host);
        Socket client = new Socket(host, port);
        client.setSoTimeout(timeout);
        handleConnection(client);
    }

    /** This is the method you will override when
     * making a network client for your task.
     * @param client  A Socket connection to the server.
     */
    protected abstract void handleConnection(Socket client) throws IOException;

    /** Return the hostname of the server we're contacting.
     * @return the host name
     */
    public String getHost() {
        return (host);
    }

    /** Set the name of the host to connect to
     * @param host  The host name
     */
    public void setHost(String host) {
        this.host = host;
    }

    /** Return the port that connection will be made on.
     * @return the port number that the client will connect to the server on
     */
    public int getPort() {
        return (port);
    }

    /** Set the server port to connect to
     * @param port  The server port number
     */
    public void setPort(int port) {
        if (port == -1)
        {
            this.port = 80;
        } else {
            this.port = port;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
