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
package com.volantis.osgi.impl.cm.monitor;

import com.volantis.synergetics.directory.monitor.DirectoryMonitor;
import com.volantis.synergetics.directory.monitor.DirectoryMonitorCallback;
import com.volantis.synergetics.directory.monitor.RegistrationException;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.lang.reflect.UndeclaredThrowableException;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;

/**
 * Monitors the configuration directory, updating the {@link ConfigurationAdmin}
 * service (supplied by OSGi) of any changes.
 */
public class FileSystemConfigurationMonitor
        implements DirectoryMonitorCallback {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(
                    FileSystemConfigurationMonitor.class);
    /**
     * Name of the property which holds the name of the file to which the
     * configuration object is persisted.
     */
    public static final String FILENAME_PROPERTY =
            "com.volantis.synergetics.cm.monitor.filename";

    /**
     * The directory in which all the configuration objects should be stored.
     * @todo change this to the correct path, ATM this is relative to the
     * @todo osgi/bundles dir.
     */
    public static final String CONFIG_DIRECTORY = "../../osgi/config";

    /**
     * The {@link org.osgi.service.cm.ConfigurationAdmin} service (provided by
     * OSGi) which will be updated with any changes to the configuration objects.
     */
    private ConfigurationAdmin configurationAdmin;

    /**
     * The {@link com.volantis.synergetics.directory.monitor.DirectoryMonitor}
     * service (provided by OSGi) that monitors the configuration directory
     * for changes.
     */
    private DirectoryMonitor monitor;
    
    private ComponentContext context;
    private File dir;

    // Javadoc inherited.
    public void beginChangeSet(File directory) {
        // No action required.
    }

    // Javadoc inherited.
    public void fileAdded(File directory, String filename) {
        updateConfiguration(directory, filename);
    }

    // Javadoc inherited.
    public void fileUpdated(File directory, String filename) {
        updateConfiguration(directory, filename);
    }

    // Javadoc inherited.
    public void fileRemoved(File directory, String filename) {

        Configuration configuration = getSingleConfiguration(
            "(" + FILENAME_PROPERTY + "=" + filename + ")",
            getServicePid(filename),
            getFactoryPid(directory));
        if (configuration != null) {
            try {
                configuration.delete();
            } catch (IOException e) {
                LOGGER.error("could-not-delete-configuration",
                        configuration.getPid(), e);
            }
        }
    }

    /**
     * Returns the factory PID to be used for the specified directory.
     *
     * <p>Returns null, if the given directory is the configuration directory,
     * otherwise returns the name of the directory .</p>
     *
     * @param directory the directory to check
     * @return the factory PID of the specified directory
     */
    private String getFactoryPid(final File directory) {
        String factoryPid;
        try {
            final File canonicalDirectory = directory.getCanonicalFile();
            if (canonicalDirectory.equals(dir.getCanonicalFile())) {
                factoryPid = null;
            } else {
                factoryPid = canonicalDirectory.getName();
            }
        } catch (IOException e) {
            // can't tell
            throw new UndeclaredThrowableException(e);
        }
        return factoryPid;
    }

    /**
     * Returns the service PID for the specified file name.
     *
     * @param filename the name of the file relative to the containing directory
     * (the name without the path of the parent dir)
     * @return the service PID
     */
    private String getServicePid(final String filename) {
        return filename;
    }

    // Javadoc inherited.
    public void endChangeSet(File directory) {
        // No action required.
    }

    /**
     * This should retrieve the configuration properties from the specified
     * file and update the appropriate {@link org.osgi.service.cm.Configuration}
     * stored in the {@link org.osgi.service.cm.ConfigurationAdmin} service.
     *
     * @param directory     in which the configuration file is stored
     * @param filename      name of the configuration property
     */
    private void updateConfiguration(File directory, String filename) {

        // Load the configuration properties from the file.
        File file = new File(directory, filename);
        if (file.exists()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                // Deliberately fail silently in this case, because there is
                // nothing useful we can do.
                LOGGER.error("could-not-load-properties",
                        file.getAbsolutePath(), e);
            }

            // Get the corresponding configuration from the ConfigurationAdmin
            // service and update it.
            Configuration configuration = getSingleConfiguration(
                "(" + FILENAME_PROPERTY + "=" + filename + ")",
                getServicePid(filename),
                getFactoryPid(directory));
            if (configuration != null) {
                try {
                    properties.put(FILENAME_PROPERTY, filename);
                    configuration.update(properties);
                } catch (IOException e) {
                    // Deliberately fail silently, but log the problem.
                    LOGGER.error("could-not-update-configuration",
                            configuration.getPid(), e);
                }
            }
        } else {
            // Deliberately fail silently, but log the problem.
            LOGGER.error("could-not-find-file",
                    new Object[]{filename, directory});
        }
    }

    /**
     * Returns the single configuration which matches the specified filter,
     * returning null if multiple matches are found.
     *
     * <p>Creates a new Configuration if the configurationAdmin doesn't have
     * configurations registered for the filter. ManagedService config is
     * created if the factory PID is null (using the specified PID) and
     * ManagedServiceFactory configuration is created if the factory is
     * non-null.</p>
     *
     * @param filter    with which to search the Configurations registered
     *                  with the ConfigurationAdmin service
     * @param pid       the PID to be used if a new service configuration needs
     *                  to be created
     * @param factoryPid the factory PID to be used if a new service factory
     *                  configuration needs to be created
     * @return Configuration the configuration object which matched the filter,
     * may be null if zero or multiple matches were found
     */
    private Configuration getSingleConfiguration(String filter, String pid,
                                                 String factoryPid) {
        Configuration configuration = null;
        try {
            Configuration[] configurations =
                    configurationAdmin.listConfigurations(filter);
            if (configurations == null) {
                if (factoryPid == null) {
                    configuration = configurationAdmin.getConfiguration(pid, null);
                } else {
                    configuration =
                        configurationAdmin.createFactoryConfiguration(factoryPid, null);
                }
            } else {
                int size = configurations.length;
                if (size == 0) {
                    // The service to which this configuration applies may not be
                    // currently registered with the ConfigurationAdmin service.
                    LOGGER.info("could-not-find-configuration", filter);
                    configuration = configurationAdmin.getConfiguration(pid);
                } else if (size == 1) {
                    configuration = configurations[0];
                } else {
                    // This is unlikely to be a good thing, however we can't do
                    // anything about it, so we just log it and return null.
                    LOGGER.warn("too-many-matching-configurations",
                            new Object[]{new Integer(size), filter, new Integer(1)});
                }
            }
        } catch (IOException e) {
            // Deliberately fail silently, but log the problem.
            LOGGER.error(e);
        } catch (InvalidSyntaxException e) {
            // Deliberately fail silently, but log the problem.
            LOGGER.error(e);
        }
        return configuration;
    }

    /**
     * Called by OSGi to unset the Directory monitor this ConfigurationMonitor
     * needs to run
     *
     * @param monitor the DirectoryMonitor
     */
    public void unsetDirectoryMonitor(DirectoryMonitor monitor) {
        unregisterDirectory();
        this.monitor = null;
    }

    /**
     * Called by OSGi to set the Directory monitor this ConfigurationMonitor
     * needs to run
     *
     * @param monitor the DirectoryMonitor
     */
    public void setDirectoryMonitor(DirectoryMonitor monitor) {
        this.monitor = monitor;
        registerDirectory();
    }

    /**
     * Registers the directory to monitor changes.
     */
    private void registerDirectory() {
        if (isReady()) {
            // Ensure that the configuration directory is monitored for changes.
            // This should cause the ConfigurationAdmin service to be updated with
            // all configuration information that has changed since this service
            // was last running.
            try {
                dir = new File(
                    context.getBundleContext().getProperty("volantis.watcher.dir"),
                    CONFIG_DIRECTORY);
                this.monitor.registerDirectory(dir, this, true, true);
            } catch (RegistrationException e) {
                throw new UndeclaredThrowableException(e);
            }
        }
    }

    /**
     * Unregisters the directory.
     */
    private void unregisterDirectory() {
        if (isReady()) {

            monitor.unregisterDirectory(dir);
        }
    }

    /**
     * Returns true iff DirectoryMonitor, ConfigurationAdmin and
     * ComponentContext are set to non-null values.
     */
    private boolean isReady() {
        return monitor != null && configurationAdmin != null && context != null;
    }

    /**
     * Called by OSGi to unset the ConfigurationAdmin this ConfigurationMonitor
     * needs to run
     *
     * @param configurationAdmin the ConfigurationAdmin
     */
    public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        unregisterDirectory();
        this.configurationAdmin = null;
    }

    /**
     * Called by OSGi to set the ConfigurationAdmin this ConfigurationMonitor
     * needs to run
     *
     * @param configurationAdmin the ConfigurationAdmin
     */
    public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
        registerDirectory();
    }

    /**
     * Called automatically by OSGi
     *
     * @param context the ComponentContext to use
     */
    protected void activate(ComponentContext context)
            throws RegistrationException {
        this.context = context;
        registerDirectory();
    }

    /**
     * Called automatically by OSGi when this Service Component is deactivated
     *
     * @param context
     */
    protected void deactivate(ComponentContext context) {
        unregisterDirectory();
        this.context = null;
    }
}
