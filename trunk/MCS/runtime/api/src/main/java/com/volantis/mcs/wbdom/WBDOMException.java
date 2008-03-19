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
 * 22-May-03    Geoff           VBM:2003042905 - Created; the exception thrown 
 *                              by WBDOM classes. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.WBSAXException;

/**
 * The exception thrown by WBDOM classes.
 * <p>
 * Initially it seemed attractive to make this exception extend 
 * {@link WBSAXException} so that we could avoid wrapping exceptions as they
 * passed up the stack from WBSAX to WBDOM. Unfortunately, this doesn't work
 * because in order to avoid the wrapping, the inverse is actually required 
 * (i.e. the WBSAX exception would have to extend the WBDOM exception), which
 * is clearly bogus. Seems like there is some underlying problem with Java
 * in this regard but I have no more time to think about it.
 */
public class WBDOMException extends Exception {

    /**
     * Create an instance of this class with the message specified. 
     * 
     * @param message the exception message to use.
     */ 
    public WBDOMException(String message) {
        super(message);
    }

    /**
     * Create an instance of this class with the cause specified. 
     * 
     * @param cause the exception which caused this exception.
     */ 
    public WBDOMException(Throwable cause) {
        super(cause);
    }

    /**
     * Create an instance of this class with the message and cause specified. 
     * 
     * @param message the exception message to use.
     * @param cause the exception which caused this exception.
     */ 
    public WBDOMException(String message, Throwable cause) {
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

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/2	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
