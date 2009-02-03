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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.File;
import java.io.IOException;

/**
 * The activator for the watcher.
 *
 * <p>This is implemented as a bundle activator as the intent is that in future
 * this could become a bundle too but right now it is not.</p>
 */
public class Activator
        implements BundleActivator {

    /**
     * The watcher.
     */
    private Watcher watcher;

    // Javadoc inherited.
    public void start(BundleContext context) throws Exception {

        String dirName = context.getProperty("volantis.watcher.dir");
        if (dirName == null) {
            return;
        }

        File dir = new File(dirName);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Directory '" + dirName +
                        "' does not exist and could not be created");
            }
        }

        int interval = 5;
        String intervalString = context.getProperty(
                "volantis.watcher.interval");
        if (intervalString != null) {
            try {
                interval = Integer.parseInt(intervalString);
            } catch (NumberFormatException e) {
                // Unexpected but not necessary so ignore.
            }
        }

        // Create the watcher, and run it on a daemon worker thread.
        watcher = new Watcher(context, dir, interval);
        Thread worker = new Thread(watcher);
        worker.setDaemon(true);
        worker.start();
    }

    // Javadoc inherited.
    public void stop(BundleContext context) throws Exception {
        if (watcher != null) {
            watcher.stop();
        }
    }
}
