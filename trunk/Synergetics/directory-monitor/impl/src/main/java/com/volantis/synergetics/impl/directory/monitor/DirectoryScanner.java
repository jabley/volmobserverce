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

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.directory.monitor.RegistrationException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * This class implements the scanning mechanism used to monitor directories
 */
public class DirectoryScanner extends TimerTask {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DirectoryScanner.class);

    /**
     * the registry to use
     */
    private final DirectoryMonitorRegistry registry;

    /**
     * Construct using a registry
     *
     * @param registry the registry
     */
    DirectoryScanner(DirectoryMonitorRegistry registry) {
        this.registry = registry;
    }

    /**
     * Run once and exit.
     */
    public void run() {
        try{
            // this method clones a list of the monitored directories
            // to allow safe unsynchronized access to it via iterators.
            List files = registry.cloneMonitoredDirectories();
            Iterator it = files.iterator();
            while (it.hasNext()) {

                try {
                    MonitoredDirectoryInfo mi =
                        (MonitoredDirectoryInfo) it.next();
                    processDirectory(mi);
                }catch (Throwable t) {
                    // catch all exceptions
                    LOGGER.error(t);
                }
            }
        } catch (Throwable t) {
            // yup we really do want to catch all exceptions
            LOGGER.error(t);
        }
    }

    /**
     * Process a particular directory to fire events to the callback and to
     * update the timestamp persistence mechanism being used.
     *
     * @param mi the container holding the information about the direcctory
     * being monitored.
     */
    private void processDirectory(MonitoredDirectoryInfo mi) {
        File dir = new File(mi.getCanonicalDirname());
        if (!dir.exists()) {
            LOGGER.warn("directory-has-been-removed", mi.getCanonicalDirname());
        }
        TimestampPersistence persistence = mi.getPersistence();
        // This maps the filenames to the String timestamps (as a long dated
        // from the epoch 1970. See {@link File.lastModified} for details.)
        Properties timestamps = persistence.getTimestamps();
        File[] files = dir.listFiles(persistence.getFileFilter());
        // generate a map of the entries in the directory
        Map dirFiles = new HashMap();
        for (int i=0; i<files.length; i++) {
            // map the filenames to the Long timestamps.
            dirFiles.put(files[i].getName(), new Long(files[i].lastModified()));
        }
        try {

            boolean updateStarted = false;

            // now iterate over the entries in the directory and see if they exist
            // in the timestamps.
            Iterator it = dirFiles.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry dirEntry = (Map.Entry) it.next();
                String dirFileName = (String) dirEntry.getKey();
                Long dirFileTimestamp = (Long) dirEntry.getValue();
                // exists in the directory and the timestamps
                final File file = new File(dir, dirFileName);
                final boolean isDirectory = file.isDirectory();
                if (!isDirectory && timestamps.containsKey(dirFileName)) {
                    String ts = timestamps.getProperty(dirFileName);
                    Long lts = new Long(ts);
                    if (lts.compareTo(dirFileTimestamp) < 0) {
                        // updated
                        if (!updateStarted) {
                            mi.getCallback().beginChangeSet(dir);
                            updateStarted = true;
                        }
                        try {
                            // fire the event and update the timestamp list
                            mi.getCallback().fileUpdated(dir, dirFileName);
                        } catch (Throwable t) {
                            // catch all exceptions, so the other files are
                            // checked as well
                            LOGGER.error(t);
                        }
                        timestamps.setProperty(
                            dirFileName, dirFileTimestamp.toString());
                    }
                } else {
                    try {
                        final String canonicalPath = file.getCanonicalPath();
                        if (!isDirectory ||
                                !registry.isRegistered(canonicalPath)) {
                            // exists only in the directory.
                            // added
                            if (!updateStarted) {
                                mi.getCallback().beginChangeSet(dir);
                                updateStarted = true;
                            }
                            try {
                                // fire the event and update the timestamp list
                                fileAdded(mi, dir, dirFileName);
                            } catch (Throwable t) {
                                // catch all exceptions, so the other files are
                                // checked as well
                                LOGGER.error(t);
                            }
                        }
                    } catch (IOException e) {
                        // this should never happen, as the directory was added
                        // to the registry using its canonical name..
                        LOGGER.error(e);
                    }
                    timestamps.setProperty(
                        dirFileName, dirFileTimestamp.toString());
                }
            }

            // Iterate over the timestamps to see which files have been deleted
            // from the directory.
            it = timestamps.keySet().iterator();
            while (it.hasNext()) {
                String tsEntryName = (String) it.next();
                if (!dirFiles.containsKey(tsEntryName)) {
                    // file has been deleted from directory.
                    if (!updateStarted) {
                        mi.getCallback().beginChangeSet(dir);
                        updateStarted = true;
                    }
                    try {
                        fileRemoved(mi, dir, tsEntryName);
                    } catch (Throwable t) {
                        // catch all exceptions, so the other files are processed as
                        // well
                        LOGGER.error(t);
                    }
                    // this iterator should allow deletes
                    it.remove();
                }
            }

            mi.getCallback().endChangeSet(dir);
        } finally {
            // store the new timestamp selection.
            persistence.storeTimestamps(timestamps);
        }
    }

    /**
     * If the file that has been added corresponds to a directory, then
     * register it to be monitored if its parent specified recursive
     * monitoring. If it simply references a file, then just alert the
     * registered {@link com.volantis.synergetics.directory.monitor.DirectoryMonitorCallback}.
     *
     * @param mi        describes the parent directory of the file that has
     *                  been added
     * @param dir       canonical name of the containing directory
     * @param fileName  name of the file or directory that has been added
     */
    private void fileAdded(MonitoredDirectoryInfo mi,
                           File dir,
                           String fileName ) {
        try {
            File file = new File(dir, fileName);
            // If a directory has been added and its parent specified recursive
            // monitoring, then register it for monitoring.
            if (file.isDirectory() && mi.isRecursive()) {
                final String canonicalPath = file.getCanonicalPath();
                registry.register(canonicalPath,
                    new MonitoredDirectoryInfo(canonicalPath, mi.getBundleId(),
                        mi.getCallback(), mi.getPersistenceFactory(),
                        mi.isRecursive()));
            } else {
                // Otherwise just let the containing directory's registered
                // callback know.
                mi.getCallback().fileAdded(dir, fileName);
            }
        } catch (IOException e) {
            // Silently fail - can't handle this any better.
            LOGGER.error(e);
        } catch (RegistrationException e) {
            // Silently fail - can't handle this any better.
            LOGGER.error(e);
        }
    }

    /**
     * If the file that has been removed corresponds to a directory, then
     * unregister it. If it simply references a file, then just alert the
     * registered
     * {@link com.volantis.synergetics.directory.monitor.DirectoryMonitorCallback}.
     *
     * @param mi        describes the parent directory of the file that has
     *                  been removed
     * @param dir       canonical name of the containing directory
     * @param fileName  name of the file or directory that has been added
     */
    private void fileRemoved(MonitoredDirectoryInfo mi,
                             File dir,
                             String fileName) {
        final File file = new File(dir, fileName);
        final boolean isDirectory = file.isDirectory();
        if (isDirectory) {
            try {
                registry.unregister(mi.getBundleId(), file.getCanonicalPath());
            } catch (IOException e) {
                // this should never happen, as the directory was added
                // to the registry using its canonical name..
                LOGGER.error(e);
            }
        } else {
            mi.getCallback().fileRemoved(dir, fileName);
        }
    }
}
