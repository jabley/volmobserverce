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
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * Base for all tasks.
 */
abstract class BaseTask {

    /**
     * The service references.
     */
    protected final ServiceReference[] references;

    /**
     * Initialise.
     *
     * @param references The service references.
     */
    protected BaseTask(
            ServiceReference[] references) {

        this.references = references;
    }

    /**
     * Do the task.
     *
     * @param context The context.
     * @param log     The log service.
     */
    protected abstract void doTask(BundleContext context, LogService log);
}
