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

import com.volantis.mcs.themes.MutableStylePropertiesMock;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.BackgroundXPositionKeywords;
import com.volantis.mcs.themes.properties.BackgroundYPositionKeywords;

/**
 * Test {@link BackgroundPositionParser}.
 */
public class BackgroundPositionParserTestCase
        extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Make sure that a single length works properly.
     */
    public void testSingleLengthValue() throws Exception {

        StyleValue expected = STYLE_VALUE_FACTORY.getPair(LENGTH_1PX,
                                                          PERCENTAGE_50);

        expectSetProperty(StylePropertyDetails.BACKGROUND_POSITION, expected);

        parseDeclarations("background-position: 1px");
    }

    /**
     * Make sure that a single percentage works properly.
     */
    public void testSinglePercentageValue() throws Exception {

        StyleValue expected = STYLE_VALUE_FACTORY.getPair(PERCENTAGE_25,
                                                          PERCENTAGE_50);

        expectSetProperty(StylePropertyDetails.BACKGROUND_POSITION, expected);

        parseDeclarations("background-position: 25%");
    }

    /**
     * Make sure that all combinations of the keyworws work properly.
     */
    public void testKeywords() throws Exception {

        StyleValue[] xValues = new StyleValue[]{
            BackgroundXPositionKeywords.CENTER,
            BackgroundXPositionKeywords.LEFT,
            BackgroundXPositionKeywords.CENTER,
            BackgroundXPositionKeywords.RIGHT,
        };

        String[] xKeywords = new String[]{
            null, "left", "center", "right"
        };

        StyleValue[] yValues = new StyleValue[]{
            BackgroundYPositionKeywords.CENTER,
            BackgroundYPositionKeywords.TOP,
            BackgroundYPositionKeywords.CENTER,
            BackgroundYPositionKeywords.BOTTOM,
        };

        String[] yKeywords = new String[]{
            null, "top", "center", "bottom"
        };

        // Consume the default expectation to create style properties.
        styleSheetFactoryMock.createStyleProperties();

        for (int x = 0; x < xKeywords.length; x += 1) {
            for (int y = 0; y < yKeywords.length; y += 1) {

                String xKeyword = xKeywords[x];
                String yKeyword = yKeywords[y];

                // If there are no keywords then skip onto the next.
                if (xKeyword == null && yKeyword == null) {
                    continue;
                }

                // Try x first, then y.
                doTestCombination(xKeyword, yKeyword,
                                  xValues[x], yValues[y]);

                // Swap them around if there is more than one.
                if (xKeyword != null && yKeyword != null) {
                    doTestCombination(yKeyword, xKeyword,
                                      xValues[x], yValues[y]);
                }
            }
        }
    }


    /**
     * Make sure that all combinations of the keyworws work properly.
     */
    public void testMixedTypes() throws Exception {

        StyleValue[] firstValues = new StyleValue[]{
            LENGTH_1PX,
            PERCENTAGE_25,
            BackgroundXPositionKeywords.LEFT,
            BackgroundXPositionKeywords.CENTER,
            BackgroundXPositionKeywords.RIGHT,
        };

        String[] firstKeywords = new String[]{
            "1px", "25%", "left", "center", "right"
        };

        StyleValue[] secondValues = new StyleValue[]{
            LENGTH_2CM,
            PERCENTAGE_50,
            BackgroundYPositionKeywords.TOP,
            BackgroundYPositionKeywords.CENTER,
            BackgroundYPositionKeywords.BOTTOM,
        };

        String[] secondKeywords = new String[]{
            "2cm", "50%", "top", "center", "bottom"
        };

        // Consume the default expectation to create style properties.
        styleSheetFactoryMock.createStyleProperties();

        for (int x = 0; x < firstKeywords.length; x += 1) {
            for (int y = 0; y < secondKeywords.length; y += 1) {

                String firstKeyword = firstKeywords[x];
                String secondKeyword = secondKeywords[y];

                // If there are no keywords then skip onto the next.
                if (firstKeyword == null && secondKeyword == null) {
                    continue;
                }

                doTestCombination(firstKeyword, secondKeyword,
                                  firstValues[x], secondValues[y]);
            }
        }
    }

    private void doTestCombination(
            String firstKeyword, String secondKeyword,
            final StyleValue xValue,
            final StyleValue yValue) {

        StringBuffer buffer;
        StyleValue pair;
        buffer = new StringBuffer("background-position:");
        if (firstKeyword != null) {
            buffer.append(" ").append(firstKeyword);
        }
        if (secondKeyword != null) {
            buffer.append(" ").append(secondKeyword);
        }

        // Set up expectations for this test.
        mutableStylePropertiesMock = new MutableStylePropertiesMock(
                "mutableStylePropertiesMock (" + buffer.toString() + ")",
                expectations);
        styleSheetFactoryMock.expects.createStyleProperties()
                .returns(mutableStylePropertiesMock);

        pair = STYLE_VALUE_FACTORY.getPair(xValue, yValue);
        expectSetProperty(StylePropertyDetails.BACKGROUND_POSITION,
                          pair);

        System.out.println("Testing " + buffer.toString());
        parseDeclarations(buffer.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
