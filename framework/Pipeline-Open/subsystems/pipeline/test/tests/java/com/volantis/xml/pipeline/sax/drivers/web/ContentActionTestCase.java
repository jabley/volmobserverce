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

package com.volantis.xml.pipeline.sax.drivers.web;

import junit.framework.TestCase;

/**
 * Test the <code>ContentAction</code> object.
 */
public class ContentActionTestCase extends TestCase {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    public void testGetContentAction() throws Exception {

        ContentAction result = ContentAction.getContentAction(null);
        assertNull("Result should be null", result);

        result = ContentAction.getContentAction("ignore");
        assertNotNull("Result should not be null", result);
        assertEquals("Result should match", ContentAction.IGNORE, result);

        result = ContentAction.getContentAction("consume");
        assertNotNull("Result should not be null", result);
        assertEquals("Result should match", ContentAction.CONSUME, result);

        result = ContentAction.getContentAction("should not be found");
        assertNull("Result should be null", result);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit

 ===========================================================================
*/
