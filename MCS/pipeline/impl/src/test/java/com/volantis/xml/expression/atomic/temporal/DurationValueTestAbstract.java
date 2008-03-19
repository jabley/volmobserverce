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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class DurationValueTestAbstract extends TestCaseAbstract {

    /**
     * Return an instance of duration value to test
     *
     * @param value the string representation of a duration
     * @return an object representing the duration
     */
    public abstract DurationValue getDurationValue(String value);

    /**
     * Return an instance of duration value to test
     *
     * @param millis the duration in milliseconds
     * @return an object representing the duration
     */
    public abstract DurationValue getDurationValue(long millis);

    public void testInitialize() throws Exception {
        {
            // test positive
            DurationValue d = getDurationValue("P");
            doTestDuration(d, true, 0, 0, 0, 0, 0, 0, 0);
        }
        {
            // test negative
            DurationValue d = getDurationValue("-P");
            doTestDuration(d, false, -0, -0, -0, -0, -0, -0, -0);
        }

        {
            // test day only
            DurationValue d = getDurationValue("-P1D");
            doTestDuration(d, false, -0, -0, -1, -0, -0, -0, -0);
        }

        {
            // test full date (yes all values are unlimited)
            DurationValue d = getDurationValue("-P2005Y234M100004D");
            doTestDuration(d, false, -2005, -234, -100004, -0, -0, -0, -0);
        }

        {
            // test seconds and millis only
            DurationValue d = getDurationValue("-PT2.3S");
            doTestDuration(d, false, -0, -0, -0, -0, -0, -2, -3);
        }

        {
            //test seconds only
            DurationValue d = getDurationValue("-PT2S");
            doTestDuration(d, false, -0, -0, -0, -0, -0, -2, -0);
        }

        {
            //test full duration
            DurationValue d = getDurationValue("P2005Y12M365DT48H33M2.7543S");
            doTestDuration(d, true, 2005, 12, 365, 48, 33, 2, 7543);
        }
    }

    public void testMillisecondInit() throws Exception {
        {
            DurationValue d = getDurationValue(3153600000000L);
            // works out to about 99 and a bit years
            assertEquals(99, d.getYears());
        }


    }

    public void testParseFailure() throws Exception {
        {
            try {
                DurationValue d = getDurationValue("P-1Y");
                fail("sign must preceed P");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }

        {
            try {
                DurationValue d = getDurationValue("P23YT");
                fail("T Must be succeeded by a time fragment");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }

        {
            try {
                DurationValue d = getDurationValue("P4.5Y");
                fail("date fragments must be integer");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
        {
            try {
                DurationValue d = getDurationValue("P4.5M");
                fail("date fragments must be integer");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
        {
            try {
                DurationValue d = getDurationValue("P4.5D");
                fail("date fragments must be integer");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
        {
            try {
                DurationValue d = getDurationValue("PT4.5H");
                fail("time fragments must be integer (apart from seconds)");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
        {
            try {
                DurationValue d = getDurationValue("PT4.5M");
                fail("time fragments must be integer (apart from seconds)");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
        {
            try {
                DurationValue d = getDurationValue("dP45M");
                fail("P can only be prefixed by an optional '-'");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
        {
            try {
                DurationValue d = getDurationValue("P45MrD");
                fail("time and date must be integers");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
    }

    /**
     * Simply test that the duration object matches the specified sign days
     * hours minutes etc
     *
     * @param d
     * @param sign
     * @param years
     * @param months
     * @param days
     * @param hours
     * @param minutes
     * @param seconds
     * @param millis
     */
    private void doTestDuration(DurationValue d, boolean sign, int years,
                              int months, int days, int hours,
                              int minutes, double seconds, int millis) {

        assertEquals("Sign must be equal", sign, d.isPositive());
        assertEquals("Years", years, d.getYears());
        assertEquals("months", months, d.getMonths());
        assertEquals("days", days, d.getDays());
        assertEquals("hours", hours, d.getHours());
        assertEquals("minutes", minutes, d.getMinutes());
        // test with margin for error
        assertTrue("seconds", Math.abs(seconds - d.getSeconds()) < 0.001);

    }
}
