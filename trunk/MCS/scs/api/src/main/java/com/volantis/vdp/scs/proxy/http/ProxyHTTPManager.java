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
package com.volantis.vdp.scs.proxy.http;

import java.util.HashMap;

import com.volantis.vdp.scs.connectors.client.connection.ClientBroker;
import com.volantis.vdp.scs.util.Request;

/**
 * Manages the http proxy.
 */
public class ProxyHTTPManager {

    private static HashMap clientConnections = new HashMap();

    /**
     * Forwards the given request using the given client broker and waits for the response.
     * @param request forwarded request
     * @param clientBroker client broker used to obtain the response
     */
    public static void forwardRequest(Request request,
                                      ClientBroker clientBroker) {

      /*  if(clientConnections.containsKey(request.getHost())) {
            logger.debug("From pool connections.");
            ProxyHTTP proxy = (ProxyHTTP)
                                clientConnections.get(request.getHost());
            proxy.redirect(request);
        } else {
            logger.debug("New connectors.");*/
           new ProxyHTTP(request, clientBroker);

/*            clientConnections.put(request.getHost(), proxy);
        }*/

    }

}
