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
import org.osgi.framework.BundleException;

import java.io.File;
import java.util.Map;

/**
 * Uninstall a bundle.
 *
 * <p>Taken when there is an installed bundle but no matching file at the
 * bundle location.</p>
 */
public class UninstallAction
        extends Action {

    /**
     * The bundle to uninstall.
     */
    private final Bundle bundle;

    /**
     * The time stamp file for the installed bundle; this will be deleted.
     */
    private final File timeStampFile;

    /**
     * Initialise.
     *
     * @param bundle        The bundle to uninstall.
     * @param timeStampFile The time stamp file for the installed bundle; this
     *                      will be deleted.
     */
    public UninstallAction(Bundle bundle, File timeStampFile) {
        super(bundle.getLocation());
        this.bundle = bundle;
        this.timeStampFile = timeStampFile;
    }

    // Javadoc inherited.
    public Bundle takeAction(Map failedActions) {
        try {
            bundle.uninstall();
            report("Uninstalled");
            failedActions.remove(location);
            timeStampFile.delete();
        } catch (BundleException e) {
            handleException("Cannot uninstall '" + bundle.getLocation() + "'",
                    e);
        }

        return bundle;
    }
}
