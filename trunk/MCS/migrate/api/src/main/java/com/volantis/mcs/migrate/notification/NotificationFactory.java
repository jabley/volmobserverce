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
package com.volantis.mcs.migrate.notification;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;

import java.lang.reflect.UndeclaredThrowableException;

public abstract class NotificationFactory {

    /**
     * The default instance.
     */
    private static final NotificationFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = NotificationFactory.class.getClassLoader();
            Class implClass = loader.loadClass("com.volantis.mcs." +
                    "migrate.impl.notification.DefaultNotificationFactory");
            defaultInstance = (NotificationFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static NotificationFactory getDefaultInstance() {
        return defaultInstance;
    }

    public abstract Notification createThrowableNotification(
            NotificationType type, Throwable throwable);

    public abstract Notification createLocalizedNotification(
            NotificationType type, String messageKey);

    public abstract Notification createLocalizedNotification(
            NotificationType type, String messageKey,
            Object param);

    public abstract Notification createLocalizedNotification(
            NotificationType type, String messageKey,
            Object[] params);

    /**
     * Create a notification reporter for command-line output.
     *
     * @return A notification reporter for command-line output
     */
    public abstract NotificationReporter createCLIReporter();

    /**
     * Create a notification reporter for use with standard runtime logging.
     * <p></p>
     * @param logger The logger which will receive notifications.
     * @return A configured reporter.
     */
    public abstract NotificationReporter createLogDispatcherReporter(
            LogDispatcher logger);

}
