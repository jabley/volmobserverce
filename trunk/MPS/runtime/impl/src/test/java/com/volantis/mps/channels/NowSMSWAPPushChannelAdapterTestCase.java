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

import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.message.MultiChannelMessageImpl;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.RecipientException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * This tests the NowSMSWAPPushChannelAdapter using the superclass tests in
 * <code>PhoneGatewayChannelAdapterTestAbstract</code> and also specific tests
 * in this class.
 */
public class NowSMSWAPPushChannelAdapterTestCase
        extends PhoneGatewayChannelAdapterTestAbstract {
    /**
     * The channel name used for all of the tests for this channel.
     */
    protected static final String CHANNEL_NAME = "WAPPush";

    /**
     * Initialise a new instance of this test case.
     */
    public NowSMSWAPPushChannelAdapterTestCase () {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public NowSMSWAPPushChannelAdapterTestCase ( String s ) {
        super ( s );
    }

    // JavaDoc inherited
    protected void setUp () throws Exception {
        super.setUp ();
    }

    // JavaDoc inherited
    protected void tearDown () throws Exception {
        super.tearDown ();
    }

    /**
     * Testing the send
     */
    public void testSendImpl () throws Exception {
        // Create a customised test instance so that this method can be
        // tested without having to have a real server on the other end!
        NowSMSWAPPushChannelAdapter channel =
                new NowSMSWAPPushChannelAdapter ( CHANNEL_NAME,
                        createChannelInfo () ) {
                    protected boolean sendAsGetRequest ( String paramString )
                            throws IOException, MalformedURLException {
                        // Return false for some message sends...
                        return !( paramString.startsWith ( "PhoneNumber=%2B44failure" ) );
                    }
                };

        // Create a test message
        MultiChannelMessage multiChannelMessage = new MultiChannelMessageImpl();
        // Unless overriding MultiChannelMessage#generateTargetMessageAsURL
        // then just use a URL based message for these tests otherwise it
        // will try and initialise a servlet and get very upset with life!
        multiChannelMessage.setMessageURL ( new URL ( "http://test.message/file" ) );
        multiChannelMessage.setSubject ( "Test Message" );

        // Create some recipients
        MessageRecipients messageRecipients = createTestRecipients ();

        // Create a sender
        MessageRecipient messageSender =
                new MessageRecipient ( new InternetAddress ( "mps@volantis.com" ),
                        "Master" );

        // And now send that message to those recipients from that sender! :-)
        MessageRecipients failures = channel.sendImpl ( multiChannelMessage,
                messageRecipients,
                messageSender );
        assertNotNull ( "Should be a failure list, even if it is empty",
                failures );
        assertTrue ( "Expected some failures",
                failures.getIterator ().hasNext () );

        // Can't access recipients by index (annoyingly) so iterate over the
        // one expected failure here (Note: a mismatched channel is not a
        // failure, as it is expected that the message will be sent to the
        // other channel during the same batch send in a running MPS).
        for ( Iterator i = failures.getIterator (); i.hasNext (); ) {
            MessageRecipient recipient = ( MessageRecipient ) i.next ();
            System.out.println ( "Failure:\n" );
            assertEquals ( "Failed recipient MSISDN should match",
                    "failure",
                    recipient.getMSISDN () );
            assertEquals ( "Failed recipient channel name should match",
                    CHANNEL_NAME,
                    recipient.getChannelName () );
        }
    }

    /**
     * A utility method that creates some sample recipients of messages to
     * enable testing of sending messages in this channel.
     *
     * @return A list of recipients.
     *
     * @throws RecipientException If there is a problem creating a recipient or
     *                            adding one to the list of recipients.
     * @throws AddressException   If there is a problem creating the internet
     *                            address of a recipient.
     */
    private MessageRecipients createTestRecipients ()
            throws RecipientException, AddressException {
        // Create a list
        MessageRecipients messageRecipients = new MessageRecipients ();

        // Create a recipient
        MessageRecipient recipient = new MessageRecipient ();
        recipient.setMSISDN ( "0123456789" );
        recipient.setAddress ( new InternetAddress ( "test@volantis.com" ) );
        recipient.setChannelName ( CHANNEL_NAME );
        recipient.setDeviceName ( "Master" );
        messageRecipients.addRecipient ( recipient );

        // Create a recipient
        recipient = new MessageRecipient ();
        recipient.setMSISDN ( "+44123456789" );
        recipient.setAddress ( new InternetAddress ( "me@volantis.com" ) );
        recipient.setChannelName ( CHANNEL_NAME );
        recipient.setDeviceName ( "Master" );
        messageRecipients.addRecipient ( recipient );

        // Create a recipient
        recipient = new MessageRecipient ();
        recipient.setMSISDN ( "failure" );
        recipient.setAddress ( new InternetAddress ( "you@volantis.com" ) );
        recipient.setChannelName ( CHANNEL_NAME );
        recipient.setDeviceName ( "Master" );
        messageRecipients.addRecipient ( recipient );

        // Create a recipient - intentionally for a different channel
        recipient = new MessageRecipient ();
        recipient.setMSISDN ( "1234567890" );
        recipient.setAddress ( new InternetAddress ( "them@volantis.com" ) );
        recipient.setChannelName ( "SMTP" );
        recipient.setDeviceName ( "Master" );
        messageRecipients.addRecipient ( recipient );

        // Return the list
        return messageRecipients;
    }

    /**
     * Testing the creation of a test message
     */
    public void testCreateMessage () throws Exception {
        NowSMSWAPPushChannelAdapter channel =
                ( NowSMSWAPPushChannelAdapter ) getTestInstance ();

        // Create a test MultiChannelMessage to return a URL without
        // trying to use a servlet or ...
        MultiChannelMessage multiChannelMessage = new MultiChannelMessageImpl () {
            public URL generateTargetMessageAsURL ( String deviceName,
                                                    String mssUrl )
                    throws MessageException {
                URL url = null;
                try {
                    url = new URL ( "http://host.domain/servlet/id" );
                } catch ( MalformedURLException mue ) {
                    // Shouldn't happen in the test!
                }
                return url;
            }
        };

        // Expected result based on the MultiChannelMessage above
        String expected = "host.domain/servlet/id";

        // Create a message
        String messageOne =
                channel.createMessage ( multiChannelMessage, "Device One" );
        // Check it is as expected
        assertNotNull ( "Message should exist (1)", messageOne );
        assertEquals ( "Message should match expected (1)",
                messageOne,
                expected );

        // Create another message
        String messageTwo =
                channel.createMessage ( multiChannelMessage, "Device Two" );
        // Check it is as expected
        assertNotNull ( "Message should exist (2)", messageTwo );
        assertEquals ( "Message should match expected (2)",
                messageTwo,
                expected );

        // Now check the two retrieved messages are the same in content; the
        // test class above does not differentiate on device name
        assertEquals ( "Retrieved messages should be the same",
                messageOne,
                messageTwo );
    }

    /**
     * Testing the construction of various parameter strings
     */
    public void testConstructParameters () throws Exception {
        NowSMSWAPPushChannelAdapter channel =
                ( NowSMSWAPPushChannelAdapter ) getTestInstance ();

        // Test data
        String msisdn = "+441483739739";
        String messageLink = "www.volantis.com/wappush";
        String subject = "Test WAP Push";

        // Test null msisdn
        try {
            channel.constructParameters ( null, messageLink, subject );
            fail ( "Previous call should have caused an exception (1)" );
        } catch ( MessageException me ) {
            // Test success
        }
        // Test null messagelink
        try {
            channel.constructParameters ( msisdn, null, subject );
            fail ( "Previous call should have caused an exception (2)" );
        } catch ( MessageException me ) {
            // Test success
        }

        // Test valid with subject
        String paramString =
                channel.constructParameters ( msisdn, messageLink, subject );
        String expected =
                NowSMSWAPPushChannelAdapter.DESTINATION + "=" + msisdn + "&" +
                NowSMSWAPPushChannelAdapter.MESSAGE + "=" + messageLink + "&" +
                NowSMSWAPPushChannelAdapter.SUBJECT + "=" +
                URLEncoder.encode ( subject );
        assertEquals ( "Parameter strings should match (1)",
                expected,
                paramString );

        // test valid without subject
        paramString =
                channel.constructParameters ( msisdn, messageLink, null );
        expected =
                NowSMSWAPPushChannelAdapter.DESTINATION + "=" + msisdn + "&" +
                NowSMSWAPPushChannelAdapter.MESSAGE + "=" + messageLink + "&" +
                NowSMSWAPPushChannelAdapter.NO_SUBJECT_KEY + "=" +
                NowSMSWAPPushChannelAdapter.NO_SUBJECT_VALUE;
        assertEquals ( "Parameter strings should match (2)",
                expected,
                paramString );
    }

    /**
     * Testing removing the protocol part of a URL in string form.
     */
    public void testRemoveProtocol () throws Exception {
        NowSMSWAPPushChannelAdapter channel =
                ( NowSMSWAPPushChannelAdapter ) getTestInstance ();
        // Test standard http
        URL url = new URL ( "http://www.volantis.com" );
        String expected = "www.volantis.com";
        String removed = channel.removeProtocol ( url );
        assertNotNull ( "Should have a valid removed string (1)", removed );
        assertFalse ( "Should not match original URL (1)",
                url.toExternalForm ().equals ( removed ) );
        assertEquals ( "Should match expected (1)", expected, removed );

        // Test ftp
        url = new URL ( "ftp://www.volantis.com" );
        removed = channel.removeProtocol ( url );
        assertNotNull ( "Should have a valid removed string (2)", removed );
        assertFalse ( "Should not match original URL (2)",
                url.toExternalForm ().equals ( removed ) );
        assertEquals ( "Should match expected (2)", expected, removed );

        // Test no protocol (i.e. no http:// or ftp:// or ...) is not needed.
        // It tests a situation that should not occur as the URL object
        // requires a valid protocol and if not it throws an exception.

        // Test null URL
        url = null;
        try {
            removed = channel.removeProtocol ( url );
            fail ( "Should have had an exception thrown by the previous line" );
        } catch ( MessageException me ) {
            // Test success!
        }

        // Test with added path/file.xml
        url = new URL ( "http://www.volantis.com/path/to/some/file.xml" );
        expected = "www.volantis.com/path/to/some/file.xml";
        removed = channel.removeProtocol ( url );
        assertNotNull ( "Should have a valid removed string (3)", removed );
        assertFalse ( "Should not match original URL (3)",
                url.toExternalForm ().equals ( removed ) );
        assertEquals ( "Should match expected (3)", expected, removed );
    }

    /**
     * Create a testable channel info for use in creating test instances of
     * this channel.
     *
     * @return An initialised channel info map containing {@link
     *         PhoneGatewayChannelAdapter#URL} and {@link PhoneGatewayChannelAdapter#DEFAULT_COUNTRY_CODE}.
     */
    private Map createChannelInfo () {
        Map channelInfo = new HashMap ();
        channelInfo.put ( NowSMSWAPPushChannelAdapter.URL,
                getDefaultHost () );
        channelInfo.put ( NowSMSWAPPushChannelAdapter.DEFAULT_COUNTRY_CODE,
                getDefaultCountryCode () );
        channelInfo.put ( NowSMSWAPPushChannelAdapter.MSS_URL,
                getDefaultMSSUrl () );
        return channelInfo;
    }

    // JavaDoc inherited
    protected PhoneGatewayChannelAdapter getTestInstance () throws Exception {
        return new NowSMSWAPPushChannelAdapter ( CHANNEL_NAME,
                createChannelInfo () );
    }

    // JavaDoc inherited
    protected String getDefaultHost () {
        return DEFAULT_HOST;
    }

    // JavaDoc inherited
    protected String getDefaultCountryCode () {
        return "+44";
    }

    /**
     * Returnds the default URL for the MSS servlet
     *
     * @return
     */
    protected String getDefaultMSSUrl () {
        return "http://myserver:8080/mss";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	614/1	amoore	VBM:2005042509 Refactored MessageChannel to implements redundant recipient processing in subclasses

 29-Apr-05	624/1	amoore	VBM:2005042509 Refactored MessageChannel to include redundant recipient processing in subclasses

 21-Mar-05	435/1	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 21-Mar-05	426/3	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 21-Mar-05	426/1	ianw	VBM:2005031506 Allow MSS to be installed without MPS

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 11-Aug-04	149/3	claire	VBM:2004073005 WAP Push for MPS: New channel adapter, generating messages as URLs, config update

 ===========================================================================
*/
