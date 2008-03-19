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

package com.volantis.styling.integration.functions.counter;

import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.impl.functions.counter.AlphabeticFormatterSelector;
import com.volantis.styling.impl.functions.counter.CounterFormatter;
import com.volantis.styling.impl.functions.counter.CounterFormatterMock;
import com.volantis.styling.impl.functions.counter.CounterFormatterSelector;
import com.volantis.styling.impl.functions.counter.DecimalFormatter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link com.volantis.styling.impl.functions.counter.AlphabeticFormatterSelector}.
 */
public class AlphabeticFormatterSelectorTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that if the counter value is out of the range supported by the
     * letter formatters that it returns the {@link DecimalFormatter}.
     */
    public void testOutOfRangeSelectsDecimalFormatter()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        final CounterFormatterMock formatterMock =
                new CounterFormatterMock("formatterMock", expectations);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        CounterFormatterSelector selector =
                new AlphabeticFormatterSelector(formatterMock);
        CounterFormatter formatter;

        // Test zero.
        formatter = selector.selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA,
                0);
        assertSame(DecimalFormatter.getDefaultInstance(), formatter);

        // Test negative.
        formatter = selector.selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA,
                -9);
        assertSame(DecimalFormatter.getDefaultInstance(), formatter);
    }

    /**
     * Ensure that if the counter value is in the range supported by the
     * letter formatters that it returns the formatter supplied to the
     * constructor.
     */
    public void testInRangeReturnsSuppliedFormatter()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        final CounterFormatterMock formatterMock =
                new CounterFormatterMock("formatterMock", expectations);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        CounterFormatterSelector selector =
                new AlphabeticFormatterSelector(formatterMock);
        CounterFormatter formatter;

        // Test 1.
        formatter = selector.selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA,
                1);
        assertSame(formatterMock, formatter);

        // Test 1000.
        formatter = selector.selectFormatter(ListStyleTypeKeywords.LOWER_ALPHA,
                1000);
        assertSame(formatterMock, formatter);
    }
}
