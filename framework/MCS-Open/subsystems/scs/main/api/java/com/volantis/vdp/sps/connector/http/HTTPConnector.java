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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.vdp.sps.connector.http;

import com.volantis.vdp.sps.connector.Connector;
import com.volantis.vdp.sps.utils.Converter;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 * This class implements connection on http protocol
 * User: rstroz01
 * Date: 2005-12-30
 * Time: 13:37:47
 */
public class HTTPConnector extends Connector implements Runnable {
    public static final int DEFAULT_PORT = 80;


    /**
     * constructor
     * @param requestData received request data from SecureConnectionServer
     * @param baseUrl baseURL readed from config
     * @param requestId identificator of request
     */
    public HTTPConnector(byte[] requestData, String baseUrl, int requestId) {
        super(requestData, baseUrl, requestId);
    }

    /**
     * Method start thread. This method send remaped request
     * to server and receive response. Response is wrapped
     * to SCP packet and write to SCP
     */
    public void run() {
        int port = DEFAULT_PORT;
        Socket socket = null;
        if (baseServerPort > 0) port = baseServerPort;
        if (baseServerName == null) {
            LOGGER.error("Unknow Base Server name");
            return;
        }
        try {
            socket = new Socket(baseServerName, port);

            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os =
                    new DataOutputStream(socket.getOutputStream());
            ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();

            os.write(getRemapedRequest());
            byte[] httpHeader = readHttpHeader(is);
            tempBuffer.write(httpHeader);


            int responseContentSize = getHttpContentSize();

            if (responseContentSize >= 0) {
                byte[] responseContent;
                responseContent = new byte[responseContentSize];
                for (int i = 0; i < responseContentSize; i++) {
                    responseContent[i] = is.readByte();
                }
                tempBuffer.write(responseContent);
            } else if (responseContentSize == -1) {

                if ("chunked".equals(getTransferEncoding())) {
                    byte[] chunkHexLength;
                    byte[] chunkContent;
                    int chunkLength = 0;
                    do {

                        chunkHexLength = readHexStringChunkSize(is);
                        chunkLength = Converter.hex2int(
                                new String(chunkHexLength, 0,
                                        chunkHexLength.length).trim());
                        tempBuffer.write(chunkHexLength, 0,
                                chunkHexLength.length);

                        chunkContent = readChunkContent(is, chunkLength);
                        tempBuffer.write(chunkContent, 0, chunkLength + 2);

                    } while (chunkLength != 0);
                } else {
                    int character = 0;
                    while ((character = is.readByte()) >= 0) {
                        tempBuffer.write(character);
                    }
                }
            }
            setResponse(tempBuffer.toByteArray());

            writer.write(getResponse(), getRequestId(),
                    getRequestType().getBytes());

        } catch (UnknownHostException e) {
            e.printStackTrace();
            LOGGER.error("Unknown Host: " + baseServerName);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Connection error");
        } finally {
            if (socket != null && socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    LOGGER.error("Can't close socket");
                }
            }
        }
    }

    /**
     * Method return Content-Length from http response header
     * named Content-Length
     *
     * @return int represents content size
     */
    private int getHttpContentSize() {
        Iterator it = headers.iterator();
        while (it.hasNext()) {
            HTTPHeader httpHeader = (HTTPHeader) it.next();
            if ("content-length".equals(httpHeader.getKey())) {
                return Integer.parseInt(httpHeader.getValue());
            }
        }
        return -1;
    }

    /**
     * Method getting type of transfer encoding from http headers (use if
     * Content-Length not defined and response have content)
     *
     * @return String - value of transfer encoding from heder field
     */
    private String getTransferEncoding() {
        Iterator it = headers.iterator();
        while (it.hasNext()) {
            HTTPHeader httpHeader = (HTTPHeader) it.next();
            if ("transfer-encoding".equals(httpHeader.getKey())) {
                return httpHeader.getValue();
            }
        }
        return null;
    }

    /**
     * Method read http response header
     *
     * @return array of bytes readed from server
     */
    private byte[] readHttpHeader(DataInputStream is) throws IOException {

        StringBuffer buffer = new StringBuffer();
        boolean header = true;
        char[] endOfHeader = new char[4];

        while (header) {
            char headByte = 0;
            headByte = (char) is.readByte();
            buffer.append(headByte);
            if (headByte == '\n' && buffer.length() >= 4) {
                buffer.getChars(buffer.length() - 4, buffer.length(),
                        endOfHeader, 0);
                if (endOfHeader[1] == '\n') {
                    header = false;
                }
            }
        }
        parseHttpResponseHead(buffer);
        return buffer.toString().getBytes();
    }

    /**
     * Method read hexadecimal string with CRLN that represent size of chunk
     *
     * @param is - InputStream from this method read bytes
     * @return byte array with ASCII representation of chunk size
     * @throws IOException
     */
    private byte[] readHexStringChunkSize(DataInputStream is)
            throws IOException {

        int i = 0;
        byte[] hexChunkLength;
        byte[] buffer = new byte[10];
        do {
            buffer[i] = is.readByte();
            i++;

        } while ((char) buffer[i - 1] != '\n' && i < buffer.length);

        hexChunkLength = new byte[i];
        System.arraycopy(buffer, 0, hexChunkLength, 0, i);
        return hexChunkLength;
    }

    /**
     * Method reading data chunk from http server use when transfer-coding
     * equals chunk
     *
     * @param is
     * @param chunkLength
     * @return
     * @throws IOException
     */
    private byte[] readChunkContent(DataInputStream is, int chunkLength)
            throws IOException {
        byte[] chunkContent = new byte[chunkLength + 2];
        for (int it = 0; it < chunkLength + 2; it++) {
            byte contentByte = is.readByte();
            chunkContent[it] = contentByte;
        }
        return chunkContent;
    }
}
