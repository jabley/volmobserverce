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
package com.volantis.styling.integration.functions;

import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.expressions.StylingFunction;
import com.volantis.styling.impl.functions.CounterFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the CSS counter() function.
 */
public class CounterFunctionTestCase
        extends CounterFunctionTestAbstract {
    /**
     * Get a CSS counter() function instance.
     *
     * @return A CSSFunction for the counter() function
     */
    private StylingFunction getFunction() {
        return new CounterFunction();
    }

    /**
     * Tests retrieving the value of a counter.
     *
     * @throws Exception if an error occurs
     */
    public void testCounter() throws Exception {

        StylingFunction function = getFunction();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        evaluationContextMock.expects.getCounterValue("cow")
                .returns(6).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List functionArgs = new ArrayList();
        functionArgs.add(getCounterIdentifier("cow"));
        StyleValue functionValue =
                function.evaluate(evaluationContextMock,
                        "counter", functionArgs);

        assertNotNull("Function should return a value", functionValue);
        assertTrue("Function should return a StyleInteger",
                functionValue instanceof StyleInteger);
        int functionInt = ((StyleInteger) functionValue).getInteger();
        assertEquals("Existing counter should have specified value",
                6, functionInt);
    }

    /**
     * Tests retrieving the value of a counter with a specified style.
     *
     * @throws Exception
     */
    public void testStyledCounter()
            throws Exception {

        StylingFunction function = getFunction();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        evaluationContextMock.expects.getCounterValue("hippopotamus")
                .returns(6);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List functionArgs = new ArrayList();
        functionArgs.add(getCounterIdentifier("hippopotamus"));
        functionArgs.add(ListStyleTypeKeywords.LOWER_ALPHA);

        StyleValue functionValue =
                function.evaluate(evaluationContextMock,
                        "counter", functionArgs);

        assertNotNull("Function should return a value", functionValue);
        assertTrue("Function should return a StyleString",
                functionValue instanceof StyleString);
        String functionString =
                ((StyleString) functionValue).getString();
        assertEquals("Existing counter should have specified value",
                "f", functionString);
    }

    /**
     * Ensure that the formatter selector is used properly.
     */
    public void testFormatterSelector()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        formatterSelectorMock.expects
                .selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA, 6)
                .returns(formatterMock);

        StyleString result =
            StyleValueFactory.getDefaultInstance().getString(null, "blah");
        formatterMock.expects.formatAsStyleValue(
                ListStyleTypeKeywords.LOWER_ALPHA, 6)
                .returns(result);

        evaluationContextMock.expects.getCounterValue("hippopotamus")
                .returns(6);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        StylingFunction function = new CounterFunction(formatterSelectorMock);

        List functionArgs = new ArrayList();
        functionArgs.add(getCounterIdentifier("hippopotamus"));
        functionArgs.add(ListStyleTypeKeywords.LOWER_ALPHA);

        StyleValue functionValue =
                function.evaluate(evaluationContextMock,
                        "counter", functionArgs);
        assertSame(result, functionValue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 ===========================================================================
*/
