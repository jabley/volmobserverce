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

/**
 * Pure driver connection configuration parameters
 *
 */
public class JDBCDatasource implements DatasourceConfiguration {

    /**
     * datasource name
     */
    private String name;

    /**
     * JDBC driver class
     */
    private String driverClass;

    /**
     * Connection string to database
     */
    private String connectionString;

    // javadoc unnecessary
    public String getName() {
        return this.name;
    }

    // javadoc unnecessary
    public void setName(String name) {
        this.name = name;
    }

    // javadoc unnecessary
    public String getDriverClass() {
        return this.driverClass;
    }

    // javadoc unnecessary
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    // javadoc unnecessary
    public String getConnectionString() {
        return this.connectionString;
    }

    // javadoc unnecessary
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    // javadoc inherited
    public ConnectionStrategy createConnectionStrategy() {
        return new PureDriverConnectionStrategy(this.getDriverClass(), this.getConnectionString());
    }
    
    // javadoc inherited
    public DataSourceType getType() {
        return DataSourceType.JDBC_DATASOURCE;
    }
}
