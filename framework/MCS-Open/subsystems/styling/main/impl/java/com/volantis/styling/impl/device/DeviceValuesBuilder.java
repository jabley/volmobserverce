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

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueArray;
import com.volantis.mcs.themes.SparsePropertyValueArray;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.device.DeviceValues;

/**
 * Builder for {@link DeviceValues}.
 */
public class DeviceValuesBuilder {

    /**
     * The default value.
     */
    private final StyleValue defaultValue;

    /**
     * The array of property values.
     */
    private final PropertyValueArray propertyValues;

    /**
     * Initialise.
     *
     * @param defaultValue The default value.
     */
    public DeviceValuesBuilder(StyleValue defaultValue) {
        this.defaultValue = defaultValue;
        propertyValues = new SparsePropertyValueArray();
    }

    /**
     * Set the property value.
     * @param propertyValue The property value to set.
     */
    public void setPropertyValue(PropertyValue propertyValue) {
        propertyValues.setPropertyValue(propertyValue);
    }

    /**
     * Get the newly built values.
     *
     * @return The newly built values.
     */
    public DeviceValues getValues() {
        return new DeviceValuesImpl(propertyValues, defaultValue);
    }
}
