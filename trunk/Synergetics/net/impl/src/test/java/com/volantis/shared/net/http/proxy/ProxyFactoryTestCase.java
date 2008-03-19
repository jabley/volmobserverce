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

package com.volantis.shared.net.http.proxy;

import com.volantis.shared.net.impl.proxy.HTTPSystemPropertyKeys;
import com.volantis.shared.net.impl.proxy.ProxyFactoryImpl;
import com.volantis.shared.net.proxy.Proxy;
import com.volantis.shared.net.proxy.ProxyFactory;
import com.volantis.shared.net.proxy.ProxyManager;

/**
 * Tests for the {@link ProxyFactory}.
 */
public class ProxyFactoryTestCase
        extends ProxyTestAbstract {

    /**
     * Ensure that if no proxy host is specified, or it is empty then no system
     * proxy is provided.
     *
     * @throws Exception
     */
    public void testNoHost() throws Exception {

        // Ensure that when the host is unspecified or empty that no system
        // proxy is created.
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.HTTP_PROXY_HOST, "").returns(" ");
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_HOST, "").returns(" ");

        ProxyFactory factory = new ProxyFactoryImpl(systemPropertiesMock);
        ProxyManager manager = factory.getSystemProxyManager();
        assertNull(manager);
    }

    /**
     * Ensure that when all proxy related properties are specified that the
     * system proxy and its manager are configured properly.
     */
    public void testAll() throws Exception {

        // Ensure that when the host is specified a system proxy is created.
        addHTTPPropertyExpectations("foobar", "99", "me", "mine",
                "*.volantis.com|www.google.com");

        ProxyFactory factory = new ProxyFactoryImpl(systemPropertiesMock);
        ProxyManager manager = factory.getSystemProxyManager();

        Proxy proxy = manager.getProxyForHost("www.google.com2");
        assertNotNull(proxy);
        checkProxy(proxy, "foobar", 99, "me", "mine");

        assertNull(manager.getProxyForHost("www.volantis.com"));
        assertNull(manager.getProxyForHost("foo.volantis.com"));
        assertNull(manager.getProxyForHost("www.google.com"));
    }
}

