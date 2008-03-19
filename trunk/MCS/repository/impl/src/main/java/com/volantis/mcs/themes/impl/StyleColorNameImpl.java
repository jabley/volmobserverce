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

import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 * A style color represented by name.
 */
public final class StyleColorNameImpl
        extends StyleColorImpl implements StyleColorName {

    /**
     * The color name.
     */
    private String name;

    /**
     * The standard RGB value for the color name.
     */
    private StyleColorRGB rgb;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleColorNameImpl() {
    }

    /**
     * Initialise.
     *
     * @param name The color name enumeration.
     * @param rgb
     * @deprecated Use enumeration.
     */
    public StyleColorNameImpl(String name, StyleColorRGB rgb) {
        // Colour names should not be case sensitive.
        this.name = name.toLowerCase();
        this.rgb = rgb;
    }

    public String getName() {
        return name;
    }

    public StyleColorRGB getRGB() {
        return rgb;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    protected boolean equalsImpl(Object o) {
        return false;
    }

    protected int hashCodeImpl() {
        return System.identityHashCode(this);
    }

    public String getStandardCSS() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10818/1	pduffin	VBM:2005121401 Added color orange, refactored NamedColor and StyleColorName to remove duplication of data

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 ===========================================================================
*/
