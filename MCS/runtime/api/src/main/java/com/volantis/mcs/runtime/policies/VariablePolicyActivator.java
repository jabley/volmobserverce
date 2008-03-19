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

import com.volantis.mcs.policies.InternalVariablePolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.Iterator;
import java.util.List;

public class VariablePolicyActivator
        extends AbstractConcretePolicyActivator {

    public VariablePolicyActivator(PolicyReferenceFactory referenceFactory) {
        super(referenceFactory);
    }

    protected ActivatedPolicy activateImpl(
            RuntimeProject actualProject, PolicyBuilder policyBuilder,
            RuntimeProject logicalProject) {

        VariablePolicyBuilder variablePolicyBuilder =
                (VariablePolicyBuilder) policyBuilder;

        // Ensure we prune any invalid variants before we start activating.
        // We must do it now as the activation process will trigger lots
        // of redundant(!) validation which will fail otherwise.
        // TODO: avoid rendundant validation and consequent...
        // TODO: avoid redundant logging of warnings
        InternalVariablePolicyBuilder internalVariablePolicyBuilder =
                (InternalVariablePolicyBuilder) variablePolicyBuilder;
        internalVariablePolicyBuilder.validateAndPrune();
        MarinerURL baseURL = getBaseURL(actualProject, policyBuilder,
                logicalProject);

        activateAlternateReferences(logicalProject, variablePolicyBuilder,
                baseURL);

        // Iterate over all the variantBuilders to activate any policy
        // references.
        List variantBuilders = variablePolicyBuilder.getVariantBuilders();
        for (Iterator i = variantBuilders.iterator(); i.hasNext();) {
            VariantBuilder builder = (VariantBuilder) i.next();
            ContentBuilder contentBuilder = builder.getContentBuilder();

            if (contentBuilder instanceof URLContentBuilder) {
                URLContentBuilder urlContentBuilder =
                        (URLContentBuilder) contentBuilder;
                PolicyReference reference =
                        urlContentBuilder.getBaseURLPolicyReference();
                if (reference != null) {
                    reference = activateReference(logicalProject, baseURL,
                            reference);
                    urlContentBuilder.setBaseURLPolicyReference(reference);
                }
            }
        }

        // Convert the builder back into a policy. This will trigger another
        // full validation of the policy with consequent redundant logging.
        ActivatedVariablePolicy activatedVariablePolicy =
                new ActivatedVariablePolicyImpl(
                        variablePolicyBuilder.getVariablePolicy(),
                        actualProject, logicalProject);

        return activatedVariablePolicy;
    }
}
