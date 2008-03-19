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

package com.volantis.mcs.runtime.policies.base;

import com.volantis.mcs.policies.BaseURLPolicy;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicyImpl;

/**
 * Implementation of {@link ActivatedBaseURLPolicy}.
 */
public class ActivatedBaseURLPolicyImpl
        extends ActivatedPolicyImpl
        implements ActivatedBaseURLPolicy {

    /**
     * The underlying unactivated policy.
     */
    private final BaseURLPolicy delegate;

    /**
     * Initialise.
     *
     * @param delegate       The underlying unactivated policy.
     * @param actualProject  The actual project containing the policy.
     * @param logicalProject The logical project containing the policy.
     */
    public ActivatedBaseURLPolicyImpl(
            BaseURLPolicy delegate,
            RuntimeProject actualProject,
            RuntimeProject logicalProject) {
        super(actualProject, logicalProject);
        this.delegate = delegate;
    }

    // Javadoc inherited.
    protected Policy getPolicy() {
        return delegate;
    }

    // Javadoc inherited.
    public BaseURLPolicyBuilder getBaseURLPolicyBuilder() {
        return delegate.getBaseURLPolicyBuilder();
    }

    // Javadoc inherited.
    public String getBaseURL() {
        return delegate.getBaseURL();
    }

    // Javadoc inherited.
    public BaseLocation getBaseLocation() {
        return delegate.getBaseLocation();
    }
}
