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

import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.repository.RepositoryException;

import java.util.Collection;

/**
 * Provides facilities to manage policies within a project.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <p>Managers are not thread safe as they maintain state specific to a
 * particular operation so a separate instance must be created for each thread
 * that wants to access the policies. As long as they are using different
 * managers multiple threads can access policies in the same project, however
 * the behaviour is undefined if a thread attempts to modify the policies while
 * at the same time another thread is reading, or modifying the policies.</p>
 *
 * <p>The manager frees user code from having to know anything about the
 * underlying repository and connections and in particular having to write
 * code to manage and clean up connections in all possible situations. The
 * only case where user code will have to manage cleaning up is if it wants
 * to manage batch operations itself, rather than using the
 * {@link #performBatchOperation(BatchOperation, TransactionLevel)} method that
 * does it automatically.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 * @mock.generate 
 */
public interface PolicyManager {

    /**
     * Checks whether the underlying repository supports the specified
     * transaction level.
     *
     * @param level The required transaction level.
     * @return True if it supports it, false otherwise.
     */
    boolean supportsTransactionLevel(TransactionLevel level);

    /**
     * Begins a batch operation.
     *
     * <p>If this executes succesfully then the manager may own a system
     * resource that needs clearing up so it is absolutely imperative that
     * irrespective of what may happen that either the
     * {@link #endBatchOperation()} or {@link #abortBatchOperation()} is
     * called.</p>
     *
     * @param level The transaction level.
     * @throws RepositoryException If there was a problem beginning a batch
     *                             operation.
     * @see #performBatchOperation(BatchOperation, TransactionLevel)
     */
    void beginBatchOperation(TransactionLevel level)
            throws RepositoryException;

    /**
     * Ends a batch operation.
     *
     * <p>This must only be called if a previous call to
     * {@link #beginBatchOperation(TransactionLevel)} was successful.</p>
     *
     * <p>If the underlying repository supports transactions and the batch
     * operation requested transaction level of {@link TransactionLevel#FULL}
     * then this will ensure that all the changes made since the
     * {@link #beginBatchOperation(TransactionLevel)} method have been
     * persisted.</p>
     *
     * @throws RepositoryException If there was a problem ending the batch
     *                             operation.
     * @see #performBatchOperation(BatchOperation, TransactionLevel)
     */
    void endBatchOperation() throws RepositoryException;

    /**
     * Aborts a batch operation.
     *
     * <p>This must only be called if a previous call to
     * {@link #beginBatchOperation(TransactionLevel)} was successful.</p>
     *
     * <p>If the underlying repository supports transactions and the batch
     * operation requested transaction level of {@link TransactionLevel#FULL}
     * then this will ensure that all the changes made since the
     * {@link #beginBatchOperation(TransactionLevel)} method have been
     * discarded.</p>
     *
     * @throws RepositoryException If there was a problem aborting the batch
     *                             operation.
     * @see #performBatchOperation(BatchOperation, TransactionLevel)
     */
    void abortBatchOperation() throws RepositoryException;

    /**
     * Performs a batch operation.
     *
     * <p>This is the recommended way of performing a batch operation as it
     * ensures that any system resources that may be retrieved by the manager
     * are freed whatever the result of the batch operation.</p>
     *
     * <p>The supplied {@link BatchOperation} instance encapsulates the
     * operations that should be performed together. It is invoked after the
     * batch operation begins. If it returns false, or throws an exception and
     * the underlying repository supports transactions then the operations are
     * aborted, otherwise they are persisted.</p>
     *
     * @param operation The operation to perform.
     * @param level     The transaction level to use.
     * @throws RepositoryException If there was a problem with any of the
     *                             operations.
     */
    void performBatchOperation(
            BatchOperation operation, TransactionLevel level)
            throws RepositoryException;

    /**
     * Get a collection of policy names within the project.
     *
     * <p>The order of names within the returned collection is undefined and
     * may change from release to release. All names will start with a '/' and
     * are unique within the project.</p>
     *
     * @param policyType The policy type, if null then all policy names are
     *                   returned.
     * @return A collection of all the policy names within the project, if the
     *         project is empty then an empty collection is returned.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    Collection getPolicyNames(PolicyType policyType)
            throws RepositoryException;

    /**
     * Get the policy with the specified name.
     *
     * @param name The name of the policy within the project, must start with a
     *             '/' and have a valid policy extension, i.e.
     *             <code>mimg</code> for image policies.
     * @return The policy with the specified name, or null if it could not be
     *         found.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    Policy getPolicy(String name)
            throws RepositoryException;

    /**
     * Add the policy to the project.
     *
     * <p>The policy name must start with a '/' and have a valid policy
     * extension, i.e. <code>mimg</code> for image policies.</p>
     *
     * <p>This will fail if the policy already exists.</p>
     *
     * @param policy The policy to add.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    void addPolicy(Policy policy)
            throws RepositoryException;

    /**
     * Remove the policy.
     *
     * @param name The name of the policy within the project, must start with a
     *             '/' and have a valid policy extension, i.e.
     *             <code>mimg</code> for image policies.
     * @return True if the policy existed and was removed and false if it did
     *         not actually exist.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    boolean removePolicy(String name)
            throws RepositoryException;

    /**
     * Update an existing policy, or add a new one.
     *
     * <p>The policy name must start with a '/' and have a valid policy
     * extension, i.e. <code>mimg</code> for image policies.</p>
     *
     * <p>This will either update an existing policy, or add a new one if the
     * policy does not exist.</p>
     *
     * @param policy The policy to update.
     * @return True if the policy existed and was updated and false if it did
     *         not previously exist and was added.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    boolean updatePolicy(Policy policy)
            throws RepositoryException;
}
