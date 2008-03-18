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

package com.volantis.shared.throwable;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Provides support for implementing {@link ThrowableExtensions}.
 */
public class ThrowableSupport {

    /**
     * Determines whether the JDK supports a throwable cause. If it does then
     * there is no need to print out the cause as that will be done
     * by the JDK.
     */
    private static boolean JDK_SUPPORTS_CAUSE;

    static {
        try {
            // Check to see whether the initCause method exists on Throwable.
            // If it does then it means that our extended exceptions do not
            // need to explicitly print the cause as that will be handled by
            // Throwable itself.
            Throwable.class.getMethod("initCause", new Class[]{
                Throwable.class
            });
            JDK_SUPPORTS_CAUSE = true;
        } catch (NoSuchMethodException e) {
            JDK_SUPPORTS_CAUSE = false;
        }
    }

    /**
     * Prints the cause of the specified exception to the specified
     * <code>PrintStream</code>.
     */
    public static void printCauseStackTrace(
            ThrowableExtensions extendedThrowable, PrintStream ps) {

        // Only print the cause stack trace out if the JDK is not going to
        // do it as well.
        if (!JDK_SUPPORTS_CAUSE) {
            Throwable cause = extendedThrowable.getCause();
            if (cause != null) {
                ps.print("Cause: ");
                cause.printStackTrace(ps);
            }
        }
    }

    /**
     * Prints the cause of the specified exception to the specified
     * <code>PrintWriter</code>.
     */
    public static void printCauseStackTrace(
            ThrowableExtensions extendedThrowable, PrintWriter pw) {

        // Only print the cause stack trace out if the JDK is not going to
        // do it as well.
        if (!JDK_SUPPORTS_CAUSE) {
            Throwable cause = extendedThrowable.getCause();
            if (cause != null) {
                pw.print("Cause: ");
                cause.printStackTrace(pw);
            }
        }
    }
}
