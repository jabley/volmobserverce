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

package com.volantis.mcs.protocols.renderer;

/**
 * Exception thrown by protocol rendering code.
 */ 
public class RendererException extends Exception {

    /**
     * Initialise a new <code>RendererException</code> with no message.
     */
    public RendererException() {
    }

    /**
     * Initialise a new <code>RendererException</code> with the specified
     * message.
     * @param message The message.
     */
    public RendererException(String message) {
        super(message);
    }

    /**
     * Initialise a new <code>RendererException</code> with the specified
     * message which was caused by the specified Throwable.
     * @param message The message.
     * @param cause The cause of this throwable being thrown.
     */
    public RendererException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Initialise a new <code>RendererException</code> which was caused by the
     * specified Throwable.
     * @param cause The cause of this throwable being thrown.
     */
    public RendererException(Throwable cause) {
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

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 15-Mar-04	3274/7	pduffin	VBM:2004022704 Minor formatting and javadoc changes

 11-Mar-04	3274/1	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 ===========================================================================
*/
