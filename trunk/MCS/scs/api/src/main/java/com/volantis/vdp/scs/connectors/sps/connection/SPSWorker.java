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
package com.volantis.vdp.scs.connectors.sps.connection;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

import com.volantis.vdp.scs.protocol.SCPPacket;
import com.volantis.vdp.scs.protocol.SCPHeader;
import com.volantis.vdp.scs.connectors.sps.SPSConnectionPool;

import javax.net.ssl.SSLSocket;

import com.volantis.vdp.configuration.scs.ISCSConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The encoded connection between the SecureConnectionServer and the sps. This class contains input and output stream.
 * Sends and receives the data to/from the sps.
 */
public class SPSWorker extends Thread {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SPSWorker.class);

    private SSLSocket client;

    private DataInputStream in;
    private DataOutputStream out;

    private SPSConnection connection;

    private SPSBroker broker;

    private boolean run;

    /**
     * Creates encoded input and output stream with the sps client.
     * @param client socket
     */
    public SPSWorker(SSLSocket client, ISCSConfiguration config) {

        this.run = true;

        this.client = client;

        try {
            this.client.startHandshake();
            this.in = new DataInputStream(client.getInputStream());
            this.out = new DataOutputStream(client.getOutputStream());
        } catch(IOException ioe) {
            logger.error("io-exception");
        }

        this.broker = new SPSBroker(this, config);
        connection = new SPSConnection(broker);

        SPSConnectionPool.addConnection(null, connection);

    }

    /**
     * Turns on mechanism that receives data from the sps server.
     */
    public void run() {

        try {

            int size = 0;
            byte[] bufferHeader = new byte[10];
            while(run) {
                if( (size = in.read(bufferHeader, 0,
                        SCPHeader.SIZE_OF_HEADER )) > 0 ) {
                    String typePacket = new String(bufferHeader, 0, 2);
                    if( typePacket.equals(SCPHeader.AUTHENTICATION) ||
                        typePacket.equals(SCPHeader.KEEP_ALIVE) ||
                        typePacket.equals(SCPHeader.HTTP_REQUEST_RESPONSE) ||
                        typePacket.equals(SCPHeader.HTTPS_REQUEST_RESPONSE)) {

                        SCPHeader header = new SCPHeader(bufferHeader);
                        int totalSizeOfPacket = SCPHeader.SIZE_OF_HEADER;
                        totalSizeOfPacket +=
                                header.getSizeOfDatablock();

                        byte[] buffer = new byte[totalSizeOfPacket];

                        System.arraycopy(bufferHeader, 0, buffer,
                                0, SCPHeader.SIZE_OF_HEADER);

                        int currentSizeOfPacket = SCPHeader.SIZE_OF_HEADER;
                        while ( currentSizeOfPacket < totalSizeOfPacket ) {
                            if( (size = in.read(buffer,
                                    currentSizeOfPacket,
                                    (totalSizeOfPacket -
                                            currentSizeOfPacket))) > 0 ) {
                                currentSizeOfPacket += size;
                                if(currentSizeOfPacket == totalSizeOfPacket) {
                                    broker.work(new SCPPacket(buffer),
                                                   connection);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        } catch(IOException ioe) {
            dropedConnection();
        } finally {
           close();
        }

    }

    /**
     * Sends the data to the sps server.
     * @param data the data as byte array
     */
    public void send(byte[] data) {
        synchronized(out) {
            try {
               out.write(data);
               out.flush();
            } catch(IOException ioe) {
                dropedConnection();
            }
        }
    }

    /**
     * Sends data to the sps server.
     * @param data the data
     * @param off the start offset in the data
     * @param len number of bytes to write
     */
    public void send(byte[] data, int off, int len) {
        synchronized(out) {
            try {
               out.write(data, off, len);
               out.flush();
            } catch(IOException ioe) {
                dropedConnection();
            }
        }
    }

    /**
     * Stops sps data receiving mechanism. Closes input and ouptut stream and SSLSocket.
     */
    public void close() {

        try {
            this.run = false;
            in.close();
            out.close();
            client.close();
        } catch(IOException ioe) {
        }
    }

    /**
     * Returns the SPSBroker associated with this class.
     * @return the SPSBroker associated with this class
     */
    public SPSBroker getBroker() {
        return broker;
    }

    /**
     * Returns the SPSConnection associated with this a object class.
     * @return the SPSConnection associated with this a object class
     */
    public SPSConnection getConnection() {
        return connection;
    }

    /**
     * Calls a close() method and removes connection association from the connection list.
     */
    private void dropedConnection() {

        run = false;
        close();
        SPSConnectionPool.removeConnection(connection);

    }
}
