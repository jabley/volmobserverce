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

import com.volantis.cache.CacheEntry;
import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.impl.group.InternalGroup;
import com.volantis.cache.impl.integrity.IntegrityCheckingReporter;
import com.volantis.shared.io.IndentingWriter;

/**
 * Internal interface for cache entries.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface InternalCacheEntry
        extends CacheEntry {

    /**
     * Set the value of the entry.
     *
     * <p>Caller must hold the mutex of this object.</p>
     *
     * @param value The value to set.
     */
    void setValue(Object value);

    /**
     * Get the group to which this entry belongs.
     *
     * <p>The value returned is valid only while the caller holds the mutex on
     * this entry.</p>
     *
     * @return The group, may be null if this entry has just been created, or
     *         has been removed / detached from this group.
     */
    InternalGroup getGroup();

    /**
     * Set the group of the entry.
     *
     * <p>Caller must hold the mutex of this object.</p>
     *
     * @param group The group to set.
     */
    void setGroup(InternalGroup group);

    /**
     * Get the state of the entry.
     *
     * <p>The value returned is valid only while the caller holds the mutex on
     * this entry.</p>
     *
     * @return The state of the entry.
     */
    EntryState getState();

    /**
     * Set the state of the entry.
     *
     * <p>Caller must hold the mutex of this object.</p>
     *
     * @param state The state of the entry.
     */
    void setState(EntryState state);

    /**
     * Get the link used to hold this entry in the list of entries within a
     * group.
     *
     * <p>The returned link can only be added to a list while synchronized
     * on this entry and the monitor protecting the list. Removal of the link
     * from the
     *
     * @return The link within a group's list of entries.
     */
    CacheEntryList.Link getLink();

    /**
     * Get the throwable associated with the entry.
     *
     * <p>The value returned is valid only while the caller holds the mutex on
     * this entry.</p>
     *
     * @return The throwable.
     */
    Throwable getThrowable();

    /**
     * Set the throwable associated with the entry.
     *
     * <p>Caller must hold the mutex of this object.</p>
     *
     * @param throwable The throwable.
     */
    void setThrowable(Throwable throwable);

    /**
     * Set the extension object associated with the entry.
     *
     * <p>Caller must hold the mutex of this object.</p>
     *
     * @param extensionObject The extension object associated with the entry.
     */
    void setExtensionObject(Object extensionObject);

    /**
     * Get the {@link InternalAsyncResult} associated with the entry.
     *
     * @return The {@link InternalAsyncResult}, may be null.
     */ 
    InternalAsyncResult getAsyncResult();

    /**
     * Set the {@link InternalAsyncResult} associated with the entry.
     *
     * @param asyncResult The {@link InternalAsyncResult} to associate with the
     *                    entry.
     */
    void setAsyncResult(InternalAsyncResult asyncResult);

    /**
     * Indicates whether the entry is in the cache or has been removed.
     *
     * <p>The value returned is valid only while the caller holds the mutex on
     * this entry.</p>
     *
     * @return True if the entry is in the cache false otherwise.
     */
    boolean inCache();

    /**
     * Remove the entry from the cache.
     */
    void removeFromCache();

    boolean removeFromCache(CacheEntryFilter filter);

    /**
     * Perform an integrity check on the internal structure of the entry.
     *
     * <p>This method must only be called by tests when no other threads are
     * using the cache.</p>
     *      @param actualGroup The group to which the entry is expected to belong.
     @param reporter
     */
    void performIntegrityCheck(
            InternalGroup actualGroup, IntegrityCheckingReporter reporter);

    /**
     * Output the internal structure of the entry.
     *
     * <p>This method must only be called by tests when no other threads are
     * using the cache.</p>

     * @param writer The writer to which the structure should be written.
     */
    void debugStructure(IndentingWriter writer);
}
