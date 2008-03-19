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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.device;

import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * The GetDeviceNameFunction ExpressionFunction. Gets the name of a device
 * and returns the appropriate value.
 */
public class GetDeviceNameFunction extends DeviceAccessorFunction {

    // javadoc inherited
    protected String getFunctionName() {
        return "getDeviceName";
    }

    // javadoc inherited
    public Value invoke(ExpressionContext expressionContext,
                        Value[] values) throws ExpressionException {

        // only no arguments
        assertArgumentCount(values, 0);

        // the return value
        Value value = Sequence.EMPTY;

        // extract the DevicePolicyValueAccessor that contains the current
        // device
        DevicePolicyValueAccessor devicePolicyValueAccessor =
                (DevicePolicyValueAccessor) expressionContext.getProperty(
                        DevicePolicyValueAccessor.class);

        if (devicePolicyValueAccessor != null) {
            String policyValue = devicePolicyValueAccessor.getDeviceName();
            value = createValue(expressionContext.getFactory(), policyValue);
        }

        return value;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	10049/1	schaloner	VBM:2005092818 Ongoing device name support

 26-Sep-05	9593/1	adrianj	VBM:2005092209 Hide experimental device policies from customer code

 25-Jul-05	9121/1	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-03	1771/1	doug	VBM:2003103104 Allowed getPolicyValue function to handle composite policy values

 11-Aug-03	1017/1	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
