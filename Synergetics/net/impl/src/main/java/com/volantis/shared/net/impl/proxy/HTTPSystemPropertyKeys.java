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
/**
 * (c) Copyright Volantis Systems Ltd. 2005. 
 */
package com.volantis.shared.net.impl.proxy;

/**
 * Contain a set of key into the System properties related to Proxy
 * configurations.
 */
public class HTTPSystemPropertyKeys {

    /**
     * The system property key to retrieve the generic proxy host name
     */
    public static final String PROXY_HOST = "proxyHost";

    /**
     * The system property key to retrieve the generic proxy port number,
     * the default port for the protocol should be used if this property does
     * not exist, or is empty.
     */
    public static final String PROXY_PORT = "proxyPort";

    /**
     * The "|" separated list of hosts for which a proxy is not needed
     */
    public static final String PROXY_EXCLUDE = "http.nonProxyHosts";

    /**
     * The system property key to retrieve the http proxy host name
     */
    public static final String HTTP_PROXY_HOST = "http.proxyHost";

    /**
     * The password to use when authenticating with the specified proxy
     */
    public static final String PROXY_PASSWORD = "volantis.pipeline.proxyPassword";

    /**
     * The system property key to retrieve the http proxy port number
     * (80 should be used if this property does not exist or is empty)
     */
    public static final String HTTP_PROXY_PORT = "http.proxyPort";

    /**
     * The username to supply to the specified proxy
     */
    public static final String PROXY_USER = "volantis.pipeline.proxyUser";

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/1	matthew	VBM:2005092809 Allow proxy configuration via system properties

 ===========================================================================
*/
