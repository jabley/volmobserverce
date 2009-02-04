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
package com.volantis.mcs.migrate.unit.framework.recogniser;

import com.volantis.mcs.migrate.impl.framework.recogniser.RegexpPathRecogniser;

/**
 * Test case for {@link RegexpPathRecogniser}.
 */
public class RegexpPathRecogniserTestCase
        extends RegexpRecogniserTestAbstract {

    // Javadoc inherited.
    protected boolean recognise(String data, String re) {

        // Create the recogniser and run it against the input data.
        RegexpPathRecogniser recogniser = new RegexpPathRecogniser(re);
        boolean match = recogniser.recognisePath(data);
        return match;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
