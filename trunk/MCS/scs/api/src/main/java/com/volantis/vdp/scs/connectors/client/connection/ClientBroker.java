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
package com.volantis.vdp.scs.connectors.client.connection;

import java.util.*;

import com.volantis.vdp.scs.managers.sps.SPSManager;
import com.volantis.vdp.scs.managers.request.RequestIdentifierManager;
import com.volantis.vdp.scs.managers.request.RequestManager;

import com.volantis.vdp.configuration.scs.ISCSConfiguration;

import com.volantis.vdp.scs.util.Request;
import com.volantis.vdp.scs.util.Samples;

import com.volantis.vdp.scs.protocol.SCPPacket;
import com.volantis.vdp.scs.proxy.Proxy;

/**
 *  Magages the client connection. Responsible for forwarding requests and responses between
 *  client (for example MCS) and remote host, and for managing timeouts.
 */
public class ClientBroker {

    private Proxy proxy;
    private boolean proxyHTTP;
    private boolean proxyHTTPS;

    private ClientConnection clientConnection;

    private int currentReqId;

    private boolean timerRun;

    private HashMap reqTimeoutManager;

    private ISCSConfiguration config;

    private String host;

    /**
     * Creates ClientBroker bound with the specified client connection.
     * @param clientConnection client connection
     */
    public ClientBroker(ClientConnection clientConnection, ISCSConfiguration config) {
        this.proxy = null;
        this.proxyHTTP = false;
        this.proxyHTTPS = false;

        this.clientConnection = clientConnection;

        this.timerRun = true;

        reqTimeoutManager = new HashMap();

        this.config = config;

        this.host = null;

        timer();
    }

    /**
     * Forwards client requests to sps manager, using HTTP or HTTPS depending on the protocol specified by client.
     * @param data the client request
     */
    public void work(String data) {
        List requests = convertRequest(data);

        if( !proxyHTTP && !proxyHTTPS ) {
            ListIterator iter = requests.listIterator() ;

            while( iter.hasNext() ) {
                Request request = (Request) iter.next();

                this.currentReqId = RequestIdentifierManager.getNewRequestId();
                RequestManager.addReqIdConnection(currentReqId, this);

                if( request.getType().equals("HTTPS")) {
                    SPSManager.sendHTTPSRequest(data, request,
                            currentReqId, this);
                } else {
                    SPSManager.sendHTTPRequest(data, request,
                            currentReqId, this);
                }

                if( request.getConnection().equalsIgnoreCase("keep-alive") ) {
                    clientConnection.
                            upgradeKeepAliveTime();
                    clientConnection.setKeepAlive(true);
                }
            }
        } else {
                proxy.send(data.getBytes());
                clientConnection.
                        upgradeKeepAliveTime();

        }
    }

    /**
     * Sends the data to the client.
     * @param packet the data as object SCPPacket class
     */
    public void send(SCPPacket packet) {
        removeConnFromReqTmManager(packet.getRequestIdetifier());
        this.clientConnection.send(packet.getDatablock());
    }

    /**
     * Sends the data to the client.
     * @param data the data as int
     */
    public void send(int data) {
        this.clientConnection.send(data);
    }

    /**
     * Sends the data to client.
     * @param buffer the data as byte array
     */
    public void send(byte[] buffer) {
        this.clientConnection.send(buffer);
    }

    /**
     * Sends the data to client.
     * @param buffer the data as byte array
     * @param off the start offset in the data
     * @param len the number of bytes to write
     */
    public void send(byte[] buffer, int off, int len) {
        this.clientConnection.send(buffer, off, len);
    }

    /**
     * Adds request ID to the timeouts list.
     * @param reqId request Id of the sent SCP packet
     */
    public void addConnToReqTmManager(int reqId) {
        addConnToReqTmManager(new Integer(reqId));
    }

    /**
     * Adds request ID to the timeouts list.
     * @param reqId request Id of the sent SCP packet
     */
    public void addConnToReqTmManager(Integer reqId) {
        this.reqTimeoutManager.put(reqId,
                new Long(System.currentTimeMillis()));
    }

    /**
     * Removes request ID from the timeouts list.
     * @param reqId request Id of the sent SCP packet
     */
    public void removeConnFromReqTmManager(int reqId) {
        removeConnFromReqTmManager(new Integer(reqId));
    }

    /**
     * Removes request ID to the timeouts list.
     * @param reqId request Id sent SCP packet
     */
    public void removeConnFromReqTmManager(Integer reqId) {
        this.reqTimeoutManager.remove(reqId);
    }

    /**
     * Sets the proxy parameter.
     * @param proxy proxy parameter
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Sets the proxyHTTP parameter.
     * @param on sets the proxyHTTP parameter
     */
    public void setProxyHTTP(boolean on) {
        this.proxyHTTP = on;
    }

    /**
     * Sets the proxyHTTPS parameter.
     * @param on sets the proxyHTTPS parameter
     */
    public void setProxyHTTPS(boolean on) {
        this.proxyHTTPS = on;
    }

    /**
     * Closes the client connection.
     */
    public void close() {
        if( proxy != null ) proxy.close();
        this.clientConnection.close();
    }

    /**
     * Enable/disable SO_KEEPALIVE.
     * @param on whether or not to have socket keep alive turned on
     */
    public void setKeepAlive(boolean on) {
        this.clientConnection.setKeepAlive(on);
    }

    /**
     * Checks periodically the timeout condition using additional thread.
     */
    private void timer() {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    while(timerRun) {
                        check();
                        Thread.sleep(1000);
                    }
                } catch(InterruptedException ie) {
                }
            }
        };
        new Thread(r).start();
    }

    /**
     * Checks the timeout condition
     */
    private void check() {
        Iterator iter = reqTimeoutManager.keySet().iterator();
        while( iter.hasNext() ) {
            Integer tempReqId = (Integer) iter.next();
            Long start = (Long) reqTimeoutManager.get(tempReqId);
            if( start != null &&
                    System.currentTimeMillis() - start.longValue() >
                            this.config.getProxyGatewayTimeoutt() * 1000) {
                this.clientConnection.send(Samples.gwTimeout().getBytes());
                this.reqTimeoutManager.remove(tempReqId);
                RequestManager.removeSessionByReqId(tempReqId);
            }
        }
    }

    /**
     * Creates the list of requests from sequence of data.
     * @param data data given as a String
     * @return list of requests
     */
    private List convertRequest(String data) {
        List listOfRequest = new ArrayList();

        int index = data.indexOf("\r\n\r\n");
        Integer contentLength = null;

        while( index >= 0 ) {
            String headerRequest = data.substring(0, index);
            data = data.substring(index).trim();
            Request request = new Request(headerRequest);
            if(this.host == null) {
                this.host = request.getHost();
            } else if(!this.host.equals(request.getHost())) {
                this.host = request.getHost();
                if( this.proxy != null ) {
                    this.proxy.close();
                    this.proxy = null;
                }
                proxyHTTP = false;
                proxyHTTPS = false;
            }

            if(request.getContentLength() > 0) {
                byte[] content = new byte[request.getContentLength()];
                if(request.getContentLength() <= data.length()) {
                    System.arraycopy(data.substring(0,
                        request.getContentLength()).getBytes(), 0, content,
                        0, request.getContentLength());
                        data = data.substring(request.getContentLength());
                } else {
                    System.arraycopy(data.getBytes(), 0, content,
                            0, data.length());
                    data = "";
                }
                request.setContent(content);
            }
            listOfRequest.add(request);
            index = data.indexOf("\r\n\r\n");
        }

        return listOfRequest;
    }
}
