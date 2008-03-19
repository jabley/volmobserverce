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

package com.volantis.mcs.dom2theme.integration.optimizer;

import com.volantis.mcs.dom2theme.impl.optimizer.OptimizerHelper;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerChecker;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyStatus;
import com.volantis.mcs.dom2theme.impl.optimizer.StatusUsage;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.properties.PropertyDetails;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.InitialValueFinder;
import com.volantis.styling.values.PropertyValues;

public class TestPropertyClearerChecker implements PropertyClearerChecker {

    private final InitialValueFinder finder;
    private StyleValues inputValues;

    public TestPropertyClearerChecker() {
        this.finder = new InitialValueFinder();
    }

    public PropertyStatus checkStatus(
            StyleProperty property, StyleValue inputValue,
            StatusUsage usage,
            PropertyValues inputValues,
            StyleValue deviceValue) {

        final PropertyDetails details = property.getStandardDetails();
        StyleValue initialValue = finder.getInitialValue(this.inputValues, details);
        if (inputValue == OptimizerHelper.ANY ||
                inputValue.equals(initialValue)) {
            return PropertyStatus.CLEARABLE;
        }

        return PropertyStatus.REQUIRED;
    }

    public void setInputValues(StyleValues inputValues) {
        this.inputValues = inputValues;
    }
}
