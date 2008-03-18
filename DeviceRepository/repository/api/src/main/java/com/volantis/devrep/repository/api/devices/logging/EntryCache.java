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
package com.volantis.devrep.repository.api.devices.logging;

import java.lang.ref.SoftReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.Reference;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

/**
 * Cache to store log entries to filter out duplicates.
 */
public class EntryCache {
    private final Set cache = Collections.synchronizedSet(new HashSet());
    private final ReferenceQueue queue = new ReferenceQueue();

    /**
     * Adds the entry to the cache, if the cache doesn't contain the entry.
     *
     * Returns true, iff the cache didn't contain the entry.
     *
     * @param entry the entry to add
     * @return true if the entry is new to the cache
     */
    public boolean add(final Entry entry) {
        removeStaleEntries();
        final EntryReference reference = new EntryReference(entry, queue);
        return cache.add(reference);
    }

    /**
     * Removes gc'd entries.
     */
    private void removeStaleEntries() {
        for (Reference ref = queue.poll(); ref != null; ref = queue.poll()) {
            cache.remove(ref);
        }
    }

    private static class EntryReference extends SoftReference {
        private final int hashCode;

        public EntryReference(final Entry referent, final ReferenceQueue queue) {
            super(referent, queue);
            if (referent == null) {
                throw new IllegalArgumentException("Referent cannot be null");
            }
            hashCode = referent.hashCode();
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Object referent = get();
            final Object otherReferent = ((EntryReference) o).get();
            return referent == null && otherReferent == null ||
                referent != null && referent.equals(otherReferent);
        }

        public int hashCode() {
            return hashCode;
        }
    }
}
