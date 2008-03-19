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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.values.converters.ColorConverter;

/**
 * A style color represented by a single RGB value.
 */
public final class StyleColorRGBImpl
        extends StyleColorImpl implements StyleColorRGB {

    public static final StyleColorRGB BLACK = new StyleColorRGBImpl(0, 0, 0);
    public static final StyleColorRGB WHITE = new StyleColorRGBImpl(255, 255, 255);
    public static final StyleColorRGB RED = new StyleColorRGBImpl(255, 0, 0);
    public static final StyleColorRGB LIME = new StyleColorRGBImpl(0, 255, 0);
    public static final StyleColorRGB GREEN = new StyleColorRGBImpl(0, 128, 0);
    public static final StyleColorRGB BLUE = new StyleColorRGBImpl(0, 0, 255);
    public static final StyleColorRGB GRAY = new StyleColorRGBImpl(128, 128, 128);
    public static final StyleColorRGB SILVER = new StyleColorRGBImpl(192, 192, 192);
    public static final StyleColorRGB YELLOW = new StyleColorRGBImpl(255, 255, 0);
    public static final StyleColorRGB AQUA = new StyleColorRGBImpl(0, 255, 255);
    public static final StyleColorRGB FUCHSIA = new StyleColorRGBImpl(255, 0, 255);
    public static final StyleColorRGB MAROON = new StyleColorRGBImpl(128, 0, 0);
    public static final StyleColorRGB NAVY = new StyleColorRGBImpl(0, 0, 128);
    public static final StyleColorRGB OLIVE = new StyleColorRGBImpl(128, 128, 0);
    public static final StyleColorRGB PURPLE = new StyleColorRGBImpl(128, 0, 128);
    public static final StyleColorRGB TEAL = new StyleColorRGBImpl(0, 128, 128);

    /**
     * The rgb value.
     */
    private int rgb;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleColorRGBImpl() {
    }

    /**
     * Initialise.
     *
     * @param rgb The RGB value.
     */
    public StyleColorRGBImpl(int rgb) {
        this(null, rgb);
    }

    /**
     * Initialise.
     *
     * @param location The source location of the object, may be null.
     * @param rgb The RGB value.
     */
    public StyleColorRGBImpl(SourceLocation location, int rgb) {
        super(location);

        // Validate the argument.
        if (rgb < 0 || rgb > 0xffffff) {
            throw new IllegalArgumentException
                    ("RGB value must be within the range 0x000000 to 0xffffff");
        }

        this.rgb = rgb;
    }

    public StyleColorRGBImpl(int red, int green, int blue) {

        // Validate the arguments.
        validateComponent(red, "Red");
        validateComponent(green, "Green");
        validateComponent(blue, "Blue");

        this.rgb = (red << 16) + (green << 8) + blue;
    }

    private void validateComponent(int component, String componentName) {
        if (component < 0 || component > 0xff) {
            throw new IllegalArgumentException(componentName +
                    " value must be within the range 0x00 to 0xff");
        }
    }

    public int getRGB() {
        return rgb;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleColorRGBImpl)) {
            return false;
        }

        StyleColorRGBImpl other = (StyleColorRGBImpl) o;

        return rgb == other.rgb;
    }

    protected int hashCodeImpl() {
        int result = 0;
        result = 37 * result + rgb;
        return result;
    }

    public String getStandardCSS() {
        return ColorConverter.convertColorRGBToText(rgb, true);
    }

    public int getStandardCost() {

        int r0 = (rgb & 0xf00000) >> 20;
        int r1 = (rgb & 0x0f0000) >> 16;
        int g0 = (rgb & 0x00f000) >> 12;
        int g1 = (rgb & 0x000f00) >> 8;
        int b0 = (rgb & 0x0000f0) >> 4;
        int b1 = rgb & 0x00000f;

        if (r0 == r1 && g0 == g1 && b0 == b1) {
            return 4; // 1 for #, 3 for digits.
        } else {
            return 7; // 1 for #, 6 for digits.
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10816/1	pduffin	VBM:2005121401 Porting forward changes from MCS 3.5

 14-Dec-05	10818/1	pduffin	VBM:2005121401 Added color orange, refactored NamedColor and StyleColorName to remove duplication of data

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 ===========================================================================
*/
