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

import com.volantis.mcs.repository.RepositoryException;

import java.util.Properties;

/**
 * A JDBC driver base data source that also provides information about the
 * vendor.
 */
public class VendorDriverDataSource
        extends DriverDataSource
        implements VendorDataSource {

    private final JDBCDriverVendor vendor;

    public VendorDriverDataSource(
            String driverClassName, String url, Properties driverProperties,
            JDBCDriverVendor vendor)
            throws RepositoryException {
        super(driverClassName, url, driverProperties);

        this.vendor = vendor;
    }

    public VendorDriverDataSource(
            String driverClassName, String url, JDBCDriverVendor vendor)
            throws RepositoryException {
        this(driverClassName, url, null, vendor);
    }

    public JDBCDriverVendor getVendor() {
        return vendor;
    }
}
