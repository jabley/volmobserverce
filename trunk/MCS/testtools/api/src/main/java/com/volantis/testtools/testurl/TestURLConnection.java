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
 * $Header: /src/voyager/com/volantis/testtools/marinerurl/MarinerURLConnection.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815   Handle a connection to a
 *                              marinerurl URL.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.testurl;

import org.apache.log4j.Category;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;


public class TestURLConnection extends URLConnection {
    /** Copyright */
    private static String mark="(c) Volantis Systems Ltd 2000.";

    /**
     * The log4j object to log to.
     */
    private static Category logger=Category.getInstance(
            "com.volantis.mcs.testtools.testurl.MarinerURLConnection");

    /** The input stream for this connection */
    private InputStream is=null;

    /** The headers */
    private Map headers=null;

    /** Request properties map */
    private Map requestProperties=null;

    /** The name of the URL channel */
    private String channel;

    /** List of keys for quick access */
    private Vector keys=null;

    /** Creates a new instance of MarinerURLConnection. The channel name
     * is stored in the <code>file</code> member of the URL. To open the
     * channel 'fred' you would use :-
     * <code>URL url = new URL( "testurl:fred" );</code>
     *
     * @param url  The URL to connect to.
     */
    public TestURLConnection(URL url) {
        super(url);
        requestProperties=new Hashtable();
        channel=url.getFile();
        headers=new Hashtable();
        keys=new Vector();
    }

    /** Returns the value for the <code>n</code><sup>th</sup> header field.
     * It returns <code>null</code> if there are fewer than
     * <code>n</code> fields.
     * <p>
     * This method can be used in conjunction with the
     * <code>getHeaderFieldKey</code> method to iterate through all
     * the headers in the message.
     *
     * @param   n   an index.
     * @return  the value of the <code>n</code><sup>th</sup> header field.
     */
    public synchronized String getHeaderField(int n) {
        if (!connected) {
            throw new IllegalStateException("Not connected.");
        }

        String k=getHeaderFieldKey(n);

        if (k!=null) {
            return getHeaderField(k);
        } else {
            return null;
        }
    }

    /** Returns the value of the named header field.
     * If called on a connection that sets the same header multiple times
     * with possibly different values, only the last value is returned.
     *
     * @param   name   the name of a header field.
     * @return  the value of the named header field, or <code>null</code>
     *          if there is no such field in the header.
     *
     */
    public synchronized String getHeaderField(String name) {
        if (!connected) {
            throw new IllegalStateException("Not connected.");
        }

        if (name==null) {
            return null;
        }

        return (String)headers.get(name.toLowerCase());
    }

    /** Returns the key for the <code>n</code><sup>th</sup> header field.
     *
     * @param   n   an index.
     * @return  the key for the <code>n</code><sup>th</sup> header field,
     *          or <code>null</code> if there are fewer than <code>n</code>
     *          fields.
     *
     */
    public synchronized String getHeaderFieldKey(int n) {
        if (!connected) {
            throw new IllegalStateException("Not connected.");
        }

        if (n<keys.size()) {
            return (String)keys.elementAt(n);
        } else {
            return null;
        }
    }

    /** Returns an input stream that reads from this open connection.
     *
     * @return     an input stream that reads from this open connection.
     * @exception  IOException              if an I/O error occurs while
     *               creating the input stream.
     * @exception  UnknownServiceException  if the protocol does not support
     *               input.
     *
     */
    public InputStream getInputStream()
        throws IOException {
        return is;
    }

    /** Sets the general request property. If a property with the key already
     * exists, overwrite its value with the new value.
     *
     * @param   key     the keyword by which the request is known
     *                  (e.g., "<code>accept</code>").
     * @param   value   the value associated with it.
     * @throws IllegalStateException if already connected
     * @throws NullPointerException if key is <CODE>null</CODE>
     */
    public void setRequestProperty(String key, String value) {
        if (connected) {
            throw new IllegalStateException("Already connected");
        }

        if (key==null) {
            throw new NullPointerException("key is null");
        }

        requestProperties.put(key, value);
    }

    /** Returns the value of the named general request property for this
     * connection.
     *
     * @param key the keyword by which the request is known (e.g., "accept").
     * @return  the value of the named general request property for this
     *           connection. If key is null, then null is returned.
     * @throws IllegalStateException if already connected
     */
    public String getRequestProperty(String key) {
        if (connected) {
            throw new IllegalStateException("Already connected.");
        } else {
            return (String)requestProperties.get(key);
        }
    }

    /** Opens a communications link to the resource referenced by this
     * URL, if such a connection has not already been established.
     * If the <code>connect</code> method is called when the connection
     * has already been opened (indicated by the <code>connected</code>
     * field having the value <code>true</code>), the call is ignored.
     *
     * @exception  IOException  if an I/O error occurs while opening the
     *               connection.
     */
    public void connect()
        throws IOException {
        if (!connected) {
            if (channel.equals(TestURLRegistry.ERROR_ON_CONNECT)) {
                throw new IOException("Simulated fault on connection.");
            }

            is=TestURLRegistry.getInstance().getInputStream(channel);

            if (isHTTP()) {
                readHeaders();
            }

            if (is==null) {
                throw new IOException("No testurl registered with channel " +
                    channel);
            }

            connected=true;
        }
    }

    /** Returns a <code>String</code> representation of this URL connection.
     *
     * @return  a string representation of this <code>URLConnection</code>.
     */
    public String toString() {
        return this.getClass().getName() + ":" + url;
    }

    /** Determine if the input stream is an HTTP response. If it is then the
     * first 7 characters will be 'HTTP/1.' as in HTTP/1.1 or HTTP/1.0.
     * @return true is this is an HTTP response or false otherwise.
     * @throws IOException if an error occurs while reading the stream.
     */
    protected boolean isHTTP()
        throws IOException {
        int c1;
        int c2;
        int c3;
        int c4;
        int c5;
        int c6;
        int c7;

        is.mark(10);
        c1=is.read();
        c2=is.read();
        c3=is.read();
        c4=is.read();
        c5=is.read();
        c6=is.read();
        c7=is.read();
        is.reset();

        if ((c1=='H') && (c2=='T') && (c3=='T') && (c4=='P') && (c5=='/') &&
              (c6=='1') && (c7=='.')) {
            return true;
        } else {
            return false;
        }
    }

    /** Read the headers from an HTTP response. Headers are denoted by name
     * value pairs separated by the ':' character.
     * @throws IOException if an error occurs while reading the stream.
     */
    protected void readHeaders()
        throws IOException {
        String status=readLine();
        String header;

        while (((header=readLine())!=null) && (!header.trim().equals(""))) {
            int colon=header.indexOf(":");

            if (colon>=0) {
                String key=header.substring(0, colon).trim();
                String value=header.substring(colon + 1).trim();
                keys.addElement(key);
                headers.put(
                    key.toLowerCase(),
                    value);
            }
        }
    }

    /** Read the next ASCII line from an input stream. The line ends when
     * either a CR/LF pair or LF occurs or the stream ends.
     * @return the next line of the stream as a String
     * @throws IOException if an error occurs while reading the stream.
     */
    protected String readLine()
        throws IOException {
        StringBuffer result=new StringBuffer();
        int chr;

        while (((chr=is.read())!=-1) && (chr!=10) && (chr!=13)) {
            result.append((char)chr);
        }

        if ((chr==-1) && (result.length()==0)) {
            return null;
        }

        if (chr==13) {
            is.mark(1);

            if (is.read()!=10) {
                is.reset();
            }
        }

        return result.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Aug-03	956/3	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 ===========================================================================
*/
