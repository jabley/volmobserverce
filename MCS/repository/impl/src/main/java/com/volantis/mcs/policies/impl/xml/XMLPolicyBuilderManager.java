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

package com.volantis.mcs.policies.impl.xml;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.AbstractPolicyBuilderManager;
import com.volantis.mcs.project.BuilderBatchOperation;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.TransactionLevel;
import com.volantis.mcs.repository.RepositoryException;

import java.util.Collection;

/**
 * Manages policies in an XML repository.
 *
 * <p>This does not create any connections as XML policy accessors do not use
 * connections any more so there is no need to create them.</p>
 */
public class XMLPolicyBuilderManager
        extends AbstractPolicyBuilderManager {

    public XMLPolicyBuilderManager(InternalProject project) {
        super(project);
    }

    public boolean supportsTransactionLevel(TransactionLevel level) {
        return level == TransactionLevel.NONE;
    }

    public void beginBatchOperation(TransactionLevel level) {
    }

    public void endBatchOperation() {
    }

    public void abortBatchOperation() {
    }

    public void performBatchOperation(
            BuilderBatchOperation operation, TransactionLevel level)
            throws RepositoryException {

        operation.perform(this);
    }

    public Collection getPolicyBuilderNames(PolicyType policyType)
            throws RepositoryException {
        return getPolicyBuilderNames(null, policyType);
    }

    public PolicyBuilder getPolicyBuilder(String name)
            throws RepositoryException {
        return accessor.retrievePolicyBuilder(null, project, name);
    }

    public void addPolicyBuilder(PolicyBuilder builder)
            throws RepositoryException {
        accessor.addPolicyBuilder(null, project, builder);
    }

    public boolean updatePolicyBuilder(PolicyBuilder builder)
            throws RepositoryException {
        return accessor.updatePolicyBuilder(null, project, builder);
    }

    public boolean removePolicyBuilder(String name)
            throws RepositoryException {

        return accessor.removePolicyBuilder(null, project, name);
    }
}
