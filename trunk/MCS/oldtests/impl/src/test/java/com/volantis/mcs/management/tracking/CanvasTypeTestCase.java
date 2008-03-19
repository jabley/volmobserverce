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
package com.volantis.mcs.management.tracking;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests to ensure that the CanvasType class instanciates the correct
 * enum elements.
 */
public class CanvasTypeTestCase extends TestCaseAbstract {

    public CanvasTypeTestCase(String name) {
        super(name);
    }

    /**
     * Ensure that the CanvasType enums are properly instanciated and have the
     * correct internal names.
     * @throws Exception
     */
    public void testEnumsInstanciated() throws Exception {

        assertEquals("montage", CanvasType.literal("montage").toString());
        assertEquals("main", CanvasType.literal("main").toString());
        assertEquals("gear", CanvasType.literal("gear").toString());
        assertEquals("portal", CanvasType.literal("portal").toString());
        assertEquals("portlet", CanvasType.literal("portlet").toString());
        assertEquals("message", CanvasType.literal("message").toString());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4702/1	matthew	VBM:2004061402 rework JMXPageTrackerFactory error handling

 11-Jun-04	4689/1	matthew	VBM:2004060706 add basic PageTracking facilities

 ===========================================================================
*/
