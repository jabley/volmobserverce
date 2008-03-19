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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Verifies that {@link SerializeStyleValues} behaves as expected.
 */
public class SerializeStyleValuesTestCase extends TestCaseAbstract {

    /**
     * Verify that an integer is correctly converted in a String representing
     * that number in hexadecimal.
     */
    public void testConvertColorRGBToTextRequiringLeadingZeros() {
        final String rgbString = "#3333cc";
        doTestConvertColorRGBToText(rgbString.substring(1), rgbString);
    }

    /**
     * Verify that an integer which maps to a hex string of less than six digits
     * is correctly converted to a String representing a number in hexadecimal
     * with leading zeros.
     */
    public void testConvertColorRGBToTextNotRequiringLeadingZeros() {
        final String rgbString = "#0033cc";
        doTestConvertColorRGBToText(rgbString.substring(1), rgbString);
    }

    /**
     * Verify that zero is correctly converted to a String.
     */
    public void testConvertColorRGBToTextForZero() {
        final String rgbString = "#000000";
        doTestConvertColorRGBToText(rgbString.substring(1), rgbString);
    }

    /**
     * Test #convertColorRGBToText.
     * @param expected      expected result of converting RGB to text
     */
    public void doTestConvertColorRGBToText(String input, String expected) {
        final int rgb = Integer.parseInt(input ,16);
        assertEquals(expected,
                SerializeStyleValues.convertColorRGBToText(rgb));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Oct-05	9946/2	emma	VBM:2005102007 Fixing problem with serializing colorRGB values

 21-Oct-05	9942/2	emma	VBM:2005102007 Fixing problem with serializing colorRGB values

 ===========================================================================
*/
