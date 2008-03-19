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
import com.volantis.styling.impl.functions.counter.UpperAlphaLatinFormatter;

/**
 * Test cases for {@link UpperAlphaLatinFormatter}.
 */
public class UpperAlphaLatinFormatterTestCase
        extends CounterFormatterTestAbstract {

    // Javadoc inherited.
    protected CounterFormatter createFormatter() {
        return new UpperAlphaLatinFormatter();
    }

    /**
     * Ensure upper alpha / latin tests work properly.
     */
    public void testFormatting() throws Exception {
        doTestFormatter(2, "B");
        doTestFormatter(19, "S");
        doTestFormatter(1055, "ANO");
        doTestFormatter(18279, "AAAA");
    }
}
