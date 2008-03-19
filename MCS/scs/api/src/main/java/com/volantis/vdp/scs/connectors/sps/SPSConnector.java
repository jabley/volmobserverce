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
package com.volantis.vdp.scs.connectors.sps;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.volantis.vdp.scs.connectors.sps.connection.SPSWorker;
import com.volantis.vdp.configuration.scs.ISCSConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.net.ssl.*;

/**
 * TCP/IP connector used to establish encoded connections between SecureConnectionServer and sps.
 */
public class SPSConnector {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SPSConnector.class);

    private SSLServerSocket serv;

    private ISCSConfiguration config;

    /** 
     * Default anonymous cipher.
     */
    final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };

    /**
     * Default constructor creating SSLServerSocket that listens to sps client request.
     */
    public SPSConnector(ISCSConfiguration config) {
        this.config = config;
        try {

            // Connect to server and open I/O streams

            SSLServerSocketFactory socketServerFactory =
                (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

            serv = (SSLServerSocket) socketServerFactory.
                    createServerSocket(config.getSCPort());
            serv.setEnabledCipherSuites(enabledCipherSuites);
            startSPSConnector();
        } catch(IOException ioe) {
            logger.error("io-exception", ioe);
        }

    }

    /**
     * Establishes encoded connection between SecureConnectionServer and sps.
     * Creates new instance a SPSWorker class.
     */
    private void startSPSConnector() {
        Runnable r = new Runnable() {
            public void run() {
                while (true) {

                    try {
                        SSLSocket acceptedSocket =  (SSLSocket) serv.accept();
                        new SPSWorker(acceptedSocket,
                                config).start();
                    } catch (IOException ioe) {
                        logger.error("io-exception", ioe);
                    }
                }
            }
        };
        new Thread(r).start();
    }

    /**
     * Stops the connector.
     */
    public void stop() {

        try {
            if( serv != null && !serv.isClosed()) {
                serv.close();
            }
        } catch(IOException ioe) {
            logger.error("io-exception", ioe);
        }
    }
}
