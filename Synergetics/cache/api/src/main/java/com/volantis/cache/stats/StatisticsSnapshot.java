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

import com.volantis.shared.time.Time;

/**
 * A snapshot of statistical information from the group.
 * 
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface StatisticsSnapshot
        extends Statistics {

    /**
     * The time at which this snapshot was gathered in milliseconds since the
     * epoch.
     *
     * @return The time at which this snapshot was gathered.
     */
    Time getGatherTime();

    /**
     * The total number of entries in the group when the snapshot was taken.
     *
     * <p>As the snapshot includes all entries added and removed from the time
     * that the group was created this is the difference between the entries
     * added and the entries removed.</p>
     *
     * @return The total number of entries in the cache.
     */
    int getEntryCount();

    /**
     * Calculate the difference between this statistics snapshot and the other.
     *
     * <p>The other snapshot must have been gathered sometime before this one
     * was gathered and from the same group.</p>
     *
     * @param other The other set of statistics.
     * @return The difference.
     */
    StatisticsDelta difference(StatisticsSnapshot other);
}
