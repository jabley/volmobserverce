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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.model;

/**
 * Exception that may be thrown by visitor visit methods.
 */
public class MenuModelVisitorException extends Exception {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message the exception message
     */
    public MenuModelVisitorException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message the exception message
     * @param throwable the cause for this exception being propagated
     */
    public MenuModelVisitorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param throwable the cause for this exception being propagated
     */
    public MenuModelVisitorException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Initializes the new instance using the given parameters.
     */
    public MenuModelVisitorException() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 ===========================================================================
*/
