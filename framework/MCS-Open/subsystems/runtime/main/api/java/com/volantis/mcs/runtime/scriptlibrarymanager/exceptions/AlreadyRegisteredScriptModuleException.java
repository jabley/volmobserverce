/*
This file is part of Volantis Mobility Server.

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server. If not, see <http://www.gnu.org/licenses/>.
*/
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.scriptlibrarymanager.exceptions;

/**
 * Custom IllegalStateException based exception
 */
public class AlreadyRegisteredScriptModuleException extends IllegalStateException {

    /**
     * The copyright
     */
    private static String mark = "(c) Volantis Systems Ltd 2008."
            + " All Rights Reserved.";

    /**
     * Initialize the new instance.
     */
    public AlreadyRegisteredScriptModuleException() {
        super();
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message the exception message string
     */
    public AlreadyRegisteredScriptModuleException(String message) {
        super(message);
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param message the exception message string
     * @param throwable the underlying exception being wrapped
     */
    public AlreadyRegisteredScriptModuleException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Initialize the new instance with the given parameters.
     *
     * @param throwable the underlying exception being wrapped
     */
    public AlreadyRegisteredScriptModuleException(Throwable throwable) {
        super(throwable);
    }
}