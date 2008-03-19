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

import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;

import java.util.List;

/**
 * Base for all activated {@link ConcretePolicy}s.
 */
public abstract class ActivatedConcretePolicyImpl
        extends ActivatedPolicyImpl
        implements ConcretePolicy {

    /**
     * Initialise.
     *
     * @param actualProject  The actual project containing the policy.
     * @param logicalProject The logical project containing the policy.
     */
    protected ActivatedConcretePolicyImpl(
            RuntimeProject actualProject, RuntimeProject logicalProject) {
        super(actualProject, logicalProject);
    }

    // Javadoc inherited.
    protected Policy getPolicy() {
        return getConcretePolicy();
    }

    /**
     * Get the underlying {@link ConcretePolicy}.
     *
     * @return The underlying {@link ConcretePolicy}.
     */
    protected abstract ConcretePolicy getConcretePolicy();

    // Javadoc inherited.
    public List getAlternatePolicies() {
        return getConcretePolicy().getAlternatePolicies();
    }

    // Javadoc inherited.
    public PolicyReference getAlternatePolicy(PolicyType policyType) {
        return getConcretePolicy().getAlternatePolicy(policyType);
    }
}
