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

import com.volantis.mcs.policies.PolicyType;

import java.util.HashMap;
import java.util.Map;

/**
 * A map from {@link PolicyType} to {@link CacheControlConstraints}.
 */
public class SeparateCacheControlConstraintsMap
        implements CacheControlConstraintsMap {

    /**
     * The map from {@link PolicyType} to {@link CacheControlConstraints}.
     */
    private final Map type2Constraints;

    /**
     * Initialise.
     */
    public SeparateCacheControlConstraintsMap() {
        type2Constraints = new HashMap();
    }

    /**
     * Add constraints.
     *
     * @param policyType  The policy type to which the constraints apply.
     * @param constraints The constraints.
     */
    public void addConstraints(
            PolicyType policyType,
            CacheControlConstraints constraints) {
        type2Constraints.put(policyType, constraints);
    }

    // Javadoc inherited.
    public CacheControlConstraints getConstraints(PolicyType policyType) {
        if (policyType == null) {
            throw new IllegalArgumentException("policyType cannot be null");
        }
        return (CacheControlConstraints) type2Constraints.get(policyType);
    }
}
