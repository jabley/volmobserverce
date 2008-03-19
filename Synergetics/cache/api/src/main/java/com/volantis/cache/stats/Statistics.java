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

import com.volantis.shared.time.Period;

/**
 * Contains statistical information about the cache that could be used to
 * tune it.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface Statistics {

    /**
     * The period over which the statistics apply in milliseconds.
     *
     * @return The period over which the statistics apply.
     */
    Period getPeriod();

    /**
     * The number of queries that returned an object straight from the cache,
     * without having to invoked the provider.
     *
     * @return The hit count.
     */
    int getHitCount();

    /**
     * The number of queries that had to invoke the provider in order to
     * retrieve an object.
     *
     * <p>This is the same as the number of entries that were added to the
     * cache.</p>
     *
     * @return The missed queries / added entries count.
     */
    int getMissedAddedCount();

    /**
     * The hit rate.
     *
     * <p>The hit rate is the percentage of queries that hit the cache out of
     * the total number of queries. It is expressed as a number between 0 and
     * 100.</p>
     *
     * @return The hit rate.
     */
    int getHitRate();

    /**
     * The number of queries made on the cache.
     *
     * @return The query count.
     */
    int getQueryCount();

    /**
     * The number of entries removed from the cache.
     *
     * <p>This includes entries removed for all reasons.</p>
     *
     * @return The number of entries removed from the cache.
     */
    int getRemovedCount();
}
