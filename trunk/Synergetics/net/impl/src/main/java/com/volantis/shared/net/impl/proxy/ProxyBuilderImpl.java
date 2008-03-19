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
import com.volantis.shared.net.proxy.ProxyBuilder;

/**
 * Default implementation of {@link ProxyBuilder}.
 */
public class ProxyBuilderImpl
        implements ProxyBuilder {

    /**
     * The host.
     */
    private String host;

    /**
     * The port.
     */
    private int port;

    /**
     * The user.
     */
    private String user;

    /**
     * The password.
     */
    private String password;

    // Javadoc inherited.
    public String getHost() {
        return host;
    }

    // Javadoc inherited.
    public void setHost(String host) {
        if ("".equals(host)) {
            throw new IllegalArgumentException("host may not be empty");
        }
        this.host = host;
    }

    // Javadoc inherited.
    public int getPort() {
        return port;
    }

    // Javadoc inherited.
    public void setPort(int port) {
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("port must be within range 1..65535 inclusive but is " +
                    port);
        }
        this.port = port;
    }

    // Javadoc inherited.
    public String getUser() {
        return user;
    }

    // Javadoc inherited.
    public void setUser(String user) {
        if ("".equals(user)) {
            throw new IllegalArgumentException("user may not be empty");
        }
        this.user = user;
    }

    // Javadoc inherited.
    public String getPassword() {
        return password;
    }

    // Javadoc inherited.
    public void setPassword(String password) {
        if ("".equals(password)) {
            throw new IllegalArgumentException("password may not be empty");
        }
        this.password = password;
    }

    // Javadoc inherited.
    public Proxy buildProxy() {
        if (host == null) {
            throw new IllegalArgumentException("host cannot be null");
        }
        if ((user == null) != (password == null)) {
            throw new IllegalArgumentException(
                    "Either user and password must both be specified, " +
                    "or both be null but user='" + user +
                    "' and password = '" + password + "'");
        }

        return new ProxyImpl(this);
    }
}
