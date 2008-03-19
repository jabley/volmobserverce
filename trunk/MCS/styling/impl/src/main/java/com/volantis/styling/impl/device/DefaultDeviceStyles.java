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

import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceValues;

/**
 * The styles that are used by default if there is no specific information
 * on how the target device may style an entity.
 *
 * <p>As the document being styled may have nested styles to arbitrary
 * (although limited in practice) these nest infinitely so as to ensure that
 * there is always a valid set of styles against which they can be
 * compared.</p>  
 */
public class DefaultDeviceStyles
        implements DeviceStyles {

    /**
     * The default values to use.
     */
    private final DeviceValues defaultValues;

    /**
     * Initialise.
     *
     * @param defaultValues The default values to use.
     */
    public DefaultDeviceStyles(DeviceValues defaultValues) {
        this.defaultValues = defaultValues;
    }

    // Javadoc inherited.
    public DeviceValues getValues() {
        return defaultValues;
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("... empty {");
        defaultValues.appendStandardCSS(buffer);
        buffer.append("} ...");
        return buffer.toString();
    }

    // Javadoc inherited.
    public DeviceStyles getNestedStyles(PseudoStyleEntity entity) {
        return this;
    }

    // Javadoc inherited.
    public DeviceStyles getMatchingStyles(
            StatefulPseudoClassSet pseudoClasses) {
        return this;
    }
}
