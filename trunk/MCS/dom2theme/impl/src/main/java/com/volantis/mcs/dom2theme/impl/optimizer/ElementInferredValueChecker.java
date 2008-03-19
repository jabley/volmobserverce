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
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.InitialValueFinder;

/**
 * Checks inferred values for pseudo class rules.
 *
 * <p>The value is only inferred (and hence can only be cleared) if the
 * check is being done for a shorthand and the value matches the initial value,
 * otherwise it is always required.</p>
 */
public class ElementInferredValueChecker
        extends AbstractInferredValueChecker {

    /**
     * Initialise.
     *
     * @param initialValueFinder The finder of initial values.
     */
    public ElementInferredValueChecker(
            InitialValueFinder initialValueFinder) {
        super(initialValueFinder);
    }

    /**
     * Check to see whether the input value matches the inherited value.
     *
     * <p>This must only be called if the property is inherited.</p>
     *
     * @param details     The details of the property.
     * @param inputValue  The input value to check.
     * @return True if it matches, false otherwise.
     */
    private boolean checkInheritedValue(
            PropertyDetails details, StyleValue inputValue) {

        // If the property is automatically inherited and the value
        // matches the inherited value then it can be cleared.
        StyleProperty property = details.getProperty();
        StyleValue parentValue = parentValues.getStyleValue(property);
        return OptimizerHelper.styleValueEqualsWithAny(inputValue, parentValue);
    }

    // Javadoc inherited.
    public PropertyStatus checkInferred(
            StatusUsage usage, StyleValues inputValues,
            PropertyDetails details, StyleValue inputValue,
            boolean checkInitialValue) {

        if (usage == StatusUsage.INDIVIDUAL) {
            return checkInferredIndividual(details, inputValue, inputValues,
                    checkInitialValue);
        } else if (usage == StatusUsage.SHORTHAND) {
            return checkInferredShorthand(inputValues, details, inputValue,
                    checkInitialValue);
        } else {
            throw new IllegalArgumentException("Unknown usage " + usage);
        }
    }

    /**
     * Check the inferred value for an individual property.
     *
     * <p>This will either check an inherited or initial value depending on
     * whether the property is automatically inherited or not.</p>
     *
     * <p>It will return {@link PropertyStatus#CLEARABLE} if the value will be
     * inferred, and {@link PropertyStatus#REQUIRED} if it will not be.</p>
     *
     * @param details           The details of the property.
     * @param inputValue        The input value to check.
     * @param inputValues       The input values.
     * @param checkInitialValue True if the initial value should be checked.
     * @return The status.
     */
    private PropertyStatus checkInferredIndividual(
            PropertyDetails details, StyleValue inputValue,
            StyleValues inputValues, boolean checkInitialValue) {

        PropertyStatus status = PropertyStatus.REQUIRED;
        if (details.isInherited()) {
            if (checkInheritedValue(details, inputValue)) {
                status = PropertyStatus.CLEARABLE;
            }
        } else if (checkInitialValue) {
            if (checkInitialValue(details, inputValue, inputValues)) {
                status = PropertyStatus.CLEARABLE;
            }
        }

        return status;
    }

    /**
     * Check the inferred value for a shorthand property.
     *
     * <p>If the property is automatically inherited it will check the
     * initial value for use within a shorthand and inherited for use as an
     * individual property, otherwise it will just check the initial
     * value.</p>
     *
     * <p>This will return one of the following:</p>
     * <ul>
     * <li>{@link PropertyStatus#CLEARABLE} if it will be inferred both within
     * a shorthand and as an individual property.</li>
     * <li>{@link PropertyStatus#REQUIRED} if it will not be inferred either
     * within a shorthand or as an individual property.</li>
     * <li>{@link PropertyStatus#REQUIRED_FOR_INDIVIDUAL} if it will be
     * inferred within a shorthand but not as an individual property.</li>
     * <li>{@link PropertyStatus#REQUIRED_FOR_INDIVIDUAL} if it will be
     * inferred as an individual property but not within a shorthand.</li>
     * </ul>
     *
     * @param details           The details of the property.
     * @param inputValue        The input value to check.
     * @param inputValues       The input values.
     * @param checkInitialValue True if the initial value should be checked.
     * @return The status.
     */
    private PropertyStatus checkInferredShorthand(
            StyleValues inputValues, PropertyDetails details,
            StyleValue inputValue, boolean checkInitialValue) {

        boolean requiredForShorthand = true;
        boolean requiredForIndividual = true;

        boolean inherited = details.isInherited();
        if (inherited) {
            // It is required for an individual property if it is inherited but
            // does not match the parent value.
            requiredForIndividual = !checkInheritedValue(details, inputValue);
        }

        if (checkInitialValue) {
            boolean initial = checkInitialValue(details, inputValue, inputValues);

            // If the property is not inherited and does not match the initial
            // value then it is required for an individual property.
            if (!inherited) {
                requiredForIndividual = !initial;
            }

            // If the property does not match the initial value then it is
            // required for a shorthand.
            requiredForShorthand = !initial;
        }

        if (requiredForShorthand && requiredForIndividual) {
            return PropertyStatus.REQUIRED;
        } else if (requiredForShorthand) {
            return PropertyStatus.REQUIRED_FOR_SHORTHAND;
        } else if (requiredForIndividual) {
            return PropertyStatus.REQUIRED_FOR_INDIVIDUAL;
        } else {
            return PropertyStatus.CLEARABLE;
        }
    }
}
