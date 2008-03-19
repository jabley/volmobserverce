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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.time;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link Period}.
 */
public class PeriodTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that can create a period with a negative value.
     */
    public void testNegativePeriod() throws Exception {
        new Period(-1);
    }

    /**
     * Ensure that {@link Period#treatZeroAsIndefinitely(long)} works
     * correctly.
     */
    public void testTreatZeroAsIndefinitely() {
        Period period = Period.treatZeroAsIndefinitely(0);
        assertSame(period, Period.INDEFINITELY);
    }

    /**
     * Ensure that {@link Period#inMillisTreatIndefinitelyAsZero()} works
     * correctly.
     */
    public void testInMillisTreatIndefinitelyAsZero() {
        assertEquals(100, new Period(100).inMillisTreatIndefinitelyAsZero());
        assertEquals(0, Period.INDEFINITELY.inMillisTreatIndefinitelyAsZero());
        try {
            new Period(0).inMillisTreatIndefinitelyAsZero();
        } catch(IllegalStateException expected) {
        }
    }

    /**
     * Ensure that {@link Period#inMillis()} works correctly.
     */
    public void testInMillis() {
        assertEquals(100, new Period(100).inMillis());
        try {
            Period.INDEFINITELY.inMillis();
        } catch(IllegalStateException expected) {
        }
    }

    /**
     * Ensure that {@link Period#toString()} works correctly.
     */
    public void testToString() {
        assertEquals("100ms", new Period(100).toString());
        assertEquals("indefinitely", Period.INDEFINITELY.toString());
    }
}
