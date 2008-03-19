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

import java.util.ArrayList;
import java.util.List;

import com.volantis.vdp.scs.connectors.client.connection.ClientBroker;
import com.volantis.vdp.scs.connectors.sps.connection.SPSBroker;

/**
 * The list contains ConnectionSession objects.
 */
public class RequestManager {

    private static List sessions = new ArrayList();

    /**
     * Creates new connection session and adds to the list.
     * @param reqId the request ID
     * @param clientBroker the client broker
     */
    public static void addReqIdConnection(int reqId,
                                          ClientBroker clientBroker) {

        ConnectionSession session =
            new ConnectionSession(reqId, clientBroker);

        sessions.add(session);
    }

    /**
     * Creates new connection session and adds to the list.
     * @param reqId the request ID
     * @param clientBroker the client broker
     * @param spsBroker the sps broker
     */
    public static void addReqIdConnection(int reqId,
                                          ClientBroker clientBroker,
                                          SPSBroker spsBroker) {

        ConnectionSession session =
            new ConnectionSession(reqId, clientBroker, spsBroker);

        sessions.add(session);
    }

    /**
     * Removes a connection session from the list of connection sessions.
     * @param reqId the request ID
     */
    public static void removeSessionByReqId(Integer reqId) {
        removeSessionByReqId(reqId.intValue());
    }

    /**
     * Removes a connection session from the list of connection sessions.
     * @param reqId the request ID
     */
    public static void removeSessionByReqId(int reqId) {

        int index = indexOf(reqId);
        if( index != -1) sessions.remove(index);

    }

    /**
     * Returns the ClientBroker that is associated with the given request ID.
     * @param reqId the request ID
     * @return the ClientBroker
     */
    public static ClientBroker getClientBroker(int reqId) {

        int index = indexOf(reqId);
        ClientBroker clientBroker = null;

        if( index != -1 ) {
            ConnectionSession session =
                (ConnectionSession) sessions.get(indexOf(reqId));

            if(session != null)
                clientBroker = (ClientBroker) session.getClientBroker();
        }
        
        return clientBroker;

    }

    /**
     * Returns the ClientBroker that is associated with the given request ID.
     * @param reqId the request ID
     * @return the ClientBroker
     */
    public static ClientBroker getClientBroker(Integer reqId) {
        return getClientBroker(reqId.intValue());
    }

    /**
     * Returns the SPSBroker that is associated with the given request ID.
     * @param reqId the request ID
     * @return the SPSBroker
     */
    public static SPSBroker getSPSBroker(int reqId) {

        ConnectionSession session =
            (ConnectionSession) sessions.get(indexOf(reqId));

        SPSBroker spsBroker = null;

        if(session != null)
            spsBroker = (SPSBroker) session.getSPSBroker();

        return spsBroker;

    }

    /**
     * Returns the SPSBroker that is associated with the given request ID.
     * @param reqId the request ID
     * @return the SPSBroker
     */
    public static SPSBroker getSPSBroker(Integer reqId) {

        return getSPSBroker(reqId.intValue());

    }

    /**
     * Returns the ConnectionSession that is associated with the given request ID.
     * @param reqId the request ID
     * @return the ConnectionSession
     */
    public static ConnectionSession getSession(int reqId) {

        return (ConnectionSession)
                sessions.get(indexOf(reqId));

    }

    /**
     * Returns an index of connection session at the list of connection sessions.
     * @param reqId the request ID
     * @return an index of connection session at the list of connection sessions
     */
    private static int indexOf(int reqId) {

        int index = -1;

        for(int i = 0; i < sessions.size(); i++) {
            ConnectionSession session =
                (ConnectionSession) sessions.get(i);

            if( session.getRequestId() == reqId ) {
                index = i;
                break;
            }
        }

        return index;
    }
}
