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
import com.volantis.styling.expressions.StylingFunctionMock;
import com.volantis.styling.impl.expressions.ArgumentsMock;
import com.volantis.styling.impl.expressions.FunctionCall;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for {@link FunctionCall}.
 */
public class FunctionCallTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Ensure that function call evaluation evaluates the arguments as well.
     */
    public void testEvaluate() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final StylingFunctionMock stylingFunctionMock =
                new StylingFunctionMock("stylingFunctionMock", expectations);
        final ArgumentsMock argumentsMock =
                new ArgumentsMock("argumentsMock", expectations);
        final EvaluationContextMock evaluationContextMock =
                new EvaluationContextMock("evaluationContextMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        List values = Arrays.asList(new StyleValue[] {
            STYLE_VALUE_FACTORY.getString(null, "arg 1"),
            STYLE_VALUE_FACTORY.getString(null, "arg 2"),
            STYLE_VALUE_FACTORY.getString(null, "arg 3"),
        });

        StyleValue result = STYLE_VALUE_FACTORY.getString(null, "result");

        argumentsMock.expects.evaluate(evaluationContextMock).returns(values);
        stylingFunctionMock.expects
                .evaluate(evaluationContextMock, "fred", values)
                .returns(result);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FunctionCall call = new FunctionCall("fred", stylingFunctionMock,
                argumentsMock);
        call.evaluate(evaluationContextMock);
    }

}
