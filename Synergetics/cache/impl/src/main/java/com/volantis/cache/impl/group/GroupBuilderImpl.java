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

package com.volantis.cache.impl.group;

import com.volantis.cache.impl.InternalCache;
import com.volantis.shared.system.SystemClock;

/**
 * Implementation of {@link InternalGroupBuilder}.
 */
public class GroupBuilderImpl
        implements InternalGroupBuilder {

    /**
     * The maximum count of entries that can be in the group.
     */
    private int maxCount;

    /**
     * Initialise.
     */
    public GroupBuilderImpl() {
    }

    // Javadoc inherited.
    public int getMaxCount() {
        return maxCount;
    }

    // Javadoc inherited.
    public void setMaxCount(int maxCount) {
        if (maxCount <= 0) {
            throw new IllegalArgumentException(
                    "maxCount must be > 0 but is " + maxCount);
        }
        this.maxCount = maxCount;
    }

    // Javadoc inherited.
    public InternalGroup buildGroup(
            InternalCache cache, final InternalGroup parent,
            String description, SystemClock clock) {
        if (maxCount == 0) {
            throw new IllegalStateException("maxCount must be set");
        }

        return new GroupImpl(cache, parent, maxCount, description, clock);
    }
}
