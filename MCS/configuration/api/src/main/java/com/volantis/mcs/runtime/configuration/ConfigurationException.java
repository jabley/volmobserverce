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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/ConfigurationException.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a WrappingException
 *                              for reporting configuration build problems. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 * An {@link Exception} for reporting configuration build problems.
 */ 
public class ConfigurationException extends Exception {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
