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

package com.volantis.mcs.build.themes.definitions.types;

/**
 * A Fraction type which consists of two other style types.
 */
public interface FractionType extends CompositeType {

    /**
     * Retrieves the numerator of the fraction.
     * @return The numerator of the fraction
     */
    public Type getNumerator();

    /**
     * Retrieves the denominator of the fraction.
     * @return The denominator of the fraction.
     */
    public Type getDenominator();

    /**
     * Sets the numerator of the fraction.
     * @param numerator The new value for the numerator of the fraction
     */
    public void setNumerator(Type numerator);

    /**
     * Sets the denominator of the fraction.
     * @param denominator The new value for the denominator of the fraction
     */
    public void setDenominator(Type denominator);
}
