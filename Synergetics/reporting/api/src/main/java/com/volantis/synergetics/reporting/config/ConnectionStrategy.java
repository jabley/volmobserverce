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

import javax.sql.DataSource;

/**
 * Strategy pattern.
 * Delaying decision about creating database connections mechanism
 */
public interface ConnectionStrategy {
    /**
     * Get opened connection to sql database
     * @return Connection sql database connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException;
    
    /**
     * Gets {@link javax.sql.DataSource} instance related to this connection
     * strategy.
     * 
     * @return DataSource which may be used to obtain the database connection
     */
    public DataSource getDataSource(); 
}
