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
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * Test the value renderer for font size.
 */
public class HTML3_2FontSizeEmulationAttributeValueRendererTestCase 
        extends TestCaseAbstract {

    /**
     * Test the determine font size method for Points.
     */
    public void testDetermineFontSizePoints() throws Exception {
        HTML3_2FontSizeEmulationAttributeValueRenderer valueRenderer = new 
                HTML3_2FontSizeEmulationAttributeValueRenderer();
        final int array[] = (int[]) PrivateAccessor.getField(
                HTML3_2FontSizeEmulationAttributeValueRenderer.class,
                "POINT_FONT_SIZE_LOOKUP");

        for (int i = -10; i < 50; i++) {
            String actual = valueRenderer.determineFontSize(i, array);
            if (i > 16) {
                assertEquals("Value for " + i + " should match", "7", actual);
            } else if (i > 12) {
                assertEquals("Value for " + i + " should match", "6", actual);
            } else if (i > 10) {
                assertEquals("Value for " + i + " should match", "5", actual);
            } else if (i > 8) {
                assertEquals("Value for " + i + " should match", "4", actual);
            } else if (i > 7) {
                assertEquals("Value for " + i + " should match", "3", actual);
            } else if (i > 6) {
                assertEquals("Value for " + i + " should match", "2", actual);
            } else {
                assertEquals("Value for " + i + " should match", "1", actual);
            }
        }
    }

    /**
     * Test the determine font size method for Pixels.
     */
    public void testDetermineFontSizePixels() throws Exception {
        HTML3_2FontSizeEmulationAttributeValueRenderer valueRenderer = new 
                HTML3_2FontSizeEmulationAttributeValueRenderer();
        final int array[] = (int[]) PrivateAccessor.getField(
                HTML3_2FontSizeEmulationAttributeValueRenderer.class,
                "PIXELS_FONT_SIZE_LOOKUP");

        for (int i = -10; i < 50; i++) {
            String actual = valueRenderer.determineFontSize(i, array);
            if (i > 29) {
                assertEquals("Value for " + i + " should match", "7", actual);
            } else if (i > 22) {
                assertEquals("Value for " + i + " should match", "6", actual);
            } else if (i > 17) {
                assertEquals("Value for " + i + " should match", "5", actual);
            } else if (i > 12) {
                assertEquals("Value for " + i + " should match", "4", actual);
            } else if (i > 11) {
                assertEquals("Value for " + i + " should match", "3", actual);
            } else if (i > 9) {
                assertEquals("Value for " + i + " should match", "2", actual);
            } else {
                assertEquals("Value for " + i + " should match", "1", actual);
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 29-Jun-04	4720/6	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 29-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 ===========================================================================
*/
