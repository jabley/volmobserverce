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

import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupBuilder;
import com.volantis.cache.impl.InternalCache;
import com.volantis.shared.system.SystemClock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A map of {@link InternalGroup} that remembers the order in which they were
 * added.
 */
public class GroupMap {

    /**
     * The map from {@link Object} that is the group name to the
     * {@link InternalGroup}.
     */
    private final Map map;

    /**
     * A list of {@link InternalGroup}.
     */
    private final List list;

    /**
     * Initialise.
     */
    public GroupMap() {
        this.map = new HashMap();
        this.list = new ArrayList();
    }

    /**
     * Add the group.
     *
     * @param groupName The name of the group, must be unique within this map.
     * @param cache     The owning cache.
     * @param parent    The parent group, will be null for root group.
     * @param builder   The builder of the group.
     * @return The group that was built.
     * @throws IllegalStateException if the group already exists.
     */
    public Group addGroup(
            Object groupName, InternalCache cache, InternalGroup parent,
            GroupBuilder builder, SystemClock clock) {
        Group group;

        group = (Group) map.get(groupName);
        if (group != null) {
            throw new IllegalStateException("Group '" +
                    groupName + "' already exists");
        }

        InternalGroupBuilder internalBuilder = (InternalGroupBuilder) builder;
        group = internalBuilder.buildGroup(cache, parent,
                groupName.toString(), clock);

        map.put(groupName, group);
        list.add(group);

        return group;
    }

    /**
     * Get the nested group with the specified name.
     *
     * @param groupName The name of the group.
     * @return The group with that name or null if it could not be found.
     */
    public Group getGroup(Object groupName) {
        return (Group) map.get(groupName);
    }

    /**
     * Get a copy of the list of groups.
     *
     * @return A copy of the list of groups, may be null.
     */
    public List getGroupList() {
        return new ArrayList(list);
    }
}
