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
import com.volantis.mcs.themes.properties.MCSMMFlashXScaledAlignKeywords;
import com.volantis.mcs.themes.properties.MCSMMFlashYScaledAlignKeywords;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test for {@link MCSMMFlashScaledAlignParser}.
 */
public class MCSMMFlashScaledAlignParserTestCase
        extends ParsingPropertiesMockTestCaseAbstract {

    private static final StyleValue X_CENTER = MCSMMFlashXScaledAlignKeywords.CENTER;

    private static final StyleValue Y_CENTER = MCSMMFlashYScaledAlignKeywords.CENTER;

    public static TestSuite suite() {

        TestSuite suite = new TestSuite();

        suite.addTestSuite(MCSMMFlashScaledAlignParserTestCase.class);

        StyleValue[] xValues = new StyleValue[]{
            MCSMMFlashXScaledAlignKeywords.LEFT,
            MCSMMFlashXScaledAlignKeywords.CENTER,
            MCSMMFlashXScaledAlignKeywords.RIGHT,
        };

        String[] xKeywords = new String[]{
            "left", "center", "right"
        };

        StyleValue[] yValues = new StyleValue[]{
            MCSMMFlashYScaledAlignKeywords.CENTER,
            MCSMMFlashYScaledAlignKeywords.TOP,
            MCSMMFlashYScaledAlignKeywords.CENTER,
            MCSMMFlashYScaledAlignKeywords.BOTTOM,
        };

        String[] yKeywords = new String[]{
            null, "top", "center", "bottom"
        };

        for (int x = 0; x < xKeywords.length; x += 1) {
            for (int y = 0; y < yKeywords.length; y += 1) {

                String xKeyword = xKeywords[x];
                String yKeyword = yKeywords[y];

                Test test = new KeywordCombination(
                        xKeyword, yKeyword, xValues[x], yValues[y]);
                suite.addTest(test);
            }
        }

        return suite;
    }

    /**
     * Make sure that a single keyword works properly.
     */
    public void testSingleKeywordValue() throws Exception {

        StyleValue expected = STYLE_VALUE_FACTORY.getPair(X_CENTER, Y_CENTER);

        expectSetProperty(StylePropertyDetails.MCS_MMFLASH_SCALED_ALIGN,
                          expected);

        parseDeclarations("mcs-mmflash-scaled-align: center");
    }

    private static class KeywordCombination
            extends ParsingPropertiesMockTestCaseAbstract {

        private final String css;
        private final StyleValue xValue;
        private final StyleValue yValue;

        public KeywordCombination(
                String xKeyword, String yKeyword,
                StyleValue xValue, StyleValue yValue) {

            this.xValue = xValue;
            this.yValue = yValue;

            StringBuffer buffer;
            buffer = new StringBuffer("mcs-mmflash-scaled-align:");
            if (xKeyword != null) {
                buffer.append(" ").append(xKeyword);
            }
            if (yKeyword != null) {
                buffer.append(" ").append(yKeyword);
            }
            css = buffer.toString();

            setName("test - '" + css + "'");
        }

        protected void runTest() throws Throwable {

            StyleValue pair;

            pair = STYLE_VALUE_FACTORY.getPair(xValue, yValue);
            expectSetProperty(StylePropertyDetails.MCS_MMFLASH_SCALED_ALIGN,
                              pair);

            parseDeclarations(css);
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 28-Sep-05	9487/1	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
