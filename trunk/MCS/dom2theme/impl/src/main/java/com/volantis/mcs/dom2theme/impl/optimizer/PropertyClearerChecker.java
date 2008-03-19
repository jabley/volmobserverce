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

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Checks to see whether a property can be cleared.
 *
 * @mock.generate
 */
public interface PropertyClearerChecker {

    /**
     * Check the status of the property value.
     *
     * <p>This involves checking to see how the target device treats the
     * property, e.g. inherits, initial value etc. to see whether the value
     * can be cleared without affecting the style of the page.</p>
     *
     * @param property
     * @param inputValue The value to check.
     * @param usage
     * @param inputValues
     * @param deviceValue
     * @return A status value that indicates how the device will treat the
     *         property value.
     */
    PropertyStatus checkStatus(
            StyleProperty property, StyleValue inputValue,
            StatusUsage usage, PropertyValues inputValues,
            StyleValue deviceValue);

}
