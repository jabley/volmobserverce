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
import org.osgi.framework.BundleContext;

import java.io.File;
import java.util.Map;

/**
 * Install a new bundle.
 *
 * <p>Taken when a new file is found in the watched directory without a
 * matching bundle installed.</p>
 */
public class InstallAction
        extends Action {

    /**
     * The context that will be performing the install.
     */
    private final BundleContext context;

    /**
     * The last modified time on the watched bundle.
     */
    private final long watchedLastModified;

    /**
     * The time stamp file to use to record when the bundle was installed.
     */
    private final File timeStampFile;

    /**
     * Initialise.
     *
     * @param context             The context that will be performing the
     *                            install.
     * @param location            The location (URL as string) of the bundle.
     * @param watchedLastModified The last modified time on the watched bundle.
     * @param timeStampFile       The time stamp file to use to record when the
     *                            bundle was installed.
     */
    public InstallAction(
            BundleContext context, String location, long watchedLastModified,
            File timeStampFile) {
        super(location);
        this.context = context;
        this.watchedLastModified = watchedLastModified;
        this.timeStampFile = timeStampFile;
    }

    // Javadoc inherited.
    public Bundle takeAction(Map failedActions) {
        Bundle bundle = null;
        try {
            bundle = context.installBundle(location);

            if (timeStampFile.exists()) {
                timeStampFile.setLastModified(watchedLastModified);
            } else {
                timeStampFile.createNewFile();
            }
            report("Installed");
        } catch (Throwable t) {
            handleException("Cannot install '" + location + "'", t);
            failedActions.put(location, new Long(watchedLastModified));
            return null;
        }
        return bundle;
    }

    public long getWatchedLastModified() {
        return watchedLastModified;
    }
}
