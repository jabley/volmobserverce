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
import com.volantis.mcs.repository.jdbc.JDBCRepositoryType;
import com.volantis.mcs.repository.jdbc.MCSDriverConfiguration;

class DB2MVSVendorFactory
        extends AbstractVendorFactory {

    public DB2MVSVendorFactory() {
        super(JDBCRepositoryType.DB2_MVS);
    }

    public InternalJDBCRepository createRepository(
            InternalJDBCRepositoryConfiguration configuration) {
        return new DB2MVSRepository(configuration);
    }

    public String getDriverSpecificURL(MCSDriverConfiguration configuration)
            throws RepositoryException {
        StringBuffer url = new StringBuffer("jdbc:" +
                JDBCRepositoryType.DB2_MVS.getSubProtocol() + ":").
                append(getSource(configuration));

        if (logger.isDebugEnabled()) {
            logger.debug("Using DB2 JDBC driver URL of: " + url);
        }

        return url.toString();
    }
}
