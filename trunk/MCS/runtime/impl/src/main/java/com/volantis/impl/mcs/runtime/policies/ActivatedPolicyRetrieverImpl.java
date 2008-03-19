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

package com.volantis.impl.mcs.runtime.policies;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.policies.PolicyBuilderResponse;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.ActivatedPolicy;
import com.volantis.mcs.runtime.policies.ActivatedPolicyRetriever;
import com.volantis.mcs.runtime.policies.PolicyActivator;
import com.volantis.mcs.runtime.project.ProjectManager;

/**
 * A {@link ActivatedPolicyRetriever} that simply activates the
 * {@link PolicyBuilder} retrieved from a {@link PolicyBuilderReader}.
 */
public class ActivatedPolicyRetrieverImpl
        implements ActivatedPolicyRetriever {

    /**
     * The activator.
     */
    private final PolicyActivator activator;

    /**
     * The project manager.
     */
    private final ProjectManager projectManager;

    /**
     * Initialise.
     *
     * @param activator      The activator.
     * @param projectManager The project manager.
     */
    public ActivatedPolicyRetrieverImpl(
            PolicyActivator activator,
            ProjectManager projectManager) {
        this.activator = activator;
        this.projectManager = projectManager;
    }

    // Javadoc inherited.
    public ActivatedPolicy retrievePolicy(RuntimeProject logicalProject,
                                          String name)
            throws RepositoryException {

        ActivatedPolicy policy = null;

        RuntimeProject actualProject = logicalProject;

        // Only fallback if the name is project relative.
        boolean supportFallback = name.startsWith("/");

        // Iterate over the project and its fallbacks.
        boolean finished = false;
        for (; !finished && policy == null && actualProject != null;
             actualProject = actualProject.getBaseProject()) {

            // This iteration is the last one if fallbacks are not being
            // performed.
            finished = !supportFallback;

            // Look for the policy in the actual project, if it is found
            // then it is treated as if it belonged to the logical project.
            PolicyBuilderReader reader = actualProject.getPolicyBuilderReader();

            // Try and get the response, may be null.
            PolicyBuilderResponse response =
                    reader.getPolicyBuilder(actualProject, name);
            if (response == null) {
                // No response from reader so try next one.
                continue;
            }

            // If the logical project is the global project then it
            // maybe that the policy is actually in a different project
            // but the request was deferred until now in order to allow
            // the project identification to be combined with retrieval
            // of the policy in order to save time on the server.
            RuntimeProject responseProject = (RuntimeProject)
                    response.getProject();
            if (responseProject != actualProject) {
                RuntimeProject global = projectManager.getGlobalProject();
                if (actualProject == global && logicalProject == global) {
                    actualProject = responseProject;
                    logicalProject = responseProject;
                } else {
                    // The project can change only if the policy is remote and
                    // the actual project is the global project.
                    throw new IllegalStateException(
                            "Response indicates policy is in " +
                            responseProject + " instead of " + actualProject +
                            " but either it, or " + logicalProject +
                            " is not the global project " + global);
                }
            }

            // Get the builder from the response, again may be null.
            PolicyBuilder policyBuilder = response.getBuilder();
            if (policyBuilder == null) {
                // No response from builder so try next one.
                continue;
            }

            // Activate the policy builder
            policy = activator.activate(actualProject, policyBuilder,
                    logicalProject);
        }

        return policy;
    }
}
