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

package com.volantis.mcs.migrate.impl.notification.reporter;

import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;

/**
 * A simple implementation of the notification reporter interface that prints
 * notification messages to the standard output, for use in CLI applications.
 */
public class SimpleCLINotificationReporter implements NotificationReporter {
    /**
     * Reports the specified notification by printing its message to the
     * console.
     *
     * @param notification The notification to report
     */
    public void reportNotification(Notification notification) {
        if (notification.getType() == NotificationType.ERROR) {
            System.err.println("ERROR " + notification.getMessage());
        } else if (notification.getType() == NotificationType.INFO) {
            System.out.println(notification.getMessage());
        } else {
            throw new IllegalStateException("unknown notification type");
        }
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
