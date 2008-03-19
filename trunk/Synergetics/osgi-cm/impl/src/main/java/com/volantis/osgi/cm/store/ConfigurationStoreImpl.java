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

package com.volantis.osgi.cm.store;

import com.volantis.osgi.cm.ConfigurationImpl;
import com.volantis.osgi.cm.FrameworkConstants;
import com.volantis.osgi.cm.InternalConfiguration;
import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import com.volantis.osgi.cm.util.ExtendedDictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Implementation of {@link ConfigurationStore}.
 *
 * <p>Persists the configuration as a standard {@link Properties} file. This
 * does not currently support any property types other than strings.</p>
 */
public class ConfigurationStoreImpl
        implements ConfigurationStore {

    /**
     * Special property that is used to record whether the configuration has
     * been initialized (i.e. has had any properties set).
     */
    private static final String INITIALIZED = "/initialized";

    /**
     * Special property that is used to record the bundle location to which the
     * configuration has been explicitly bound.
     */
    private static final String BUNDLE_LOCATION = "/bundleLocation";

    /**
     * Prefix of the pids automatically generated for the factory
     * configurations.
     */
    private static final String GENERATED_PID_PREFIX =
            "com.volantis.osgi.cm.factory-pid.";

    /**
     * The file manager that handles the creation and deletion of the files.
     */
    private final FileManager fileManager;

    /**
     * Initialise.
     *
     * @param fileManager The file manager.
     */
    public ConfigurationStoreImpl(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // Javadoc inherited.
    public InternalConfiguration[] load() throws IOException {

        List files = fileManager.listFiles();
        List configurations = new ArrayList();
        for (Iterator i = files.iterator(); i.hasNext();) {
            File file = (File) i.next();

            InternalConfiguration configuration =
                    createConfigurationFromFile(file);
            if (configuration != null) {
                configurations.add(configuration);
            }
        }

        InternalConfiguration[] array =
                new InternalConfiguration[configurations.size()];
        configurations.toArray(array);
        return array;
    }

    /**
     * Create a new configuration from a file.
     *
     * @param file The file.
     * @return The new configuration.
     * @throws IOException If there was a problem accessing the file.
     */
    private InternalConfiguration createConfigurationFromFile(File file)
            throws IOException {

        Properties properties = new Properties();

        InputStream is = new FileInputStream(file);
        try {
            properties.load(is);
        } catch (IOException e) {
            // Discard as it must be corrupt in some way.
            file.delete();
        } finally {
            is.close();
        }

        // Check to see whether the configuration has been initialized, if
        // it has not then don't bother creating a configuration and just
        // delete the file.
        Object removed = properties.remove(INITIALIZED);
        if (removed == null) {
            // Delete the file.
            file.delete();
            return null;
        }

        // Process the special properties.
        String bundleLocation = (String) properties.remove(BUNDLE_LOCATION);
        String pid = (String) properties.remove(FrameworkConstants.SERVICE_PID);
        String factoryPid = (String) properties
                .remove(FrameworkConstants.SERVICE_FACTORYPID);

        CaseInsensitiveDictionary dictionary =
                new CaseInsensitiveDictionary(properties);

        return new ConfigurationImpl(pid, factoryPid, file, bundleLocation,
                dictionary);
    }

    // Javadoc inherited.
    public void update(InternalConfiguration configuration) throws IOException {

        File persistentFile = configuration.getPersistentFile();
        Properties properties = new Properties();

        // If the storable has properties then the configuration has been
        // initialized, otherwise it has not.
        ExtendedDictionary dictionary = (ExtendedDictionary)
                configuration.getProperties();
        if (dictionary == null) {
            // Don't do anything.
        } else {
            // Record that the property was initialized.
            properties.put(INITIALIZED, "");
            properties.putAll(dictionary);
        }

        // Only persist the specified location, not the one to which it is
        // currently bound.
        String bundleLocation = configuration.getSpecifiedLocation();
        if (bundleLocation != null) {
            properties.put(BUNDLE_LOCATION, bundleLocation);
        }

        // Store the properties.
        OutputStream os = new FileOutputStream(persistentFile);
        try {
            properties.store(os, null);
        } finally {
            os.close();
        }
    }

    // Javadoc inherited.
    public void remove(InternalConfiguration configuration)
            throws IOException {

        File persistentFile = configuration.getPersistentFile();
        fileManager.releaseFile(persistentFile);
    }

    // Javadoc inherited.
    public InternalConfiguration createFactoryConfiguration(
            String factoryPid, String bundleLocation)
            throws IOException {

        // Allocate a file for the configuration.
        File file = fileManager.allocateFile();

        // Calculate a relative path for the file, and replace / with . to
        // make it usable as a valid PID.
        String relativePath = fileManager.getRelativePath(file);
        String suffix = relativePath.replace('/', '.');
        String pid = GENERATED_PID_PREFIX + suffix;

        return new ConfigurationImpl(
                pid, factoryPid, file, bundleLocation, null);
    }

    // Javadoc inherited.
    public InternalConfiguration createConfiguration(
            String pid, String bundleLocation) throws IOException {

        // Allocate a file for the configuration.
        File file = fileManager.allocateFile();

        return new ConfigurationImpl(
                pid, null, file, bundleLocation, null);
    }
}
