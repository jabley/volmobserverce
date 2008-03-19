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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.vdp.sps.connector;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.vdp.sps.response.ISCPWriter;
import com.volantis.synergetics.log.LogDispatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * Abstract class that define common methods for
 * HttpsConnection and HttpConnection
 * User: rstroz01
 * Date: 2006-01-02
 * Time: 09:01:00
 */
public abstract class Connector implements IConnector {

    /**
     * Class represents http one header field
     */
    protected class HTTPHeader {
        private String key;
        private String value;

        /**
         * Inner class represent http header
         *
         */
        private HTTPHeader() {
        }

        /**
         * Constructor making this class based on httpHeader line
         *
         * @param httpHeader
         */
        public HTTPHeader(String httpHeader) {
            String[] keyValuePair = httpHeader.split(": ");
            if (keyValuePair.length == 2) {
                this.key = keyValuePair[0].trim().toLowerCase();
                this.value = keyValuePair[1].trim().toLowerCase();
            }
        }

        /**
         * @return String - http header key name
         */
        public String getKey() {
            return key;
        }

        /**
         * @return String - http header key value
         */
        public String getValue() {
            return value;
        }
    }

    protected static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(Connector.class);
//    protected static final Logger LOGGER = Logger.getLogger(Connector.class);
    public static final String REQ_TYPE_GET = "GET";
    public static final String REQ_TYPE_POST = "POST";
    public static final String REQ_TYPE_CONNEC = "CONNECT";
    public static final String REQ_TYPE_HEAD = "HEAD";

    private byte[] request;
    private byte[] remapedRequest = null;
    private byte[] response;

    private int requestId;
    private int requestPort;

    private String requestType;
    private String requestResource;
    private String requestProtocol;
    private String baseURL;
    protected ISCPWriter writer;
    protected String baseServerName;
    protected int baseServerPort;

    protected List headers = new LinkedList();
    private int httpStatusCode;
    private String httpProtocol;
    private String httpStatusMessage;

    /**
     * Default constructor
     *
     */
    public Connector() {
    }

    /**
     * Constructor parse http request, set requestId and
     * baseUrl
     * @param request
     * @param baseUrl
     * @param requestId
     */
    public Connector(byte[] request, String baseUrl, int requestId) {
        this.request = request;
        this.baseURL = baseUrl;
        this.requestId = requestId;

        // Default host name if other not defined.
        this.baseServerName = "localhost";
        try {
            URL url = new URL(baseUrl);
            this.baseServerName = url.getHost();
            this.baseServerPort = url.getPort();
        } catch (MalformedURLException e) {
            LOGGER.error("Base url is in wrong format");
        }

        StringBuffer buffer = new StringBuffer();
        int i = 0;

        while (request[i] != (byte) '\n' && i < request.length) {
            buffer.append((char) request[i]);
            i++;
        }

        String[] req = buffer.toString().trim().split(" ");
        if (req.length == 3) {
            this.requestType = req[0];
            this.requestResource = req[1];
            this.requestProtocol = req[2];
        }
    }

    /**
     *
     * @return String represent request type
     */
    public String getRequestType() {
        return this.requestType;
    }

    /**
     *
     * @return - String represents request protocol
     */
    public String getRequestProtocol() {
        return requestProtocol;
    }

    /**
     *
     * @return String represents Request Resource
     */
    public String getRequestResource() {
        return requestResource;
    }

    /**
     * Method set request resource
     * @param requestResource String
     */
    public void setRequestResource(String requestResource) {
        this.requestResource = requestResource;
    }

    /**
     * Method remap http request (replace request host to baseUrl)
     * @return
     */
    public byte[] getRemapedRequest() {
        if (remapedRequest == null) {
            int i = 1;
            // Skip request (first line in buffer)
            while (request[i - 1] != (byte) '\n' && i < request.length) {
                i++;
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append(getRequestType()).append(" ");
            buffer.append(baseURL);
            String resource = getRequestResource();

            if (resource.startsWith("http://")) {
                try {
                    URL url = new URL(getRequestResource());
                    requestPort = url.getPort();
                    buffer.append(url.getFile());

                } catch (MalformedURLException e) {
                    LOGGER.error("Request url in wrong fromat");
                }
            } else {
                buffer.append(getRequestResource());
            }

            buffer.append(" ").append(getRequestProtocol()).append("\r\n");
            byte[] newRequest = buffer.toString().getBytes();

            remapedRequest = new byte[newRequest.length + request.length - i];
            for (int j = 0; newRequest.length > j; j++) {
                remapedRequest[j] = newRequest[j];
            }
            for (int j = newRequest.length; j < remapedRequest.length; j++) {
                remapedRequest[j] = request[i];
                i++;
            }
        }
        return remapedRequest;
    }

    /**
     * Method parse header and mapped all header field to list HTTPHead classes
     *
     * @param headers - StringBuffer contain raw http header
     */
    protected void parseHttpResponseHead(StringBuffer headers) {
        String httpResponseHead = headers.toString();
        String[] httpHeadersTable = httpResponseHead.split("\r\n");
        String[] httpResponseStatus = httpHeadersTable[0].split(" ", 3);
        this.httpProtocol = httpResponseStatus[0];
        this.httpStatusCode = Integer.parseInt(httpResponseStatus[1]);
        this.httpStatusMessage = httpResponseStatus[2];

        for (int i = 1; i < httpHeadersTable.length; i++) {
            HTTPHeader httpHeader = new HTTPHeader(httpHeadersTable[i]);
            this.headers.add(httpHeader);
        }
    }


    /**
     *
     * @param response method set response from base server
     */
    protected void setResponse(byte[] response) {
        this.response = response;
    }

    /**
     * @return byte[] - response from destination server
     */
    public byte[] getResponse() {
        return response;
    }

    /**
     * @return int requestId
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     *
     * @return rerurn request port
     */
    public int getRequestPort() {
        return requestPort;
    }

    /**
     * method set writer
     */
    public void setWriter(ISCPWriter writer) {
        this.writer = writer;
    }


    /**
     * Setter set remaped requesr
     * @param remapedRequest
     */
    protected void setRemapedRequest(byte[] remapedRequest) {
        this.remapedRequest = remapedRequest;
    }

    /**
     * return true if connector is connected and false if not
     * connected
     */
    public boolean isConnected() {
        return false;
    }
}
