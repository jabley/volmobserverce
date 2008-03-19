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

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;


/**
 * Generic tests for ExpresionFunctions
 */
public abstract class RequestFunctionTestAbstract
        extends ExpressionFunctionTestAbstract {

    /**
     * Add an expectation for a single value to the request related mocks.
     */
    protected abstract void addSingleValueExpectations(String name, String value);

    /**
     * Test invoke with no arguments.
     * @throws Exception
     */
    public void testInvokeNoArgs() throws Exception {
        Expression expression = parser.parse(getFunctionQName() + "()");
        try {
            expression.evaluate(expressionContext);

            fail("Should have had an exception thrown when evaluating " +
                 "expression");
        } catch (ExpressionException e) {
            // Expected condition
        } catch (ExtendedRuntimeException e) {
            // @todo later remove this work-around required for bug in pipeline
        }
    }

    /**
     * Test invoke with a missing entity and no default.
     * @throws Exception
     */
    public void testInvokeMissing() throws Exception {
        Expression expression = parser.parse(getFunctionQName() + "('name')");

        addSingleValueExpectations("name", null);

        Value result = expression.evaluate(expressionContext);

        assertTrue("Result not a sequence",
                   result instanceof Sequence);
        assertTrue("Result sequence not empty",
                   ((Sequence)result).getLength() == 0);
    }

    /**
     * Test invoke with a missing entity and a String default.
     * @throws Exception
     */
    public void testInvokeMissingStringDefault() throws Exception {
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 'value')");

        addSingleValueExpectations("name", null);

        Value result = expression.evaluate(expressionContext);

        assertEquals("Result not as",
                     "value",
                     result.stringValue().asJavaString());
    }

    /**
     * Test invoke with a missing entity and a numeric default.
     * @throws Exception
     */
    public void testInvokeMissingNumericDefault() throws Exception {
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 2)");

        addSingleValueExpectations("name", null);

        Value result = expression.evaluate(expressionContext);

        assertEquals("Result not as",
                     "2", // Numerics represent as floating
                     result.stringValue().asJavaString());
    }

    /**
     * Test invoke with an existing entity and a default. The
     * existing entity should be used thus ignoring the default.
     * Sub-classes must perform additional setup for this test
     * by overriding setUpInvokeHeaderIgnoreDefault.
     * @throws Exception
     */
    public void testInvokeIgnoreDefault() throws Exception {
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 'fred')");

        addSingleValueExpectations("name", "value");

        Value result = expression.evaluate(expressionContext);

        assertEquals("Result not as",
                     "value",
                     result.stringValue().asJavaString());
    }

    /**
     * Test that the function can be evaluated with a [1] predicate
     * @throws Exception if an error occurs
     */
    public void testInvokeWithNumericPredicate() throws Exception {
        // parse the expression
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 'default')[1]");

        // register the name value pair with the request
        addSingleValueExpectations("name", "value");

        // evaluate the expression
        Value result = expression.evaluate(expressionContext);

        // ensure that the correct value was returned
        assertEquals("Unexpected value returned",
                     "value",
                     result.stringValue().asJavaString());
    }

    /**
     * Test that the function returns the default argument when a [1] predicate
     * is specified and no name value pair has been added to the request
     * @throws Exception if an error occurs
     */
    public void testInvokeWithNumericPredicateDefaUlt() throws Exception {
        // parse the expression
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 'default')[1]");

        addSingleValueExpectations("name", null);

        // evaluate the expression
        Value result = expression.evaluate(expressionContext);

        // ensure that the correct value was returned
        assertEquals("Unexpected value returned",
                     "default",
                     result.stringValue().asJavaString());
    }

    /**
     * Test that the function returns an empty sequence when a [2] predicate
     * is specified
     * @throws Exception if an error occurs
     */
    public void testInvokeWithNumericPredicateOutOfBounds() throws Exception {
        // parse the expression
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 'default')[2]");

        addSingleValueExpectations("name", null);

        // evaluate the expression
        Value result = expression.evaluate(expressionContext);

        assertTrue("Result was not a Sequence instance",
                   result instanceof Sequence);

        Sequence sequence = (Sequence)result;

        // ensure that the correct value was returned
        assertEquals("Sequence was not empty",
                     0,
                     sequence.getLength());
    }

     /**
     * Test that the function returns an empty sequence when a [2] predicate
     * is specified and default is provided via constructor
     * @throws Exception if an error occurs
     */
    public void testDefaultInvokeWithNumericPredicateOutOfBounds() throws Exception {
        // parse the expression
        Expression expression = parser.parse(getFunctionQName() +
            "('name', 'default')[2]");

        // register the name value pair with the request
        addSingleValueExpectations("name", "value");

        // evaluate the expression
        Value result = expression.evaluate(expressionContext);

        assertTrue("Result was not a Sequence instance",
                   result instanceof Sequence);

        Sequence sequence = (Sequence)result;

        // ensure that the correct value was returned
        assertEquals("Sequence was not empty",
                     0,
                     sequence.getLength());
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Oct-03	1623/1	steve	VBM:2003102006 Undo 2003090501

 22-Oct-03	1620/1	steve	VBM:2003102006 Backout changes for 2003090501

 22-Aug-03	1221/1	doug	VBM:2003080702 Fixed issue with expression predicates

 14-Aug-03	1096/1	adrian	VBM:2003070805 updated usages of XMLPipelineContext and PropertyContainer to match pipeline api changes

 12-Aug-03	1017/3	allan	VBM:2003070409 Moved mock test helpers

 11-Aug-03	1017/1	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
