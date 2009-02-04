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

import com.volantis.cache.CacheEntry;
import com.volantis.cache.group.Group;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.ActivatedPolicyRetriever;
import com.volantis.shared.system.SystemClock;
import com.volantis.synergetics.log.LogDispatcher;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Implementation of {@link CacheableObjectProvider} that will retrieve
 * and manage {@link ActivatedPolicy}s.
 *
 * <p>A new instance of this needs to be created on every request for a policy
 * as it contains information specified to the request.</p>
 */
public class CacheablePolicyProvider
        implements CacheableObjectProvider {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CacheablePolicyProvider.class);

    /**
     * The underlying retriever.
     */
    private final ActivatedPolicyRetriever retriever;

    /**
     * The project from which the policy is to be retrieved.
     */
    private final RuntimeProject project;

    /**
     * The name of the policy that is being retrieved.
     */
    private final String name;

    /**
     * The policy cache, this is needed to get default cache control and to
     * select the appropriate group.
     */
    private final PolicyCache cache;

    /**
     * Initialise.
     *
     * @param retriever The underlying retriever.
     * @param project   The project from which the policy is to be retrieved.
     * @param name      The name of the policy that is being retrieved.
     * @param cache     The policy cache.
     */
    public CacheablePolicyProvider(
            ActivatedPolicyRetriever retriever,
            RuntimeProject project, String name,
            PolicyCache cache) {
        this.retriever = retriever;
        this.project = project;
        this.name = name;
        this.cache = cache;
    }

    // Javadoc inherited.
    public ProviderResult retrieve(
            SystemClock clock, Object key, CacheEntry entry) {

        CacheControl cacheControl;
        ActivatedPolicy policy;
        Throwable throwable = null;
        try {
            policy = retriever.retrievePolicy(project, name);

            // todo need to be able to handle the case where the policy could
            // todo not be retrieved but the project for the policy was found
            // todo and is not the same as the supplied one. i.e. when a
            // todo request is made to a remote repository which finds the
            // todo project for the policy but not the policy. The reason we
            // todo need to do that is because we need to use the actual
            // todo project for the policy to get default cache control stuff.

            if (policy == null) {
                cacheControl = cache.getDefaultCacheControl(project, key);
            } else {
                // Determine whether the policy is cacheable.
                cacheControl = policy.getCacheControl();
            }

        } catch (Throwable e) {
            // Catch all exceptions so that they will trigger the retry
            // mechanism if configured.
            if (logger.isErrorEnabled()) {
                logger.error(e);
            }

            cacheControl = cache.getDefaultCacheControl(project, key);
            policy = null;

            // Remember the throwable so that it can be returned back to the
            // cache which will place the entry into the error state.
            throwable = e;
        }

        // If this is cacheable then update the state, either the one
        // associated with the entry, or a newly created one if the entry has
        // not been supplied, or does not have one. The latter will only occur
        // if this is the first time that a policy is being retrieved for the
        // entry.
        //
        // If this is not cacheable then there is no point in updating the
        // state as while it will be stored in the entry it will not be used.
        // Therefore, in that case return null so that state based on an
        // uncacheable policy is not stored in the entry.

        final boolean cacheable = cacheControl.getCacheThisPolicy();
        CachedPolicyState state = null;
        if (cacheable) {

            ActivatedPolicy expiredPolicy = null;

            // Attempt to get the state from the entry if it has been provided.
            // If it has not been specified then the policy must previously
            // have been uncacheable.
            if (entry != null) {
                state = (CachedPolicyState) entry.getExtensionObject();
                expiredPolicy = (ActivatedPolicy) entry.getValue();
            }

            // Create a new state object if it did not exist before, or it did
            // and a new policy has been retrieved.
            if (state == null || policy != null) {
                state = new CachedPolicyState(clock, cacheControl, policy);
            } else {
                policy = state.retrieveFailed(clock, expiredPolicy);
            }
        }

        Group group = cache.selectGroup(key, policy);

        // If there is a policy then never return the throwable as we are
        // retaining during retry and the caller wants to see the retained
        // policy not an exception. Otherwise if a throwable is set then throw
        // that.
        ProviderResult result;
        if (policy != null || throwable == null) {
            result = new ProviderResult(policy, group, cacheable, state);
        } else if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new UndeclaredThrowableException(throwable);
        }
        return result;
    }
}
