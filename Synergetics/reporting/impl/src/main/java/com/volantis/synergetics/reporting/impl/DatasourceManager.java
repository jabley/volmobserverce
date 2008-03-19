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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl;

import java.util.HashMap;
import java.util.Map;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.reporting.config.ConfigurationException;
import com.volantis.synergetics.reporting.config.ConnectionStrategy;
import com.volantis.synergetics.reporting.config.DatasourceConfiguration;

/**
 * JDBC datasources manager. Singleton pattern.
 *
 */
public class DatasourceManager {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DatasourceManager.class);

    /**
     * Singleton instance
     */
    private static DatasourceManager instance = new DatasourceManager();

    /**
     * map datasource name -> ConnectionStrategy
     */
    private Map datasources;

    /**
     * Constructor disabled. Singleton pattern;
     *
     */
    private DatasourceManager() {
        datasources = new HashMap();
    }

    /**
     * Get instance. singlrton pattern.
     * @return DatasourceManager
     */
    public static DatasourceManager getInstance() {
        return instance;
    }
    /**
     * Add new connection strategy to cache
     * @param datasourceParameters DatasourceConfiguration
     * @return ConnectionStrategy
     */
    public synchronized ConnectionStrategy addConnectionStrategy(
            DatasourceConfiguration datasourceParameters) {
        ConnectionStrategy connStrategy = null;
        try {
            connStrategy = datasourceParameters.createConnectionStrategy();
        } catch (ConfigurationException ex) {
            LOGGER.error("datasource-creation-error", datasourceParameters.getName());
        }
        if(connStrategy != null) {
            // overwrite any other datasource that has the same name.
            datasources.put(datasourceParameters.getName(), connStrategy);
        }
        return connStrategy;
    }

    /**
     * Get connection strategy
     * @param datasourceName String datasource name
     * @return ConnectionStrategy
     */
    public ConnectionStrategy getConnectionStrategy(String datasourceName) {
        return (ConnectionStrategy) datasources.get(datasourceName);
    }

    /**
     * Create and register connection strategy if not exists
     * otherwise return connection stategy from cache
     * @param datasourceParameters
     * @return ConnectionStrategy
     */
    public ConnectionStrategy createConnectionStrategy(
            DatasourceConfiguration datasourceParameters) {
        ConnectionStrategy connStrategy =
            getConnectionStrategy(datasourceParameters.getName());
        //create new not exists in cache
        if(connStrategy == null) {
            connStrategy = addConnectionStrategy(datasourceParameters);
        }
        return connStrategy;
    }
}
