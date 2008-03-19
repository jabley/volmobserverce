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

package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.definitions.values.FrequencyValue;

import java.io.PrintStream;

/**
 * Implementation of a frequency value.
 */
public class FrequencyValueImpl implements FrequencyValue {
    /**
     * The numerical component of this frequency value.
     */
    private double number;

    /**
     * The units in which this frequency is measured.
     */
    private String units;

    // Javadoc inherited
    public void setNumber(double newNumber) {
        number = newNumber;
    }

    // Javadoc inherited
    public void setUnits(String newUnits) {
        units = newUnits;
    }

    // Javadoc inherited
    public void writeConstructCode(String indent, PrintStream out) {
        out.print(indent);
        out.print("styleValueFactory.getFrequency(null, ");
        out.print(number);
        out.print(", FrequencyUnit.");
        out.print(units.toUpperCase());
        out.print(")");
    }
}
