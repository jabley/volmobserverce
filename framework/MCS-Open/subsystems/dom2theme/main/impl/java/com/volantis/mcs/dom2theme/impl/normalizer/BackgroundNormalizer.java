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

import com.volantis.mcs.dom2theme.impl.optimizer.OptimizerHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Normalize background image related properties.
 */
public class BackgroundNormalizer
        extends AbstractNormalizer {

    /**
     * The set of properties that are only required if a background image
     * is specified.
     */
    private static final StyleProperty[] IMAGE_RELATED = new StyleProperty[]{
        StylePropertyDetails.BACKGROUND_ATTACHMENT,
        StylePropertyDetails.BACKGROUND_POSITION,
        StylePropertyDetails.BACKGROUND_REPEAT,
    };

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     */
    public BackgroundNormalizer(ImmutableStylePropertySet supportedProperties) {
        super(supportedProperties);
    }

    // Javadoc inherited.
    public void normalize(MutablePropertyValues inputValues) {

        // Before analysing the standard properties need to determine whether
        // or not to use a background dynamic visual or not.
        StyleValue image = inputValues.getComputedValue(
                StylePropertyDetails.BACKGROUND_IMAGE);

        // If no image was found then a number of the other properties
        // have no effect so clear them. The only property left is color so
        // if that cannot be thrown away then create a shorthand containing
        // only the color.
        if (image == null || image == BackgroundImageKeywords.NONE) {
            for (int i = 0; i < IMAGE_RELATED.length; i++) {
                StyleProperty property = IMAGE_RELATED[i];
                if (supportedProperties.contains(property)) {
                    inputValues.setComputedValue(property, OptimizerHelper.ANY);
                }
            }
        }
    }
}
