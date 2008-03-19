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

package com.volantis.mcs.accessors.common;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.objects.RepositoryObjectIdentity;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.compatibility.New2OldConverter;
import com.volantis.mcs.policies.compatibility.Old2NewConverter;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;

public class DependentAccessor {

    protected final PolicyBuilderAccessor policyAccessor;
    protected final Old2NewConverter old2New = new Old2NewConverter();
    protected final New2OldConverter new2Old = new New2OldConverter();
    protected final Project defaultProject;

    public DependentAccessor(
            PolicyBuilderAccessor containerAccessor,
            Project defaultProject) {
        this.policyAccessor = containerAccessor;
        this.defaultProject = defaultProject;
    }

    protected VariablePolicyBuilder getVariablePolicyBuilder(
            RepositoryConnection connection, final Project project,
            String name, boolean required) throws RepositoryException {

        final VariablePolicyBuilder policyBuilder = (VariablePolicyBuilder)
                policyAccessor.retrievePolicyBuilder(connection, project, name);

        if (required && policyBuilder == null) {
            throw new RepositoryException("Unable to retrieve policy: " +
                    name);
        }
        if (policyBuilder != null && policyBuilder.getName() == null) {
            throw new IllegalStateException(
                    "Name of the policy cannot be null.");
        }

        return policyBuilder;
    }

    protected Project getProject(final RepositoryObjectIdentity identity) {
        Project project = identity.getProject();
        return project == null ? defaultProject : project;
    }

    protected Project getProject(final RepositoryObject object) {
        Project project = object.getProject();
        return project == null ? defaultProject : project;
    }

    protected void checkConnection(RepositoryConnection connection) {
        if (!(connection instanceof LocalRepositoryConnection)) {
            throw new IllegalArgumentException(
                    "Connection must be for a local repository");
        }
    }
}
