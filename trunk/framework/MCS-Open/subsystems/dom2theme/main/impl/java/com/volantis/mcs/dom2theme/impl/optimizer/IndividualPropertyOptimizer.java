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

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Optimizer for individual properties.
 */
public class IndividualPropertyOptimizer
        implements PropertyGroupOptimizer {

    /**
     * The checker.
     */
    private final PropertyClearerChecker checker;

    /**
     * The individual properties to optimize.
     */
    private final StyleProperty[] individualProperties;

    /**
     * Initialise.
     *
     * @param checker              The checker.
     * @param individualProperties The individual properties to optimize.
     */
    public IndividualPropertyOptimizer(PropertyClearerChecker checker,
                                       StyleProperty[] individualProperties) {
        this.checker = checker;
        this.individualProperties = individualProperties;
    }

    // Javadoc inherited.
    public void optimize(
            TargetEntity target, PropertyValues inputValues,
            MutableStyleProperties outputValues,
            DeviceValues deviceValues) {

        for (int i = 0; i < individualProperties.length; i++) {
            StyleProperty property = individualProperties[i];
            StyleValue styleValue = inputValues.getStyleValue(property);
            PropertyValue optimized;
            if (styleValue != null) {
                optimized = optimizeImpl(property, styleValue,
                        inputValues, deviceValues);
                if (optimized != null) {
                    outputValues.setPropertyValue(optimized);
                }
            }
        }

    }

    /**
     * Optimize an individual property.
     *
     * @param property     The property to optimize.
     * @param inputValue   The input value to check.
     * @param inputValues  The input values.
     * @param deviceValues The device values.
     * @return The value to copy, null if no value is needed.
     */
    private PropertyValue optimizeImpl(
            StyleProperty property, StyleValue inputValue,
            PropertyValues inputValues,
            DeviceValues deviceValues) {

        StyleValue deviceValue = deviceValues.getStyleValue(property);
        PropertyStatus status = checker.checkStatus(property, inputValue,
                StatusUsage.INDIVIDUAL, inputValues, deviceValue);
        if (status.isRequiredForIndividual()) {
            // If the priority that would be used by the device is greater than
            // normal then make this property important to ensure that it has
            // the desired effect.
            Priority devicePriority = deviceValues.getPriority(property);
            if (devicePriority.isGreaterThan(Priority.NORMAL)) {
                return ThemeFactory.getDefaultInstance().createPropertyValue(
                    property, inputValue, Priority.IMPORTANT);
            } else {
                return ThemeFactory.getDefaultInstance().createPropertyValue(
                    property, inputValue);
            }
        }

        return null;
    }
}
