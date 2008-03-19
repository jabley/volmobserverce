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

package com.volantis.mcs.dom2theme.impl.normalizer;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Tests for {@link BorderNormalizer}.
 */
public class BorderNormalizerTestCase
        extends PropertiesNormalizerTestAbstract {


    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Test that when normalizing properties containing a style of none
     * that the others for that edge are set to any.
     */
    public void testNormalizeStyleNone() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_COLOR,
                StyleColorNames.RED);
        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_STYLE,
                BorderStyleKeywords.NONE);
        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                BorderWidthKeywords.THICK);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BorderNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputValues);

        checkNormalized("border-top-color:ANY;" +
                "border-top-style:none;" +
                "border-top-width:ANY");
    }

    /**
     * Test that when normalizing properties containing a width of 0px.
     * that the others for that edge are set to any.
     */
    public void testNormalizeZeroWidth() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_COLOR,
                StyleColorNames.RED);
        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_STYLE,
                BorderStyleKeywords.SOLID);
        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BorderNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputValues);

        checkNormalized("border-top-color:ANY;" +
                "border-top-style:ANY;" +
                "border-top-width:0");
    }

    /**
     * Test that when normalizing properties containing a width of 0px.
     * that only those properties that have been set are changed.
     */
    public void testNormalizeMissingValues() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_STYLE,
                BorderStyleKeywords.SOLID);
        inputValues.setComputedValue(StylePropertyDetails.BORDER_TOP_WIDTH,
                STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        PropertiesNormalizer normalizer =
                new BorderNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputValues);

        checkNormalized("border-top-style:ANY;" +
                "border-top-width:0");
    }
}
