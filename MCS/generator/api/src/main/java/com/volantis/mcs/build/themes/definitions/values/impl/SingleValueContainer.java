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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.definitions.values.ValueContainer;
import com.volantis.mcs.build.themes.definitions.values.Value;

/**
 * A container that can hold a single value.
 */
public class SingleValueContainer
    implements ValueContainer {

    /**
     * The value held within the container.
     */
    private Value value;

    /**
     * Add a single value.
     * @throws IllegalStateException If a value has already been added.
     */
    public void addValue(Value value) {
        if (this.value != null) {
            throw new IllegalStateException("Only one value allowed");
        }

        this.value = value;
    }

    /**
     * Get the value that was added.
     * @return The value that was added.
     */
    public Value getValue() {
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
