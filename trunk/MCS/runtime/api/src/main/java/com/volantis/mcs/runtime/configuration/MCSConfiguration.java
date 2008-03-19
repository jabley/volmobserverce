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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.pipeline.extensions.PipelineConfigurationExtensionFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Contains the business logic based on the MCS configuration file.  This class
 * delegates to an instance of {@link MarinerConfiguration} which must be
 * supplied during creation for any actual data values from the configuration
 * file.
 */
public class MCSConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MCSConfiguration.class);

    /**
     * The configuration data object that this object delegates to when it
     * requires any of that data.
     */
    private MarinerConfiguration config;

    /**
     * Factory for JDBC data sources
     */
    private JDBCDataSourceFactory jdbcDataSourceFactory =
        new JDBCDataSourceFactory();

    /**
     * Factory for MCS data sources
     */
    private MCSDataSourceFactory mcsDataSourceFactory =
        new MCSDataSourceFactory();

    /**
     * Factory for JNDI data sources
     */
    private JNDIDataSourceFactory jndiDataSourceFactory =
        new JNDIDataSourceFactory();

    /**
     * Factory for anonymous data sources
     */
    private AnonymousDataSourceFactory anonymousDataSourceFactory =
        new AnonymousDataSourceFactory();

    /**
     * Factory for connection pool data sources
     */
    private ConnectionPoolFactory connectionPoolFactory =
        new ConnectionPoolFactory();

    /**
     * A map of all the global data sources
     */
    private Map globalDataSources;

    /**
     * A map of extension driver based data sources
     */
    private Map extensionDataSources = new HashMap();

    /**
     * A reference to a local repository data source
     */
    private DataSource localRepositoryJDBCDataSource;

    /**
     * Initialize a new instance of this MCS cobfiguration object.  It is
     * initialized using the supplied data configuration object which is used
     * internally for any actual config values.
     *
     * @param config The data from the MCS configuration file.  May not be null
     */
    public MCSConfiguration(MarinerConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }
        this.config = config;
    }

    /**
     * Add extension datasources to the configuration
     *
     * @param dataSources a Map of the datasources to add
     */
    public void addExtensionDataSources(Map dataSources) {
        extensionDataSources.putAll(dataSources);
    }

    /**
     * Resolve all data sources.  This resolves concrete and reference data
     * sources based on the configuration data supplied to this class as it
     * is constructed.  It also resolves any local repository.
     */
    public void resolveDataSources() {

        // First Pass - Resolve All concrete datasources

        // resolve global datasources
        DataSourcesConfiguration dataSourcesConfiguration =
                config.getDataSourcesConfiguration();

        if (dataSourcesConfiguration != null) {
            globalDataSources =
                    resolveConcreteDataSources(
                            dataSourcesConfiguration.getNamedDataSources());
        }

        // extension hook
        PipelineConfigurationExtensionFactory.getDefaultInstance().
                extendDataSourceResolution(config, this);


        // Second pass - Resolve Reference datasources

        // resolve global datasources
        if (globalDataSources != null) {
            globalDataSources =
                    resolveReferenceDataSources(globalDataSources, null);
        }

        // extension hook
        PipelineConfigurationExtensionFactory.getDefaultInstance().
                extendReferenceDataSourceResolution(this);

        // Now we have everything else resolved resolve the local repository
        LocalRepositoryConfiguration localRepository =
                config.getLocalRepository();

        if (localRepository.getJDBCRepositoryConfiguration() != null) {
            AnonymousDataSource localRepositoryAnonymousDataSource =
                    localRepository.getJDBCRepositoryConfiguration()
                    .getDataSourceConfiguration();

            if (localRepositoryAnonymousDataSource
                    instanceof DataSourceConfiguration) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Local Repository is a Reference");
                }
                localRepositoryJDBCDataSource =
                        resolveReferenceDataSource(
                                globalDataSources,
                                ((DataSourceConfiguration)
                        localRepositoryAnonymousDataSource).getRef());
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Local Repository is a Concrete DataSource");
                }
                localRepositoryJDBCDataSource =
                        resolveConcreteDataSource(
                                localRepositoryAnonymousDataSource);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("JDBC Repository Configuration is null");
            }
        }

    }

    /**
     * This method resolves a data source configuration to a concrete
     * data source. If the data source is a reference then a null is returned.
     *
     * @param namedDataSource The data source configuration object to resolve
     *
     * @return A concrete DataSource or null
     */
    private DataSource resolveConcreteDataSource(
            AnonymousDataSource namedDataSource) {

        JNDIConfiguration jndiConfiguration = config.getJndiConfiguration();

        DataSource dataSource = null;
        if (namedDataSource instanceof JDBCDriverConfiguration) {
            if (logger.isDebugEnabled()) {
                logger.debug("Local Repository is a JDBC DataSource");
            }
            dataSource = jdbcDataSourceFactory.createDataSource(
                    (JDBCDriverConfiguration)namedDataSource);
        } else if (namedDataSource instanceof MCSDatabaseConfiguration) {
            if (logger.isDebugEnabled()) {
                logger.debug("Local Repository is an MCS DataSource");
            }
            dataSource = mcsDataSourceFactory.createDataSource(
                    (MCSDatabaseConfiguration)namedDataSource);
        } else if (namedDataSource instanceof JNDIDataSourceConfiguration) {
            if (logger.isDebugEnabled()) {
                logger.debug("Local Repository is a JNDI DataSource");
            }
            dataSource = jndiDataSourceFactory.createDataSource(
                    (JNDIDataSourceConfiguration)namedDataSource,
                    jndiConfiguration);
        } else if (
                namedDataSource instanceof AnonymousDataSourceConfiguration) {

            if (logger.isDebugEnabled()) {
                logger.debug("Local Repository is an Anonymous DataSource");
            }
            dataSource = anonymousDataSourceFactory.createDataSource(
                    (AnonymousDataSourceConfiguration)namedDataSource,
                    jndiConfiguration);
        } else if (namedDataSource instanceof ConnectionPoolConfiguration) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Local Repository is a Connection Pool DataSource");
            }
            dataSource = connectionPoolFactory.createDataSource(
                    (ConnectionPoolConfiguration)namedDataSource,
                    jndiConfiguration);
        } else {
            // This must be a DataSourceConfiguration so return null.
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Local Repository is unkown type :"
                        + namedDataSource.getClass().getName());
            }
        }
        return dataSource;
    }

    /**
     * This method takes a List of data source configuration objects and
     * resolves them to concrete datasources or references to concrete data
     * sources.
     *
     * @param dataSourcesConfiguration The data sources configuration list.
     *
     * @return A Map of named concrete data sources.
     */
    public Map resolveConcreteDataSources(List dataSourcesConfiguration) {

        Map dataSourcesMap = new HashMap();

        Iterator dataSourcesConfigurationIterator =
                dataSourcesConfiguration.iterator();

        while (dataSourcesConfigurationIterator.hasNext()) {

            NamedDataSourceConfiguration namedDataSourceConfiguration =
                    (NamedDataSourceConfiguration)
                    dataSourcesConfigurationIterator.next();

            DataSource dataSource =
                    resolveConcreteDataSource(
                            namedDataSourceConfiguration.
                            getDataSourceConfiguration());

            if (dataSource != null) {
                dataSourcesMap.put(
                        namedDataSourceConfiguration.getName(),
                        dataSource);
                if (namedDataSourceConfiguration.getConnectAtStartUp()
                        .booleanValue()) {
                    try {
                        //Check that we can connect
                        Connection con = dataSource.getConnection();
                        con.close();
                    } catch (SQLException e) {
                        // log any errors
                        logger.error("datasource-connection-failure", e);
                    }
                }
            } else
            // If the DataSource is null then it must be a reference,
            // we are not responsible for resolving those, so just store
            // the reference for now.
                dataSourcesMap.put(
                        namedDataSourceConfiguration.getName(),
                        ((DataSourceConfiguration)
                        namedDataSourceConfiguration
                        .getDataSourceConfiguration()).getRef());
        }
        return dataSourcesMap;
    }

    /**
     * A utility method to resolve reference datasources in the target Map to
     * concrete dataSources.
     * @param targetDataSources The Map of concrete and reference data sources
     *                          to resolve.
     * @param globalDataSources An optional global Map of concrete data sources
     *                          to resolve against.
     * @return The fully resolved Map of data sources
     */
    public Map resolveReferenceDataSources(Map targetDataSources,
                                            Map globalDataSources) {


        Iterator dataSourcesIterator = targetDataSources.keySet().iterator();

        while (dataSourcesIterator.hasNext()) {
            Object obj = dataSourcesIterator.next();

            if (obj instanceof String) {
                DataSource dataSource =
                        resolveReferenceDataSource(targetDataSources,
                                                   (String)obj);
                if (dataSource == null) {
                    // Try and resolve the datasource from
                    // the global datasosurces.
                    dataSource =
                            resolveReferenceDataSource(
                                    globalDataSources,
                                    (String)obj);
                }

                if (dataSource != null) {
                    targetDataSources.put((String)obj, dataSource);
                } else {
                    //todo: throw a wobbly
                }
            }
        }
        return targetDataSources;
    }

    /**
     * Recursively resolves reference data sources to concrete data sources.
     *
     * @param concreteDataSources A map of concrete and reference data sources
     *                            to resolve against.
     * @param dataSourceName The name of the data source.
     *
     * @return The concrete data source or null if not found.
     */
    private DataSource resolveReferenceDataSource(
            Map concreteDataSources,
            String dataSourceName) {

        DataSource dataSource = null;
        if (concreteDataSources != null) {
            Object obj = concreteDataSources.get(dataSourceName);
            if (obj != null) {
                if (obj instanceof String) {
                    //recurse until we find the data source or null
                    dataSource =
                            resolveReferenceDataSource(concreteDataSources,
                                                       (String)obj);
                } else {
                    //found a concrete data source
                    dataSource = (DataSource)obj;
                }
            }

        }
        return dataSource;
    }

    /**
     * Get the Map of named global DataSources
     *
     * @return a Map of named DataSources
     */
    public Map getGlobalDataSources() {
        return globalDataSources;
    }

    /**
     * Get the local repository DataSource
     *
     * @return a DataSource
     */
    public DataSource getLocalRepositoryJDBCDataSource() {
        return localRepositoryJDBCDataSource;
    }

    /**
     * Get the Map of named Extension DataSources
     * 
     * @return a Map of named DataSources
     */
    public Map getExtensionDataSources() {
        return extensionDataSources;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7632/4	doug	VBM:2005041110 Fixed issue with SQLDriver configuration not being read in from MCS config

 12-Apr-05	7632/2	doug	VBM:2005041110 Fixed issue with SQLDriver configuration not being read in from MCS config

 12-Apr-05	7625/1	doug	VBM:2005041110 Fixed issue with SQLDriver configuration not being read in from MCS config

 01-Apr-05	6798/6	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 09-Sep-04	5466/1	claire	VBM:2004090905 New Build Mechanism: Refactor business logic out of MarinerConfiguration

 ===========================================================================
*/
