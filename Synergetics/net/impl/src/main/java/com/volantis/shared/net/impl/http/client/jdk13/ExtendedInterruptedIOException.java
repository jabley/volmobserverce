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

package com.volantis.shared.net.impl.http.client.jdk13;

import com.volantis.shared.throwable.ThrowableExtensions;
import com.volantis.shared.throwable.ThrowableSupport;

import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An exception that wraps a causative exception.
 */
public class ExtendedInterruptedIOException
        extends InterruptedIOException
        implements ThrowableExtensions {
    /**
     * The original causative exception, if any
     */
    private Throwable cause;

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message the exception message string
     */
    public ExtendedInterruptedIOException(String message) {
        this(message, null);
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message the exception message string
     * @param cause   the underlying exception being wrapped
     */
    public ExtendedInterruptedIOException(String message, Throwable cause) {
        super(message);

        this.cause = cause;
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param cause the underlying exception being wrapped
     */
    public ExtendedInterruptedIOException(Throwable cause) {
        this.cause = cause;
    }

    /**
     * Initialize the new instance with the given parameters.
     */
    public ExtendedInterruptedIOException() {
    }

    /**
     * Returns the underlying exception being wrapped.
     *
     * @return the underlying wrapped exception, or null
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Returns this exception's message. If the exception has no message but
     * has a causative exception, that exception's message is returned
     * instead.
     *
     * @return the exception's message
     */
    public String getMessage() {
        String message = super.getMessage();

        if ((message == null) && (cause != null)) {
            message = cause.getMessage();
        }

        return message;
    }

    /**
     * Prints this exception and its causative exception to the standard
     * error stream.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * Prints this exception and its causative exception to the specified
     * <code>PrintStream</code>.
     */
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            super.printStackTrace(ps);

            ThrowableSupport.printCauseStackTrace(this, ps);
        }
    }

    /**
     * Prints this exception and its causative exception to the specified
     * <code>PrintWriter</code>.
     */
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            super.printStackTrace(pw);

            ThrowableSupport.printCauseStackTrace(this, pw);
        }
    }
}
