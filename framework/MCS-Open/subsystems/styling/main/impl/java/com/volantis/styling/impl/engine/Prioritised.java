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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;

/**
 * A group of property values with the same priority.
 */
public class Prioritised {

    /**
     * The priority.
     */
    private final Priority priority;

    /**
     * The shared values.
     */
    private final PropertyValue[] values;

    /**
     * Initialse.
     *
     * @param priority The priority.
     * @param values   The shared values.
     */
    public Prioritised(Priority priority, PropertyValue[] values) {
        this.priority = priority;
        this.values = values;
    }

    /**
     * Get the priority.
     *
     * @return The priority.
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Get the values.
     *
     * @return The values.
     */
    public PropertyValue[] getValues() {
        return values;
    }
}
