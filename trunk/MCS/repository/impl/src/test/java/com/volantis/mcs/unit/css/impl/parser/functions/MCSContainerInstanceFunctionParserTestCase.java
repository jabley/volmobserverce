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

package com.volantis.mcs.unit.css.impl.parser.functions;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.css.impl.parser.functions.MCSContainerInstanceFunctionParser;
import com.volantis.mcs.css.impl.parser.functions.FunctionParser;
import com.volantis.mcs.css.impl.parser.ParserContextMock;
import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserValidationException;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleString;

import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link MCSContainerInstanceFunctionParser}.
 */
public class MCSContainerInstanceFunctionParserTestCase
    extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();
    private ParserContextMock parserContextMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        parserContextMock = new ParserContextMock(
                "parserContextMock", expectations);
    }

    /**
     * Test that an empty set of arguments is wrong.
     */
    public void testEmpty() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        parserContextMock.expects.addDiagnostic(
                CSSParserMessages.WRONG_NUMBER_ARGUMENTS_RANGE,
                new Object[]{
                    "foo",
                    new Integer(1),
                    "any",
                    new Integer(0)
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List arguments = Arrays.asList(new Object[] {
        });

        MCSContainerInstanceFunctionParser parser =
                new MCSContainerInstanceFunctionParser();

        parse(parser, arguments, true);
    }

    /**
     * Test that the wrong type for a name
     */
    public void testWrongTypeForName() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        parserContextMock.expects.addDiagnostic(
                CSSParserMessages.WRONG_ARGUMENT_TYPE,
                new Object[]{
                    "foo",
                    new Integer(0),
                    "string",
                    "integer",
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List arguments = Arrays.asList(new Object[] {
            STYLE_VALUE_FACTORY.getInteger(null, 9)
        });

        MCSContainerInstanceFunctionParser parser =
                new MCSContainerInstanceFunctionParser();

        parse(parser, arguments, true);
    }

    private StyleValue parse(
            FunctionParser parser, List arguments, boolean expectedToFail) {

        try {
            StyleValue value = parser.parse(
                    parserContextMock, null, "foo", arguments);
            if (expectedToFail) {
                fail("Did not fail as expected");
            }
            return value;
        } catch (ParserValidationException e) {
            if (!expectedToFail) {
                fail("Received unexpected exception");
            }
        }

        return null;
    }

    /**
     * Test that the wrong type for an argument
     */
    public void testWrongTypeForArgument() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        parserContextMock.expects.addDiagnostic(
                CSSParserMessages.WRONG_ARGUMENT_TYPE,
                new Object[]{
                    "foo",
                    new Integer(1),
                    "integer",
                    "string",
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List arguments = Arrays.asList(new Object[] {
            STYLE_VALUE_FACTORY.getString(null, "string"),
            STYLE_VALUE_FACTORY.getString(null, "argument")
        });

        MCSContainerInstanceFunctionParser parser =
                new MCSContainerInstanceFunctionParser();

        parse(parser, arguments, true);
    }

    /**
     * Test successful.
     */
    public void testSuccess() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List arguments = Arrays.asList(new Object[] {
            STYLE_VALUE_FACTORY.getString(null, "string"),
            STYLE_VALUE_FACTORY.getInteger(null, 0),
            STYLE_VALUE_FACTORY.getInteger(null, 2)
        });

        MCSContainerInstanceFunctionParser parser =
                new MCSContainerInstanceFunctionParser();

        StyleFunctionCall call =
                STYLE_VALUE_FACTORY.getFunctionCall(null, "foo", arguments);

        StyleValue value = parse(parser, arguments, false);
        assertEquals("Expected Value", call, value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/
