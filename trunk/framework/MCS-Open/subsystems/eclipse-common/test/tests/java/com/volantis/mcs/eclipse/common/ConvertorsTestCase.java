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
package com.volantis.mcs.eclipse.common;

import junit.framework.TestCase;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;


/**
 * This class tests PolicyUtils
 */
public class ConvertorsTestCase extends TestCase {

    public ConvertorsTestCase(String name) {
        super(name);
    }

    protected void setUp()
            throws Exception {

        super.setUp();

        // Make sure a display has been created before running the tests.
        Display.getDefault();
    }

    protected void tearDown()
            throws Exception {

        // Dispose of the previously created display.
        Display current = Display.getCurrent();
        if (current != null) {
            current.dispose();
        }

        super.tearDown();
    }

    /**
     * This tests the Convertors.testRGBToHex(RGB rgb) method
     * @throws Exception
     */
    public void testRGBToHex() throws Exception {
        RGB rgb = new RGB(0, 0, 0);
        String expected = "#000000";
        String testValue = Convertors.RGBToHex(rgb);
        assertEquals(testValue, expected);
        rgb = new RGB(255, 255, 255);
        expected = "#ffffff";
        testValue = Convertors.RGBToHex(rgb);
        assertEquals(testValue, expected);
        rgb = new RGB(0, 0, 255);
        expected = "#0000ff";
        testValue = Convertors.RGBToHex(rgb);
        assertEquals(testValue, expected);
        rgb = new RGB(187, 32, 80);
        expected = "#bb2050";
        testValue = Convertors.RGBToHex(rgb);
        assertEquals(testValue, expected);
    }

    /**
     * This tests the Convertors.testHexToRGB(String hex) method
     * @throws Exception
     */
    public void testHexToRGBT() throws Exception {
        RGB rgb = new RGB(0, 0, 0);
        RGB testValue = Convertors.hexToRGB("#000");
        assertEquals(testValue.red, rgb.red);
        assertEquals(testValue.green, rgb.green);
        assertEquals(testValue.blue, rgb.blue);
        rgb = new RGB(255, 255, 255);
        testValue = Convertors.hexToRGB("#fff");
        assertEquals(testValue.red, rgb.red);
        assertEquals(testValue.green, rgb.green);
        assertEquals(testValue.blue, rgb.blue);
        rgb = new RGB(187, 32, 80);
        testValue = Convertors.hexToRGB("#bb2050");
        assertEquals(testValue.red, rgb.red);
        assertEquals(testValue.green, rgb.green);
        assertEquals(testValue.blue, rgb.blue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Nov-03	1634/1	pcameron	VBM:2003102205 A few changes to ColorSelector

 ===========================================================================
*/
