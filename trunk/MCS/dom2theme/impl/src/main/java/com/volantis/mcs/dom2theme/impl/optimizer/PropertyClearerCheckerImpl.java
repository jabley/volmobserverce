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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.InitialValueAccuracy;
import com.volantis.styling.properties.PropertyDetails;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.InitialValueFinder;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.log.LogDispatcher;

public class PropertyClearerCheckerImpl
        implements PropertyClearerChecker {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(PropertyClearerCheckerImpl.class);

    /**
     * The device specific details of the properties that are to be optimized.
     *
     * <p>Any properties not in this set are ignored.</p>
     */
    private final PropertyDetailsSet detailsSet;

    /**
     * The checker for properties of elements (including pseudo elements).
     */
    private final InferredValueChecker elementChecker;

    /**
     * The checker for properties of stateful pseudo classes.
     */
    private final InferredValueChecker pseudoClassChecker;

    /**
     * The finder of initial values.
     */
    private final InitialValueFinder initialValueFinder;

    /**
     * Indicates whether to produce debug output.
     */
    private final boolean debugEnabled;

    /**
     * The current inferrer checker, set up by
     * {@link #prepare(com.volantis.mcs.themes.StyleValues, TargetEntity)}.
     */
    private InferredValueChecker inferredValueChecker;

    /**
     * Initialise.
     *
     * @param detailsSet Details about the set of properties to output.
     */
    public PropertyClearerCheckerImpl(PropertyDetailsSet detailsSet) {

        this.detailsSet = detailsSet;
        this.initialValueFinder = new InitialValueFinder();

        // Remember whether debugEnabled logging is turned on. This check is done
        // once here because this class is used thousands of times on every
        // page and every little helps.
        this.debugEnabled = LOGGER.isDebugEnabled();

        elementChecker = new ElementInferredValueChecker(initialValueFinder);
        pseudoClassChecker = new PseudoClassInferredValueChecker(
                initialValueFinder);
    }

    /**
     * Prepare the checker.
     *
     * @param parentValues The parent values.
     * @param target       The entity to which the values are targeted.
     */
    public void prepare(
            StyleValues parentValues,
            TargetEntity target) {

        inferredValueChecker = (target == TargetEntity.ELEMENT) ?
                elementChecker : pseudoClassChecker;

        if (target == TargetEntity.ELEMENT) {
            inferredValueChecker = elementChecker;
        } else if (target == TargetEntity.PSEUDO_CLASS) {
            inferredValueChecker = pseudoClassChecker;
        } else {
            throw new IllegalArgumentException(
                    "Unknown check type: " + target);
        }
        inferredValueChecker.prepare(parentValues);
    }

    // Javadoc inherited.
    public PropertyStatus checkStatus(
            StyleProperty property, StyleValue inputValue,
            StatusUsage usage, PropertyValues inputValues,
            StyleValue deviceValue) {

        PropertyDetails details = detailsSet.getPropertyDetails(property);
        if (details == null) {
            // The property is not supported on the target device so assume
            // that it can just be discarded.
            return PropertyStatus.CLEARABLE;
        }

        final boolean initialKnown =
                (details.getInitialValueAccuracy() ==
                InitialValueAccuracy.KNOWN);

        // The property can be cleared if it will have no effect on the styling
        // on the device. In general that means that it can be cleared if its
        // value matches the value that the device would use in the event that
        // it was cleared.
        //
        // If the value used by the device is not known then we assume that it
        // would use the value determined by the standards, unless the user
        // explicitly set the property value in their theme in which case it is
        // always preserved.
        //
        // If the value used by the device is known then the property is
        // cleared if it matches that value and left if it does not,
        // irrespective of whether it was explicitly specified or not.
        //
        // The device can select the value to use in a number of different ways.
        //
        // 1) The device may have a default style sheet (or behave as if it has)
        //    that contains a rule that explicitly sets the value of that
        //    property on that element.
        //
        // 2) The property is automatically inherited from its parent element.
        //
        // 3) The device specific initial value is used for all unset properties
        //    on the root element and all properties that do not automatically
        //    inherit on other elements.

        // Assume that the property is required.
        PropertyStatus result = PropertyStatus.REQUIRED;

        // Check to see what is known about the property setting on this
        // element.
        if (deviceValue == DeviceValues.DEFAULT) {

            // If the property was not explicitly specified, or we know what
            // the initial value is then check the inferred values, otherwise
            // do not in order to maintain backwards compatability.
            if (!inputValues.wasExplicitlySpecified(property) || initialKnown) {
                result = inferredValueChecker.checkInferred(
                        usage, inputValues, details, inputValue, true);
            }

        } else if (deviceValue == DeviceValues.NOT_SET) {

            // Clear if matches inherited or a known initial value.
            result = inferredValueChecker.checkInferred(
                    usage, inputValues, details, inputValue, initialKnown);

        } else if (deviceValue == DeviceValues.UNKNOWN) {

            // Not safe to clear unless it is ANY
            result = (inputValue == OptimizerHelper.ANY) ?
                    PropertyStatus.CLEARABLE : PropertyStatus.REQUIRED;

        } else if (OptimizerHelper.styleValueEqualsWithAny(inputValue,
                deviceValue)) {

            // Known value.
            result = PropertyStatus.CLEARABLE;
        }

        if (debugEnabled) {
            debug("Status of " + inputValue + " is " + result);
        }
        return result;
    }

    /**
     * Wrapper around <code>logger.debug()</code> to prevent the commit
     * trigger from failing due to it not being wrapped in test for
     * isDebugEnabled().
     *
     * @param message The message to log.
     */
    private void debug(String message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(message);
        }
    }
}
