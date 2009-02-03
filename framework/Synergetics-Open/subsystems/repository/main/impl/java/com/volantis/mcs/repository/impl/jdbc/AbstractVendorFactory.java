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
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;
import com.volantis.mcs.repository.jdbc.VendorDriverDataSource;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;

public abstract class AbstractVendorFactory
        implements VendorFactory {

    /**
     * Used for logging.
     */
    protected static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractVendorFactory.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractVendorFactory.class);

    /**
     * Helper method to obtain the database host from the configuration.
     * properties.
     *
     * @param configuration the configuration.
     * @return The name of the host to use when connecting to the database.
     * @throws RepositoryException if host is not set.
     */
    protected static String getHost(MCSDriverConfiguration configuration)
            throws RepositoryException {

        String host = configuration.getHost();
        if (host == null || host.length() == 0) {
            throw new JDBCRepositoryException(
                    EXCEPTION_LOCALIZER.format("jdbc-missing-host"));
        }

        return host;
    }

    /**
     * Helper method to obtain the source property from the configuration.
     *
     * @param configuration the configuration
     * @return The source to use when attempting to connect to the database.
     * @throws RepositoryException if source is not set.
     */
    protected static String getSource(MCSDriverConfiguration configuration)
            throws RepositoryException {

        String source = configuration.getSource();
        if (source == null || source.length() == 0) {
            throw new JDBCRepositoryException(
                    EXCEPTION_LOCALIZER.format("jdbc-missing-source"));
        }

        return source;
    }

    protected final JDBCRepositoryType repositoryType;

    protected AbstractVendorFactory(JDBCRepositoryType repositoryType) {
        this.repositoryType = repositoryType;
    }

    public DataSource createDriverDataSource(
            MCSDriverConfiguration configuration) throws RepositoryException {
        return new VendorDriverDataSource(repositoryType.getDriverClassName(),
                getDriverSpecificURL(configuration),
                repositoryType.getVendor());
    }
}
