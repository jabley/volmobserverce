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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.synergetics.localization.MessageLocalizer;

/**
 * A notification with built-in support for localization.
 */
public class LocalizedMessageNotification extends SimpleMessageNotification {
    /**
     * The message localizer for this class.
     */
    private static final MessageLocalizer MESSAGES =
            LocalizationFactory.createMessageLocalizer(
                    LocalizedMessageNotification.class);

    /**
     * Constructs a notification with a specified message key.
     *
     * @param type
     * @param messageKey The key for the message of the notification
     */
    public LocalizedMessageNotification(NotificationType type,
            String messageKey) {
        super(type);
        setMessage(MESSAGES.format(messageKey));
    }

    /**
     * Constructs a notification with a specified message key and parameter.
     *
     * @param type
     * @param messageKey The key for the message of the notification
     * @param param The parameter for the message
     */
    public LocalizedMessageNotification(NotificationType type,
            String messageKey, Object param) {
        super(type);
        setMessage(MESSAGES.format(messageKey, param));
    }

    /**
     * Constructs a notification with a specified message key and parameters.
     *
     * @param type
     * @param messageKey The key for the message of the notification
     * @param params The parameters for the message
     */
    public LocalizedMessageNotification(NotificationType type,
            String messageKey, Object[] params) {
        super(type);
        setMessage(MESSAGES.format(messageKey, params));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	8613/1	geoff	VBM:2005052404 Holding VBM for XDIME CP prior to 3.3.1 release

 18-May-05	8181/4	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
