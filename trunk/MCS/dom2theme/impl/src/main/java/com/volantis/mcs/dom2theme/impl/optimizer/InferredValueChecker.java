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

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.properties.PropertyDetails;

/**
 * Checks the values of inferred values.
 *
 * <p>An inferred value is one that is obtained either by inheritance or from
 * the initial value.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface InferredValueChecker {

    /**
     * Prepare the checker for new set of properties.
     *
     * @param parentValues The parent values to use for inferring
     */
    void prepare(StyleValues parentValues);

    /**
     * Check whether the value can be inferred.
     *
     * @param usage             The usage to be made of the value.
     * @param inputValues       The input values.
     * @param details           The details of the property.
     * @param inputValue        The input value to check.
     * @param checkInitialValue Indicates whether initial values should be
     *                          checked.
     * @return The status.
     */
    PropertyStatus checkInferred(
            StatusUsage usage, StyleValues inputValues,
            PropertyDetails details, StyleValue inputValue,
            boolean checkInitialValue);
}
