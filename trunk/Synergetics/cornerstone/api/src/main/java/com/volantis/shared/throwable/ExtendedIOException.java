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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.throwable;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * An exception that wraps a causative exception.
 */
public class ExtendedIOException extends IOException
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
    public ExtendedIOException(String message) {
        this(message, null);
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message the exception message string
     * @param cause the underlying exception being wrapped
     */
    public ExtendedIOException(String message, Throwable cause) {
        super(message);

        this.cause = cause;
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param cause the underlying exception being wrapped
     */
    public ExtendedIOException(Throwable cause) {
        this.cause = cause;
    }

    /**
     * Initialize the new instance with the given parameters.
     */
    public ExtendedIOException() {
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


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
