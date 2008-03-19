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

package com.volantis.styling.integration.expressions;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.expressions.EvaluationContextMock;
import com.volantis.styling.impl.expressions.Arguments;
import com.volantis.styling.impl.expressions.ArgumentsImpl;
import com.volantis.styling.impl.expressions.StyleCompiledExpression;
import com.volantis.styling.impl.expressions.StylingExpressionMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link com.volantis.styling.impl.expressions.ArgumentsImpl}
 */
public class ArgumentsTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private EvaluationContextMock evaluationContextMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        evaluationContextMock = new EvaluationContextMock(
                "evaluationContextMock", expectations);
    }

    /**
     * Ensure that empty arguments are handled properly.
     */
    public void testEmptyArguments() {

        Arguments arguments;

        List values = new ArrayList();
        List evaluated;

        arguments = new ArgumentsImpl(values, false);
        evaluated = arguments.evaluate(evaluationContextMock);
        assertEquals(values, evaluated);
    }

    /**
     * Ensure that fixed arguments are handled properly.
     */
    public void testFixedArguments() {

        Arguments arguments;

        List values = Arrays.asList(new StyleValue[]{
            STYLE_VALUE_FACTORY.getString(null, "arg 1"),
            STYLE_VALUE_FACTORY.getString(null, "arg 2"),
            STYLE_VALUE_FACTORY.getString(null, "arg 3"),
        });

        arguments = new ArgumentsImpl(values, false);
        List evaluated = arguments.evaluate(evaluationContextMock);
        assertSame(values, evaluated);
    }

    /**
     * Ensure that variable arguments are handled properly.
     */
    public void testVariableArguments() {

        Arguments arguments;


        // =====================================================================
        //   Create Mocks
        // =====================================================================
        final StylingExpressionMock expressionMock =
                new StylingExpressionMock("expressionMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        StyleValue result = STYLE_VALUE_FACTORY.getString(null, "result");

        expressionMock.expects.evaluate(evaluationContextMock).returns(result);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List values = Arrays.asList(new StyleValue[]{
            STYLE_VALUE_FACTORY.getString(null, "arg 1"),
            new StyleCompiledExpression(expressionMock),
            STYLE_VALUE_FACTORY.getString(null, "arg 3"),
        });

        List expected = Arrays.asList(new StyleValue[]{
            STYLE_VALUE_FACTORY.getString(null, "arg 1"),
            result,
            STYLE_VALUE_FACTORY.getString(null, "arg 3"),
        });

        arguments = new ArgumentsImpl(values, true);
        List evaluated = arguments.evaluate(evaluationContextMock);
        assertEquals(expected, evaluated);
    }
}
