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

public class TimeComparatorTestCase
 extends TestCaseAbstract {
    private Period PERIOD_100MS;
    private Period PERIOD_200MS;
    private Time TIME_100MS;
    private Time TIME_200MS;

    protected void setUp() throws Exception {
        super.setUp();

        PERIOD_100MS = Period.inMilliSeconds(100);
        PERIOD_200MS = Period.inMilliSeconds(200);

        TIME_100MS = Time.inMilliSeconds(100);
        TIME_200MS = Time.inMilliSeconds(200);
    }

    /**
     * Ensure that the greater than or equal period comparator works.
     */
    public void testGreaterThanOrEqualPeriod() {
        Comparator comparator = Comparator.GE;
        assertTrue(comparator.compare(PERIOD_200MS, PERIOD_100MS));
        assertTrue(comparator.compare(PERIOD_200MS, PERIOD_200MS));
        assertFalse(comparator.compare(PERIOD_100MS, PERIOD_200MS));

        assertTrue(comparator.compare(Period.INDEFINITELY, PERIOD_100MS));

        assertFalse(comparator.compare(PERIOD_100MS, Period.INDEFINITELY));

        assertFalse(comparator.compare(Period.INDEFINITELY,
                Period.INDEFINITELY));
    }

    /**
     * Ensure that the greater than period comparator works.
     */
    public void testGreaterThanPeriod() {
        Comparator comparator = Comparator.GT;
        assertTrue(comparator.compare(PERIOD_200MS, PERIOD_100MS));
        assertFalse(comparator.compare(PERIOD_200MS, PERIOD_200MS));
        assertFalse(comparator.compare(PERIOD_100MS, PERIOD_200MS));

        assertTrue(comparator.compare(Period.INDEFINITELY, PERIOD_100MS));

        assertFalse(comparator.compare(PERIOD_100MS, Period.INDEFINITELY));

        assertFalse(comparator.compare(Period.INDEFINITELY,
                Period.INDEFINITELY));
    }

    /**
     * Ensure that the less than or equal period comparator works.
     */
    public void testLessThanOrEqualPeriod() {
        Comparator comparator = Comparator.LE;
        assertTrue(comparator.compare(PERIOD_100MS, PERIOD_200MS));
        assertTrue(comparator.compare(PERIOD_100MS, PERIOD_100MS));
        assertFalse(comparator.compare(PERIOD_200MS, PERIOD_100MS));

        assertFalse(comparator.compare(Period.INDEFINITELY, PERIOD_100MS));

        assertTrue(comparator.compare(PERIOD_100MS, Period.INDEFINITELY));

        assertFalse(comparator.compare(Period.INDEFINITELY,
                Period.INDEFINITELY));
    }

    /**
     * Ensure that the less than period comparator works.
     */
    public void testLessThanPeriod() {
        Comparator comparator = Comparator.LT;
        assertTrue(comparator.compare(PERIOD_100MS, PERIOD_200MS));
        assertFalse(comparator.compare(PERIOD_100MS, PERIOD_100MS));
        assertFalse(comparator.compare(PERIOD_200MS, PERIOD_100MS));

        assertFalse(comparator.compare(Period.INDEFINITELY, PERIOD_100MS));

        assertTrue(comparator.compare(PERIOD_100MS, Period.INDEFINITELY));

        assertFalse(comparator.compare(Period.INDEFINITELY,
                Period.INDEFINITELY));
    }

    /**
     * Ensure that the equal period comparator works.
     */
    public void testEqualPeriod() {
        Comparator comparator = Comparator.EQ;
        assertFalse(comparator.compare(PERIOD_100MS, PERIOD_200MS));
        assertTrue(comparator.compare(PERIOD_100MS, PERIOD_100MS));
        assertFalse(comparator.compare(PERIOD_200MS, PERIOD_100MS));

        assertFalse(comparator.compare(Period.INDEFINITELY, PERIOD_100MS));

        assertFalse(comparator.compare(PERIOD_100MS, Period.INDEFINITELY));

        assertFalse(comparator.compare(Period.INDEFINITELY,
                Period.INDEFINITELY));
    }

    /**
     * Ensure that the not equal period comparator works.
     */
    public void testNotEqualPeriod() {
        Comparator comparator = Comparator.NE;
        assertTrue(comparator.compare(PERIOD_100MS, PERIOD_200MS));
        assertFalse(comparator.compare(PERIOD_100MS, PERIOD_100MS));
        assertTrue(comparator.compare(PERIOD_200MS, PERIOD_100MS));

        assertTrue(comparator.compare(Period.INDEFINITELY, PERIOD_100MS));

        assertTrue(comparator.compare(PERIOD_100MS, Period.INDEFINITELY));

        assertFalse(comparator.compare(Period.INDEFINITELY,
                Period.INDEFINITELY));
    }


    /**
     * Ensure that the greater than or equal time comparator works.
     */
    public void testGreaterThanOrEqualTime() {
        Comparator comparator = Comparator.GE;
        assertTrue(comparator.compare(TIME_200MS, TIME_100MS));
        assertTrue(comparator.compare(TIME_200MS, TIME_200MS));
        assertFalse(comparator.compare(TIME_100MS, TIME_200MS));

        assertTrue(comparator.compare(Time.NEVER, TIME_100MS));

        assertFalse(comparator.compare(TIME_100MS, Time.NEVER));

        assertFalse(comparator.compare(Time.NEVER, Time.NEVER));
    }

    /**
     * Ensure that the greater than time comparator works.
     */
    public void testGreaterThanTime() {
        Comparator comparator = Comparator.GT;
        assertTrue(comparator.compare(TIME_200MS, TIME_100MS));
        assertFalse(comparator.compare(TIME_200MS, TIME_200MS));
        assertFalse(comparator.compare(TIME_100MS, TIME_200MS));

        assertTrue(Comparator.GE.compare(Time.NEVER, TIME_100MS));

        assertFalse(Comparator.GE.compare(TIME_100MS, Time.NEVER));

        assertFalse(comparator.compare(Time.NEVER, Time.NEVER));
    }

    /**
     * Ensure that the less than or equal time comparator works.
     */
    public void testLessThanOrEqualTime() {
        Comparator comparator = Comparator.LE;
        assertTrue(comparator.compare(TIME_100MS, TIME_200MS));
        assertTrue(comparator.compare(TIME_100MS, TIME_100MS));
        assertFalse(comparator.compare(TIME_200MS, TIME_100MS));

        assertFalse(comparator.compare(Time.NEVER, TIME_100MS));

        assertTrue(comparator.compare(TIME_100MS, Time.NEVER));

        assertFalse(comparator.compare(Time.NEVER, Time.NEVER));
    }

    /**
     * Ensure that the less than time comparator works.
     */
    public void testLessThanTime() {
        Comparator comparator = Comparator.LT;
        assertTrue(comparator.compare(TIME_100MS, TIME_200MS));
        assertFalse(comparator.compare(TIME_100MS, TIME_100MS));
        assertFalse(comparator.compare(TIME_200MS, TIME_100MS));

        assertFalse(comparator.compare(Time.NEVER, TIME_100MS));

        assertTrue(comparator.compare(TIME_100MS, Time.NEVER));

        assertFalse(comparator.compare(Time.NEVER, Time.NEVER));
    }

    /**
     * Ensure that the equal time comparator works.
     */
    public void testEqualTime() {
        Comparator comparator = Comparator.EQ;
        assertFalse(comparator.compare(TIME_100MS, TIME_200MS));
        assertTrue(comparator.compare(TIME_100MS, TIME_100MS));
        assertFalse(comparator.compare(TIME_200MS, TIME_100MS));

        assertFalse(comparator.compare(Time.NEVER, TIME_100MS));

        assertFalse(comparator.compare(TIME_100MS, Time.NEVER));

        assertFalse(comparator.compare(Time.NEVER, Time.NEVER));
    }

    /**
     * Ensure that the not equal time comparator works.
     */
    public void testNotEqualTime() {
        Comparator comparator = Comparator.NE;
        assertTrue(comparator.compare(TIME_100MS, TIME_200MS));
        assertFalse(comparator.compare(TIME_100MS, TIME_100MS));
        assertTrue(comparator.compare(TIME_200MS, TIME_100MS));

        assertTrue(comparator.compare(Time.NEVER, TIME_100MS));

        assertTrue(comparator.compare(TIME_100MS, Time.NEVER));

        assertFalse(comparator.compare(Time.NEVER, Time.NEVER));
    }
}
