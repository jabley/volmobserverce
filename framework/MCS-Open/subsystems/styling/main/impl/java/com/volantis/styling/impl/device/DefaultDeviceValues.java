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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.StyleProperty;

/**
 * The default values that are used by the device.
 *
 * <p>By default the device will use either {@link DeviceValues.DEFAULT} or
 * {@link DeviceValues.NOT_SET} depending on whether the information about
 * the device is incomplete or not.
 */
public class DefaultDeviceValues
        implements DeviceValues {

    /**
     * The defaul value.
     */
    private final StyleValue defaultValue;

    /**
     * Initialise.
     *
     * @param defaultValue The default value.
     */
    public DefaultDeviceValues(StyleValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    // Javadoc inherited.
    public StyleValue getStyleValue(StyleProperty property) {
        return defaultValue;
    }

    // Javadoc inherited.
    public Priority getPriority(StyleProperty property) {
        return Priority.NORMAL;
    }

    // Javadoc inherited.
    public void appendStandardCSS(StringBuffer buffer) {
        buffer.append("..." + defaultValue.getStandardCSS() + "...");
    }
}
