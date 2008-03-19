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
 * $Header: /src/voyager/com/volantis/mcs/utilities/HttpClient.java,v 1.11 2003/02/20 11:48:10 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Apr-02    Steve           VBM:2002040812 Network client for extrnal
 *                              communication with an Http server
 * 01-May-02    Mat             VBM:2002040815 - Added code to read the result
 *                              of the call and to remove all lines up to
 *                              a line starting with <?xml
 * 27-Aug-02    Steve           VBM:2002071604 - Class no longer removes
 *                              lines until the start with <?xml since it is
 *                              not guaranteed that XML is being read. Instead
 *                              the headers are parsed into an
 *                              HttpResponseHeader class which can be retrieved
 *                              and interrogated. getBufferedReader() now does
 *                              the same as the old getReader() method.
 * 23-Oct-02    Steve           VBM:2002071604 - Modified handleConnection to
 *                              take account of connection timeouts and added
 *                              a new method to get the input stream from the
 *                              socket that does not go through a BufferedReader
 *                              to handle non-ascii input.
 * 11-Nov-02    Steve           VBM:2002040812 If connecting with parameters,
 *                              the protocol should be 'http' not 'HTTP'.
 * 10-Dec-02    Byron           VBM:2002120902 - Added logging and updated
 *                              code formatting. Fixed logger name.
 * 13-Jan-03    Byron           VBM:2003010910 - Modified handleConnection so
 *                              it does not reset the timeout value.
 * 23-Jan-03    Sumit           VBM:2003011004 - Removed the debug section of
 *                              code in getBufferedReader() which was stripping
 *                              linebreaks out of the socket input
 * 07-Feb-03    Steve           VBM:2002071604 - Upgraded exception logging to
 *                              a warning in handleConnection()
 * 20-Feb-03    Steve           VBM:2003022001 - Deprecated this class.
 *                              please use org.apache.commons.httpclient 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import java.util.Collection;
import java.util.Iterator;

import java.net.Socket;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * @author steve
 * @deprecated Use org.apache.commons.httpclient.HttpClient instead
 */
public class HttpClient extends AbstractNetworkClient {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(HttpClient.class);

    /** The URI to connect to on the host */
    protected String uri;

    /** Request headers to send with the client request */
    protected Collection requestHeaders;

    /** The reader for the response from the server */
    protected Reader inputReader;

    /** The headers returned by the server in the response */
    private HttpResponseHeader responseHeaders;

    // Buffer size for the buffered reader.
    protected static final int BUFFER_SIZE = 1024;

    /** Creates an instance of HttpClient.  Use this constructor to create
     * a reusable instance of HttpClient and use the set methods to set the
     * relevant values each time you need to call it.
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public HttpClient()
            throws UnknownHostException, IOException
    {
    }

    /** Create an instance of an HTTP client.
     * <p>
     * This constructor can be used for a one off connection.
     * @param host The name of the HTTP host
     * @param port The port number to connect to on the server
     * @param uri The URI to request
     * @param headers A Collection of String headers to send to the server
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public HttpClient(String host, int port, String uri,
                      Collection headers)
            throws MalformedURLException, UnknownHostException, IOException
    {
        URL url = new URL("http", host, port, uri);
        connectToURL(url, headers);
    }


    /** Create an HTTP client that connects to a URL
     * @param url A URL object defining the file to retrieve
     * @param requestHeaders A Collection of request headers to send to the
     * server.
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public HttpClient(URL url, Collection requestHeaders)
            throws UnknownHostException, IOException
    {
        connectToURL(url, requestHeaders);
    }

    /** Create an HTTP client that connects to a URL passed as a String
     * @param host The URL of the file to retrieve
     * @param requestHeaders A Collection of request headers to send to the
     * server.
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public HttpClient(String host, Collection requestHeaders)
            throws MalformedURLException, UnknownHostException, IOException
    {
        URL url = new URL(host);
        connectToURL(url, requestHeaders);
    }

    /** Connect to a URL
     * @param url The file to retrieve as a URL object
     * @param headers   A Collection of request headers to send to the
     * server.
     */
    private void connectToURL(URL url, Collection headers)
            throws UnknownHostException, IOException
    {
        setHost(url.getHost());
        setPort(url.getPort());
        setUri(url.getPath());
        setRequestHeaders(headers);
        connect();
    }


    /** Set the URI on the server for the requested file.
     * @param uri The URI to connect to
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /** Set the request headers to send to the server on connection.
     * @param headers A Collection of String objects to send.
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public void setRequestHeaders(Collection headers) {
        this.requestHeaders = headers;
    }

    /** Handle a sucessful connection to the HTTP server.
     * @param socket The Socket being used to talk to the server.
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    protected void handleConnection(Socket socket)
            throws IOException {

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Output the URI and HTTP Version
        String msg = "GET " + uri + " HTTP/1.0";
        if (logger.isDebugEnabled()) {
            logger.debug("Connected to " + uri);
            logger.debug(msg);
        }
        out.println(msg);

        // Send any request headers
        if (logger.isDebugEnabled()) {
            logger.debug("Starting to write out headers.");
        }
        if (requestHeaders != null) {
            Iterator iter = requestHeaders.iterator();
            while (iter.hasNext()) {
                String header = (String) iter.next();
                if (logger.isDebugEnabled()) {
                    logger.debug("Header=" + header);
                }
                out.println(header);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Finished writing headers.");
        }
        // End the headers
        out.println();

        // We are now ready to get the input back via getReader()
        inputReader = new InputStreamReader(socket.getInputStream());
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Starting to read headers.");
            }
            responseHeaders = new HttpResponseHeader(inputReader);
            if (logger.isDebugEnabled()) {
                logger.debug("Finished reading headers.");
            }
        } catch (Exception ste) {
            responseHeaders = null;
            logger.warn("error-reading-headers", ste );
        }
    }

    /** Create and return a BufferedReader from the socket input stream.
     * @return A BufferedReader the first line of which will be the source
     *              following the HTTP response headers.
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public BufferedReader getBufferedReader() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(inputReader,
                                                           BUFFER_SIZE);
        return bufferedReader;
    }

    /** Get the InputStreamReader from the socket.
     * @return InputStreamReader to the input from the socket.
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public Reader getInputStreamReader() throws IOException
    {
        return inputReader;
    }


    /** Return the response headers read from the HTTP Response
     * @return The headers in an HttpResponseHeader object
     * @deprecated Use jakarta apache commons HttpClient instead
     */
    public HttpResponseHeader getResponseHeaders()
    {
        return responseHeaders;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05	7240/1	emma	VBM:2005022812 mergevbm from MCS 3.3

 02-Mar-05	7214/1	emma	VBM:2005022812 Fixing leftover localization logging problems

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 27-May-04	4075/1	ianw	VBM:2004041408 Ported forward ATG changes and merged

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
