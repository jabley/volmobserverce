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

/**
 * <p>Interface defining a Recipient.</p>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and
 * source levels.</strong></p>
 *
 * @volantis-api-include-in InternalAPI
 */
public interface Recipient {

    /**
     * Set the {@link Address} of this Recipient.
     *
     * @param address the Address - may not null.
     */
    void setAddress(Address address);

    /**
     * Return the Address of this Recipient - not null.
     *
     * @return the Address
     */
    Address getAddress();

    /**
     * Set the MPS channel name of this Recipient. If this is not set (i.e. it
     * is null), then MPS will attempt to determine the most suitable channel
     * for the specified message and this Recipient.
     *
     * @param channel the MPS channel name, as specified in the configuration.
     *                May be null.
     */
    void setChannel(String channel);

    /**
     * Return the channel name. May be null.
     *
     * @return the MPS channel name.
     */
    String getChannel();

    /**
     * Set the MCS device name of this Recipient's device, which may be used to
     * determine the most suitable channel to use to deliver the message.
     *
     * @param name MCS device name. Not null.
     * @see #setChannel(String)
     */
    void setDeviceName(String name);

    /**
     * Return the MCS device name of this Recipient's device. Not null.
     *
     * @return the MCS device name.
     */
    String getDeviceName();

    /**
     * Set the {@link RecipientType} of this Recipient. Not null.
     *
     * @param type the RecipientType (not null).
     */
    void setRecipientType(RecipientType type);

    /**
     * Return the RecipientType of this Recipient. Defaults to
     * {@link RecipientType.TO}.
     *
     * @return the RecipientType
     */
    RecipientType getRecipientType();

    /**
     * Set the reason why the message could not be delivered to this Recipient.
     *
     * <em>This method is not intended for use by clients.</em>
     *
     * @param reason
     */
    void setFailureReason(String reason);

    /**
     * Return the reason why the message could not be delivered to this
     * Recipient.
     *
     * @return a string containing the reason why it failed.
     */
    String getFailureReason();

}
