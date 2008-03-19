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


/**
 * A list of cache entries.
 *
 * <p>Each cache entry has links to and from the next entry in the list, this
 * structure is used as it allows very efficient removal of an entry without
 * actually searching the entire list. The head and tail of the list are marked
 * using sentinels. This structure is used instead of using the first and last
 * entry respectively as it simplifies removal and insertion code at the slight
 * expense of iterating. As the former operations are performed far more often
 * than the latter this seems a reasonable trade to make.</p>
 */
public class CacheEntryList {

    /**
     * The sentinel at the head of the list.
     */
    private final Link headSentinel;

    /**
     * The sentinel at the tail of the list.
     */
    private final Link tailSentinel;

    /**
     * The size of the list.
     */
    private int size;

    /**
     * Initialise.
     */
    public CacheEntryList() {

        headSentinel = new Link(null);
        tailSentinel = new Link(null);
        headSentinel.next = tailSentinel;
        tailSentinel.prev = headSentinel;
        size = 0;
    }

    /**
     * Add the entry to the head of the list.
     *
     * @param entry The entry to add.
     */
    public void addHeadEntry(InternalCacheEntry entry) {
        Link link = entry.getLink();
        headSentinel.next.prev = link;
        link.next = headSentinel.next;
        link.prev = headSentinel;
        headSentinel.next = link;
        size += 1;
    }

    /**
     * Get the head entry.
     *
     * @return The head entry, or null if the list is empty.
     */
    public InternalCacheEntry getHeadEntry() {
        return headSentinel.next.entry;
    }

    /**
     * Get the tail entry.
     *
     * @return The tail entry, or null if the list is empty.
     */
    public InternalCacheEntry getTailEntry() {
        return tailSentinel.prev.entry;
    }

    /**
     * Remove the entry from the list.
     *
     * @param entry The entry to remove.
     */
    public void remove(InternalCacheEntry entry) {
        Link link = entry.getLink();
        link.next.prev = link.prev;
        link.prev.next = link.next;
        size -= 1;
    }

    /**
     * Get the next entry.
     *
     * @param entry The entry.
     * @return The next entry, or null if this is at the end of the list.
     */
    public InternalCacheEntry getNextEntry(InternalCacheEntry entry) {
        Link link = entry.getLink();
        return link.next.entry;
    }

    /**
     * Get the size of the list.
     *
     * @return The size of the list.
     */
    public int size() {
        return size;
    }

    /**
     * A link in the chain of linked lists.
     */
    public static class Link {

        /**
         * The reference to the next link.
         *
         * <p>This must only be modified while holding the mutex on the
         * {@link CacheEntryList} to which this link belongs, or to which it
         * is being added.</p>
         */
        Link next;

        /**
         * The reference to the next link.
         *
         * <p>This must only be modified while holding the mutex on the
         * {@link CacheEntryList} to which this entry belongs, or to which it
         * is being added.</p>
         */
        Link prev;

        /**
         * The link to the cache entry.
         */
        final InternalCacheEntry entry;

        /**
         * Initialise.
         *
         * @param entry The entry of this link.
         */
        public Link(InternalCacheEntry entry) {
            this.entry = entry;
        }
    }
}
