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

package com.volantis.xml.pipeline.sax.proxy;

import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.net.proxy.ProxyFactory;
import com.volantis.shared.net.proxy.ProxyBuilder;

/**
 * Adapts an old style {@link Proxy} to a new style
 * {@link com.volantis.shared.net.proxy.Proxy}.
 */
public class ProxyManagerAdapter
        implements ProxyManager {

    /**
     * The old style proxy.
     */
    private final Proxy proxy;

    /**
     * The new style proxy that encapsulates information from old style
     * proxy apart from the {@link Proxy#useForHost(String)} method.
     */
    private final com.volantis.shared.net.proxy.Proxy newProxy;

    /**
     * Initialise.
     *
     * @param proxy The old style proxy.
     */
    public ProxyManagerAdapter(Proxy proxy) {
        this.proxy = proxy;

        ProxyFactory factory = ProxyFactory.getDefaultInstance();

        ProxyBuilder builder = factory.createProxyBuilder();
        builder.setHost(proxy.getHost());
        builder.setPort(proxy.getPort());
        builder.setUser(proxy.getUser());
        builder.setPassword(proxy.getPassword());
        this.newProxy = builder.buildProxy();
    }

    // Javadoc inherited.
    public com.volantis.shared.net.proxy.Proxy getProxyForHost(String host) {
        if (proxy.useForHost(host)) {
            return newProxy;
        } else {
            return null;
        }
    }
}
