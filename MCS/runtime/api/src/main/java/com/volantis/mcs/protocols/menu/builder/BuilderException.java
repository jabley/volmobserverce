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

package com.volantis.mcs.protocols.menu.builder;

/**
 * This exception is thrown when the builder detects an invalid build state.
 */
public class BuilderException extends Exception {
    /**
     * Initializes the new instance with the given parameters.
     */
    public BuilderException() {
        super();
    }

    /**
     * Initializes the new instance with the given parameters.
     * 
     * @param message the detailed message associated with the exception
     */
    public BuilderException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message the detailed message associated with the exception
     * @param cause the exception for which this exception is created
     */
    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param cause the exception for which this exception is created
     */
    public BuilderException(Throwable cause) {
        super(cause);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 05-Mar-04	3292/1	philws	VBM:2004022703 Added Menu Model Builder API

 ===========================================================================
*/
