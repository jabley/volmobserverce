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
 * Base for implementations of {@link InferredValueChecker}.
 */
public abstract class AbstractInferredValueChecker
        implements InferredValueChecker {

    /**
     * The finder of initial values.
     */
    private final InitialValueFinder initialValueFinder;

    /**
     * The parent values.
     */
    protected StyleValues parentValues;

    /**
     * Initialise.
     *
     * @param initialValueFinder The finder of initial values.
     */
    protected AbstractInferredValueChecker(InitialValueFinder initialValueFinder) {
        this.initialValueFinder = initialValueFinder;
    }

    // Javadoc inherited.
    public void prepare(StyleValues parentValues) {
        this.parentValues = parentValues;
    }

    /**
     * Check to see whether the input value matches the initial value of the
     * property.
     *
     * @param details     The details of the property.
     * @param inputValue  The input value to check.
     * @param inputValues The other input values for use by the finder.
     * @return True if it matches the initial value, false otherwise.
     */
    protected boolean checkInitialValue(
            PropertyDetails details, StyleValue inputValue,
            StyleValues inputValues) {

        // If the value matches the initial value then it can be cleared.
        StyleValue initialValue = initialValueFinder.getInitialValue(
                inputValues, details);

        return OptimizerHelper.styleValueEqualsWithAny(inputValue,
                initialValue);
    }
}
