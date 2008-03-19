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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetMock;

/**
 * Test some general style sheet parsing issues.
 */
public class StyleSheetParsingTestCase
        extends ParsingMockTestCaseAbstract {

    protected StyleSheetMock styleSheetMock;

    protected void setUp() throws Exception {
        super.setUp();

        styleSheetMock = new StyleSheetMock("styleSheetMock", expectations);

        styleSheetFactoryMock.expects.createStyleSheet()
                .returns(styleSheetMock);
    }

    /**
     * Test that parsing an empty CSS returns an empty style sheet.
     */
    public void testEmptyCSS() {

        StyleSheet styleSheet = parseStyleSheet("");

        assertNotNull("Style sheet should not be null", styleSheet);
        assertEquals("Style sheet must match", styleSheetMock, styleSheet);
    }

    /**
     * Test that parsing white space returns an empty style sheet.
     */
    public void testWhiteSpaceCSS() {

        StyleSheet styleSheet = parseStyleSheet(" \t\n\f");

        assertNotNull("Style sheet should not be null", styleSheet);
        assertEquals("Style sheet must match", styleSheetMock, styleSheet);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
