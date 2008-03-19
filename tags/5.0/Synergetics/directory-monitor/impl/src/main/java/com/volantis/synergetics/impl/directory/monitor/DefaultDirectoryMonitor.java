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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.impl.directory.monitor;

import com.volantis.synergetics.directory.monitor.DirectoryMonitor;
import com.volantis.synergetics.directory.monitor.DirectoryMonitorCallback;
import com.volantis.synergetics.directory.monitor.RegistrationException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.io.File;
import java.io.IOException;

/**
 * A default implementation of the directory monitor
 */
public class DefaultDirectoryMonitor implements DirectoryMonitor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultDirectoryMonitor.class);

    /**
     * The centralized registry that holds information about all monitored
     * directories
     */
    private final DirectoryMonitorRegistry registry;

    /**
     * The bundle that caused this instance to be created
     */
    private final long bundleId;

    /**
     * Initialize with a bundle, and registry
     *
     * @param bundleId the id of the bundle this Directory monitor is for
     * @param registry the registry to register directories in
     */
    DefaultDirectoryMonitor(long bundleId, DirectoryMonitorRegistry registry) {
        this.registry = registry;
        this.bundleId = bundleId;
    }

    // Javadoc inherited
    public void registerDirectory(File dir,
                                  DirectoryMonitorCallback callback,
                                  boolean persistentTimestamps) 
	throws RegistrationException {
	// monitor the directory without monitoring any children
	registerDirectory(dir, callback, persistentTimestamps, false);
    }

    // Javadoc inherited
    public void registerDirectory(File dir,
                                  DirectoryMonitorCallback callback,
                                  boolean persistentTimestamps,
                                  boolean recursive)
        throws RegistrationException {

        if (!dir.exists()) {
            throw new RegistrationException("directory-does-not-exist", dir);
        }
        if (dir.isFile()) {
            throw new RegistrationException("file-is-not-directory", dir);
        }
        if (!dir.canWrite()) {
            throw new RegistrationException("directory-is-not-writable", dir);
        }
        String dirName = null;
        try {
            // get the canonical path to avoid duplicates. Note we store
            // String path names rather then files to avoid issues with
            // renames/state changes in File objects
            dirName = dir.getCanonicalPath();
        } catch (IOException e) {
            throw new RegistrationException(e);
        }

        TimestampPersistenceFactory tpf = null;
        if (persistentTimestamps) {
            tpf = new OnDiskTimestampPersistenceFactory();
        } else {
            tpf = new InMemoryTimestampPersistenceFactory();
        }

        registry.register(bundleId, dirName, callback, tpf, recursive);
    }

    // Javadoc inherited
    public void unregisterDirectory(File dir) {
        String dirName = null;
        try {
            // get the canonical path to avoid duplicates. Note we store
            // String path names rather then files to avoid issues with
            // renames/state changes in File objects
            dirName = dir.getCanonicalPath();
        } catch (IOException e) {
            LOGGER.warn("could-not-unregister-directory", dir, e);
        }

        registry.unregister(bundleId, dirName);
    }
}
