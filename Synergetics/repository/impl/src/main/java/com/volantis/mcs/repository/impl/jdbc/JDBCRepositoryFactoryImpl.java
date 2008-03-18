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

package com.volantis.mcs.repository.impl.jdbc;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.DriverDataSource;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCDriverConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCDriverVendor;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.jdbc.MCSConnectionPoolConfiguration;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;
import com.volantis.mcs.repository.jdbc.VendorDataSource;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JDBCRepositoryFactoryImpl
        extends JDBCRepositoryFactory {

    private static final Map VENDOR_2_FACTORY;

    static {
        Map map = new HashMap();
        map.put(JDBCDriverVendor.ORACLE8, new Oracle8VendorFactory());
        map.put(JDBCDriverVendor.DB2, new DB2VendorFactory());
        map.put(JDBCDriverVendor.DB2MVS, new DB2MVSVendorFactory());
        map.put(JDBCDriverVendor.MSSQL_DATAD, new MSSQLDataDVendorFactory());
        map.put(JDBCDriverVendor.MSSQL_JSQL, new MSSQLJSQLVendorFactory());
        map.put(JDBCDriverVendor.MSSQL_MICROSOFT,
                new MSSQLMicrosoftVendorFactory());
        map.put(JDBCDriverVendor.MSSQL_2005, new MSSQL2005MicrosoftVendorFactory());
        map.put(JDBCDriverVendor.SYBASE, new SybaseVendorFactory());
        map.put(JDBCDriverVendor.POSTGRESQL, new PostgreSQLVendorFactory());
        map.put(JDBCDriverVendor.HYPERSONIC, new HypersonicVendorFactory());
        map.put(JDBCDriverVendor.MYSQL, new MySQLVendorFactory());
        map.put(JDBCDriverVendor.DERBY_SERVER, new DerbyVendorFactory());
        VENDOR_2_FACTORY = map;
    }

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    JDBCRepositoryFactoryImpl.class);

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to Oracle.
     */
    private static final String PRODUCT_ORACLE = "oracle";

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to DB2.
     */
    private static final String PRODUCT_DB2 = "db2";

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to HSQL Database Engine.
     */
    private static final String PRODUCT_HYPERSONIC = "HSQL Database Engine";

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to Microsoft SQL Server.
     */
    private static final String PRODUCT_MSSQL = "Microsoft SQL Server";

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to PostgreSQL.
     */
    private static final String PRODUCT_POSTGRES = "PostgreSQL";

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to MySQL.
     */
    private static final String PRODUCT_MYSQL = "MySQL";

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to Apache Derby.
     */
    private static final String PRODUCT_DERBY = "Apache Derby";

    /**
     * Constant value for the product name returned from
     * {@link java.sql.DatabaseMetaData#getDatabaseProductName()} when
     * connected to Sybase Adaptive Enterprise Server.
     */
    private static final String PRODUCT_SYBASE = "Adaptive Server Enterprise";

    public DataSource createJDBCDriverDataSource(
            JDBCDriverConfiguration configuration)
            throws RepositoryException {

        // If we find parameters then we place these parameters as key/value
        // pairs in the properties file. The idea is to provide a mechanism
        // for supplying JDBC configuration values using this map.
        // For example, if the key/value is 'user'/'volantis' then this value
        // would be used instead of any value for 'user' found in the config
        // file.
        final Properties driverProperties = configuration.getDriverProperties();

        final String driverClassName = configuration.getDriverClassName();
        final String url = configuration.getDriverSpecificDatabaseURL();
        DataSource dataSource = new DriverDataSource(driverClassName, url,
                driverProperties);

        // If it is configured create a connection pool around the data source,
        // otherwise just returns the dataSource.
        return createMCSConnectionPool(
                configuration.getConnectionPoolConfiguration(),
                dataSource);

    }

    public DataSource createMCSConnectionPool(
            MCSConnectionPoolConfiguration configuration,
            DataSource dataSource)
            throws RepositoryException {

        // Create and configure a Connection pool if there is one.
        if (configuration == null || !configuration.isEnabled()) {
            return dataSource;
        }

        int maxConnections = configuration.getMaxConnections();
        int maxFreeConnections = configuration.getMaxFreeConnections();
        //int optimalFreeConnections;
        int minFreeConnections = configuration.getMinFreeConnections();
        int initialConnections = configuration.getInitialConnections();

        boolean keepConnectionsAlive = configuration.isKeepAliveActive();
        ConnectionPool connectionPool = new ConnectionPool(dataSource);
        if (keepConnectionsAlive) {
            connectionPool.setKeepConnectionsAlive(true);
            int connectionPollInterval = configuration.getKeepAlivePollInterval();
            if (connectionPollInterval > 0) {
                connectionPool.setConnectionPollInterval(
                        connectionPollInterval);
            }
        }

        connectionPool.setMaxConnections(maxConnections);
        connectionPool.setMaxFreeConnections(maxFreeConnections);
        //connectionPool.setOptimalFreeConnections (optimalFreeConnections);
        connectionPool.setMinFreeConnections(minFreeConnections);
        connectionPool.setInitialConnections(initialConnections);

        connectionPool.start();

        return connectionPool;
    }

    public DataSource createMCSDriverDataSource(
            MCSDriverConfiguration configuration)
            throws JDBCRepositoryException, RepositoryException {

        // Get the vendor string.
        JDBCDriverVendor vendor = configuration.getDriverVendor();
        if (vendor == null) {
            throw new JDBCRepositoryException(
                    EXCEPTION_LOCALIZER.format("jdbc-missing-vendor"));
        }

        VendorFactory factory = (VendorFactory) VENDOR_2_FACTORY.get(vendor);
        if (factory == null) {
            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-unknown-vendor", vendor));
        }

        DataSource dataSource = factory.createDriverDataSource(configuration);

        return createMCSConnectionPool(
                configuration.getConnectionPoolConfiguration(),
                dataSource);
    }

    public MCSConnectionPoolConfiguration createMCSConnectionPoolConfiguration() {
        return new MCSConnectionPoolConfigurationImpl();
    }

    public JDBCDriverConfiguration createJDBCDriverConfiguration() {
        return new JDBCDriverConfigurationImpl();
    }

    public MCSDriverConfiguration createMCSDriverConfiguration() {
        return new MCSDriverConfigurationImpl();
    }

    public JDBCRepositoryConfiguration createJDBCRepositoryConfiguration() {
        return new JDBCRepositoryConfigurationImpl();
    }

    public LocalRepository createJDBCRepository(
            JDBCRepositoryConfiguration configuration)
            throws RepositoryException {

        JDBCDriverVendor vendor = configuration.getDriverVendor();

        DataSource dataSource = configuration.getDataSource();
        if (vendor == null) {
            vendor = getVendorFromDatabase(dataSource);
        }

        VendorFactory factory = (VendorFactory) VENDOR_2_FACTORY.get(vendor);
        if (factory == null) {
            throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                    "jdbc-unknown-vendor", vendor));
        }

        InternalJDBCRepository repository =
                factory.createRepository(
                        (InternalJDBCRepositoryConfiguration) configuration);

        JDBCRepositoryConnection connection = null;
        try {
            connection = (JDBCRepositoryConnection) repository.connect();
            connection.getConnection();

        } finally {
            connection.disconnect();
        }

        return repository;
    }

    private JDBCDriverVendor getVendorFromDatabase(DataSource dataSource)
            throws JDBCRepositoryException {

        JDBCDriverVendor vendor;
        if (dataSource instanceof VendorDataSource) {
            VendorDataSource vendorDataSource = (VendorDataSource) dataSource;
            vendor = vendorDataSource.getVendor();
        } else {
            String databaseProduct = null;
            Connection con = null;
            try {
                con = dataSource.getConnection();
                DatabaseMetaData metaData = con.getMetaData();
                databaseProduct = metaData.getDatabaseProductName();
                con.close();
            } catch (SQLException sqle) {
                throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                        "sql-exception"), sqle);
            }

            if (PRODUCT_ORACLE.equalsIgnoreCase(databaseProduct)) {
                vendor = JDBCDriverVendor.ORACLE8;
            } else if (databaseProduct != null && databaseProduct.regionMatches(
                            true, 0, PRODUCT_DB2, 0, PRODUCT_DB2.length())) {
                vendor = JDBCDriverVendor.DB2;
            } else if (PRODUCT_MSSQL.equalsIgnoreCase(databaseProduct)) {
                vendor = JDBCDriverVendor.MSSQL_JSQL;
            } else if (PRODUCT_SYBASE.equalsIgnoreCase(databaseProduct)) {
                vendor = JDBCDriverVendor.SYBASE;
            } else if (PRODUCT_POSTGRES.equalsIgnoreCase(databaseProduct)) {
                vendor = JDBCDriverVendor.POSTGRESQL;
            } else if (PRODUCT_HYPERSONIC.equalsIgnoreCase(databaseProduct)) {
                vendor = JDBCDriverVendor.HYPERSONIC;
            } else if (PRODUCT_MYSQL.equalsIgnoreCase(databaseProduct)) {
                vendor = JDBCDriverVendor.MYSQL;
            } else if (PRODUCT_DERBY.equalsIgnoreCase(databaseProduct)) {
                vendor = JDBCDriverVendor.DERBY_SERVER;
            } else {
                throw new JDBCRepositoryException(EXCEPTION_LOCALIZER.format(
                        "jdbc-unknown-vendor",
                        databaseProduct));
            }
        }
        return vendor;
    }

    public DataSource createAnonymousDataSource(
            DataSource dataSource, String username, String password) {
        return new AnonymousDataSource(dataSource, username, password);
    }

}
