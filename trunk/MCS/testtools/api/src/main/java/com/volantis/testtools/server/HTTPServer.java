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
 * $Header: /src/voyager/com/volantis/testtools/server/HTTPServer.java,v 1.2 2003/03/10 14:22:22 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Oct-02    steve           VBM:2002071604 Extended network server
 *                              that serves HTTP responses. 
 * 10-Mar-03    Steve           VBM:2003021101 - Ability to add headers at 
 *                              runtime for cookies and the like.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Category;

public class HTTPServer extends NetworkServer
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * The log4j object to log to.
     */
    private static Category logger
    = Category.getInstance( "com.volantis.testtools.server.HTTPServer");
    
    /** Extra headers to send */
    private List hdrs = null;
    
    /** Creates a new instance of HTTPServer */
    public HTTPServer( int port, int maxConnections ) {
        super( port, maxConnections );
        hdrs = new ArrayList();
    }
    
    /** Send standard HTTP response 
     *  Use HTTP 1.0 for compatibility with all clients.
     */
    protected void printHTTPHeader(PrintWriter out ) {
        StringBuffer hdr = new StringBuffer( "HTTP/1.0 200 OK\r\n" );
        
        hdr.append( "Server: HTTPServer\r\n" )
           .append( "Content-Type: " ).append( getContentType() ).append( "\r\n" );
        if( hdrs.size() > 0 ) {
            Iterator iter = hdrs.iterator();
            while( iter.hasNext() ) {
                String header = (String)iter.next();
                hdr.append( header ).append( "\r\n" );
            }
        }
        hdr.append( "\r\n" );
        out.println( hdr.toString() );
    }
    
    protected String getContentType() {
        return "text/plain";
    }

    /** Add a header to the standard HTTP header.
     * @param hdr the header to add.
     */
    public void addHeader( String hdr ) {
        hdrs.add( hdr );
    }
    
    /** Remove all extra headers
     */
    public void clearHeaders() {
        hdrs.clear();
    }
        
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
