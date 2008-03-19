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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatVisitorException.java,v 1.2 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Apr-03    Geoff           VBM:2003041603 - Created; a WrappingException
 *                              that is passed through the FormatVisitor
 *                              methods.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.shared.throwable.ExtendedRuntimeException;

/**
 * A WrappingException that is passed through the FormatVisitor methods.
 * <p>
 * Suitable for wrapping client code exceptions generated in visitor instances.
 */ 
public class FormatVisitorException extends ExtendedRuntimeException {

    /**
     * Create a new <code>FormatVisitorException</code> with the specified 
     * message which was caused by the specified Throwable.
     * 
     * @param message The message.
     * @param cause The cause of this exception being thrown.
     */
    public FormatVisitorException(String message, Throwable cause) {
        super(message, cause);
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

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
