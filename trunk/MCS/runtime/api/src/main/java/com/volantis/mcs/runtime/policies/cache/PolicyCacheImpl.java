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
import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.group.Group;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;

/**
 * Implementation of {@link PolicyCache}.
 */
public class PolicyCacheImpl
        implements PolicyCache {

    /**
     * The key for the local group.
     */
    public static final Object LOCAL_GROUP_KEY = new Object() {
        public String toString() {
            return "<LOCAL_GROUP_KEY>";
        }
    };

    /**
     * The key for the remote group.
     */
    public static final Object REMOTE_GROUP_KEY = new Object() {
        public String toString() {
            return "<REMOTE_GROUP_KEY>";
        }
    };

    /**
     * The key for the local default group.
     */
    public static final Object LOCAL_DEFAULT_GROUP_KEY = new Object() {
        public String toString() {
            return "<LOCAL_DEFAULT_GROUP_KEY>";
        }
    };

    /**
     * The key for the remote default group.
     */
    public static final Object REMOTE_DEFAULT_GROUP_KEY = new Object() {
        public String toString() {
            return "<REMOTE_DEFAULT_GROUP_KEY>";
        }
    };

    /**
     * The underlying cache.
     */
    private final Cache delegate;

    /**
     * The map of quotas used by remote policies.
     */
    private final RemotePartitions partitions;

    /**
     * The remote cache control defaults.
     */
    private final PolicyCachePartitionConstraints localPartitionConstraints;
    private final PolicyCachePartitionConstraints remotePartitionConstraints;

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
     * The sub group of {@link #remoteGroup} that contains all remote policies
     * that belong to projects that do not have their own partition.
     */
    private final Group remoteDefaultGroup;

    /**
     * Initialise.
     *
     * @param cache                      The underlying cache.
     @param partitions                   The map of quotas used by remote
          *                                   policies.
     @param localPartitionConstraints
     @param remotePartitionConstraints
     */
    public PolicyCacheImpl(
            Cache cache, RemotePartitions partitions,
            PolicyCachePartitionConstraints localPartitionConstraints,
            PolicyCachePartitionConstraints remotePartitionConstraints) {
        delegate = cache;
        this.partitions = partitions;
        this.localPartitionConstraints = localPartitionConstraints;
        this.remotePartitionConstraints = remotePartitionConstraints;

        Group root = cache.getRootGroup();
        localGroup = root.getGroup(LOCAL_GROUP_KEY);
        localDefaultGroup = localGroup.getGroup(LOCAL_DEFAULT_GROUP_KEY);
        remoteGroup = root.getGroup(REMOTE_GROUP_KEY);
        remoteDefaultGroup = remoteGroup.getGroup(REMOTE_DEFAULT_GROUP_KEY);
    }

    // Javadoc inherited.
    public Object retrieve(Object key, CacheableObjectProvider provider) {
        return delegate.retrieve(key, provider);
    }

    // Javadoc inherited.
    public void flushProject(
            RuntimeProject project,
            PolicyType policyType) {

        CacheEntryFilter filter = getFilter(policyType);

        Group group = project.getCacheGroup();
        group.flush(filter);
    }

    // Javadoc inherited.
    public Group getRemotePartitionGroup(String path) {
        RemotePartition partition = partitions.getRemotePartition(path);
        return partition == null ? null : partition.getGroup();
    }

    // Javadoc inherited.
    public Group getLocalGroup() {
        return localGroup;
    }

    // Javadoc inherited.
    public Group getLocalDefaultGroup() {
        return localDefaultGroup;
    }

    // Javadoc inherited.
    public PolicyCachePartitionConstraints getLocalPartitionConstraints() {
        return localPartitionConstraints;
    }

    public Group getRemoteGroup() {
        return remoteGroup;
    }

    // Javadoc inherited.
    public Group getRemoteDefaultGroup() {
        return remoteDefaultGroup;
    }

    // Javadoc inherited.
    public PolicyCachePartitionConstraints getRemotePartitionConstraints() {
        return remotePartitionConstraints;
    }

    // Javadoc inherited.
    public void flushRemotePolicy(String name) {
        delegate.removeEntry(name);
    }

    // Javadoc inherited.
    public CacheEntryFilter getFilter(PolicyType policyType) {
        CacheEntryFilter filter = null;
        if (policyType != null) {
            filter = new PolicyTypeCacheEntryFilter(policyType);
        }
        return filter;
    }

    // Javadoc inherited.
    public Object getKey(RuntimeProject project, String name) {

        Object key;

        boolean remote = project.isRemote();
        boolean global = remote && project.getContainsOrphans();

        // If the name is not project relative then the project must be the
        // global one, otherwise the project cannot be global.
        if (name.startsWith("/")) {
            if (global) {
                throw new IllegalArgumentException(
                        "Project is global but name '" + name +
                        "' is project relative");
            }

            if (remote) {
                key = project.makeAbsolutePolicyURL(name);
            } else {
                key = new ProjectSpecificKey(project, name);
            }

        } else {

            // Cannot have an absolute URL to a policy from a local project,
            // at least not at the moment. However, an absolute URL for a
            // policy in a remote project is allowed,
            if (!remote) {
                throw new IllegalArgumentException(
                        "Project is not remote and name '" + name +
                        "' is not project relative");
            }

            key = name;
        }

        return key;
    }

    /**
     * Organizes the policy cache.
     *
     * <p>Each project that specifies its own cache has its own group, the rest
     * use either the local default group, or the remote default group.</p>
     *
     * todo Maybe this should be done up front so every project has a cache group
     * set.
     */
    public Group selectGroup(final Object key, final ActivatedPolicy policy) {

        Group baseGroup = null;

        // If a policy was found then check it's project to see whether it
        // already has a cache group selected.
        if (policy != null) {
            RuntimeProject project = policy.getLogicalProject();
            baseGroup = project.getCacheGroup();
        }

        // If a group could not be found from the project then try it based on
        // the key.
        if (key instanceof ProjectSpecificKey) {
            ProjectSpecificKey projectSpecificKey = (ProjectSpecificKey) key;
            baseGroup = selectLocalProjectGroup(projectSpecificKey.getProject());
        } else if (key instanceof String) {
            String stringKey = (String) key;
            baseGroup = selectRemoteProjectGroup(stringKey);
        } else {
            throw new IllegalStateException(
                    "Key is not a String, or a ProjectSpecificKey, it is '" +
                    key + "'");
        }

        Group policyGroup = null;
        if (policy != null) {
            // Get the group specific to the policy type.
            PolicyType policyType = policy.getPolicyType();
            policyGroup = baseGroup.findGroup(policyType);
        }

        if (policyGroup == null) {
            policyGroup = baseGroup;
        }

        return policyGroup;
    }

    // Javadoc inherited.
    public CacheControl getDefaultCacheControl(
            RuntimeProject project, Object key) {

        CacheControl defaults = project.getCacheControlDefaults();
        if (defaults == null) {
            if (key instanceof ProjectSpecificKey) {
                // Local so return local defaults.
                defaults = localPartitionConstraints.getConstraints()
                        .getDefaultCacheControl();;
            } else {
                // Remote so return remote defaults.
                defaults = remotePartitionConstraints.getConstraints()
                        .getDefaultCacheControl();
            }

        }

        return defaults;
    }

    /**
     * Select the local project group.
     *
     * @param project The project.
     * @return The group used by the local project.
     */
    private Group selectLocalProjectGroup(RuntimeProject project) {

        // If the project has its own cache group then use that, else
        // use the root project.
        Group projectGroup = project.getCacheGroup();
        if (projectGroup == null) {
            if (project.isRemote()) {
                throw new IllegalStateException("Only supports local projects");
            } else {
                projectGroup = localDefaultGroup;
            }
        }

        return projectGroup;
    }

    /**
     * Select the remote project group.
     *
     * @param path The path, to a quota
     * @return The group for the quota, or the default one if the path does not
     *         match a quota.
     */
    private Group selectRemoteProjectGroup(String path) {

        Group group = getRemotePartitionGroup(path);
        if (group == null) {
            group = getRemoteDefaultGroup();
        }

        return group;
    }

    // Javadoc inherited.
    public void flush(PolicyType policyType) {
        Group root = delegate.getRootGroup();
        root.flush(getFilter(policyType));
    }

    // Javadoc inherited.
    public void flushAll() {
        Group root = delegate.getRootGroup();
        root.flush(null);
    }
}
