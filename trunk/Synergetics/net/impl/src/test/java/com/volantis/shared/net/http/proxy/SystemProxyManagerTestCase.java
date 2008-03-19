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
import com.volantis.shared.net.impl.proxy.SystemProxyManager;
import com.volantis.shared.net.proxy.Proxy;
import com.volantis.shared.net.proxy.ProxyFactory;

/**
 * Test cases for {@link SystemProxyManager}.
 */
public class SystemProxyManagerTestCase
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

        SystemProxyManager manager = new SystemProxyManager(
                ProxyFactory.getDefaultInstance(),
                systemPropertiesMock);
        assertNull(manager.getSystemProxy());
    }

    /**
     * Ensure that if no http proxy host is specified, that the generic one is
     * used.
     *
     * @throws Exception
     */
    public void testGenericHost() throws Exception {


        // Ensure that when the host is unspecified or empty that no system
        // proxy is created.
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.HTTP_PROXY_HOST, "").returns(" ");

        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_HOST, "").returns("foobar");

        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_PORT, "").returns("90");

        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_USER, "").returns("me");
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_PASSWORD, "").returns("mine");
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_EXCLUDE, "").returns("");

        SystemProxyManager manager = new SystemProxyManager(
                ProxyFactory.getDefaultInstance(),
                systemPropertiesMock);
        Proxy proxy = manager.getSystemProxy();
        checkProxy(proxy, "foobar", 90, "me", "mine");
    }

    /**
     * Ensure that when the proxy host is specified but nothing else is that it
     * all defaults.
     *
     * @throws Exception
     */
    public void testHostOnly() throws Exception {

        // Ensure that when the host is specified a system proxy is created.
        addHTTPPropertyExpectations("foobar", "", "", "", "");

        SystemProxyManager manager = new SystemProxyManager(
                ProxyFactory.getDefaultInstance(),
                systemPropertiesMock);
        Proxy proxy = manager.getSystemProxy();
        checkProxy(proxy, "foobar", 80, null, null);
    }

    /**
     * Ensure that when all proxy related properties except the excludes list
     * are specified that the system proxy is configured properly.
     */
    public void testAllExceptExcludesList() throws Exception {

        // Ensure that when the host is specified a system proxy is created.
        addHTTPPropertyExpectations("foobar", "99", "me", "mine", "");

        SystemProxyManager manager = new SystemProxyManager(
                ProxyFactory.getDefaultInstance(),
                systemPropertiesMock);
        Proxy proxy;
        proxy = manager.getSystemProxy();
        checkProxy(proxy, "foobar", 99, "me", "mine");
    }

    /**
     * Ensure that when all proxy related properties are specified that the
     * system proxy and its manager are configured properly.
     */
    public void testAll() throws Exception {

        // Ensure that when the host is specified a system proxy is created.
        addHTTPPropertyExpectations("foobar", "99", "me", "mine",
                "*.volantis.com|www.google.com");

        SystemProxyManager manager = new SystemProxyManager(
                ProxyFactory.getDefaultInstance(),
                systemPropertiesMock);
        Proxy systemProxy = manager.getSystemProxy();
        checkProxy(systemProxy, "foobar", 99, "me", "mine");

        assertNull(manager.getProxyForHost("www.volantis.com"));
        assertNull(manager.getProxyForHost("foo.volantis.com"));
        assertNull(manager.getProxyForHost("www.google.com"));
        assertSame(systemProxy, manager.getProxyForHost("www.google.com2"));
    }

    private SystemProxyManager createManagerWithExcludeList(
            String excludeList) {

        addHTTPPropertyExpectations("foobar", "99", "me", "mine",
                excludeList);
        return new SystemProxyManager(ProxyFactory.getDefaultInstance(),
                systemPropertiesMock);
    }

    public void testSimpleExclude() throws Exception {
        SystemProxyManager manager;

        manager = createManagerWithExcludeList("localhost");

        assertNotNull("No match", manager.getProxyForHost("not localhost"));
        assertNull("Match", manager.getProxyForHost("localhost"));

        manager = createManagerWithExcludeList("localhost.com");

        assertNotNull("No match", manager.getProxyForHost("not localhost.com"));
        assertNull("Match", manager.getProxyForHost("localhost.com"));
    }

    /**
     * Ensure wildcard macthing works
     *
     * @throws Exception
     */
    public void testWildcardExclude() throws Exception {
        SystemProxyManager manager;

        manager = createManagerWithExcludeList("*.localhost");

        assertNull("Match", manager.getProxyForHost("a.localhost"));
        assertNull("Match", manager.getProxyForHost("2785.localhost"));
        assertNotNull("No Match (due to training space",
                manager.getProxyForHost("2785.localhost "));

        manager = createManagerWithExcludeList("abc.*.localhost");

        assertNull("Match", manager.getProxyForHost("abc.not.localhost"));
        assertNull("Match", manager.getProxyForHost("abc.*.localhost"));
        assertNull("Match", manager.getProxyForHost("abc..localhost"));
    }

    /**
     * Ensure OR matching works
     *
     * @throws Exception
     */
    public void testORExclude() throws Exception {
        SystemProxyManager manager;

        manager = createManagerWithExcludeList("www.google.com|localhost");

        assertNull("Match", manager.getProxyForHost("localhost"));
        assertNull("Match", manager.getProxyForHost("www.google.com"));
        assertNotNull("No Match (due to training space",
                manager.getProxyForHost("localhost "));

        manager = createManagerWithExcludeList("abc.*.localhost|*.google.com");

        assertNull("Match", manager.getProxyForHost("abc.not.localhost"));
        assertNull("Match", manager.getProxyForHost("abc.*.localhost"));
        assertNull("Match", manager.getProxyForHost("abc..localhost"));
        assertNull("Match", manager.getProxyForHost("mail.google.com"));
        assertNull("Match", manager.getProxyForHost(".google.com"));
    }
}
