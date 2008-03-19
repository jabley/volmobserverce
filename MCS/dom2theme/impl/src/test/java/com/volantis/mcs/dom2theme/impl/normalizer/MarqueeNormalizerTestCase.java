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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.impl.normalizer;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;
import com.volantis.mcs.themes.properties.OverflowKeywords;
import com.volantis.styling.values.MutablePropertyValuesMock;

/**
 * Tests for {@link MarqueeNormalizer}.
 */
public class MarqueeNormalizerTestCase
        extends PropertiesNormalizerTestAbstract {

    private MutablePropertyValuesMock inputPropertyValues;

    protected void setUp() throws Exception {
        super.setUp();

        inputPropertyValues = new MutablePropertyValuesMock(
                "inputPropertyValues", expectations);
    }

    /**
     * Verify that if the {@link StylePropertyDetails.DISPLAY} property is not
     * set to BLOCK, any marquee properties that have been set are cleared.
     */
    public void testNormalizeDisplayNotBlock() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.DISPLAY).returns(DisplayKeywords.INLINE);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.OVERFLOW).returns(OverflowKeywords.SCROLL);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE).returns(
                        MCSMarqueeStyleKeywords.ALTERNATE);


        inputPropertyValues.expects.clearPropertyValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE);

        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_DIRECTION);

        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_REPETITION);

        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_SPEED);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MarqueeNormalizer normalizer = new MarqueeNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputPropertyValues);
    }

    /**
     * Verify that if the {@link StylePropertyDetails.OVERFLOW} property is set
     * to a value other than SCROLL, any marquee properties that have been set
     * are cleared.
     */
    public void testNormalizeOverflowNotScroll() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.DISPLAY).returns(DisplayKeywords.BLOCK);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.OVERFLOW).returns(OverflowKeywords.VISIBLE);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE).returns(
                        MCSMarqueeStyleKeywords.ALTERNATE);

        inputPropertyValues.expects.clearPropertyValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE);
        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_DIRECTION);

        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_REPETITION);

        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_SPEED);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MarqueeNormalizer normalizer = new MarqueeNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputPropertyValues);
    }

    /**
     * Verify that if the {@link StylePropertyDetails.MCS_MARQUEE_STYLE} property
     * is set to NONE, any marquee properties that have been set are cleared.
     */
    public void testNormalizeMarqueeStyleNone() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.DISPLAY).returns(DisplayKeywords.BLOCK);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.OVERFLOW).returns(OverflowKeywords.SCROLL);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE).returns(
                        MCSMarqueeStyleKeywords.NONE);

        inputPropertyValues.expects.clearPropertyValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE);
        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_DIRECTION);

        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_REPETITION);

        inputPropertyValues.expects.clearPropertyValue(
                        StylePropertyDetails.MCS_MARQUEE_SPEED);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MarqueeNormalizer normalizer = new MarqueeNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputPropertyValues);
    }

    /**
     * Verify that if the {@link StylePropertyDetails.DISPLAY} property is set
     * to BLOCK, {@link StylePropertyDetails.OVERFLOW} is either not set or set
     * to SCROLL, and {@link StylePropertyDetails.MCS_MARQUEE_STYLE} is not set
     * to none, then then the marquee properties that have been set are not
     * cleared.
     */
    public void testNormalizeMarqueeRequired() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.DISPLAY).returns(DisplayKeywords.BLOCK);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.OVERFLOW).returns(OverflowKeywords.SCROLL);
        inputPropertyValues.expects.getComputedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE).returns(
                        MCSMarqueeStyleKeywords.ALTERNATE);

        inputPropertyValues.expects.setComputedValue(
                    StylePropertyDetails.DISPLAY, DisplayKeywords.MCS_MARQUEE);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MarqueeNormalizer normalizer = new MarqueeNormalizer(ALL_PROPERTIES);
        normalizer.normalize(inputPropertyValues);
    }
}
