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

/**
 *  The SPSConnection allows to send the data to the sps server if an active flag is true.
 */
public class SPSConnection {

    private SPSBroker broker;

    private boolean active;

    /**
     * Constructor the SPSConnection
     * @param broker sps broker that will be associated with this SPSConnection
     */
    public SPSConnection(SPSBroker broker) {

        this.broker = broker;

        this.active = false;
    }

    /**
     * Returns the SPSBroker associated with a object this class.
     * @return the SPSBroker
     */
    public SPSBroker getBroker() {
        return this.broker;
    }

    /**
     * Returns an active flag.
     * @return an active flag. True if the sps connection is active or flase if not
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets an active flag.
     * @param active a flag
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
