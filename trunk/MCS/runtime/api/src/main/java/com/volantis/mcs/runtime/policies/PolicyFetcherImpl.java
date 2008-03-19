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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Implementation of {@link PolicyFetcher}.
 */
public class PolicyFetcherImpl
        implements PolicyFetcher {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PolicyFetcherImpl.class);

    /**
     * The accessor for retrieving the policy.
     */
    private final ActivatedPolicyRetriever retriever;

    /**
     * Initialise.
     *
     * @param retriever   See {@link #retriever}
     */
    public PolicyFetcherImpl(ActivatedPolicyRetriever retriever) {
        this.retriever = retriever;
    }

    // Javadoc inherited.
    public ActivatedPolicy fetchPolicy(RuntimePolicyReference reference) {

        RuntimeProject project = (RuntimeProject) reference.getProject();
        String name = reference.getName();
        PolicyType expectedPolicyType = reference.getExpectedPolicyType();

        ActivatedPolicy policy = null;
        try {
            // Look for the policy in the project.
            policy = retriever.retrievePolicy(project, name);

            // If a specific type was requested then make sure the one that was
            // retrieved matches it. If it does not just ignore it.
            if (policy != null) {
                PolicyType actualPolicyType = policy.getPolicyType();
                if (expectedPolicyType != null &&
                        actualPolicyType != expectedPolicyType) {

                    if (logger.isInfoEnabled()) {
                        logger.info("Expected " + expectedPolicyType +
                                " received " + actualPolicyType);
                    }

                    policy = null;
                }
            }

        } catch (RepositoryException e) {
            logger.warn("repository-exception", e);
            policy = null;
        }
        return policy;
    }
}
