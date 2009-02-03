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
package com.volantis.xml.pipeline.sax.convert;

import com.volantis.shared.throwable.ExtendedException;

/**
 * An exception that can be thrown by a URLConverter.
 */
public class URLConversionException extends ExtendedException {
    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message exception message
     */
    public URLConversionException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message exception message
     * @param cause   the original exception that caused this exception to be
     *                raised
     */
    public URLConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param cause   the original exception that caused this exception to be
     *                raised
     */
    public URLConversionException(Throwable cause) {
        super(cause);
    }

    /**
     * Initializes the new instance with the given parameters.
     */
    public URLConversionException() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 ===========================================================================
*/
