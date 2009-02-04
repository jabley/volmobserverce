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
import com.volantis.synergetics.log.LogDispatcher;

/**
 * An integration with the standard MCS com.volantis.synergetics.log.LogDispatcher logging.
 * <p/>
 * </p>
 *
 * @see #reportNotification(com.volantis.mcs.migrate.api.notification.Notification)
 */
public class LogDispatcherNotificationReporter implements NotificationReporter {

    private LogDispatcher logger;

    /**
     * Create and initialize the reporter.
     * <p></p>
     *
     * @param logger The logger which will receive notifications.
     */
    public LogDispatcherNotificationReporter(LogDispatcher logger) {
        this.logger = logger;
    }

    /**
     * Relays the specified notification to a LogDispatcher logger.
     * <p/>
     * If the notification is flagged as being an <i>error</i> then it
     * is logged as a LogDispatcher <strong>error</strong>.
     * Otherwise the notification is logged as a <strong>debug</debug>
     * entry, if debug is enabled for the logger.
     * </p>
     *
     * @param notification The notification to report
     */
    public void reportNotification(Notification notification) {

        if (notification.getType() == NotificationType.ERROR) {
            if (logger.isErrorEnabled()) {
                logger.error("error-notification-reporter", notification.getMessage());
            }
        } else if (notification.getType() == NotificationType.INFO) {

            if (logger.isInfoEnabled()) {
                logger.info("info-notification-reporter", notification.getMessage());
            }
        } else {
            throw new IllegalStateException("unexpected notification type");
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - post-review amendments: new reporter type

 ===========================================================================
*/
