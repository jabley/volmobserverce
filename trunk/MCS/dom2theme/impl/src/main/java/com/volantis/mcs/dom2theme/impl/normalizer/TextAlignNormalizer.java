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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Discards text-align settings on inline elements.
 */
public class TextAlignNormalizer
        extends AbstractNormalizer {

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     */
    public TextAlignNormalizer(ImmutableStylePropertySet supportedProperties) {
        super(supportedProperties);
    }

    public void normalize(MutablePropertyValues inputValues) {
        StyleValue display = getSupportedStyleValue(inputValues,
                StylePropertyDetails.DISPLAY);

        if (display == DisplayKeywords.INLINE ||
                inputValues.shouldExcludeFromCSS(
                        StylePropertyDetails.TEXT_ALIGN)) {
            inputValues.clearPropertyValue(StylePropertyDetails.TEXT_ALIGN);
        }
    }
}
