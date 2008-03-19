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
package com.volantis.mcs.devices;

/**
 * This Exception is thrown to indicate a problem when accessing the device 
 * repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class DeviceRepositoryException extends Exception {
    public DeviceRepositoryException() {
    }

    public DeviceRepositoryException(Throwable cause) {
        super(cause);
    }

    public DeviceRepositoryException(String message) {
        super(message);
    }

    public DeviceRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 21-Sep-04	5569/2	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 27-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - fix build dependencies

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/
