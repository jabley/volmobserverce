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
import com.volantis.mcs.themes.properties.BackgroundAttachmentKeywords;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.mcs.themes.properties.BackgroundRepeatKeywords;
import com.volantis.mcs.themes.properties.BackgroundXPositionKeywords;
import com.volantis.mcs.themes.properties.BackgroundYPositionKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;

/**
 * Tests for {@link BackgroundNormalizer}.
 */
public class BackgroundNormalizerTestCase
        extends PropertiesNormalizerTestAbstract {

    private void setNonImageBackgroundProperties() {
        inputValues.setComputedValue(
                StylePropertyDetails.BACKGROUND_ATTACHMENT,
                BackgroundAttachmentKeywords.FIXED);

        inputValues.setComputedValue(
                StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.RED);

        inputValues.setComputedValue(
                StylePropertyDetails.BACKGROUND_POSITION,
                STYLE_VALUE_FACTORY.getPair(BackgroundXPositionKeywords.CENTER,
                        BackgroundYPositionKeywords.BOTTOM));

        inputValues.setComputedValue(
                StylePropertyDetails.BACKGROUND_REPEAT,
                BackgroundRepeatKeywords.REPEAT);
    }

    /**
     * Test that when normalizing properties containing no image that the
     * properties are set to any.
     */
    public void testNormalizeNoImage() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // There is no background-image.
        inputValues.setComputedValue(StylePropertyDetails.BACKGROUND_IMAGE,
                BackgroundImageKeywords.NONE);

        setNonImageBackgroundProperties();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BackgroundNormalizer normalizer =
                new BackgroundNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputValues);

        checkNormalized("background-attachment:ANY;background-color:red;" +
                "background-image:none;background-position:ANY;" +
                "background-repeat:ANY");
    }

    /**
     * Test that when normalizing properties containing and image that the
     * properties are not modified.
     */
    public void testNormalizeImage() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // There is no background-image.
        inputValues.setComputedValue(StylePropertyDetails.BACKGROUND_IMAGE,
            STYLE_VALUE_FACTORY.getURI(null, "/fred.gif"));

        setNonImageBackgroundProperties();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BackgroundNormalizer normalizer =
                new BackgroundNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputValues);

        checkNormalized("background-attachment:fixed;" +
                "background-color:red;background-image:url(/fred.gif);" +
                "background-position:center bottom;" +
                "background-repeat:repeat");
    }
}
