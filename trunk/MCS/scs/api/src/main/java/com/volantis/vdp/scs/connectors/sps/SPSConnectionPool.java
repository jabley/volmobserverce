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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.volantis.vdp.scs.connectors.sps.connection.SPSConnection;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * A list of connections and URLs associated with these connections.
 */
public class SPSConnectionPool {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SPSConnectionPool.class);

    private static SPSConnectionPool pool = new SPSConnectionPool();

    private static List urls;

    private static List connections;

    /**
     * Default constructor.
     */
    public SPSConnectionPool() {

        urls = new ArrayList();
        connections = new ArrayList();

    }

    /**
     * Adds a new connection and URL association to the list of all connections.
     * @param url identifies a sps server
     * @param connection identifies a sps connection
     */
    public static void addConnection(URL url, SPSConnection connection) {
        if(logger.isDebugEnabled()) {
            logger.debug("Add new connectors to a pool connections.");
        }

        urls.add(url);
        connections.add(connection);

    }

    /**
     * Removes connection and URL association from mapping list by SPSConnection instance.
     * @param connection indetifies connection and URL association from mapping list
     */
    public static void removeConnection(SPSConnection connection) {
        if(logger.isDebugEnabled()) {
            logger.debug("Remove connectors from a pool connections.");
        }

        int index = connections.indexOf(connection);
        if(index != -1) {
            connections.remove(index);
            urls.remove(index);
        }
    }

    /**
     * Removes connection and URL assiocation from mapping list by URL.
     * @param url indetifies connection and URL association from mapping list
     */
    public static void removeConnectionByURL(URL url) {
        if(logger.isDebugEnabled()) {
            logger.debug("Remove connectors from a pool connections.");
        }

        int index = urls.indexOf(url);
        if(index != -1) {
            connections.remove(index);
            urls.remove(index);
        }

    }

    /**
     * Returns a sps connection bound with a specified URL.
     * @param url identifies connection and URL assiocation in the list
     * @return a sps connection bound with a specified URL
     */
    public static SPSConnection getConnectionByUrl(URL url) {

        SPSConnection conn = null;

        int index = urls.indexOf(url);
        if(index != -1) {
            conn = (SPSConnection) connections.get(index);
        }

        return conn;

    }

    /**
     * Bounds the given URL with a specified connection.
     * @param url binds sps connection with this URL
     * @param conn identifies connection and URL association in the list
     */
    public static void setURLByConnection(URL url, SPSConnection conn) {

        int index = connections.indexOf(conn);
        if(index != -1) {
            urls.set(index, url);
        }
    }

    /**
     * Returns number and list of connections as string.
     * @return number and list of connections as string
     */
    public static String print() {

        String retVal = "";

        retVal += "Number of connections: " + urls.size() + "\n\n";


        for(int i = 0; i < urls.size(); i++) {

            URL url = (URL) urls.get(i);
            SPSConnection conn = (SPSConnection) connections.get(i);

            retVal += url.toString() + ": " + conn.isActive() + "\n";
        }

        return retVal;

    }

    /**
     * Returns a list of all active connections.
     * @return list of all active connections
     */
    public static List getAllActiveConnections() {

        List activeConnections = new ArrayList();

        for(int i = 0; i < urls.size(); i++) {
            SPSConnection conn = (SPSConnection) connections.get(i);
            if(urls.get(i) != null && conn.isActive()) {
                activeConnections.add(conn);
            }
        }

        return activeConnections;
    }
}
