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


import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import java.util.Iterator;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.JDBCRepository;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This is an implementation of the DataSourceFactory which
 * provides the ability to create a JDBC specific datasource from the JDBC
 * specific configuration.
 */
public class JDBCDataSourceFactory {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(JDBCDataSourceFactory.class);

    /**
     * Create the factory with the NamedDataSourceConfiguration object
     */
    public  JDBCDataSourceFactory() {
    }

    /**
     * Create a properties map that may be used by the creation of the
     * datasource.
     *
     * @param  config the JDBCDriverConfiguration object
     * @return        a map of properties where the key is defined in
     *                JDBCRepository constants.
     */
    private Map createPropertiesMap(JDBCDriverConfiguration config) {
        Map properties = new HashMap();


        properties.put(JDBCRepository.DATABASE_URL_PROPERTY,
                       config.getDatabaseURL());

        properties.put(JDBCRepository.DRIVER_CLASS_PROPERTY,
                       config.getDriverClass());

        properties.put(JDBCRepository.PARAMETERS_PROPERTY,
                       config.getParameters());
        Map userParameters = config.getParameters();
        Iterator userParametersIterator = userParameters.keySet().iterator();
        while (userParametersIterator.hasNext()) {
            String key = (String)userParametersIterator.next();
            String value = (String)userParameters.get(key);
            properties.put(key,value);
        }
        return properties;
    }

    // javadoc inherited.
    public DataSource createDataSource(JDBCDriverConfiguration config) {
        DataSource dataSource = null;
        if (config != null) {

                try {
                    Map properties = 
                        createPropertiesMap(config);
                    dataSource = JDBCRepository.createJDBCDriverDataSource(properties);
                        
                } catch (RepositoryException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not create dataSource: " + e);
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

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 09-Mar-04	2867/4	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Jul-03	478/1	byron	VBM:2003061401 Unfinished issues of 2003060403

 27-Jun-03	586/1	byron	VBM:2003062704 Username and password are not used if no connection pooling in mariner-config.xml

 13-Jun-03	316/3	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
