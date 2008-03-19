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
import com.volantis.shared.system.SystemClockMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link Countdown}.
 */
public class CountdownTestCase
        extends TestCaseAbstract {

    private SystemClockMock clockMock;

    protected void setUp() throws Exception {
        super.setUp();

        clockMock = new SystemClockMock("clockMock", expectations);
    }

    /**
     * Ensure that the countdown for an indefinite period is treated specially.
     */
    public void testIndefinitePeriod() throws Exception {

        Countdown countdown = Countdown.getCountdown(
                Period.INDEFINITELY, clockMock);
        Period remaining = countdown.countdown();
        assertEquals(Period.INDEFINITELY, remaining);
    }

    /**
     * Ensure that the countdown for a limited period is handled correctly.
     */
    public void testLimitedPeriod() throws Exception {

        // The start time.
        clockMock.expects.getCurrentTime().returns(Time.inSeconds(0));

        Countdown countdown = Countdown.getCountdown(
                Period.inSeconds(10), clockMock);

        Period remaining;

        // The first call to countdown occurs after 5 seconds has passed.
        clockMock.expects.getCurrentTime().returns(Time.inSeconds(5));
        remaining = countdown.countdown();
        assertEquals(Period.inSeconds(5), remaining);
                                         
        // The next call to countdown occurs after 10 seconds has passed must
        // fail as it has timed out.
        clockMock.expects.getCurrentTime().returns(Time.inSeconds(10));
        try {
            countdown.countdown();
            fail("Failed to time out");
        } catch(TimedOutException expected) {
            assertEquals("Operation that started at 0ms and had a timeout of " +
                    "10000ms timed out at 10000ms", expected.getMessage());
        }
    }
}
