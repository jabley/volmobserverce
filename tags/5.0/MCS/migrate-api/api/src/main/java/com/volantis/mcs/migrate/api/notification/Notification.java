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

package com.volantis.mcs.migrate.api.notification;

/**
 * A generic interface for a notification event. Allows messages (information,
 * warnings, errors etc) to be specified independently from the environment in
 * which they are being generated/displayed.
 * <p>Implementations of this interface may provide additional methods relevant
 * to the specific form of notification taking place.</p>
 */
public interface Notification {
    /**
     * Generates a human-readable text form of the notification.
     *
     * @return A human-readable text form of the notification
     */
    public String getMessage();

    /**
     * Return the type of notification.
     *
     * @return the type of notification.
     */
    public NotificationType getType();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
