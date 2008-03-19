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

package com.volantis.cache.impl.notification;

import com.volantis.cache.CacheEntry;
import com.volantis.cache.notification.RemovalListener;

/**
 * A list of {@link RemovalListener}.
 *
 * <p>This class is optimized to make event dispatching as fast as possible
 * at the expense of adding and removing listeners.</p>
 *
 * <p>Adding and removing listeners take a copy of the array of listeners and
 * add or remove the appropriate listener. They do this while synchronized on
 * the instance to ensure that concurrent calls work properly. Dispatching an
 * event takes a reference to the existing array while synchronized on this
 * and then iterates over the array unsynchronized. This is safe to do because
 * the existing array is never modified.</p>
 */
public class RemovalListenerList {

    /**
     * An empty array of listeners.
     */
    private static final RemovalListener[] EMPTY_ARRAY = new RemovalListener[0];

    /**
     * The array of listeners.
     */
    private RemovalListener[] listeners;

    /**
     * Initialise.
     */
    public RemovalListenerList() {
        this.listeners = EMPTY_ARRAY;
    }

    /**
     * Add the listener to the list of listeners.
     *
     * @param listener The listeners.
     * @throws IllegalStateException if attempting to add a listener that already
     *                               exists in the list.
     */
    public void addListener(RemovalListener listener) {
        // Synchronize as this may be called concurrently.
        synchronized (this) {
            int found = findListener(listener);
            if (found > -1) {
                throw new IllegalStateException("Listener " + listener +
                        " has already been added");
            }

            // Create a new array that can contain the original plus the
            // new listener. Doing this on add means that it is not
            // necessary to synchronize on the array while dispatching
            // events which usually happens far more often than adding
            // listeners.
            int count = listeners.length;
            RemovalListener[] newListeners =
                    new RemovalListener[count + 1];
            System.arraycopy(listeners, 0, newListeners, 0, count);
            newListeners[count] = listener;
            listeners = newListeners;
        }
    }

    /**
     * Remove the listener from the list of listeners.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(RemovalListener listener) {
        // Synchronize as this may be called concurrently.
        synchronized (this) {
            int found = findListener(listener);
            if (found == -1) {
                throw new IllegalStateException("Listener " + listener +
                        " is not in the list");
            }

            int count = listeners.length;
            if (count == 1) {
                listeners = EMPTY_ARRAY;
            } else {
                int newSize = count - 1;
                RemovalListener[] newListeners =
                        new RemovalListener[newSize];

                if (found > 0) {
                    // Copy the listeners from before the one being
                    // removed.
                    System.arraycopy(listeners, 0, newListeners, 0, found);
                }

                if (found < newSize) {
                    // Copy the listeners from after the one being
                    // removed.
                    System.arraycopy(listeners, found + 1,
                            newListeners, found,
                            newSize - found);
                }
                listeners = newListeners;
            }
        }
    }

    /**
     * Dispatch the remove event to the listeners.
     *
     * @param entry The entry that has been removed.
     */
    public void dispatchRemovedEntryEvent(CacheEntry entry) {
        RemovalListener[] list;
        synchronized (this) {
            // Get a reference to the array while synchronized. The array
            // contents are never modified so can be used while unsyncronized.
            list = listeners;
        }

        for (int i = 0; i < list.length; i++) {
            RemovalListener listener = list[i];
            listener.entryRemoved(entry);
        }
    }
    
    /**
     * Find the listener in the list.
     *
     * <p>This must only be called while synchronized on this.</p>
     *
     * @param listener The listener to find.
     * @return The index of the listener, or -1 if the listener could not be
     *         found.
     */
    private int findListener(RemovalListener listener) {
        // Iterate over the existing array looking for the listener.
        int found = -1;
        for (int i = 0; i < listeners.length && found == -1; i++) {
            RemovalListener removalListener = listeners[i];
            if (removalListener == listener) {
                found = i;
            }
        }
        return found;
    }
}
