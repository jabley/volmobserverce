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

package com.volantis.mps.bms;

/**
 * Generic Exception for problems with the MessageService.
 * 
 * @volantis-api-include-in InternalAPI
 */
public class MessageServiceException extends Exception {

    /**
     * Constructs a new MessageServiceException with null as its detail message.
     * The cause is not initialized and may be subsequently be initialized with
     * a call to {@link #initCause(Throwable)}
     */
    public MessageServiceException() {
        super();
    }

    /**
     * Constructs a new MessageServiceException with the specified detail
     * message. The cause is not initialized and may be subsequently be
     * initialized with a call to {@link #initCause(Throwable)}
     *
     * @param message the detail message. This is saved for later and may be
     *                retrieved by a call to {@link #getMessage()}
     */
    public MessageServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new MessageServiceException with the specified cause and a
     * detail message of (cause==null ? null : cause.toString()) (which
     * typically contains the class and detail message of cause). This
     * constructor is useful for exceptions that are little more than wrappers
     * for other throwables (for example,
     * java.security.PrivilegedActionException).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method). (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public MessageServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructs a new MessageServiceException with the specified detail
     * message and cause.</p>
     * <p>Note that the detail message associated with cause is not
     * automatically incorporated in this exception's detail message.</p>
     *
     * @param message the detail message (which is saved for later retrieval by
     *                the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     */
    public MessageServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
