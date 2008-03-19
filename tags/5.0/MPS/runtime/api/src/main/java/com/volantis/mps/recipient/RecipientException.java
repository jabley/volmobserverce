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
 * $Header: /src/mps/com/volantis/mps/recipient/RecipientException.java,v 1.2 2003/03/20 10:15:36 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 19-Mar-03    Geoff           VBM:2003032001 - Added a to do as I was going
 *                              past.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * This exception encapsulates any error occurring during recipient related
 * operations.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @todo this demonstration of the "cut & paste" pattern ought to use something like {@link com.volantis.shared.throwable.ExtendedException} instead.
 */
public class RecipientException extends Exception {

    private static final String mark = "(c) Volantis Systems Ltd 2002. ";

    /**
     * The underlying cause of this exception. Can be null.
     */
    protected Throwable cause;

    /**
     * Initializes the new instance without detail message.
     */
    public RecipientException() {
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param msg the detail message.
     */
    public RecipientException(String msg) {
        super(msg);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message The message.
     * @param cause   The cause of this exception being thrown.
     */
    public RecipientException(String message, Throwable cause) {
        super(message);

        this.cause = cause;
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param cause The cause of this exception being thrown.
     */
    public RecipientException(Throwable cause) {
        this.cause = cause;
    }

    /**
     * Returns the message associated with the exception or, if there is no
     * message, the cause exception's message if one is available.
     *
     * @return a message describing the exception
     */
    public String getMessage() {
        String message = super.getMessage();

        if ((message == null) && (cause != null)) {
            message = cause.getMessage();
        }

        return message;
    }

    /**
     * Gets the root cause of this exception.
     *
     * @return The root cause of this exception, may be null.
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * This prints the stack trace of this <code>RecipientException</code>. If
     * there was a root cause then the stack trace of the root cause is printed
     * after.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * This prints the stack trace of this <code>RecipientException</code>. If
     * there was a root cause then the stack trace of the root cause is printed
     * after.
     *
     * @param stream The <code>PrintStream</code> to which the stack trace is
     *               printed.
     */
    public void printStackTrace(PrintStream stream) {
        synchronized (stream) {
            super.printStackTrace(stream);
            if (cause != null) {
                stream.println();
                stream.print("Root cause: ");
                cause.printStackTrace(stream);
            }
        }
    }

    /**
     * This prints the stack trace of this <code>RecipientException</code>. If
     * there was a root cause then the stack trace of the root cause is printed
     * after.
     *
     * @param writer The <code>PrintWriter</code> to which the stack trace is
     *               printed.
     */
    public void printStackTrace(PrintWriter writer) {
        synchronized (writer) {
            super.printStackTrace(writer);
            if (cause != null) {
                writer.print("Root cause: ");
                cause.printStackTrace(writer);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-May-05	666/1	philws	VBM:2005050311 Port of failureReason from 3.3

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 22-Apr-05	610/1	philws	VBM:2005040503 Port Public API changes from 3.3

 22-Apr-05	608/1	philws	VBM:2005040503 Update MPS Public API

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
