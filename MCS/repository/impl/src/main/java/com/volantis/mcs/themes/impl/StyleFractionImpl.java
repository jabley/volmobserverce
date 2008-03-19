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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.synergetics.ObjectHelper;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleValue;

/**
 */
public final class StyleFractionImpl extends StyleValueImpl
        implements StyleFraction {

    /**
     * The numerator component of the fraction.
     */
    private StyleValue numerator;

    /**
     * The denominator component of the fraction.
     */
    private StyleValue denominator;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleFractionImpl() {
    }

    /**
     * Initialise.
     *
     * @param numerator     value of the numerator component of the fraction
     * @param denominator   value of the denominator component of the fraction
     * @throws IllegalArgumentException if the numerator component of the
     * fraction is null
     */
    public StyleFractionImpl(StyleValue numerator, StyleValue denominator) {
        this(null, numerator, denominator);
    }

    /**
     * Initialise.
     *
     * @param location    The source location of the object, may be null.
     * @param numerator   value of the numerator component of the fraction
     * @param denominator value of the denominator component of the fraction
     * @throws IllegalArgumentException if the numerator component of the
     *                                  fraction is null
     */
    public StyleFractionImpl(
            SourceLocation location, StyleValue numerator,
            StyleValue denominator) {
        super(location);

        // Validate the argument.
        if (numerator == null) {
            throw new IllegalArgumentException("numerator may not be null");
        }

        this.numerator = numerator;
        this.denominator = denominator;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.FRACTION;
    }

    // Javadoc inherited.
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }


    public StyleValue getNumerator() {
        return numerator;
    }

    public StyleValue getDenominator() {
        return denominator;
    }

    // Javadoc inherited.
    protected boolean equalsImpl(Object o) {

        if (!(o instanceof StyleFractionImpl)) {
            return false;
        }
        StyleFractionImpl other = (StyleFractionImpl) o;

        return ObjectHelper.equals(numerator, other.numerator) &&
                ObjectHelper.equals(denominator, other.denominator);
    }

    // Javadoc inherited.
    protected int hashCodeImpl() {
        int result = 0;
        result = 37 * result + numerator.hashCode();
        if (denominator != null) {
            result = 37 * result + denominator.hashCode();
        }
        return result;
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(numerator);
        if (denominator != null) {
            buffer.append("/");
            buffer.append(denominator);
        }
        return buffer.toString();
    }

    // Javadoc inherited.
    public int getStandardCost() {
        int cost = numerator.getStandardCost();
        if (denominator != null) {
            cost += denominator.getStandardCost();
        }
        return cost;
    }
}
