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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.repository.remote;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyBuildersResponse;
import com.volantis.mcs.remote.PolicyBuilders;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.ActivatedPolicyRetriever;
import com.volantis.mcs.runtime.policies.PolicyActivator;
import com.volantis.mcs.runtime.policies.cache.CacheablePolicyProvider;
import com.volantis.mcs.runtime.policies.cache.PolicyCache;
import com.volantis.mcs.runtime.repository.remote.xml.RemotePolicyBuildersReader;
import com.volantis.shared.system.SystemClock;

import java.util.Iterator;

/**
 * Preloads policies from a remote URL.
 */
public class RemotePolicyPreloader {

    /**
     * Activates policies.
     */
    private final PolicyActivator policyActivator;

    /**
     * Reads {@link PolicyBuilders}.
     */
    private final RemotePolicyBuildersReader buildersReader;

    /**
     * The cache into which the policies should be added.
     */
    private final PolicyCache policyCache;

    /**
     * Initialise.
     *
     * @param buildersReader  Reads {@link com.volantis.mcs.remote.PolicyBuilders}.
     * @param policyActivator Activates policies.
     * @param policyCache     The cache into which the policies should be added.
     */
    public RemotePolicyPreloader(
            RemotePolicyBuildersReader buildersReader,
            PolicyActivator policyActivator,
            PolicyCache policyCache) {

        this.buildersReader = buildersReader;
        this.policyActivator = policyActivator;
        this.policyCache = policyCache;
    }

    /**
     * Preload the policies that are found in the resource at the specific
     * url.
     *
     * @param url The location of the resource containing definitions of a
     *            number of policies.
     * @throws com.volantis.mcs.repository.RepositoryException
     *          If there was a problem accessing the
     *          policies.
     */
    public void preloadPolicies(String url)
            throws RepositoryException {

        if (!RemoteRepositoryHelper.isRemoteName(url)) {
            throw new IllegalArgumentException("Unqualified name " +
                    url);
        }

        PolicyBuildersResponse response =
                buildersReader.getPolicyBuilders(null, url);

        final RuntimeProject project = (RuntimeProject) response.getProject();

        PolicyBuilders builders = response.getBuilders();
        if (builders != null) {
            Iterator iterator = builders.getPolicyBuilders().iterator();
            while (iterator.hasNext()) {
                PolicyBuilder policyBuilder =
                        (PolicyBuilder) iterator.next();

                // Complete post-load initialization ("activation") for the
                // object. This call will do nothing if there are no
                // activators
                final ActivatedPolicy policy = policyActivator.activate(
                        project, policyBuilder, project);

                SingleRetriever retriever = new SingleRetriever(policy);
                String name = policy.getName();
                CacheablePolicyProvider provider = new CacheablePolicyProvider(
                        retriever, project, name, policyCache);
                policyCache.retrieve(name, provider);
            }
        }
    }

    private static class SingleRetriever
            implements ActivatedPolicyRetriever {

        private final ActivatedPolicy policy;

        public SingleRetriever(ActivatedPolicy policy) {
            this.policy = policy;
        }

        public ActivatedPolicy retrievePolicy(
                RuntimeProject project,
                String name) {
            return policy;
        }
    }
}
