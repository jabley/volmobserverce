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

import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.mcs.themes.MutableStyleProperties;

/**
 * Optimizes a group of properties.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface PropertyGroupOptimizer {
    
    /**
     * Optimize the properties.
     *
     * <p>This will try and remove as many properties as possible without
     * changing the styling, this may involve replacing a number of properties
     * with an equivalent set of shorthands.</p>
     *
     @param target
     @param inputValues
     @param outputValues       The properties to optimize.
     @param deviceValues
     */
    void optimize(
            TargetEntity target, PropertyValues inputValues,
            MutableStyleProperties outputValues,
            DeviceValues deviceValues);
}
