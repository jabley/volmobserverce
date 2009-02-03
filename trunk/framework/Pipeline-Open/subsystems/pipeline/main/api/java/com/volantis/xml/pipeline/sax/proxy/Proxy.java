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
package com.volantis.xml.pipeline.sax.proxy;

/**
 * Used to represent information about a proxy
 *
 * @deprecated Don't use, not public.
 */
public interface Proxy {

    /**
     * Get id.
     * @return The id.
     */
    String getId();

    /**
     * Get the proxy host.
     * @return The host.
     */
    String getHost();

    /**
     * Get the proxy port.
     * @return The port.
     */
    int getPort();

    /**
     * @return the user name for proxy authentication
     */
    String getUser();

    /**
     * @return the password used for proxy authentication
     */
    String getPassword();

    /**
     * Return true if the proxy setting represented by this object should be
     * used for the host name specified.
     *
     * @param hostname the hostname to examine
     * @return true if these proxy settings should be used for the specified
     * host
     */
    boolean useForHost(String hostname);

    /**
     * @return true if authhroization canbe used for this proxy
     */
    boolean useAuthorization();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/1	matthew	VBM:2005092809 Allow proxy configuration via system properties

 ===========================================================================
*/
