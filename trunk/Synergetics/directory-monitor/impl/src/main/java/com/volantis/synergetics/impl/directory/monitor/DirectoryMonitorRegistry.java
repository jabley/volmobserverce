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

import com.volantis.synergetics.directory.monitor.DirectoryMonitorCallback;
import com.volantis.synergetics.directory.monitor.RegistrationException;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The registry for directoy monitoring.
 */
public class DirectoryMonitorRegistry {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DirectoryMonitorRegistry.class);

    /**
     * All locking should be performed on this object
     */
    private final Object lock = new Object();

    /**
     * A list containing {@link MonitoredDirectoryInfo} objects
     */
    private final List monitoredDirectories = new LinkedList();

    /**
     * The timer that will invoke directory scans.
     */
    private Timer timer = new Timer(true);

    /**
     * The {@link TimerTask} that periodically checks the directories for
     * changes
     */
    private DirectoryScanner scannerTask = new DirectoryScanner(this);

    /**
     * If registerWithTimer is true then the constructed TimerTask will
     * be registered with the Timer. If false it will not be (good for testing)
     */
    DirectoryMonitorRegistry(boolean registerWithTimer) {
        if (registerWithTimer) {
            timer.schedule(scannerTask, 10000, 5000);
        }
    }

    /**
     * Used for test only
     * @return the thread scanning in the background
     */
    TimerTask getScanner() {
        return scannerTask;
    }

    /**
     * Return a clone of the monitored directories that is safe for the
     * scanner thread to iterate over.
     *
     * @return a List containig MonitoredDirectoryInfo objects
     */
    List cloneMonitoredDirectories() {
        synchronized(lock) {
            return new LinkedList(monitoredDirectories);
        }
    }

    /**
     * Register a directory, with the callback and bundle specified by the
     * parent {@link MonitoredDirectoryInfo}
     *
     * @param canonicalDirPath  the directory to register
     * @param parent            parent monitored directory
     *
     * @throws RegistrationException if the file is not a directory, does not
     * exist, is not writable or has already been registered.
     */
     void register(String canonicalDirPath, MonitoredDirectoryInfo parent)
            throws RegistrationException {
         register(parent.getBundleId(), canonicalDirPath, parent.getCallback(),
                 parent.getPersistenceFactory(), parent.isRecursive());
     }

    /**
     * Register a directory and its callback with the specified bundle.
     *
     * @param bundleId              the id of the bundle against which to
     *                              register the directory
     * @param canonicalDirPath      the directory to register
     * @param callback              to invoke when changes occur in that
     *                              directory.
     * @param persistenceFactory    which will be used to create the
     *                              TimestampPersistence for this directory and
     *                              any child monitored directories
     * @param recursive             true if the specified directories child
     *                              directories should also be monitored,
     *                              false otherwise
     *
     * @throws RegistrationException if the file is not a directory, does not
     * exist, is not writable or has already been registered.
     */
    void register(long bundleId,
                  String canonicalDirPath,
                  DirectoryMonitorCallback callback,
                  TimestampPersistenceFactory persistenceFactory,
                  boolean recursive) throws RegistrationException {
        if (null == callback) {
            throw new RegistrationException("argument-must-not-be",
                                            new Object[]{"callback", "null"});
        }
        synchronized(lock) {
            boolean found = isRegistered(canonicalDirPath);

            // we didn't find the filename so we can add it
            if (found) {
                throw new RegistrationException(
                    "directory-already-registered", canonicalDirPath);
            } else {
                final MonitoredDirectoryInfo mdi = new MonitoredDirectoryInfo(
                        canonicalDirPath, bundleId, callback,
                        persistenceFactory, recursive);
                monitoredDirectories.add(mdi);
            }
        }
    }

    /**
     * Returns true iff the specified canonical directory path is among the
     * monitored directories.
     * 
     * @param canonicalDirPath the path to check
     * @return true iff the directory is already registered
     */
    boolean isRegistered(String canonicalDirPath) {
        synchronized (lock) {
            boolean found = false;
            Iterator it = monitoredDirectories.iterator();
            while (it.hasNext() && !found) {
                MonitoredDirectoryInfo mi = (MonitoredDirectoryInfo) it.next();
                if (mi.getCanonicalDirname().equals(canonicalDirPath)) {
                    found = true;
                }
            }
            return found;
        }
    }

    /**
     * Unregister the specified directory associated with the specified bundle.
     * If the specified directory was not registered by the specfied bundle then
     * it will not be removed. No error will be thrown
     *
     * @param bundleId the bundle id
     * @param canonicalDirPath the directory associtated with that bundle that
     * should be removed
     */
    void unregister(long bundleId, String canonicalDirPath) {
        synchronized (lock) {
            boolean found = false;
            Iterator it = monitoredDirectories.iterator();
            while (it.hasNext() && !found) {
                MonitoredDirectoryInfo mi = (MonitoredDirectoryInfo) it.next();
                if (mi.getCanonicalDirname().equals(canonicalDirPath)) {
                    if (mi.getBundleId() != bundleId) {
                        LOGGER.warn(
                            "could-not-unregister-directory", canonicalDirPath);
                    } else {
                        it.remove();
                        found = true;
                    }
                }

            }
        }
    }

    /**
     * Unregister all directories associated with the specified bundle
     *
     * @param bundleId the bundle whose files should be removed
     */
    void unregister(long bundleId) {
        synchronized(lock) {
            Iterator it = monitoredDirectories.iterator();
            while (it.hasNext()) {
                MonitoredDirectoryInfo mi = (MonitoredDirectoryInfo) it.next();
                if (mi.getBundleId() == bundleId) {
                    it.remove();
                }
            }
        }
    }
}
