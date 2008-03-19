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
package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DirectionStyleKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Given a set of prpoerty values translate some values to other values
 * based on a second property value.
 */
public class PropertyValueTranslator {

    /**
     * Translate specific property values to others.
     *
     * Currenlty this only translates text-align start and end to left and
     * right so is hard coded. If we extend this functionality we could use
     * a more generic approach.
     *
     * @param values to be searched and updated.
     */
    public void translate(MutablePropertyValues values) {

        StyleValue align = values.getComputedValue(StylePropertyDetails.TEXT_ALIGN);
        StyleValue direction = values.getComputedValue(StylePropertyDetails.DIRECTION);

        StyleValue newAlign = null;

        if (align != null) {
            if (align == TextAlignKeywords.START) {
                if (direction == DirectionStyleKeywords.LTR) {
                    newAlign = TextAlignKeywords.LEFT;
                } else if (direction == DirectionStyleKeywords.RTL) {
                    newAlign = TextAlignKeywords.RIGHT;
                }
            } else if (align == TextAlignKeywords.END) {
                if (direction == DirectionStyleKeywords.LTR) {
                    newAlign = TextAlignKeywords.RIGHT;
                } else if (direction == DirectionStyleKeywords.RTL) {
                    newAlign = TextAlignKeywords.LEFT;
                }

            }

            if (newAlign != null) {
                values.setComputedValue(StylePropertyDetails.TEXT_ALIGN, newAlign);
            }
        }

    }
}
