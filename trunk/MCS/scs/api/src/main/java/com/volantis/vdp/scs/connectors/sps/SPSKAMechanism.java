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
package com.volantis.vdp.scs.connectors.sps;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.volantis.vdp.scs.managers.request.RequestIdentifierManager;
import com.volantis.vdp.scs.protocol.KAPacket;
import com.volantis.vdp.scs.connectors.sps.connection.SPSConnection;

import com.volantis.vdp.configuration.scs.ISCSConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Mechanism sending periodically KA packet from SecureConnectionServer to sps.
 */
public class SPSKAMechanism {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SPSKAMechanism.class);

    private int keepAlive;

    /**
     * Default constructor.
     */
    public SPSKAMechanism(ISCSConfiguration config) {
        this.keepAlive = config.getSCKeepAlive()*1000; //milliseconds
    }

    /**
     *  Starts a mechanism sending KA packets. It is executed in separate thread.
     */
    public void start() {

        Thread timer = new Thread() {
            public void run() {
                try {
                    while(!interrupted()) {
                        if(logger.isDebugEnabled()) {
                            logger.debug("Send KA packets");
                        }
                        sendKeepAlivePackets();
                        sleep(keepAlive);
                    }
                } catch(InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        timer.start();
    }

    /**
     * This method is called periodically by start() method. Sends KA packet to all active connections.
     */
    private void sendKeepAlivePackets() {
        List activeConnections = SPSConnectionPool.getAllActiveConnections();

        Iterator iter = activeConnections.iterator();
        while(iter.hasNext()) {
            SPSConnection spsConn = (SPSConnection) iter.next();
            spsConn.getBroker().send(
                    new KAPacket(
                            RequestIdentifierManager.getNewRequestId()).
                            createKAPacket());
        }
    }
}
