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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.vdp.sps.request.impl;

import com.volantis.vdp.sps.connector.IConnector;

import java.util.Date;

/**
 *
 * This Class wrap connection and thread joined with this connection
 *
 * User: rstroz01
 * Date: 2006-01-06
 * Time: 10:39:18
 */
public class WorkConnectionThreads {
    private Thread thread;
    private Date lastAccessTime;
    private IConnector connector;

    /**
     * Construct this class using Thread object and connection object
     * @param thread
     * @param connector
     */
    public WorkConnectionThreads(Thread thread, IConnector connector) {
        this.thread = thread;
        this.connector = connector;

        lastAccessTime = new Date();
    }

    /**
     * Method return true if connector is connected and false if else
     * @return
     */
    public boolean isConnected() {
        return getConnector().isConnected();
    }

    /**
     * Method start thread and set last Acces Time
     *
     */
    public void start() {
        lastAccessTime = new Date();
        this.thread.start();
    }

    /**
     * method return true if connection is timed out and false if else
     * @return
     */
    public boolean isTimeOuted() {
        if ((new Date().getTime() - lastAccessTime.getTime()) > 300*1000) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return request identifier
     */
    public int getRequestIdentifier() {
        return this.connector.getRequestId();
    }

    /**
     *
     * @return connector object
     */
    public IConnector getConnector() {
        return this.connector;
    }
}
