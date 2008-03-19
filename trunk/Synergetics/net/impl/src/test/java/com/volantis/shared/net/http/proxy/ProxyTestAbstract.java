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
import com.volantis.shared.net.proxy.Proxy;
import com.volantis.shared.system.SystemPropertiesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base for tests that deal with {@link Proxy}.
 */
public abstract class ProxyTestAbstract
        extends TestCaseAbstract {

    protected SystemPropertiesMock systemPropertiesMock;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        systemPropertiesMock = new SystemPropertiesMock("systemPropertiesMock",
                expectations);
    }

    /**
     * Check the proxy to make sure it has the expected state.
     * @param proxy The proxy to check.
     * @param expectedHost The expected host.
     * @param expectedPort The expected port.
     * @param expectedUser The expected user.
     * @param expectedPassword The expected password.
     */
    protected void checkProxy(
            Proxy proxy, final String expectedHost, final int expectedPort,
            final Object expectedUser,
            final Object expectedPassword) {
        assertNotNull(proxy);
        assertEquals(expectedHost, proxy.getHost());
        assertEquals(expectedPort, proxy.getPort());
        assertEquals(expectedUser, proxy.getUser());
        assertEquals(expectedPassword, proxy.getPassword());
    }

    /**
     * Add expectations for HTTP specific proxy properties.
     *
     * @param host         The host to return.
     * @param port         The port to return.
     * @param user         The user to return.
     * @param password     The password to return.
     * @param excludesList The excludes list to return.
     */
    protected void addHTTPPropertyExpectations(
            final String host, final String port, final String user,
            final String password,
            final String excludesList) {
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.HTTP_PROXY_HOST, "").returns(host);
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.HTTP_PROXY_PORT, "").returns(port);
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_USER, "").returns(user);
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_PASSWORD, "").returns(password);
        systemPropertiesMock.expects.getProperty(
                HTTPSystemPropertyKeys.PROXY_EXCLUDE, "").returns(excludesList);
    }
}
