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

package com.volantis.mcs.repository.jdbc;

import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.factory.MetaDefaultFactory;

import javax.sql.DataSource;

/**
 * Factory for creating JDBC Repository related objects.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User specializations of this class are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public abstract class JDBCRepositoryFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.mcs.repository.impl.jdbc.JDBCRepositoryFactoryImpl",
                        JDBCRepositoryFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static JDBCRepositoryFactory getDefaultInstance() {
        return (JDBCRepositoryFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    protected JDBCRepositoryFactory() {
    }

    /**
     * Create a DataSource object that wraps an arbitrary JDBC Driver.
     *
     * <p>Note - MCS is not guaranteed to work with any drivers other than
     * those that it explicitly supports, see {@link JDBCDriverVendor}.</p>
     *
     * @param configuration the configuration for the driver.
     * @return a valid DataSource object
     * @throws RepositoryException if there was a problem creating the data
     *                             source.
     */
    public abstract DataSource createJDBCDriverDataSource(
            JDBCDriverConfiguration configuration)
            throws RepositoryException;

    /**
     * Create an MCS connection pool that wraps another DataSource.
     *
     * <p>Note - an MCS connection pool will not be as effective as a pooled
     * data source provided by the JDBC driver vendor and should therefore only
     * be used if no pooled data source is provided.</p>
     *
     * @param configuration the connection pool configuration.
     * @param dataSource    The DataSource that will be wrapped by the
     *                      connection pool.
     * @return The pooled DataSource.
     * @throws RepositoryException if there was a problem creating the
     *                             connection pool.
     */
    public abstract DataSource createMCSConnectionPool(
            MCSConnectionPoolConfiguration configuration,
            DataSource dataSource)
            throws RepositoryException;

    /**
     * Create a DataSource object that wraps an MCS supported JDBC driver.
     *
     * @param configuration the configuration
     * @return a valid DataSource object
     * @throws RepositoryException if there was a problem creating the data
     *                             source.
     */
    public abstract DataSource createMCSDriverDataSource(
            MCSDriverConfiguration configuration)
            throws RepositoryException;

    /**
     * Create an instance to encapsulate the MCS connection pool configuration.
     *
     * @return A new {@link MCSConnectionPoolConfiguration} instance.
     */
    public abstract MCSConnectionPoolConfiguration createMCSConnectionPoolConfiguration();

    /**
     * Create an object to encapsulate the JDBC driver configuration.
     *
     * @return A new {@link JDBCDriverConfiguration} instance.
     */
    public abstract JDBCDriverConfiguration createJDBCDriverConfiguration();

    /**
     * Create an object to encapsulate the MCS driver configuration.
     *
     * @return A new {@link MCSDriverConfiguration} instance.
     */
    public abstract MCSDriverConfiguration createMCSDriverConfiguration();

    /**
     * Create an object to encapsulate the JDBC repository configuration.
     *
     * @return A new {@link JDBCRepositoryConfiguration} instance.
     */
    public abstract JDBCRepositoryConfiguration createJDBCRepositoryConfiguration();

    /**
     * Create a JDBC repository with the specified configuration.
     *
     * @param configuration contains the repository configuration.
     * @return a newly instantiated LocalRepository configured as specified.
     * @throws RepositoryException if an error occurs whilst creating the
     *                             repository.
     */
    public abstract LocalRepository createJDBCRepository(
            JDBCRepositoryConfiguration configuration)
            throws RepositoryException;

    /**
     * Create an anonymous datasource, one which implicitly knows its own
     * username and password without the user having to specify it at
     * connection time.
     *
     * @param dataSource The DataSource to anonimise.
     * @param username   The username to use during connection.
     * @param password   The password to use during connection.
     * @return Tha anonymised DataSource
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-include-in ProfessionalServicesAPI
     * @volantis-api-include-in InternalAPI
     */
    public abstract DataSource createAnonymousDataSource(
            DataSource dataSource, String username, String password);
}
