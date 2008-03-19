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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.MutableStyleProperties;

/**
 * Handle a value within a shorthand.
 *
 * <p>Instances of these are created for individual properties.</p>
 */
interface ShorthandValueHandler
    extends ValueConverter {

    /**
     * Set the property (may be more than one) to the specified value.
     *
     * @param properties The properties.
     * @param value The value to set.
     * @param priority
     */
    public void setPropertyValue(
            MutableStyleProperties properties, StyleValue value,
            Priority priority);

    /**
     * Get the name of the property in the format used by the CSS specification
     * for properties.
     *
     * @return The reference to the shorthand.
     */
    String getShorthandReference();

    /**
     * Get the initial value for the property.
     *
     * @return The initial value.
     */
    StyleValue getInitial();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
