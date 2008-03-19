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
/* -----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/mcs/protocols/ProtocolException.java,v 1.4 2003/04/17 10:21:07 geoff Exp $
 * -----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * -----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- ------------------------------------------------
 * 07-Mar-01    Paul            Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 14-Apr-03    Geoff           VBM:2003041603 - Make it extend 
 *                              WrappingException, and removed unused errorCode
 *                              instance variable, constructor and accessor.
 * -----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

public class ProtocolException extends Exception {

    /**
     * Creates new <code>ProtocolException</code> without detail message.
     */
    public ProtocolException() {
    }

    /**
     * Initalise the new instance with the specified parameter.
     *
     * @param cause The cause of this exception being thrown.
     */
    public ProtocolException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an <code>ProtocolException</code> with the specified
     * detail message.
     *
     * @param message The detail message.
     */
    public ProtocolException(String message) {
        super(message);
    }

    /**
     * Create a new <code>ProtocolException</code> with the specified message
     * which was caused by the specified Throwable.
     *
     * @param message The message.
     * @param cause   The cause of this exception being thrown.
     */
    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
