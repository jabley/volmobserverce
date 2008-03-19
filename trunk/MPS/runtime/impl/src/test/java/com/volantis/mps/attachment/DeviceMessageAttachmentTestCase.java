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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/attachment/DeviceMessageAttachmentTestCase.java,v 1.1 2002/12/09 15:05:11 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Dec-02    Sumit           VBM:2002112602 - Created
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mps.attachment;

import com.volantis.mps.message.MessageException;


/**
 * This specialises the MessageAttachement tests for
 * {@link DeviceMessageAttachment}.
 */
public class DeviceMessageAttachmentTestCase
        extends MessageAttachmentTestAbstract {
    /**
     * A valid device name that can be used for the test instances.
     */
    protected static final String DEVICE_NAME = "Thunderbird";

    /**
     * A valid channle name that can be used for the test instances.
     */
    protected static final String CHANNEL_NAME = "smtp";

    /**
     * Initialise a new instance of this test case.
     */
    public DeviceMessageAttachmentTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DeviceMessageAttachmentTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This tests the creation of DeviceMessageAttachements
     */
    public void testDeviceMessageAttachmentCreation() throws Exception {
        // This does not test failure conditions in MessageAttachment as
        // they are tested elsewhere in this test hierarchy

        // Test valid, but undefined
        DeviceMessageAttachment ma = (DeviceMessageAttachment)getTestInstance();

        // Test valid, and complete
        ma = (DeviceMessageAttachment) getTestInstance(VALUE,
                                                       "text/plain",
                                                       MessageAttachment.FILE);

        // Test invalid - device name
        try {
            ma = new DeviceMessageAttachment(VALUE,
                                             "text/plain",
                                             MessageAttachment.FILE,
                                             null,
                                             "smtp");
            fail("Previous call should have caused an exception (1)");
        } catch (MessageException me) {
            // Test success
        }

        // Test invalid - channel name
        try {
            ma = new DeviceMessageAttachment(VALUE,
                                             "text/plain",
                                             MessageAttachment.FILE,
                                             "Thunderbird",
                                             null);
            fail("Previous call should have caused an exception (2)");
        } catch (MessageException me) {
            // Test success
        }

    }

    /**
     * This tests {set|get}DeviceName methods.
     */
    public void testDeviceNames() throws Exception {
        final String anotherDevice = "Mozilla";

        DeviceMessageAttachment ma = new DeviceMessageAttachment(VALUE,
                                                                 MIME,
                                                                 VALUE_TYPE,
                                                                 DEVICE_NAME,
                                                                 CHANNEL_NAME);

        // Get value and check it is as it should be
        String retrieved = ma.getDeviceName();
        assertEquals("Values should match", DEVICE_NAME, retrieved);

        // Set an invalid value
        try {
            ma.setDeviceName(null);
            fail("Previous call should have caused an exception");
        } catch (MessageException me) {
            // Test success
        }

        // Set a valid value
        ma.setDeviceName(anotherDevice);

        // Get value and check it is as it should be
        retrieved = ma.getDeviceName();
        assertEquals("Values should match", anotherDevice, retrieved);
    }

    /**
     * This tests {set|get}ChannelName methods.
     */
    public void testChannelNames() throws Exception {
        final String anotherChannel = "Mozilla";

        DeviceMessageAttachment ma = new DeviceMessageAttachment(VALUE,
                                                                 MIME,
                                                                 VALUE_TYPE,
                                                                 DEVICE_NAME,
                                                                 CHANNEL_NAME);

        // Get value and check it is as it should be
        String retrieved = ma.getChannelName();
        assertEquals("Values should match", CHANNEL_NAME, retrieved);

        // Set an invalid value
        try {
            ma.setChannelName(null);
            fail("Previous call should have caused an exception");
        } catch (MessageException me) {
            // Test success
        }

        // Set a valid value
        ma.setChannelName(anotherChannel);

        // Get value and check it is as it should be
        retrieved = ma.getChannelName();
        assertEquals("Values should match", anotherChannel, retrieved);
    }

    /**
     * This tests the equality functionality.
     */
    public void testEquals() throws Exception {
        // Test non populated equality
        DeviceMessageAttachment attachment =
                (DeviceMessageAttachment) getTestInstance();
        DeviceMessageAttachment anotherAttachment =
                (DeviceMessageAttachment) getTestInstance();
        assertTrue("Attachments should be equal (1)",
                     attachment.equals(anotherAttachment));
        assertTrue("Attachments should be equal (2)",
                     anotherAttachment.equals(attachment));

        // Test populated equality
        DeviceMessageAttachment one =
                new DeviceMessageAttachment(VALUE,
                                            MIME,
                                            VALUE_TYPE,
                                            "Evolution",
                                            CHANNEL_NAME);
        DeviceMessageAttachment two =
                new DeviceMessageAttachment(VALUE,
                                            MIME,
                                            VALUE_TYPE,
                                            "Evolution",
                                            CHANNEL_NAME);
        DeviceMessageAttachment three =
                new DeviceMessageAttachment(VALUE,
                                            MIME,
                                            VALUE_TYPE,
                                            "Netscape",
                                            CHANNEL_NAME);
        assertTrue("Attachments should be equal (3)", one.equals(two));
        assertTrue("Attachments should not be equal", !one.equals(three));
    }

    // JavaDoc inherited
    public MessageAttachment getTestInstance() {
        return new DeviceMessageAttachment();
    }

    // JavaDoc inherited
    public MessageAttachment getTestInstance(String value,
                                             String mimeType,
                                             int valueType)
            throws MessageException {
        return new DeviceMessageAttachment(value,
                                           mimeType,
                                           valueType,
                                           DEVICE_NAME,
                                           CHANNEL_NAME);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-May-05	693/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 05-May-05	671/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 24-Sep-04	142/3	claire	VBM:2004070806 Correct invalid attachment type on PublicAPI DeviceMessageAttachment

 ===========================================================================
*/
