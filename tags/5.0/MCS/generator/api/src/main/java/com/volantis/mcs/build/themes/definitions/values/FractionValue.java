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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes.definitions.values;

/**
 * Representation of a pair of values which make up a fraction.
 */
public interface FractionValue extends Value {

    /**
     * Sets the numerator value.
     * @param newNumerator The new numerator value
     */
    public void setNumerator(Value newNumerator);

    /**
     * Sets the denominator value.
     * @param newDenominator The new denominator value
     */
    public void setDenominator(Value newDenominator);
}
