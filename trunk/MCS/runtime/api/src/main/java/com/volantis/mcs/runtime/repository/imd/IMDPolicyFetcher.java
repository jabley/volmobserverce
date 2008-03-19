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

package com.volantis.mcs.runtime.repository.imd;

import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.PolicyActivator;
import com.volantis.mcs.runtime.policies.PolicyFetcher;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link PolicyFetcher} that wraps another so that it can look for inline
 * policies before delegating to the other fetcher.
 */
public class IMDPolicyFetcher
        implements PolicyFetcher {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(IMDPolicyFetcher.class);

    /**
     * The fetcher to which this delegates if it cannot find an inline policy.
     */
    private final PolicyFetcher delegate;

    /**
     * The policy activator.
     */
    private final PolicyActivator activator;

    /**
     * The provider of the current project.
     */
    private final CurrentProjectProvider projectProvider;

    /**
     * The repository.
     */
    private final Map repository;

    /**
     * Initialise.
     *
     * @param delegate        The fetcher to which this delegates if it cannot
     *                        find an inline policy.
     * @param activator       The policy activator.
     * @param projectProvider The provider of the current project.
     */
    public IMDPolicyFetcher(
            PolicyFetcher delegate, PolicyActivator activator,
            CurrentProjectProvider projectProvider) {
        this.delegate = delegate;
        this.activator = activator;
        this.projectProvider = projectProvider;
        repository = new HashMap();
    }

    // Javadoc inherited.
    public ActivatedPolicy fetchPolicy(RuntimePolicyReference reference) {

        // Look in the page repository first.
        String name = reference.getName();
        ActivatedPolicy policy = getActivatedInlinePolicy(name);

        // If the policy was not found, either because it was not there or was
        // not searched for then delegate to the normal fetcher.
        if (policy == null) {
            policy = delegate.fetchPolicy(reference);
        }

        return policy;
    }

    /**
     * Activate and add the specified builder to the set of inline policies.
     *
     * @param builder The builder to add.
     */
    public void addInlinePolicyBuilder(PolicyBuilder builder) {
        String name = builder.getName();
        if (logger.isDebugEnabled()) {
            logger.debug("Adding to the map: " + name + " builder " + builder);
        }

        RuntimeProject currentProject = projectProvider.getCurrentProject();
        ActivatedPolicy activatedPolicy = (ActivatedPolicy)
                activator.activate(currentProject, builder, currentProject);

        repository.put(name, activatedPolicy);
    }

    /**
     * Get the activated inline policy with the specified name.
     *
     * @param name The name of the policy.
     * @return The activated inline policy, or null if it could not be found.
     */
    private ActivatedPolicy getActivatedInlinePolicy(String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("Looking for " + name + " in the map");
        }
        return (ActivatedPolicy) repository.get(name);
    }
}
