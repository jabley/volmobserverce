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

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Connection through JNDI data source.
 *
 */
public class JNDIConnectionStrategy implements ConnectionStrategy {

    /**
     * Data source
     */
    private final DataSource dataSource;

    /**
     * Constructor
     * @param params JndiDatasource configuration parameters
     * @throws ConfigurationException
     */
    public JNDIConnectionStrategy(JNDIDatasource params)
            throws ConfigurationException {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup(params.getJndiName());
        } catch (NamingException ex) {
            throw new ConfigurationException(ex);
        }
    }

    //  javadoc inherited
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // javadoc inherited
    public DataSource getDataSource() {
        return dataSource;
    }

}
