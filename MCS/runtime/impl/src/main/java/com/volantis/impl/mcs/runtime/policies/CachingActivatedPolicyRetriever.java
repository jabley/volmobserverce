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

package com.volantis.impl.mcs.runtime.policies;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.ActivatedPolicyRetriever;
import com.volantis.mcs.runtime.policies.cache.CacheablePolicyProvider;
import com.volantis.mcs.runtime.policies.cache.PolicyCache;

public class CachingActivatedPolicyRetriever
        implements ActivatedPolicyRetriever {

    private final PolicyCache cache;

    private final ActivatedPolicyRetriever delegate;

    public CachingActivatedPolicyRetriever(
            PolicyCache cache, ActivatedPolicyRetriever delegate) {
        this.cache = cache;
        this.delegate = delegate;
    }

    public ActivatedPolicy retrievePolicy(
            final RuntimeProject project, final String name)
            throws RepositoryException {

        Object key = cache.getKey(project, name);

        CacheablePolicyProvider provider = new CacheablePolicyProvider(
                delegate, project, name, cache);
        ActivatedPolicy policy = (ActivatedPolicy)
                cache.retrieve(key, provider);

        return policy;
    }
}
