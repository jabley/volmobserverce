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

import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;

/**
 * Test {@link BorderParser}.
 */
public class BorderParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    private static StyleValue KEYWORD_SOLID = BorderStyleKeywords.SOLID;

    private static StyleValue KEYWORD_THIN = BorderWidthKeywords.THIN;

    /**
     * Test that inherit sets the values properly.
     */
    public void testInherit()
            throws Exception {

        StyleValue inherit = STYLE_VALUE_FACTORY.getInherit();
        expectProperties(new StyleValue[] {
            inherit,
            inherit,
            inherit,
            inherit,
        }, PropertyGroups.BORDER_COLOR_PROPERTIES);
        expectProperties(new StyleValue[] {
            inherit,
            inherit,
            inherit,
            inherit,
        }, PropertyGroups.BORDER_STYLE_PROPERTIES);
        expectProperties(new StyleValue[] {
            inherit,
            inherit,
            inherit,
            inherit,
        }, PropertyGroups.BORDER_WIDTH_PROPERTIES);

        parseDeclarations("border: inherit");
    }

    /**
     * Test that separate color, width and style work.
     */
    public void testColorWidthStyle()
            throws Exception {

        expectProperties(new StyleValue[] {
            COLOR_GREEN,
            COLOR_GREEN,
            COLOR_GREEN,
            COLOR_GREEN,
        }, PropertyGroups.BORDER_COLOR_PROPERTIES);
        expectProperties(new StyleValue[] {
            KEYWORD_SOLID,
            KEYWORD_SOLID,
            KEYWORD_SOLID,
            KEYWORD_SOLID,
        }, PropertyGroups.BORDER_STYLE_PROPERTIES);
        expectProperties(new StyleValue[] {
            KEYWORD_THIN,
            KEYWORD_THIN,
            KEYWORD_THIN,
            KEYWORD_THIN,
        }, PropertyGroups.BORDER_WIDTH_PROPERTIES);

        parseDeclarations("border: green thin solid");
    }

    /**
     * Test that separate width and style work.
     */
    public void testWidthStyle()
            throws Exception {

        expectProperties(new StyleValue[] {
            null,
            null,
            null,
            null,
        }, PropertyGroups.BORDER_COLOR_PROPERTIES);
        expectProperties(new StyleValue[] {
            KEYWORD_SOLID,
            KEYWORD_SOLID,
            KEYWORD_SOLID,
            KEYWORD_SOLID,
        }, PropertyGroups.BORDER_STYLE_PROPERTIES);
        expectProperties(new StyleValue[] {
            KEYWORD_THIN,
            KEYWORD_THIN,
            KEYWORD_THIN,
            KEYWORD_THIN,
        }, PropertyGroups.BORDER_WIDTH_PROPERTIES);

        parseDeclarations("border: thin solid");
    }

    /**
     * Test that separate color and style work.
     */
    public void testColorStyle()
            throws Exception {

        expectProperties(new StyleValue[] {
            COLOR_GREEN,
            COLOR_GREEN,
            COLOR_GREEN,
            COLOR_GREEN,
        }, PropertyGroups.BORDER_COLOR_PROPERTIES);
        expectProperties(new StyleValue[] {
            KEYWORD_SOLID,
            KEYWORD_SOLID,
            KEYWORD_SOLID,
            KEYWORD_SOLID,
        }, PropertyGroups.BORDER_STYLE_PROPERTIES);
        expectProperties(new StyleValue[] {
            null,
            null,
            null,
            null,
        }, PropertyGroups.BORDER_WIDTH_PROPERTIES);

        parseDeclarations("border: green solid");
    }

    /**
     * Test that separate width and color work.
     */
    public void testWidthColor()
            throws Exception {

        expectProperties(new StyleValue[] {
            COLOR_GREEN,
            COLOR_GREEN,
            COLOR_GREEN,
            COLOR_GREEN,
        }, PropertyGroups.BORDER_COLOR_PROPERTIES);
        expectProperties(new StyleValue[] {
            null,
            null,
            null,
            null,
        }, PropertyGroups.BORDER_STYLE_PROPERTIES);
        expectProperties(new StyleValue[] {
            KEYWORD_THIN,
            KEYWORD_THIN,
            KEYWORD_THIN,
            KEYWORD_THIN,
        }, PropertyGroups.BORDER_WIDTH_PROPERTIES);

        parseDeclarations("border: thin green");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
