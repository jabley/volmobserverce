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

package com.volantis.mcs.runtime.policies.composite;

import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.RolloverImagePolicy;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedConcretePolicyImpl;

/**
 * Implementation of {@link ActivatedRolloverImagePolicy}.
 */
public class ActivatedRolloverImagePolicyImpl
        extends ActivatedConcretePolicyImpl
        implements ActivatedRolloverImagePolicy {

    /**
     * The underlying unactivated policy.
     */
    private final RolloverImagePolicy delegate;

    /**
     * Initialise.
     *
     * @param delegate       The underlying unactivated policy.
     * @param actualProject  The actual project containing the policy.
     * @param logicalProject The logical project containing the policy.
     */
    public ActivatedRolloverImagePolicyImpl(
            RolloverImagePolicy delegate, RuntimeProject actualProject,
            RuntimeProject logicalProject) {
        super(actualProject, logicalProject);
        this.delegate = delegate;
    }

    // Javadoc inherited.
    protected ConcretePolicy getConcretePolicy() {
        return delegate;
    }

    // Javadoc inherited.
    public RolloverImagePolicyBuilder getRolloverImagePolicyBuilder() {
        return delegate.getRolloverImagePolicyBuilder();
    }

    // Javadoc inherited.
    public PolicyReference getNormalPolicy() {
        return delegate.getNormalPolicy();
    }

    // Javadoc inherited.
    public PolicyReference getOverPolicy() {
        return delegate.getOverPolicy();
    }
}
