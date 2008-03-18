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
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An extended version of the SAXParseException that fixes a number of issues
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
public class ExtendedSAXParseException
        extends SAXParseException
        implements ThrowableExtensions {

    /**
     * A Throwable that is the root cause of this Exception. May be null.
     */
    private Throwable cause;

    /**
     * Creates a new ExtendedSAXParseException
     * @param message the exception message
     * @param locator the locator
     */
    public ExtendedSAXParseException(String message, Locator locator) {
        super(message, locator);
    }

    /**
     * Creates a new ExtendedSAXParseException
     * @param message the exception message
     * @param locator the locator
     * @param throwable the underlying throwable
     */
    public ExtendedSAXParseException(String message,
                                     Locator locator,
                                     Throwable throwable) {
        super(message, locator, null);
        this.cause = throwable;
    }

    /**
     * Creates a new ExtendedSAXParseException
     * @param message the exception message
     * @param publicId the locator public ID
     * @param systemId the locator system ID
     * @param lineNumber the locator line number
     * @param columnNumber the locator column number
     */
    public ExtendedSAXParseException(String message,
                                     String publicId,
                                     String systemId,
                                     int lineNumber,
                                     int columnNumber) {
        super(message, publicId, systemId, lineNumber, columnNumber);
    }

    /**
     * Creates a new ExtendedSAXParseException
     * @param message the exception message
     * @param publicId the locator public ID
     * @param systemId the locator system ID
     * @param lineNumber the locator line number
     * @param columnNumber the locator column number
     * @param throwable the underlying throwable
     */
    public ExtendedSAXParseException(String message,
                                     String publicId,
                                     String systemId,
                                     int lineNumber,
                                     int columnNumber,
                                     Throwable throwable) {
        super(message, publicId, systemId, lineNumber, columnNumber, null);
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

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 15-Jul-03	191/1	doug	VBM:2003071403 Extended version of the SAXParseException class

 ===========================================================================
*/
