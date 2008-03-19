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
 * Test cases for {@link Time}.
 */
public class TimeTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that cannot create a time with a negative time.
     */
    public void testNegativeTime() throws Exception {
        try {
            Time.inMilliSeconds(-1);
            fail("Allowed negative time");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Ensure that {@link Time#getPeriodSince(Time)} method works correctly.
     */
    public void testPeriodSince() {
        Time before = Time.inMilliSeconds(100);
        Time after = Time.inMilliSeconds(250);
        Period period = after.getPeriodSince(before);
        assertEquals(150, period.inMillis());

        period = Time.NEVER.getPeriodSince(before);
        assertEquals(Period.INDEFINITELY, period);

        try {
            period = before.getPeriodSince(Time.NEVER);
            fail("Did not detect attempt to get period since NEVER");
        } catch (IllegalStateException expected) {
            assertEquals("Cannot determine period since never",
                    expected.getMessage());
        }
    }

    /**
     * Ensure that {@link Time#getPeriodSince(Time)} method works correctly.
     */
    public void testAddPeriod() {
        // Add a finite period to a finite time.
        Time before = Time.inMilliSeconds(100);
        Period period = Period.inSeconds(100);
        Time after = before.addPeriod(period);
        assertEquals(100100, after.inMillis());

        // Add a finite period to an infinite time.
        after = Time.NEVER.addPeriod(period);
        assertEquals(after, Time.NEVER);

        // Add an infinite period to a finite time.
        after = before.addPeriod(Period.INDEFINITELY);
        assertEquals(after, Time.NEVER);

        // Add an infinite period to an infinite time.
        after = Time.NEVER.addPeriod(Period.INDEFINITELY);
        assertEquals(after, Time.NEVER);
    }
}
