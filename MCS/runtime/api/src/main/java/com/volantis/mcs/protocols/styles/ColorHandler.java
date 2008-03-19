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

package com.volantis.mcs.protocols.styles;

import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;

/**
 * Converts a color into a string, ignores keywords so can also be used
 * for background colors which can have a keyword of transparent.
 */
public final class ColorHandler
        extends ValueRendererChecker {

    private static final char[] hexDigits = new char[]{
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public void visit(StyleColorName value, Object object) {
        string = value.getName();
    }

    public void visit(StyleColorPercentages value, Object object) {
        int r = (int) (value.getRed() * 255);
        int g = (int) (value.getGreen() * 255);
        int b = (int) (value.getBlue() * 255);
        string = renderRGB(r << 16 + g << 8 + b);
    }

    public void visit(StyleColorRGB value, Object object) {
        string = renderRGB(value.getRGB());
    }

    private String renderRGB(int rgb) {

        int r0 = (rgb & 0xf00000) >> 20;
        int r1 = (rgb & 0x0f0000) >> 16;
        int g0 = (rgb & 0x00f000) >> 12;
        int g1 = (rgb & 0x000f00) >> 8;
        int b0 = (rgb & 0x0000f0) >> 4;
        int b1 = rgb & 0x00000f;

        return "#" +
                nibble(r0) + nibble(r1) +
                nibble(g0) + nibble(g1) +
                nibble(b0) + nibble(b1);
    }

    private char nibble(int nibble) {
        return hexDigits[nibble];
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
