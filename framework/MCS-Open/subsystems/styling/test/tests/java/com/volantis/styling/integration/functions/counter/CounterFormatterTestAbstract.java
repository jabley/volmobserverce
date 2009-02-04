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

import com.volantis.mcs.themes.StyleString;
import com.volantis.styling.impl.functions.counter.CounterFormatter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for tests for {@link CounterFormatter} implementations.
 */
public abstract class CounterFormatterTestAbstract
        extends TestCaseAbstract {

    protected CounterFormatter formatter;

    protected void setUp() throws Exception {
        super.setUp();

        formatter = createFormatter();
    }

    /**
     * Create the formatter to test.
     *
     * @return The formatter to test.
     */
    protected abstract CounterFormatter createFormatter();

    /**
     * Test the formatter.
     *
     * @param counterValue   The input value.
     * @param expectedResult The expected result.
     */
    protected void doTestFormatter(
            final int counterValue, final String expectedResult) {
        StyleString value = (StyleString) formatter.formatAsStyleValue(null,
                counterValue);
        assertEquals(expectedResult, value.getString());
    }
}
