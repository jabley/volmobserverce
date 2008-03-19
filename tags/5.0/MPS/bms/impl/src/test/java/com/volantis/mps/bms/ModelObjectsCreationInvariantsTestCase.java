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

package com.volantis.mps.bms;

import junit.framework.TestCase;

import java.net.URL;

public class ModelObjectsCreationInvariantsTestCase extends TestCase {

    MessageFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = MessageFactory.getDefaultInstance();
    }

    public void testCreateMSISDN() throws Exception {
        MSISDN msisdn = factory.createMSISDN("+447777123456789");
        assertNotNull(msisdn);
        assertEquals("+447777123456789", msisdn.getValue());
    }

    public void testNullMSISDNFails() throws Exception {
        try {
            factory.createMSISDN(null);
            fail("Shouldn't allow null msisdn");
        } catch (IllegalArgumentException expected) {
            // ignore
        }
    }

    public void testCreateSMTPAddress() throws Exception {
        SMTPAddress address = factory.createSMTPAddress(
                "james.abley@volantis.com");

        assertNotNull(address);
        assertEquals("james.abley@volantis.com", address.getValue());
    }

    public void testNullSMTPAddressFails() throws Exception {
        try {
            factory.createSMTPAddress(null);
            fail("Shouldn't allow null smtp address");
        } catch (IllegalArgumentException expected) {
            // ignore
        }
    }

    public void testCreateRecipient() throws Exception {
        Address address = factory.createMSISDN("07777123456789");

        Recipient recipient = factory.createRecipient(address, "Nokia-6600");

        assertNotNull(recipient);

        assertEquals("07777123456789", recipient.getAddress().getValue());
        assertEquals("Default recipient Type is TO", RecipientType.TO,
                recipient.getRecipientType());
        assertEquals("Nokia-6600", recipient.getDeviceName());
        assertNull("Default Channel is null", recipient.getChannel());
    }

    public void testCreateRecipientWithNullAddressFails() throws Exception {
        try {
            factory.createRecipient(null, "Nokia-6600");
            fail("Shouldn't allow recipient with null address");
        } catch (IllegalArgumentException expected) {
            // ignore
        }
    }

    public void testCreateRecipientWithNullDeviceFails() throws Exception {
        try {
            Address address = factory.createMSISDN("07777123456789");
            factory.createRecipient(address, null);
            fail("Shouldn't allow recipient with null device");
        } catch (IllegalArgumentException expected) {
            // ignore
        }
    }

    public void testCreateMessage() throws Exception {
        Message message = factory.createMessage(new URL(
                "http://localhost/mcs/some.page.xdime"));
        assertNotNull(message);
    }
}
