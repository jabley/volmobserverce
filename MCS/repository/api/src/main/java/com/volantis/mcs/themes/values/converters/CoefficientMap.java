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

import java.util.HashMap;

/**
 * Coefficient map used by unit converters.
 */
public class CoefficientMap {
    /**
     * Coefficients table.
     */
    private final HashMap coefficients = new HashMap();

    /**
     * Adds an entry in the coefficients table.
     * 
     * @param unit a unit to store coefficient for
     * @param coefficient a coefficient value
     */
    public void put(Object unit, double coefficient) {
        coefficients.put(unit, new Double(coefficient));
    }

    /**
     * Gets coefficient value to convert value between source and destination
     * units, or <code>Double.NaN</code> if conversion is not possible.
     * 
     * @param sourceUnit source unit
     * @param destUnit destination unit
     * @return coefficient value
     */
    public double get(Object sourceUnit, Object destUnit) {
        if (sourceUnit == destUnit) {
            return 1.0;
        } else {
            return get(sourceUnit) / get(destUnit);
        }
    }

    /**
     * Returns coefficient value for unit, or <code>Double.NaN</code> if it's
     * not found.
     * 
     * @param unit a unit
     * @return coefficient value
     */
    public double get(Object unit) {
        Double coefficient = (Double) coefficients.get(unit);

        if (coefficient == null) {
            return Double.NaN;
        } else {
            return coefficient.doubleValue();
        }
    }
}
