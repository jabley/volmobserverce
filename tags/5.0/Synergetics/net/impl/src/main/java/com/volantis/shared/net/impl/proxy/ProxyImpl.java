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

package com.volantis.shared.net.impl.proxy;

import com.volantis.shared.net.proxy.Proxy;

/**
 * Default implementation of {@link Proxy}.
 */
public class ProxyImpl
        implements Proxy {

    /**
     * The host.
     */
    private final String host;

    /**
     * The port.
     */
    private final int port;

    /**
     * The user.
     */
    private final String user;

    /**
     * The password.
     */
    private final String password;

    /**
     * Initialise.
     *
     * @param builder The builder from which this will be initialised.
     */
    public ProxyImpl(ProxyBuilderImpl builder) {
        host = builder.getHost();
        port = builder.getPort();
        user = builder.getUser();
        password = builder.getPassword();
    }

    // Javadoc inherited.
    public String getHost() {
        return host;
    }

    // Javadoc inherited.
    public int getPort() {
        return port;
    }

    // Javadoc inherited.
    public String getUser() {
        return user;
    }

    // Javadoc inherited.
    public String getPassword() {
        return password;
    }

    // Javadoc inherited.
    public boolean useAuthorization() {
        return user != null;
    }
}
