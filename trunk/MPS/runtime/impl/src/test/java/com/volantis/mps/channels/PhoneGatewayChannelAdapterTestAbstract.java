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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.channels;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.net.FakeSocketFactory;

import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;

/**
 * This tests the PhoneGatewayChannelAdapter and provides a basis for sub
 * class implementation tests.
 * 
 * @todo: Later Include MessageChannel in this test hierarchy and provide tests
 * @todo:       for SMS, SMTP, and MMS channels.
 */
public abstract class PhoneGatewayChannelAdapterTestAbstract
        extends TestCaseAbstract {
    /**
     * A URL for the server for the tests that works with the
     * FakeSocketFactory.  Test instances can provide their own host hence
     * the {@link #getDefaultHost} method BUT there is no guarantee the
     * tests using the socket factory will work.
     */
    protected static final String DEFAULT_HOST = "http://localhost/";

    /**
     * Initialise an instance of this test case.
     */
    public PhoneGatewayChannelAdapterTestAbstract() {
    }

    /**
     * Initialise a named instance of this test case.
     *
     * @param s The name of this test case.
     */
    public PhoneGatewayChannelAdapterTestAbstract(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        // Fake sockets to allow the tests to provide the response they want
        // from any external socket connection (which is intercepted).
        //FakeSocketFactory.registerForClientSockets();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        // todo Later Once FakeSocketFactory has an unregister/disable operation ensure it is used here
    }

    /**
     * Test the construction of parameter strings from key-value pairs.
     */
    public void testConstructParamString() throws Exception {
        PhoneGatewayChannelAdapter channel = getTestInstance();

        // Test data
        final String key1 = "key1";
        final String value1 = "value one";
        final String key2 = "key2";
        final String value2 = "value two";

        // Create pair arrays of key-value combinations
        String[] pairOne = new String[] {key1, value1};
        String[] pairTwo = new String[] {key2, value2};

        List parameters = new ArrayList();
        parameters.add(pairOne);
        parameters.add(pairTwo);

        // Test an unencoded string
        String expected = key1 + "=" + value1 + "&" + key2 + "=" + value2;
        String params = channel.constructParamString(parameters, false);
        assertNotNull("There should be a parameter string (1)", params);
        assertEquals("Parameters should match expected (1)", expected, params);

        // Test an encoded string
        expected = key1 + "=" + URLEncoder.encode(value1) +
                "&" +
                key2 + "=" + URLEncoder.encode(value2);
        params = channel.constructParamString(parameters, true);
        assertNotNull("There should be a parameter string (2)", params);
        assertEquals("Parameters should match expected (2)", expected, params);
    }

    /**
     * Test the formatting of a MSISDN number.
     */
    public void testformatMSISDN() throws Exception {
        PhoneGatewayChannelAdapter channel = getTestInstance();

        // Test data
        final String code = getDefaultCountryCode();
        final String encodedCode = URLEncoder.encode(code);

        final String baseNumber = "123456789";
        final String number = "0" + baseNumber;
        final String internationalNumber = code + baseNumber;

        final String expectedType = "/TYPE=PLMN";

        String expected;
        String actual;

        // Require an international prefix to be added, no type, encoded
        expected = encodedCode + baseNumber;
        actual = channel.formatMSISDN(number, false, true);
        assertEquals("Formatted number should match actual (1)",
                     actual,
                     expected);

        // No prefix required, no type, encoded
        actual = channel.formatMSISDN(internationalNumber, false, true);
        assertEquals("Formatted number should match actual (2)",
                     actual,
                     expected);

        // Require an international prefix to be added, no type, not encoded
        expected = code + baseNumber;
        actual = channel.formatMSISDN(number, false, false);
        assertEquals("Formatted number should match actual (3)",
                     actual,
                     expected);

        // No prefix required, no type, not encoded
        actual = channel.formatMSISDN(internationalNumber, false, false);
        assertEquals("Formatted number should match actual (4)",
                     actual,
                     expected);

        // Require an international prefix to be added, type added, encoded
        expected = encodedCode + baseNumber + expectedType;
        actual = channel.formatMSISDN(number, true, true);
        assertEquals("Formatted number should match actual (5)",
                     actual,
                     expected);

        // No prefix required, type added, encoded
        actual = channel.formatMSISDN(internationalNumber, true, true);
        assertEquals("Formatted number should match actual (6)",
                     actual,
                     expected);

        // Require an international prefix to be added, type added, not encoded
        expected = code + baseNumber + expectedType;
        actual = channel.formatMSISDN(number, true, false);
        assertEquals("Formatted number should match actual (7)",
                     actual,
                     expected);

        // No prefix required, type added, not encoded
        actual = channel.formatMSISDN(internationalNumber, true, false);
        assertEquals("Formatted number should match actual (8)",
                     actual,
                     expected);
    }

    /**
     * Test the sending of a GET request to the server specified by
     * {@link #getDefaultHost}.
     *
     * @todo This test fails with Java 1.4.2_13 and Java 1.4.2_14, but works OK
     * with Java 1.4.2_10. The stack trace with Java 1.4.2_13 is
     * java.lang.reflect.UndeclaredThrowableException: java.net.ConnectException: Connection refused
     *     at java.net.PlainSocketImpl.socketConnect(Native Method)
     *     at java.net.PlainSocketImpl.doConnect(PlainSocketImpl.java:305)
     *     at java.net.PlainSocketImpl.connectToAddress(PlainSocketImpl.java:171)
     *     at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:158)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
     *     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
     *     at java.lang.reflect.Method.invoke(Method.java:324)
     *     at junitx.util.PrivateAccessor.invoke(PrivateAccessor.java:253)
     *     at com.volantis.synergetics.testtools.net.SocketImplWrapper.connect(SocketImplWrapper.java:118)
     *     at java.net.Socket.connect(Socket.java:464)
     *     at java.net.Socket.connect(Socket.java:414)
     *     at java.net.Socket.<init>(Socket.java:310)
     *     at java.net.Socket.<init>(Socket.java:125)
     *     at our.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory.createSocket(DefaultProtocolSocketFactory.java:86)
     *     at our.apache.commons.httpclient.HttpConnection$1.doit(HttpConnection.java:660)
     *     at our.apache.commons.httpclient.HttpConnection$SocketTask.run(HttpConnection.java:1291)
     *     at java.lang.Thread.run(Thread.java:534)
     *
     * The test is commented out until we identify and fix the problem.
     */
    public void no_testSendAsGetRequest() throws Exception {
        // Test data
        String paramString = "somekey=somevalue";

        // Test a valid send
        String[] content = new String[] {
            "HTTP/1.1 200 OK"
        };
        FakeSocketFactory.putResponse(content, 80);
        PhoneGatewayChannelAdapter channel = getTestInstance();
        boolean success = channel.sendAsGetRequest(paramString);
        FakeSocketFactory.release(80);
        assertTrue("Send should have succeeded", success);

        // Test a failed send
        content = new String[]{
            "HTTP/1.1 500 Internal Server Error"
        };
        FakeSocketFactory.putResponse(content, 80);
        success = channel.sendAsGetRequest(paramString);
        FakeSocketFactory.release(80);
        assertFalse("Send should not have succeeded", success);
        FakeSocketFactory.release(80);
    }

    /**
     * Provide a testable instance of a <code>PhoneGatewayChannelAdapter</code>
     * for use in the tests at this level of the test hierarchy.  This should
     * almost certainly use the
     *
     * @return An initialised instance of a
     *        <code>PhoneGatewayChannelAdapter</code>.
     */
    protected abstract PhoneGatewayChannelAdapter getTestInstance()
            throws Exception;

    /**
     * Return the host that the channel is initialised with to allow for
     * testing of any functionality that requires knowledge of the host.
     * There is a {@link #DEFAULT_HOST} that can be returned by this method,
     * and for the tests in this class may be required...
     * Returning null will cause problems in the test cases.
     *
     * @return A string containing the host URL as would be specified in
     *         the channel config for MPS.
     */
    protected abstract String getDefaultHost();

    /**
     * Return the country code that the channel is to be initialised with to
     * allow for testing of any functionality that requires knowledge of the
     * country code.  Returning null will cause problems in the test cases.
     *
     * @return A string containing the default country code in use for the
     *         channel under test that includes the +
     */
    protected abstract String getDefaultCountryCode();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Aug-04	149/3	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 ===========================================================================
*/
