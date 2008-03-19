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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;

import com.volantis.mcs.themes.properties.FontSizeKeywords;

/**
 * Constants for font size information.
 */
public abstract class FontSizeConstants {
    /**
     * The relative multiplier value to use for larger/smaller when applied
     * to values other than absolute keywords.
     */
    public static final double RELATIVE_MULTIPLIER = 1.2;

    /**
     * The StyleKeyword for font-size: xx-small
     */
    public static final StyleValue VALUE_XX_SMALL =
            FontSizeKeywords.XX_SMALL;

    /**
     * The StyleKeyword for font-size: x-small
     */
    public static final StyleValue VALUE_X_SMALL =
            FontSizeKeywords.X_SMALL;

    /**
     * The StyleKeyword for font-size: small
     */
    public static final StyleValue VALUE_SMALL =
            FontSizeKeywords.SMALL;

    /**
     * The StyleKeyword for font-size: medium
     */
    public static final StyleValue VALUE_MEDIUM =
            FontSizeKeywords.MEDIUM;

    /**
     * The StyleKeyword for font-size: large
     */
    public static final StyleValue VALUE_LARGE =
            FontSizeKeywords.LARGE;

    /**
     * The StyleKeyword for font-size: x-large
     */
    public static final StyleValue VALUE_X_LARGE =
            FontSizeKeywords.X_LARGE;

    /**
     * The StyleKeyword for font-size: xx-large
     */
    public static final StyleValue VALUE_XX_LARGE =
            FontSizeKeywords.XX_LARGE;

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private FontSizeConstants() {
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Nov-04	6158/1	adrianj	VBM:2004110108 Added FontSizeComputer

 11-Nov-04	6138/1	adrianj	VBM:2004110810 Added FontSizeComputer

 ===========================================================================
*/
