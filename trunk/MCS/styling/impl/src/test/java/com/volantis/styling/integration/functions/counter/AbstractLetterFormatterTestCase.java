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

import com.volantis.styling.impl.functions.counter.AbstractAlphabeticFormatter;
import com.volantis.styling.impl.functions.counter.CounterFormatter;

/**
 * Test cases for {@link AbstractAlphabeticFormatter}.
 */
public class AbstractLetterFormatterTestCase
        extends CounterFormatterTestAbstract {

    /**
     * Array of some test letters.
     */
    protected static String[] TEST_LETTERS = new String[]{
        "A", "B", "C",
    };

    protected CounterFormatter createFormatter() {
        return new AbstractAlphabeticFormatter(TEST_LETTERS);
    }

    /**
     * Ensure that formatting is correct.
     */
    public void testFormatting()
            throws Exception {

        doTestFormatter(1, "A");
        doTestFormatter(2, "B");
        doTestFormatter(3, "C");
        doTestFormatter(4, "AA");
        doTestFormatter(5, "AB");
        doTestFormatter(6, "AC");
        doTestFormatter(7, "BA");
        doTestFormatter(8, "BB");
        doTestFormatter(9, "BC");
        doTestFormatter(10, "CA");
        doTestFormatter(11, "CB");
        doTestFormatter(12, "CC");
        doTestFormatter(13, "AAA");
    }

}
