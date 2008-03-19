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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.config;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.volantis.synergetics.factory.MetaFactory;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Connection direct through JDBC driver.
 *
 */
public class PureDriverConnectionStrategy implements ConnectionStrategy {

    /**
     * Connection string to database
     */
    private final String url;

    /**
     * Used for localized logger
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(PureDriverConnectionStrategy.class);

    /**
     * Constructor
     * @param driverClass JDBC driver class
     * @param databaseURL SQL database URL
     */
    public PureDriverConnectionStrategy(String driverClass, String databaseURL) {
        this.url = databaseURL;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Checking for JDBC driver class: " + driverClass);
        }
        new MetaFactory(driverClass, PureDriverConnectionStrategy.class.getClassLoader()).createInstance();
    }

    //  javadoc inherited
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    // javadoc inherited
    public DataSource getDataSource() {
        // we returning new data source anonymous implementation instance
        // which is using already initialized DriverManager
        return new DataSource() {

            // javadoc inherited
            public boolean isWrapperFor(Class iface) throws SQLException {
                throw new UnsupportedOperationException("isWrapperFor not implemented");
            }

            // javadoc inherited
            public Object unwrap(Class iface) throws SQLException {
                throw new UnsupportedOperationException("unwrap not implemented");
            }

            // javadoc inherited
            public int getLoginTimeout() throws SQLException {
                return DriverManager.getLoginTimeout();
            }

            // javadoc inherited
            public void setLoginTimeout(int timeout) throws SQLException {
                DriverManager.setLoginTimeout(timeout);
            }

            // javadoc inherited
            public PrintWriter getLogWriter() throws SQLException {
                return DriverManager.getLogWriter();
            }

            // javadoc inherited
            public void setLogWriter(PrintWriter printWriter)
                    throws SQLException {
                DriverManager.setLogWriter(printWriter);
            }

            // javadoc inherited
            public Connection getConnection() throws SQLException {
                return PureDriverConnectionStrategy.this.getConnection();
            }

            // javadoc inherited
            public Connection getConnection(String login, String password)
                    throws SQLException {
                return DriverManager.getConnection(url, login, password);
            }
            
        };
    }

}
