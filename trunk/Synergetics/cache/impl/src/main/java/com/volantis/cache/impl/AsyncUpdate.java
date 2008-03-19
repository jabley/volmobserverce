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
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.time.Period;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The result of an asynchronous query against a entry that needs updating,
 * either because it has never been set, or because it has expired.
 *
 * <p>An instance of this class is created each time an entry needs updating,
 * it will only ever be accessed by a single thread.</p>
 */
class AsyncUpdate extends UpdatingAsyncResult {

    private boolean pending;

    /**
     * Initialise.
     *
     * @param entry The entry to update.
     */
    public AsyncUpdate(InternalCacheEntry entry) {
        super(entry);

        pending = true;
    }

    // Javadoc inherited.
    public Object update(ProviderResult result) {

        synchronized(this) {
            if (pending) {
                pending = false;
            } else {
                throw new IllegalStateException("Timed out");
            }
        }

        // The value will usually (but not always) change as a result of
        // updating the cached object.
        return updateCacheEntry(result, entry);
    }

    // Javadoc inherited.
    public void failed(Throwable throwable) {

        failedInternal(throwable, null);
    }

    private void failedInternal(Throwable throwable, String message) {

        // Ignore this call if the entry has already been updated.
        synchronized(this) {
            if (pending) {
                pending = false;
            } else {
                return;
            }
        }

        // If no throwable has been specified but a message has then create an
        // exception.
        if (throwable == null) {
            if (message != null) {
                throwable = new IllegalStateException(message);
            }
        }

        synchronized(entry) {
            if (throwable != null) {
                // An error happened while updating the
                // entry. Mark the entry as being in error and set the
                // throwable so it will be reported next time the entry
                // is retrieved.
                entry.setState(EntryState.ERROR);
                entry.setThrowable(throwable);
                entry.setAsyncResult(null);
            }

            // Notify every thread that is waiting that
            // there is a problem.
            entry.notifyAll();
        }
    }

    /**
     * Called by code that attempts to cleanup if necessary.
     *
     * <p>If this is called after {@link #update(ProviderResult)} then it does
     * nothing.</p>
     *
     * @param message
     */
    private void cleanup(String message) {
        failedInternal(null, message);
    }

    // Javadoc inherited.
    public CacheEntry getEntry() {
        return entry;
    }

    // Javadoc inherited.
    public Object getValue() {
        return entry.getValue();
    }

    // Javadoc inherited.
    public void schedule(Timer timer, Period period) {
        timer.schedule(new CleanupTimerTask(this), period.inMillis());
    }

    public void finalize() {
        cleanup("Cache entry updater was finalized " +
                "before the entry was updated");
    }

    /**
     * A task that will clean up if it is not updated within the specified
     * time.
     */
    private static class CleanupTimerTask
            extends TimerTask {

        /**
         * A weak reference to the {@link AsyncUpdate}.
         *
         * <p>This is needed because a hard reference would prevent the
         * {@link AsyncUpdate} from being garbage collected until after the
         * time out had expired and so would prevent the {@link AsyncUpdate}
         * from being garbage collected if the caller had discarded its
         * reference.</p>
         *
         * <p>A weak reference is used instead of a soft reference because the
         * garbage collector MUST clear weak references when it can but MAY
         * clear soft ones.</p>
         *
         */
        private WeakReference reference;

        /**
         * Initialise.
         *
         * @param update The {@link AsyncUpdate} to clear.
         */
        public CleanupTimerTask(AsyncUpdate update) {
            reference = new WeakReference(update);
        }

        // Javadoc inherited.
        public void run() {
            AsyncUpdate update = (AsyncUpdate) reference.get();
            if (update != null) {
                update.cleanup("Timed out");
                reference.clear();
            }
        }
    }
}
