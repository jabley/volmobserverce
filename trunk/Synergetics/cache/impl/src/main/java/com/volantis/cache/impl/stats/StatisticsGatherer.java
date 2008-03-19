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

import com.volantis.cache.stats.StatisticsSnapshot;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Time;
import com.volantis.shared.time.Period;

/**
 * Responsible for gathering and taking snapshots of the statistical
 * information associated with a group.
 */
public class StatisticsGatherer {

    /**
     * The clock to use to access the current time.
     */
    private final SystemClock clock;

    /**
     * The time that this was created.
     */
    private final Time creationTime;

    /**
     * A count of the number of queries that have been satisfied directly from
     * the associated object.
     */
    private int hitCount;

    /**
     * A count of the number of entries that have been added to the
     * associated object.
     */
    private int missAddedCount;

    /**
     * A count of the number of entries that have been removed from the
     * associated object.
     */
    private int removedCount;

    /**
     * Initialise.
     *
     * @param clock The clock.
     */
    public StatisticsGatherer(SystemClock clock) {
        this.clock = clock;
        creationTime = clock.getCurrentTime();
    }

    /**
     * Initialise.
     *
     * <p>Used when combining a number of statistics snapshots into one.</p>
     *
     * @param gatherer The main gatherer, from which the creation time is
     *                 taken.
     */
    public StatisticsGatherer(StatisticsGatherer gatherer) {
        this.clock = gatherer.clock;
        this.creationTime = gatherer.creationTime;
        this.missAddedCount = gatherer.missAddedCount;
        this.hitCount = gatherer.hitCount;
        this.removedCount = gatherer.removedCount;
    }

    /**
     * Record a hit on an entry.
     */
    public synchronized void hit() {
        hitCount += 1;
    }

    /**
     * Record a missed / added entry.
     */
    public synchronized void missedAndAdded() {
        missAddedCount += 1;
    }

    /**
     * Record an entry was removed.
     */
    public synchronized void removed() {
        removedCount += 1;
    }

    /**
     * Get a snapshot of the statistics for this group and all nested groups.
     *
     * @param source The source of the snapshot.
     * @return A snapshot of the statistics for this group and all nested
     *         groups.
     */
    public synchronized StatisticsSnapshot getStatisticsSnapshot(
            Object source) {

        Time timestamp = clock.getCurrentTime();
        Period period = timestamp.getPeriodSince(creationTime);

        return new StatisticsSnapshotImpl(source, period, timestamp, hitCount,
                missAddedCount, removedCount);
    }

    /**
     * Add a snapshot.
     *
     * <p>Adds the various counts from the snapshot to the current counts in
     * this gatherer.</p>
     *
     * @param snapshot The snapshots.
     */
    public void addSnapshot(StatisticsSnapshot snapshot) {
        missAddedCount += snapshot.getMissedAddedCount();
        hitCount += snapshot.getHitCount();
        removedCount += snapshot.getRemovedCount();
    }
}
