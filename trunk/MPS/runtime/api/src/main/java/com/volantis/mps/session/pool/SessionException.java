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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.session.pool;

import com.volantis.mps.message.MessageException;

/**
 * This exception indicates that there was a problem with the Session with the
 * SMSC which resulted in Messages not being sent.
 */
public class SessionException extends MessageException {

    // Javadoc unnecessary.
    public SessionException() {
    }

    // Javadoc unnecessary.
    public SessionException(String message) {
        super(message);
    }

    // Javadoc unnecessary.
    public SessionException(Throwable cause) {
        super(cause);
    }

    // Javadoc unnecessary.
    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
