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
 * $Header: /src/mps/com/volantis/testtools/server/NetworkServer.java,v 1.2 2003/03/26 17:43:14 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Oct-02    Steve           VBM:2002071604 - A tiny server that serves
 *                              to sockets
 *                              Taken from Core Servlets and JSP,
 *                              http://www.apl.jhu.edu/~hall/csajsp/.
 *                              1999 Marty Hall; may be freely used or adapted.
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class NetworkServer extends Thread
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * The logger to use
     */
    private static final Logger logger =
        Logger.getLogger(NetworkServer.class);
    
    private int port, maxConnections;

    /** Build a server on specified port. It will continue
    *  to accept connections, passing each to
    *  handleConnection until an explicit exit
    *  command is sent (e.g., System.exit) or the
    *  maximum number of connections is reached. Specify
    *  0 for maxConnections if you want the server
    *  to run indefinitely.
    * @param port int the port number to listen to
    * @param maxConnections int the number of connections to allow
    * before dying or 0 for an inifinte number
    */

    public NetworkServer(int port, int maxConnections) {
        setPort(port);
        setMaxConnections(maxConnections);
    }
    
    public void run() {
        listen();
    }

    /** Monitor a port for connections. Each time one
    *  is established, pass resulting Socket to handleConnection.
    */

    public void listen() {
        int i=0;
        try {
            ServerSocket listener = new ServerSocket(port);
            Socket server;
            while((i++ < maxConnections) || (maxConnections == 0)) {
                server = listener.accept();
                handleConnection(server);
            }
        } catch (IOException ioe) {
            logger.error( "IOException", ioe );
        }
    }

    /** This is the method that provides the behavior
    *  to the server, since it determines what is
    *  done with the resulting socket. <B>Override this
    *  method in servers you write.</B>
    *  <P>
    *  This generic version simply reports the host
    *  that made the connection, shows the first line
    *  the client sent, and sends a single line
    *  in response.
    *  @param server Socket that the server is attached to
    */
    protected void handleConnection(Socket server)
      throws IOException{
        BufferedReader in = SocketUtil.getReader(server);
        PrintWriter out = SocketUtil.getWriter(server);
        if(logger.isDebugEnabled()) {
            logger.debug( "got connection from " +
                server.getInetAddress().getHostName() + "\n" +
                "with first line '" + in.readLine() + "'");
        }
        out.println("Generic Network Server");
        server.close();
    }

    /** Gets the max connections server will handle before
    *  exiting. A value of 0 indicates that server
    *  should run until explicitly killed.
    */
    public int getMaxConnections() {
        return(maxConnections);
    }

    /** Sets max connections. A value of 0 indicates that
    *  server should run indefinitely (until explicitly
    *  killed).
    */
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /** Gets port on which server is listening. */
    public int getPort() {
        return(port);
    }

    /** Sets port. <B>You can only do before "connect"
    *  is called.</B> That usually happens in the constructor.
    */
    protected void setPort(int port) {
        this.port = port;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
