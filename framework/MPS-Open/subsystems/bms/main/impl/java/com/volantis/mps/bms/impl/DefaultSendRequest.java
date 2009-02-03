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

import com.volantis.mps.bms.Message;
import com.volantis.mps.bms.Recipient;
import com.volantis.mps.bms.SendRequest;
import com.volantis.mps.bms.Address;
import com.volantis.mps.bms.Sender;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation of SendRequest.
 */
public class DefaultSendRequest implements SendRequest {

    private Collection recipients;

    private Sender sender = new DefaultSender(); 

    private Message message;

    public Recipient[] getRecipients() {
        Recipient[] recipientsArray = new Recipient[0];
        if (recipients != null) {
            recipientsArray = (Recipient[])
                recipients.toArray(new Recipient[recipients.size()]);

        }
        return recipientsArray;
    }

    public void addRecipient(Recipient recipient) {
        checkForNull(recipient, "recipient");
        if (null == recipients) {
            this.recipients = new ArrayList();
        }
        recipients.add(recipient);
    }

    public Sender getSender() {
        // Note that the sender may be null if not defined in XML
        // as Jibx does not take initialised instance variables into account
        // during unmarshalling.
        if (sender == null) {
            sender = new DefaultSender();
        }
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        checkForNull(message, "message");
        this.message = message;
    }

    /**
     * Test param to see if it is null.
     *
     * @param param
     * @param fieldName
     * @throws IllegalArgumentException if the param is null, using the
     *                                  fieldName in the detail message.
     */
    private void checkForNull(Object param, String fieldName) {
        if (null == param) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}
