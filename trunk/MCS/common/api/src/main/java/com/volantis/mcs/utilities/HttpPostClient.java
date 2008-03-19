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
 * $Header: /src/voyager/com/volantis/mcs/utilities/Attic/HttpPostClient.java,v 1.1.2.2 2002/08/27 14:10:15 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Aug-02    Chris W         VBM:2002081511           Created.
 *                              AbstractNetworkClient, HttpClient and HttpPostClient
 *                              make it easy for the Remap servlet to send POST
 *                              requests to ATG and receive ATG's response.
 * 27-Aug-02    Chris W         VBM:2002081511 - debug logging added in
 *                              handleConnection()
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;


public class HttpPostClient extends HttpClient {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(HttpPostClient.class);

    /**
     * NameValuePairs containing data that will be sent using HTTP POST
     */
    private String postData;
    
    /** Create an instance of an HTTP Post client
     * This constructor can be used for a one off connection.
     * @param host     The name of the HTTP host
     * @param port     The port number to connect to on the server
     * @param uri      The URI to request
     * @param headers  A Collection of String headers to send to the server
     */
    public HttpPostClient(String host, int port, String uri, Collection requestHeaders)
                throws UnknownHostException, IOException
    {
        super(host, port, uri, requestHeaders);        
    }
    
    /**
     * Creates an instance of HttpClient.  Use this constructor to create 
     * a reusable instance of HttpClient and use the set methods to set the
     * relevant values each time you need to call it.
     */
    public HttpPostClient() throws UnknownHostException, IOException
    {
    }        
    
    /**
     * Outputs a HTTP POST request
     * @param socket The Socket to which the HTTP POST is written
     * @throws IOException
     */
    protected void handleConnection(Socket socket) throws IOException
    {
        PrintWriter out = new PrintWriter( socket.getOutputStream(), true );
        
        // Output the URI and HTTP Version
        out.println("POST " + uri + " HTTP/1.0" );
        if (logger.isDebugEnabled()) {
            logger.debug("POST " + uri + " HTTP/1.0");
        }
        
        // Send any request headers
        Iterator iter = requestHeaders.iterator();
        while( iter.hasNext() ) {
            String header = (String)iter.next();
            out.println( header );
            if (logger.isDebugEnabled()) {
                logger.debug(header);
            }
        }
        
        // Tell server it's a POST request
        out.println("Content-Type: application/x-www-form-urlencoded");
        if (logger.isDebugEnabled()) {
            logger.debug("Content-Type: application/x-www-form-urlencoded");
        }
        
        
        // Tell server length of POST data
        out.println("Content-Length: "+getPostData().length());
        if (logger.isDebugEnabled()) {
            logger.debug("Content-Length: "+getPostData().length());
        }
        
        // End the headers
        out.println();        
        if (logger.isDebugEnabled()) {
            logger.debug("writing blank line");
        }
        
        // Send the POST data
        if (logger.isDebugEnabled()) {
            logger.debug("about to write POST data:"+getPostData());
        }
        out.println(postData);        
                
        out.flush();        
        
        // We are now ready to get the input back via getReader()
        // For compatability with HttpClient superclass
        InputStreamReader inStream =
        new InputStreamReader( socket.getInputStream() );
        inputReader = new BufferedReader( inStream, BUFFER_SIZE );                
    }
    
    /**
     * Sets the String to be POSTed. It is assumed that the string has
     * already been url encoded.
     * @param thePostData.
     */
    public void setPostData(String postData)
    {
        this.postData = postData;
    }
    
    /**
     * Gets the String to be POSTed
     * @return String
     */
    public String getPostData()
    {
        return postData;
    }
    
    /**
     * Returns a Reader containing the reply.
     * @return Reader
     * @throws IOException
     */
    public Reader getReader() throws IOException
    {
        return inputReader;
    }
        
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 27-Apr-04	3843/2	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
