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

package com.volantis.cache.stats;

import com.volantis.cache.CacheMock;
import com.volantis.cache.impl.stats.StatisticsSnapshotImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;

/**
 * Test cases for {@link StatisticsSnapshot}.
 */
public class StatisticsSnapshotTestCase
        extends TestCaseAbstract {
    private CacheMock cacheMock;

    protected void setUp() throws Exception {
        super.setUp();


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        cacheMock = new CacheMock("cacheMock", expectations);

    }

    /**
     * Ensure that it is an error to compare two snapshots from different
     * caches.
     */
    public void testDetectsDifferentCaches() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final CacheMock otherCacheMock =
                new CacheMock("otherCacheMock", expectations);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StatisticsSnapshot snapshot1 = new StatisticsSnapshotImpl(cacheMock,
                Period.inMilliSeconds(1000L),
                Time.inMilliSeconds(10L), 100, 10, 70);
        StatisticsSnapshot snapshot2 = new StatisticsSnapshotImpl(
                otherCacheMock,
                Period.inMilliSeconds(2000L),
                Time.inMilliSeconds(1010L),
                200, 20, 90);

        try {
            snapshot1.difference(snapshot2);
            fail("Did not detect different caches");
        } catch (IllegalArgumentException expected) {
            assertEquals("Statistics not gathered from same source",
                    expected.getMessage());
        }
    }

    /**
     * Ensure that it is an error if the other snapshot is not older than this
     * one.
     */
    public void testDetectsOtherYounger() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StatisticsSnapshot snapshot1 = new StatisticsSnapshotImpl(cacheMock,
                Period.inMilliSeconds(1000L),
                Time.inMilliSeconds(10L),
                100, 10, 70);
        StatisticsSnapshot snapshot2 = new StatisticsSnapshotImpl(cacheMock,
                Period.inMilliSeconds(2000L),
                Time.inMilliSeconds(1010L),
                200, 20, 90);

        try {
            snapshot1.difference(snapshot2);
            fail("Did not detect other snapshot was younger");
        } catch (IllegalArgumentException expected) {
            assertEquals("Other snapshot must have been gathered before this snapshot",
                    expected.getMessage());
        }
    }

    /**
     * Ensure that difference works properly.
     */
    public void testDifference() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StatisticsSnapshot snapshot1 = new StatisticsSnapshotImpl(cacheMock,
                Period.inMilliSeconds(1000L),
                Time.inMilliSeconds(10L),
                100, 10, 70);
        StatisticsSnapshot snapshot2 = new StatisticsSnapshotImpl(cacheMock,
                Period.inMilliSeconds(2000L),
                Time.inMilliSeconds(1010L),
                220, 24, 100);

        StatisticsDelta delta = snapshot2.difference(snapshot1);
        assertEquals(Period.inMilliSeconds(1000L), delta.getPeriod());
        assertEquals(120, delta.getHitCount());
        assertEquals(14, delta.getMissedAddedCount());
        assertEquals(30, delta.getRemovedCount());
    }
}
