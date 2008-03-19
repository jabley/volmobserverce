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

/**
 * Store information about a monitored directory. This class is Immutable and
 * must stay that way.
 */
class MonitoredDirectoryInfo {

    /**
     * The canonical filename of the directory being monitored
     */
    private final String canonicalDirname;

    /**
     * The id of the bundle that registered this directory for monitoring
     */
    private final long bundleId;

    /**
     * The callback to use to notify of changes in this directory
     */
    private final DirectoryMonitorCallback callback;

    /**
     * The timestamp persistence mechanism to use for this directory
     */
    private final TimestampPersistence persistence;

    /**
     * The factory which will be used to create {@link TimestampPersistence}
     * objects for this monitored directory (and its children if recursive). 
     */
    private final TimestampPersistenceFactory persistenceFactory;

    /**
     * Indicates whether sub directories of this monitored directory should
     * also be monitored for changes.
     */
    private boolean recursive = false;

    /**
     * This constructor is the only means to set the member variables of this
     * class
     *
     * @param dirname               the canonical directory name
     * @param bundleId              the ID of the bundle that requested the monitoring
     * @param callback              the callback to invoke when changes occur
     * @param persistenceFactory    which creates the persistence layer to use
     * @param recursive             true if sub directories of this monitored
     *                              directory should also be monitored, false
     *                              otherwise.
     */
    MonitoredDirectoryInfo(String dirname,
                           long bundleId,
                           DirectoryMonitorCallback callback,
                           TimestampPersistenceFactory persistenceFactory,
                           boolean recursive) {
        this.canonicalDirname = dirname;
        this.bundleId = bundleId;
        this.callback = callback;
        this.persistenceFactory = persistenceFactory;
        this.persistence = persistenceFactory.
                createTimestampPersistence(dirname, recursive);
        this.recursive = recursive;
    }

    // Javadoc unnecessary
    String getCanonicalDirname() {
        return canonicalDirname;
    }

    // Javadoc unnecessary
    long getBundleId() {
        return bundleId;
    }

    // Javadoc unnecessary
    DirectoryMonitorCallback getCallback() {
        return callback;
    }

    // Javadoc unnecessary
    TimestampPersistence getPersistence() {
        return persistence;
    }

    // Javadoc unnecessary.
    public boolean isRecursive() {
        return recursive;
    }

    // Javadoc unnecessary.
    public TimestampPersistenceFactory getPersistenceFactory() {
        return persistenceFactory;
    }
}
