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
package com.volantis.mcs.runtime.configuration;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.util.Properties;
import java.util.Map;

/**
 * A simple fake JDBC driver for testing MCS configuration.
 */
public class FakeJDBCDriver implements Driver {
    static {
        try {
            // Register the JWDriver with DriverManager
            FakeJDBCDriver driver = new FakeJDBCDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException sqle) {
            throw new RuntimeException("Failure to initialise fake JDBC driver");
        }
    }

    // Javadoc inherited
    public Connection connect(String url, Properties info) throws SQLException {
        return acceptsURL(url) ? new FakeConnection() : null;
    }

    // Javadoc inherited
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith("jdbc:fake:");
    }

    // Javadoc inherited
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
            throws SQLException {
        return new DriverPropertyInfo[0];
    }

    // Javadoc inherited
    public int getMajorVersion() {
        return 7;
    }

    // Javadoc inherited
    public int getMinorVersion() {
        return 3;
    }

    // Javadoc inherited
    public boolean jdbcCompliant() {
        return true;
    }

    private class FakeConnection implements Connection {
        public Statement createStatement() throws SQLException {
            return null;
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return null;
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            return null;
        }

        public String nativeSQL(String sql) throws SQLException {
            return null;
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
        }

        public boolean getAutoCommit() throws SQLException {
            return false;
        }

        public void commit() throws SQLException {
        }

        public void rollback() throws SQLException {
        }

        public void close() throws SQLException {
        }

        public boolean isClosed() throws SQLException {
            return false;
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return null;
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
        }

        public boolean isReadOnly() throws SQLException {
            return false;
        }

        public void setCatalog(String catalog) throws SQLException {
        }

        public String getCatalog() throws SQLException {
            return null;
        }

        public void setTransactionIsolation(int level) throws SQLException {
        }

        public int getTransactionIsolation() throws SQLException {
            return 0;
        }

        public SQLWarning getWarnings() throws SQLException {
            return null;
        }

        public void clearWarnings() throws SQLException {
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return null;
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return null;
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return null;
        }

        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return null;
        }

        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        }

        public void setHoldability(int holdability) throws SQLException {
        }

        public int getHoldability() throws SQLException {
            return 0;
        }

        public Savepoint setSavepoint() throws SQLException {
            return null;
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            return null;
        }

        public void rollback(Savepoint savepoint) throws SQLException {
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return null;
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return null;
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return null;
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return null;
        }

        public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException {
            return null;
        }

        public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException {
            return null;
        }
    }
}
