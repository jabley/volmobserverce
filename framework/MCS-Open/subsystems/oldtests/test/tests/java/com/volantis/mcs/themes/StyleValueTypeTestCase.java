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
package com.volantis.mcs.themes;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for the {@link com.volantis.mcs.themes.StyleValueType} class
 */
public class StyleValueTypeTestCase extends TestCaseAbstract  {

    /**
     * Tests that the {@link StyleValueType#ANGLE} instance is registered
     * with the correct literal
     */
    public void testAngleType() {
        assertEquals("Result not as",
                     StyleValueType.ANGLE,
                     StyleValueType.get("angle"));
    }

    /**
     * Tests that the {@link StyleValueType#COLOR} instance is registered
     * with the correct literal
     */
    public void testColorType() {
        assertEquals("Result not as",
                     StyleValueType.COLOR,
                     StyleValueType.get("color"));
    }

    /**
     * Tests that the {@link StyleValueType#INHERIT} instance is registered
     * with the correct literal
     */
    public void testInheritType() {
        assertEquals("Result not as",
                     StyleValueType.INHERIT,
                     StyleValueType.get("inherit"));
    }

    /**
     * Tests that the {@link StyleValueType#INTEGER} instance is registered
     * with the correct literal
     */
    public void testIntegerType() {
        assertEquals("Result not as",
                     StyleValueType.INTEGER,
                     StyleValueType.get("integer"));
    }

    /**
     * Tests that the {@link StyleValueType#KEYWORD} instance is registered
     * with the correct literal
     */
    public void testKeywordType() {
        assertEquals("Result not as",
                     StyleValueType.KEYWORD,
                     StyleValueType.get("keyword"));
    }

    /**
     * Tests that the {@link StyleValueType#LENGTH} instance is registered
     * with the correct literal
     */
    public void testLengthType() {
        assertEquals("Result not as",
                     StyleValueType.LENGTH,
                     StyleValueType.get("length"));
    }

    /**
     * Tests that the {@link StyleValueType#LIST} instance is registered
     * with the correct literal
     */
    public void testListType() {
        assertEquals("Result not as",
                     StyleValueType.LIST,
                     StyleValueType.get("list"));
    }

    /**
     * Tests that the {@link StyleValueType#COMPONENT_URI} instance is
     * registered with the correct literal
     */
    public void testComponentURIType() {
        assertEquals("Result not as",
                     StyleValueType.COMPONENT_URI,
                     StyleValueType.get("componentURI"));
    }

    /**
     * Tests that the {@link StyleValueType#TRANSCODABLE_URI} instance is
     * registered with the correct literal
     */
    public void testTranscodableURIType() {
        assertEquals("Result not as",
                     StyleValueType.TRANSCODABLE_URI,
                     StyleValueType.get("transcodableURI"));
    }

    /**
     * Tests that the {@link StyleValueType#NUMBER} instance is registered
     * with the correct literal
     */
    public void testNumberType() {
        assertEquals("Result not as",
                     StyleValueType.NUMBER,
                     StyleValueType.get("number"));
    }

    /**
     * Tests that the {@link StyleValueType#PAIR} instance is registered
     * with the correct literal
     */
    public void testPairType() {
        assertEquals("Result not as",
                     StyleValueType.PAIR,
                     StyleValueType.get("pair"));
    }

    /**
     * Tests that the {@link StyleValueType#PERCENTAGE} instance is registered
     * with the correct literal
     */
    public void testPercentageType() {
        assertEquals("Result not as",
                     StyleValueType.PERCENTAGE,
                     StyleValueType.get("percentage"));
    }

    /**
     * Tests that the {@link StyleValueType#STRING} instance is registered
     * with the correct literal
     */
    public void testStringType() {
        assertEquals("Result not as",
                     StyleValueType.STRING,
                     StyleValueType.get("string"));
    }

    /**
     * Tests that the {@link StyleValueType#TIME} instance is registered
     * with the correct literal
     */
    public void testTimeType() {
        assertEquals("Result not as",
                     StyleValueType.TIME,
                     StyleValueType.get("time"));
    }

    /**
     * Tests that the {@link StyleValueType#URI} instance is registered
     * with the correct literal
     */
    public void testURIType() {
        assertEquals("Result not as",
                     StyleValueType.URI,
                     StyleValueType.get("URI"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jan-04	2467/1	doug	VBM:2003112408 Added the StyleType & StylePropertyDetails type-safe enums

 ===========================================================================
*/
