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

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

/**
 * The class representing a response header.
 */
public class ResponseHeader {

    private String headerResponse;
    private String headerLine;
    private int contentLength;
    private int keepAliveTimeout;
    private String transferEncoding;
    private String connection;
    private List attrsHeader;

    private static final int KEEP_ALIVE_TIMEOUT = 300000;

    /**
     * Creates the class instance basing on the http/https response
     *
     * @param data the whole http/https response which this response is obtained from
     */
    public ResponseHeader(String data) {

        this.contentLength = 0;
        this.transferEncoding = "";
        this.connection = "";
        this.headerLine = "";

        this.headerResponse = data;

        String[] params = headerResponse.split(ConstData.EMPTY_LINE);
        this.headerLine = params[0];

        this.attrsHeader = new ArrayList();
        int i = 1;
        while (i < params.length) {
            String field = params[i];

            if (field.toLowerCase().startsWith("connection")) {
                this.connection = field.split(": ")[1];
                this.connection = this.connection.trim();
            } else if (field.toLowerCase().startsWith("content-length")) {
                String contentLengthString = field.split(": ")[1];
                contentLengthString = contentLengthString.trim();
                this.contentLength =
                        Integer.valueOf(contentLengthString).intValue();
            } else if (field.toLowerCase().startsWith("transfer-encoding")) {
                this.transferEncoding = field.split(": ")[1];
                this.transferEncoding = this.transferEncoding.trim();
            }

            if( connection.equalsIgnoreCase("keep-alive") ) {
                String keepAliveValue = getAttribute("keep-alive");
                keepAliveValue = keepAliveValue.toLowerCase();

                if( keepAliveValue == null || keepAliveValue.length() == 0)
                    this.keepAliveTimeout = KEEP_ALIVE_TIMEOUT;
                else if( Util.isDigit(keepAliveValue) )
                    this.keepAliveTimeout =
                            Integer.parseInt(keepAliveValue) * 1000;
                else if( keepAliveValue.indexOf("timeout") != -1 ) {
                    keepAliveValue = keepAliveValue.replaceAll(" ", "");
                    keepAliveValue = keepAliveValue.substring(
                            keepAliveValue.indexOf("timeout=") +
                                    String.valueOf("timeout=").length());
                    if( keepAliveValue.indexOf(",") != -1) {
                        keepAliveValue = keepAliveValue.substring(0,
                                keepAliveValue.indexOf(","));
                    }
                    this.keepAliveTimeout =
                            Integer.parseInt(keepAliveValue) * 1000;
                } 
            }

            this.attrsHeader.add(field);
            i++;
        }

        headerResponse += ConstData.EMPTY_LINE;
    }

    /**
     * Returns the whole header of the response.
     *
     * @return the whole header of the response
     */
    public String getHeader() {
        return this.headerResponse;
    }

    /**
     * Returns the whole header of the response as byte array.
     *
     * @return the whole header of the response as byte array
     */
    public byte[] getBytes() {
        return this.headerResponse.getBytes();
    }

    /**
     * Returns the header length.
     *
     * @return  the header length
     */
    public int getHeaderLength() {
        return this.headerResponse.length();
    }

    /**
     * Returns the first line of this header.
     *
     * @return the first line of this header
     */
    public String getHeaderLine() {
        return this.headerLine;
    }

    /**
     * Returns the connection attribute value.
     * @return the connection attribute value, the most often "keep-alive" or "close"
     */
    public String getConnection() {
        return this.connection;
    }

    /**
     * Returns keep alive timeout parameter.
     * @return keep alive timeout parameter
     */
    public int getKeepAliveTimeout() {
        return this.keepAliveTimeout;
    }

    /**
     * Returns the transfer encoding attribute value.
     *
     * @return the transfer encoding attribute value
     */
    public String getTransfEncoding() {
        return this.transferEncoding;
    }

    /**
     * Returns the content length of the response
     * @return the content length
     */
    public int getContentLength() {
        return this.contentLength;
    }

    /**
     * Returns the value of the header attribute with a given name.
     *
     * @param key the name of the attribute
     * @return the value of the header attribute with a given name
     */
    public String getAttribute(String key) {
        String value = "";

        ListIterator iter = this.attrsHeader.listIterator();
        while(iter.hasNext()) {
            String field = (String) iter.next();
            if(field.toLowerCase().startsWith(key.toLowerCase())) {
                value = field.split(": ")[1];
                value = value.trim();
            }
        }
        return value;
    }
}
 
