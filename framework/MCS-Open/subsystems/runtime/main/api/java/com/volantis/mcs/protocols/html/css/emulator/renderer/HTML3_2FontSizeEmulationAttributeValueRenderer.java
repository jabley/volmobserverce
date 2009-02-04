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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationAttributeValueRenderer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Renders a 'font-size' style value as an emulated attribute value for HTML 
 * 3.2.
 * <p>
 * The emulated attribute value is calculated by translating a length or 
 * keyword into the related HTML font sizes '1' - '7' using lookup tables.
 */ 
public class HTML3_2FontSizeEmulationAttributeValueRenderer 
        implements StyleEmulationAttributeValueRenderer {

    /**
     * String representations of valid font sizes.
     */
    private static final String[] STRING_FONT_SIZES =
            {"1", "2", "3", "4", "5", "6", "7"};

    /**
     * An array of point-font-size to string-font-size mappings. The mapping is
     * implicit insofar as a point-font-size maps to a the value of the index
     * in the {@link #STRING_FONT_SIZES} array where the index is determined by
     * the value in this array that is greater than the point-size value.<p>
     *
     * For example, a point font size of 18 is greater than the last value in
     * this lookup array, which is 16, and therefore the corresponding string
     * value is '7' (the index of 5 is used + 1). Similarly, 13 maps to '6'.
     */
    private static final int[] POINT_FONT_SIZE_LOOKUP =
            {6, 7, 8, 10, 12, 16};

    /**
     * An array of pixels-font-size to string-font-size mappings. The mapping is
     * implicit insofar as a point-font-size maps to a the value of the index
     * in the {@link #STRING_FONT_SIZES} array where the index is determined by
     * the value in this array that is greater than the point-size value.<p>
     *
     * For example, a point font size of 30 is greater than the last value in
     * this lookup array, which is 29, and therefore the corresponding string
     * value is '7' (the index of 5 is used + 1). Similarly, 24 maps to '6'.
     */
    private static final int[] PIXELS_FONT_SIZE_LOOKUP =
            {9, 11, 12, 17, 22, 29};

    /**
     * The use of relative values, for example percentage/em/ex sizes or
     * bolder/lighter font weights, will not be covered by this
     * assignment.<p/>
     *
     * Also, if the point size is returned a double value which is always
     * rounded to the nearest whole integer and then compared to the map of
     * font sizes in order to obtain the correct value (1, 2, 3, 4, 5, 6 or
     * 7).
     *
     * @param value the value for the font size.
     * @return the font size extracted from the style properties.
     */
    public String render(StyleValue value) {

        String result = null;
        if (value instanceof StyleKeyword) {
            // React to KEYWORD values.
            result = getFontSizeFromKeyword((StyleKeyword) value);
        } else if (value instanceof StyleLength) {
            // React to LENGTH values. Note that percentage/em/ex sizes
            // and bolder/lighter are explicitly not handled here.
            StyleLength styleLength = (StyleLength) value;
            if (styleLength.getUnit() == LengthUnit.PX) {
                // Pixels
                int pixels = styleLength.pixels();
                result = determineFontSize(pixels,
                        PIXELS_FONT_SIZE_LOOKUP);
            } else if (styleLength.getUnit() == LengthUnit.PT) {
                // Points.
                double point = styleLength.getNumber();
                result = determineFontSize(Math.round(point),
                        POINT_FONT_SIZE_LOOKUP);
            }
        }
        return result;
    }

    /**
     * Get the font size from the style keyword.
     *
     * @param styleKeyword
     *         the style keyword.
     * @return the font size from the style keyword.
     */
    private String getFontSizeFromKeyword(StyleKeyword styleKeyword) {
        
        String fontSize = null;
        if (styleKeyword == FontSizeKeywords.XX_SMALL) {
                fontSize = "1";
        } else if (styleKeyword == FontSizeKeywords.X_SMALL) {
                fontSize = "2";
        } else if (styleKeyword == FontSizeKeywords.SMALL) {
                fontSize = "3";
        } else if (styleKeyword == FontSizeKeywords.MEDIUM) {
                fontSize = "4";
        } else if (styleKeyword == FontSizeKeywords.LARGE) {
                fontSize = "5";
        } else if (styleKeyword == FontSizeKeywords.X_LARGE) {
                fontSize = "6";
        } else if (styleKeyword == FontSizeKeywords.XX_LARGE) {
                fontSize = "7";
        } else if (styleKeyword == FontSizeKeywords.SMALLER) {
                fontSize = "-1";
        } else if (styleKeyword == FontSizeKeywords.LARGER) {
                fontSize = "+1";
        }
        return fontSize;
    }

    /**
     * Helper method to determine the font size form the value and array.
     *
     * @param value  the value of the font size used to match against the value
     *               in the the array.
     * @param lookup the array of lookup values.
     * @return the String value of the font size as determined using the lookup
     *         array.
     */
    protected String determineFontSize(long value, final int[] lookup) {
        
        String fontSize = null;
        // Start at the end of the array and work backwards.
        for (int i = lookup.length - 1; fontSize == null && i >= 0; i--) {
            if (value > lookup[i]) {
                fontSize = STRING_FONT_SIZES[i + 1];
            }
        }
        if (fontSize == null) {
            fontSize = STRING_FONT_SIZES[0];
        }
        return fontSize;
    }

    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
