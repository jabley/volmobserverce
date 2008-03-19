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

import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Converts between length units.
 */
public final class LengthUnitConverter {
    /**
     * Coefficients table shared between instances of LengthUnitConverter.
     */
    private static final CoefficientMap coefficientMap = new CoefficientMap();

    /**
     * Definitions of coefficients between length units.
     */
    static {
        coefficientMap.put(LengthUnit.MM, 1.0);
        coefficientMap.put(LengthUnit.CM, 10.0); // 1 cm = 10 mm
        coefficientMap.put(LengthUnit.IN, 25.4); // 1 in = 25.4 mm
        coefficientMap.put(LengthUnit.PT, 25.4 / 72); // 1 pt = 1/72 in
        coefficientMap.put(LengthUnit.PC, (25.4 / 72) * 12); // 1 pc = 12 pt
    }

    /**
     * Static instance of LengthUnitConverter.
     */
    static private final LengthUnitConverter instance = new LengthUnitConverter();

    /**
     * @return Returns static instance of this class.
     */
    public static LengthUnitConverter getInstance() {
        return instance;
    }

    /**
     * Returns length value converted from source unit to destination unit, or
     * Double.NaN if conversion is not possible.
     * 
     * @param value source length value
     * @param fromUnit source length unit
     * @param toUnit destination length unit
     * @return destination length value
     */
    public double convert(double value, LengthUnit sourceUnit,
            LengthUnit destUnit) {
        return value * coefficientMap.get(sourceUnit, destUnit);
    }
}
