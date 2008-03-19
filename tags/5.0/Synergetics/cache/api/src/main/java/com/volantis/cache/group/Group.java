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

package com.volantis.cache.group;

import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.notification.RemovalListener;
import com.volantis.cache.stats.StatisticsSnapshot;

/**
 * Contains a number of entries and possible other groups.
 *
 * <p>Groups are used to structure a cache internally. They are organized into
 * a hierarchy and can container entries and/or other groups.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface Group {

    /**
     * Add a listener that will be notified when entries are removed from the
     * cache.
     *
     * @param listener The listener to add.
     */
    void addRemovalListener(RemovalListener listener);

    /**
     * Add a nested group.
     *
     * @param groupName The name of the group, must be unique within this group.
     * @param builder   The builder of the group.
     * @return The group that was built.
     * @throws IllegalStateException if the group already exists.
     */
    Group addGroup(Object groupName, GroupBuilder builder);

    /**
     * Get the nested group with the specified name.
     *
     * @param groupName The name of the group.
     * @return The group with that name.
     * @throws IllegalStateException if the group does not exist.
     */
    Group getGroup(Object groupName);

    /**
     * Get the nested group with the specified name.
     *
     * @param groupName The name of the group.
     * @return The group with that name, or null if it could not be found.
     */
    Group findGroup(Object groupName);

    /**
     * Flush the entries of this group and all nested groups that are matched
     * by the filter.
     *
     * @param filter The filter to select entries, if this is null then it is
     *               assumed that all entries will be removed.
     */
    void flush(CacheEntryFilter filter);

    /**
     * Get a snapshot of the statistics for this group and all nested groups.
     *
     * @return A snapshot of the statistics for this group and all nested
     *         groups.
     */
    StatisticsSnapshot getStatisticsSnapshot();
}
