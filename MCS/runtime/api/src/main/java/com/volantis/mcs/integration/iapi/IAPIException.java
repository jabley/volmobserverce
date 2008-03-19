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
* (c) Volantis Systems Ltd 2003. 
* ----------------------------------------------------------------------------
*/

package com.volantis.mcs.integration.iapi;

/**
 * This class is used when an exception occurs inside IAPI.
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * 
 * @todo later refactor this to extract commonality with PAPI
 */
public class IAPIException extends Exception {
    
    /**
     * Create a new <code>IAPIException</code> with no message.
     */
    public IAPIException () {
    }
    
    /**
     * Create a new <code>IAPIException</code>
     * with the specified message.
     * @param message The message.
     */
    public IAPIException (String message) {
        super (message);
    }
    
    /**
     * Create a new <code>IAPIException</code> with the specified 
     * message which was caused by the specified Throwable.
     * @param message The message.
     * @param cause The cause of this exception being thrown.
     */
    public IAPIException (String message, Throwable cause) {
        super (message, cause);
    }
    
    /**
     * Create a new <code>IAPIException</code> which was caused by the
     * specified Throwable.
     * @param cause The cause of this exception being thrown.
     */
    public IAPIException (Throwable cause) {
        super (cause);
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

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
