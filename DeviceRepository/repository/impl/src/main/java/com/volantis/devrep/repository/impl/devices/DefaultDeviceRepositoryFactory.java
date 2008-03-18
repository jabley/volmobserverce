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
package com.volantis.devrep.repository.impl.devices;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.devices.logging.UnknownDevicesLogger;
import com.volantis.devrep.repository.impl.DeviceRepositoryLocationImpl;
import com.volantis.devrep.repository.impl.accessors.jdbc.JDBCDeviceRepositoryAccessor;
import com.volantis.devrep.repository.impl.devices.jdbc.DefaultJDBCDeviceRepositoryConfiguration;
import com.volantis.devrep.repository.impl.devices.xml.DefaultXMLDeviceRepositoryConfiguration;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryConfiguration;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.DeviceRepositoryFactory;
import com.volantis.mcs.devices.jdbc.JDBCDeviceRepositoryConfiguration;
import com.volantis.mcs.devices.xml.XMLDeviceRepositoryConfiguration;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.Repository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.jdbc.InternalJDBCRepository;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;
import com.volantis.synergetics.cache.GenericCache;
import com.volantis.synergetics.cache.GenericCacheConfiguration;
import com.volantis.synergetics.cache.GenericCacheFactory;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;

/**
 * A default implementation of a factory for creating {@link DeviceRepository}
 * instances.
 */
public class DefaultDeviceRepositoryFactory extends DeviceRepositoryFactory {

    private static final DeviceRepositoryAccessorFactory REPOSITORY_ACCESSOR_FACTORY =
        DeviceRepositoryAccessorFactory.getDefaultInstance();

    private static final String DEFAULT_STRATEGY = "least-recently-used";

    // Javadoc inherited.
    public JDBCDeviceRepositoryConfiguration
            createJDBCDeviceRepositoryConfiguration() {

        return new DefaultJDBCDeviceRepositoryConfiguration();
    }

    // Javadoc inherited.
    public XMLDeviceRepositoryConfiguration
            createXMLDeviceRepositoryConfiguration() {

        return new DefaultXMLDeviceRepositoryConfiguration();
    }

    // Javadoc inherited.
    public DeviceRepository createDeviceRepository(
            JDBCDeviceRepositoryConfiguration configuration)
            throws DeviceRepositoryException {

        JDBCRepositoryFactory factory =
                JDBCRepositoryFactory.getDefaultInstance();
        JDBCRepositoryConfiguration repositoryConfiguration =
                factory.createJDBCRepositoryConfiguration();
        repositoryConfiguration.setDataSource(
                configuration.getDataSource());
        repositoryConfiguration.setReleaseConnectionsImmediately(false);  
        repositoryConfiguration.setShortNames(configuration.isUseShortNames());

        DeviceRepositoryLocation location = new DeviceRepositoryLocationImpl(
                configuration.getDefaultProject());

        try {
            // Create the JBDC repository.
            InternalJDBCRepository repository = (InternalJDBCRepository)                 
                    factory.createJDBCRepository(repositoryConfiguration);

            // Create the JDBC accessor.
            DeviceRepositoryAccessor accessor =
                    new JDBCDeviceRepositoryAccessor(repository, location);

            configureCache(accessor, configuration);
            // Initialise the device pattern cache.
            initialiseDevicePatternCache(repository, accessor);

            // Return the default device repository impl using the JDBC
            // repository and accessor we just created.
            final String logFileName =
                configuration.getUnknownDevicesLogFileName();
            final UnknownDevicesLogger logger;
            if (logFileName != null && logFileName.length() > 0) {
                logger = UnknownDevicesLogger.getLogger(
                    new File(logFileName));
            } else {
                logger = null;
            }
            return new DefaultDeviceRepository(repository, accessor, logger);
        } catch (RepositoryException e) {
            throw new DeviceRepositoryException(e);
        }
    }

    // javadoc inherited.
    public DeviceRepository getDeviceRepository(DataSource datasource,
            String project) throws DeviceRepositoryException {

        JDBCDeviceRepositoryConfiguration config =
                new DefaultJDBCDeviceRepositoryConfiguration();
        config.setDataSource(datasource);
        config.setDefaultProject(project);
        config.setUseShortNames(false);
        configureDefaultCacheValues(config);
        return createDeviceRepository(config);
    }


    public DeviceRepository createDeviceRepository(
            XMLDeviceRepositoryConfiguration configuration,
            final String unknownDevicesLogFileName)
            throws DeviceRepositoryException {
        
        try {
            // Create the XML repository
            // Note that we can only handle file: urls.
            DeviceRepositoryLocation location =
                    new DeviceRepositoryLocationImpl(
                            configuration.getRepositoryUrl().getFile());

            XMLRepositoryFactory factory =
                    XMLRepositoryFactory.getDefaultInstance();
            LocalRepository repository =
                    factory.createXMLRepository(null);

            // Create the XML accessor.

            DeviceRepositoryAccessor accessor =
                REPOSITORY_ACCESSOR_FACTORY.createDeviceRepositoryAccessor(
                    repository, location, configuration);

            configureCache(accessor, configuration);
            // Initialise the device pattern cache.
            initialiseDevicePatternCache(repository, accessor);

            // Return the default device repository impl using the XML
            // repository and accessor we just created.
            final UnknownDevicesLogger logger;
            if (unknownDevicesLogFileName != null &&
                unknownDevicesLogFileName.length() > 0) {
                final File logFile = new File(unknownDevicesLogFileName);
                logger = UnknownDevicesLogger.getLogger(logFile);
            } else {
                logger = null;
            }
            return new DefaultDeviceRepository(repository, accessor,
                configuration.getAllowExperimentalPolicies(), logger);
        } catch (RepositoryException e) {
            throw new DeviceRepositoryException(e);
        }
    }

    // javadoc inherited.
    public DeviceRepository getDeviceRepository(
                final URL repositoryUrl, final String unknownDevicesLogFileName)
            throws DeviceRepositoryException {

        XMLDeviceRepositoryConfiguration config =
                new DefaultXMLDeviceRepositoryConfiguration();
        config.setRepositoryURL(repositoryUrl);
        configureDefaultCacheValues(config);
        return createDeviceRepository(config, unknownDevicesLogFileName);
    }

    /**
     * Initialise the accessor's device pattern cache so it is usable.
     *
     * @param repository the repository that the accessor is to be used with.
     * @param accessor the accessor to initialise
     * @throws RepositoryException if there was a problem.
     */
    private void initialiseDevicePatternCache(Repository repository,
            DeviceRepositoryAccessor accessor) throws RepositoryException {

        RepositoryConnection connection = null;
        try {
            connection = repository.connect();
            accessor.initializeDevicePatternCache(connection);
            connection.disconnect();
            connection = null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Configure the cache for the repository.
     * @param config The cache Configuration.
     */
    private void configureCache(DeviceRepositoryAccessor accessor,
            DeviceRepositoryConfiguration config) {

        if (config != null) {
            GenericCacheConfiguration genericConfiguration =
                    new GenericCacheConfiguration();

            genericConfiguration.setStrategy(DEFAULT_STRATEGY);
            genericConfiguration.setMaxEntries(config.getMaxEntries());
            genericConfiguration.setTimeout(config.getMaxAge());
            genericConfiguration.setRequestReferenceCaching(false);

            GenericCache cache =
                GenericCacheFactory.createCache(null, genericConfiguration);
            accessor.setDeviceCache(cache);
        }

    }

    /**
     * Configure default cache values if they have not been supplied.
     *
     * @param config The configurtation item on which to set the default
     * cache values.
     */
    public void configureDefaultCacheValues(
            DeviceRepositoryConfiguration config) {
        // Never expire
        config.setMaxAge(-1);
        // Allow 1000 entries
        config.setMaxEntries(1000);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 02-Aug-04	5058/1	geoff	VBM:2004080208 Implement the missing mutable http headers for device repository

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4970/1	byron	VBM:2004072704 Public API for Device Repository: implement unit and/or integration tests

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/
