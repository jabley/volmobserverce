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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.throwable.ExtendedException;

/**
 * An expetion that indicates some HTTP error occured. It extends
 * ExtendedExcpetion so that we can wrap a root cause exception as different
 * frameworks may be used to actually perform an HTTP request.
 */
public class HTTPException extends ExtendedException {

    /**
     * Creates a <code>HTTPException</code> instance
     * @param cause the root cause
     */
    public HTTPException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a <code>HTTPException</code> instance
     * @param message a descriptive message
     */
    public HTTPException(String message) {
        super(message);
    }

    /**
     * Creates a <code>HTTPException</code> instance
     * @param message a descriptive message
     * @param cause the root cause
     */
    public HTTPException(String message, Throwable cause) {
        super(message, cause);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 12-Jul-04	751/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
