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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;

import java.util.HashMap;
import java.util.Map;

/**
 * Selects the appropriate {@link PolicyActivator} to use based on the
 * {@link PolicyType} of the {@link PolicyBuilder} being activated.
 */
public class MultiplexingPolicyActivator
        implements PolicyActivator {

    /**
     * Map from {@link PolicyType} to {@link PolicyActivator}.
     */
    private final Map policyTypeToActivator;

    /**
     * Initialise.
     */
    public MultiplexingPolicyActivator() {
        policyTypeToActivator = new HashMap();
    }


    /**
     * Add the given activator to the set of activators to be used
     * for the given class of repository object.
     *
     * @param policyType the type of policy for which
     *                   the activator should be registered
     * @param activator  the activator being registered
     */
    public void addActivator(
            PolicyType policyType,
            PolicyActivator activator) {

        if (policyTypeToActivator.containsKey(policyType)) {
            throw new IllegalStateException("Activator for " + policyType +
                    " already added");
        }

        policyTypeToActivator.put(policyType, activator);
    }

    /**
     * Get the activator for the specified type.
     * @param policyType The policy type.
     * @return The activator for the type.
     * @throws IllegalArgumentException if no activator could be found for the
     * policy type.
     */
    private PolicyActivator getActivator(PolicyType policyType) {
        PolicyActivator activator = (PolicyActivator)
                policyTypeToActivator.get(policyType);
        if (activator == null) {
            throw new IllegalArgumentException(
                    "No activator found for " + policyType);
        }
        return activator;
    }

    // Javadoc inherited.
    public ActivatedPolicy activate(
            RuntimeProject actualProject, PolicyBuilder policyBuilder,
            RuntimeProject logicalProject) {
        PolicyActivator activator = getActivator(policyBuilder.getPolicyType());
        ActivatedPolicy policy = activator.activate(
                actualProject, policyBuilder, logicalProject);
        return policy;
    }
}
