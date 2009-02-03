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

import java.util.Map;

/**
 * Start a bundle.
 */
public class StartAction
        extends Action {

    /**
     * The bundle to start.
     */
    private final Bundle bundle;

    /**
     * The last modified time on the watched bundle.
     */
    private final long watchedLastModified;

    /**
     * Initialise.
     *
     * @param bundle The bundle to start.
     * @param watchedLastModified
     */
    public StartAction(Bundle bundle, long watchedLastModified) {
        super(bundle.getLocation());
        this.bundle = bundle;
        this.watchedLastModified = watchedLastModified;
    }

    // Javadoc inherited.
    public Bundle takeAction(Map failedActions) {
        try {
            bundle.start();
            report("Started");
        } catch (Throwable t) {
            handleException("Cannot start '" + bundle.getLocation() + "'", t);
            failedActions.put(location, new Long(watchedLastModified));
            return null;
        }

        return bundle;
    }
}
