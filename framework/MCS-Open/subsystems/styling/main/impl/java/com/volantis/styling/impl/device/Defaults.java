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

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceValues;

/**
 * Container for the different defaults that will be used by the device styles.
 */
public class Defaults {

    /**
     * The default value.
     */
    private final StyleValue defaultValue;

    /**
     * The default values that always return the default value above.
     */
    private final DeviceValues defaultValues;

    /**
     * The default styles that always return the default values above.
     */
    private final DeviceStyles defaultStyles;

    /**
     * Initialise.
     *
     * @param defaultValue The default value.
     */
    public Defaults(StyleValue defaultValue) {
        this.defaultValue = defaultValue;
        defaultValues = new DefaultDeviceValues(defaultValue);
        defaultStyles = new DefaultDeviceStyles(defaultValues);
    }

    /**
     * Get the default value.
     *
     * @return The default value.
     */
    public StyleValue getDefaultValue() {
        return defaultValue;
    }

    /**
     * Get the default values.
     *
     * @return The default values.
     */
    public DeviceValues getDefaultValues() {
            return defaultValues;
    }

    /**
     * Get the default styles.
     *
     * @return The default styles.
     */
    public DeviceStyles getDefaultStyles() {
            return defaultStyles;
    }
}
