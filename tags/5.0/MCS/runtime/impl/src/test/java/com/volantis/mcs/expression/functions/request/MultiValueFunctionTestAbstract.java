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
package com.volantis.mcs.expression.functions.request;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import junit.framework.Assert;

/**
 * Test case for ExpressionFunctions that can only provide multiple values.
 */
public abstract class MultiValueFunctionTestAbstract
        extends RequestFunctionTestAbstract {

    protected void addSingleValueExpectations(String name, String value) {
        if (value == null) {
            addMultiValueExpectations(name, null);
        } else {
            addMultiValueExpectations(name, new String[] {value});
        }
    }

    /**
     * Add an expectation for a multi value to the request related mocks.
     */
    protected abstract void addMultiValueExpectations(String name, String []value);

    /**
     * Test invoke provides multiple values for headers that have
     * mulitple values.
     * @throws Exception
     */
    public void testInvokeMultiValue() throws Exception {
        Expression expression = parser.parse(getFunctionQName() +
            "('name')");

        addMultiValueExpectations("name", new String[] {
            "value", "other"
        });

        Value result = expression.evaluate(expressionContext);

        Assert.assertTrue("Result not a sequence",
                   result instanceof Sequence);

        Assert.assertEquals("Result not as",
                     "value other",
                     result.stringValue().asJavaString());
    }

    /**
     * Ensure that multiple values can be accessed via the [number] predicate
     * @throws Exception if an error occurs
     */
    public void testInvokeSecondValueWithPredicate() throws Exception {
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 'default')[2]");

        addMultiValueExpectations("name", new String[] {
                    "value", "another value"
        });

        Value result = expression.evaluate(expressionContext);

        Assert.assertTrue("Result not a sequence",
                   result instanceof Sequence);

        Assert.assertEquals("Result not as",
                     "another value",
                     result.stringValue().asJavaString());

    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Aug-03	1221/2	doug	VBM:2003080702 Fixed issue with expression predicates

 11-Aug-03	1017/1	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
