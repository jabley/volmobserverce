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

package com.volantis.mcs.policies.impl.jdbc;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.AbstractPolicyBuilderManager;
import com.volantis.mcs.project.BuilderBatchOperation;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.TransactionLevel;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;

import java.util.Collection;
import java.util.List;

public class JDBCPolicyBuilderManager
        extends AbstractPolicyBuilderManager {

    private RepositoryConnection batchConnection;
    private TransactionLevel level;

    public JDBCPolicyBuilderManager(InternalProject project) {
        super(project);
    }

    public boolean supportsTransactionLevel(TransactionLevel level) {
        return true;
    }

    public void beginBatchOperation(TransactionLevel level)
            throws RepositoryException {

        if (level == null) {
            throw new IllegalArgumentException("level cannot be null");
        }
        if (this.level != null) {
            throw new IllegalStateException(
                    "Nested batch operations not supported");
        }

        boolean success = false;
        batchConnection = repository.connect();
        this.level = level;
        if (level == TransactionLevel.FULL) {
            try {
                batchConnection.beginOperationSet();
                success = true;
            } finally {
                if (!success) {
                    releaseBatchConnection();
                }
            }
        }
    }

    public void endBatchOperation()
            throws RepositoryException {

        if (level == null) {
            throw new IllegalStateException(
                    "Batch operations not begun");
        }

        try {
            if (level == TransactionLevel.FULL) {
                batchConnection.endOperationSet();
            }
        } finally {
            releaseBatchConnection();
        }
    }

    private void releaseBatchConnection()
            throws RepositoryException {

        try {
            batchConnection.disconnect();
        } finally {
            batchConnection = null;
            level = null;
        }
    }

    public void abortBatchOperation()
            throws RepositoryException {

        if (level == null) {
            throw new IllegalStateException(
                    "Batch operations not begun");
        }

        try {
            if (level == TransactionLevel.FULL) {
                batchConnection.abortOperationSet();
            }
        } finally {
            releaseBatchConnection();
        }
    }

    public void performBatchOperation(
            BuilderBatchOperation operation, TransactionLevel level)
            throws RepositoryException {

        beginBatchOperation(level);
        boolean success = false;
        try {
            success = operation.perform(this);
        } finally {
            if (success) {
                endBatchOperation();
            } else {
                abortBatchOperation();
            }
        }
    }

    public Collection getPolicyBuilderNames(PolicyType policyType)
            throws RepositoryException {

        RepositoryConnection connection = prepareForSingleOperation();
        List names;
        try {
            names = getPolicyBuilderNames(connection, policyType);
        } finally {
            disconnectSingleOperation(connection);
        }
        return names;
    }

    /**
     * Prepares the connection for a single operation.
     *
     * <p>If this is in the middle of a batch operation then use the connection
     * that was allocated for the batch operation. Otherwise, allocate a new
     * connection just for this operation.</p>
     *
     * @return The connection to use for this operation.
     */
    private RepositoryConnection prepareForSingleOperation()
            throws RepositoryException {

        if (batchConnection != null) {
            return batchConnection;
        } else {
            RepositoryConnection connection = repository.connect();
            connection.beginOperationSet();
            return connection;
        }
    }

    /**
     * Cleans up after a single operation.
     *
     * <p>If the single operation used the batch connection then there is
     * nothing to do as that will be needed for another operation. Otherwise,
     * disconnect the supplied connection.</p>
     *
     * @param connection The connection that was used for the single operation.
     */
    private void disconnectSingleOperation(RepositoryConnection connection)
            throws RepositoryException {

        if (connection != batchConnection) {
            try {
                connection.endOperationSet();
            } finally {
                connection.disconnect();
            }
        }
    }

    public PolicyBuilder getPolicyBuilder(String name)
            throws RepositoryException {

        RepositoryConnection connection = prepareForSingleOperation();
        try {
            return accessor.retrievePolicyBuilder(connection, project,
                    name);
        } finally {
            disconnectSingleOperation(connection);
        }
    }

    public void addPolicyBuilder(PolicyBuilder builder)
            throws RepositoryException {

        RepositoryConnection connection = prepareForSingleOperation();
        try {
            accessor.addPolicyBuilder(connection, project, builder);
        } finally {
            disconnectSingleOperation(connection);
        }
    }

    public boolean updatePolicyBuilder(PolicyBuilder builder)
            throws RepositoryException {

        RepositoryConnection connection = prepareForSingleOperation();
        try {
            return accessor.updatePolicyBuilder(connection, project,
                    builder);
        } finally {
            disconnectSingleOperation(connection);
        }
    }

    public boolean removePolicyBuilder(String name)
            throws RepositoryException {

        RepositoryConnection connection = prepareForSingleOperation();
        try {
            return accessor.removePolicyBuilder(connection, project, name);
        } finally {
            disconnectSingleOperation(connection);
        }
    }
}
