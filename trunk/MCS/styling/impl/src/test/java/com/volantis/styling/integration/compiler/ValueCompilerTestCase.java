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

package com.volantis.styling.integration.compiler;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.compiler.FunctionResolverMock;
import com.volantis.styling.expressions.EvaluationContextMock;
import com.volantis.styling.expressions.StylingFunctionMock;
import com.volantis.styling.impl.compiler.ValueCompiler;
import com.volantis.styling.impl.compiler.ValueCompilerImpl;
import com.volantis.styling.impl.expressions.StyleCompiledExpression;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Arrays;
import java.util.Collections;

public class ValueCompilerTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private CSSParser cssParser;

    protected void setUp() throws Exception {
        super.setUp();

        CSSParserFactory factory = CSSParserFactory.getDefaultInstance();
        cssParser = factory.createStrictParser();
    }

    /**
     * Ensure that a function call as a parameter is compiled and evaluated
     * properly.
     */
    public void testCompileAndEvaluateFunctionAsParameter() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final FunctionResolverMock functionResolverMock =
                new FunctionResolverMock("functionResolverMock", expectations);

        final StylingFunctionMock fooFunctionMock =
                new StylingFunctionMock("fooFunctionMock", expectations);
        final StylingFunctionMock barFunctionMock =
                new StylingFunctionMock("barFunctionMock", expectations);

        final EvaluationContextMock evaluationContextMock =
                new EvaluationContextMock("evaluationContextMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        functionResolverMock.expects.resolve("foo").returns(fooFunctionMock);
        functionResolverMock.expects.resolve("bar").returns(barFunctionMock);

        StyleValue barResult = STYLE_VALUE_FACTORY.getString(null, "bar result");
        StyleValue fooResult = STYLE_VALUE_FACTORY.getString(null, "foo result");

        // The bar function should be evaluated first.
        barFunctionMock.expects.evaluate(evaluationContextMock, "bar",
                Collections.EMPTY_LIST).returns(barResult);

        // The foo function should then be evaluated with the result of
        // evaluating the bar function as its argument.
        fooFunctionMock.expects.evaluate(evaluationContextMock, "foo",
                Arrays.asList(new StyleValue[] {
                    barResult
                })).returns(fooResult);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        StyleValue value = cssParser.parseStyleValue(
                StylePropertyDetails.MCS_CONTAINER,
                "foo(bar())");

        ValueCompiler compiler = new ValueCompilerImpl(functionResolverMock);
        StyleCompiledExpression compiledValue = (StyleCompiledExpression)
                compiler.compile(value);
        StyleValue evaluatedValue =
                compiledValue.evaluate(evaluationContextMock);
        assertSame(fooResult, evaluatedValue);
    }
}
