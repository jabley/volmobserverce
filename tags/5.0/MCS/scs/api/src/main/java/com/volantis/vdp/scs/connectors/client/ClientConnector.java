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
package com.volantis.vdp.scs.connectors.client;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.Logger;
import com.volantis.vdp.scs.connectors.client.connection.ClientConnection;

import com.volantis.vdp.configuration.scs.ISCSConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * TCP/IP connector used to establish connections between client (for example MCS) and SecureConnectionServer.
 */
public class ClientConnector extends Thread {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ClientConnector.class);

    private ServerSocket serv = null;

    private boolean run;

    private ISCSConfiguration config;

    /**
     * Default constructor creating ServerSocket that listens to client request.
     */
    public ClientConnector(ISCSConfiguration config) {
        this.run = true;
        this.config = config;

        try {
            serv = new ServerSocket(config.getProxyPort());
        } catch(IOException ioe) {
            logger.error("io-exception");
        }
    }

    /**
     * Establishes connections between client(for example MCS) and SecureConnectionServer.
     * Creates new instance a ClientConnection class.
     */
   public void run() {
            try {
                while(run) {
                    ClientConnection conn =
                            new ClientConnection(serv.accept(), config);
                }
            } catch(IOException ioe) {
                logger.error("io-exception");
            }
   }

    /**
     * Stops the connector.
     */
    public void stopServer() {
        this.run = false;
    }
}
