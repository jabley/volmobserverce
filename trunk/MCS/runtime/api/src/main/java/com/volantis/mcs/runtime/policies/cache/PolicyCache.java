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

import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.group.Group;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;

/**
 * Policy specific extension of the cache.
 *
 * <p>The policy cache is organized as follows:</p>
 *
 * <p>The root group has two sub groups, one for local policies and one for
 * remote policies. Each of those has at least one sub group which is the
 * default group into which go those policies that are in a project that has
 * not defined its own specific group. They also have a sub group for each
 * project that defines its own partition. The remote group also has a sub
 * group for each partition defined as a path prefix, aka quota.</p>
 *
 * <p>e.g.</p>
 * <pre>
 * root
 *  |
 *  +----- local
 *  |       |
 *  |       +----- default
 *  |       |
 *  |       +----- projects/welcome
 *  |       |
 *  |       +----- projects/my-welcome
 *  |
 *  +----- remote
 *  |       |
 *  |       +----- default
 *  |       |
 *  |       +----- http://remote/other/project (remote project)
 *  |       |
 *  |       +----- http://remote/other/quota   (remote quota)
 * </pre>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface PolicyCache {

    /**
     * Retrieve the object associated with the specified key.
     *
     * <p>If no entry for the object exists in the cache then it will invoke
     * the specified provider.</p>
     *
     * @param key      The key to the object.
     * @param provider The object responsible for providing the object to the
     *                 cache.
     * @return The object associated with the specified key.
     * @todo Refactor this to make it more specific to policies and less generic.
     */
    Object retrieve(Object key, CacheableObjectProvider provider);

    /**
     * Get the key to use to access the specified policy in the specified
     * project.
     *
     * <p>If the project is remote then the name will be the key, otherwise
     * both the project and the name will be the key.</p>
     *
     * @param project The project to search for the policy.
     * @param name    The name of the policy.
     * @return The object to use as the key into the cache.
     * @throws IllegalArgumentException if the project was remote but the
     *                                  name wasn't, or the name was but the project wasn't.
     */
    Object getKey(RuntimeProject project, String name);

    /**
     * Select the appropriate group for the specified entry.
     *
     * @param key
     * @param policy
     * @return The group, will not be null.
     */
    Group selectGroup(final Object key, final ActivatedPolicy policy);

    /**
     * Get the default {@link CacheControl} information for the specified key.
     *
     * <p>If the key is for a local policy then return the local defaults,
     * otherwise return the global ones.</p>
     *
     * @param project The project to which the policy was thought to belong.
     * @param key     The key for which the cache control information is to be
     *                selected.
     * @return The default cache control information.
     */
    CacheControl getDefaultCacheControl(RuntimeProject project, Object key);

    /**
     * Flushes the cache for the specified policy type.
     *
     * @param policyType The policy type to flush, if null causes all policies
     *                   of any type to be flushed.
     */
    void flush(PolicyType policyType);

    /**
     * Flushes caches for all policy types
     */
    void flushAll();

    /**
     * Flush the policies of the specified type that belong to the specified
     * project,
     *
     * @param project    The project, may not be null.
     * @param policyType The policy type to flush, if null causes all policies
     *                   of any type to be flushed.
     */
    void flushProject(RuntimeProject project, PolicyType policyType);

    /**
     * Get the remote group that matches the specified partition path.
     *
     * @param path The path whose containing group is required.
     * @return The remote group that contains the specified path, or null.
     */
    Group getRemotePartitionGroup(String path);

    /**
     * The local group.
     *
     * @return The group that contains all local policies.
     */
    Group getLocalGroup();

    /**
     * Get the local default group.
     *
     * @return The local default group.
     */
    Group getLocalDefaultGroup();

    /**
     * Get the constraints on the local partitions.
     *
     * @return The constraints on local partitions.
     */
    PolicyCachePartitionConstraints getLocalPartitionConstraints();

    /**
     * The remote group.
     *
     * @return The group that contains all remote policies.
     */
    Group getRemoteGroup();

    /**
     * Get the remote default group.
     *
     * @return The remote default group.
     */
    Group getRemoteDefaultGroup();

    /**
     * Get the constraints on the remote partitions.
     *
     * @return The constraints on remote partitions.
     */
    PolicyCachePartitionConstraints getRemotePartitionConstraints();

    /**
     * Flush a specific policy by name.
     *
     * <p>This only applies to remote policies as they are the ones stored
     * using just the name as the key.</p>
     *
     * @param name The name of the policy.
     */
    void flushRemotePolicy(String name);

    /**
     * Get a filter that will select policies of the specified type.
     *
     * @param policyType The policy type on which to filter, if null causes all
     *                   policies of any type to be selected.
     * @return A filter for the specified type, or null if the policy type was
     *         null.
     */
    CacheEntryFilter getFilter(PolicyType policyType);
}
