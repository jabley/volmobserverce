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
 * This DISelect Set XPath function returns the usable height of a device's
 * display as a decimal number. This value may be smaller than that returned by
 *  the di-cssmq-device-height function. Some devices, for example, have fixed
 * areas of their display that are unavailable to applications.
 * <P/>
 * The function returns the value supplied in the default argument if the
 * usable height cannot be determined from the delivery context. It returns 0
 * if the usable height cannot be determined from the delivery context and no
 * default argument is provided.
 */
public class DIHeightFunction extends DILengthFunction {

    // Javadoc inherited.
    protected Value execute(
            ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, String name,
            Value defaultValue) {

        // retrieve the usable display height in pixels from the repository
        double usableHeight = retrieveLengthValue(accessor,
                DevicePolicyConstants.USABLE_HEIGHT_IN_PIXELS, name);

        DoubleValue actualValue;
        try {
            if (!PX.equalsIgnoreCase(name)) {
                double usableHeightInMm = calculateUsableLengthInMillimetres(
                        accessor, DevicePolicyConstants.USABLE_HEIGHT_IN_PIXELS,
                        DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS,
                        DevicePolicyConstants.ACTUAL_HEIGHT_IN_MM);
                usableHeight = convertLengthFromMm(usableHeightInMm, name);
            }

            actualValue = new SimpleDoubleValue(expressionContext.getFactory(),
                    usableHeight);
        } catch (NumberFormatException e) {
            actualValue = null;
        }

        return calculateLength(expressionContext, accessor, name, defaultValue,
                actualValue);
    }

    // Javadoc inherited.
    protected String getFunctionName() {
        return "cssmq-height";
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
