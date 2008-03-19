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
import com.volantis.styling.values.InitialValueFinder;

/**
 * Checks inferred values for pseudo class rules.
 *
 * <p>The value is only inferred (and hence can only be cleared) if the
 * check is being done for a shorthand and the value matches the initial value,
 * otherwise it is always required.</p>
 */
public class PseudoClassInferredValueChecker
        extends AbstractInferredValueChecker {

    /**
     * Initialise.
     *
     * @param initialValueFinder The finder of initial values.
     */
    public PseudoClassInferredValueChecker(
            InitialValueFinder initialValueFinder) {
        super(initialValueFinder);
    }

    // Javadoc inherited.
    public PropertyStatus checkInferred(
            StatusUsage usage, StyleValues inputValues,
            PropertyDetails details, StyleValue inputValue,
            boolean checkInitialValue) {

        // Default to required for elements.
        PropertyStatus status = PropertyStatus.REQUIRED;
        if (usage == StatusUsage.SHORTHAND && checkInitialValue) {
            boolean initial = checkInitialValue(details, inputValue,
                    inputValues);
            if (initial) {
                status = PropertyStatus.CLEARABLE;
            }
        }

        return status;
    }
}
