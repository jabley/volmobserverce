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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm.dispatcher;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 * Adapter that wraps a {@link BaseTask} and implements {@link Runnable}.
 */
class RunnableTaskAdapter
        implements Runnable {

    /**
     * The bundle context.
     */
    private final BundleContext context;

    /**
     * The log service used to log information while processing tasks.
     */
    private final LogService log;

    /**
     * The task to wrap.
     */
    private final BaseTask task;

    /**
     * Initialise.
     *
     * @param context The bundle context.
     * @param log     The log service.
     */
    protected RunnableTaskAdapter(
            BundleContext context, LogService log,
            BaseTask task) {
        this.task = task;

        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }

        if (log == null) {
            throw new IllegalArgumentException("log cannot be null");
        }

        this.context = context;
        this.log = log;
    }

    public final void run() {
        task.doTask(context, log);
    }
}
