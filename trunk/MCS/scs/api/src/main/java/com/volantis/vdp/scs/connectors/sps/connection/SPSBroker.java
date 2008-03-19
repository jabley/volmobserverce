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

import org.apache.log4j.Logger;

import com.volantis.vdp.scs.authentication.AuthenticationProvider;
import com.volantis.vdp.scs.authentication.AuthenticationToken;

import com.volantis.vdp.scs.protocol.AUPacket;
import com.volantis.vdp.scs.protocol.SCPPacket;
import com.volantis.vdp.scs.connectors.sps.SPSConnectionPool;
import com.volantis.vdp.scs.managers.sps.SPSManager;

import com.volantis.vdp.configuration.scs.ISCSConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The sps broker distributes the SCP Packet that is delivered from the sps Worker.
 */
public class SPSBroker {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SPSBroker.class);

    private SPSWorker worker;

    private AuthenticationProvider auth;

    /**
     * Creates the sps Broker that is associated with the sps worker given as speciefied parameter.
     * @param worker the sps worker
     */
    public SPSBroker(SPSWorker worker, ISCSConfiguration config) {

        this.worker = worker;
        try {
            Class classDefinition =
                    Class.forName(config.getAuthenticationProviderClass());
            this.auth =
                    (AuthenticationProvider) classDefinition.newInstance();
        } catch(ClassNotFoundException cnfe) {

        } catch(InstantiationException ie) {

        } catch(IllegalAccessException iae) {

        }

    }

    /**
     * Delivers the SCP packet to the sps manager (for example packets starts with HT)
     * or sends response to the sps if received authentication packet.
     * @param packet the data delivered from the sps worker
     * @param connection the sps connection
     */
    public void work(SCPPacket packet, SPSConnection connection) {

        if(packet.getRequestTypeIdetifier().equals(
                SCPPacket.AUTHENTICATION)) {

            AUPacket auPacket = new AUPacket(packet);

            AuthenticationToken token = this.auth.authenticate(
                    auPacket.getUserId(), auPacket.getPassword());

            if(token != null) {
                byte[] response =
                        String.valueOf(token.getStatus()).getBytes();
                packet.setDatablock(response);
                worker.send(packet.getBytes());

                if(token.getStatus() == AuthenticationToken.LOGIN_SUCCESFUL) {
                    SPSConnectionPool.setURLByConnection(
                                token.getUrl(),
                                connection
                            );
                    connection.setActive(true);

                    if(logger.isDebugEnabled()) {
                        logger.debug("Successful authentication.");
                    }
                } else {
                    connection.setActive(false);
                    if(logger.isDebugEnabled()) {
                        logger.debug("Fail authentication.");
                    }
                }
            }


        } else if(packet.getRequestTypeIdetifier().equals(
                SCPPacket.HTTP_REQUEST_RESPONSE)) {

            if(connection.isActive()) {
                SPSManager.sendHTTPResponse(packet);
            } else {
                if(logger.isDebugEnabled()) {
                    logger.debug("Packet lost. Connection isn't active.");
                }
            }
        } else if(packet.getRequestTypeIdetifier().equals(
                SCPPacket.HTTPS_REQUEST_RESPONSE)) {

            if(connection.isActive()) {
                SPSManager.sendHTTPSResponse(packet);
            } else {
                if(logger.isDebugEnabled()) {
                    logger.debug("Packet lost. Connection isn't active.");
                }
            }
        } else if(packet.getRequestTypeIdetifier().equals(
                SCPPacket.KEEP_ALIVE)) {

        }
    }

    /**
     * Sends the data as byte array if connection between SecureConnectionServer and sps is active.
     * @param data the data sent to the sps worker
     */
    public void send(byte[] data) {
        if(worker.getConnection().isActive())
            this.worker.send(data);
    }
}
