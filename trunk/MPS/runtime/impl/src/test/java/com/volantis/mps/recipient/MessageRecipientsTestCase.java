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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/recipient/MessageRecipientsTestCase.java,v 1.5 2003/03/20 10:15:37 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-01-03    ianw             VBM:2002111211 - Created
 * 25-Feb-03    Mat             VBM:2003022002 - Changed to use the
 *                              BeanInitialiser to start and configure the bean
 * 17-Mar-03    Geoff           VBM:2003031403 - Removed unused/dead imports.
 * 19-Mar-03    Geoff           VBM:2003032001 - Refactored to use external
 *                              ConfigValues.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

import java.util.Iterator;

import javax.mail.internet.InternetAddress;

import com.volantis.mcs.runtime.Volantis;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.ServletContextStub;
import junitx.util.PrivateAccessor;

/**
 * This tests the <code>MessageRecipients</code> class.
 */
public class MessageRecipientsTestCase extends TestCaseAbstract {

    protected Volantis volantisBean = null;
    protected ServletContextStub servletContext;

    public MessageRecipientsTestCase(java.lang.String testName) {
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
     * Test of addRecipient method from {@link MessageRecipients}
     */
    public void testAddRecipient() throws Exception {
        MessageRecipients messageRecipients = new MessageRecipients();

        String userPrefix = "bozo";
        String userSuffix = "@volantis.com";

        // Add entires in reverse order as they should be sorted
        for (int n = 9; n > 0; n--) {
            InternetAddress address =
                    new InternetAddress(userPrefix + n + userSuffix);

            MessageRecipient messageRecipient =
                    new MessageRecipient(address, null);
            messageRecipients.addRecipient(messageRecipient);
        }

        Iterator i = messageRecipients.getIterator();

        int tot = 0;
        while (i.hasNext()) {
            MessageRecipient messageRecipient = (MessageRecipient) i.next();

            InternetAddress testAddress =
                    new InternetAddress(
                            userPrefix + (tot + 1) + userSuffix);
            assertEquals("Wrong recipient ",
                         testAddress.getAddress(),
                         messageRecipient.getAddress().getAddress());
            tot++;
        }
        assertEquals("Expecting 10 recipients", tot, 9);
    }

    /**
     * Test of removeRecipient method from {@link MessageRecipients}
     */
    public void testRemoveRecipient() throws Exception {
        MessageRecipients messageRecipients = new MessageRecipients();

        String userPrefix = "bozo";
        String userSuffix = "@volantis.com";

        InternetAddress userToRemove =
                new InternetAddress("bozo5@volantis.com");

        // Add entires in reverse order as they should be sorted
        for (int n = 9; n > 0; n--) {
            InternetAddress address =
                    new InternetAddress(userPrefix + n + userSuffix);

            MessageRecipient messageRecipient =
                    new MessageRecipient(address, null);
            messageRecipients.addRecipient(messageRecipient);
        }

        messageRecipients.removeRecipient(
                new MessageRecipient(userToRemove, null));

        Iterator i = messageRecipients.getIterator();

        int tot = 0;
        while (i.hasNext()) {
            MessageRecipient messageRecipient = (MessageRecipient) i.next();

            InternetAddress testAddress =
                    new InternetAddress(
                            userPrefix + (tot + 1) + userSuffix);

            if ((tot + 1) == 5) {
                assertFalse("Message recipient "
                            + testAddress.getAddress()
                            + " should have been removed",
                            messageRecipient.getAddress().equals(
                                    testAddress));
                tot++;
                testAddress = new InternetAddress(
                        userPrefix + (tot + 1) + userSuffix);
            }
            assertEquals("Wrong recipient ",
                         testAddress.getAddress(),
                         messageRecipient.getAddress().getAddress());
            tot++;
        }
        assertEquals("Expecting 9 recipients", tot, 9);
    }

    /**
     * Test of resolveDeviceNames method from {@link MessageRecipients}
     */
    public void testResolveDeviceNames() throws Exception {
        MessageRecipients messageRecipients = new MessageRecipients();

        String userPrefix = "bozo";
        String userSuffix = "@volantis.com";
        String deviceName;

        // Add entires in reverse order as they should be sorted
        for (int n = 9; n > 0; n--) {
            InternetAddress address =
                    new InternetAddress(userPrefix + n + userSuffix);

            // Set all even numbers device to Ardvark
            if (n % 2 == 0) {
                deviceName = "Ardvark";
            } else {
                deviceName = null;
            }

            MessageRecipient messageRecipient =
                    new MessageRecipient(address, deviceName);

            // This is necessary to remove the need to create the entire
            // Volantis bean _just_ to get two Strings out...
            PrivateAccessor.setField(messageRecipient,
                                     "messageRecipientInfo",
                                     new MessageRecipientInfoTestHelper());

            messageRecipients.addRecipient(messageRecipient);
        }

        // Non destructive resolution.

        messageRecipients.resolveDeviceNames(false);
        Iterator i = messageRecipients.getIterator();

        int tot = 0;
        while (i.hasNext()) {
            MessageRecipient messageRecipient = (MessageRecipient) i.next();

            int n;
            // Ardvarks should be sorted first so entries 2,4,6 and 8
            // should be first, followed by 1,3,5,7 and 9
            if (tot < 4) {
                n = (tot + 1) * 2;
            } else {
                n = (tot - 4) * 2 + 1;
            }
            InternetAddress testAddress =
                    new InternetAddress(userPrefix + n + userSuffix);
            assertEquals("Wrong recipient ",
                         testAddress.getAddress(),
                         messageRecipient.getAddress().getAddress());
            if (n % 2 == 0) {
                assertEquals("Device name has been overidden",
                             "Ardvark",
                             messageRecipient.getDeviceName());
            } else {
                assertEquals("Device name has not been overidden",
                             "Outlook",
                             messageRecipient.getDeviceName());
            }
            tot++;
        }
        assertEquals("Expecting 9 recipients", tot, 9);

        // Destructive resolution.

        messageRecipients.resolveDeviceNames(true);
        i = messageRecipients.getIterator();

        tot = 0;
        while (i.hasNext()) {
            tot++;
            MessageRecipient messageRecipient = (MessageRecipient) i.next();

            InternetAddress testAddress =
                    new InternetAddress(userPrefix + tot + userSuffix);

            assertEquals("Wrong recipient ",
                         testAddress.getAddress(),
                         messageRecipient.getAddress().getAddress());

            assertEquals("Device name has not been overidden",
                         "Outlook",
                         messageRecipient.getDeviceName());
        }
        assertEquals("Expecting 9 recipients", tot, 9);
    }

    /**
     * Test of resolveChannelNames method from {@link MessageRecipients}
     */
    public void testResolveChannelNames() throws Exception {
        MessageRecipients messageRecipients = new MessageRecipients();

        String userPrefix = "bozo";
        String userSuffix = "@volantis.com";
        String channelName;

        // Add entires in reverse order as they should be sorted
        for (int n = 9; n > 0; n--) {
            InternetAddress address =
                    new InternetAddress(userPrefix + n + userSuffix);

            // Set all even numbers device to Ardvark
            if (n % 2 == 0) {
                channelName = "Ardvark";
            } else {
                channelName = null;
            }

            MessageRecipient messageRecipient =
                    new MessageRecipient(address, "Outlook");

            messageRecipient.setChannelName(channelName);

            // This is necessary to remove the need to create the entire
            // Volantis bean _just_ to get two Strings out...
            PrivateAccessor.setField(messageRecipient,
                                     "messageRecipientInfo",
                                     new MessageRecipientInfoTestHelper());

            messageRecipients.addRecipient(messageRecipient);
        }

        // Non destructive resolution.

        messageRecipients.resolveChannelNames(false);
        Iterator i = messageRecipients.getIterator();

        int tot = 0;
        while (i.hasNext()) {
            MessageRecipient messageRecipient = (MessageRecipient) i.next();

            int n;
            // Ardvarks should be sorted first so entries 2,4,6 and 8
            // should be first, followed by 1,3,5,7 and 9
            if (tot < 4) {
                n = (tot + 1) * 2;
            } else {
                n = (tot - 4) * 2 + 1;
            }
            InternetAddress testAddress =
                    new InternetAddress(userPrefix + n + userSuffix);

            assertEquals("Wrong recipient ",
                         testAddress.getAddress(),
                         messageRecipient.getAddress().getAddress());

            if (n % 2 == 0) {
                assertEquals("Channel name has been overriden",
                             "Ardvark",
                             messageRecipient.getChannelName());
            } else {
                assertEquals("Channel name has been overriden",
                             "smtp",
                             messageRecipient.getChannelName());
            }
            tot++;
        }
        assertEquals("Expecting 9 recipients", tot, 9);

        // Destructive resolution.

        messageRecipients.resolveChannelNames(true);
        i = messageRecipients.getIterator();

        tot = 0;
        while (i.hasNext()) {
            tot++;
            MessageRecipient messageRecipient = (MessageRecipient) i.next();

            InternetAddress testAddress =
                    new InternetAddress(userPrefix + tot + userSuffix);

            assertEquals("Wrong recipient ",
                         testAddress.getAddress(),
                         messageRecipient.getAddress().getAddress());
            assertEquals("Channel name has not been overriden",
                         messageRecipient.getChannelName(),
                         "smtp");
        }

        assertEquals("Expecting 9 recipients got ", tot, 9);
    }

    /**
     * Test of getIterator method from {@link MessageRecipients}
     */
    public void notestGetIterator() {
        // This is tested by all other test cases.
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

 27-Oct-03	45/10	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 27-Oct-03	45/8	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 27-Oct-03	45/6	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 24-Oct-03	45/4	mat	VBM:2003101502 Rework tests to use AppManager properly

 23-Oct-03	45/2	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
