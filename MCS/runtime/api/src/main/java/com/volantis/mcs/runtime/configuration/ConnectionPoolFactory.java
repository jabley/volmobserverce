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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepository;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an implementation of the DataSourceFactory which
 * provides the ability to create a JDBC specific datasource from the JDBC
 * specific configuration.
 */
public class ConnectionPoolFactory {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ConnectionPoolFactory.class);

    // Setup factories for concrete datasources
    private JDBCDataSourceFactory jdbcDataSourceFactory = 
        new JDBCDataSourceFactory();
                
    private MCSDataSourceFactory mcsDataSourceFactory = 
        new MCSDataSourceFactory();
            
    private JNDIDataSourceFactory jndiDataSourceFactory =
        new JNDIDataSourceFactory();
            
    private AnonymousDataSourceFactory anonymousDataSourceFactory =
        new AnonymousDataSourceFactory();

    /**
     * Create the factory. 
     */
    public ConnectionPoolFactory() {

    }

    /**
     * Create a properties map that may be used by the creation of the
     * datasource.
     *
     * @param  config the JDBCDriverConfiguration object
     * @return        a map of properties where the key is defined in
     *                JDBCRepository constants.
     */
    private Map createPropertiesMap(AnonymousDataSource config) {
        Map properties = new HashMap();


//        properties.put(JDBCRepository.DATABASE_URL_PROPERTY,
//                       config.getDatabaseURL());
//
//        properties.put(JDBCRepository.DRIVER_CLASS_PROPERTY,
//                       config.getDriverClass());
//
//        properties.put(JDBCRepository.PARAMETERS_PROPERTY,
//                       config.getParameters());

        return properties;
    }

    /**
     * Create the DataSource from the AnonymouseDataSource object
     *
     * @param config            the NamedDataSource object.
     */
    public DataSource createDataSource(ConnectionPoolConfiguration config,
            JNDIConfiguration jndiConfiguration) {
                
        DataSource dataSource = null;
        if (config != null) {
            DataSource delegateDataSource = null;
            AnonymousDataSource connectionPoolDataSource =
                config.getDataSourceConfiguration();
            if (connectionPoolDataSource instanceof JDBCDriverConfiguration) {
                delegateDataSource =
                    jdbcDataSourceFactory.createDataSource(
                        (JDBCDriverConfiguration)connectionPoolDataSource);
            } else if (
                connectionPoolDataSource instanceof MCSDatabaseConfiguration) {
                delegateDataSource =
                    mcsDataSourceFactory.createDataSource(
                        (MCSDatabaseConfiguration)connectionPoolDataSource);
            } else if (
                connectionPoolDataSource instanceof JNDIDataSourceConfiguration) {
                delegateDataSource =
                    jndiDataSourceFactory.createDataSource(
                        (JNDIDataSourceConfiguration)connectionPoolDataSource,
                        jndiConfiguration);
            } else if (
                connectionPoolDataSource 
                    instanceof AnonymousDataSourceConfiguration) {
                        delegateDataSource = 
                            anonymousDataSourceFactory.createDataSource(
                (AnonymousDataSourceConfiguration)connectionPoolDataSource,
                    jndiConfiguration);
            }
            if (delegateDataSource != null) {
                Map connectionPoolProperties = new HashMap();
                
                // Setup the connection pool properties
                connectionPoolProperties.put(
                        JDBCRepository.POOL_ENABLED_PROPERTY,"true");
                connectionPoolProperties.put(
                        JDBCRepository.POOL_MAX_CONNECTIONS_PROPERTY,
                        config.getMaximum());        
                connectionPoolProperties.put(
                        JDBCRepository.CONNECTION_POLL_INTERVAL_PROPERTY,
                        config.getPollInterval());
                connectionPoolProperties.put(
                        JDBCRepository.KEEP_CONNECTIONS_ALIVE_PROPERTY,
                        config.getKeepAlive());        

                try {
                    dataSource = 
                        JDBCRepository.createConnectionPool(
                                connectionPoolProperties,delegateDataSource);
                } catch (RepositoryException re) {
                	if (logger.isDebugEnabled()) {
	                    logger.debug("Error creating connection pool",re);
					}
                }
            }

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot create a datasource with a 'null' " +
                             "configuration");
            }
        }       
        return dataSource;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 09-Mar-04	2867/2	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
