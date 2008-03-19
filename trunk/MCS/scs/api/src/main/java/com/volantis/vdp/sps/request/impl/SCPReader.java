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
package com.volantis.vdp.sps.request.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.vdp.configuration.sps.ISPSConfiguration;
import com.volantis.vdp.scs.protocol.SCPPacket;
import com.volantis.vdp.sps.connector.IConnector;
import com.volantis.vdp.sps.connector.KeepAliveMonitor;
import com.volantis.vdp.sps.connector.exception.ConnectionException;
import com.volantis.vdp.sps.connector.http.HTTPConnector;
import com.volantis.vdp.sps.connector.https.HTTPSConnector;
import com.volantis.vdp.sps.request.ISCPReader;
import com.volantis.vdp.sps.response.ISCPWriter;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class read requests from SCP implaments
 * ISPReader interface
 */
public class SCPReader implements ISCPReader, Runnable {

    protected static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SCPReader.class);

    //public static Logger LOGGER = Logger.getLogger(SCPReader.class);
    private DataInputStream is;
    private boolean running = true;
    private ISPSConfiguration config;
    private List httpsRequestsList = new LinkedList();
    private ISCPWriter writer;

    /**
     * Derfault constructor - don't use
     *
     */
    public SCPReader() {
    }

    /**
     * Constructor - construct this class use Socket and config
     * @param connection
     * @param config
     * @throws IOException
     */
    public SCPReader(Socket connection, ISPSConfiguration config)
            throws IOException {
        this.config = config;
        is = new DataInputStream(connection.getInputStream());
    }

    /**
     * Construct this class user DataInputStream and config
     * @param is
     * @param config
     */
    public SCPReader(DataInputStream is, ISPSConfiguration config) {
        this.config = config;
        this.is = is;
    }

    /**
     * Start reader on established connection
     */
    public void run() {
        byte[] requestHeader = new byte[SCPPacket.SIZE_OF_HEADER];
        while (running) {
            byte[] dataBlock;
            IConnector connector = null;
            try {
                is.read(requestHeader, 0, SCPPacket.SIZE_OF_HEADER);
                SCPPacket packet = new SCPPacket();
                packet.setHeaderByte(requestHeader);

                int size = packet.getSizeOfDatablock();
                if (size > 0) {
                    dataBlock = new byte[size];
                    for (int i = 1; i < size; i++) {
                        dataBlock[i] = is.readByte();
                    }
                } else {
                    dataBlock = new byte[0];
                }

                if (SCPPacket.KEEP_ALIVE
                        .equals(packet.getRequestTypeIdetifier())) {
                    KeepAliveMonitor.monit();
                } else {
                    if (SCPPacket.HTTP_REQUEST_RESPONSE
                            .equals(packet.getRequestTypeIdetifier())) {
                        KeepAliveMonitor.monit();
                        connector = new HTTPConnector(dataBlock,
                                config.getWebServerURL(),
                                packet.getRequestIdetifier());
                        Thread t = new Thread(connector);
                        t.start();
                    } else if (SCPPacket.HTTPS_REQUEST_RESPONSE
                            .equals(packet.getRequestTypeIdetifier())) {
                        KeepAliveMonitor.monit();

                        Iterator it = httpsRequestsList.iterator();

                        // Serch for conection with id equals requestID
                        while (it.hasNext()) {
                            WorkConnectionThreads workThread;
                            connector = null;
                            workThread = (WorkConnectionThreads) it.next();
                            if (workThread.getRequestIdentifier() ==
                                    packet.getRequestIdetifier()) {
                                connector = workThread.getConnector();
                                ((HTTPSConnector) connector)
                                        .setRequestData(dataBlock);
                                workThread.start();
                            }
                        }

                        // remove timeouted threads.
                        it = httpsRequestsList.iterator();
                        while (it.hasNext()) {
                            WorkConnectionThreads workThread =
                                    (WorkConnectionThreads) it.next();
                            if (workThread.isTimeOuted() ||
                                    !workThread.isConnected()) {
                                ((HTTPSConnector) workThread.getConnector())
                                        .close();
                                httpsRequestsList.remove(workThread);
                            }
                        }

                        // if not found then ceate new connection and run as
                        // thread
                        if (connector == null) {
                            try {
                                connector = new HTTPSConnector(dataBlock,
                                        config.getWebServerURL(),
                                        packet.getRequestIdetifier());
                                connector.setWriter(this.writer);
                                Thread t = new Thread(connector);
                                t.start();
                                httpsRequestsList
                                        .add(new WorkConnectionThreads(t,
                                                connector));
                            } catch (ConnectionException ex) {

                                LOGGER.error("Connection error");
                            }
                        }
                    } else {
                        LOGGER.error("Bad SCP request recived");
                        break;
                    }
                }
            } catch (IOException e) {
                LOGGER.warn("I/O error " + e.getMessage());
            }
        }
    }

    /**
     * Set write who write response to SecureConnectionServer server connection
     *
     * @param writer ISCPWriter
     */
    public void setWriter(ISCPWriter writer) {
        this.writer = writer;
    }

    /**
     * Set running flag
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
