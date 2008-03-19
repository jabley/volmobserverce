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
package com.volantis.xml.sax;

import com.volantis.shared.throwable.ThrowableExtensions;
import com.volantis.shared.throwable.ThrowableSupport;
import org.xml.sax.SAXException;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An extended version of the SAXException that fixes a number of issues
 * with it.
 * <p>Specifically it addresses the following problems.</p>
 * <ul>
 * <li>Adds a JDK 1.4 method for accessing the underlying throwable.</li>
 * <li>Allows the exception to encapsulate Throwables as well as Exceptions.
 * </li>
 * <li>Ensures that the stack trace for the underlying cause is printed when
 * the stack trace is printed along with any location information.</li>
 * </ul>
 */
public class ExtendedSAXException
        extends SAXException
        implements ThrowableExtensions {

    /**
     * A Trowable that is the root cause of this Exception. May be null.
     */
    private Throwable cause;

    /**
     * Creates a new ExtendedSAXException
     * @param message the exception message
     */
    public ExtendedSAXException(String message) {
        super(message);
    }

    /**
     * Creates a new ExtendedSAXException
     * @param message the exception message
     * @param throwable the underlying throwable
     */
    public ExtendedSAXException(String message,
                                     Throwable throwable) {
        super(message, null);
        this.cause = throwable;
    }

    /**
     * Creates a new ExtendedSAXException
     * @param throwable the underlying throwable
     */
    public ExtendedSAXException(Throwable throwable) {
        super((Exception) null);
        this.cause = throwable;
    }

    // javadoc inherited
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
     * Get the cause as an exception.
     * <p>If the original cause was a Throwable but not an Exception
     * then this returns null.</p>
     * @return The cause, or null.
     * @deprecated Use {@link #getCause} instead.
     */
    public Exception getException() {
        Exception exception = null;
        if (null != cause && cause instanceof Exception) {
            exception = (Exception)cause;
        }
        return exception;
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

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 ===========================================================================
*/
