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
package com.volantis.vdp.cli;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.xml.DOMConfigurator;

import com.volantis.vdp.scs.connectors.sps.SPSConnector;
import com.volantis.vdp.scs.connectors.sps.SPSKAMechanism;
import com.volantis.vdp.scs.connectors.client.ClientConnector;
import com.volantis.vdp.configuration.ConfigurationFactory;
import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.configuration.scs.ISCSConfiguration;
import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.configuration.ConfigurationFactory;

/**
 * Main class of Secure Connector Server.
 */
public class SecureConnectionServer {

    private ISCSConfiguration cfg;

    /**
     * Default constructor.
     */
    public SecureConnectionServer() {
    }

    /**
     * Initiates a logger.
     */
    private void initLogger() {
        try {
            DOMConfigurator.configure(cfg.getLog4jFile());
        } catch (FactoryConfigurationError e) {
            System.out.println("Can't init logger: " + e);
            System.exit(1);
        }
    }

    /**
     * Reads config file (scs-config.xml).
     */
    private void initConfiguration() {
        ConfigurationFactory factory = ConfigurationFactory.getInstance();
        try {
            cfg = (ISCSConfiguration)
                  factory.getConfiguration(ISCSConfiguration.class);
        } catch (ConfigurationException ce) {
            System.out.println("Can't read configuration file: " + ce);
            System.exit(1);
        }
    }

    /**
     * Starts the Secure Connector Server.
     */
    public void start() {
        initConfiguration();
        initLogger();
        new SPSConnector(cfg);
        new ClientConnector(cfg).start();
        new SPSKAMechanism(cfg).start();
    }

    /**
     * Main class. Checks if definited all parameters. If yes then calls start method.
     * @param args input parameters
     */
    public static void main(String[] args) {

        if( System.getProperty("config.dir") == null ) {
            System.out.println("You must give " +
                    "'config.dir' system parameter.");
            System.exit(1);
        }

        new SecureConnectionServer().start();
    }

}
