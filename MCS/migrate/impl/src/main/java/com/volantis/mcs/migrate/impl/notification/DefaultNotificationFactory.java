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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.impl.notification;

import com.volantis.mcs.migrate.impl.notification.reporter.LogDispatcherNotificationReporter;
import com.volantis.mcs.migrate.impl.notification.reporter.SimpleCLINotificationReporter;
import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.synergetics.log.LogDispatcher;

public class DefaultNotificationFactory extends NotificationFactory {

    public Notification createThrowableNotification(NotificationType type,
            Throwable throwable) {
        return new ThrowableNotification(type, throwable);
    }

    public Notification createLocalizedNotification(NotificationType type,
            String messageKey) {
        return new LocalizedMessageNotification(type, messageKey);
    }

    public Notification createLocalizedNotification(NotificationType type,
            String messageKey, Object param) {
        return new LocalizedMessageNotification(type, messageKey, param);
    }

    public Notification createLocalizedNotification(NotificationType type,
            String messageKey, Object[] params) {
        return new LocalizedMessageNotification(type, messageKey, params);
    }

    public NotificationReporter createLogDispatcherReporter(
            LogDispatcher logger) {
        return new LogDispatcherNotificationReporter(logger);
    }

    public NotificationReporter createCLIReporter() {
        return new SimpleCLINotificationReporter();
    }
}
