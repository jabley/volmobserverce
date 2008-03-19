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

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraints;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * Base for all {@link PolicyActivator}s.
 */
public abstract class AbstractPolicyActivator
        implements PolicyActivator {

    /**
     * The factory to use to create references.
     */
    protected final PolicyReferenceFactory referenceFactory;

    /**
     * Initialise.
     *
     * @param referenceFactory The reference factory.
     */
    protected AbstractPolicyActivator(PolicyReferenceFactory referenceFactory) {
        this.referenceFactory = referenceFactory;
    }

    // Javadoc inherited.
    public ActivatedPolicy activate(
            RuntimeProject actualProject, PolicyBuilder policyBuilder,
            RuntimeProject logicalProject) {

        // Activate the cache control
        PolicyType policyType = policyBuilder.getPolicyType();
        CacheControlConstraintsMap constraintsMap =
                logicalProject.getCacheControlConstraintsMap();
        CacheControlConstraints constraints = constraintsMap.getConstraints(
                policyType);

        // Get the cache control builder from the policy.
        CacheControlBuilder cacheControlBuilder =
                policyBuilder.getCacheControlBuilder();
        if (cacheControlBuilder == null) {
            // No builder was set so use a default builder.
            cacheControlBuilder = constraints.getDefaultCacheControl()
                    .getCacheControlBuilder();
            policyBuilder.setCacheControlBuilder(cacheControlBuilder);
        } else {
            constraints.applyConstraints(cacheControlBuilder);
        }

        return activateImpl(actualProject, policyBuilder, logicalProject);
    }

    /**
     * Internal activate method.
     */
    protected abstract ActivatedPolicy activateImpl(
            RuntimeProject actualProject, PolicyBuilder policyBuilder,
            RuntimeProject logicalProject);

    /**
     * Activate the reference by creating a normalized reference that has been
     * resolved against the base URL of the containing policy.
     *
     * @param logicalProject The project that owns the containing policy.
     * @param baseURL       The baseURL of the containing policy.
     * @param reference     The reference to activate.
     * @return The normalized reference.
     */
    protected PolicyReference activateReference(
            RuntimeProject logicalProject, final MarinerURL baseURL,
            PolicyReference reference) {

        // Create a normalized reference immediately.
        String name = reference.getName();
        return referenceFactory.createNormalizedReference(logicalProject,
                baseURL, name, reference.getExpectedPolicyType());
    }

    /**
     * Get the base URL for the policy.
     *
     * <p>Relative references to policies within the policy need to be
     * resolved against the location of the policy before they can be
     * retrieved. The policy name can be either absolute (if it is a remote
     * policy), or project relative (starts with a /). This method attempts to
     * create a project relative base URL if it can, otherwise leaves it
     * absolute.</p>
     *
     * @param actualProject The project containing the builder.
     * @param builder       The builder whose base URL is to be constructed.
     * @param logicalProject
     * @return An absolute base URL for the builder.
     */
    protected MarinerURL getBaseURL(
            RuntimeProject actualProject,
            PolicyBuilder builder,
            RuntimeProject logicalProject) {

        // If the policy is host relative (starts with a /) then leave it
        // as it is.
        String name = builder.getName();
        MarinerURL url;
        if (actualProject == logicalProject) {
            // The projects are the same so leave it as it.
            url = new MarinerURL(name);
        } else if (name.startsWith("/")) {
            // The policy name is project relative (starts with a /) so leave
            // it as it is.
            url = new MarinerURL(name);
        } else if (isGlobal(actualProject)) {
            throw new IllegalStateException("Actual project cannot be global");
        } else if (isGlobal(logicalProject)) {
            throw new IllegalStateException("Logical project cannot be global");
        } else {
            // Create a path relative to the actual project.
            String projectRelative = actualProject.makeProjectRelativePath(
                    new MarinerURL(name), true);
            url = new MarinerURL(projectRelative);
        }

        return url;
    }

    /**
     * Check to see whether the project is the remote global one.
     *
     * @param project The project to check.
     * @return True if it is, false otherwise.
     */
    private boolean isGlobal(RuntimeProject project) {
        return project.isRemote() && project.getContainsOrphans();
    }
}
