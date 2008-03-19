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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for {@link FontFamilyParser}.
 */
public class FontFamilyParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Test a single string font.
     */
    public void testSingleString() throws Exception {

        expectSetProperty(StylePropertyDetails.FONT_FAMILY, FONT_ARIAL);

        parseDeclarations("font-family: \"Arial\"");
    }

    /**
     * Test a single keyword font.
     */
    public void testSingleKeyword() throws Exception {

        expectSetProperty(StylePropertyDetails.FONT_FAMILY, FONT_SERIF);

        parseDeclarations("font-family: serif");
    }

    /**
     * Test a single identifier font.
     */
    public void testSingleIdentifier() throws Exception {

        expectSetProperty(StylePropertyDetails.FONT_FAMILY, FONT_COURIER);

        parseDeclarations("font-family: Courier");
    }

    /**
     * Test multiple fonts.
     */
    public void testMultipleFonts() throws Exception {

        expectSetProperty(StylePropertyDetails.FONT_FAMILY, FONT_ARIAL_SERIF);

        parseDeclarations("font-family: \"Arial\", serif");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
