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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/recipient/MessageRecipientTestCase.java,v 1.4 2003/03/20 10:15:37 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-01-03    ianw             VBM:2002111211 - Created
 * 24-Feb-03    Mat             VBM:2003022002 - Changed to use BeanInitialiser
 *                              to create the bean.
 * 17-Mar-03    Geoff           VBM:2003031403 - Removed unused/dead imports.
 * 19-Mar-03    Geoff           VBM:2003032001 - Refactored to use external
 *                              ConfigValues.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

import javax.mail.internet.InternetAddress;

import com.volantis.mcs.runtime.Volantis;
import com.volantis.testtools.stubs.ServletContextStub;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * Test the <code>MessageRecipient</code> class.
 */
public class MessageRecipientTestCase extends TestCaseAbstract {

    protected Volantis volantisBean = null;
    protected ServletContextStub servletContext;

    public MessageRecipientTestCase(java.lang.String testName) {
        super(testName);
    }

    // JavaDoc inherited
    public void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test constructing {@link MessageRecipient} instances
     */ 
    public void testContructors() throws Exception {
        MessageRecipient messageRecipient1 = new MessageRecipient();

        InternetAddress internetAddress =
                new InternetAddress("ianw@volantis.com");

        String deviceName = "PC";

        MessageRecipient messageRecipient2 =
                new MessageRecipient(internetAddress, deviceName);

        if (messageRecipient1.getAddress() != null) {
            fail("Empty MessageRecipient getAddress not null");
        }
        if (messageRecipient1.getDeviceName() != null) {
            fail("Empty MessageRecipient getDeviceName not null");
        }
        if (messageRecipient2.getAddress() != internetAddress) {
            fail("Non empty MessageRecipient getAddress not equal");
        }
        if (!messageRecipient2.getDeviceName().equals(deviceName)) {
            fail("Non empty MessageRecipient getDeviceName not equal");
        }
    }

    /**
     * Test of setAddress method from {@link MessageRecipient}
     */
    public void testSetAddress() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        InternetAddress internetAddress =
                new InternetAddress("ianw@volantis.com");

        messageRecipient.setAddress(internetAddress);

        if (messageRecipient.getAddress() != internetAddress) {
            fail("Address not equal");
        }
    }

    /**
     * Test of getAddress method from {@link MessageRecipient}
     */
    public void testGetAddress() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        InternetAddress internetAddress =
                new InternetAddress("ianw@volantis.com");

        messageRecipient.setAddress(internetAddress);

        if (!messageRecipient.getAddress().equals(internetAddress)) {
            fail("Address not equal");
        }
    }

    /**
     * Test of setMSISDN method from MessageRecipient}
     */
    public void testSetMSISDN() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        String msISDN = "12345678";
        messageRecipient.setMSISDN(msISDN);

        if (!messageRecipient.getMSISDN().equals(msISDN)) {
            fail("MSISDN not equal");
        }
    }

    /**
     * Test of setDeviceName method from {@link MessageRecipient}
     */
    public void testSetDeviceName() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        String deviceName = "PC";
        messageRecipient.setDeviceName(deviceName);

        if (!messageRecipient.getDeviceName().equals(deviceName)) {
            fail("DeviceName not equal");
        }
    }

    /**
     * Test of setChannelName method from {@link MessageRecipient}
     */
    public void testSetChannelName() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        String channelName = "SMTP";
        messageRecipient.setChannelName(channelName);

        if (!messageRecipient.getChannelName().equals(channelName)) {
            fail("ChannelName not equal");
        }
    }

    /**
     * Test of setRecipientType method from {@link MessageRecipient}
     */
    public void testSetRecipientType() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        int recipientType = MessageRecipient.NOT_RESOLVED;

        messageRecipient.setRecipientType(recipientType);

        assertEquals("Recipient type should be NOT_RESOLVED",
                     messageRecipient.getRecipientType(),
                     MessageRecipient.NOT_RESOLVED);

        recipientType = MessageRecipient.OK;

        messageRecipient.setRecipientType(recipientType);

        assertEquals("Recipient type should be OK",
                     messageRecipient.getRecipientType(),
                     MessageRecipient.OK);
    }

    /**
     * Test of resolveDeviceName method from {@link MessageRecipient}
     */
    public void testResolveDeviceName() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        // This is necessary to remove the need to create the entire Volantis
        // bean _just_ to get two Strings out...
        PrivateAccessor.setField(messageRecipient,
                                 "messageRecipientInfo",
                                 new MessageRecipientInfoTestHelper());

        messageRecipient.resolveDeviceName(false);

        assertEquals("Resolved device not equal to default",
                     "Outlook",
                     messageRecipient.getDeviceName());

        messageRecipient.setDeviceName("aardvark");
        messageRecipient.resolveDeviceName(false);

        assertEquals("Resolved device overrode current value",
                     "aardvark",
                     messageRecipient.getDeviceName());

        messageRecipient.resolveDeviceName(true);

        assertEquals("Resolved device did not overide current value",
                     "Outlook",
                     messageRecipient.getDeviceName());
    }

    /**
     * Test of resolveChannelName method from {@link MessageRecipient}
     */
    public void testResolveChannelName() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        // This is necessary to remove the need to create the entire Volantis
        // bean _just_ to get two Strings out...
        PrivateAccessor.setField(messageRecipient,
                                 "messageRecipientInfo",
                                 new MessageRecipientInfoTestHelper());

        messageRecipient.resolveChannelName(false);

        assertEquals("Resolved channel not equal to default",
                     "smtp",
                     messageRecipient.getChannelName());

        messageRecipient.setChannelName("aardvark");
        messageRecipient.resolveChannelName(false);

        assertEquals("Resolved channel overrode current value",
                     "aardvark",
                     messageRecipient.getChannelName());

        messageRecipient.resolveChannelName(true);

        assertEquals("Resolved channel did not override current value",
                     "smtp",
                     messageRecipient.getChannelName());
    }

    /**
     * Test of clone method from {@link MessageRecipient}
     */
    public void testClone() throws Exception {
        MessageRecipient messageRecipient = new MessageRecipient();

        InternetAddress internetAddressOrig =
                new InternetAddress("ianw@volantis.com");

        InternetAddress internetAddressNew =
                new InternetAddress("mat@volantis.com");

        String channelNameOrig = "smtp";
        String channelNameNew = "smsc";
        String deviceNameOrig = "Outlook";
        String deviceNameNew = "SMS Hamdset";
        String msISDNOrig = "12345678";
        String msISDNNew = "87654321";

        messageRecipient.setAddress(internetAddressOrig);
        messageRecipient.setChannelName(channelNameOrig);
        messageRecipient.setDeviceName(deviceNameOrig);
        messageRecipient.setMSISDN(msISDNOrig);

        MessageRecipient clone = (MessageRecipient) messageRecipient.clone();

        assertEquals("Cloned address not equal",
                     messageRecipient.getAddress(),
                     clone.getAddress());

        assertEquals("Cloned channel name not equal",
                     messageRecipient.getChannelName(),
                     clone.getChannelName());

        assertEquals("Cloned device name not equal",
                     messageRecipient.getDeviceName(),
                     clone.getDeviceName());

        assertEquals("Cloned MSISDN not equal",
                     messageRecipient.getMSISDN(),
                     clone.getMSISDN());

        // Modify original - ensure that we have done a deep copy of fields.
        messageRecipient.setAddress(internetAddressNew);
        messageRecipient.setChannelName(channelNameNew);
        messageRecipient.setDeviceName(deviceNameNew);
        messageRecipient.setMSISDN(msISDNNew);

        assertFalse("Cloned address equal",
                    messageRecipient.getAddress().equals(
                            clone.getAddress()));

        assertFalse("Cloned channel name equal",
                    messageRecipient.getChannelName().equals(
                            clone.getChannelName()));

        assertFalse("Cloned device name equal",
                    messageRecipient.getDeviceName().equals(
                            clone.getDeviceName()));

        assertFalse("Cloned MSISDN equal",
                    messageRecipient.getMSISDN().equals(
                            clone.getDeviceName()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 24-Sep-04	140/3	claire	VBM:2004070704 Fixing testcases and replacing some hypersonic usage with stubs

 22-Sep-04	140/1	claire	VBM:2004070704 Tidying up Transforce use in the MimeMessageAssembler and fixing testcases

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 27-Oct-03	45/12	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 27-Oct-03	45/10	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 27-Oct-03	45/8	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 27-Oct-03	45/6	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 24-Oct-03	45/4	mat	VBM:2003101502 Rework tests to use AppManager properly

 23-Oct-03	45/2	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
