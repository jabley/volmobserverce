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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.repository.jdbc.JDBCRepository;
import com.volantis.mcs.repository.RepositoryException;

import javax.sql.DataSource;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.PrintWriter;

/**
 * This class is a DataSource but also has a properties map (names and values)
 * that may be used to modify the behaviour of the DataSource.
 */
public class PropertiesDriverDataSource
    implements DataSource {

    /**
     * The DataSource delegate object.
     */
    private DataSource delegateDataSource;

    /**
     * The map of names and value properties.
     */
    private Map properties;

    /**
     * Create a <code>PropertiesDataSource</code> object with an associated
     * delegate <code>DataSource</code> object and name/value properties.
     *
     * @param  delegate            the delegate <code>DataSource</code> object.
     * @param  properties          the properties map for names and their
     *                             values.
     * @throws RepositoryException if a Repository exception occurs.
     */
    public PropertiesDriverDataSource(DataSource delegate,
                                      Map properties)
        throws RepositoryException {

        if (delegate == null) {
            throw new IllegalArgumentException("DriverDataSource parameter" +
                                               "may not be null.");
        }
        delegateDataSource = delegate;
        this.properties = properties;
    }

    // javadoc inherited.
    public Connection getConnection() throws SQLException {
        String username = (String)properties.get(JDBCRepository.USERNAME_PROPERTY);
        String password = (String)properties.get(JDBCRepository.PASSWORD_PROPERTY);


        if (username != null && password != null) {
            return getConnection(username, password);
        }
        // Assume the user wishes to connect anonymously (with a username
        // or password).
        return delegateDataSource.getConnection();
    }

    // javadoc inherited.
    public Connection getConnection(String username, String password) throws SQLException {
        return delegateDataSource.getConnection(username, password);
    }

    // javadoc inherited.
    public int getLoginTimeout() throws SQLException {
        return delegateDataSource.getLoginTimeout();
    }

    // javadoc inherited.
    public PrintWriter getLogWriter() throws SQLException {
        return delegateDataSource.getLogWriter();
    }

    // javadoc inherited.
    public void setLoginTimeout(int seconds) throws SQLException {
        delegateDataSource.setLoginTimeout(seconds);
    }

    // javadoc inherited.
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        delegateDataSource.setLogWriter(printWriter);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 27-Jun-03	586/1	byron	VBM:2003062704 Username and password are not used if no connection pooling in mariner-config.xml

 ===========================================================================
*/
