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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.values.converters;

/**
 * Converter between color values
 */
public final class ColorConverter {

    private static char [] HEX_DIGITS = new char [] {
      '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Convert a color RGB int value into text.
     *
     * @param rgb The RGB int value.
     * @return The text representation of the color name, or null.
     */
    public static String convertColorRGBToText(int rgb, boolean useTriple) {

        int r0 = (rgb & 0xf00000) >> 20;
        int r1 = (rgb & 0x0f0000) >> 16;
        int g0 = (rgb & 0x00f000) >> 12;
        int g1 = (rgb & 0x000f00) >> 8;
        int b0 = (rgb & 0x0000f0) >> 4;
        int b1 = rgb & 0x00000f;

        final StringBuffer textString = new StringBuffer("#");
        if (useTriple && r0 == r1 && g0 == g1 && b0 == b1) {
            textString.append(HEX_DIGITS[r0]);
            textString.append(HEX_DIGITS[g0]);
            textString.append(HEX_DIGITS[b0]);
        } else {
            textString.append(HEX_DIGITS[r0]);
            textString.append(HEX_DIGITS[r1]);
            textString.append(HEX_DIGITS[g0]);
            textString.append(HEX_DIGITS[g1]);
            textString.append(HEX_DIGITS[b0]);
            textString.append(HEX_DIGITS[b1]);
        }

        return textString.toString();
    }
}
