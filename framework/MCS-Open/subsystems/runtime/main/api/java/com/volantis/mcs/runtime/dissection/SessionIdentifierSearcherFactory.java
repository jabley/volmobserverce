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

package com.volantis.mcs.runtime.dissection;

/**
 * This factory creates the appropriate SessionIdentifierSearcher for splitting
 * urls up into prefix, jsessionid and suffix. This is used by the URLOptimiser
 * to ensure that the jsessionid is stored in the string table once per page.
 */
public class SessionIdentifierSearcherFactory {

    /**
     * Default constructor
     */
    public SessionIdentifierSearcherFactory() {
    }

    /**
     * Returns the appropriate SessionIdentifierSearcher for the application
     * server on which Volantis MCS is running.
     */
    public static SessionIdentifierSearcher create() {
        return new StandardSessionIdentifierSearcher();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
