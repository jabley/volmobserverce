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

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCDriverVendor;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;
import com.volantis.mcs.repository.jdbc.VendorDriverDataSource;

import javax.sql.DataSource;
import java.util.Properties;

class SybaseVendorFactory
        extends AbstractVendorFactory {

    public SybaseVendorFactory() {
        super(JDBCRepositoryType.SYBASE);
    }

    public InternalJDBCRepository createRepository(
            InternalJDBCRepositoryConfiguration configuration) {
        return new SybaseRepository(configuration);
    }

    public DataSource createDriverDataSource(
            MCSDriverConfiguration configuration) throws RepositoryException {

        Properties connectionProperties = new Properties();

        // Enable wide column support
        connectionProperties.put("JCONNECT_VERSION", "6");

        return new VendorDriverDataSource(repositoryType.getDriverClassName(),
                getDriverSpecificURL(configuration),
                connectionProperties, JDBCDriverVendor.SYBASE);
    }

    public String getDriverSpecificURL(MCSDriverConfiguration configuration)
            throws RepositoryException {

        String host = getHost(configuration);
        int port = configuration.getPort();

        String url = "jdbc:" + JDBCRepositoryType.SYBASE.getSubProtocol() +
                ":Tds:" + host + ":" + port + "/" +
                getSource(configuration);
        return url;
    }
}
