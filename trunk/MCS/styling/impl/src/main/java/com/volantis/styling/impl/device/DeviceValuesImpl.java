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

package com.volantis.styling.impl.device;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueArray;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.StyleProperty;

/**
 * Implementation of {@link DeviceValues}.
 */
public class DeviceValuesImpl
        implements DeviceValues {

    /**
     * The array of property values.
     */
    protected final PropertyValueArray array;

    /**
     * The default value to use if no specific value has been set.
     */
    private final StyleValue defaultValue;

    /**
     * Initialise.
     *
     * @param array        The property value array.
     * @param defaultValue The default value.
     */
    public DeviceValuesImpl(PropertyValueArray array, StyleValue defaultValue) {
        this.array = array;
        this.defaultValue = defaultValue;
    }

    // Javadoc inherited.
    public StyleValue getStyleValue(StyleProperty property) {

        PropertyValue propertyValue = getPropertyValue(property);
        if (propertyValue == null) {
            return defaultValue;
        } else {
            return propertyValue.getValue();
        }
    }

    /**
     * Get the property value for the specified property.
     *
     * @param property The property whose value is needed.
     * @return The property value, or null.
     */
    private PropertyValue getPropertyValue(StyleProperty property) {
        if (array == null) {
            return null;
        } else {
            return array.getPropertyValue(property);
        }
    }

    // Javadoc inherited.
    public Priority getPriority(StyleProperty property) {
        PropertyValue propertyValue = getPropertyValue(property);
        if (propertyValue == null) {
            return Priority.NORMAL;
        } else {
            return propertyValue.getPriority();
        }
    }

    // Javadoc inherited.
    public void appendStandardCSS(StringBuffer buffer) {
        array.appendStandardCSS(buffer);
    }
}
