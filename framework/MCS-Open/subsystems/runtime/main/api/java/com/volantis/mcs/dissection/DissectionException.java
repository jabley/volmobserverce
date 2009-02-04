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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection;

public class DissectionException extends Exception {

    /**
     * Creates new <code>DissectionException</code> without detail message.
     */
    public DissectionException() {
    }

    /**
     * Constructs an <code>DissectionException</code> with the specified
     * detail message.
     * @param message The detail message.
     */
    public DissectionException(String message) {
        super(message);
    }

    /**
     * Create a new <code>DissectionException</code> which was caused by the
     * specified Throwable.
     * @param cause The cause of this exception being thrown.
     */
    public DissectionException(Throwable cause) {
        super(cause);
    }

    /**
     * Create a new <code>DissectionException</code> with the specified message
     * which was caused by the specified Throwable.
     * @param message The message.
     * @param cause The cause of this exception being thrown.
     */
    public DissectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
