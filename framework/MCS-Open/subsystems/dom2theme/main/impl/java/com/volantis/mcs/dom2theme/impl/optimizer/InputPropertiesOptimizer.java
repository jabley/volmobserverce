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

import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.device.DeviceValues;

/**
 * Optimize the input properties.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface InputPropertiesOptimizer {

    /**
     * Calculate the output properties.
     *
     * @param elementName  The element for which the properties are being
     *                     calculated.
     * @param pseudoPath   The pseudo path to the input properties on the element.
     * @param inputValues  The input values to optimize.
     * @param parentValues The parent values.
     * @param deviceValues
     * @return The output properties, or null if no output properties are
     *         needed.
     */
    MutableStyleProperties calculateOutputProperties(
            String elementName, PseudoStylePath pseudoPath,
            MutablePropertyValues inputValues,
            StyleValues parentValues,
            DeviceValues deviceValues);
}
