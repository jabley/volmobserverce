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
 * Simple test case to ensure that DefaultCanvasDetails returns the
 * information it was instanciated with.
 */
public class DefaultCanvasDetailsTestCase extends TestCaseAbstract {

    /**
     * Standard test case constructor.
     */
    public DefaultCanvasDetailsTestCase(String name) {
        super(name);
    }

    /**
     * Perform simple test to ensure that the DefaultCanvasDetails object
     * returns the values it was instanciated with.
     */
    public void testDefaultCanvasDetails() throws Exception {
        //set up a DefaultPageDetails object
        CanvasDetails canvasDetails = new DefaultCanvasDetails(
                "myTitle", CanvasType.GEAR, "myTheme", "myLayout");
        doComparison(canvasDetails, "myTitle", CanvasType.GEAR,
                "myTheme", "myLayout");
    }

    /**
     * Utility method to compare a given CanvasDetails object against known
     * values.
     * @param canvasDetails the canvas details to compare.
     * @param title the title of the canvas.
     * @param canvasType the type of the canvas.
     * @param themeName the name of the associated theme.
     * @param layoutName the name of the associated layout.
     */
    public static void doComparison(CanvasDetails canvasDetails, String title,
                                    CanvasType canvasType, String themeName,
                                    String layoutName) {

        assertEquals("Is the title what we expect", title,
                canvasDetails.getTitle());
        assertEquals("Is the canvas type what we expect", canvasType,
                canvasDetails.getCanvasType());
        assertEquals("Is the theme name what we expect", themeName,
                canvasDetails.getThemeName());
        assertEquals("Is the layout name what we expect", layoutName,
                canvasDetails.getLayoutName());
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
