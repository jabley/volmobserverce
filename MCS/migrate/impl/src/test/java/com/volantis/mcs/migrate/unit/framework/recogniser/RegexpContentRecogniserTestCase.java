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

import com.volantis.mcs.migrate.impl.framework.recogniser.RegexpContentRecogniser;

import java.io.ByteArrayInputStream;

/**
 * Test case for {@link RegexpContentRecogniser}.
 */
public class RegexpContentRecogniserTestCase
        extends RegexpRecogniserTestAbstract {

    // Javadoc inherited.
    protected boolean recognise(String data, String re) {

        // Create the recogniser and run it against the input data.
        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());

        RegexpContentRecogniser recogniser = new RegexpContentRecogniser(re);

        boolean match = recogniser.recogniseContent(bais);
        return match;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/8	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/6	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/4	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
