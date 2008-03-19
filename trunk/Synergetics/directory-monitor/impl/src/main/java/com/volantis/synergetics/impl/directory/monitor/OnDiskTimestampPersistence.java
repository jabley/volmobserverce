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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A single timestamp persistence mechanism that uses an on disk file to
 * store timestamps from files. This means that files already noticed as
 * modified will not have a new added event filed when the directory they are
 * in is registered for monitoring a second time.
 */
public class OnDiskTimestampPersistence implements TimestampPersistence {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(OnDiskTimestampPersistence.class);

    /**
     * Filename of the timestamp storage file.
     */
    private static final String TIMESTAMP_FILENAME = "timestamps.properties";

    /**
     * This persistence mechanism uses a single properties file to store the
     * timestamps. Although the name is fixed, whether or not directories
     * should be included varies depending on the filter, so we cannot share it.
     */
    private FileFilter filter = new FileFilter() {
        public boolean accept(File pathname) {
            boolean result = true;
            if (TIMESTAMP_FILENAME.equals(pathname.getName())
                || (!recursive && pathname.isDirectory())) {
                result = false;
            }
            return result;
        }
    };

    /**
     * Directory in which the persisted timestamps are stored.
     */
    private final String dirName;

    /**
     * Determines whether directories should be included in this persistence
     * management or not.
     */
    private final boolean recursive;

    /**
     * Required constructor
     *
     * @param directoryName     the directory name to monitor
     * @param recursive         indicates whether child directories should
     *                          also be monitored
     */
    OnDiskTimestampPersistence(String directoryName, boolean recursive) {
        this.dirName = directoryName;
        this.recursive = recursive;
    }

    // Javadoc inherited
    public FileFilter getFileFilter() {
        return filter;
    }

    /**
     * Return the timestamps as a set of properties
     *
     * @return the timestamps as a set of properties
     */
    public Properties getTimestamps() {
        Properties timestamps = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(dirName, TIMESTAMP_FILENAME));
            timestamps.load(fis);
        } catch (FileNotFoundException e) {
            // the file may not exist yet or have been deleted
            LOGGER.info(e);
        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
        return timestamps;
    }

    /**
     * Store the timestamps
     *
     * @param timestamps timestamps to store
     */
    public void storeTimestamps(Properties timestamps) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(dirName, TIMESTAMP_FILENAME));
            timestamps.store(fos, "timestamps");
        } catch (FileNotFoundException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
    }
}
