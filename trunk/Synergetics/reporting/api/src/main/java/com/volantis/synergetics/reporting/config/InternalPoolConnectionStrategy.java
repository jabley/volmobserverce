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

import org.apache.commons.dbcp.BasicDataSource;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Connection through internal dedicated pooled data source.
 *
 */
public class InternalPoolConnectionStrategy implements ConnectionStrategy {

    /**
     * Default pool parameter maximum active connections
     */
    private static final int DEFAULT_MAX_ACTIVE = 15;

    /**
     * Default pool parameter maximum idle connections
     */
    private static final int DEFAULT_MAX_IDLE = 5;

    /**
     * Default pool parameter maximum time before connection timeout in miliseconds
     */
    private static final int DEFAULT_MAX_WAIT = 30000;

    /**
     * Used for localized logger
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(InternalPoolConnectionStrategy.class);

    /**
     * Poolable data source
     */
    private final BasicDataSource dataSource;

    /**
     * Constructor
     * @param params PoolDatasource
     */
    public InternalPoolConnectionStrategy(
            final InternalPoolDatasource params) {
        dataSource = new BasicDataSource();

        dataSource.setDriverClassName(params.getDriverClass());
        dataSource.setUrl(params.getUrl());
        dataSource.setUsername(params.getUsername());
        dataSource.setPassword(params.getPassword());

        if(params.getMaxActiveAsInt() > 0) {
            dataSource.setMaxActive(params.getMaxActiveAsInt());
        } else {
            dataSource.setMaxActive(DEFAULT_MAX_ACTIVE);
        }

        if(params.getMaxWaitAsInt() > 0) {
            dataSource.setMaxWait(params.getMaxWaitAsInt());
        } else {
            dataSource.setMaxWait(DEFAULT_MAX_WAIT);
        }

        if(params.getMaxIdleAsInt() > 0) {
            dataSource.setMaxIdle(params.getMaxIdleAsInt());
        } else {
            dataSource.setMaxIdle(DEFAULT_MAX_IDLE);
        }

        dataSource.addConnectionProperty("autoReconnect", "true");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("connection-pool-initialized",
                    getInitializationValues());
        }
    }

    /**
     * Get initialization parameters info
     * @return String datasource initialization values info
     */
    private String getInitializationValues() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append("driverClass=");
        sb.append(dataSource.getDriverClassName());
        sb.append(", url=");
        sb.append(dataSource.getUrl());
        sb.append(", maxActive=");
        sb.append(dataSource.getMaxActive());
        sb.append(", maxIdle=");
        sb.append(dataSource.getMaxIdle());
        sb.append(", maxWait=");
        sb.append(dataSource.getMaxWait());
        return sb.toString();
    }

    // javadoc inherited
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // javadoc inherited
    public DataSource getDataSource() {
        return dataSource;
    }

}
