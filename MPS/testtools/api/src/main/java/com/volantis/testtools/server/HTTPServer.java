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
 * $Header: /src/mps/com/volantis/testtools/server/HTTPServer.java,v 1.1 2002/12/10 09:48:58 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Oct-02    steve           VBM:2002071604 Extended network server
 *                              that serves HTTP responses. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.server;

import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class HTTPServer extends NetworkServer
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * The logger to use
     */
    private static final Logger logger = 
        Logger.getLogger(HTTPServer.class);
    
    /** Creates a new instance of HTTPServer */
    /** Creates a new instance of HTMLServer */
    public HTTPServer( int port, int maxConnections )
    {
        super( port, maxConnections );
    }
    
    /** Send standard HTTP response 
     *  Use HTTP 1.0 for compatibility with all clients.
     */
    protected void printHTTPHeader(PrintWriter out ) {
        out.println(
            "HTTP/1.0 200 OK\r\n" +
            "Server: HTTPServer\r\n" +
            "Content-Type: " + getContentType() + "\r\n" +
            "\r\n" );
    }
    
    protected String getContentType()
    {
        return "text/plain";
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
