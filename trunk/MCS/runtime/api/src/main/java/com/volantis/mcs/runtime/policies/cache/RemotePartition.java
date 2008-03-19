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

import com.volantis.cache.group.Group;

/**
 * A remote partition.
 */
public class RemotePartition {

    /**
     * The prefix of URLs that belong in this partition.
     */
    private final String prefix;

    /**
     * The group associated with the partition.
     */
    private final Group group;

    /**
     * Initialise.
     *
     * @param prefix The prefix of URLs that belong in this partition.
     * @param group  The group associated with the partition.
     */
    public RemotePartition(String prefix, Group group) {
        this.prefix = prefix;
        this.group = group;
    }

    /**
     * Get the prefix.
     *
     * @return The prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the group.
     *
     * @return The group.
     */
    public Group getGroup() {
        return group;
    }
}
