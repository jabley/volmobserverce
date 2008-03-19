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
 * <p>Interface defining the contents of a request to send a message to given
 * recipients.</p>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and
 * source levels.</strong></p>
 *
 * @volantis-api-include-in InternalAPI
 */
public interface SendRequest {

    /**
     * Return a non-null array of Recipient objects that this message should be
     * sent to.
     *
     * @return an array of Recipients (may be zero-length)
     */
    public Recipient[] getRecipients();

    /**
     * Add a Recipient to the list of Recipients to send this message to.
     *
     * @param recipient a Recipient - not null.
     */
    public void addRecipient(Recipient recipient);

    /**
     * Return a Sender representing the originator of the message.
     *
     * @return a Sender
     */
    public Sender getSender();

    /**
     * Set the Sender representing the orginator of the message.
     *
     * @param sender a Sender - may be null.
     */
    public void setSender(Sender sender);

    /**
     * Return the Message to be sent.
     *
     * @return Message
     */
    public Message getMessage();

    /**
     * Set the Message to be sent.
     *
     * @param message a Message - not null.
     */
    public void setMessage(Message message);
}
