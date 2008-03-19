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
import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyType;

/**
 * Filters entries based on the type of the policy.
 */
class PolicyTypeCacheEntryFilter
        implements CacheEntryFilter {

    /**
     * The policy type.
     */
    private final PolicyType policyType;

    /**
     * Initialise.
     *
     * @param policyType The policy type.
     */
    public PolicyTypeCacheEntryFilter(PolicyType policyType) {
        this.policyType = policyType;
    }

    // Javadoc inherited.
    public boolean select(CacheEntry entry) {
        Policy policy = (Policy) entry.getValue();
        return (policy != null && policy.getPolicyType() == policyType);
    }
}
