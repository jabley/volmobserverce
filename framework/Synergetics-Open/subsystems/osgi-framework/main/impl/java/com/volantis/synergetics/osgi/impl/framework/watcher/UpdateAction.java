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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi.impl.framework.watcher;

import org.osgi.framework.Bundle;

import java.io.File;
import java.util.Map;

/**
 * Update a bundle.
 *
 * <p>Taken when an installed bundle and a file exist and the file has been
 * modified since the last time the bundle was installed / updated as
 * indicated by the time stamp file.</p>
 */
public class UpdateAction
        extends Action {

    /**
     * The last modified time on the watched bundle.
     */
    private final long watchedLastModified;

    /**
     * The time stamp file to use to record when the bundle was updated.
     */
    private final File timeStampFile;

    /**
     * The bundle to update.
     */
    private final Bundle bundle;

    /**
     * Initialise.
     *
     * @param bundle              The bundle to update.
     * @param watchedLastModified The last modified time on the watched bundle.
     * @param timeStampFile       The time stamp file to use to record when the
     *                            bundle was updated.
     */
    public UpdateAction(
            Bundle bundle, long watchedLastModified, File timeStampFile) {
        super(bundle.getLocation());
        this.bundle = bundle;
        this.watchedLastModified = watchedLastModified;
        this.timeStampFile = timeStampFile;
    }

    // Javadoc inherited.
    public Bundle takeAction(Map failedActions) {

        try {
            bundle.update();
            if (timeStampFile.exists()) {
                timeStampFile.setLastModified(watchedLastModified);
            } else {
                timeStampFile.createNewFile();
            }
            report("Updated");
        } catch (Throwable e) {
            handleException("Could not update '" +
                    bundle.getLocation() + "'", e);
            failedActions.put(location, new Long(watchedLastModified));
            return null;
        }

        return bundle;
    }

    public long getWatchedLastModified() {
        return watchedLastModified;
    }
}
