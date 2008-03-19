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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.repository.impl.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A DataSource that looks like it is anonymous but is in fact the
 * opposite despite the name. Basically this class allows DataSource users
 * to call the getConnection() - no args - method but get a connection that
 * is obtained using a username and password (i.e. with the
 * getConnection(username, password) method. It does this simply by
 * storing username and password data passed in on construction and
 * delegating to an underlying DataSource.
 */
public class AnonymousDataSource
        implements DataSource {

    private DataSource dataSource;

    private String username;

    private String password;

    /**
     * This datasource encapsulates a datasource, supplying it with user and
     * password information.
     */
    public AnonymousDataSource(
            DataSource dataSource,
            String username,
            String password) {
        this.dataSource = dataSource;
        this.username = username;
        this.password = password;
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection(username, password);
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     */
    public Connection getConnection(String username, String password)
            throws SQLException {

        return dataSource.getConnection(username, password);
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getLogWriter()
     */
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getLoginTimeout()
     */
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);

    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#setLoginTimeout(int)
     */
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);

    }

    /**
     * Returns the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return username;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
