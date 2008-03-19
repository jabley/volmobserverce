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

import com.volantis.cache.Cache;
import com.volantis.cache.CacheBuilder;
import com.volantis.cache.CacheFactory;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.shared.system.SystemClock;

/**
 * Builds a {@link PolicyCache}.
 */
public class PolicyCacheBuilder {

    /**
     * The underlying cache factory.
     */
    private final CacheFactory factory;

    /**
     * The builder for the remote quota map.
     */
    private final RemotePartitionsBuilder partitionsBuilder;

    /**
     * The newly created underlying cache.
     */
    private final Cache cache;

    /**
     * The group that contains all local policies.
     */
    private final Group localGroup;

    /**
     * The sub group of {@link #localGroup} that contains all local policies
     * that belong to projects that do not have their own partition.
     */
    private final Group localDefaultGroup;

    /**
     * The group that contains all remote policies.
     */
    private final Group remoteGroup;

    /**
     * The constraints on cache partitions for local projects.
     */
    private PolicyCachePartitionConstraints localPartitionConstraints;

    /**
     * The constraints on cache partitions for remote projects.
     */
    private PolicyCachePartitionConstraints remotePartitionConstraints;

    /**
     * Initialise.
     *
     * @param localSize  The local size.
     * @param remoteSize The remote size.
     */
    public PolicyCacheBuilder(int localSize, int remoteSize) {
        this(localSize, remoteSize, SystemClock.getDefaultInstance());
    }

    /**
     * Initialise.
     *
     * @param localSize  The local size.
     * @param remoteSize The remote size.
     * @param clock      The clock used to calculate and check expiration times.
     */
    public PolicyCacheBuilder(
            int localSize, int remoteSize,
            SystemClock clock) {
        factory = CacheFactory.getDefaultInstance();
        partitionsBuilder = new RemotePartitionsBuilder();

        int totalSize;
        if (localSize == Integer.MAX_VALUE ||
                remoteSize == Integer.MAX_VALUE) {
            totalSize = Integer.MAX_VALUE;
        } else {
            totalSize = localSize + remoteSize;
        }

        CacheBuilder builder = factory.createCacheBuilder();
        builder.setMaxCount(totalSize);
        builder.setExpirationChecker(new PolicyExpirationChecker());
        builder.setClock(clock);
        cache = builder.buildCache();

        Group root = getRootGroup();

        GroupBuilder groupBuilder;

        // Create the outermost local group.
        groupBuilder = factory.createGroupBuilder();
        groupBuilder.setMaxCount(localSize);

        localGroup = root.addGroup(PolicyCacheImpl.LOCAL_GROUP_KEY,
                groupBuilder);

        // Create the local default group. It has the same size as the
        // outermost one at the moment because there is no way to configure
        // it any differently.
        localDefaultGroup = localGroup.addGroup(
                PolicyCacheImpl.LOCAL_DEFAULT_GROUP_KEY, groupBuilder);

        // Create the outermost remote group.
        groupBuilder = factory.createGroupBuilder();
        groupBuilder.setMaxCount(remoteSize);

        remoteGroup = root.addGroup(PolicyCacheImpl.REMOTE_GROUP_KEY,
                groupBuilder);

        // Create the remote default group. It has the same size as the
        // outermost one at the moment because there is no way to configure
        // it any differently.
        remoteGroup.addGroup(PolicyCacheImpl.REMOTE_DEFAULT_GROUP_KEY,
                groupBuilder);

    }

    /**
     * Get the root group from the underlying cache.
     *
     * @return The root group.
     */
    private Group getRootGroup() {
        return cache.getRootGroup();
    }

    /**
     * Add a policy type specific group to the default local group.
     *
     * @param policyType The policy type.
     * @param maxCount   The size of the group.
     */
    public void addDefaultLocalPolicySpecificGroup(
            PolicyType policyType, int maxCount) {

        GroupBuilder groupBuilder = factory.createGroupBuilder();
        groupBuilder.setMaxCount(maxCount);

        // Add the group for the policy type to the local group.
        localDefaultGroup.addGroup(policyType, groupBuilder);
    }

    /**
     * Add a quota.
     *
     * @param url   The url.
     * @param quota The quota as a percentage of the default remote group size.
     */
    public void addDefaultRemotePathSpecificGroup(String url, int quota) {
        partitionsBuilder.addPartition(url, quota);
    }

    /**
     * Get the local partition constraints.
     *
     * @return The local partition constraints.
     */
    public PolicyCachePartitionConstraints getLocalPartitionConstraints() {
        return localPartitionConstraints;
    }

    /**
     * Set the local partition constraints.
     *
     * @param localPartitionConstraints The local partition constraints.
     */
    public void setLocalPartitionConstraints(
            PolicyCachePartitionConstraints localPartitionConstraints) {
        this.localPartitionConstraints = localPartitionConstraints;
    }

    /**
     * Get the remote partition constraints.
     *
     * @return The remote partition constraints.
     */
    public PolicyCachePartitionConstraints getRemotePartitionConstraints() {
        return remotePartitionConstraints;
    }

    /**
     * Set the remote partition constraints.
     *
     * @param remotePartitionConstraints The remote partition constraints.
     */
    public void setRemotePartitionConstraints(
            PolicyCachePartitionConstraints remotePartitionConstraints) {
        this.remotePartitionConstraints = remotePartitionConstraints;
    }

    /**
     * Get the newly constructed {@link PolicyCache}.
     *
     * @return The {@link PolicyCache}.
     */
    public PolicyCache getPolicyCache() {
        // Get the remote group.
        RemotePartitions partitions = partitionsBuilder.buildRemotePartitions(
                remoteGroup);

        if (localPartitionConstraints == null) {
            throw new IllegalStateException(
                    "Local partitions constraints not set");
        }

        if (remotePartitionConstraints == null) {
            throw new IllegalStateException(
                    "remote partitions constraints not set");
        }

        return new PolicyCacheImpl(cache, partitions,
                localPartitionConstraints, remotePartitionConstraints);
    }
}
