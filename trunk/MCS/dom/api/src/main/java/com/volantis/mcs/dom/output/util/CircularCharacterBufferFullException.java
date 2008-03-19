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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom.output.util;

/**
 * This class is responsible for providing an Exception that should be
 * thrown by {@link CircularCharacterBuffer#add(char)} or
 * {@link CircularCharacterBuffer#add(char[])} if the maximum size of the
 * buffer is reached.
 */
public class CircularCharacterBufferFullException extends Exception {

    /**
     * Initialises an instance with the supplied parameters.
     *
     * @param message the message describing the exception.
     */
    public CircularCharacterBufferFullException(String message) {
        super(message);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 30-Sep-05	9583/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 ===========================================================================
*/
