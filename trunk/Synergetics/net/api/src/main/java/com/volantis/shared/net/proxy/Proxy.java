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

package com.volantis.shared.net.proxy;

/**
 * Represents a proxy.
 *
 * <p>A proxy in this sense is a host to which requests must be routed,
 * normally required in order to access hosts outside a protected network.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate 
 */
public interface Proxy {

    /**
     * Get the proxy host.
     *
     * @return The host.
     */
    String getHost();

    /**
     * Get the proxy port.
     *
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
     * @return true if authhroization canbe used for this proxy
     */
    boolean useAuthorization();
}
