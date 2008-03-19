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
package com.volantis.mcs.expression.functions.request;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.Value;
import junit.framework.Assert;

/**
 * Test case for ExpressionFunctions that can only provide single values.
 */
public abstract class SingleValueFunctionTestAbstract
        extends RequestFunctionTestAbstract {

    /**
     * Test invoke provides the first value for headers that have
     * mulitple values.
     * @throws Exception
     */
    public void testInvokeIgnoreSecondValue() throws Exception {
        Expression expression = parser.parse(getFunctionQName() +
            "('name')");

        addSingleValueExpectations("name", "value");
        addSingleValueExpectations("name", "other");

        Value result = expression.evaluate(expressionContext);

        Assert.assertEquals("Result not as",
                     "value",
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

 11-Aug-03	1017/1	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
