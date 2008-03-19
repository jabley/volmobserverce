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

package com.volantis.mps.bms.impl;

import com.volantis.mps.bms.Address;
import com.volantis.mps.bms.Failures;
import com.volantis.mps.bms.MSISDN;
import com.volantis.mps.bms.MalformedAddressException;
import com.volantis.mps.bms.Message;
import com.volantis.mps.bms.MessageFactory;
import com.volantis.mps.bms.Recipient;
import com.volantis.mps.bms.SMTPAddress;
import com.volantis.mps.bms.SendRequest;
import com.volantis.mps.bms.Sender;

import java.net.URL;
import java.util.regex.Pattern;

public class DefaultMessageFactory extends MessageFactory {

    private static final Pattern SMTP_PATTERN = Pattern.compile("[!-~]+@[!-~]+");

    // javadoc inherited
    public MSISDN createMSISDN(String msisdn) throws MalformedAddressException {
        return new DefaultMSISDN(msisdn);
    }

    // javadoc inherited
    public SMTPAddress createSMTPAddress(String address) throws MalformedAddressException {
        return new DefaultSMTPAddress(address);
    }

    // javadoc inherited
    public Recipient createRecipient(Address address, String deviceName) {
        return new DefaultRecipient(address, deviceName);
    }

    // javadoc inherited.
    public Message createMessage(String content) {
        return new DefaultMessage(content);
    }

    // javadoc inherited
    public Message createMessage(URL url) {
        return new DefaultMessage(url);
    }

    // javadoc inherited
    public Failures createFailures() {
        return new DefaultFailures();
    }

    // javadoc inherited
    public SendRequest createSendRequest() {
        return new DefaultSendRequest();
    }

    // javadoc inherited
    public Sender createSender(MSISDN msisdn, SMTPAddress smtpAddress) {
        return new DefaultSender(msisdn,  smtpAddress);
    }

    /**
     * Return a String form of the Address. Used by JiBX for serializing the
     * object to XML.
     *
     * @param address an Address
     * @return a String form of the Address.
     */
    public static String serializeAddress(Address address) {
        // Do we need to worry about this?
//        if (null == address) {
//            return null;
//        }

        return address.getValue();
    }

    /**
     * Return an Address based on the provided String. If the specified String
     * is null, then it will return null, otherwise it will try to create an
     * appropriate implementation.
     *
     * @param address a String representing an address value.
     * @return an Address implementation.
     * @throws IllegalArgumentException if the address cannot be resolved to a
     *                                  concrete implementation.
     */
    public static Address deserializeAddress(String address) {

        // guard condition - sender may be null, for example.
        if (null == address) {
            return null;
        }

        try {
            if (isSMTPAddress(address)) {
                return MessageFactory.getDefaultInstance().createSMTPAddress(address);
            }
            return MessageFactory.getDefaultInstance().createMSISDN(address);
        } catch (MalformedAddressException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static boolean isSMTPAddress(String address) {
        synchronized (SMTP_PATTERN) {
            return SMTP_PATTERN.matcher(address).matches();
        }
    }

    /**
     * Return a String form of the MSISDN. Used by JiBX for serializing the
     * object to XML.
     *
     * @param msisdn a MSISDN
     * @return a String form of the MSISDN.
     */
    public static String serializeMSISDN(MSISDN msisdn) {
        // Do we need to worry about this?
//        if (null == address) {
//            return null;
//        }

        return msisdn.getValue();
    }

    /**
     * Return a MSISDN based on the provided String. If the specified String
     * is null, then it will return null, otherwise it will try to create an
     * appropriate implementation.
     *
     * @param msisdn a String representing a MSISDN value.
     * @return a MSISDN implementation.
     * @throws IllegalArgumentException if the MSISDN cannot be resolved to a
     *                                  concrete implementation.
     */
    public static MSISDN deserializeMSISDN(String msisdn) {

        // guard condition - sender may be null, for example.
        if (null == msisdn) {
            return null;
        }

        try {
            return MessageFactory.getDefaultInstance().createMSISDN(msisdn);
        } catch (MalformedAddressException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Return the String form of the SMTPAddress.
     *
     * @param address an SMTPAddress.
     * @return a String form of the SMTPAddress.
     */
    public static String serializeSMTPAddress(SMTPAddress address) {
        return address.getValue();
    }

    /**
     * Return an SMTPAddress implementation based on the provided address
     * String. If the specified String is null, then it will return null,
     * otherwise it will try to create an appropriate implementation.
     *
     * @param address a String representing an SMTP address.
     * @return an SMTPAddress implementation.
     * @throws IllegalArgumentException if the SMTP address cannot be resolved
     *                                  to a concrete implementation.
     */
    public static SMTPAddress deserializeSMTPAddress(String address) {
        if (null == address) {
            return null;
        }

        try {
            return MessageFactory.getDefaultInstance().createSMTPAddress(address);
        } catch (MalformedAddressException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
