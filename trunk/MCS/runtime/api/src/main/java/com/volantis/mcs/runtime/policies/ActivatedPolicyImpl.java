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

import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.runtime.RuntimeProject;

/**
 * Base for all activated {@link Policy}s.
 */
public abstract class ActivatedPolicyImpl
        implements ActivatedPolicy {

    /**
     * The actual project.
     */
    private final RuntimeProject actualProject;

    /**
     * The logical project.
     */
    private final RuntimeProject logicalProject;

    /**
     * Initialise.
     *
     * @param actualProject  The actual project containing the policy.
     * @param logicalProject The logical project containing the policy.
     */
    protected ActivatedPolicyImpl(
            RuntimeProject actualProject, RuntimeProject logicalProject) {
        this.actualProject = actualProject;
        this.logicalProject = logicalProject;
    }

    /**
     * Get the underlying {@link Policy}.
     *
     * @return The underlying {@link Policy}.
     */
    protected abstract Policy getPolicy();

    // Javadoc inherited.
    public RuntimeProject getActualProject() {
        return actualProject;
    }

    // Javadoc inherited.
    public RuntimeProject getLogicalProject() {
        return logicalProject;
    }

    // Javadoc inherited.
    public PolicyBuilder getPolicyBuilder() {
        return getPolicy().getPolicyBuilder();
    }

    // Javadoc inherited.
    public String getName() {
        return getPolicy().getName();
    }

    // Javadoc inherited.
    public CacheControl getCacheControl() {
        return getPolicy().getCacheControl();
    }

    // Javadoc inherited.
    public PolicyType getPolicyType() {
        return getPolicy().getPolicyType();
    }
}
