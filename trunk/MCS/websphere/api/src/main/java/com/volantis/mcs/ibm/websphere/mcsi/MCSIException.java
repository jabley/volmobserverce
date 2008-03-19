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

package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.papi.PAPIException;

/**
 * This class is used when an exception occurs inside MCSI.
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * 

 */
public class MCSIException extends PAPIException {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";
    
    /**
     * Create a new <code>MCSIException</code> with no message.
     */
    public MCSIException () {
    }
    
    /**
     * Create a new <code>MCSIException</code>
     * with the specified message.
     * @param message The message.
     */
    public MCSIException (String message) {
        super (message);
    }
    
    /**
     * Create a new <code>MCSIException</code> with the specified 
     * message which was caused by the specified Throwable.
     * @param message The message.
     * @param cause The cause of this exception being thrown.
     */
    public MCSIException (String message, Throwable cause) {
        super (message, cause);
    }


    /**
     * Create a new <code>MCSIException</code> which was caused by the
     * specified Throwable.
     * @param cause The cause of this exception being thrown.
     */
    public MCSIException (Throwable cause) {
        super (cause);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
