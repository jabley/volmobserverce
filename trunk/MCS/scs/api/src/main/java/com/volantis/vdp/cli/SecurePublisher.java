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
package com.volantis.vdp.cli;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.vdp.configuration.ConfigurationFactory;
import com.volantis.vdp.configuration.sps.ISPSConfiguration;
import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.sps.authentication.Auth;
import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.configuration.ConfigurationFactory;
import com.volantis.vdp.scs.authentication.AuthenticationToken;
import com.volantis.vdp.scs.connectors.client.ClientConnector;
import com.volantis.vdp.sps.connector.KeepAliveMonitor;
import com.volantis.vdp.sps.request.ISCPReader;
import com.volantis.vdp.sps.request.impl.SCPReader;
import com.volantis.vdp.sps.response.ISCPWriter;
import com.volantis.vdp.sps.response.impl.SCPWriter;
import com.volantis.synergetics.log.LogDispatcher;
import org.apache.log4j.xml.DOMConfigurator;

import javax.net.ssl.*;
import javax.xml.parsers.FactoryConfigurationError;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * The CLI command for starting up a Secure Publisher.
 */
public class SecurePublisher {


    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SecurePublisher.class);

    /**
     * when network connection error system
     */
    public static final int LOGIN_FAILED_NETWORK_ERROR = 4;
    public static final String[] AUTH_FAILED_MESSAGES = {
            "invalid userid",
            "invalid user password",
            "user password expired",
            "network error"};
    /**
     * Socket for connection to Secure Proxy Server
     */
    private SSLSocket server;
    private Thread readerThread;
    private String host;
    private int port;
    
    /**
     * Default anonymous cipher.
     */
    private final String[] enabledCipherSuites =
            { "SSL_DH_anon_WITH_RC4_128_MD5" };

    /**
     * Configuration interface at this time is not implemented
     */
    private ISPSConfiguration config;
    private DataInputStream input;
    private DataOutputStream output;
    private Auth auth;

    public static void main(String[] args) {
        System.out.println(System.getProperty("config.dir"));
        SecurePublisher publisher = new SecurePublisher();
        publisher.init();
        publisher.start();
    }


    public void start() {
        ISCPReader reader = new SCPReader(input, config);
        ISCPWriter writer = new SCPWriter(output);

        readerThread = new Thread((SCPReader) reader);

        readerThread.start();
    }


    /**
     * Method read configuration, initialize connection to SCP,
     * send authorization request and receive auth response
     */
    private void init() {

        ConfigurationFactory configurationFactory =
                ConfigurationFactory.getInstance();
        try {
            config = (ISPSConfiguration) configurationFactory
                    .getConfiguration(ISPSConfiguration.class);
        } catch (ConfigurationException e) {
            System.out.println("Can't read configuration file with error: " +
                    e.getMessage());
            System.exit(1);
        }


        try {
            DOMConfigurator.configure(config.getLog4jFile());
        } catch (FactoryConfigurationError e) {
            System.out.println("Can't initialize log4j");
        }

        host = config.getSCHost();
        port = config.getSCPort();

        try {

            // Connect to server and open I/O streams
            server = configureSCSSocket();
            input = new DataInputStream(server.getInputStream());
            output = new DataOutputStream(server.getOutputStream());
            auth = new Auth(input, output, config);
            int authorization = auth.auth();

            if (authorization != AuthenticationToken.LOGIN_SUCCESFUL) {
                // Authorization failed write message to stdout and terminate server with error code
                System.out.println("Autorization failed: " +
                        AUTH_FAILED_MESSAGES[authorization - 1]);
                System.exit(authorization);
            }

            KeepAliveMonitor keepAliveMonitor = new KeepAliveMonitor(this);
            Thread t = new Thread(keepAliveMonitor);
            t.setDaemon(true);
            t.start();

        } catch (ConnectException e) {
            logger.error("connection-exception", e);
            System.exit(1);
        } catch (IOException e) {
            logger.error("io-exception", e);
            System.exit(2);
        }
    }

    /**
     * Method started server again if connection is timed out
     *
     * @return true if connect and false if not connect
     */
    public boolean startServer() {

        boolean connected = false;

        if (readerThread.isAlive()) {
            readerThread.destroy();
        }

        if (server.isConnected()) {
            try {
                server.close();
            } catch (IOException ex) {

            }
        }

        try {
            server = configureSCSSocket();
            input = new DataInputStream(server.getInputStream());
            output = new DataOutputStream(server.getOutputStream());
            auth = new Auth(input, output, config);
            if (auth.auth() == AuthenticationToken.LOGIN_SUCCESFUL) {
                connected = true;
            }

        } catch (ConnectException e) {
            logger.error("connection-exception",e);
        } catch (IOException e) {
            logger.error("io-exception",e);
        }

        start();
        return connected;
    }


    /**
     * A utility method to configure an SSL socket with anonymous ciphers.
     *
     * @return An SSLXocket if succesful.
     * @throws IOException If an error occurs creating the socket.
     */
    private SSLSocket configureSCSSocket() throws IOException {
        SSLSocket socket;
        SSLSocketFactory factory =
                (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) factory.createSocket(host, port);

        socket.setEnabledCipherSuites(enabledCipherSuites);
        return socket;
    }
}

