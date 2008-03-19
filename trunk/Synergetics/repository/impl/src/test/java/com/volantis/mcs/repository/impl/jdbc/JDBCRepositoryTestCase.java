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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Nov-02    Adrian          VBM:2002103004 - Created this class as a
 *                              testcase for JDBCRepository
 * 16-May-03    Allan           VBM:2003051303 - Restructured to use 
 *                              DataSourceStub, createTestableRepository() and
 *                              extend AbstractRepositoryTestAbstract. Added
 *                              tests for terminate(), openConnection(),
 *                              closeConnection(), addConnection() and
 *                              removeConnection().
 * 19-May-03    Allan           VBM:2003051303 - Removed suite() and main(). 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.AbstractRepository;
import com.volantis.mcs.repository.AbstractRepositoryTestAbstract;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.MCSConnectionPoolConfiguration;
import com.volantis.mcs.repository.jdbc.AlternateNames;
import mock.java.sql.ConnectionMock;
import mock.javax.sql.DataSourceMock;

import java.util.Collection;
import java.util.Collections;

/**
 * This class unit test the JDBCRepositoryclass.
 */
public class JDBCRepositoryTestCase extends AbstractRepositoryTestAbstract {
    private DataSourceMock dataSourceMock;
    private ConnectionMock connectionMock;

    protected void setUp() throws Exception {
        super.setUp();

        dataSourceMock = new DataSourceMock("dataSourceMock", expectations);
        connectionMock = new ConnectionMock("connectionMock", expectations);
    }

    /**
     * Provide an AbstractRepository (in this case a JDBCRepositoryImpl) that
     * can be used to test with.
     * @return a testable JDBCRepositoryImpl.
     */ 
    protected AbstractRepository createTestableRepository() {
        InternalJDBCRepositoryConfiguration configuration =
                new JDBCRepositoryConfigurationImpl();
        configuration.setDataSource(dataSourceMock);
        return createRepository(configuration);
    }

    protected JDBCRepositoryImpl createRepository(
            InternalJDBCRepositoryConfiguration configuration) {
        return new JDBCRepositoryImpl(configuration);
    }

    /**
     * Test that the addConnection() method adds a connection to the 
     * connections collection.
     * @throws RepositoryException if there is a problem opening a
     * connection.
     */ 
    public void testAddConnection() throws RepositoryException {
        JDBCRepositoryImpl repository = (JDBCRepositoryImpl)
                createTestableRepository();
        Collection connections = repository.connections;
        connections.clear();
        repository.addConnection(repository.openConnection());
        assertEquals("The connections collection should contain 1 connection",
                     1, connections.size());
    }

    /**
     * Test that the getTableName() method returns its passed parameter.
     * @throws RepositoryException if there is a problem opening a
     * connection.
     */
    public void testGetTableName() throws RepositoryException {
        JDBCRepositoryImpl repository =
                (JDBCRepositoryImpl) createTestableRepository();

        AlternateNames names = new AlternateNames("NORMAL", "SHORT");

        String tableName = repository.getAppropriateName(names);
        assertEquals("The tablename is incorrect",
                     "NORMAL", tableName);
    }
    
    /**
     * Test that the removeConnection() method removes a connection from the 
     * connections collection.
     * @throws RepositoryException if there is a problem opening a
     * connection.
     */ 
    public void testRemoveConnection() throws RepositoryException {
        JDBCRepositoryImpl repository =
                (JDBCRepositoryImpl) createTestableRepository();
        Collection connections = repository.connections;
        connections.clear();
        RepositoryConnection connection = repository.openConnection();
        connections.add(connection);
        assertEquals("The connections collection should contain 1 connection",
                     1, connections.size());
        repository.removeConnection(connection);
        assertEquals("The connections collection should contain 0 connections",
                     0, connections.size());
    }    
    
    /**
     * Tests that the openConnection() method has no effect on the connections
     * collection.
     * @throws RepositoryException if there is a problem with openConnection().
     */
    public void testOpenConnectionNoConnections() throws RepositoryException {
        JDBCRepositoryImpl repository =
                (JDBCRepositoryImpl) createTestableRepository();

        Collection connections = repository.connections;
        connections.clear();
        repository.openConnection();
        assertEquals("openConnection() has added to connections",
                     0, connections.size());
    }

    /**
     * Test that the closeConnection() calls the connection closeConnection()
     * method
     * @throws RepositoryException if there is an unexpected problem with
     * closeConnection().
     */
    public void testCloseConnectionRemovesConnection() 
            throws RepositoryException {
        JDBCRepositoryImpl repository =
                (JDBCRepositoryImpl) createTestableRepository();

        final String exceptionMessage = "closed connection";

        JDBCRepositoryConnection connection =
                new JDBCRepositoryConnectionImpl(repository, true,
                                             "name", "password", true) {
                    public void closeConnection() throws RepositoryException {
                        throw new RepositoryException(exceptionMessage);
                    }
                };

        try {
            repository.closeConnection(connection);
            fail("JDBCRepositoryImpl.closeConnection() is not calling " +
                 "closeConnection() on the JDBCRepositoryConnectionImpl.");
        } catch (RepositoryException e) {
            if (!(e.getMessage().equals(exceptionMessage))) {
                throw e;
            }
            // Success.
        }
    }

    /**
     * Test that the disconnect() method calls closeConnection().
     * @throws RepositoryException If there is a problem with disconnect().
     */
    public void testDisconnectCallsCloseConnection()
            throws RepositoryException {

        JDBCRepositoryImpl repository = (JDBCRepositoryImpl)
                createTestableRepository();

        final String closeConnectionMessage = "closeConnection() called";
        JDBCRepositoryConnection connection =
                new JDBCRepositoryConnectionImpl(repository, false, "user",
                                             "password", true) {
                    public void closeConnection() throws RepositoryException {
                        throw new RepositoryException(closeConnectionMessage);
                    }
                };

        try {
            connection.disconnect();
            fail("The closeConnection() method is not called by " +
                 "disconnect().");
        } catch (RepositoryException e) {
            if (!closeConnectionMessage.equals(e.getMessage())) {
                throw e;
            }
        }
    }

    /**
     * Test that the closeConnection() method removes the connection being
     * closed from the JDBCRepositoryImpl connections collection.
     * @throws RepositoryException If closeConnection() fails.
     */
    public void testCloseConnectionsRemovesConnection()
            throws RepositoryException {

        dataSourceMock.expects.getConnection("user", "password")
                .returns(connectionMock).any();
        connectionMock.expects.close();

        JDBCRepositoryImpl repository = (JDBCRepositoryImpl)
                createTestableRepository();

        Collection connections = repository.connections;
        connections.clear();
        JDBCRepositoryConnection connection =
                new JDBCRepositoryConnectionImpl(repository, false, "user",
                                             "password", true);
        connection.getConnection();
        assertEquals("There should be one connection in the connections" +
                     "collection", 1, connections.size());
        connection.closeConnection();
        assertEquals("There should be no connections in the connections" +
                     "collection", 0, connections.size());
    }

    /**
     * Test that the getConnection() method adds a connection to the
     * JDCBRepository connections collection.
     * @throws RepositoryException If getConnection() fails.
     */
    public void testGetConnectionAddsConnection()
            throws RepositoryException {

        dataSourceMock.expects.getConnection("user", "password")
                .returns(connectionMock).any();

        JDBCRepositoryImpl repository = (JDBCRepositoryImpl)
                createTestableRepository();

        Collection connections = repository.connections;
        connections.clear();
        JDBCRepositoryConnection connection =
                new JDBCRepositoryConnectionImpl(repository, false, "user",
                                             "password", true);
        connection.getConnection();
        assertEquals("There should be one connection in the connections" +
                     "collection", 1, connections.size());

    }

    /**
     * Cause some connections to be added to the connections collection of
     * a given JDBCRepositoryImpl.
     * @param repository The JDBCRepositoryImpl.
     * @param noOfConnections The number of connections to add.
     * @throws RepositoryException if there is a problem with opening a
     * connection or terminating.
     */ 
    private void addSomeConnections(JDBCRepositoryImpl repository,
                                    int noOfConnections) 
            throws RepositoryException {
        
        for(int i=0; i<noOfConnections; i++) {
            JDBCRepositoryConnection connection = (JDBCRepositoryConnection)
                    repository.openConnection();
            connection.getConnection();
        }
    }
    
    /**
     * Test that terminate() removes all connections from the connections
     * collection.
     * @throws RepositoryException if there is a problem with opening a
     * connection or terminating.
     */
    public void testTerminateRemovesAllConnections() 
            throws RepositoryException {

        JDBCRepositoryImpl repository =
                (JDBCRepositoryImpl) createTestableRepository();
        Collection connections = repository.connections;
        connections.clear();

        int noOfConnections = 3;

        dataSourceMock.expects.getConnection(null, null)
                .returns(connectionMock).fixed(3);
        connectionMock.expects.close().fixed(3);

        addSomeConnections(repository, noOfConnections);

        assertEquals("There should be " + noOfConnections +
                     " connections in the connections " +
                     "collection", noOfConnections, connections.size());

        repository.terminate();

        assertEquals("Expected terminate() to remove all connections.",
                     0, connections.size());
    }

    /**
     * Test that createConnectionPool() no longer 
     * throws an exception if it is called with an anonymous login.
     */ 
    public void testCreateConnectionPool() throws RepositoryException {
        MCSConnectionPoolConfiguration configuration =
                new MCSConnectionPoolConfigurationImpl();
        configuration.setEnabled(true);

        dataSourceMock.expects.getConnection()
                .returns(connectionMock).fixed(2);
        connectionMock.expects.getAutoCommit().returns(false).fixed(2);
        connectionMock.expects.getCatalog().returns("catalog").fixed(2);
        connectionMock.expects.isReadOnly().returns(false).fixed(2);
        connectionMock.expects.getTransactionIsolation().returns(1).fixed(2);
        connectionMock.expects.getTypeMap().returns(Collections.EMPTY_MAP).fixed(2);

        JDBCRepositoryFactory factory =
                JDBCRepositoryFactory.getDefaultInstance();

        try {
            factory.createMCSConnectionPool(configuration, dataSourceMock);
        } catch (RepositoryException e) {
            fail("method has thrown an exception with anonymous datasource");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-May-04	4023/1	ianw	VBM:2004032302 Added support for short length tables

 16-Mar-04	2867/4	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 13-Feb-04	3007/2	doug	VBM:2004021103 Ensured the JDBCRepositoryImpl#unlock method does not throw an IllegaArgumentException

 10-Jun-03	356/1	allan	VBM:2003060907 Moved some common testtools into Synergetics

 ===========================================================================
*/
