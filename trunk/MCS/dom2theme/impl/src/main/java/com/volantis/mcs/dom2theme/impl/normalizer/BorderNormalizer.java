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
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Normalizes border properties.
 */
public class BorderNormalizer
        extends AbstractNormalizer {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * A zero length.
     */
    private static final StyleLength ZERO =
        STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX);

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     */
    public BorderNormalizer(ImmutableStylePropertySet supportedProperties) {
        super(supportedProperties);
    }

    // Javadoc inherited.
    public void normalize(MutablePropertyValues inputValues) {

        // If any of the edge style properties are set to none, or any of
        // the widths are set to 0 then the value of any of the other properties
        // for the same edge are irrelevant so mark them as being able to take
        // any value.
        for (int i = 0; i < PropertyGroups.EDGE_COUNT; i++) {
            StyleProperty property;
            StyleValue value;
            boolean ignoreColor = false;
            boolean ignoreStyle = false;
            boolean ignoreWidth = false;

            property = PropertyGroups.BORDER_STYLE_PROPERTIES[i];
            value = getSupportedStyleValue(inputValues,
                    property);
            if (value == BorderStyleKeywords.NONE) {
                ignoreColor = true;
                ignoreWidth = true;
            } else {
                property = PropertyGroups.BORDER_WIDTH_PROPERTIES[i];
                value = getSupportedStyleValue(inputValues,
                        property);
                if (ZERO.equals(value)) {
                    ignoreColor = true;
                    ignoreStyle = true;
                }
            }

            if (ignoreColor) {
                changeValue(inputValues,
                        PropertyGroups.BORDER_COLOR_PROPERTIES[i],
                        OptimizerHelper.ANY);
            }
            if (ignoreStyle) {
                changeValue(inputValues,
                        PropertyGroups.BORDER_STYLE_PROPERTIES[i],
                        OptimizerHelper.ANY);
            }
            if (ignoreWidth) {
                changeValue(inputValues,
                        PropertyGroups.BORDER_WIDTH_PROPERTIES[i],
                        OptimizerHelper.ANY);
            }
        }
    }

}
