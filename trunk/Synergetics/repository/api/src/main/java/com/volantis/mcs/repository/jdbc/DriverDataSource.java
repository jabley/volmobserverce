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
 * $Header: /src/voyager/com/volantis/mcs/repository/jdbc/DriverDataSource.java,v 1.6 2002/03/18 12:41:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 10-Aug-01    Paul            VBM:2001071607 - Added some debug logging.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.jdbc;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * A DataSource implementation that delegates to a DriverManager to do the
 * work.
 */
public class DriverDataSource implements DataSource {

    private static String mark = "(c) Volantis Systems Ltd 2000.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DriverDataSource.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    DriverDataSource.class);

    /**
     * The jdbc url that locates the repository
     */
    private String url;

    /**
     * Properties passed to the driver.
     */
    private Properties driverProperties;

    /**
     * Construct a DriverDataSource
     * @param driverClassName the class name of the jdbc driver to use
     * @param url the jdbc url
     * @param driverProperties the Properties associated with the DataSource
     * @throws RepositoryException if there is a problem loading the
     * jdbc driver.
     */
    public DriverDataSource(String driverClassName,
                            String url,
                            Properties driverProperties)

            throws RepositoryException {

        // Try and load the Driver class.
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException cnfe) {
            // Getting a CNFE might be OK if we as the driver may have been
            // preloaded on a different class loader.
            logger.warn("cannot-load-driver", new Object[]{cnfe});
            try {
                DriverManager.getDriver(url);
            } catch (SQLException sqle) {
                throw new MissingDriverRepositoryException(
                        EXCEPTION_LOCALIZER.format(
                                "cannot-load-driver", driverClassName), cnfe);
            }
        }

        this.url = url;
        this.driverProperties = driverProperties;
        logger.info("url-info", new Object[]{url});
    }

    /**
     * Construct a DriverDataSource that has no additional properties.
     * @param driverClassName the class name of the jdbc driver to use
     * @param url the jdbc url
     * @throws RepositoryException if there is a problem loading the
     * jdbc driver.
     */
    public DriverDataSource(String driverClassName, String url)
            throws RepositoryException {
        this(driverClassName, url, null);

    }

    /**
     * Construct a new DriverDatasource without a driver class. In order
     * for this DriverDataSource to work the driver class associated with
     * the given url will either have to have been already loaded or
     * specified in the jdbc.drivers system property (see DriverManager class
     * javadoc).
     * @param url the jdbc url
     */
    public DriverDataSource(String url) {
        this.url = url;
    }

    // javadoc inherited
    public Connection getConnection()
            throws SQLException {
        Connection connection = null;
        if (driverProperties != null) {
            connection = DriverManager.getConnection(url, driverProperties);
        } else {
            connection = DriverManager.getConnection(url);
        }

        return connection;
    }

    // javadoc inherited
    public Connection getConnection(String username, String password)
            throws SQLException {

        return DriverManager.getConnection(url, username, password);
    }

    // javadoc inherited
    public void setLogWriter(PrintWriter out) throws SQLException {

        DriverManager.setLogWriter(out);
    }

    // javadoc inherited
    public PrintWriter getLogWriter() throws SQLException {

        return DriverManager.getLogWriter();
    }

    // javadoc inherited
    public void setLoginTimeout(int seconds) throws SQLException {

        DriverManager.setLoginTimeout(seconds);
    }

    // javadoc inherited
    public int getLoginTimeout() throws SQLException {

        return DriverManager.getLoginTimeout();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 18-Apr-05	7692/1	allan	VBM:2005041504 SimpleDeviceRepositoryFactory - create a dev rep from a url

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 09-Mar-04	2867/4	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 19-Feb-04	2789/7	tony	VBM:2004012601 refactored localised logging to synergetics

 16-Feb-04	2789/5	tony	VBM:2004012601 localised logging services

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 29-Jan-04	2756/3	ianw	VBM:2004012601 More Localisation changes

 28-Jan-04	2756/1	ianw	VBM:2004012601 Start of Localisation of Message Files

 ===========================================================================
*/
