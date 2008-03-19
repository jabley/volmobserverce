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

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.mcs.context.DeviceAncestorRelationship;
import com.volantis.mcs.expression.functions.AbstractFunctionTestAbstract;

/**
 * Unit test for the {@link com.volantis.mcs.expression.functions.device.DeviceRelationshipFunction} class
 */
public abstract class DeviceRelationshipFunctionTestAbstract
        extends AbstractFunctionTestAbstract {

    /**
     * Tests the {@link com.volantis.mcs.expression.functions.device.DeviceRelationshipFunction#invoke} when the
     * requesting device is the actual device that is passed in as
     * the argument to the invoke function
     * @throws Exception if an error occurs
     */
    public void testInvokeWhenDevice() throws Exception {
        // set up the value that the request context will return from
        // its getAncestorRelationship method.

        accessorMock.expects.getRelationshipTo("TestDevice")
                .returns(DeviceAncestorRelationship.DEVICE);

        // allow subclasses to check the result
        assertInvokeWhenDeviceResult(evaluateExpression());
    }

    /**
     * Allow subclasses to assert the value returned from the the
     * {@link #testInvokeWhenDevice} test
     * @param result the value to check
     * @throws Exception if an error occurs
     */
    public abstract void assertInvokeWhenDeviceResult(Value result)
            throws Exception;

    /**
     * Tests the {@link com.volantis.mcs.expression.functions.device.DeviceRelationshipFunction#invoke} when the
     * requesting device is an ancestor of the device that is passed in as
     * the argument to the invoke function
     * @throws Exception if an error occurs
     */
    public void testInvokeWhenAncestor() throws Exception {
        // set up the value that the request context will return from
        // its getAncestorRelationship method.
        accessorMock.expects.getRelationshipTo("TestDevice")
                .returns(DeviceAncestorRelationship.ANCESTOR);

        // allow subclasses to check the result
        assertInvokeWhenAncestorResult(evaluateExpression());
    }

     /**
     * Allow subclasses to assert the value returned from the the
     * {@link #testInvokeWhenAncestor} test
     * @param result the value to check
     * @throws Exception if an error occurs
     */
    public abstract void assertInvokeWhenAncestorResult(Value result)
            throws Exception;


    /**
     * Tests the {@link com.volantis.mcs.expression.functions.device.DeviceRelationshipFunction#invoke} when the
     * requesting device is unrelated to the device that is passed in as
     * the argument to the invoke function
     * @throws Exception if an error occurs
     */
    public void testInvokeWhenUnrelated() throws Exception {
        // set up the value that the request context will return from
        // its getAncestorRelationship method.
        accessorMock.expects.getRelationshipTo("TestDevice")
                .returns(DeviceAncestorRelationship.UNRELATED);

        // allow subclasses to check the result
        assertInvokeWhenUnrelatedResult(evaluateExpression());
    }

    /**
     * Allow subclasses to assert the value returned from the the
     * {@link #testInvokeWhenUnrelated} test
     * @param result the value to check
     * @throws Exception if an error occurs
     */
    public abstract void assertInvokeWhenUnrelatedResult(Value result)
            throws Exception;

    /**
     * Tests the {@link com.volantis.mcs.expression.functions.device.DeviceRelationshipFunction#invoke} when the
     * requesting device is unknown
     * @throws Exception if an error occurs
     */
    public void testInvokeWhenUnknown() throws Exception {
        // set up the value that the request context will return from
        // its getAncestorRelationship method.
        accessorMock.expects.getRelationshipTo("TestDevice")
                .returns(DeviceAncestorRelationship.UNKNOWN);

        // allow subclasses to check the result
        assertInvokeWhenUnknownResult(evaluateExpression());
    }

    /**
     * Allow subclasses to assert the value returned from the the
     * {@link #testInvokeWhenUnknown} test
     * @param result the value to check
     * @throws Exception if an error occurs
     */
    public abstract void assertInvokeWhenUnknownResult(Value result)
            throws Exception;

    /**
     * Helper method that parses an expression string and then evaluates
     * that expression
     * @return the Value resulting from evaluating the expression
     * @throws ExpressionException if an error occurs
     */
    protected Value evaluateExpression() throws ExpressionException  {
        // create an expression
        Expression expression = parser.parse(getFunctionQName() +
                                             "('TestDevice')");

        // evalute the expression
        return expression.evaluate(expressionContext);
    }

    // javadoc inherited
    protected String getURI() {
        return DEVICE_URI;
    }

    // javadoc inherited
    protected String getFunctionQName() {
        return "device:" + getFunctionName();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jul-05	9121/1	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
