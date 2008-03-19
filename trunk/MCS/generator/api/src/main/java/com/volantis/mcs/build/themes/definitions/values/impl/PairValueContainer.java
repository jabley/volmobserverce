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
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.definitions.values.ValueContainer;
import com.volantis.mcs.build.themes.definitions.values.Value;

/**
 * A container that can hold a pair of values.
 */
public class PairValueContainer implements ValueContainer {
    /**
     * The first contained value.
     */
    private Value first;

    /**
     * The second contained value.
     */
    private Value second;

    // Javadoc inherited
    public void addValue(Value value) {
        if (first == null) {
            first = value;
        } else if (second == null) {
            second = value;
        } else {
            throw new IllegalStateException(
                    "Pair can only contain two values");
        }
    }

    /**
     * Retrieve the first contained value.
     * @return The first contained value
     */
    public Value getFirst() {
        return first;
    }

    /**
     * Retrieve the second contained value.
     * @return The second contained value
     */
    public Value getSecond() {
        return second;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Oct-04	5833/1	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 ===========================================================================
*/
