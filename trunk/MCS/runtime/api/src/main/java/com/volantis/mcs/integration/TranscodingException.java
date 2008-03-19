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
package com.volantis.mcs.integration;

/**
 * An exception that can be thrown if an asset transcoder cannot successfully
 * perform a specific transcoding operation.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class TranscodingException extends Exception {
    /**
     * Initializes the new instance.
     */
    public TranscodingException() {
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message the exception message
     */
    public TranscodingException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message the exception message
     * @param cause   the underlying exception that caused this exception to be
     *                thrown
     */
    public TranscodingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param cause the underlying exception that caused this exception to be
     *              thrown
     */
    public TranscodingException(Throwable cause) {
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

 28-Jul-04	4976/4	philws	VBM:2004072801 Add the PluggableAssetTranscoder to the Public API

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

 ===========================================================================
*/
