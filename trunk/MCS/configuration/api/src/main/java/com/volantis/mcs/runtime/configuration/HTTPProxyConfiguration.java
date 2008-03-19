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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 *  The configuration for the HTTP Proxy.
 */

public class HTTPProxyConfiguration {

    /**
     * The host on which the HTTP proxy resides.
     */
    private String host;

    /**
     * The port on which the HTTP proxy is listening;
     */
    private String port;



    /**
     * The the value of the host.
     *
     * @return The host as a string.
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the value of the host.
     *
     * @param host The name of the host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Get the port number.
     *
     * @return The port as an String.
     */
    public String getPort() {
        return port;
    }

    /**
     * Set the value of the port.
     *
     * @param port The port on which the proxy is listening.
     */
    public void setPort(String port) {
        this.port = port;
    }


}
