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
package com.volantis.synergetics.directory.monitor;

import java.io.File;

/**
 * This interface allows notification of modification to files in
 * a directory
 *
 * <p>
 * <strong>Warning: This is an interface for implementation by user
 * code, however, we reserve the right to extend this in future. Therefore,
 * implementors SHOULD extend the provided implementation class so that they
 * will be compatible with those future changes. Implementors that do not
 * extend that class are assumed to have accepted responsibility for updating
 * their implementation when this interface changes.</strong>
 * </p>
 *
 * @mock.generate
 */
public interface DirectoryMonitorCallback {

    /**
     * This method will be called to indicate that a set of changes has occured
     * in the directory being monitored and that notification will follow.
     * <p>
     * The Directory may not exist or may have become unwritable prior to this
     * method being called.
     * </p>
     * <p>
     * The callback will occur on an arbitrary thread so care must be taken
     * to ensure any interaction it has are threadsafe
     * </p>
     *
     * @param directory the directory being monitored
     */
    void beginChangeSet(File directory);

    /**
     * Called when a file is added in the monitored directory.
     * <p>
     * The Directory may not exist or may have become unwritable prior to this
     * method being called.
     * </p>
     * <p>
     * The file specified in this method may no longer exist or may not be
     * readable/writable by the time this method is called
     * </p>
     * <p>
     * The callback will occur on an arbitrary thread so care must be taken
     * to ensure any interaction it has are threadsafe
     * </p>
     *
     * @param directory the directory being monitored
     * @param filename the name of the file in that directory which changed
     */
    void fileAdded(File directory, String filename);

    /**
     * Called when a file is updated in the monitored directory
     * <p>
     * The Directory may not exist or may have become unwritable prior to this
     * method being called.
     * </p>
     * <p>
     * The file specified in this method may no longer exist or may not be
     * readable/writable by the time this method is called
     * </p>
     * <p>
     * The callback will occur on an arbitrary thread so care must be taken
     * to ensure any interaction it has are threadsafe
     * </p>
     *
     * @param directory the directory being monitored
     * @param filename the name of the file that was modified
     */
    void fileUpdated(File directory, String filename);

    /**
     * Called when a file is removed from the directory being monitored
     * <p>
     * The Directory may not exist or may have become unwritable prior to this
     * method being called.
     * </p>
     * <p>
     * The file specified in this method may no longer exist or may not be
     * readable/writable by the time this method is called
     * </p>
     * <p>
     * The callback will occur on an arbitrary thread so care must be taken
     * to ensure any interaction it has are threadsafe
     * </p>
     *
     * @param directory the directory being monitored
     * @param filename the name of the file that was removed.
     */
    void fileRemoved(File directory, String filename);

    /**
     * This method will be called that a set of change notifications has been
     * completed.
     * <p>
     * The Directory may not exist or may have become unwritable prior to this
     * method being called.
     * </p>
     * <p>
     * The callback will occur on an arbitrary thread so care must be taken
     * to ensure any interaction it has are threadsafe
     * </p>
     * 
     * @param directory the directory being monitored.
     */
    void endChangeSet(File directory);
}
