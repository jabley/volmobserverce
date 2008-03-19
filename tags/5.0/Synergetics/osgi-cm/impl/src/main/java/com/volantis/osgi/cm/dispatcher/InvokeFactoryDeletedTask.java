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
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.log.LogService;

/**
 * Task to invoke the {@link ManagedServiceFactory#deleted(String)} method
 * asynchronously.
 *
 * <p>Used when a {@link Configuration} is deleted.</p>
 */
class InvokeFactoryDeletedTask
        extends BaseTask {

    /**
     * The configuration pid.
     */
    private final String pid;

    /**
     * @param references The service references.
     * @param pid        The configuration pid.
     */
    public InvokeFactoryDeletedTask(
            ServiceReference[] references, final String pid) {
        super(references);

        this.pid = pid;
    }

    // Javadoc inherited.
    protected void doTask(BundleContext context, LogService log) {
        for (int i = 0; i < references.length; i++) {
            ServiceReference reference = references[i];

            // Get the service to update.
            ManagedServiceFactory factory =
                    (ManagedServiceFactory) context.getService(reference);
            try {
                if (factory == null) {
                    // Must have deregistered so nothing to do.
                    return;
                }


                try {
                    factory.deleted(pid);
                } catch (Throwable throwable) {
                    log.log(reference, LogService.LOG_ERROR,
                            "deleted(" + pid + ") method failed:",
                            throwable);
                }
            } finally {
                context.ungetService(reference);
            }
        }
    }
}
