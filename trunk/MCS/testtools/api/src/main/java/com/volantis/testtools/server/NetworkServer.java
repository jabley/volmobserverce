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
 * $Header: /src/voyager/com/volantis/testtools/server/NetworkServer.java,v 1.6 2003/03/20 15:15:34 sumit Exp $
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
 * 09-Dec-02    Steve           VBM:2002040812 - Added delay facility
 * 10-Dec-02    Steve           VBM:2002040812 - Added shutdown method
 * 14-Jan-03    Byron           VBM:2003010910 - Add serverActive. Renamed
 *                              shutDownFlag to shutDown. Modified listen() to
 *                              correctly terminate the thread in 'shutdown'
 *                              mode. Added isActive() and doDelay() method.
 *                              Modified delay mechanism simply to sleep for
 *                              the specified 'delay' ms.
 * 10-Feb-03    Steve           VBM:2002112504 - Problems with ProxyServer tests.
 *                              Removed the shutDownFlag completely since it is
 *                              not neccessary to check the connection every one
 *                              second to see if a shutdown is required. 
 *                              Instead, the ServerSocket that is listening is
 *                              made global to the class so that the shutdown()
 *                              method simply needs to close the listener. This
 *                              causes an IOException in any accept() calls that
 *                              are being called on the listener which are caught
 *                              for a clean exit. Obviously this only works 
 *                              because the server is in a thread and it therefore
 *                              able to close the socket under porgram control.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Category;

/**
 * This class listens for connections on the specified host's port. If a
 * connection has is being made it may sleep for the specified 'delay'
 * milliseconds. The server is shutdown by calling the shutdown method. Note
 * that this shutdown method does not guarentee that immediately after the
 * shutdown call is finished the server thread terminates. There may be a delay
 * of up to 1 second before the thread terminates.
 * <p>
 * This server will also terminate if there is a problem whislt connecting to a
 * socket.
 */
public class NetworkServer extends Thread
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * The log4j object to log to.
     */
    private static Category logger
    = Category.getInstance( "com.volantis.testtools.server.NetworkServer");

    private boolean serverActive = false;
    private ServerSocket listener = null;

    private int port, maxConnections, responseDelay;

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
        responseDelay = 0;
    }
    
    public void run() {
        listen();
    }

    /**
     * Monitor a port for connections. Each time one is established, pass
     * resulting Socket to handleConnection.
     */
    public void listen() {
        int i = 0;
        try {
            listener = new ServerSocket(port);
        }
        catch( IOException ioe ) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to open server socket.", ioe);
            }
            return;
        }
        
        while ((i++ < maxConnections) || (maxConnections == 0)) {
            try {
                serverActive = true;
                Socket so = listener.accept();  
                handleConnection( so );
                serverActive = false;
            } catch (IOException e ){
                serverActive = false;
                if (logger.isDebugEnabled()) {
                    logger.debug("Got an IOException", e);
                }
                break;
            }
        }
    }
    
    
    /**
     * Shut down the server. Care must be taken if you immediately start up
     * another server on the same port as this one. The shutdown may take as
     * long as 1 second to complete (timeout for the listener).
     */
    public synchronized void shutdown() {
        if (listener != null) {
            try {
                listener.close();
            } catch (IOException ioe) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Got an IOException", ioe);
                }
            }
        }
    }

    /**
     * Return the status of the server: true if the server is ready to connect,
     * false otherwise.
     *
     * @return      true if the server is ready to connect, false otherwise.
     */
    public boolean isActive() {
        return serverActive;
    }


    /**
     * This is the method that provides the behavior to the server, since it
     * determines what is done with the resulting socket. <B>Override this
     * method in servers you write.</B> <P> This generic version simply reports
     * the host that made the connection, shows the first line the client sent,
     * and sends a single line in response.
     *
     * @param server Socket that the server is attached to
     */
    protected void handleConnection(Socket server)
      throws IOException{
        BufferedReader in = SocketUtil.getReader(server);
        PrintWriter out = SocketUtil.getWriter(server);
        if(logger.isDebugEnabled()){
            logger.debug( "got connection from " +
                server.getInetAddress().getHostName() + "\n" +
                "with first line '" + in.readLine() + "'");
        }
        doDelay();
        out.println("Generic Network Server");
        server.close();
    }

    /**
     * Delay the connection for the user specified number of milliseconds.
     * This is used for testing the timeout.
     */
    protected void doDelay() {
        if( getDelay() != 0 ) {
            try {
                sleep(getDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
    
    /** Set the delay period to wait after connection before responding.
     */
    public void setDelay( int millisecs ) {
        this.responseDelay = millisecs;
    }
    
    /** Get the response delay
     */
    public int getDelay() {
        return this.responseDelay;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
