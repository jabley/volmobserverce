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

import com.volantis.cache.impl.group.InternalGroup;
import com.volantis.cache.impl.group.PostSynchronizationAction;
import com.volantis.cache.provider.ProviderResult;

/**
 * Base class for those classes that may have to update the entry.
 */
public abstract class UpdatingAsyncResult
        implements InternalAsyncResult {

    /**
     * The entry to update.
     */
    protected final InternalCacheEntry entry;

    /**
     * Initialise.
     *
     * @param entry The entry to update.
     */
    protected UpdatingAsyncResult(InternalCacheEntry entry) {
        this.entry = entry;
    }

    // Javadoc inherited.
    public boolean isReady() {
        // This is not ready by definition.
        return false;
    }

    // Javadoc inherited.
    public Object getValue() {
        // Cannot get value when updating.
        throw new IllegalStateException("Cannot get value when updating");
    }

    /**
     * Update the cache entry.
     *
     * @param result The result from which the entry must be updated.
     * @param entry  The entry to update.
     * @return The value of the entry.
     */
    protected Object updateCacheEntry(
            ProviderResult result, InternalCacheEntry entry) {

        final Object value = result.getValue();

        PostSynchronizationAction postSynchronizationAction = null;
        synchronized (entry) {
            try {

                EntryState state = entry.getState();
                if (state != EntryState.PENDING) {
                    throw new IllegalStateException("Expected to be in " +
                            EntryState.PENDING + " but is in " + state);
                }

                // If the entry is still in the cache then make sure that
                // it is in the correct group.
                if (entry.inCache()) {
                    InternalGroup newGroup = (InternalGroup) result.getGroup();
                    postSynchronizationAction = updateGroup(entry, newGroup);
                }

                if (result.isCacheable()) {
                    // Mark the entry as ready so that other thread will pick
                    // the value up.
                    entry.setValue(value);
                    entry.setState(EntryState.READY);
                    entry.setExtensionObject(result.getExtensionObject());
                    entry.setAsyncResult(new AsyncReady(entry));
                } else {
                    // The result is uncacheable so clear the value
                    // in the entry and mark it as uncacheable.
                    // This is so that in future concurrent
                    // requests for this entry will not conflict
                    // but will instead make simultaneous
                    // requests to the provider.
                    entry.setValue(null);
                    entry.setState(EntryState.UNCACHEABLE);
                    entry.setExtensionObject(null);
                    entry.setAsyncResult(new AsyncUncacheable(entry));
                }
                entry.setThrowable(result.getThrowable());

            } finally {

                // Make sure that whatever happens that every thread
                // waiting on the entry wakes up.
                entry.notifyAll();
            }
        }

        // At this point any threads waiting on the entry being updated will
        // have been woken up and so the following code could potentially be
        // executed by multiple threads at once.

        // Perform any post query action. This must be done after the entry
        // has been updated to ensure that it is in a consistent state.
        if (postSynchronizationAction != null) {
            postSynchronizationAction.takeAction();
        }

        return value;
    }

    /**
     * Update the group associated with the entry.
     *
     * @param entry    The entry whose group may have changed.
     * @param newGroup The new group (may be the same as the old one).
     * @return The action to perform after synchronization on the entry has
     *         been exited, may be null.
     */
    private PostSynchronizationAction updateGroup(
            InternalCacheEntry entry, InternalGroup newGroup) {

        PostSynchronizationAction postSynchronizationAction = null;
        // Get the old group, will be null if this is a new entry
        // rather than an expired one, or if the entry has already
        // been removed.
        InternalGroup oldGroup = entry.getGroup();

        // If the group has changed then remove the entry from the
        // old group, if any and add it to the new group.
        if (oldGroup != newGroup) {
            if (oldGroup != null) {
                oldGroup.detachEntry(entry);
            }

            // Add the entry to the new group, this may cause an
            // entry to be purged. It may also return an action
            // that needs performing after updating the entry.
            postSynchronizationAction = newGroup.addEntry(entry);
        }
        return postSynchronizationAction;
    }
}
