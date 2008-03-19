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
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleColorNames;

/**
 * Renders a 'color' style value as an emulated attribute value for HTML 3.2.
 * <p/>
 * The emulated attribute value is calculated by passing through the 16 color
 * keywords which HTML 3.2 recognises, or rendering a rgb value as a 6 digit
 * hex number of the form '#rrggbb'.
 */
public final class HTML3_2ColorEmulationAttributeValueRenderer
    implements StyleEmulationAttributeValueRenderer {

    private static final char[] hexDigits = new char[]{
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Render a value that will be either a StyleColor (name, RGB or percent)
     * or a default "transparent" StyleKeyword.
     *
     * @param value
     * @return
     */
    public String render(StyleValue value) {

        String result = null;

        if (value instanceof StyleColorName) {
            StyleColorName color = (StyleColorName) value;
            result = renderName(color);
        } else if (value instanceof StyleColorPercentages) {
            // ignore percentages, HTML3.2 doesn't support 'em.
        } else if (value instanceof StyleColorRGB) {
            StyleColorRGB color = (StyleColorRGB) value;
            result = renderRGB(color);
        }

        return result;
    }

    /**
     * Render a StyleColor as a name.
     *
     * @param value the StyleColor to render
     * @return the rendered value, or null.
     */
    protected String renderName(StyleColorName value) {

        String result = null;

        // ignore other 'system' colors as HTML 3.2
        // doesn't support them.
        if (value == StyleColorNames.BLACK ||
                value == StyleColorNames.SILVER ||
                value == StyleColorNames.GRAY ||
                value == StyleColorNames.WHITE ||
                value == StyleColorNames.MAROON ||
                value == StyleColorNames.RED ||
                value == StyleColorNames.PURPLE ||
                value == StyleColorNames.FUCHSIA ||
                value == StyleColorNames.GREEN ||
                value == StyleColorNames.LIME ||
                value == StyleColorNames.OLIVE ||
                value == StyleColorNames.YELLOW ||
                value == StyleColorNames.NAVY ||
                value == StyleColorNames.BLUE ||
                value == StyleColorNames.TEAL ||
                value == StyleColorNames.AQUA) {
            return value.getName();
        }

        return result;
    }

    /**
     * Render a StyleColor as a rgb value in 6 digit hex notation.
     *
     * @param value the StyleColor to render
     * @return the rendered value.
     */
    private String renderRGB(StyleColorRGB value) {

        StringBuffer buffer = new StringBuffer(6);

        int rgb = value.getRGB();
        int r0 = (rgb & 0xf00000) >> 20;
        int r1 = (rgb & 0x0f0000) >> 16;
        int g0 = (rgb & 0x00f000) >> 12;
        int g1 = (rgb & 0x000f00) >> 8;
        int b0 = (rgb & 0x0000f0) >> 4;
        int b1 = rgb & 0x00000f;

        buffer.append('#');
        writeNibble(r0, buffer);
        writeNibble(r1, buffer);
        writeNibble(g0, buffer);
        writeNibble(g1, buffer);
        writeNibble(b0, buffer);
        writeNibble(b1, buffer);

        return buffer.toString();
    }

    /**
     * Write a part of a hex value.
     */
    private void writeNibble(int nibble, StringBuffer buffer) {

        char c = hexDigits[nibble];
        buffer.append(c);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 03-Aug-05	8923/1	pabbott	VBM:2005063010 End to End CSS emulation test

 19-May-05	8335/1	philws	VBM:2005051705 Port Palm WCA style emulation from 3.3

 19-May-05	8305/1	philws	VBM:2005051705 Provide style emulation rendering for HTML Palm WCA version 1.1

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
