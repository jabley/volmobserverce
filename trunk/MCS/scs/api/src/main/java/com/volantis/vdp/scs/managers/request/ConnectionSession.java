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
package com.volantis.vdp.scs.managers.request;

import com.volantis.vdp.scs.connectors.client.connection.ClientBroker;
import com.volantis.vdp.scs.connectors.sps.connection.SPSBroker;

/**
 * The ConnectionSession binds the ClientBroker, the SPSBroker and request ID of SCPPacket along.
 */
public class ConnectionSession {

    private int reqId;

    private ClientBroker clientBroker;

    private SPSBroker spsBroker;

    /**
     * Default constructor
     */
    public ConnectionSession() {
        this.reqId = 0;
        this.clientBroker = null;
        this.spsBroker = null;
    }

    /**
     * Creates the connection session.
     * @param reqId request ID of the SCP packet
     * @param clientBroker the client broker
     */
    public ConnectionSession(int reqId,
                             ClientBroker clientBroker) {
        this.reqId = reqId;
        this.clientBroker = clientBroker;
        this.spsBroker = null;
    }

    /**
     * Creates the connection session.
     * @param reqId request ID of the SCP packet
     * @param clientBroker the client broker
     * @param spsBroker the sps broker
     */
    public ConnectionSession(int reqId,
                             ClientBroker clientBroker,
                             SPSBroker spsBroker) {
        this.reqId = reqId;
        this.clientBroker = clientBroker;
        this.spsBroker = spsBroker;
    }

    /**
     * Returns the clientBroker.
     * @return the clientBroker
     */
    public ClientBroker getClientBroker() {
        return clientBroker;
    }
    /**
     * Sets the client broker.
     * @param clientBroker the clientBroker to set
     */
    public void setClientBroker(ClientBroker clientBroker) {
        this.clientBroker = clientBroker;
    }

    /**
     * Returns the reqId.
     * @return the request ID
     */
    public int getRequestId() {
        return reqId;
    }
    /**
     * Sets the request Id.
     * @param reqId the request ID to set
     */
    public void setRequestId(int reqId) {
        this.reqId = reqId;
    }
    /**
     * Returns the SPSBroker.
     * @return the SPSBroker
     */
    public SPSBroker getSPSBroker() {
        return spsBroker;
    }
    /**
     * Sets the SPSBroker.
     * @param spsBroker the SPSBroker to set
     */
    public void setSPSBroker(SPSBroker spsBroker) {
        this.spsBroker = spsBroker;
    }
}
