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

import com.volantis.cache.CacheEntry;
import com.volantis.cache.stats.StatisticsSnapshot;
import com.volantis.cache.group.Group;
import com.volantis.cache.impl.InternalCacheEntry;
import com.volantis.cache.impl.integrity.IntegrityCheckingReporter;
import com.volantis.cache.notification.RemovalListener;
import com.volantis.shared.io.IndentingWriter;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate base="Group"
 */
public interface InternalGroup
        extends Group {

    /**
     * Add the entry to this group.
     *
     * <p>If the group is full then this will attempt to purge an entry from
     * the group, or nested groups.</p>
     *
     * <p>This is invoked while being synchronized on the cache entry.</p>
     *
     * @param entry The entry to add.
     * @return True if an entry should be purged, false otherwise.
     */
    PostSynchronizationAction addEntry(InternalCacheEntry entry);

    /**
     * Called to indicate that a nested group has added an entry.
     */
    PostSynchronizationAction addedNestedEntry();

    /**
     * Bubble an entry removed event upwards.
     *
     * @param entry The entry being removed.
     */
    void bubbleEntryRemovedEvent(CacheEntry entry);

    /**
     * Get the count of the number of entries in the group and all nested
     * groups.
     *
     * @return The number of entries in the group and the nested group.
     */
    int getEntryCount();

    /**
     * Purge excessive entries.
     */
    void purgeExcessiveEntries();

    /**
     * The specified entry was hit.
     *
     * @param entry The entry that was hit.
     */
    void hitEntry(InternalCacheEntry entry);

    /**
     * Detach an entry from the group.
     *
     * <p>This must be invoked while holding the mutexes associated with the
     * entry.</p>
     *
     * <p>This is different to purging in that the entry is not being removed
     * from the cache altogether it is simply being moved from one group to
     * another. In that regard it will not trigger a
     * {@link RemovalListener#entryRemoved(CacheEntry)} event.</p>
     *
     * @param entry The entry to remove.
     */
    void detachEntry(InternalCacheEntry entry);

    /**
     * Indicates that an entry has been detached from a nested group and so
     * this group needs to decrement its count of entries.
     *
     * <p>This method is called while holding the mutex for the entry that
     * was detached.</p>
     *
     */
    void detachedNestedEntry();

    /**
     * Perform an integrity check on the internal structure of the group.
     *
     * <p>This method must only be called by tests when no other threads are
     * using the cache.</p>
     * @param reporter
     */
    void performIntegrityCheck(IntegrityCheckingReporter reporter);

    /**
     * Output the internal structure of the group.
     *
     * <p>This method must only be called by tests when no other threads are
     * using the cache.</p>

     * @param writer The writer to which the structure should be written.
     */
    void debugStructure(IndentingWriter writer);

    /**
     * Select the least recently used entry from this or a nested group.
     *
     * @return The least recently used entry, may be null.
     */
    InternalCacheEntry selectLeastRecentlyUsedEntry();

    /**
     * Get the percentage usage of this group.
     *
     * @return The percentage usage.
     */
    int getPercentageUsage();
}
