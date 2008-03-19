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

package com.volantis.cache.impl;

import com.volantis.cache.Cache;
import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.impl.group.InternalGroup;
import com.volantis.cache.impl.integrity.IntegrityCheckingReporter;
import com.volantis.shared.io.IndentingWriter;

/**
 * Implementation of {@link InternalCacheEntry}.
 */
public class CacheEntryImpl
        implements InternalCacheEntry {

    /**
     * The owning cache.
     */
    private final InternalCache cache;

    /**
     * The key to this entry.
     */
    private final Object key;

    /**
     * The link used to add this entry into a {@link CacheEntryList}.
     */
    private final CacheEntryList.Link link;

    /**
     * The group to which this entry belongs, which may change.
     */
    private InternalGroup group;

    /**
     * The value associated with this entry, which may change.
     */
    private Object value;

    /**
     * The {@link Throwable} associated with this entry, if set this means that
     * the entry is in error and so any attempt to retrieve the value from the
     * entry will result in an error being thrown.
     */
    private Throwable throwable;

    /**
     * The state of the entry.
     */
    private EntryState state;

    /**
     * The object that the user of the cache has associated with this entry.
     */
    private Object extensionObject;

    /**
     * The result of an asynchronous query on the cache that hit this entry.
     *
     * <p>This is only set if the cache entry is in one of the following
     * states: {@link EntryState#READY}, {@link EntryState#UNCACHEABLE},
     * otherwise it is null.</p>
     */
    private InternalAsyncResult asyncResult;

    /**
     * Indicates whether the entry is in the cache or not.
     */
    private boolean inCache;

    /**
     * Initialise.
     *
     * @param cache The cache to which this entry belongs.
     * @param key   The key to the entry.
     */
    public CacheEntryImpl(InternalCache cache, Object key) {
        link = new CacheEntryList.Link(this);
        this.cache = cache;
        this.key = key;
        state = EntryState.UPDATE;

        // Entry starts off in the cache.
        inCache = true;
    }

    // Javadoc inherited.
    public Cache getCache() {
        return cache;
    }

    // Javadoc inherited.
    public EntryState getState() {
        return state;
    }

    // Javadoc inherited.
    public void setState(EntryState state) {
        this.state = state;
    }

    // Javadoc inherited.
    public CacheEntryList.Link getLink() {
        return link;
    }

    // Javadoc inherited.
    public Object getValue() {
        return value;
    }

    // Javadoc inherited.
    public void setValue(Object value) {
        this.value = value;
    }

    // Javadoc inherited.
    public InternalGroup getGroup() {
        return group;
    }

    // Javadoc inherited.
    public void setGroup(InternalGroup group) {
        this.group = group;
    }

    // Javadoc inherited.
    public Object getKey() {
        return key;
    }

    // Javadoc inherited.
    public Throwable getThrowable() {
        return throwable;
    }

    // Javadoc inherited.
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    // Javadoc inherited.
    public void setExtensionObject(Object extensionObject) {
        this.extensionObject = extensionObject;
    }

    // Javadoc inherited.
    public InternalAsyncResult getAsyncResult() {
        return asyncResult;
    }

    // Javadoc inherited.
    public void setAsyncResult(InternalAsyncResult asyncResult) {
        this.asyncResult = asyncResult;
    }

    // Javadoc inherited.
    public void removeFromCache() {
        removeFromCache(null);
    }

    // Javadoc inherited.
    public boolean removeFromCache(CacheEntryFilter filter) {

        if (filter == null) {
            filter = CacheEntryFilter.SELECT_ALL_ENTRIES;
        }

        InternalGroup group = null;
        synchronized(this) {

            // If this entry is not in the cache anymore then there is nothing
            // to do.
            if (!inCache) {
                return false;
            }

            // If the filter selects this entry then remove it.
            if (filter.select(this)) {

                // Remember that the entry is no longer in the cache.
                inCache = false;

                // Remove the entry from the cache map.
                cache.removeFromMap(key);

                // Save away the group so that events can be dispatched.
                group = this.group;

                // It may be that this entry has not yet been added to a group
                // before it has been removed.
                if (group != null) {
                    // Remove the entry from the group's entry list and clear the
                    // group.
                    group.detachEntry(this);
                }
            }
        }

        // Dispatch the events outside the synchronization block so that it
        // does not interfere with the normal running of the cache and prevents
        // listeners from causing deadlocks, e.g. by calling back into the
        // cache.
        boolean removed;
        if (group == null) {
            removed = false;
        } else {
            removed = true;

            // Make sure that the entryRemoved events are generated properly.
            group.bubbleEntryRemovedEvent(this);
        }

        return removed;
    }

    // Javadoc inherited.
    public void performIntegrityCheck(
            InternalGroup actualGroup, IntegrityCheckingReporter reporter) {

        reporter.beginChecking("Entry " + key);
        synchronized(this) {
            if (group != actualGroup) {
                reporter.reportIssue("Expected to be in group " + group +
                        " but was in group " + actualGroup);
            }

            if (!inCache) {
                reporter.reportIssue("Not in cache after all threads have " +
                        "stopped");
            }

            if (state == EntryState.PENDING) {
                reporter.reportIssue(
                        "In state PENDING after all threads have stopped");
            } else if (state == EntryState.READY) {
                // Nothing to check when ready.
            } else if (state == EntryState.ERROR) {
                // Must have a throwable.
                if (throwable == null) {
                    reporter.reportIssue("Throwable must be set when entry" +
                            " is in error");
                }
            } else if (state == EntryState.UNCACHEABLE) {
                if (extensionObject != null) {
                    reporter.reportIssue("Extension object must not be set " +
                            "when entry is uncacheable");
                }
            } else if (state == EntryState.UPDATE) {
                reporter.reportIssue(
                        "In state UPDATE after all threads have stopped");
            }
        }
        reporter.endChecking("Entry " + key);
    }

    // Javadoc inherited.
    public void debugStructure(IndentingWriter writer) {
        writer.println("Entry " + this);
        writer.beginBlock();
        synchronized(this) {
            writer.println("Key: " + key);
            writer.println("State: " + state);
            writer.println("Value: " + value);
            writer.println("In Cache: " + inCache);
        }
        writer.endBlock();
    }

    // Javadoc inherited.
    public boolean inCache() {
        return inCache;
    }

    // Javadoc inherited.
    public Object getExtensionObject() {
        return extensionObject;
    }
}
