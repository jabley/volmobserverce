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

import com.volantis.styling.impl.functions.counter.CounterFormatter;
import com.volantis.styling.impl.functions.counter.DecimalLeadingZeroFormatter;

/**
 * Test cases for {@link DecimalLeadingZeroFormatter}.
 */
public class DecimalLeadingZeroFormatterTestCase
        extends CounterFormatterTestAbstract {

    protected CounterFormatter createFormatter() {
        return new DecimalLeadingZeroFormatter();
    }

    /**
     * Ensure zero renders as 00.
     */
    public void testZero() throws Exception {
        doTestFormatter(0, "00");
    }

    /**
     * Ensure positive single digit renders as 0d where d is the digit.
     */
    public void testPositiveSingleDigits() throws Exception {
        doTestFormatter(1, "01");
        doTestFormatter(4, "04");
        doTestFormatter(9, "09");
    }

    /**
     * Ensure negative single digit renders as -0d where d is the digit.
     */
    public void testNegativeSingleDigits() throws Exception {
        doTestFormatter(-1, "-01");
        doTestFormatter(-4, "-04");
        doTestFormatter(-9, "-09");
    }

    /**
     * Ensure multiple digits render as normal.
     */
    public void testMultipleDigits() throws Exception {
        doTestFormatter(-10, "-10");
        doTestFormatter(10, "10");
        doTestFormatter(999, "999");
    }
}
