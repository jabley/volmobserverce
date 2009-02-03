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
 *                              testcase for JDBCRepositoryConnectionImpl
 * 16-May-03    Allan           VBM:2003051303 - Restructured to use 
 *                              DataSourceStub. Added 
 *                              testCloseConnectionsRemovesConnection() and 
 *                              testGetConnectionAddsConnection(). Renamed 
 *                              testGetConnection() to 
 *                              testGetConnectionAccess(). 
 * 19-May-03    Allan           VBM:2003051303 - Removed suite() and main(). 
 *                              Removed MyJDBCRepository. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.impl.AbstractRepositoryConnectionTestAbstract;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.synergetics.testtools.stubs.DataSourceStub;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class unit test the JDBCRepositoryConnectionclass.
 */
public class JDBCRepositoryConnectionTestCase
        extends AbstractRepositoryConnectionTestAbstract {

    /**
     * This method tests the method getConnection() works as expected in terms
     * of the way the repository is accessed.
     * for the com.volantis.mcs.repository.jdbc.JDBCRepositoryConnectionImpl class.
     * @throws RepositoryException if there is a problem with getConnection().
     */
    public void testGetConnectionAccess()
            throws RepositoryException {

        /**
         * A MockDataSource that allows us to check the connection access
         * modes.
         */ 
        class MockDataSource extends DataSourceStub {
            private boolean connectedAnonymously;
            private boolean connectedUserPass;

            public boolean wasConnectedAnonymously() {
                return connectedAnonymously;
            }

            public boolean wasConnectedUserPass() {
                return connectedUserPass;
            }

            public Connection getConnection() throws SQLException {
                connectedAnonymously = true;
                return null;
            }

            public Connection getConnection(String s, String s1)
                    throws SQLException {
                connectedUserPass = true;
                return null;
            }
            
            public void reset() {
                connectedAnonymously = false;
                connectedUserPass = false;
            }
        }

        MockDataSource dataSource = new MockDataSource();

        InternalJDBCRepositoryConfiguration configuration =
                new JDBCRepositoryConfigurationImpl();
        configuration.setDataSource(dataSource);
        JDBCRepositoryImpl repository = new JDBCRepositoryImpl(configuration);

        JDBCRepositoryConnection connection = new
                JDBCRepositoryConnectionImpl(repository, false, "user",
                                         "password", true);
        connection.getConnection();

        assertTrue("Repository login should have been attempted using" +
                   "username and password", dataSource.wasConnectedUserPass());

        assertTrue("Repository login should have been attempted using" +
                   "username and password",
                   !dataSource.wasConnectedAnonymously());

        dataSource.reset();
        
        connection = new
                JDBCRepositoryConnectionImpl(repository, true, "user", "password", true);
        connection.getConnection();
        assertTrue("Repository login should have been attempted anonymously",
                   !dataSource.wasConnectedUserPass());
        assertTrue("Repository login should have been anonymously",
                   dataSource.wasConnectedAnonymously());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 10-Jun-03	356/1	allan	VBM:2003060907 Moved some common testtools into Synergetics

 ===========================================================================
*/
