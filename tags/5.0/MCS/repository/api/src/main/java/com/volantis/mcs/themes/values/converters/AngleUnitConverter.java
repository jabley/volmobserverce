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

import com.volantis.mcs.themes.values.AngleUnit;

/**
 * Converts between angle units.
 */
public final class AngleUnitConverter {
    /**
     * Coefficients table shared between instances of AngleUnitConverter.
     */
    private static final CoefficientMap coefficientMap = new CoefficientMap();

    /**
     * Definitions of coefficients between angle units.
     */
    static {
        coefficientMap.put(AngleUnit.DEG, 1.0);
        coefficientMap.put(AngleUnit.RAD, 180 / Math.PI); // 1 radian = 180/pi degree
        coefficientMap.put(AngleUnit.GRAD, 0.9); // 1 grade = 0.9 degree
    }

    /**
     * Static instance of AngleUnitConverter.
     */
    static private final AngleUnitConverter instance = new AngleUnitConverter();

    /**
     * @return Returns static instance of this class.
     */
    public static AngleUnitConverter getInstance() {
        return instance;
    }

    private AngleUnitConverter() {
        // hide it
    }

    /**
     * Returns angle value converted from <code>fromUnit</code> to
     * <code>toUnit</code>, or Double.NaN if conversion is not possible.
     * 
     * @param fromUnit source unit
     * @param toUnit destination unit
     * @return coefficient value
     */
    public double convert(double value, AngleUnit fromUnit, AngleUnit toUnit) {
        return value * coefficientMap.get(fromUnit, toUnit);
    }
}
