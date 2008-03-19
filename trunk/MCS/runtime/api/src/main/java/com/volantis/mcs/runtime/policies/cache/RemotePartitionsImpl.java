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

import com.volantis.cache.CacheFactory;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A collection of {@link RemotePartition}.
 */
public class RemotePartitionsImpl
        implements RemotePartitions {

    /**
     * The list of {@link RemotePartition}.
     */
    private final List partitions;

    /**
     * Initialise.
     *
     * @param partitionList      The list of
     *                           {@link RemotePartitionsBuilder.Partition}.
     * @param defaultRemoteGroup The parent group.
     */
    public RemotePartitionsImpl(List partitionList, Group defaultRemoteGroup) {

        partitions = new ArrayList();

        // Iterate over all the partitions in the builder creating a group for
        // them.
        CacheFactory factory = CacheFactory.getDefaultInstance();
        for (Iterator i = partitionList.iterator(); i.hasNext();) {
            RemotePartitionsBuilder.Partition builderQuota =
                    (RemotePartitionsBuilder.Partition) i.next();
            GroupBuilder groupBuilder = factory.createGroupBuilder();
            groupBuilder.setMaxCount(builderQuota.getSize());
            String prefix = builderQuota.getPrefix();
            Group group = defaultRemoteGroup.addGroup(prefix,
                    groupBuilder);

            partitions.add(new RemotePartition(prefix, group));
        }
    }

    public RemotePartition getRemotePartition(String url) {
        ListIterator iterator = partitions.listIterator();
        while (iterator.hasNext()) {
            RemotePartition partition = (RemotePartition) iterator.next();

            String prefix = partition.getPrefix();

            if (url.startsWith(prefix)) {
                return partition;
            }
        }

        return null;
    }
}
