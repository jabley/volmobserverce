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
 * $Header: /src/mps/com/volantis/testtools/server/WMLServer.java,v 1.1 2002/12/10 09:48:58 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Oct-02    Steve           VBM:2002071604 - A tiny server that serves
 *                              WML. 
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
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class WMLServer extends HTTPServer
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * The logger to use
     */
    private static final Logger logger =
           Logger.getLogger(WMLServer.class);
    
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
                System.out.println( "WMLServer [ -port <port> ] [-connections <connections>] [ -file <filename> ]" );
                System.out.println( "If port is not specified, default is 8088" );
                System.out.print( "If file is not specified, the request headers " );
                System.out.println( "will be returned as an WML document." );
                System.exit( 0 );
            }
            argno++;
        }        
        
        logger.info( "Connecting to port " + port );
        WMLServer server = new WMLServer(port, connections);
        server.setFile( file );
        server.start();
    }
    
    /** Creates a new instance of WMLServer 
     * Listens to port 8088 and serves WML headers 
     */
    public WMLServer()
    {
        this( 8088, 0 );
    }
    
    /** Creates a new instance of WMLServer 
     * Listens to the specified port and serves WML headers 
     */
    public WMLServer( int port )
    {
        this( port, 0 );
    }

    /** Creates a new instance of WMLServer 
     * Listens to the specified port and serves WML headers. Dies after
     * the specified number of connections.
     */
    public WMLServer( int port, int maxConnections )
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
                logger.error( e );
            }
        }
    }
    
    /** Set the content to serve
     */
    public void setContent( StringBuffer buff ) {
        content = buff;
    }

    /**
     * return the WML content type
     */
    protected String getContentType() {
        return "text/vnd.wap.wml";
    }
    
    
    /** This is the method that provides the behavior
     *  to the server, since it determines what is
     *  done with the resulting socket. <B>Override this
     *  method in servers you write.</B>
     *  <P>
     *  @param server Socket that the server is attached to
     *
     */
    protected void handleConnection(Socket server) throws IOException
    {
        logger.info( "WMLServer: got connection from " +
                                    server.getInetAddress().getHostName());
   
        PrintWriter out = SocketUtil.getWriter(server);
        
        BufferedReader in = SocketUtil.getReader(server);
        Vector inputLines = new Vector();
        String line = in.readLine();
        while( line != null ) {
            inputLines.add( line );
            if ( line.length() == 0) { // Blank line
                if (usingPost(inputLines)) {
                    readPostData(inputLines, in);
                }
                break;
            }
            line = in.readLine();
        }
        

        printHTTPHeader( out );
        if( content == null ) {               
            printHeader(out);
            Iterator iter = inputLines.iterator();
            out.println( "<p>" );
            while( iter.hasNext() ) {
                line = (String)iter.next();
                out.print( line );
                out.println( "<br />" );
            }
            out.println( "</p>" );
            printTrailer(out);
        } else {
            out.println( content.toString() );
        }
        server.close();
    }

    /** Send standard HTTP response and top of a standard Web page.
     * Use HTTP 1.0 for compatibility with all clients.
     */
    protected void printHeader(PrintWriter out) {
        out.println(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<!DOCTYPE wml PUBLIC " +
            "\"-//WAPFORUM//DTD WML 1.1//EN\" " +
            "\"http://www.wapforum.org/DTD/DTD/wml1_1.1.xml\">\n" +
            "<wml>\n" +
            "<card>\n" );
    }

    // Print bottom of a standard Web page.

    protected void printTrailer(PrintWriter out) {
        out.println("</card></wml>" );
    }
        
    /** Normal Web page requests use GET, so this
     * server can simply read a line at a time.
     * However, WML forms can also use POST, in which
     * case we have to determine the number of POST bytes
     * that are sent so we know how much extra data
     * to read after the standard HTTP headers.
     */
    private boolean usingPost( Vector inputs) {
        String l = (String)inputs.elementAt( 0 );
        return( l.toUpperCase().startsWith("POST") );
    }

    private void readPostData( Vector inputs, BufferedReader in)
      throws IOException {
        int contentLength = contentLength(inputs);
        char[] postData = new char[contentLength];
        in.read(postData, 0, contentLength);
        inputs.add( new String(postData, 0, contentLength) );
  }

    // Given a line that starts with Content-Length,
    // this returns the integer value specified.
    private int contentLength( Vector inputs) {
        String input;
        Iterator iter = inputs.iterator();
        while( iter.hasNext() ) {
            String l = (String)iter.next();
            if ( l.length() == 0) {
                break;
            }
            input = l.toUpperCase();
            if (input.startsWith("CONTENT-LENGTH"))
            {
                return(getLength(input));
            }
        }
        return(0);
    }

    private int getLength(String length) {
        StringTokenizer tok = new StringTokenizer(length);
        tok.nextToken();
        return(Integer.parseInt(tok.nextToken()));
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
