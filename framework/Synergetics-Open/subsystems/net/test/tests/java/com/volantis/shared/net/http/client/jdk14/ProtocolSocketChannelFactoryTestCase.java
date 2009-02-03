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

package com.volantis.shared.net.http.client.jdk14;

import com.volantis.shared.net.impl.http.client.jdk14.ProtocolSocketChannelFactory;
import com.volantis.shared.net.http.HttpServerMock;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Test cases for {@link ProtocolSocketChannelFactory}.
 */
public class ProtocolSocketChannelFactoryTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that when creating a socket to an unknown host that a
     * {@link UnknownHostException} is thrown.
     */
    public void testConnectUnknownHost() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ProtocolSocketChannelFactory factory =
                new ProtocolSocketChannelFactory(Period.INDEFINITELY);

        String host = "-unknown-host-";
        try {
            factory.createSocket(host, 8080);
            fail("Did not detect unknown host");
        } catch (UnknownHostException expected) {
            assertEquals(host, expected.getMessage());
        }

        try {
            factory.createSocket(host, 8080, InetAddress.getLocalHost(), 0);
            fail("Did not detect unknown host");
        } catch (UnknownHostException expected) {
            assertEquals(host, expected.getMessage());
        }
    }

    /**
     * Ensure that when creating a socket to the local host that it works.
     */
    public void testConnectLocalHost() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        HttpServerMock serverMock = new HttpServerMock();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        ProtocolSocketChannelFactory factory =
                new ProtocolSocketChannelFactory(Period.INDEFINITELY);

        String host = "localhost";
        int port = serverMock.getServerPort();
        Socket socket = factory.createSocket(host, port);
        socket.close();
    }
}
