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
package com.volantis.vdp.sps.connector.https;

import com.volantis.vdp.sps.connector.Connector;
import com.volantis.vdp.sps.connector.exception.ConnectionException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *
 * Connector working on https connection
 * User: rstroz01
 * Date: 2005-12-30
 * Time: 13:37:57
 * <p/>
 */
public class HTTPSConnector extends Connector implements Runnable {
    public static final int DEFAULT_PORT = 443;
    private Socket httpsServer;
    private boolean tunnel;
    private DataInputStream iStream;
    private DataOutputStream oStream;

    /**
     * Constructor
     * @param requestData - request data received from SecureConnectionServer
     * @param baseUrl - baseURL readed from config file
     * @param requestId - Identifier of request
     * @throws ConnectionException
     */
    public HTTPSConnector(byte[] requestData, String baseUrl, int requestId)
            throws ConnectionException {
        super(requestData, baseUrl, requestId);
        connect();
        tunnel = false;
    }

    /**
     * Establish connection over ssl and send data retransmit data between
     * SecureConnectionServer and destination server
     */
    public void run() {
        if (tunnel) {
            try {
                oStream.write(getRemapedRequest());
                setResponse(getResponseFromServer(iStream));
            } catch (IOException e) {
                LOGGER.error("IO Error on https server socket");
                close();
            }
        } else {
            StringBuffer responseBuffer = new StringBuffer();
            responseBuffer.append(getRequestProtocol());
            responseBuffer.append(" 200 Connection established\r\n");
            responseBuffer.append("Proxy-agent: Volantis SecureConnectionServer<-->SecureConnectionServer\r\n\r\n");
            setResponse(responseBuffer.toString().getBytes());
            tunnel = true;
        }
        writer.write(getResponse(), getRequestId(),
                getRequestType().getBytes());
    }

    // Method read HTTPS response from server
    private byte[] getResponseFromServer(DataInputStream source)
            throws IOException {
        byte[] header = new byte[5];
        byte[] buffer;
        int packetLength;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        try {
            do {
                header[0] = source.readByte();
                if ((byte2Int(header[0]) & 0xC0) != 0) {
                    if ((byte2Int(header[0]) & 0x80) > 0) {
                        header[1] = source.readByte();
                        packetLength = ((header[0] & 0x7f) << 8) |
                                ((int) header[1] & 0xFF);
                        byteBuffer.write(header, 0, 2);
                    } else {
                        header[1] = source.readByte();
                        header[2] = source.readByte();
                        packetLength = ((header[0] & 0x3f) << 8) |
                                ((int) header[1] & 0xFF);
                        byteBuffer.write(header, 0, 3);
                    }
                    buffer = new byte[packetLength];
                } else {
                    source.readFully(header, 1, 4);
                    packetLength = byte2Int(header[3], header[4]);

                    buffer = new byte[packetLength];
                    byteBuffer.write(header);
                }
                for (int i = 0; i < packetLength; i++) {
                    buffer[i] = source.readByte();
                }
                try {
                    byteBuffer.write(buffer);
                } catch (IOException ex) {
                    LOGGER.warn("Error writting to buffer");
                }
            } while (source.available() > 0);
            return byteBuffer.toByteArray();
        } catch (SocketTimeoutException ex) {
            LOGGER.warn("Read timeout - probably end of data from server");
        }
        return new byte[0];
    }

    // Method establish connection to destination host
    private void connect() throws ConnectionException {
        int port = DEFAULT_PORT;
        if (baseServerPort > 0) port = baseServerPort;
        if (baseServerName == null) {
            LOGGER.error("Unknow Base Server name");
            throw new ConnectionException("Unknow Base Server name");
        }

        try {
            httpsServer = new Socket(baseServerName, port);
            iStream = new DataInputStream(httpsServer.getInputStream());
            oStream = new DataOutputStream(httpsServer.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            LOGGER.error("Unknown Host: " + baseServerName);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Connection error");
        }
    }


    /**
     * Method clenly close socket connection to server
     */
    public void close() {
        if (httpsServer != null && httpsServer.isConnected()) {
            try {
                httpsServer.close();
            } catch (IOException e) {
                LOGGER.error("Can't close socket");
            }
        }
    }

    /**
     * Convert two byte to integer
     * @param lo
     * @param hi
     * @return
     */
    private int byte2Int(byte lo, byte hi) {
        int i = 0;
        i += ((int) lo & 0xFF) << 8;
        i += ((int) hi & 0xFF);

        return i;
    }

    // Convert byte to integer
    private int byte2Int(byte bytes) {
        return ((int) bytes & 0xFF);
    }


    /**
     *
     * @param requestData request data
     */
    public void setRequestData(byte[] requestData) {
        setRemapedRequest(requestData);
    }

    /**
     * return true if connected and false if not connected
     */
    public boolean isConnected() {
        return httpsServer.isConnected();
    }
}



