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

import java.io.FileFilter;
import java.util.Properties;

/**
 * Simple interface for handling timestamp persistence.
 *
 * All implementations of this class MUST implement a constructor that take
 * a single String as an argument. This argument is the canonical filename of
 * the directory to monitor.
 *
 * @mock.generate
 */
public interface TimestampPersistence {

    /**
     * Return a FileFilter that can be used to exclude any files used by this
     * timestamp persistence mechansim from showing up in directory listings.
     * In general this should filter out directories
     *
     * @return a FileFilter that excludes any files that this persistence
     * mechanism uses.
     */
    public FileFilter getFileFilter();

    /**
     * Return the filename/timestamp pairs from the timestamp persistence
     *
     * @return a Properties containing filenames (String) mapped to
     * timestamps (String). The timestamps are String representations of the
     * long time in ms since 1970. See {@link java.io.File#lastModified()} for
     * more information on the format.
     */
    public Properties getTimestamps();

    /**
     * Store the timestamps defined in the specified properties object. The
     * properties object should map String filenames (the filename relative to
     * the directory being monitored) to Timestamps. The timestamps are String
     * representations of the long time in ms since 1970. See
     * {@link java.io.File#lastModified()} for
     * more information on the format.
     *
     * @param timestamps timestamps to store
     */
    public void storeTimestamps(Properties timestamps);

}
