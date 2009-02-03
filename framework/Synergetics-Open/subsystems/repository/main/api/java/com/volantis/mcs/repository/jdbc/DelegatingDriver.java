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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.repository.jdbc;

import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.util.Properties;

/**
 * A wrapper class which allows the JDBC driver to be loaded by a classloader
 * other than the current one.
 *
 * <p>IMPORTANT NOTE: This class must be kept in a location where it is visible
 * to JDBCRepositoryFactoryImpl (ie. within the same subsystem), as in order
 * to allow the driver to be loaded by a classloader not visible by the
 * factory, it is wrapped with this class - its only purpose for existing is
 * to be visible to the factory class.</p>
 */
public class DelegatingDriver implements Driver {

    /**
     * Driver to which all request should be delegated.
     */
    private Driver delegate;

    /**
     * Initialize a new instance using the given parameters.
     * @param d to which to delegate
     */
    public DelegatingDriver(Driver d) {
        this.delegate = d;
    }

    // Javadoc inherited.
    public int getMajorVersion() {
        return delegate.getMajorVersion();
    }

    // Javadoc inherited.
    public int getMinorVersion() {
        return delegate.getMinorVersion();
    }

    // Javadoc inherited.
    public boolean jdbcCompliant() {
        return delegate.jdbcCompliant();
    }

    // Javadoc inherited.
    public boolean acceptsURL(String url) throws SQLException {
        return delegate.acceptsURL(url);
    }

    // Javadoc inherited.
    public Connection connect(String url, Properties info)
            throws SQLException {
        return delegate.connect(url, info);
    }

    // Javadoc inherited.
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
            throws SQLException {
        return delegate.getPropertyInfo(url, info);
    }

    /**
     * Return the name of the driver to which this class delegates (as returned
     * by getClass().getName()).
     * @return String name of the driver class to which this class delegates
     */
    public String getDelegateClassName() {
        return delegate.getClass().getName();
    }
}