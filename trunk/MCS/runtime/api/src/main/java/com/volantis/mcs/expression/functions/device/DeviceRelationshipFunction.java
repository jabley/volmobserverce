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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.device;

import com.volantis.mcs.context.DeviceAncestorRelationship;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.functions.AbstractFunction;

/**
 * Base class for expression {@link com.volantis.xml.expression.Function}
 * implementations that test the relationship between the device that is
 * making the current request and some other device that is specified
 * via the functions only argument
 */
public abstract class DeviceRelationshipFunction
        extends AbstractFunction {

    // javadoc inherited
    public Value invoke(ExpressionContext expressionContext,
                        Value[] values) throws ExpressionException {

        // only expect a single argument that will name a device
        assertArgumentCount(values, 1);

         // Default to a UNKNOWN relationship
        DeviceAncestorRelationship deviceRelationship =
                DeviceAncestorRelationship.UNKNOWN;

        // retrieve the DevicePolicyValueAccessor
        DevicePolicyValueAccessor policyAccessor =
                (DevicePolicyValueAccessor) expressionContext.getProperty(
                        DevicePolicyValueAccessor.class);


        if (policyAccessor != null) {
            Value deviceValue = values[0];
            String deviceName = deviceValue.stringValue().asJavaString();

            deviceRelationship = policyAccessor.getRelationshipTo(deviceName);
        }

        // allow sublcasses to process the relationship
        return createFunctionReturnValue(expressionContext.getFactory(),
                                         deviceRelationship);
    }

    /**
     * Allow concrete subclasses to determine the {@link Value} that is
     * returned for the given device relationship
     * @param factory an <code>ExpressionFactory</code> that can be used to
     * factor <code>Value</code> instances
     * @param relationship the given device relationship
     * @return a Value instance
     */
    protected abstract Value createFunctionReturnValue(
            ExpressionFactory factory,
            DeviceAncestorRelationship relationship);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Aug-05	9121/3	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 25-Jul-05	9121/1	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
