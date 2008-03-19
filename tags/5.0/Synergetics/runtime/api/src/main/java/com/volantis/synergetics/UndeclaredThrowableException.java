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
 * $Header: /src/voyager/com/volantis/mcs/utilities/UndeclaredThrowableException.java,v 1.2 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Oct-01    Paul            VBM:2001092608 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 03-Jun-03    Allan           VBM:2003060301 - This class moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Wraps a throwable in a runtime exception to allow it to be rethrown, even
 * when it has not been declared. <p> This class is provided in JDK1.3 but as
 * we need to compile it on JDK1.2 we need to provide our own implementation.
 * </p> <p> This should not be used to circumvent having to declare the
 * exceptions which are thrown by a method but is available to use in those
 * situations when an exception cannot / should not occur and you don't want to
 * have to expose those internal exceptions to the user, e.g. extending
 * Object.clone on an object which implements the Cloneable interface should
 * not result in a CloneNotSupportedException but it is safer to wrap it in
 * this exception than to simply ignore it. </p>
 *
 * @deprecated Use standard exception now.
 */
public class UndeclaredThrowableException
    extends RuntimeException {

    /**
     * The undeclared checked exception that was thrown.
     */
    private Throwable undeclaredThrowable;

    /**
     * Constructs an <code>UndeclaredThrowableException</code> with the
     * specifed <code>Throwable</code>.
     *
     * @param undeclaredThrowable The undeclared checked exception that was
     *                            thrown.
     */
    public UndeclaredThrowableException(Throwable undeclaredThrowable) {
        super();

        this.undeclaredThrowable = undeclaredThrowable;
    }

    /**
     * Constructs an <code>UndeclaredThrowableException</code> with the
     * specified <code>Throwable</code> and a detail message.
     *
     * @param undeclaredThrowable The undeclared checked exception that was
     *                            thrown.
     * @param s                   The detail message.
     */
    public UndeclaredThrowableException(Throwable undeclaredThrowable,
                                        String s) {
        super(s);

        this.undeclaredThrowable = undeclaredThrowable;
    }

    /**
     * Returns the <code>Throwable</code> instance wrapped in this
     * <code>UndeclaredThrowableException</code>.
     *
     * @return The undeclared checked exception that was thrown.
     */
    public Throwable getUndeclaredThrowable() {
        return undeclaredThrowable;
    }

    /**
     * Prints this <code>UndeclaredThrowableException</code> and its backtrace
     * to the standard error stream.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * Prints this <code>UndeclaredThrowableException</code> and its backtrace
     * to the specified <code>PrintStream</code>.
     */
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            if (undeclaredThrowable != null) {
                ps.print("java.lang.reflect.UndeclaredThrowableException: ");
                undeclaredThrowable.printStackTrace(ps);
            } else {
                super.printStackTrace(ps);
            }
        }
    }

    /**
     * Prints this <code>UndeclaredThrowableException</code> and its backtrace
     * to the specified <code>PrintWriter</code>.
     */
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            if (undeclaredThrowable != null) {
                pw.print("java.lang.reflect.UndeclaredThrowableException: ");
                undeclaredThrowable.printStackTrace(pw);
            } else {
                super.printStackTrace(pw);
            }
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */
