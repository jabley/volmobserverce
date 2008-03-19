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

package com.volantis.mcs.runtime.policies.cache;

/**
 * Constraints on the a partition of a policy cache.
 */
public class PolicyCachePartitionConstraintsImpl
        implements PolicyCachePartitionConstraints {

    /**
     * Maximum partition size.
     */
    private final int maxPartitionSize;

    /**
     * Default partition size.
     */
    private final int defaultPartitionSize;

    /**
     * Constraints that apply to all policies in this partition.
     */
    private final CacheControlConstraints constraints;

    /**
     * Initialise.
     *
     * @param maxPartitionSize     Maximum partition size.
     * @param defaultPartitionSize Default partition size.
     * @param constraints          Constraints that apply to all policies in
     *                             this partition.
     */
    public PolicyCachePartitionConstraintsImpl(
            int maxPartitionSize, int defaultPartitionSize,
            CacheControlConstraints constraints) {
        this.maxPartitionSize = maxPartitionSize;
        this.defaultPartitionSize = defaultPartitionSize;
        this.constraints = constraints;
    }

    public int constrainPartitionSize(Integer partitionSize) {
        if (partitionSize == null) {
            return defaultPartitionSize;
        } else {
            int size = partitionSize.intValue();
            return Math.min(size, maxPartitionSize);
        }
    }

    public CacheControlConstraints getConstraints() {
        return constraints;
    }
}
