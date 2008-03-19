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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;
import com.volantis.mcs.themes.properties.OverflowKeywords;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Identifies whether the marquee properties are relevant (given
 * the values of the display and overflow properties), and if not
 * clears them.
 */
public class MarqueeNormalizer
        extends AbstractNormalizer {

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     */
    public MarqueeNormalizer(ImmutableStylePropertySet supportedProperties) {
        super(supportedProperties);
    }

    // Javadoc inherited.
    public void normalize(MutablePropertyValues inputValues) {

        StyleValue display = inputValues.getComputedValue(
                StylePropertyDetails.DISPLAY);
        StyleValue overflow = inputValues.getComputedValue(
                StylePropertyDetails.OVERFLOW);
        StyleValue marqueeStyle = inputValues.getComputedValue(
                StylePropertyDetails.MCS_MARQUEE_STYLE);

        // The marquee style outputProperties are only relevant if
        // a) the display is set to BLOCK
        // b) marquee style is set to any value but NONE
        // c) overflow is set to SCROLL
        boolean marqueeRequired = ((DisplayKeywords.BLOCK == display) &&
                (MCSMarqueeStyleKeywords.NONE != marqueeStyle) &&
                (OverflowKeywords.SCROLL == overflow));

        if (!marqueeRequired) {
            // Clear the input and output values (just in case another
            // normaliser depends on them).
            inputValues.clearPropertyValue(
                    StylePropertyDetails.MCS_MARQUEE_DIRECTION);
            inputValues.clearPropertyValue(
                    StylePropertyDetails.MCS_MARQUEE_REPETITION);
            inputValues.clearPropertyValue(
                    StylePropertyDetails.MCS_MARQUEE_SPEED);
            inputValues.clearPropertyValue(
                    StylePropertyDetails.MCS_MARQUEE_STYLE);
        } else {
            // The display must be set to the mcs-marquee keyword for the
            // marquee styles to be rendered. This will be removed before
            // rendering by the CSSSelectorFilter if the device doesn't support
            // it.
            inputValues.setComputedValue(StylePropertyDetails.DISPLAY,
                    DisplayKeywords.MCS_MARQUEE);
        }
    }
}
