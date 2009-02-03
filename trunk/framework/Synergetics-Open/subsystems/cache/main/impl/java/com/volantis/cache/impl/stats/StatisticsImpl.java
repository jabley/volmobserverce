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

import com.volantis.cache.stats.Statistics;
import com.volantis.shared.time.Period;

/**
 * Base of the {@link Statistics} classes.
 */
public class StatisticsImpl
        implements Statistics {

    /**
     * The period over which these statistics apply.
     */
    private final Period period;

    /**
     * The number of cache hits.
     */
    private final int hitCount;

    /**
     * The number of cache misses / entries added.
     */
    private final int missedAddedCount;

    /**
     * The number of entries removed.
     */
    private final int removedCount;

    /**
     * Initialise.
     *
     * @param period       The period over which the statistics where gathered.
     * @param hitCount     The hit count during that period.
     * @param missCount    The miss / added count during that period.
     * @param removedCount The removed count during that period.
     */
    protected StatisticsImpl(
            Period period, int hitCount, int missCount,
            int removedCount) {
        this.period = period;
        this.hitCount = hitCount;
        this.missedAddedCount = missCount;
        this.removedCount = removedCount;
    }

    // Javadoc inherited.
    public Period getPeriod() {
        return period;
    }

    // Javadoc inherited.
    public int getHitCount() {
        return hitCount;
    }

    // Javadoc inherited.
    public int getMissedAddedCount() {
        return missedAddedCount;
    }

    // Javadoc inherited.
    public int getHitRate() {
        int queryCount = getQueryCount();
        if (queryCount == -1) {
            return 0;
        } else {
            return hitCount * 100 / queryCount;
        }
    }

    // Javadoc inherited.
    public int getQueryCount() {
        return hitCount + missedAddedCount;
    }

    // Javadoc inherited.
    public int getRemovedCount() {
        return removedCount;
    }
}
