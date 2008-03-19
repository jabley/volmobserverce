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
 * Internal connection pool configuration parameters
 *
 */
public class InternalPoolDatasource implements DatasourceConfiguration {

    /**
     * datasource name
     */
    private String name;

    /**
     * JDBC driver class
     */
    private String driverClass;

    /**
     * Connection URL to database
     */
    private String url;

    /**
     * Username
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * Max number of connections in the pool
     */
    private String maxActive;

    /**
     * Max number of idle connections in the pool
     */
    private String maxIdle;

    /**
     * Max time before connection timeout  (in miliseconds)
     */
    private String maxWait;

    // javadoc unnecessary
    public void setName(String name) {
        this.name = name;
    }

    // javadoc unnecessary
    public String getName() {
        return name;
    }

    // javadoc unnecessary
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    // javadoc unnecessary
    public String getDriverClass() {
        return driverClass;
    }

    // javadoc unnecessary
    public String getMaxWait() {
        return maxWait;
    }

    // javadoc unnecessary
    public int getMaxWaitAsInt() {
        try {
            return Integer.parseInt(maxWait);
        } catch(NumberFormatException ex) {
            return Integer.MIN_VALUE;
        }
    }

    // javadoc unnecessary
    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    // javadoc unnecessary
    public String getPassword() {
        return password;
    }

    // javadoc unnecessary
    public void setPassword(String password) {
        this.password = password;
    }

    // javadoc unnecessary
    public String getUrl() {
        return url;
    }

    // javadoc unnecessary
    public void setUrl(String url) {
        this.url = url;
    }

    // javadoc unnecessary
    public String getUsername() {
        return username;
    }

    // javadoc unnecessary
    public void setUsername(String username) {
        this.username = username;
    }

    // javadoc unnecessary
    public String getMaxActive() {
        return maxActive;
    }

    // javadoc unnecessary
    public int getMaxActiveAsInt() {
        try {
            return Integer.parseInt(maxActive);
        } catch(NumberFormatException ex) {
            return Integer.MIN_VALUE;
        }
    }

    // javadoc unnecessary
    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    // javadoc unnecessary
    public String getMaxIdle() {
        return maxIdle;
    }

    // javadoc unnecessary
    public int getMaxIdleAsInt() {
        try {
            return Integer.parseInt(maxIdle);
        } catch(NumberFormatException ex) {
            return Integer.MIN_VALUE;
        }
    }

    // javadoc unnecessary
    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    // javadoc inherited
    public ConnectionStrategy createConnectionStrategy() {
        return new InternalPoolConnectionStrategy(this);
    }

    // javadoc inherited
    public DataSourceType getType() {
        return DataSourceType.INTERNAL_POOL_DATASOURCE;
    }
}
