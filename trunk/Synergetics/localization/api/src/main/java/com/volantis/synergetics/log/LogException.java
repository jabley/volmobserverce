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
package com.volantis.synergetics.log;

/**
 * An exception for reporting logger configuration problems.
 */
public class LogException extends Exception {

    /**
     * Initialize a new instance.
     */
    public LogException() {
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param message describing the exception
     */
    public LogException(String message) {
        super(message);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Mar-05	411/4	emma	VBM:2005020303 Modifications after review

 17-Mar-05	411/2	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 ===========================================================================
*/
