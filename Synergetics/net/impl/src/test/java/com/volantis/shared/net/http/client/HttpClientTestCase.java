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

package com.volantis.shared.net.http.client;

import com.volantis.shared.net.http.HttpServerMock;
import com.volantis.shared.net.http.WaitTransaction;
import com.volantis.shared.net.http.HttpStatusCode;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.HttpMethodBase;
import our.apache.commons.httpclient.methods.GetMethod;
import our.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * Tests for {@link HttpClient}.
 */
public class HttpClientTestCase
        extends TestCaseAbstract {

    private HttpServerMock serverMock;

    protected void setUp() throws Exception {
        super.setUp();

        serverMock = new HttpServerMock();
    }

    /**
     * Ensure that a normal get works.
     */
    public void testGet()
            throws Exception {

        doMethodTest(new GetMethod(), new String[]{
            "GET / HTTP/1.1",
            "User-Agent: Jakarta Commons-HttpClient/2.0.2",
            "Host: " + serverMock.getServerAddress(),
        });
    }

    /**
     * Ensure that a normal post works.
     */
    public void testPost()
            throws Exception {

        doMethodTest(new PostMethod(), new String[]{
            "POST / HTTP/1.1",
            "User-Agent: Jakarta Commons-HttpClient/2.0.2",
            "Host: " + serverMock.getServerAddress(),
            "Content-Length: 0",
        });
    }

    /**
     * Do a normal method test.
     *
     * @param method         The method to execute.
     * @param requestContent The expected request content.
     * @throws IOException If there was a problem.
     */
    private void doMethodTest(
            final HttpMethodBase method, final String[] requestContent)
            throws IOException {

        String[] responseContent = new String[]{
            "HTTP/1.0 200 OK",
            "Date: Fri, 31 Dec 1999 23:59:59 GMT",
            "Content-Type: text/plain",
            "",
            "<p>A Get</p>"
        };

        serverMock.addTransaction(requestContent, responseContent);

        HttpClient client = createClient(10, 15);

        initialiseMethod(method);

        HttpStatusCode statusCode = client.executeMethod(method);
        assertEquals(HttpStatusCode.OK, statusCode);
    }

    /**
     * Ensure that the round trip timeout works.
     */
    public void testRoundTripTimeout()
            throws Exception {

        serverMock.addTransaction(new WaitTransaction(2000));

        long start = System.currentTimeMillis();

        HttpClient client = createClient(10, 1);
        HttpMethodBase method = createGetMethod();
        try {
            HttpStatusCode statusCode = client.executeMethod(method);
            assertEquals(HttpStatusCode.OK, statusCode);
        } catch (InterruptedIOException expected) {
            long end = System.currentTimeMillis();

            long period = end - start;
            assertTrue("Period " + period + " should be >= 1000",
                    period >= 1000);
        }
    }

    /**
     * Create a HTTPClient with the specified timeouts.
     *
     * @param connectionTimeout The connection timeout.
     * @param roundTripTimeout  The round trip timeout.
     * @return The client.
     */
    private HttpClient createClient(
            final int connectionTimeout, final int roundTripTimeout) {
        HttpClientFactory factory = HttpClientFactory.getDefaultInstance();
        HttpClientBuilder builder = factory.createClientBuilder();
        builder.setConnectionTimeout(
                Period.inSeconds(connectionTimeout));
        builder.setRoundTripTimeout(
                Period.inSeconds(roundTripTimeout));
        HttpClient client = builder.buildHttpClient();
        return client;
    }

    /**
     * Create an initialised get method.
     *
     * @return The method.
     */
    private HttpMethodBase createGetMethod() {
        HttpMethodBase method = new GetMethod();
        initialiseMethod(method);
        return method;
    }

    /**
     * Initialise the method so that it will connect to the mock server.
     *
     * @param method The method to initialise.
     */
    private void initialiseMethod(HttpMethodBase method) {
        HostConfiguration configuration = new HostConfiguration();
        configuration.setHost("localhost", serverMock.getServerPort(), "http");
        method.setHostConfiguration(configuration);
    }
}
