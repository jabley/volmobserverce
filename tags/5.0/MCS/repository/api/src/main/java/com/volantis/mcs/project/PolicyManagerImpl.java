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
package com.volantis.mcs.project;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilder;

import java.util.Collection;

/**
 * Default policy manager which simply delegates to a
 * {@link PolicyBuilderManager}.
 * <p>
 * Implementing it this way avoids having duplicate layers down into the
 * accessors and is a reasonable way to do it since policies are always derived
 * from policy builders anyway.
 */
public class PolicyManagerImpl implements PolicyManager {

    /**
     * The policy builder manager that this class delegates to.
     */
    private final PolicyBuilderManager policyBuilderManager;

    /**
     * Initialise.
     *
     * @param policyBuilderManager the policy builder manager to delegate to.
     */
    public PolicyManagerImpl(PolicyBuilderManager policyBuilderManager) {
        this.policyBuilderManager = policyBuilderManager;
    }

    // Javadoc inherited.
    public boolean supportsTransactionLevel(TransactionLevel level) {
        return policyBuilderManager.supportsTransactionLevel(level);
    }

    // Javadoc inherited.
    public void beginBatchOperation(TransactionLevel level)
            throws RepositoryException {
        policyBuilderManager.beginBatchOperation(level);
    }

    // Javadoc inherited.
    public void endBatchOperation() throws RepositoryException {
        policyBuilderManager.endBatchOperation();
    }

    // Javadoc inherited.
    public void abortBatchOperation() throws RepositoryException {
        policyBuilderManager.abortBatchOperation();
    }

    // Javadoc inherited.
    public void performBatchOperation(
            final BatchOperation operation, TransactionLevel level)
            throws RepositoryException {

        BuilderBatchOperation builderOperation = new BuilderBatchOperation() {
            public boolean perform(PolicyBuilderManager manager)
                    throws RepositoryException {
                return operation.perform(PolicyManagerImpl.this);
            }
        };

        policyBuilderManager.performBatchOperation(builderOperation, level);
    }

    // Javadoc inherited.
    public Collection getPolicyNames(PolicyType policyType)
            throws RepositoryException {
        return policyBuilderManager.getPolicyBuilderNames(policyType);
    }

    // Javadoc inherited.
    public Policy getPolicy(String name)
            throws RepositoryException {
        PolicyBuilder policy= policyBuilderManager.getPolicyBuilder(name);
        
        if (policy==null) {
            return null; 
        }
        else {
            return policy.getPolicy();
        }
    }

    // Javadoc inherited.
    public void addPolicy(Policy policy)
            throws RepositoryException {
        policyBuilderManager.addPolicyBuilder(policy.getPolicyBuilder());
    }

    // Javadoc inherited.
    public boolean removePolicy(String name)
            throws RepositoryException {
        return policyBuilderManager.removePolicyBuilder(name);
    }

    // Javadoc inherited.
    public boolean updatePolicy(Policy policy)
            throws RepositoryException {
        return policyBuilderManager.updatePolicyBuilder(policy.getPolicyBuilder());
    }

}
