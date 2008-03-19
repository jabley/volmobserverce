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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.vdp.scs.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A class representing HTTP/HTTPs requests, mapping its all params to class fields.
 */
public class Request {

    private String method;
    private String url;
    private String uri;
    private String protocol;
    private String host;
    private String type;

    private int contentLength;
    private String connection;
    private int keepAliveTimeout;

    private List params;

    private boolean correct;

    private byte[] content;

    private static final int KEEP_ALIVE_TIMEOUT = 300000;   //in milliseconds

    /**
     * Creates the Reqeust object basing on http/https request given as a String.
     * @param data http/https request given as a String
     */
    public Request(String data) {
        this.params = new ArrayList();
        this.correct = false;
        this.content = new byte[0];
        this.contentLength = 0;
        this.connection = "close";
        this.keepAliveTimeout = 0;

        String[] tempParams = data.split(ConstData.EMPTY_LINE);

        setHeaderLine(tempParams[0]);
        setHeader(tempParams);

    }

    /**
     * Returns method type of this request.
     * @return method type of this request, for example POST or GET.
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Returns destination url that this request was sent to.
     * @return destination url that this request was sent to
     */
    public String getURL() {
        return this.url;
    }

    /**
     * Returns destination uri that this request was sent to.
     * @return destination uri that this request was sent to
     */
    public String getURI() {
        return this.uri;
    }

    /**
     * Returns protocol specified by this request.
     * @return protocol specified by this request, either HTTP/1.0 or HTTP/1.1
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Returns type of the reuqest
     * @return type of the request. Maybe HTTP or HTTPS.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the content length of request.
     * @return the content length of request
     */
    public int getContentLength() {
        return this.contentLength;
    }

    /**
     * Returns the total length of request.
     * @return the total length of request
     */
    public int getTotalLength() {
        return getHeader().length()+this.contentLength;
    }

    /**
     * Returns the keep alive timeout.
     * @return the keep alive timeout
     */
    public int getKeepAliveTimeout() {
        return this.keepAliveTimeout;
    }

    /**
     * Returns the connection field of HTTP request.
     * @return the connection field
     */
    public String getConnection() {
        return this.connection;
    }

    /**
     * Returns this request's content.
     * @return this request's content
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * Sets content of the request.
     * @param content content of the request
     */
    public void setContent(byte[] content) {
        System.arraycopy(content, 0, this.content, 0, this.contentLength);
    }

    /**
     * Checks if the destination url bound with this request is correct.
     * @return true if the destination url bound with this request is correct. Otherwise returns false
     */
    public boolean isCorrect() {
        return this.correct;
    }

    /**
     * Returns the value of a header parameter with the given name
     * @param name name of the requested header parameter
     * @return value the requested header parameter
     */
    public String getAttributeValue(String name) {
        String value = null;

        ListIterator iter = params.listIterator();
        while( iter.hasNext() ) {
            String line = (String) iter.next();

            if(line.toLowerCase().startsWith(name)) {
                value = line.split(" ")[1];
                value = value.trim();
                break;
            }
        }

        return value;
    }

    /**
     * Returns a list of all header parameters
     * @return a list of all header parameters, where each element is a full line of header -
     * key and value, separated by a colon.
     */
    public List getParams() {
        return params;
    }

    /**
     * Returns request that will be send to server.
     * @return request that will be send to server
     */
    public String getHeader() {
        String header;

        header = this.method + " ";

        if(this.method.equalsIgnoreCase("connect"))
            header += this.url + " ";
        else
            header += this.uri + " ";

        header += this.protocol;
        header += ConstData.EMPTY_LINE;

        ListIterator iter = this.params.listIterator();
        while(iter.hasNext()) {
            header += (String) iter.next();
            header += ConstData.EMPTY_LINE;
        }

        header += ConstData.EMPTY_LINE;

        return header;
    }

    /**
     * Returns the proxied header as a byte array. Proxied header is an original header with modified destination url.
     * @return the proxied header as a byte array
     */
    public byte[] getHeaderBytes() {
        return getHeader().getBytes();
    }


    /**
     * Returns the whole request as a byte array.
     * @return the whole request as a byte array
     */
    public byte[] getBytes() {
        byte[] wholeRequest = new byte[this.getTotalLength()];
        System.arraycopy(getHeader().getBytes(), 0, wholeRequest,
                0, getHeader().length());
        System.arraycopy(content, 0, wholeRequest,
                getHeader().length(), this.contentLength);

        return wholeRequest;
    }

    /**
     * Returns the request header.
     * @return the header
     */
    public String toString() {

        String retVal = "";

        retVal = "Method: " + this.method + "\n";
        retVal += "URL: " + this.url + "\n";
        retVal += "Protocol: " + this.protocol + "\n";
        retVal += "\n";

        ListIterator iter = params.listIterator();
        while ( iter.hasNext() ) {
            retVal += (String) iter.next();
            retVal += ConstData.EMPTY_LINE;
        }
        
        return retVal;
    }

    /**
     * Returns the host name.
     * @return the host name
     */
    public String getHost() {
        return getAttributeValue("host");
    }

    /**
     * Returns the first line of the request.
     * @param headerLine first line of the request, containing method, url and protocol
     */
    private void setHeaderLine(String headerLine) {

        String[] partsOfHeaerLine = headerLine.split(" ");

        if( partsOfHeaerLine.length == 3 ) {
            this.method = partsOfHeaerLine[0].trim();
            this.url = partsOfHeaerLine[1].trim();
            this.protocol = partsOfHeaerLine[2].trim();
            this.correct = true;

            if( !this.method.equals("CONNECT")) {
                this.uri = this.url.replaceFirst("http://", "");
                this.uri = this.uri.substring(this.uri.indexOf("/"));

                this.host = this.url.replaceFirst("http://", "");
                this.host = this.host.substring(0, host.indexOf("/"));

                this.type = "HTTP";
            } else {
                this.host = this.url.substring(0, this.url.indexOf(":"));
                this.type = "HTTPS";
            }
        }
    }

    /**
     * Sets headers field from the String array.
     * @param attrs array of strings
     */
    private void setHeader(String[] attrs) {
        //attributes header
        int i = 1;
        while( i < attrs.length ) {
            if(!attrs[i].toLowerCase().startsWith("proxy-connection")) {
                this.params.add(attrs[i].trim());
            } else {
                String connection = "Connection: ";
                connection += attrs[i].trim().split(" ")[1];
                this.params.add(connection);

                this.connection = attrs[i].trim().split(" ")[1];
            }

            if(attrs[i].toLowerCase().startsWith("content-length")) {
                String[] partsContentLength = attrs[i].trim().split(" ");
                this.contentLength =
                    Integer.valueOf(partsContentLength[1]).intValue();
                this.content = new byte[this.contentLength];
            }
            i++;
        }

        if( this.connection.equalsIgnoreCase("keep-alive") ) {
            String keepAliveValue = getAttributeValue("keep-alive");

            if( keepAliveValue == null || keepAliveValue.length() == 0)
                this.keepAliveTimeout = KEEP_ALIVE_TIMEOUT;
            else if( Util.isDigit(keepAliveValue) )
                this.keepAliveTimeout =
                        Integer.parseInt(keepAliveValue) * 1000;


        }
    }
}
