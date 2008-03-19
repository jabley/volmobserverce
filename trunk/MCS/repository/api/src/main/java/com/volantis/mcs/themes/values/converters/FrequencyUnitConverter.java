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

package com.volantis.mcs.themes.values.converters;

import com.volantis.mcs.themes.values.FrequencyUnit;

/**
 * Converts between frequency units.
 */
public final class FrequencyUnitConverter {
    /**
     * Coefficients table shared between instances of FrequencyUnitConverter.
     */
    private static final CoefficientMap coefficientMap = new CoefficientMap();

    /**
     * Definitions of coefficients between frequency units.
     */
    static {
        coefficientMap.put(FrequencyUnit.HZ, 1.0);
        coefficientMap.put(FrequencyUnit.KHZ, 1000.0);
    }

    /**
     * Static instance of FrequencyUnitConverter.
     */
    static private final FrequencyUnitConverter instance = new FrequencyUnitConverter();

    /**
     * @return Returns static instance of this class.
     */
    public static FrequencyUnitConverter getInstance() {
        return instance;
    }

    /**
     * Gets coefficient between <code>fromUnit</code> and <code>toUnit</code>,
     * or <code>Double.NaN</code> if conversion is not possible.
     * 
     * @param fromUnit source unit
     * @param toUnit destination unit
     * @return coefficient value
     */
    public double convert(double number, FrequencyUnit fromUnit,
            FrequencyUnit toUnit) {
        return number * coefficientMap.get(fromUnit, toUnit);
    }
}
