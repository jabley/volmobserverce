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

import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.impl.functions.counter.CounterFormatter;
import com.volantis.styling.impl.functions.counter.DecimalFormatter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link com.volantis.styling.impl.functions.counter.DecimalFormatter}.
 */
public class DecimalFormatterTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that the decimal formatter returns a {@link StyleInteger}.
     *
     * @throws Exception
     */
    public void testReturnsStyleInteger() throws Exception {

        CounterFormatter formatter = new DecimalFormatter();
        StyleValue result = formatter.formatAsStyleValue(
                ListStyleTypeKeywords.DECIMAL, 4);
        assertTrue("Must be StyleInteger", result instanceof StyleInteger);
    }
}
