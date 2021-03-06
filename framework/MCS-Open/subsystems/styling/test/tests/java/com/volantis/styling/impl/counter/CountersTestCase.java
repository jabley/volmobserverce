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
package com.volantis.styling.impl.counter;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link Counters}.
 *
 * todo: add some more tests
 */
public class CountersTestCase extends TestCaseAbstract {

    /**
     * Test that the counter names are case insensitive.
     */
    public void testCase() {
        Counters counters = new Counters();

        Counter first = counters.get("first");
        first.reset(2);
        assertEquals(2, counters.get("FIRST").value());
        first.endElement();
    }

    public void testIterate() {

        // TODO: use mocks to test instead

        Counters counters = new Counters();
        Counter first = counters.get("first");
        first.reset(0);
        Counter second = counters.get("second");
        second.reset(0);

        counters.iterate(new CounterIteratee() {
            public void next(Counter counter) {
//                System.out.println(counter.value());
            }
        });
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
