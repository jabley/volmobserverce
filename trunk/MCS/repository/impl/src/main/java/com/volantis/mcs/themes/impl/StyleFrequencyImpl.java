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

import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.model.validation.SourceLocation;

/**
 */
public final class StyleFrequencyImpl
        extends StyleValueImpl implements StyleFrequency {

    /**
     * Unit in which frequency is stored
     */
    private FrequencyUnit unit;

    /**
     * Number of the units
     */
    private double number;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleFrequencyImpl() {
    }

    /**
     * Initialise.
     *
     * @param frequency The string value.
     * @param unit      The unit.
     */
    public StyleFrequencyImpl(double frequency, FrequencyUnit unit) {
        this(null, frequency, unit);
    }

    /**
     * Initialise.
     *
     * @param location  The source location of the object, may be null.
     * @param frequency The string value.
     * @param unit      The unit.
     */
    public StyleFrequencyImpl(
            SourceLocation location, double frequency, FrequencyUnit unit) {
        super(location);

        this.number = frequency;

        // Validate the argument.
        if (unit == null) {
            throw new IllegalArgumentException("unit cannot be null");
        }
        this.unit = unit;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.FREQUENCY;
    }

    /**
     * Override this method to call the correct method in the visitor.
     */
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

public FrequencyUnit getUnit() {
        return unit;
    }

    public double getNumber() {
        return number;
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleFrequencyImpl)) {
            return false;
        }

        StyleFrequencyImpl other = (StyleFrequencyImpl) o;

        return number == other.number && unit == other.unit;
    }

    protected int hashCodeImpl() {
        int result = 0;
        long bits = Double.doubleToLongBits(number);
        result = 37 * result + (int) (bits ^ (bits >>> 32));
        result = 37 * result + unit.hashCode();
        return result;
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        if (number == 0) {
            return "0";
        } else {
            return convertDoubleToText(number) + unit;
        }
    }

    // Javadoc inherited.
    public int getStandardCost() {
        if (number == 0) {
            return 1;
        } else {
            return getStandardCSS().length();
        }
    }
}
