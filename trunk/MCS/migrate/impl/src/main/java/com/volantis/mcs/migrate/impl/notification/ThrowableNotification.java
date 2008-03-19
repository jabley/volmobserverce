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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Notification for passing on information regarding an exception or error.
 */
public class ThrowableNotification implements Notification {

    private NotificationType type;

    /**
     * The throwable object that caused this notification to be generated.
     */
    private Throwable cause;

    /**
     * Construct a notification for a specified throwable cause.
     *
     * @param type
     * @param cause The exception or error that caused this notification
     * @throws java.lang.IllegalArgumentException if the cause is null
     */
    public ThrowableNotification(NotificationType type, Throwable cause) {
        if (type == null) {
            throw new IllegalArgumentException("Must specify a type");
        }
        if (cause == null) {
            throw new IllegalArgumentException("Must specify a Throwable cause");
        }
        this.type = type;
        this.cause = cause;
    }

    // Javadoc inherited
    public String getMessage() {
        // HACK: ensure stack traces are not thrown away. This means that the
        // stack traces are not logged via the normal channel in log4j, but is
        // better than it was before when they were thrown away.
        // TODO: make notifications support stack traces properly.
        StringWriter writer = new StringWriter();
        PrintWriter print = new PrintWriter(writer);
        cause.printStackTrace(print);
        print.close();
        return writer.toString();
//
//        StringBuffer buffer = new StringBuffer(cause.getLocalizedMessage());
//        Throwable throwable = cause;
//        // todo: how do we handle JDK1.4 causes (on JDK 1.3 as well!)
//        while (throwable instanceof ThrowableExtensions && (
//                ((ThrowableExtensions) cause).getCause() != null)) {
//            Throwable child = ((ThrowableExtensions) cause).getCause();
//            buffer.append(": ");
//            buffer.append(child.getLocalizedMessage());
//            throwable = child;
//        }
//        return buffer.toString();
    }

    public NotificationType getType() {
        return type;
    }

    /**
     * Retrieves the root cause of the notification.
     *
     * @return The {@link java.lang.Throwable} object that caused this notification
     */
    public Throwable getThrowableCause() {
        return cause;
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
