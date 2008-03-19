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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.functions;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.expressions.EvaluationContextMock;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;

import java.util.Arrays;

/**
 * Test cases for {@link AttrFunction}.
 */
public class AttrFunctionTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private EvaluationContextMock contextMock;

    protected void setUp() throws Exception {
        super.setUp();

        contextMock = new EvaluationContextMock("contextMock",
                expectations);
    }

    /**
     * Ensure that evaluating the function works correctly when the attribute
     * exists.
     */
    public void testExistingAttribute() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getAttributeValue(null, "xyz").returns("123");

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        AttrFunction function = new AttrFunction();
        StyleValue result = function.evaluate(contextMock, "attr",
            Arrays.asList(new StyleValue[]{
                STYLE_VALUE_FACTORY.getIdentifier(null, "xyz")
            }));
        assertEquals(STYLE_VALUE_FACTORY.getString(null, "123"), result);
    }

    /**
     * Ensure that evaluating the function works correctly when the attribute
     * is missing.
     */
    public void testMissingAttribute() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getAttributeValue(null, "xyz").returns(null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        AttrFunction function = new AttrFunction();
        StyleValue result = function.evaluate(contextMock, "attr",
            Arrays.asList(new StyleValue[]{
                STYLE_VALUE_FACTORY.getIdentifier(null, "xyz")
            }));
        assertEquals(STYLE_VALUE_FACTORY.getString(null, ""), result);
    }

}
