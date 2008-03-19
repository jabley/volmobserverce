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
package com.volantis.mcs.expression.functions.diselect;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.SimpleDoubleValue;

/**
 * This DISelect Set XPath function returns the actual height of a device's
 * display as a decimal number. This value may be larger than that returned by
 * the di-cssmq-height function.
 * <P/>
 * The function returns the value supplied in the default argument if the
 * device height cannot be determined from the delivery context. It returns 0
 * if the device height cannot be determined from the delivery context and no
 * default argument is provided.
 */
public class DIDeviceHeightFunction extends DILengthFunction {

    // Javadoc inherited.
    protected Value execute(
            ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, String name,
            Value defaultValue) {

        // retrieve the physical height of the display from the repository
        double deviceHeight;
        if (PX.equalsIgnoreCase(name)) {
            deviceHeight = retrieveLengthValue(accessor,
                DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS, name);
        } else {
            deviceHeight = retrieveLengthValue(accessor,
                DevicePolicyConstants.ACTUAL_HEIGHT_IN_MM, name);
        }

        DoubleValue actualValue = new SimpleDoubleValue(
                expressionContext.getFactory(), deviceHeight);

        return calculateLength(expressionContext, accessor, name, defaultValue,
                actualValue);
    }

    // Javadoc inherited.
    protected String getFunctionName() {
        return "cssmq-device-height";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/
