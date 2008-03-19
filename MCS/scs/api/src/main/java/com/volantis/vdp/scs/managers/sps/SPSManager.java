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
package com.volantis.vdp.scs.managers.sps;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.volantis.vdp.scs.connectors.client.connection.ClientBroker;
import com.volantis.vdp.scs.managers.request.RequestManager;
import com.volantis.vdp.scs.protocol.SCPPacket;
import com.volantis.vdp.scs.proxy.http.ProxyHTTPManager;
import com.volantis.vdp.scs.proxy.https.ProxyHTTPSManager;
import com.volantis.vdp.scs.util.Request;
import com.volantis.vdp.scs.connectors.sps.connection.SPSBroker;
import com.volantis.vdp.scs.connectors.sps.SPSConnectionPool;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Interface switching requests and responses. In case of request, checks if URL occuring in request occurs in connections and URLs assiociation list.
 * If yes, the request is sent to sps server. If not, the request is simply is proxied to the URL it contains.
 */
public class SPSManager {

    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(SPSManager.class);

    private static SPSManager manager = new SPSManager();

    public static int nreq = 0;
    public static int nres = 0;

    /**
     * Default constructor.
     */
    public SPSManager() {
    }

    /**
     * Sends HTTP request to the server.
     * @param data a byte array
     * @param request request sent by client
     * @param reqId idetifies a SCP packet sent to sps server
     * @param clientBroker client's broker
     */
    public static void sendHTTPRequest( String data,
                                        Request request,
                                        int reqId,
                                        ClientBroker  clientBroker) {

        URL url;

        try {
            url = new URL(request.getURL());
            if(isSPS(url)) {
                SPSBroker broker =
                    SPSConnectionPool.getConnectionByUrl(url).getBroker();
                SCPPacket packet = new SCPPacket(
                        SCPPacket.HTTP_REQUEST_RESPONSE,
                        reqId,
                        data.getBytes());
                broker.send(packet.getBytes());
                clientBroker.addConnToReqTmManager(reqId);

            } else {
                if(logger.isDebugEnabled()) {
                    logger.debug("HTTP proxing: " + url);
                }
                ProxyHTTPManager.forwardRequest(request, clientBroker);
            }
        } catch(MalformedURLException mue) {
            String[] mess = new String[1];
            mess[0] = mue.getMessage();
            logger.error("bad-url");
        }
    }

    /**
     * Sends the HTTP response to client.
     * @param packet the response from sps
     */
    public static void sendHTTPResponse( SCPPacket packet ) {

        int reqId = packet.getRequestIdetifier();

        ClientBroker broker = RequestManager.getClientBroker(reqId);

        if(broker != null) {
            broker.send(packet);
        }

    }

    /**
     * Sends HTTPS request to the server.
     * @param data a byte array
     * @param request request sent by client
     * @param reqId idetifies a SCP packet sent to sps server
     * @param clientBroker client's broker
     */
    public static void sendHTTPSRequest( String data,
                                         Request request,
                                         int reqId,
                                         ClientBroker clientBroker) {

        try {
            URL url = new URL("https://" + request.getURL());
            if(isSPS(url)) {
                SPSBroker broker =
                    SPSConnectionPool.getConnectionByUrl(url).getBroker();
                RequestManager.getSession(reqId).setSPSBroker(broker);
                SCPPacket packet = new SCPPacket(
                        SCPPacket.HTTPS_REQUEST_RESPONSE,
                        reqId,
                        data.getBytes());
                broker.send(packet.getBytes());
                clientBroker.addConnToReqTmManager(reqId);
            } else {
                if(logger.isDebugEnabled()) {
                    logger.debug("HTTPS proxing: " + request.getURL());
                }
                ProxyHTTPSManager.forwardRequest(request, clientBroker);
            }
        } catch(MalformedURLException mue) {
            String[] mess = new String[1];
            mess[0] = mue.getMessage();
            logger.error("bad-url");
        }
    }

    /**
     * Sends HTTPS request to the server.
     * @param data data that will be send to sps server
    * @param reqId idetifies a SCP packet sent to sps server
     */
    public static void sendHTTPSRequest( byte[] data, int reqId ) {

        SPSBroker spsBroker = RequestManager.getSPSBroker(reqId);
        SCPPacket packet = new SCPPacket(
                SCPPacket.HTTPS_REQUEST_RESPONSE,
                reqId,
                data);
        spsBroker.send(packet.getBytes());

    }

    /**
     * Sends the HTTPS response to client.
     * @param packet the response from sps
     */
    public static void sendHTTPSResponse( SCPPacket packet ) {

        int reqId = packet.getRequestIdetifier();

        ClientBroker clientBroker = RequestManager.getClientBroker(reqId);

        clientBroker.send(packet.getDatablock());

    }

    /**
     * Checks if given URL occurs in connections and URLs assiociation list.
     * @param url idetifies a sps server
     * @return true if URL occurs in connections and URLs assiociation list or flase if not
     */
    public static boolean isSPS(URL url) {
        return ( SPSConnectionPool.getConnectionByUrl(url) != null &&
                 SPSConnectionPool.getConnectionByUrl(url).isActive() );
    }
}
