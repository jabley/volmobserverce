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
package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.DirectionAttribute;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Helper class
 */
public class DirectionHelper {

    static String L2R = "l2r";
    static String R2L = "r2l";
    static String FIXED = "fixed";

    /**
     * Given a layout element with directionality and the styles for the
     * element determin if the layout needs to be reversed (left to right).
     *
     * @param layout element to check
     * @param formatStyles styles form the layout element
     * @return false iff layout fixed, or layout matches style otherwise
     *               returns true.
     */
    public static boolean isDirectionReversed(DirectionAttribute layout,
        Styles formatStyles) {

        String layoutDirectionality = layout.getDirectionality();

        if (layoutDirectionality == null) {
            layoutDirectionality = FIXED;
        }

        MutablePropertyValues values = formatStyles.getPropertyValues();

        StyleValue direction =
            values.getSpecifiedValue(StylePropertyDetails.DIRECTION);

        if (direction == null) {
            direction =
                values.getComputedValue(StylePropertyDetails.DIRECTION);
        }

        if (direction == null) {
            // This should not happen !!!
            throw new IllegalStateException("No direction style found");
        }

        // If the layout is fixed or the layout directionality matches the
        // style direction then the layout does not need to be reversed
        // otherwise we will need to reverse it.
        return !(layoutDirectionality.equals(FIXED) ||
          direction == StyleKeywords.LTR && layoutDirectionality.equals(L2R) ||
          direction == StyleKeywords.RTL && layoutDirectionality.equals(R2L));

    }
}
