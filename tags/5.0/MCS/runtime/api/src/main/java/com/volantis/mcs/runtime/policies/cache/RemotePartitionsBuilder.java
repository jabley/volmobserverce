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

package com.volantis.mcs.runtime.policies.cache;

import com.volantis.cache.group.Group;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Builder for a {@link RemotePartitionsImpl}.
 */
public class RemotePartitionsBuilder {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(RemotePartitionsImpl.class);

    /**
     * The list of {@link Partition}.
     */
    private final List partitions;

    /**
     * Initialise.
     */
    public RemotePartitionsBuilder() {
        partitions = new ArrayList();
    }

    /**
     * Add a partition.
     *
     * @param prefix The prefix of URLs that belong in the partition.
     * @param size   The size of the partition.
     */
    public void addPartition(String prefix, int size) {
        ListIterator iterator = partitions.listIterator();

        // Partitions can only be specified for sets of URLs where no URL is a
        // prefix of another. We need to check the new URL against the ones
        // already in the map to ensure that this restriction is not
        // broken.
        //
        // If the new prefix starts with a prefix from an existing partition then
        // it is ignored as it is either equal to it, or more specific.
        //
        // If the prefix from an existing partition starts with the new prefix then
        // the existing partition is removed and a new prefix is added.

        Partition found = null;
        while (iterator.hasNext() && found == null) {
            Partition partition = (Partition) iterator.next();

            String existing = partition.getPrefix();

            if (prefix.startsWith(existing)) {
                // The new partition is either exactly the same as an existing one,
                // or is more specific, in either case it should just be
                // ignored.
                found = partition;
                if (prefix.length() == existing.length()) {
                    // Partition has been specified for same URL multiple times.
                    logger.warn("duplicate-cache-key",
                            new Object[]{prefix, "PolicyCache"});
                } else {
                    // Attempted to add prefix that is more specific than
                    // existing one.
                    logger.warn("cache-key-precedence",
                            new Object[]{prefix, existing});
                }
            } else if (existing.startsWith(prefix)) {
                // The new prefix is shorter than the existing one so use that
                // one instead of the existing one.
                iterator.remove();
                break;
            }
        }

        Partition partition;
        if (found == null) {

            partition = new Partition(prefix, size);
            partitions.add(partition);
        } else {
            partition = found;
        }
    }

    /**
     * Build the {@link RemotePartitionsImpl} instance.
     *
     * @param defaultRemoteGroup The parent group.
     * @return The {@link RemotePartitionsImpl}.
     */
    public RemotePartitionsImpl buildRemotePartitions(Group defaultRemoteGroup) {
        return new RemotePartitionsImpl(partitions, defaultRemoteGroup);
    }

    public List getPartitions() {
        return partitions;
    }

    /**
     * The definition of a partition before it is built.
     */
    static class Partition {

        /**
         * The prefix of URLs that belong in this partition.
         */
        private final String prefix;

        /**
         * The size of the partition.
         */
        private final int size;

        /**
         * Initialise.
         *
         * @param prefix The prefix of URLs that belong in this partition.
         * @param size   The size of the partition.
         */
        public Partition(String prefix, int size) {
            this.prefix = prefix;
            this.size = size;
        }

        /**
         * Get the prefix.
         *
         * @return The prefix.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Get the size.
         *
         * @return The size.
         */
        public int getSize() {
            return size;
        }
    }
}
