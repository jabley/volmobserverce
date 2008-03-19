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
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleValueVisitor;

/**
 * A style color represented by a single RGB value.
 */
public final class StyleColorPercentagesImpl
        extends StyleColorImpl implements StyleColorPercentages {

    /**
     * The red percentage value.
     */
    private double red;

    /**
     * The green percentage value.
     */
    private double green;

    /**
     * The red percentage value.
     */
    private double blue;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleColorPercentagesImpl() {
    }

    /**
     * Initialise.
     *
     * @param red   The red percentage value.
     * @param green The green percentage value.
     * @param blue  The blue percentage value.
     */
    public StyleColorPercentagesImpl(double red, double green, double blue) {
        this(null, red, green, blue);
    }

    /**
     * Initialise.
     *
     * @param location The source location of the object, may be null.
     * @param red      The red percentage value.
     * @param green    The green percentage value.
     * @param blue     The blue percentage value.
     */
    public StyleColorPercentagesImpl(
            SourceLocation location, double red, double green, double blue) {
        super(location);

        checkPercentage("red", red);
        checkPercentage("green", green);
        checkPercentage("blue", blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Check to make sure that the percentage is within the allowable range.
     *
     * @param component  The name of the color component whose percentage is
     *                   being checked.
     * @param percentage The percentage to check.
     */
    private void checkPercentage(String component, double percentage) {
        // Validate the argument.
        if (percentage < 0.0 || percentage > 100.0) {
            throw new IllegalArgumentException
                    (
                            component + " percentage must be within the range 0.0 to 100.0");
        }
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleColorPercentagesImpl)) {
            return false;
        }

        StyleColorPercentagesImpl other = (StyleColorPercentagesImpl) o;
        return red == other.red && green == other.green && blue == other.blue;
    }

    protected int hashCodeImpl() {
        int result = 0;
        long bits = Double.doubleToLongBits(red);
        result = 37 * result + (int) (bits ^ (bits >>> 32));
        bits = Double.doubleToLongBits(green);
        result = 37 * result + (int) (bits ^ (bits >>> 32));
        bits = Double.doubleToLongBits(blue);
        result = 37 * result + (int) (bits ^ (bits >>> 32));
        return result;
    }

    public String getStandardCSS() {
        return "rgb(" + convertDoubleToText(red) + "%"
                + convertDoubleToText(green) + "%"
                + convertDoubleToText(blue) + "%)";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 ===========================================================================
*/
