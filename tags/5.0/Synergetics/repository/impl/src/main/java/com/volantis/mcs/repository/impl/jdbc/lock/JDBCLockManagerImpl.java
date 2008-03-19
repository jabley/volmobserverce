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

package com.volantis.mcs.repository.impl.jdbc.lock;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.SQLState;
import com.volantis.mcs.repository.lock.Lock;
import com.volantis.mcs.repository.lock.LockException;
import com.volantis.synergetics.log.LogDispatcher;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link JDBCLockManager}.
 *
 * <p>Locks are stored as a row in a table. Each row has a column for the
 * project name, the resource identifier, the principal name and the
 * acquisition time. Acquiring a lock inserts a row in the table, releasing a
 * lock deletes the row from the table.</p>
 *
 * todo remove all dependencies on project in here, abstracting it away to some constant fields.
 */
public class JDBCLockManagerImpl
        implements JDBCLockManager {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JDBCLockManagerImpl.class);

    /**
     * The name of the column containing the project name.
     */
    private static final String PROJECT_COLUMN_NAME = "PROJECT";

    /**
     * The name of the column containing the resource identifier.
     */
    private static final String RESOURCE_ID_COLUMN_NAME = "RESOURCEID";

    /**
     * The name of the column containing the principal name.
     */
    private static final String PRINCIPAL_COLUMN_NAME = "PRINCIPAL";

    /**
     * The name of the column containing the acquisition time.
     */
    private static final String ACQUIRED_COLUMN_NAME = "ACQUIRED";

    /**
     * The name of the locks table.
     */
    private static final String TABLE_NAME = "VMPOLICYLOCKS";

    /**
     * The connection to the database.
     */
    private final Connection sqlConnection;

    /**
     * The statement to select all locks in a project.
     *
     * <p>The project parameter is set as part of its initialisation.</p>
     */
    private final PreparedStatement selectAllStatement;

    /**
     * The statement to select information about a single lock.
     *
     * <p>The project parameter is set as part of its initialisation.</p>
     */
    private final PreparedStatement selectLockStatement;

    /**
     * The statement to delete a lock owned by a principal.
     *
     * <p>The project parameter is set as part of its initialisation.</p>
     */
    private final PreparedStatement deleteLockStatement;

    /**
     * The statement to insert a lock row.
     *
     * <p>The project parameter is set as part of its initialisation.</p>
     */
    private final PreparedStatement insertLockStatement;

    /**
     * A map from resource identifier to lock.
     */
    private final Map locks;

    /**
     * Initialise.
     *
     * @param repository  The repository that contains the table of locks.
     * @param projectName The name of the project within the repository.
     */
    public JDBCLockManagerImpl(
            InternalJDBCRepository repository, String projectName) {

        locks = new HashMap();

        // Make sure that the connection is created.
        try {
            JDBCRepositoryConnection connection =
                    (JDBCRepositoryConnection) repository.connect();

            sqlConnection = connection.getConnection();

            // Turn auto commit off to enable atomic updates to be performed.
            sqlConnection.setAutoCommit(false);

            // Prepare all the statements.
            String sql;

            sql = "select " +
                    RESOURCE_ID_COLUMN_NAME + " , " +
                    PRINCIPAL_COLUMN_NAME + " , " +
                    ACQUIRED_COLUMN_NAME +
                    " from " + TABLE_NAME +
                    " where " + PROJECT_COLUMN_NAME + " = ?";
            selectAllStatement = sqlConnection.prepareStatement(sql);
            selectAllStatement.setString(1, projectName);

            sql = "select " +
                    PRINCIPAL_COLUMN_NAME + " , " +
                    ACQUIRED_COLUMN_NAME +
                    " from " + TABLE_NAME +
                    " where " + PROJECT_COLUMN_NAME + " = ? and " +
                    RESOURCE_ID_COLUMN_NAME + " = ?";
            selectLockStatement = sqlConnection.prepareStatement(sql);
            selectLockStatement.setString(1, projectName);

            sql = "insert into " + TABLE_NAME + " (" +
                    PROJECT_COLUMN_NAME + " , " +
                    RESOURCE_ID_COLUMN_NAME + " , " +
                    PRINCIPAL_COLUMN_NAME + " , " +
                    ACQUIRED_COLUMN_NAME + ") values (?,?,?,SYSDATE)";
            insertLockStatement = sqlConnection.prepareStatement(sql);
            insertLockStatement.setString(1, projectName);

            sql = "delete from " + TABLE_NAME + " where " +
                    PROJECT_COLUMN_NAME + " = ? and " +
                    RESOURCE_ID_COLUMN_NAME + " = ? and " +
                    PRINCIPAL_COLUMN_NAME + " = ?";
            deleteLockStatement = sqlConnection.prepareStatement(sql);
            deleteLockStatement.setString(1, projectName);

        } catch (SQLException e) {
            throw new LockException(e);
        } catch (RepositoryException e) {
            throw new LockException(e);
        }
    }

    // Javadoc inherited.
    public Principal getPrincipal(String name) {
        return new JDBCPrincipal(name);
    }

    // Javadoc inherited.
    public Collection getLocks() throws LockException {

        List locks = new ArrayList();
        try {
            ResultSet resultSet = selectAllStatement.executeQuery();
            while (resultSet.next()) {
                String resourceIdentifier = resultSet.getString(1);
                String principalName = resultSet.getString(2);
                Timestamp acquisitionTime = resultSet.getTimestamp(3);
                Principal principal = new JDBCPrincipal(principalName);
                Lock lock = updateLock(resourceIdentifier, principal,
                        acquisitionTime.getTime());
                locks.add(lock);
            }
        } catch (SQLException e) {
            throw new LockException(e);
        }

        // todo, what about the locks that are in the map but not in the database anymore.

        return locks;
    }

    /**
     * Update the lock identified by the resource and return it.
     *
     * <p>If a lock has already been requested for the resource identifier then
     * return it after updating it's state, otherwise create a new one.</p>
     *
     * @param resourceIdentifier The identifier of the resource to lock.
     * @param principal          The owner of the lock.
     * @param acquisitionTime    The time that the lock was acquired.
     * @return The updated lock.
     */
    private Lock updateLock(
            String resourceIdentifier, Principal principal,
            long acquisitionTime) {

        JDBCLock lock = (JDBCLock) locks.get(resourceIdentifier);
        if (lock == null) {
            lock = new JDBCLock(this, resourceIdentifier, principal,
                    acquisitionTime);
            locks.put(resourceIdentifier, lock);
        } else {
            lock.updateState(principal, acquisitionTime);
        }
        return lock;
    }

    // Javadoc inherited.
    public Lock getLock(String resourceIdentifier) {
        JDBCLock lock = (JDBCLock) locks.get(resourceIdentifier);

        // If a lock could not be found, then create one and associate it
        // with the resource.
        if (lock == null) {
            lock = new JDBCLock(this, resourceIdentifier, null, -1);
            locks.put(resourceIdentifier, lock);
        }

        // Update the internal state of the lock.
        lock.updateState();

        return lock;
    }

    // Javadoc inherited.
    public JDBCLockInfo acquireLock(
            String resourceIdentifier, final Principal principal)
            throws LockException {

        JDBCLockInfo lockInfo = null;
        try {
            // Initialise the lock statement.
            String principalName = principal.getName();
            insertLockStatement.setString(2, resourceIdentifier);
            insertLockStatement.setString(3, principalName);

            // Loop around until an owner has been determined.
            do {
                try {
                    // Attempt to create a new row for the lock.
                    insertLockStatement.executeUpdate();
                    insertLockStatement.getConnection().commit();
                } catch (SQLException e) {
                    String sqlState = e.getSQLState();

                    // If the row could not be created because of an identity
                    // constraint violation then the row already exists so
                    // try and get the information about the lock.
                    if (SQLState.INTEGRITY_CONSTRAINT_VIOLATION.matches(
                            sqlState)) {
                        // The row has been created by someone else.
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                    "Row has been created by someone else");
                        }
                    } else {
                        // Rethrow the exception.
                        throw e;
                    }
                }

                // Get the information about the lock, may be us, may be
                // another principal, or may be noone at all.
                lockInfo = queryLockInfo(resourceIdentifier);

            } while (lockInfo == null);

        } catch (SQLException e) {
            throw new LockException(e);
        }

        return lockInfo;
    }

    // Javadoc inherited.
    public boolean releaseLock(String resourceIdentifier, Principal principal) {

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to delete lock for " +
                        resourceIdentifier +
                        " owned by " +
                        principal.getName());
            }
            deleteLockStatement.setString(2, resourceIdentifier);
            deleteLockStatement.setString(3, principal.getName());
            int rows = deleteLockStatement.executeUpdate();
            deleteLockStatement.getConnection().commit();
            if (rows != 1) {
                // Lock was not deleted so it either did not exist, or was not
                // owned by us.
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new LockException(e);
        }
    }

    // Javadoc inherited.
    public JDBCLockInfo queryLockInfo(String resourceIdentifier) {
        JDBCLockInfo info = null;
        try {
            selectLockStatement.setString(2, resourceIdentifier);
            ResultSet resultSet = selectLockStatement.executeQuery();
            if (resultSet.next()) {
                String principalName = resultSet.getString(1);
                Principal principal = new JDBCPrincipal(principalName);
                Timestamp acquisitionTime = resultSet.getTimestamp(2);
                info = new JDBCLockInfo(principal, acquisitionTime.getTime());
            }
        } catch (SQLException e) {
            throw new LockException(e);
        }

        return info;
    }
}
