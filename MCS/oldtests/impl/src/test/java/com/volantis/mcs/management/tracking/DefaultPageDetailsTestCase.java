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
 * Simple tests for DefaultPageDetails.
 */
public class DefaultPageDetailsTestCase extends TestCaseAbstract {

    /**
     * Standard test case constructor.
     */
    public DefaultPageDetailsTestCase(String name) {
        super(name);
    }

    /**
     * Perform very simple testing to ensure DefaultPageDetails
     * returns the values it was initialised with.
     */
    public void testDefaultPageDetails() throws Exception {
        //set up a DefaultPageDetails object
        CanvasDetails canvasDetails = new DefaultCanvasDetails(
                "myTile", CanvasType.GEAR, "myTheme", "myLayout");

        String deviceName = "fakeDevice";
        PageDetails pageDetails = new DefaultPageDetails(
                canvasDetails, deviceName);

        doComparison(pageDetails, canvasDetails, deviceName);
    }

    /**
     * Compare the given page details object to the values provided.
     * @param pageDetails the PageDetails instance to compare to.
     * @param canvasDetails the CanvasDetails object to compare with that
     * contained in the PageDetails object. This check tests if the two are
     * the same object.
     * @param deviceName the name of the device assocaited with the PageDetails.
     */
    public static void doComparison(PageDetails pageDetails,
                                    CanvasDetails canvasDetails,
                                    String deviceName) {

        assertSame("CanvasDetails object is the same",
                canvasDetails, pageDetails.getCanvasDetails());
        assertEquals("Device name is the same", deviceName,
                pageDetails.getDeviceName());

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Jun-04	4689/1	matthew	VBM:2004060706 add basic PageTracking facilities

 ===========================================================================
*/
