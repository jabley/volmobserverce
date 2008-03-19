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
package com.volantis.vdp.scs.connectors.client.connection;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.volantis.vdp.configuration.scs.ISCSConfiguration;

/**
 * A connection between SecureConnectionServer and client (for example MCS).
 */
public class ClientConnection {

    private Socket socket;

    private DataInputStream clientDataIn;
    private DataOutputStream clientDataOut;
    private ClientBroker clientBroker;

    private Thread receiveClientThread;

    private long lastTime;
    private long keepalive;

    /**
     * Creates client connection.
     * @param socket the parameter that specified connection with client
     */
    public ClientConnection(Socket socket, ISCSConfiguration config) {
        this.lastTime = System.currentTimeMillis();
        this.keepalive = 300000; //default timeout

        this.socket = socket;

        this.clientBroker = new ClientBroker(this, config);

        try {
            this.clientDataIn = new DataInputStream(socket.getInputStream());
            this.clientDataOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ioe) {

        }
        receive();
    }

    /**
     * Turns on the mechanism that receives data from client and forwarding to client borker.
     */
    public void receive() {

        receiveClientThread = new Thread () {
            public void run() {
                int numberOfBytes = 0;
                byte[] buffer = new byte[1024];
                String stringBuffer = "";
                boolean run = true;
                try {
                    while(!this.isInterrupted()) {

                        while( clientDataIn.available() == 0 ) {
                            if(System.currentTimeMillis() -
                                    lastTime > keepalive ) {
                                close();
                                run = false;
                                break;
                            }
                            Thread.sleep(100);
                        }

                        if( run && (numberOfBytes =
                                clientDataIn.read(buffer)) > -1 ) {
                            if( numberOfBytes < buffer.length ) {
                                stringBuffer +=
                                        new String(buffer, 0, numberOfBytes);
                                clientBroker.work(stringBuffer);
                                stringBuffer = "";
                            } else if( numberOfBytes == buffer.length ) {
                                stringBuffer += new String(buffer);
                            }
                        }
                    }
                } catch( IOException ioe ) {
                close();
                } catch( InterruptedException ie) {}
            }
        };
        receiveClientThread.start();
    }

    /**
     * Sends the data to client
     * @param data the data as byte array
     */
    public synchronized void send(byte[] data) {
        try {
            clientDataOut.write(data);
            clientDataOut.flush();
        } catch(IOException cce) {
        }
    }

    /**
     * Sends the data to client
     * @param data the data as byte array
     * @param off the start offset in the data
     * @param len number of bytes to write
     */
    public synchronized void send(byte[] data, int off, int len) {
        try {
            clientDataOut.write(data, off, len);
            clientDataOut.flush();
        } catch(IOException cce) {
        }
    }

    /**
     * Sends the data to client
     * @param data the data as int
     */
    public synchronized void send(int data) {
        try {
            clientDataOut.write(data);
            clientDataOut.flush();
        } catch(IOException cce) {
        }
    }

    /**
     * Stops the mechanism that receives data from client. Closes input and output stream.
     * Closes the socket.
     */
    public void close() {
        try {
            receiveClientThread.interrupt();

            if( socket != null ) {
                socket.shutdownInput();
                clientDataIn.close();

                socket.shutdownOutput();
                clientDataOut.close();

                socket.close();
            }
        } catch(IOException e) {
        }
    }

    /**
     * Upgrades the lastTime parameter. 
     */
    public void upgradeKeepAliveTime() {
        this.lastTime = System.currentTimeMillis();
    }

    /**
     * Enable/disable SO_KEEPALIVE.
     * @param on whether or not to have socket keep alive turned on
     */
    public void setKeepAlive(boolean on) {
        try {
            this.socket.setKeepAlive(on);
        } catch(SocketException se) {}
    }

}
