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

package com.volantis.mcs.dom2theme.impl.normalizer;


import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Base for all {@link PropertiesNormalizer}.
 */
public abstract class AbstractNormalizer
        implements PropertiesNormalizer {

    /**
     * The set of supported properties.
     */
    protected final ImmutableStylePropertySet supportedProperties;

    /**
     * Initialise.
     *
     * @param supportedProperties The set of supported properties.
     */
    protected AbstractNormalizer(
            ImmutableStylePropertySet supportedProperties) {
        this.supportedProperties = supportedProperties;
    }

    /**
     * Get a value from the property as long as it is supported.
     *
     * @param inputValues The values from which the value should be retrieved.
     * @param property    The property whose value is requested.
     * @return The value, or null if there was no value or the property is
     *         not supported.
     */
    protected StyleValue getSupportedStyleValue(
            StyleValues inputValues,
            StyleProperty property) {
        if (supportedProperties.contains(property)) {
            return inputValues.getStyleValue(property);
        } else {
            return null;
        }
    }

    /**
     * Set the value for the specified property only if the property is
     * supported and it has already been set.
     *
     * @param values   The values to update.
     * @param property The property whose value should be changed.
     * @param value    The new value.
     */
    protected void changeValue(
            MutablePropertyValues values,
            StyleProperty property,
            final StyleValue value) {

        if (supportedProperties.contains(property)) {
            if (values.getComputedValue(property) != null) {
                values.setComputedValue(property, value);
            }
        }
    }
}
