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

import com.volantis.mcs.policies.ButtonImagePolicy;
import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedConcretePolicyImpl;

/**
 * Implementation of {@link ActivatedButtonImagePolicy}.
 */
public class ActivatedButtonImagePolicyImpl
        extends ActivatedConcretePolicyImpl
        implements ActivatedButtonImagePolicy {

    /**
     * The underlying unactivated policy.
     */
    private final ButtonImagePolicy delegate;

    /**
     * Initialise.
     *
     * @param delegate       The underlying unactivated policy.
     * @param actualProject  The actual project containing the policy.
     * @param logicalProject The logical project containing the policy.
     */
    public ActivatedButtonImagePolicyImpl(
            ButtonImagePolicy delegate, RuntimeProject actualProject,
            RuntimeProject logicalProject) {
        super(actualProject, logicalProject);
        this.delegate = delegate;
    }

    // Javadoc inherited.
    protected ConcretePolicy getConcretePolicy() {
        return delegate;
    }

    // Javadoc inherited.
    public ButtonImagePolicyBuilder getButtonImagePolicyBuilder() {
        return delegate.getButtonImagePolicyBuilder();
    }

    // Javadoc inherited.
    public PolicyReference getUpPolicy() {
        return delegate.getUpPolicy();
    }

    // Javadoc inherited.
    public PolicyReference getDownPolicy() {
        return delegate.getDownPolicy();
    }

    // Javadoc inherited.
    public PolicyReference getOverPolicy() {
        return delegate.getOverPolicy();
    }
}
