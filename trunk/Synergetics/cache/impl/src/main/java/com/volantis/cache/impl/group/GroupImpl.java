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
import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupBuilder;
import com.volantis.cache.impl.CacheEntryImpl;
import com.volantis.cache.impl.CacheEntryList;
import com.volantis.cache.impl.InternalCache;
import com.volantis.cache.impl.InternalCacheEntry;
import com.volantis.cache.impl.stats.StatisticsGatherer;
import com.volantis.cache.impl.integrity.IntegrityCheckingReporter;
import com.volantis.cache.impl.notification.RemovalListenerList;
import com.volantis.cache.notification.RemovalListener;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.shared.io.IndentingWriter;
import com.volantis.shared.system.SystemClock;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link InternalGroup}.
 */
public class GroupImpl
        implements InternalGroup {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(GroupImpl.class);

    /**
     * The object responsible for protecting access to this group.
     */
    private final Object groupsMutex = new Object();

    /**
     * The parent group.
     */
    private final InternalGroup parent;

    /**
     * A description of the group, this is the string representation of the
     * name supplied to the parent group when this group was added. It does
     * not include any hierarchical informaton.
     */
    private final String description;

    /**
     * The cache to which this group belongs.
     */
    private final InternalCache cache;

    /**
     * The maximum number of entries that can be contained within this group.
     */
    private final int maxCount;

    /**
     * The list of {@link RemovalListener}s.
     */
    private final RemovalListenerList removalListeners;

    /**
     * Action that will purge the cache of excess entries.
     */
    private final PurgeExcessEntriesAction purgeExcessEntriesAction;

    /**
     * Object responsible for gathering statistics about this group.
     */
    private final StatisticsGatherer gatherer;

    /**
     * The map from {@link Object} that is the group name to the
     * {@link InternalGroup}.
     */
    private GroupMap groups;

    /**
     * The list of entries in this group.
     *
     * <p>Must only be accessed while holding {@link #entries}.</p>
     */
    private final CacheEntryList entries;

    /**
     * The system clock.
     */
    private final SystemClock clock;

    /**
     * A count of the number of entries in this group.
     *
     * <p>This includes all entries contained in nested groups.</p>
     *
     * <p>Must only be accessed while holding {@link #entries}.</p>
     */
    private int totalCount;

    /**
     * Initialise.
     *
     * @param cache       The containing cache, may not be null.
     * @param parent      The parent group, may be null.
     * @param maxCount    The maximum size of the group, must be >= 1.
     * @param description The description, may not be null.
     * @param clock
     */
    public GroupImpl(
            InternalCache cache, InternalGroup parent, int maxCount,
            String description, SystemClock clock) {

        if (cache == null) {
            throw new IllegalArgumentException("cache cannot be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        if (clock == null) {
            throw new IllegalArgumentException("clock cannot be null");
        }

        this.cache = cache;
        this.parent = parent;
        this.description = description;
        if (maxCount <= 0) {
            throw new IllegalArgumentException(
                    "Max count must be > 0 but is " + maxCount);
        }
        this.maxCount = maxCount;
        this.clock = clock;
        entries = new CacheEntryList();
        removalListeners = new RemovalListenerList();

        purgeExcessEntriesAction = new PurgeExcessEntriesAction(this);
        gatherer = new StatisticsGatherer(clock);
    }

    // Javadoc inherited.
    public void addRemovalListener(RemovalListener listener) {
        removalListeners.addListener(listener);
    }

    // Javadoc inherited.
    public PostSynchronizationAction addEntry(InternalCacheEntry entry) {

        PostSynchronizationAction action = null;

        entry.setGroup(this);

        synchronized (entries) {
            // Add the entry to the head of the list and increment the
            // count.
            entries.addHeadEntry(entry);

            totalCount += 1;

            // Check to see what action needs to be taken now that a new entry
            // has been added.
            if (totalCount > maxCount) {
                action = purgeExcessEntriesAction;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Added entry to " + description +
                        " count now " + totalCount);
            }
        }

        // Record that an entry was added to this group and as that was as a
        // result of a cache miss then record that too.
        gatherer.missedAndAdded();

        if (parent != null) {
            // See whether the parent has exceeded its count as if so it should
            // be responsible for purging. If it has not but this group has
            // then this group is responsible.
            PostSynchronizationAction parentAction = parent.addedNestedEntry();
            if (parentAction != null) {
                action = parentAction;
            }
        }

        return action;
    }

    // Javadoc inherited.
    public PostSynchronizationAction addedNestedEntry() {

        PostSynchronizationAction action = null;

        synchronized (entries) {

            // Increment the count as an entry has been added to a descendant
            // group.
            totalCount += 1;

            // Check to see what action needs to be taken now that a new entry
            // has been added.
            if (totalCount > maxCount) {
                action = purgeExcessEntriesAction;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Added nested entry to " + description +
                    " count now " + totalCount);
            }
        }

        if (parent != null) {
            // See whether the parent has exceeded its count as if so it should
            // be responsible for purging.
            PostSynchronizationAction parentAction = parent.addedNestedEntry();
            if (parentAction != null) {
                action = parentAction;
            }
        }

        return action;
    }

    // Javadoc inherited.
    public void purgeExcessiveEntries() {

        if (logger.isDebugEnabled()) {
                logger.debug("Purging " + description);
        }

        // Keep looping around until the group no longer exceeds its limit.
        boolean purgeRequired;
        do {
            InternalCacheEntry entry = null;

            // Synchronize on the group to get an entry. The entry cannot be
            // removed at this point because in order to do that the code needs
            // to synchronize on the entry which must always be done before
            // synchronizing on the group's entry mutex.
            synchronized(entries) {

                // While inside this block the group entries cannot be modified
                // but nested groups may have removed entries and are blocking
                // waiting to update this group. This means that we may
                // purge entries that we do not strictly need to purge but that
                // is unavoidable. We may also not be able to find any entries
                // to purge in the nested groups, they may all be empty for
                // example. In that case we just loop around and try again.
                if (totalCount > maxCount) {
                    purgeRequired = true;

                    if (logger.isDebugEnabled()) {
                        logger.debug("Count " + totalCount + " max count " +
                                maxCount);
                    }

                } else {
                    purgeRequired = false;

                    if (logger.isDebugEnabled()) {
                        logger.debug("Purge no longer required");
                    }
                }
            }

            // If a purge was required and an entry to remove was found then
            // try and remove it.
            if (purgeRequired) {

                // Select the entry that is from the group with the highest
                // percentage usage and is the least recently used.
                entry = selectLeastRecentlyUsedEntry();

                if (entry != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Attempting to purge " + entry);
                    }

                    boolean removed = entry.removeFromCache(null);
                    if (logger.isDebugEnabled()) {
                        if (removed) {
                            logger.debug("Removed " + entry);
                        } else {
                            logger.debug("Failed to remove " + entry);
                        }
                    }
                }
            }

            // Go back around again as there may be more entries to remove.
        } while(purgeRequired);

        if (logger.isDebugEnabled()) {
                logger.debug("Finished purging " + description);
        }
    }

    // Javadoc inherited.
    public InternalCacheEntry selectLeastRecentlyUsedEntry() {

        // Select the group to prune, that is the group with the highest
        // percentage usage. This strategy is chosen because it should spread
        // the entries fairly across the groups in the cache. This method can
        // return the group passed in or one of its child groups.
        InternalGroup group = selectGroupWithHighestUsage();
        InternalCacheEntry entry;

        // If this group is the one with the highest usage then return the
        // entry that is least recently used.
        if (group == this) {
            entry = getLeastRecentlyUsedEntry();
        } else {
            // Recurse down to the group with the highest usage to find its
            // entry that is least recently used.
            entry = group.selectLeastRecentlyUsedEntry();
        }

        return entry;
    }

    public int getPercentageUsage() {
        synchronized(entries) {
            return calculatePercentageUsage(totalCount);
        }
    }

    // Javadoc inherited.
    public void hitEntry(InternalCacheEntry entry) {
        synchronized (entries) {
            // Move the entry to the head of the list in order to implement the
            // least recently used strategy.
            entries.remove((CacheEntryImpl) entry);
            entries.addHeadEntry((CacheEntryImpl) entry);
        }

        // Record that a query hit an entry in this group.
        gatherer.hit();
    }

    // Javadoc inherited.
    public void detachEntry(InternalCacheEntry entry) {

        synchronized (entries) {
            entries.remove(entry);
            entry.setGroup(null);

            // Decrement the count as an entry has been removed.
            totalCount -= 1;

            if (logger.isDebugEnabled()) {
                logger.debug("Detached entry from " + description +
                    " count now " + totalCount);
            }
        }

        // Record that an entry was removed from this group.
        gatherer.removed();

        if (parent != null) {
            parent.detachedNestedEntry();
        }
    }

    // Javadoc inherited.
    public void detachedNestedEntry() {

        synchronized (entries) {
            // Decrement the count as an entry has been removed from this
            // group.
            totalCount -= 1;

            if (logger.isDebugEnabled()) {
                logger.debug("Detached nested entry from " + description +
                    " count now " + totalCount);
            }
        }

        if (parent != null) {
            parent.detachedNestedEntry();
        }
    }

    /**
     * Get a list of all the entries in this group and all nested groups.
     *
     * @return A list of {@link CacheEntry}s.
     */
    private List getEntries() {
        List list = new ArrayList();
        addEntriesRecursive(list);
        return list;
    }

    /**
     * Add the entries from this group and the nested groups to the list.
     *
     * @param list The list to which the entries should be added.
     */
    private void addEntriesRecursive(List list) {

        // Recurse down to all nested groups adding their entries to the list.
        List groups = getGroupList();
        if (groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                GroupImpl group = (GroupImpl) groups.get(i);
                group.addEntriesRecursive(list);
            }
        }

        addEntries(list);
    }

    /**
     * Add the entries from this group only to the list.
     *
     * @param list The list to which the entries should be added.
     */
    private void addEntries(List list) {
        // Iterate over entries owned directly by this group adding them to the
        // list.
        synchronized (entries) {

            for (InternalCacheEntry entry = entries.getHeadEntry();
                 entry != null; entry = entries.getNextEntry(entry)) {
                list.add(entry);
            }
        }
    }

    // Javadoc inherited.
    public Group addGroup(Object groupName, GroupBuilder builder) {
        Group group;

        synchronized (groupsMutex) {
            if (groups == null) {
                groups = new GroupMap();
            }

            group = groups.addGroup(groupName, cache, this, builder, clock);
        }

        return group;
    }

    // Javadoc inherited.
    public Group getGroup(Object groupName) {
        Group group = findGroup(groupName);
        if (group == null) {
            throw new IllegalStateException(
                    "Group '" + groupName + "' does not exist");
        }
        return group;
    }

    // Javadoc inherited.
    public Group findGroup(Object groupName) {
        Group group;
        synchronized (groupsMutex) {
            if (groups != null) {
                group = groups.getGroup(groupName);
            } else {
                group = null;
            }
        }

        return group;
    }

    /**
     * Get a copy of the list of groups.
     *
     * @return A copy of the list of groups, may be null.
     */
    private List getGroupList() {
        synchronized (groupsMutex) {
            return groups == null ? null : groups.getGroupList();
        }
    }

    // Javadoc inherited.
    public void flush(CacheEntryFilter filter) {
        if (filter == null) {
            filter = CacheEntryFilter.SELECT_ALL_ENTRIES;
        }

        List entries = getEntries();

        // Iterate over the list of entries removing those entries that are
        // matched by the filter.
        for (Iterator i = entries.iterator(); i.hasNext();) {
            InternalCacheEntry entry = (InternalCacheEntry) i.next();
            entry.removeFromCache(filter);
        }
    }

    public StatisticsSnapshot getStatisticsSnapshot() {
        List groups = getGroupList();
        StatisticsSnapshot snapshot;
        if (groups == null) {
            snapshot = gatherer.getStatisticsSnapshot(this);
        } else {
            StatisticsGatherer combined = new StatisticsGatherer(gatherer);

            for (int i = 0; i < groups.size(); i++) {
                Group group = (Group) groups.get(i);
                StatisticsSnapshot nested = group.getStatisticsSnapshot();
                combined.addSnapshot(nested);
            }

            snapshot = combined.getStatisticsSnapshot(this);
        }
        return snapshot;
    }

    /**
     * Get the least recently used entry in this group.
     *
     * @return The least recently used entry, or null if this group does not
     *         own any entries directly.
     */
    private InternalCacheEntry getLeastRecentlyUsedEntry() {
        synchronized(entries) {
            return entries.getTailEntry();
        }
    }

    /**
     * Select the group from this group and its children with the highest usage.
     *
     * <p>The usage of this group is the number of entries owned directly by
     * this group / max number of entries.</p>
     *
     * <p>The usage of a child group is the number of entries (including
     * entries in nested groups) / their max number of entries.</p>
     *
     * @return The group with the highest percentage usage.
     */
    private InternalGroup selectGroupWithHighestUsage() {

        // Calculate the percentage usage of this group, that is calculated as
        // the number of entries owned by this group directly, i.e. not
        // including nested entries.
        int count;
        synchronized (entries) {
            count = entries.size();
        }

        int highestUsage = calculatePercentageUsage(count);
        InternalGroup groupToPurge = this;

        if (logger.isDebugEnabled()) {
            logger.debug(this + " has percentage usage of " + highestUsage);
        }

        List list = getGroupList();
        if (list != null) {
            // Find the group with the highest percentage usage.
            for (int i = 0; i < list.size(); i++) {
                InternalGroup group = (InternalGroup) list.get(i);
                int groupUsage = group.getPercentageUsage();

                if (logger.isDebugEnabled()) {
                    logger.debug(group + " has percentage usage of " +
                            groupUsage + "%");
                }

                if (groupUsage > highestUsage) {
                    highestUsage = groupUsage;
                    groupToPurge = group;
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Selected " + groupToPurge +
                    " as it has the highest percentage usage");
        }

        return groupToPurge;
    }

    /**
     * Calculate the percentage usage.
     *
     * <p>The usage is a number between 0 and 100 and is calculated by
     * dividing the supplied count by the maximum count.</p>
     *
     * @param count The number of entries that constitute the usage.
     * @return The usage.
     */
    private int calculatePercentageUsage(int count) {
        return Math.round(((float) count) / ((float) maxCount) * 100);
    }

    // Javadoc inherited.
    public void performIntegrityCheck(IntegrityCheckingReporter reporter) {
        reporter.beginChecking("Group " + description);

        // Iterate over the nested groups validating them and getting a count
        // of all the entries in the group.
        List groups = getGroupList();
        int actualCount = 0;
        if (groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                InternalGroup group = (InternalGroup) groups.get(i);
                group.performIntegrityCheck(reporter);
                int groupCount = group.getEntryCount();
                actualCount += groupCount;
            }
        }

        // Iterate over all the entries in this group itself.
        List list = new ArrayList();
        addEntries(list);

        actualCount += list.size();

        // If the actual count of entries and nested entries does not match
        // then report a problem.
        if (actualCount != totalCount) {
            reporter.reportIssue("Expected count " + totalCount +
                    " actual count " + actualCount);
        }

        for (int i = 0; i < list.size(); i++) {
            InternalCacheEntry entry = (InternalCacheEntry) list.get(i);
            entry.performIntegrityCheck(this, reporter);
        }

        reporter.endChecking("Group " + description);
    }

    // Javadoc inherited.
    public void debugStructure(IndentingWriter writer) {
        writer.println("Group " + description);
        writer.beginBlock();

        synchronized (entries) {
            writer.println("Max Count: " + maxCount);
            writer.println("Total Count: " + totalCount);
            int directCount = entries.size();
            writer.println("Direct Count: " + directCount);
            writer.println("Total Usage: " + calculatePercentageUsage(totalCount) + "%");
            writer.println("Direct Usage: " + calculatePercentageUsage(directCount) + "%");
        }

        // Iterate over all the entries in this group itself.
        List list = new ArrayList();
        addEntries(list);

        for (int i = 0; i < list.size(); i++) {
            InternalCacheEntry entry = (InternalCacheEntry) list.get(i);
            entry.debugStructure(writer);
        }

        // Iterate over the nested groups validating them and getting a count
        // of all the entries in the group.
        List groups = getGroupList();
        if (groups != null) {
            writer.println();

            for (int i = 0; i < groups.size(); i++) {
                InternalGroup group = (InternalGroup) groups.get(i);
                group.debugStructure(writer);
            }
        }

        writer.endBlock();
    }

    // Javadoc inherited.
    public void bubbleEntryRemovedEvent(CacheEntry entry) {
        removalListeners.dispatchRemovedEntryEvent(entry);
        if (parent != null) {
            parent.bubbleEntryRemovedEvent(entry);
        }
    }

    // Javadoc inherited.
    public int getEntryCount() {
        synchronized (entries) {
            return totalCount;
        }
    }

    public String toString() {
        return description;
    }
}
