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
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.log.LogService;

/**
 * A task that dispatches events to any {@link ConfigurationListener}.
 */
class InvokeConfigurationEventTask
        extends BaseTask {

    /**
     * The container for the {@link ServiceReference} for the {@link
     * ConfigurationAdmin} service.
     */
    private final ServiceReferenceContainer adminReferenceContainer;

    /**
     * The factory pid, may be null.
     */
    private final String factoryPid;

    /**
     * The pid.
     */
    private final String pid;

    /**
     * The event type, {@link ConfigurationEvent#CM_DELETED}, or {@link
     * ConfigurationEvent#CM_UPDATED}.
     */
    private final int type;


    /**
     * @param references              The service references.
     * @param adminReferenceContainer The container for the {@link ServiceReference}
     *                                for the {@link ConfigurationAdmin}
     *                                service.
     * @param factoryPid              The factory pid, may be null.
     * @param pid                     The pid.
     * @param type                    The event type, {@link ConfigurationEvent#CM_DELETED},
     *                                or {@link ConfigurationEvent#CM_UPDATED}.
     */
    public InvokeConfigurationEventTask(
            ServiceReference[] references,
            ServiceReferenceContainer adminReferenceContainer,
            String factoryPid,
            String pid, int type) {
        super(references);

        this.adminReferenceContainer = adminReferenceContainer;
        this.factoryPid = factoryPid;
        this.pid = pid;
        this.type = type;
    }

    // Javadoc inherited.
    protected void doTask(BundleContext context, LogService log) {

        // Get the actual reference from the container.
        ServiceReference adminServiceReference =
                adminReferenceContainer.getReference();

        ConfigurationEvent event =
                new ExtendedConfigurationEvent(adminServiceReference,
                        type, factoryPid, pid);

        for (int i = 0; i < references.length; i++) {
            ServiceReference reference = references[i];
            ConfigurationListener listener = (ConfigurationListener)
                    context.getService(reference);
            if (listener != null) {
                try {
                    listener.configurationEvent(event);
                } catch (Throwable t) {
                    // todo log it.
                } finally {
                    context.ungetService(reference);
                }
            }
        }
    }
}
