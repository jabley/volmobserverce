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

import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.AbstractConcretePolicyActivator;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * Activates a {@link ButtonImagePolicyBuilder}.
 */
public class ButtonImagePolicyActivator
        extends AbstractConcretePolicyActivator {

    /**
     * Initialise.
     *
     * @param referenceFactory The factory for creating
     *                         {@link PolicyReference}s.
     */
    public ButtonImagePolicyActivator(PolicyReferenceFactory referenceFactory) {
        super(referenceFactory);
    }

    // Javadoc inherited.
    protected ActivatedPolicy activateImpl(
            RuntimeProject actualProject, PolicyBuilder policyBuilder,
            RuntimeProject logicalProject) {

        ButtonImagePolicyBuilder buttonBuilder =
                (ButtonImagePolicyBuilder) policyBuilder;

        MarinerURL baseURL = getBaseURL(actualProject, policyBuilder,
                logicalProject);

        PolicyReference reference;

        reference = buttonBuilder.getUpPolicy();
        reference = activateReference(logicalProject, baseURL, reference);
        buttonBuilder.setUpPolicy(reference);

        reference = buttonBuilder.getOverPolicy();
        reference = activateReference(logicalProject, baseURL, reference);
        buttonBuilder.setOverPolicy(reference);

        reference = buttonBuilder.getDownPolicy();
        reference = activateReference(logicalProject, baseURL, reference);
        buttonBuilder.setDownPolicy(reference);

        return new ActivatedButtonImagePolicyImpl(
                buttonBuilder.getButtonImagePolicy(), 
                actualProject, logicalProject);
    }
}
