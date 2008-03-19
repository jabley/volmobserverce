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

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.functions.AbstractFunction;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.mcs.expression.functions.device.GetPolicyValueFunction;
import com.volantis.mcs.expression.functions.AbstractFunctionTestAbstract;

/**
 * Test case for GetPolicyValueFunction.
 */
public class GetPolicyValueFunctionTestCase
        extends AbstractFunctionTestAbstract {

    static String FUNCTION_NAME = "getPolicyValue";

    static String FUNCTION_QNAME = "device:getPolicyValue";

    // javadoc inherited
    protected AbstractFunction
            createTestableFunction(ExpressionFactory factory) {
        return new GetPolicyValueFunction();
    }

    // javadoc inherited
    protected String getFunctionQName() {
        return FUNCTION_QNAME;
    }

    // javadoc inherited
    protected String getFunctionName() {
        return FUNCTION_NAME;
    }

    // javadoc inherited
    protected String getURI() {
        return DEVICE_URI;
    }

    /**
     * Test the getPolicyValue() function with a policy that has a value
     * @throws Exception if an error occurs
     */
    public void testWhenPolicyExists() throws Exception {
        // add a policy/policy value pair to the request
        String policy = "TestPolicy";
        String policyValue = "TestPolicyValue";

        accessorMock.expects.getDependentPolicyValue(policy)
                .returns(policyValue);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + policy + "')");

        // evalute the expression
        Value value = expression.evaluate(expressionContext);

        // Value returned should be a StringValue
        assertTrue("return value should be a StringValue instance",
                   value instanceof StringValue);

        // check the StringValues string
        assertEquals("Unexpected StringValue value",
                     policyValue,
                     value.stringValue().asJavaString());
    }

    /**
     * Test the getPolicyValue() function with a policy that has NO value
     * @throws Exception if an error occurs
     */
    public void testInvokeWhenPolicyDoesNotExist() throws Exception {
        // invoke the function
        Expression expression = parser.parse(getFunctionQName() + "('Test')");

        accessorMock.expects.getDependentPolicyValue("Test").returns(null);

        // evalute the expression
        Value value = expression.evaluate(expressionContext);

        // ensure the empty sequence has been returned
        assertSame("Empty sequence should be returned if policy doesn't exist",
                   Sequence.EMPTY,
                   value);
    }

    /**
     * Test the getPolicyValue() function with a policy that has a value and
     * the value is a comma separated list of items
     * @throws Exception if an error occurs
     */
    public void testInvokeWithCommaSeperatedValue() throws Exception {
        // add a policy/policy value pair to the request
        String policy = "TestPolicy";
        String policyValue = "value1 , value2, value3";

        accessorMock.expects.getDependentPolicyValue(policy)
                .returns(policyValue);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + policy + "')");

        // evalute the expression
        Value value = expression.evaluate(expressionContext);

        // ensure a sequence is returned
        assertTrue("return value should be a Sequence instance",
                   value instanceof Sequence);

        Sequence actual = (Sequence) value;

        // check that the sequence contains all the expected items
        assertEquals("Sequence should contain 3 items",
                     3,
                     actual.getLength());

        assertEquals("Unexpected first item in Sequence",
                     "value1",
                     actual.getItem(1).stringValue().asJavaString());

        assertEquals("Unexpected second item in Sequence",
                     "value2",
                     actual.getItem(2).stringValue().asJavaString());

        assertEquals("Unexpected third item in Sequence",
                     "value3",
                     actual.getItem(3).stringValue().asJavaString());
    }

    /**
     * Ensures that the getPolicyValue() function throws an exception when
     * invoked with no arguments.
     * @throws Exception if an error occurs
     */
    public void testInvokeNoParameters() throws Exception {
        try {

            GetPolicyValueFunction function =
                    (GetPolicyValueFunction)createTestableFunction(
                            ExpressionFactory.getDefaultInstance());

            // invoke the function
            function.invoke(expressionContext, new Value[0]);

            // invoking function with no arguments shold resutl in exception
            // being thrown
            fail("Invoking getPolicyValue() function with no arguments " +
                 "should result in an exception being thrown");
        } catch (ExpressionException e) {
            // expected condition
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Sep-05	9593/1	adrianj	VBM:2005092209 Hide experimental device policies from customer code

 25-Jul-05	9121/1	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-03	1771/1	doug	VBM:2003103104 Allowed getPolicyValue function to handle composite policy values

 11-Aug-03	1017/1	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
