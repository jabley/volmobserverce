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

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.functions.AbstractFunction;
import com.volantis.mcs.expression.functions.device.IsInstanceOfFunction;
import com.volantis.mcs.expression.functions.device.DeviceRelationshipFunctionTestAbstract;

/**
 * Unit test for the {@link com.volantis.mcs.expression.functions.device.IsInstanceOfFunction} class
 */
public class IsInstanceOfFunctionTestCase
        extends DeviceRelationshipFunctionTestAbstract {

    /**
     * Name of the function being tested
     */
    private static final String FNAME = "isInstanceOf";

    // javadoc inherited
    public void assertInvokeWhenDeviceResult(Value result)
            throws Exception {
        // ensure that the value is a BooleanValue instance
        assertTrue("Value should be a BooleanValue instance",
                   result instanceof BooleanValue);

        // check that the BooleanValue is as expected
        assertSame("BooleanValue should be TRUE", BooleanValue.TRUE, result);
    }

    // javadoc inherited
    public void assertInvokeWhenAncestorResult(Value result)
            throws Exception {
        // ensure that the value is a BooleanValue instance
        assertTrue("Value should be a BooleanValue instance",
                   result instanceof BooleanValue);

        // check that the BooleanValue is as expected
        assertSame("BooleanValue should be TRUE", BooleanValue.TRUE, result);
    }

    // javadoc inherited
    public void assertInvokeWhenUnrelatedResult(Value result)
            throws Exception {
        // ensure that the value is a BooleanValue instance
        assertTrue("Value should be a BooleanValue instance",
                   result instanceof BooleanValue);

        // check that the BooleanValue is as expected
        assertSame("BooleanValue should be FALSE", BooleanValue.FALSE, result);
    }

    // javadoc inherited
    public void assertInvokeWhenUnknownResult(Value result)
            throws Exception {
        // ensure that the value is a BooleanValue instance
        assertTrue("Value should be a BooleanValue instance",
                   result instanceof BooleanValue);

        // check that the BooleanValue is as expected
        assertSame("BooleanValue should be FALSE", BooleanValue.FALSE, result);
    }

    // javadoc inherited
    protected AbstractFunction
            createTestableFunction(ExpressionFactory factory) {
        return new IsInstanceOfFunction();
    }

    // javadoc inherited
    protected String getFunctionName() {
        return FNAME;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-03	1629/3	doug	VBM:2003102401 Renamed GetIsInstanceOfTestCase to IsInstanceOfFunctionTestCase

 ===========================================================================
*/
