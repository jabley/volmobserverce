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
import com.volantis.shared.time.Period;

/**
 * Implementation of {@link StatisticsDelta}.
 */
public class StatisticsDeltaImpl
        extends StatisticsImpl
        implements StatisticsDelta {

    /**
     * Initialise.
     *
     * @param period       The period over which the statistics were gathered.
     * @param hitCount     The hit count during that period.
     * @param missCount    The miss / added count during that period.
     * @param removedCount The removed count during that period.
     */
    public StatisticsDeltaImpl(
            Period period, int hitCount, int missCount, int removedCount) {
        super(period, hitCount, missCount, removedCount);
    }
}
