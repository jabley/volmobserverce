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

package com.volantis.xml.pipeline.sax.drivers.webservice;

import com.volantis.shared.throwable.ExtendedException;

/**
 * An exception that can be thrown while processing WSDLResources.
 */
public class WSDLResourceException extends ExtendedException {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    // Javadoc inherited from superclass
    public WSDLResourceException(String message) {
        super(message);
    }

    // Javadoc inherited from superclass
    public WSDLResourceException(String message, Exception cause) {
        super(message, cause);
    }

    // Javadoc inherited from superclass
    public WSDLResourceException(Exception cause) {
        super(cause);
    }

    // Javadoc inherited from superclass
    public WSDLResourceException() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 14-Jul-03	185/1	steve	VBM:2003071402 Refactor exceptions into throwable package

 24-Jun-03	124/1	adrian	VBM:2003061902 Implemented provideInputSource method for ServletRequestResource

 ===========================================================================
*/
