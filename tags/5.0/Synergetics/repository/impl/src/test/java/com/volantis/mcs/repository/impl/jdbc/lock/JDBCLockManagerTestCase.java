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

import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryMock;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnectionMock;
import com.volantis.mcs.repository.lock.Lock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.java.sql.ConnectionMock;
import mock.java.sql.PreparedStatementMock;
import mock.java.sql.ResultSetMock;
import mock.java.sql.SQLHelper;
import mock.java.sql.SQLMockType;

import java.security.Principal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Tests for {@link JDBCLockManagerImpl}.
 */
public class JDBCLockManagerTestCase
        extends TestCaseAbstract {

    private static final SQLMockType[] ALL_COLUMN_TYPES = new SQLMockType[]{
        SQLMockType.STRING,
        SQLMockType.STRING,
        SQLMockType.STRING,
        SQLMockType.TIMESTAMP,
    };

    private static final String[] ALL_COLUMN_NAMES = new String[]{
        "PROJECT",
        "RESOURCEID",
        "PRINCIPAL",
        "ACQUIRED"
    };

    private static final SQLMockType[] NO_PROJECT_COLUMN_TYPES = new SQLMockType[]{
        SQLMockType.STRING,
        SQLMockType.STRING,
        SQLMockType.TIMESTAMP,
    };

    private static final String[] NO_PROJECT_COLUMN_NAMES = new String[]{
        "RESOURCEID",
        "PRINCIPAL",
        "ACQUIRED"
    };

    private static final SQLMockType[] SINGLE_LOCK_COLUMN_TYPES = new SQLMockType[]{
        SQLMockType.STRING,
        SQLMockType.TIMESTAMP,
    };

    private static final String[] SINGLE_LOCK_COLUMN_NAMES = new String[]{
        "PRINCIPAL",
        "ACQUIRED"
    };

    private JDBCLockManagerImpl lockManager;
    private InternalJDBCRepositoryMock repositoryMock;
    private JDBCRepositoryConnectionMock jdbcConnectionMock;
    private ConnectionMock sqlConnectionMock;
    private PreparedStatementMock selectAllStatementMock;
    private PreparedStatementMock selectLockStatementMock;
    private PreparedStatementMock insertLockStatementMock;
    private PreparedStatementMock deleteLockStatementMock;
//    private AtomicStatementMock atomicStatementMock;
    private ResultSetMock resultSetMock;

    protected void setUp() throws Exception {
        super.setUp();

        repositoryMock = new InternalJDBCRepositoryMock("repositoryMock",
                expectations);

        jdbcConnectionMock = new JDBCRepositoryConnectionMock(
                "jdbcConnectionMock",
                expectations);

        sqlConnectionMock =
                new ConnectionMock("sqlConnectionMock", expectations);

        selectAllStatementMock = SQLHelper.createPreparedStatementMock(
                "selectAllStatementMock", expectations,
                sqlConnectionMock);

        selectLockStatementMock = SQLHelper.createPreparedStatementMock(
                "selectLockStatementMock", expectations,
                sqlConnectionMock);

        insertLockStatementMock = SQLHelper.createPreparedStatementMock(
                "insertLockStatementMock", expectations,
                sqlConnectionMock);

        deleteLockStatementMock = SQLHelper.createPreparedStatementMock(
                "deleteLockStatementMock", expectations,
                sqlConnectionMock);

//        atomicStatementMock =
//                new AtomicStatementMock("atomicStatementMock", expectations);

        // A JDBC Connection is created.
        repositoryMock.expects.connect().returns(jdbcConnectionMock);

        // The SQL connection is obtained from that and auto commit is
        // turned off.
        jdbcConnectionMock.expects.getConnection()
                .returns(sqlConnectionMock).any();
        sqlConnectionMock.expects.setAutoCommit(false);

        // Initialise the selectAllStatement.
        sqlConnectionMock.expects
                .prepareStatement("select RESOURCEID , PRINCIPAL , ACQUIRED" +
                " from VMPOLICYLOCKS" +
                " where PROJECT = ?")
                .returns(selectAllStatementMock);
        selectAllStatementMock.expects.setString(1, "Project");

        // Initialise the selectLockStatement.
        sqlConnectionMock.expects
                .prepareStatement("select PRINCIPAL , ACQUIRED" +
                " from VMPOLICYLOCKS" +
                " where " +
                "PROJECT = ? and RESOURCEID = ?")
                .returns(selectLockStatementMock);
        selectLockStatementMock.expects.setString(1, "Project");

        // Initialise the insertLockStatement.
        sqlConnectionMock.expects
                .prepareStatement("insert into VMPOLICYLOCKS " +
                "(PROJECT , RESOURCEID , PRINCIPAL , ACQUIRED) " +
                "values (?,?,?,SYSDATE)")
                .returns(insertLockStatementMock);
        insertLockStatementMock.expects.setString(1, "Project");


        // Initialise the deleteLockStatement.
        sqlConnectionMock.expects
                .prepareStatement("delete from VMPOLICYLOCKS where " +
                "PROJECT = ? and RESOURCEID = ? and PRINCIPAL = ?")
                .returns(deleteLockStatementMock);
        deleteLockStatementMock.expects.setString(1, "Project");

//        AtomicCommandBuilder builder = new AtomicCommandBuilder();
//        builder.setTable("VMPOLICYLOCKS");
//        builder.addColumn("PROJECT", SQLType.STRING, ColumnUsage.QUERY);
//        builder.addColumn("RESOURCEID", SQLType.STRING, ColumnUsage.QUERY);
//        builder.addColumn("PRINCIPAL", SQLType.STRING, ColumnUsage.UPDATE);
//        builder.addColumn("ACQUIRED", SQLType.TIMESTAMP, ColumnUsage.UPDATE);
//        AtomicCommand command = builder.getCommand();


//        jdbcConnectionMock.expects.createAtomicStatement(command)
//                .returns(atomicStatementMock);
//        atomicStatementMock.expects.setColumnValue("PROJECT", "Project");

        resultSetMock = new ResultSetMock("resultSetMock", expectations);

        lockManager = new JDBCLockManagerImpl(repositoryMock, "Project");
    }

    /**
     * Test that getting locks when there is nothing there works correctly.
     */
    public void testGetLocksNoLocks() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        selectAllStatementMock.expects.executeQuery().returns(resultSetMock);

        addResultSetExpectations(resultSetMock, new Object[0][]);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Collection collection = lockManager.getLocks();
        assertEquals(Collections.EMPTY_LIST, collection);
    }

    private void addResultSetExpectations(
            final ResultSetMock resultSetMock, final Object[][] rows) {

        SQLHelper.setExpectedResults(expectations, resultSetMock,
                ALL_COLUMN_TYPES,
                ALL_COLUMN_NAMES,
                rows);
    }

    /**
     * Test that getting locks correctly returns the locks in the database.
     */
    public void testGetLocks() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        selectAllStatementMock.expects.executeQuery().returns(resultSetMock);

        SQLHelper.setExpectedResults(expectations, resultSetMock,
                NO_PROJECT_COLUMN_TYPES, NO_PROJECT_COLUMN_NAMES,
                new Object[][]{
                    new Object[]{
                        "resource1", "fred", new Timestamp(1000)
                    },
                    new Object[]{
                        "resource2", "barney", new Timestamp(2000)
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Collection collection = lockManager.getLocks();
        assertEquals(2, collection.size());
        Lock lock;
        Iterator iterator = collection.iterator();
        lock = (Lock) iterator.next();
        checkLock(lock, "Lock 1", "resource1", "fred", 1000);
        lock = (Lock) iterator.next();
        checkLock(lock, "Lock 2", "resource2", "barney", 2000);
    }

    private void checkLock(
            Lock lock, final String message,
            final String expectedResourceIdentifier,
            final String expectedPrincipalName,
            final int expectedAcquisitionTime) {
        assertEquals(message +
                " - Resource", expectedResourceIdentifier,
                lock.getResourceIdentifier());
        assertEquals(message +
                " - Principal", expectedPrincipalName,
                lock.getOwner().getName());
        assertEquals(message +
                " - Acquisition Time", expectedAcquisitionTime,
                lock.getAcquisitionTime());
    }

    /**
     * Test that getting a lock twice returns the same object.
     */
    public void testGetLockAgain() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Each attempt to get the lock will query the database by updating
        // the lock state.
        for (int i = 0; i < 2; i += 1) {
            selectLockStatementMock.expects.setString(2, "fred");
            selectLockStatementMock.expects.executeQuery()
                    .returns(resultSetMock);

            addResultSetExpectations(resultSetMock, new Object[0][]);
        }

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Lock lock1 = lockManager.getLock("fred");
        assertNotNull(lock1);
        Lock lock2 = lockManager.getLock("fred");
        assertNotNull(lock2);
        assertSame(lock1, lock2);
    }

    /**
     * Test that acquiring a lock that is not already owned uses the
     * AtomicStatement correctly.
     */
    public void testAcquireLock() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

//        final AtomicUpdateMock atomicUpdateMock =
//                new AtomicUpdateMock("atomicUpdateMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Attempting to insert the row for the lock worked, 1 row was updated.
        insertLockStatementMock.expects.setString(2, "resource1");
        insertLockStatementMock.expects.setString(3, "fred");
        insertLockStatementMock.expects.executeUpdate().returns(1);
        sqlConnectionMock.expects.commit();

        // When executed the atomic statement will report that the lock is
        // not currently owned. This will cause the LockAction to attempt
        // the update to take ownership.
//        atomicStatementMock.expects
//                .executeUpdate(
//                        mockFactory.expectsInstanceOf(AtomicAction.class))
//                .does(new InvokeAtomicAction(atomicUpdateMock, null));

        // The update was successful so the lock was acquired successfully.
//        atomicUpdateMock.expects.executeUpdate().returns(1);

        // The owner and acquisition time of the lock will be retrieved.
        selectLockStatementMock.expects.setString(2, "resource1");
        selectLockStatementMock.expects.executeQuery()
                .returns(resultSetMock);

        SQLHelper.setExpectedResults(expectations, resultSetMock,
                SINGLE_LOCK_COLUMN_TYPES, SINGLE_LOCK_COLUMN_NAMES,
                new Object[][]{
                    new Object[]{
                        "fred", new Timestamp(1234L)
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Acquiring the lock for principal fred should work.
        Principal fred = lockManager.getPrincipal("fred");
        JDBCLockInfo lockInfo = lockManager.acquireLock("resource1", fred);
        assertEquals("Owner", fred, lockInfo.getOwner());
        assertEquals("Acquisition Time", 1234L, lockInfo.getAcquisitionTime());
    }

    /**
     * Test that acquiring a lock that is already owned returns the information
     * about the previous owner.
     */
    public void testAcquireOwnedLock() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

//        final AtomicUpdateMock atomicUpdateMock =
//                new AtomicUpdateMock("atomicUpdateMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Attempting to insert the row for the lock failed with an identity
        // constraint violation.
        insertLockStatementMock.expects.setString(2, "resource1");
        insertLockStatementMock.expects.setString(3, "fred");
        insertLockStatementMock.expects.executeUpdate().returns(1);
        sqlConnectionMock.expects.commit().fails(new SQLException(
                "Identity constraint violation", "23010"));

//        atomicStatementMock.expects.setColumnValue("RESOURCEID", "resource1");
//        atomicStatementMock.expects.setColumnValue("PRINCIPAL", "fred");

        // When executed the atomic statement will report that the lock is
        // currently owned by wilma. This will cause the LockAction to report
        // that wilma is the owner.
//        atomicStatementMock.expects
//                .executeUpdate(
//                        mockFactory.expectsInstanceOf(AtomicAction.class))
//                .does(new InvokeAtomicAction(atomicUpdateMock, "wilma"));

//        insertLockStatementMock.expects.executeUpdate().returns(1);

        // The owner and acquisition time will be retrieved.
        selectLockStatementMock.expects.setString(2, "resource1");
        selectLockStatementMock.expects.executeQuery()
                .returns(resultSetMock);

        SQLHelper.setExpectedResults(expectations, resultSetMock,
                SINGLE_LOCK_COLUMN_TYPES, SINGLE_LOCK_COLUMN_NAMES,
                new Object[][]{
                    new Object[]{
                        "wilma", new Timestamp(23456789L)
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Acquiring the lock for principal fred should fail as wilma is the
        // owner.
        Principal fred = lockManager.getPrincipal("fred");
        Principal wilma = lockManager.getPrincipal("wilma");
        JDBCLockInfo lockInfo = lockManager.acquireLock("resource1", fred);
        assertEquals("Owner", wilma, lockInfo.getOwner());
        assertEquals("Acquisition Time", 23456789L,
                lockInfo.getAcquisitionTime());
    }

    /**
     * Test that acquiring a lock that is initially owned, but is released
     * between the attempt to insert the row and the select to find the
     * owner retries.
     */
    public void testAcquireRetries() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ResultSetMock resultSet2Mock =
                new ResultSetMock("resultSet2Mock", expectations);
//        final AtomicUpdateMock atomicUpdateMock =
//                new AtomicUpdateMock("atomicUpdateMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        insertLockStatementMock.expects.setString(2, "resource1");
        insertLockStatementMock.expects.setString(3, "fred");
        selectLockStatementMock.expects.setString(2, "resource1").atLeast(1);

        // Attempting to insert the row for the lock failed with an
        // identity constraint violation. This means that someone else
        // owns the lock so read the lock information.
        insertLockStatementMock.expects.executeUpdate().returns(1);
        sqlConnectionMock.expects.commit().fails(new SQLException(
                "Identity constraint violation", "23010"));

        // The lock row is deleted before the query.
        selectLockStatementMock.expects.executeQuery()
                .returns(resultSetMock);
        SQLHelper.setExpectedResults(expectations, resultSetMock,
                SINGLE_LOCK_COLUMN_TYPES, SINGLE_LOCK_COLUMN_NAMES,
                new Object[0][]);

        // Retry to insert the row for the lock succeeded.
        insertLockStatementMock.expects.executeUpdate().returns(1);
        sqlConnectionMock.expects.commit();

        // The lock row contains the requesting principal.
        selectLockStatementMock.expects.executeQuery()
                .returns(resultSet2Mock);

        SQLHelper.setExpectedResults(expectations, resultSet2Mock,
                SINGLE_LOCK_COLUMN_TYPES, SINGLE_LOCK_COLUMN_NAMES,
                new Object[][]{
                    new Object[]{
                        "fred", new Timestamp(334L)
                    }
                });

//        atomicStatementMock.expects.setColumnValue("RESOURCEID", "resource1");
//        atomicStatementMock.expects.setColumnValue("PRINCIPAL", "fred");

        // When executed the atomic statement will report that the lock is
        // not currently owned. This will cause the LockAction to attempt
        // the update to take ownership.
//        atomicStatementMock.expects
//                .executeUpdate(
//                        mockFactory.expectsInstanceOf(AtomicAction.class))
//                .does(new InvokeAtomicAction(atomicUpdateMock, null));

        // The initial update was unsuccessful because someone else made the
        // change first. This should cause the manager to try again.
//        atomicUpdateMock.expects.executeUpdate().returns(0);

        // When executed the atomic statement will report that the lock is
        // currently owned by wilma. This will cause the LockAction to report
        // that wilma is the owner.
//        atomicStatementMock.expects
//                .executeUpdate(
//                        mockFactory.expectsInstanceOf(AtomicAction.class))
//                .does(new InvokeAtomicAction(atomicUpdateMock, "wilma"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Acquiring the lock for principal fred should fail as although
        // initially she is not wilma is eventually the owner.
        Principal fred = lockManager.getPrincipal("fred");
//        Principal wilma = lockManager.getPrincipal("wilma");

        JDBCLockInfo lockInfo = lockManager.acquireLock("resource1", fred);
        assertEquals("Owner", fred, lockInfo.getOwner());
        assertEquals("Acquisition Time", 334L,
                lockInfo.getAcquisitionTime());
    }

//    /**
//     * A {@link MethodAction} that invokes an {@link AtomicAction} passing in
//     * a Map representing the contents of the result set.
//     */
//    private static class InvokeAtomicAction
//            implements MethodAction {
//
//        /**
//         * The mock {@link AtomicUpdate} that is passed into the
//         * {@link AtomicAction#perform(AtomicUpdate, java.util.Map)} method.
//         */
//        private final AtomicUpdateMock atomicUpdateMock;
//
//        /**
//         * The name of the principal found by the query. If this is null then
//         * the lock is not owned.
//         */
//        private final String principalName;
//
//        /**
//         * Initialise.
//         *
//         * @param atomicUpdateMock The mock {@link AtomicUpdate} that is passed
//         *                         into the {@link AtomicAction#perform}
//         *                         method.
//         * @param principalName    The name of the principal found by the query.
//         *                         If this is null then the lock is not owned.
//         */
//        public InvokeAtomicAction(
//                AtomicUpdateMock atomicUpdateMock,
//                final String principalName) {
//
//            this.atomicUpdateMock = atomicUpdateMock;
//            this.principalName = principalName;
//        }
//
//        // Javadoc inherited.
//        public Object perform(MethodActionEvent event) throws Throwable {
//            AtomicAction action = (AtomicAction)
//                    event.getArgument(AtomicAction.class);
//
//            // Populate the map containing the result set.
//            Map map = new HashMap();
//            map.put("PROJECT", "Project");
//            map.put("RESOURCEID", "resource1");
//            map.put("PRINCIPAL", principalName);
//
//            // Invoke the AtomicAction and return the value back to the caller.
//            int rows = action.perform(atomicUpdateMock, map);
//            return new Integer(rows);
//        }
//    }
}
