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
 * Provides a directory monitoring service that will provide notifications
 * when files in a monitored directory change.
 *
 * <p>
 * <strong>Warning: This is a service interface but is not ment for user
 * implementation.
 * </p>
 *
 * @mock.generate
 */
public interface DirectoryMonitor {

    /**
     * Register a directory to be monitored. Changes to files in the directory
     * will cause the DirectoryMonitorCallback to be fired.
     *
     * Note that a single directory can only have one callback registered to it
     *
     * @param dir the directory to monitor
     * @param callback the callback used to signal changes to files in the
     * specified directory.
     * @param persistentTimestamps if true then persistent timestamps will
     * be used. Otherwise transient timestamp storeage will be used.
     * @param recursive if true then this directory and all of its child
     * directories will be monitored, if false then only files in this
     * directory will be monitored.
     * @throws RegistrationException if the file is not a directory, does not
     * exist, is not writable or has already been registered.
     */
    void registerDirectory(File dir,
                           DirectoryMonitorCallback callback,
                           boolean persistentTimestamps,
                           boolean recursive) throws RegistrationException; 

   /**
     * Register a directory to be monitored. Changes to files in the directory
     * will cause the DirectoryMonitorCallback to be fired.
     *
     * Note that a single directory can only have one callback registered to it
     *
     * @param dir the directory to monitor
     * @param callback the callback used to signal changes to files in the
     * specified directory.
     * @param persistentTimestamps if true then persistent timestamps will
     * be used. Otherwise transient timestamp storeage will be used.
     * @throws RegistrationException if the file is not a directory, does not
     * exist, is not writable or has already been registered.
     */
    void registerDirectory(File dir,
                           DirectoryMonitorCallback callback,
                           boolean persistentTimestamps) throws RegistrationException; 

    /**
     * Unregister interest in the specified directory. This always succeeds
     *
     * @param dir the directoy to unregister interest in
     */
    void unregisterDirectory(File dir);

}
