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

/**
 * RuntimeException that wraps another causative exception
 */
public class ExtendedRuntimeException extends RuntimeException
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
    public ExtendedRuntimeException(String message) {
        this(message, null);
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message the exception message string
     * @param cause the underlying throwable cause being wrapped
     */
    public ExtendedRuntimeException(String message, Throwable cause) {
        super(message);

        this.cause = cause;
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param cause the underlying throwable cause being wrapped
     */
    public ExtendedRuntimeException(Throwable cause) {
        this.cause = cause;
    }

    /**
     * Initialize the new instance with the given parameters.
     */
    public ExtendedRuntimeException() {
    }

    /**
     * Returns the underlying exception being wrapped.
     *
     * @return the underlying throwable cause, or null
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

 04-May-05	8007/1	philws	VBM:2005050311 Port improved wrapped exception message handling from 3.3

 04-May-05	8000/1	philws	VBM:2005050311 Correct exception message handling in wrapping exceptions

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 14-Jul-03	185/2	steve	VBM:2003071402 Refactor exceptions into throwable package

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 ===========================================================================
*/
