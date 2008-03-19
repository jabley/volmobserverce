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

import com.volantis.synergetics.factory.MetaDefaultFactory;

import java.net.URL;

/**
 * <p>Abstract Factory for creating implementations of message-related objects.</p>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and
 * source levels.</strong></p>
 *
 * @volantis-api-include-in InternalAPI
 */
public abstract class MessageFactory {

    private static MetaDefaultFactory metaDefaultFactory = new MetaDefaultFactory(
            "com.volantis.mps.bms.impl.DefaultMessageFactory",
            MessageFactory.class.getClassLoader());

    /**
     * Return the default instance of the MessageFactory.
     *
     * @return the default instance of the MessageFactory
     */
    public static MessageFactory getDefaultInstance() {
        return (MessageFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Factory Method to create a MSISDN implementation.
     *
     * @param msisdn
     * @return an MSISDN implementation.
     * @throws MalformedAddressException if the MSISDN is invalid.
     * @throws IllegalArgumentException  if the msisdn is null.
     */
    public abstract MSISDN createMSISDN(String msisdn) throws MalformedAddressException;

    /**
     * Factory Method to create a SMTPAddress implementation.
     *
     * @param address
     * @return an SMTPAddress implementation.
     * @throws MalformedAddressException if the SMTPAddress is invalid.
     * @throws IllegalArgumentException  if the address is null.
     */
    public abstract SMTPAddress createSMTPAddress(String address)
            throws MalformedAddressException;

    /**
     * Factory Method to create a Recipient.
     *
     * @param address    an Address
     * @param deviceName a non-null MCS device name
     * @return a Recipient implementation.
     * @throws IllegalArgumentException if the address or deviceName is null.
     */
    public abstract Recipient createRecipient(Address address, String deviceName);

    /**
     * Factory Method to create a Message.
     *
     * @param url the URL used to access the message.
     * @return a Message.
     * @throws IllegalArgumentException if the url is null.
     */
    public abstract Message createMessage(URL url);


    /**
     * Creates a message with the given content (which is expected
     * to be XDIME).  The XDIME will be processed by MCS to determine
     * the appropriate format which is determined by the channel type.
     * <p>
     * <b>Note that the XDIME should be enclosed in a "message" element.</b>
     *
     * @param xdime the XDIME to be delivered.
     *
     * @return a message containing XDIME that is to be delivered in an
     * appropriate format depending on the output channel.
     */
    public abstract Message createMessage(String xdime);

    /**
     * Factory Method to create a Failures instance.
     *
     * @return a Failures.
     */
    public abstract Failures createFailures();

    /**
     * Factory Method to create SendRequest instance.
     *
     * @return a SendRequest.
     */
    public abstract SendRequest createSendRequest();

    /**
     * Factory Method to create a Sender instance.
     *
     * @param msisdn      a MSISDN - may be null.
     * @param smtpAddress an SMTPAddress - may be null.
     * @return a Sender.
     */
    public abstract Sender createSender(MSISDN msisdn, SMTPAddress smtpAddress);
}
