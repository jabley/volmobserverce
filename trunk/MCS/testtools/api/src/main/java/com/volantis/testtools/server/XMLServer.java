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
 * $Header: /src/voyager/com/volantis/testtools/server/XMLServer.java,v 1.3 2003/03/20 15:15:34 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Oct-02    Steve           VBM:2002071604 - A tiny server that serves
 *                              XML. 
 * 14-Jan-03    Byron           VBM:2003010910 - Modified handleConnection to
 *                              call doDelay.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;

public class XMLServer extends HTTPServer
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * The log4j object to log to.
     */
    private static Category logger
    = Category.getInstance( "com.volantis.testtools.server.XMLServer");
    
    protected StringBuffer content;
    
    public static void main(String[] args) {
        
        BasicConfigurator.configure();        
        
        int port = 8088;
        File file = null;
        
        int argno = 0;
        int connections = 0;
        while( argno < args.length ) {
            if( args[ argno ].equals( "-port" ) ) {
                argno++;
                try {
                    port = Integer.parseInt(args[argno]);
                } 
                catch(NumberFormatException nfe) {
                    System.out.println( "Invalid port. Using 8088." );
                    port = 8088;
                }
            } else if( args[ argno ].equals( "-connections" ) ) {
                argno++;
                try {
                    connections = Integer.parseInt(args[argno]);
                } 
                catch(NumberFormatException nfe) {
                    System.out.println( "Invalid connections. Using 0." );
                    connections = 0;
                }                
            } else if( args[ argno ].equals( "-file" ) ) {
                argno++;
                try {
                    file = new File( args[ argno ] );
                }
                catch( NullPointerException npe ) {
                    System.out.println( "Invalid file." );
                    file = null;
                }
            } else {
                System.out.println( "WMLServer [ -port <port>][ -connections <connections>][ -file <filename>]" );
                System.out.println( "If port is not specified, default is 8088" );
                System.out.print( "If file is not specified, the request headers " );
                System.out.println( "will be returned as an WML document." );
                System.exit( 0 );
            }
            argno++;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Connecting to port " + port);
        }

        XMLServer server = new XMLServer(port, connections);
        server.setFile( file );
        server.listen();
    }
    
    /** Creates a new instance of HTMLServer 
     * Listens to port 8088 and serves HTML headers 
     */
    public XMLServer()
    {
        this( 8088, 0 );
    }
    
    /** Creates a new instance of HTMLServer 
     * Listens to the specified port and serves HTML headers 
     */
    public XMLServer( int port )
    {
        this( port, 0 );
    }

    /** Creates a new instance of HTMLServer 
     * Listens to the specified port and serves HTML headers. Dies after
     * the specified number of connections.
     */
    public XMLServer( int port, int maxConnections )
    {
        super( port, maxConnections );
        content = null;
    }
    
    
    /** Set the file to serve
     */
    public void setFile( File f ) {
        if( f != null ) {
            try {
                FileReader reader = new FileReader( f );
                StringBuffer buff = new StringBuffer();
                int c;
                while( ( c = reader.read() ) != -1 ) {
                    buff.append( (char)c );
                }
                reader.close();
                setContent( buff );
            }
            catch( Exception e ) {
                logger.error("unexpected-exception", e);
            }
        }
    }
    
    /** Set the content to serve
     */
    public void setContent( StringBuffer buff ) {
        content = buff;
    }
    
    /** This is the method that provides the behavior
     *  to the server, since it determines what is
     *  done with the resulting socket. <B>Override this
     *  method in servers you write.</B>
     *  <P>
     *  @param server Socket that the server is attached to
     *
     *
     */
    protected void handleConnection(Socket server) throws IOException
    {
        if (logger.isDebugEnabled()) {
            logger.debug("XMLServer: got connection from " +
                         server.getInetAddress().getHostName());
        }
        
        PrintWriter out = SocketUtil.getWriter(server);
        
        BufferedReader in = SocketUtil.getReader(server);
        Vector inputLines = new Vector();
        String line = in.readLine();
        while( line != null ) {
            if( line.length() == 0 ) break;
            inputLines.add( line );
            line = in.readLine();
        }
        

        printHTTPHeader( out );
        if( content == null ) {               
            printHeader(out);
            Iterator iter = inputLines.iterator();
            while( iter.hasNext() ) {
                line = (String)iter.next();
                out.println( "<header>" + line + "</header>" );
            }
            printTrailer(out);
        } else {
            if(logger.isDebugEnabled()){
                logger.debug( "Writing " + content.toString() );
            }
            out.println( content.toString() );
        }
        doDelay();
        server.close();
    }
    
    /** Send standard HTTP response and top of a standard Web page.
     * Use HTTP 1.0 for compatibility with all clients.
     *
     */
    protected void printHeader(PrintWriter out)
    {
        out.println(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<!DOCTYPE headers>\n" +
            "<headers>" );
    }
    
    protected void printTrailer(PrintWriter out)
    {
        out.println( "</headers>" );
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/1	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 ===========================================================================
*/
