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

import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.expressions.StylingFunction;
import com.volantis.styling.impl.functions.CountersFunction;
import com.volantis.styling.impl.functions.counter.CounterFormatterMock;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the CSS counters() function.
 */
public class CountersFunctionTestCase
        extends CounterFunctionTestAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Ensure that the counter function works with a counter with a single
     * value.
     */
    public void testSingleValueCounter() {
        StylingFunction function = new CountersFunction();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        evaluationContextMock.expects.getCounterValues("horse")
                .returns(new int[]{8});

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List functionArgs = new ArrayList();
        functionArgs.add(getCounterIdentifier("horse"));
        functionArgs.add(STYLE_VALUE_FACTORY.getString(null, "."));
        functionArgs.add(ListStyleTypeKeywords.DECIMAL);
        StyleValue functionValue =
                function.evaluate(evaluationContextMock,
                        "counters", functionArgs);

        assertNotNull("Function should return a value", functionValue);
        assertTrue("Function should return a StyleString",
                functionValue instanceof StyleString);
        String functionString = ((StyleString) functionValue).getString();
        assertEquals("Single-value counter should return that value",
                "8", functionString);
    }

    /**
     * Ensure that the counter function works with a counter with multiple
     * values.
     */
    public void testMultipleValueCounter() {
        StylingFunction function = new CountersFunction();

        // =====================================================================
        //   Set Expectations
        // ==========1===========================================================

        evaluationContextMock.expects.getCounterValues("cow")
                .returns(new int[]{1, 2, 3, 4, 5});

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List functionArgs = new ArrayList();
        functionArgs.add(getCounterIdentifier("cow"));
        functionArgs.add(STYLE_VALUE_FACTORY.getString(null, "."));
        functionArgs.add(ListStyleTypeKeywords.DECIMAL);
        StyleValue functionValue =
                function.evaluate(evaluationContextMock,
                        "counters", functionArgs);

        assertNotNull("Function should return a value", functionValue);
        assertTrue("Function should return a StyleString",
                functionValue instanceof StyleString);
        String functionString = ((StyleString) functionValue).getString();
        assertEquals("Multi-value counter should return those values " +
                "with the specified separator",
                "1.2.3.4.5", functionString);
    }

    /**
     * Ensure that the formatter selector is used properly.
     */
    public void testFormatterSelector()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Another formatter that may be used for one of the values of the
        // counter.
        final CounterFormatterMock otherFormatterMock =
                new CounterFormatterMock("otherFormatterMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        formatterSelectorMock.expects
                .selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA, 0)
                .returns(otherFormatterMock);
        formatterSelectorMock.expects
                .selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA, 1)
                .returns(formatterMock);
        formatterSelectorMock.expects
                .selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA, 2)
                .returns(formatterMock);
        formatterSelectorMock.expects
                .selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA, 3)
                .returns(formatterMock);

        otherFormatterMock.expects
                .formatAsString(ListStyleTypeKeywords.LOWER_ALPHA, 0)
                .returns("0");
        formatterMock.expects
                .formatAsString(ListStyleTypeKeywords.LOWER_ALPHA, 1)
                .returns("A");
        formatterMock.expects
                .formatAsString(ListStyleTypeKeywords.LOWER_ALPHA, 2)
                .returns("B");
        formatterMock.expects
                .formatAsString(ListStyleTypeKeywords.LOWER_ALPHA, 3)
                .returns("C");

        evaluationContextMock.expects.getCounterValues("hippopotamus")
                .returns(new int[]{0, 1, 2, 3});

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        StylingFunction function = new CountersFunction(formatterSelectorMock);

        List functionArgs = new ArrayList();
        functionArgs.add(getCounterIdentifier("hippopotamus"));
        functionArgs.add(STYLE_VALUE_FACTORY.getString(null, "."));
        functionArgs.add(ListStyleTypeKeywords.LOWER_ALPHA);

        StyleValue functionValue =
                function.evaluate(evaluationContextMock,
                        "counter", functionArgs);
        StyleString expected = STYLE_VALUE_FACTORY.getString(null, "0.A.B.C");
        assertEquals(expected, functionValue);
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
