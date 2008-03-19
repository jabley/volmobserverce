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
package com.volantis.vdp.scs.proxy.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

 import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import com.volantis.vdp.scs.connectors.client.connection.ClientBroker;
import com.volantis.vdp.scs.util.Request;
import com.volantis.vdp.scs.util.ResponseHeader;
import com.volantis.vdp.scs.util.ConstData;
import com.volantis.vdp.scs.proxy.Proxy;

/**
 * A class sending and receiving a http request and response. Wrapping an requesting process in
 * the separate class is needed as this operation requires processing in own thread.
 */
public class ProxyHTTP extends Thread implements Proxy {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ProxyHTTP.class);

    private Socket socket;

    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    private ClientBroker clientBroker;

    private long lastTime;
    private long keepalive;

    private static final int SIZE_OF_BUFFER = 65536;

    /**
     * Creates the ProxyHTTP instance basing on sent request and client broker.
     * @param request the HTTP request that should be sent to server
     * @param clientBroker a broker used to obtain the response
     */
    public ProxyHTTP(Request request, ClientBroker clientBroker) {
        this.clientBroker = clientBroker;
        this.clientBroker.setProxy(this);
        this.clientBroker.setProxyHTTP(true);

        this.lastTime = System.currentTimeMillis();
        this.keepalive = 300000; //default timeout

        try {

            String[] pairHostAndPort = request.getHost().split(":");
            String host = "";
            int port = 80;

            if( pairHostAndPort.length == 2 ) {
                host = pairHostAndPort[0];
                port = Integer.valueOf(pairHostAndPort[1]).intValue();
            } else {
                host = pairHostAndPort[0];
            }
            socket = new Socket( host, port );

            if( request.getConnection().equalsIgnoreCase("keep-alive")) {
                socket.setKeepAlive(true);
            }

            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());

            send(request);
            this.start();
        } catch(UnknownHostException uke) {
            String[] mess = new String[1];
            mess[0] = uke.getMessage();
            logger.error("unknown-host");
        } catch(IOException ioe) {
            logger.error("io-exception");
        }
    }

    /**
     * Receives the response from the server. The method is processed in separate thread
     */
    public void run() {
        try {
            ResponseHeader response;
            boolean run = true;

            while(!this.isInterrupted()) {
                String headerBuffer = "";
                String line = "";

                while( dataIn.available() == 0 ) {
                    if(System.currentTimeMillis() -
                            lastTime > keepalive ) {
                        clientBroker.close();
                        run = false;
                        break;
                    }
                    Thread.sleep(100);
                }
                if( !run ) break;

                while((line = readLine()).length() > 0) {
                    headerBuffer += line + ConstData.EMPTY_LINE;
                }
                response = new ResponseHeader(headerBuffer);
                clientBroker.send(response.getBytes());

                if(response.getContentLength() > 0) {
                    readBytes(response.getContentLength(), null);
                } else if(response.getTransfEncoding().
                    equalsIgnoreCase("chunked")) {
                    int numberBytes = 0;

                    while((line = readLine()).length() ==0 ) {}

                    while(!((numberBytes =
                            Integer.parseInt(line, 16)) == 0)) {
                        readBytes(numberBytes, line);
                        while((line = readLine()).length() ==0 ) {}
                    }
                }

                String connection = response.getConnection();
                if(connection.equalsIgnoreCase("close")) {
                    clientBroker.close();
                } else if(connection.equalsIgnoreCase("keep-alive")) {
                    this.keepalive = response.getKeepAliveTimeout();
                    this.lastTime = System.currentTimeMillis();
                    socket.setKeepAlive(true);
                    clientBroker.setKeepAlive(true);
                }
            }
        } catch(IOException e) {
             clientBroker.close();
            logger.error("io-exception");
        } catch(InterruptedException e) {}
    }

    /**
     * Sends the given data to server
     * @param request the data sent to a server
     */
    public void send(Request request) {
            send(request.getBytes());
    }

    /**
     * Sends the given data to server
     * @param data the data sent to a server
     */
    public synchronized void send(byte[] data) {
        try {
            dataOut.write(data);
            dataOut.flush();
        } catch(IOException ioe) {
            String[] mess = new String[1];
            mess[0] = ioe.getMessage();
            logger.error("cannot-write-to-stream");
        }
    }

    /**
     * Sends the given data to server.
     * @param data the data sent to a server
     * @param off the offset of the sent part in the given data array
     * @param len how many bytes from the given data array should be sent
     */
    public synchronized void send(byte[] data, int off, int len) {
        try {
            dataOut.write(data, off, len);
            dataOut.flush();
        } catch(IOException ioe) {
            String[] mess = new String[1];
            mess[0] = ioe.getMessage();
            logger.error("cannot-write-to-stream");
        }
    }

    /**
     * Stops the thread responsible for sending and receiving the request, releasing
     * the resorces bound with this request.
     */
    public void close() {
        try {
            this.interrupt();

            if( socket != null ) {
                socket.shutdownInput();
                dataIn.close();

                socket.shutdownOutput();
                dataOut.close();

                socket.close();
            }
        } catch(IOException ioe) {
        }
    }

    /**
     * Reads line from input stream.
     * @return line as a string
     */
    private String readLine() {
        byte[] localBuffer = new byte[1];
        String line = "";

        int size = 0;
        try {
            while( (size = dataIn.read(localBuffer)) != -1) {
                if( size == 1 ) {
                    line += new String(localBuffer);
                    if( line.endsWith(ConstData.EMPTY_LINE) ) {
                        line = line.trim();
                        break;
                    }
                }
            }
        } catch(IOException io) {
        }
        return line;
    }

    /**
     * Reads certain number of bytes from input stream. 
     * @param number number of bytes reading from input stream
     * @param line that is as first write to output stream
     */
    private void readBytes(int number, String line) {
        if(line != null)
            clientBroker.send((line + ConstData.EMPTY_LINE).getBytes());

        int len;
        int size;
        int currentContentLength = 0;

        if( number < SIZE_OF_BUFFER )
            len = number;
        else
            len = SIZE_OF_BUFFER;

        byte[] buffer = new byte[len];

        try {
            while(!(currentContentLength == number)) {
                size = dataIn.read(buffer, 0, len);
                clientBroker.send(buffer, 0, size);
                currentContentLength += size;
                if(number - currentContentLength < len)
                    len = number - currentContentLength;
            }
        } catch(IOException io) {}
    }
}
