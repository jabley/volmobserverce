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

package com.volantis.cache.impl.stats;

import com.volantis.cache.stats.StatisticsDelta;
import com.volantis.cache.stats.StatisticsSnapshot;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;

/**
 * A snapshot of the statistics associated with a cache.
 */
public class StatisticsSnapshotImpl
        extends StatisticsImpl
        implements StatisticsSnapshot {

    /**
     * The cache to which these statistics apply.
     */
    private final Object source;

    /**
     * The time stamp when these statistics were gathered.
     */
    private final Time gatherTime;

    /**
     * Initialise.
     *
     * @param source           The source from which the statistics were
     *                         gathered.
     * @param period           The period over which the statistics were
     *                         gathered.
     * @param gatherTime       The time at which the statistics were gathered.
     * @param hitCount         The hit count of the cache.
     * @param missedAddedCount The missed queries / added entries count of the
     *                         group.
     * @param removedCount
     */
    public StatisticsSnapshotImpl(
            Object source, Period period, Time gatherTime, int hitCount,
            int missedAddedCount, int removedCount) {
        super(period, hitCount, missedAddedCount, removedCount);
        this.source = source;
        this.gatherTime = gatherTime;
    }

    // Javadoc inherited.
    public Time getGatherTime() {
        return gatherTime;
    }

    // Javadoc inherited.
    public int getEntryCount() {
        return getMissedAddedCount() - getRemovedCount();
    }

    // Javadoc inherited.
    public StatisticsDelta difference(StatisticsSnapshot other) {
        StatisticsSnapshotImpl snapshot = (StatisticsSnapshotImpl) other;

        if (snapshot.source != source) {
            throw new IllegalArgumentException(
                    "Statistics not gathered from same source");
        }

        Period period = gatherTime.getPeriodSince(snapshot.gatherTime);
        if (period.inMillis() < 0) {
            throw new IllegalArgumentException("Other snapshot must have been " +
                    "gathered before this snapshot");
        }

        int deltaHitCount = getHitCount() - snapshot.getHitCount();
        int deltaMissCount = getMissedAddedCount() -
                snapshot.getMissedAddedCount();
        int deltaRemovedCount = getRemovedCount() - snapshot.getRemovedCount();

        return new StatisticsDeltaImpl(period, deltaHitCount, deltaMissCount,
                deltaRemovedCount);
    }
}
