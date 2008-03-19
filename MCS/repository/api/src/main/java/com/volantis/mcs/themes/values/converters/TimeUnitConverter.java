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
import com.volantis.mcs.themes.values.TimeUnit;

/**
 * Converts between time units.
 */
public final class TimeUnitConverter {
    /**
     * Coefficients table shared between instances of TimeUnitConverter.
     */
    private static final CoefficientMap coefficientMap = new CoefficientMap();

    /**
     * Definitions of coefficients between time units.
     */
    static {
        coefficientMap.put(TimeUnit.MS, 1.0);
        coefficientMap.put(TimeUnit.S, 1000.0);
    }

    /**
     * Static instance of TimeUnitConverter.
     */
    static private final TimeUnitConverter instance = new TimeUnitConverter();

    /**
     * @return Returns static instance of this class.
     */
    public static TimeUnitConverter getInstance() {
        return instance;
    }

    /**
     * Returns time value converted from source unit to destination unit, or
     * Double.NaN if conversion is not possible.
     * 
     * @param value source time value
     * @param fromUnit source time unit
     * @param toUnit destination time unit
     * @return destination time value
     */
    public double convert(double value, TimeUnit sourceUnit, TimeUnit destUnit) {
        return value * coefficientMap.get(sourceUnit, destUnit);
    }
}
