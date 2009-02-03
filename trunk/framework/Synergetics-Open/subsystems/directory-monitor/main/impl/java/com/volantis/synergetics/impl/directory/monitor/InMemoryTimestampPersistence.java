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

import java.io.File;
import java.io.FileFilter;
import java.util.Properties;

/**
 * An in memory timestamp persistence mechanism. This "forgets" all
 * timestamped file when the VM is shutdown or when the directory it is
 * the persistence mechanism for is unregistered.
 *
 * <p>
 * This means that all files in the directory have an associated ADD event
 * fired when the directory is registered.
 * </p>
 */
public class InMemoryTimestampPersistence implements TimestampPersistence {

    /**
     * In memory timestamp has no on disk representation so the filter
     * always returns true for files. However whether or not directories are
     * included varies for each filter, so it cannot be shared.
     */
    private FileFilter filter = new FileFilter() {
        public boolean accept(File pathname) {
            boolean result = true;
            if (!recursive && pathname.isDirectory()) {
                result = false;
            }
            return result;
        }
    };

    /**
     * Used to store the currently recognised timestamps.
     */
    private final Properties timestamps = new Properties();

    /**
     * Determines whether directories should be included in this persistence
     * management or not.
     */
    private boolean recursive;

    InMemoryTimestampPersistence(String dirname, boolean recursive) {
        this.recursive = recursive;
    }
    
    // Javadoc inherited
    public Properties getTimestamps() {
        Properties p = new Properties();
        p.putAll(timestamps);
        return p;
    }

    // Javadoc inherited
    public FileFilter getFileFilter() {
        return filter;
    }

    // Javadoc inherited
    public void storeTimestamps(Properties timestamps) {
        this.timestamps.clear();
        this.timestamps.putAll(timestamps);
    }
}
