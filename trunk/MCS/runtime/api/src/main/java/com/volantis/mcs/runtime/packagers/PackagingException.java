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
 * $Header: /src/voyager/com/volantis/mcs/runtime/packagers/PackagingException.java,v 1.2 2003/02/18 10:28:36 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-03    Phil W-S        VBM:2003013013 - Created. Provides a mechanism
 *                              specific exception.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers;

/**
 * Provides a packaging mechanism-specific exception capable of indicating the
 * causative exception.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class PackagingException extends Exception {
    /**
     * Initializes the new instance with no message or causative
     * exception recorded.
     */
    public PackagingException() {
    }

    /**
     * Initializes the new instance with the given message. No causative
     * exception is recorded.
     *
     * @param message the exception message
     */
    public PackagingException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance with the given message and causative
     * exception.
     *
     * @param message the exception message
     * @param cause the exception that caused this exception to be generated
     */
    public PackagingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes the new instance with the given causative exception. No
     * message is recorded.
     *
     * @param cause the exception that caused this exception to be generated
     */
    public PackagingException(Throwable cause) {
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

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
