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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.impl.notification;

import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationType;

/**
 * Simple text message notification. Allows the creation of notifications that
 * contain a simple message to be displayed to the default output.
 */
public class SimpleMessageNotification implements Notification {
    /**
     * The message associated with this notification.
     */
    private String message;

    /**
     * The type of notification.
     */
    private NotificationType type;

    /**
     * Construct a simple message notification for a specified string.
     *
     * @param type The type of notification.
     * @param message The string message for this notification
     * @throws java.lang.IllegalArgumentException if the message is null
     */
    public SimpleMessageNotification(NotificationType type, String message) {
        if (message == null) {
            throw new IllegalArgumentException("Must specify non-null message");
        }
        this.type = type;
        this.message = message;
    }


    /**
     * Construct a simple message notification. Note that this constructor is
     * protected, and should only be used by child classes that need to be
     * initialised before they can determine the notification message to use.
     * When this constructor is used, {@link #setMessage} should be used to
     * set the text associated with the notification before it is used.
     *
     * @param type Indicates whether the message is an error message
     * @throws java.lang.IllegalArgumentException if the message is null
     */
    protected SimpleMessageNotification(NotificationType type) {
        this.type = type;
    }

    /**
     * Sets the message for this notification.
     *
     * @param newMessage
     */
    protected void setMessage(String newMessage) {
        message = newMessage;
    }

    // Javadoc inherited
    public String getMessage() {
        return message;
    }

    // Javadoc inherited
    public NotificationType getType() {
        return type;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8181/4	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
