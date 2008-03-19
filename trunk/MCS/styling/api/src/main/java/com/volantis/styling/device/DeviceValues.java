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

package com.volantis.styling.device;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.MarkerStyleValue;
import com.volantis.styling.properties.StyleProperty;

/**
 * The set of values that the device would associate with a styleable entity
 * in the output document.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface DeviceValues
        extends StyleValues {

    /**
     * The device does not set the value.
     */
    StyleValue NOT_SET = new MarkerStyleValue("<not-set>");

    /**
     * The device sets the value to an unknown value.
     */
    StyleValue UNKNOWN = new MarkerStyleValue("<unknown>");

    /**
     * There is no information about whether the device sets the value or not
     * so assume our default behaviour.
     */
    StyleValue DEFAULT = new MarkerStyleValue("<default>");

    /**
     * The priority that the device would associate with the specified
     * property.
     *
     * <p>This method never returns null, if the device does not have any
     * explicit priority set for the entity then a default priority of
     * {@link Priority#NORMAL} will be returned.</p>
     *
     * @param property The property whose device priority is requested.
     * @return The device priority.
     */
    Priority getPriority(StyleProperty property);

    /**
     * Append the standard CSS representation of this to the specified buffer.
     *
     * @param buffer The buffer to update.
     */
    void appendStandardCSS(StringBuffer buffer);
}
