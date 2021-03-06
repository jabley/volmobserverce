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

package com.volantis.mcs.cli;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.LocalPolicySource;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;

public class PolicyTransferAccessor
        extends TransferAccessor {

    private final PolicyBuilderAccessor accessor;
    private final Project project;

    public PolicyTransferAccessor(
            LocalRepositoryConnection connection, Project project) {
        super(connection);

        InternalProject internalProject = (InternalProject) project;
        LocalPolicySource source = (LocalPolicySource)
                internalProject.getPolicySource();
        accessor = source.getPolicyBuilderAccessor();
        this.project = project;
    }

    public RepositoryEnumeration enumeratePolicyNames(PolicyType policyType)
            throws RepositoryException {

        return accessor.enumeratePolicyBuilderNames(connection, project, policyType);
    }

    public Policy retrievePolicy(String name)
            throws RepositoryException {

        Policy policy = null;
        final PolicyBuilder policyBuilder =
                accessor.retrievePolicyBuilder(connection, project, name);
        if (policyBuilder != null) {
            policy = policyBuilder.getPolicy();
        }
        return policy;
    }

    public void updatePolicy(Policy policy) throws RepositoryException {
        accessor.updatePolicyBuilder(connection, project, policy.getPolicyBuilder());
    }

    public void addPolicy(Policy policy) throws RepositoryException {
        accessor.addPolicyBuilder(connection, project, policy.getPolicyBuilder());
    }
}
