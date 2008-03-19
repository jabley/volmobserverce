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
 * $Header: /src/voyager/com/volantis/mcs/utilities/HttpResponseHeader.java,v 1.8 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jul-02    Steve           VBM:2002071604 - Class to parse and hold the
 *                              headers returned by an HTTP response.
 * 10-Sep-02	Mat		            VBM:2002040825 - To skip over the 2 return
 *                              characters at the end of the response
 *                              parseHeaders() needed to consume an extra
 *                              line at the end of the loop.
 * 14-Oct-02    Sumit           VBM:2002091202 readNextLine checked changed
 *                              to look for 10 and then 13
 * 08-Nov-02    Steve           VBM:2002071604 - Do not skip the second blank
 *                              line at the end of the header.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis a new method
 *                              getHeaders().
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import java.io.Reader;
import java.io.IOException;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.NameValuePair;

/**
 * Class to read and parse HTTP response headers from a server.
 * The server should be connected to via a Socket and the request sent
 * to the socket in HTTP format. The server will respond with the HTTP
 * response via the Socket. 
 *
 * This class should be used to read the headers of the response 
 * <code>
 *   InputStreamReader reader = new InputStreamReader(socket.getInputStream());
 *   HttpResponseHeader header = new HttpResponseHeader( reader );
 * </code>
 */
public class HttpResponseHeader
{
    /** Copyright */
    private static String mark = "(c) Volantis Systems Ltd 2000.";
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(HttpResponseHeader.class);
    
    /** The header lines */
    private Map headers;
    
    /** The HTTP Version */
    private String version;
    
    /** HTTP Response code */
    private int errorCode;
    
    /** Response message */
    private String message;
    
    /** Creates a new instance of HttpResponseHeader. The constructor is passed
     * a Reader object which should be attached to the input stream of a Socket
     * @param reader A Reader object attached to the input stream of a Socket
     */
    public HttpResponseHeader( Reader reader )
    {
        headers = new HashMap();
        
        version = null;
        message = null;
        errorCode = 0;
        
        parseHeaders( reader );
    }

    /** Return the HTTP version from the header
     * @return The HTTP version as a string eg HTTP/1.0 or HTTP/1.1
     */
    public String getVersion()
    {
        return version;
    }
    
    /** Return the error code that was in the headers from this HTTP response
     * @return The HTTP error code as an int
     */
    public int getErrorCode()
    {
        return errorCode;
    }
    
    /** Return the message that followed the error code in the first line
     * of the header
     * @return The HTTP message accompanying the error code
     */
    public String getMessage()
    {
        return message;
    }
    
    /** Get a named header value
     * @param headerName the name of the header to get the value for
     * @return the header value as a String or null if the header doesnt exist
     */
    public String getHeader( String headerName )
    {
        NameValuePair pair = (NameValuePair)headers.get( headerName );
        if( pair == null )
        {
            return null;
        }
        return pair.getValue();
    }
    
    /** Get all headers
     * @return the headers value as a Map of NameValuePair's
     */
    public Map getHeaders() {
        return headers;
    }
        
    /** Check whether or not a header exists. This is usefull in cases where
     * headers have been recieved but have no value.
     */
    public boolean headerExists( String headerName )
    {
        return ( ( headers.get( headerName ) == null ) ? false : true );
    }
    
    /** Parse the headers from an HTTP response
     * @param reader A Reader that is attached to the Socket that the HTTP
     * response is coming from.
     */
    private void parseHeaders( Reader reader )
    {
        boolean first = true;
        
        String line = readNextLine( reader );
        if(logger.isDebugEnabled()){
            logger.debug( "Received header line " + line );
        }
        while( ( line != null ) && ( line.length() > 0 ) )
        {
            if( first )
            {
                first = !first;
                parseFirstLine( line );
            } else {
                NameValuePair pair = parseHeaderLine( line );
                if( pair != null )
                {
                    headers.put( pair.getName(), pair );
                }   
            }
            line = readNextLine( reader );
            if(logger.isDebugEnabled()){
                logger.debug( "Received header line " + line );
            }
        }     
        // Skip next line, but only if it wasnt the end of the stream
//        if( line != null )
//        {
//            line = readNextLine( reader );
//            logger.debug( "Received header line " + line );
//        }
    }

    /** Read a line of input from a Reader object.
     * This is implemented here since the Reader may not be a BufferedReader if
     * we are reading binary data for example. Since Reader does not have its own
     * getLine() method, this method will read ASCII data up to the next carriage
     * return.
     */
    private String readNextLine( Reader reader )
    {
        int ch;
        
        StringBuffer line = new StringBuffer();
        
        ch = 0;
        while( ch != 10 )
        {
            try {
                ch = reader.read();
            }
            catch( IOException ioe )
            {
                ch = 0;
            }
            if( ch == 13 ) 
            {
                ch = 0;
            }
            if( ch > 0 )
            {
                
                char chr = (char)ch;
                line.append( chr );
            }
        }
        return line.toString().trim();
    }
    
    /** Parse the first line of an HTTP response
     * The first line is of the format HTTP/1.x<space>error code<space>data
     * where the HTTP/1.x is the HTTP version 1.0 or 1.1. This method extracts
     * the version number and error code and stores them in class variables.
     * Any extra data is stored in the message variable
     */
    private void parseFirstLine( String line )
    {
        if( line == null )
        {
            return;
        }
        
        String trimmed = line.trim();
        if( trimmed.length() == 0 ) 
        {
            return;
        }
        
        int http = trimmed.indexOf( ' ' );
        if( http > -1 )
        {
            version = trimmed.substring( 0, http );
            int ecode = trimmed.indexOf( ' ', http + 1 );
            if( ecode > -1 )
            {
                try {
                    errorCode = Integer.parseInt( 
                                    trimmed.substring( http, ecode ).trim() );
                }
                catch( NumberFormatException nfe )
                {
                    errorCode = 0;
                }
                message = trimmed.substring( ecode + 1 );
            }
        }
    }
    
    /** Parse an HTTP header line.
     * Header lines are assumed to be of the form headerName: value
     * for example User-Agent: Mozilla ....
     * This method splits the line on the : character and anything to
     * the right of it is assumed to be the header name with anything
     * to the right being the value. If there is no : character then
     * the whole line is taken to be the header name and no value is
     * assigned.
     */
    private NameValuePair parseHeaderLine( String line )
    {
        if( line == null )
        {
            return null;
        }
        
        String trimmed = line.trim();
        if( trimmed.length() == 0 ) 
        {
            return null;
        }
        
        int colon = trimmed.indexOf( ':' );
        if( colon > -1 )
        {
            return new NameValuePair( trimmed.substring( 0, colon ),
                                      trimmed.substring( colon + 1 ).trim() );
        } else {
            return new NameValuePair( trimmed, null );
        }
    }
      
    /** Return a string representation of the HTTP header.
     */
    public String toString()
    {
        String s = "Version=" + version + 
                   " Error code=" + Integer.toString( errorCode );
        
        Set keys = headers.keySet();
        Iterator iter = keys.iterator();
        while( iter.hasNext() )
        {
            String key = (String)iter.next();
            NameValuePair pair = (NameValuePair)headers.get(key);
            s = s + "\n" + pair.toString();
        }
        return s;
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

 12-Aug-03	1050/1	byron	VBM:2003081203 Port NameValuePair from MCS to synergetics

 ===========================================================================
*/
