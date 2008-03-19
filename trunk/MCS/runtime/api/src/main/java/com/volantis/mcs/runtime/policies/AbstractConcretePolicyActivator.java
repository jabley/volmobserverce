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

import com.volantis.mcs.policies.ConcretePolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.List;
import java.util.ListIterator;

/**
 * Base for all activators of {@link ConcretePolicyBuilder}s.
 */
public abstract class AbstractConcretePolicyActivator
        extends AbstractPolicyActivator {

    /**
     * Initialise.
     *
     * @param referenceFactory The reference factory.
     */
    protected AbstractConcretePolicyActivator(
            PolicyReferenceFactory referenceFactory) {
        super(referenceFactory);
    }

    /**
     * Activate all the alternate references.
     *
     * @param logicalProject        The logical project that contains the
     *                              references.
     * @param concretePolicyBuilder The builder that contains the alternates.
     * @param baseURL               The base URL against which relative
     *                              references will be resolved.
     */
    protected void activateAlternateReferences(
            RuntimeProject logicalProject,
            ConcretePolicyBuilder concretePolicyBuilder,
            MarinerURL baseURL) {

        List alternates = concretePolicyBuilder.getAlternatePolicies();
        for (ListIterator i = alternates.listIterator(); i.hasNext();) {
            PolicyReference reference = (PolicyReference) i.next();

            reference = activateReference(logicalProject, baseURL, reference);
            i.set(reference);
        }
    }

}
