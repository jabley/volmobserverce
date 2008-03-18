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

import com.volantis.osgi.cm.ConfigurationSnapshot;
import com.volantis.osgi.cm.plugin.PluginManager;
import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

import java.util.Dictionary;

/**
 * Task to invoke the {@link ManagedService#updated(Dictionary)} method
 * asynchronously.
 *
 * <p>Used for when a {@link Configuration} is updated and deleted, and when a
 * {@link ManagedService} registers. The snapshot can be null either when the
 * service first registers and indicates that no configuration has been created,
 * or when the configuration has been deleted.</p>
 */
class InvokeServiceUpdatedTask
        extends BaseUpdatedTask {

    /**
     * The {@link ConfigurationSnapshot}.
     */
    private final ConfigurationSnapshot snapshot;

    /**
     * Initialise.
     *
     * @param references    The service references.
     * @param snapshot      The {@link ConfigurationSnapshot}.
     * @param pluginManager The {@link ConfigurationPlugin} manager.
     */
    public InvokeServiceUpdatedTask(
            ServiceReference[] references,
            ConfigurationSnapshot snapshot,
            PluginManager pluginManager) {
        super(references, pluginManager);

        this.snapshot = snapshot;
    }

    // Javadoc inherited.
    protected void doTask(BundleContext context, LogService log) {

        Dictionary dictionary = getDictionaryFromSnapshot(snapshot, null);

        for (int i = 0; i < references.length; i++) {
            ServiceReference reference = references[i];

            // Get the service to update.
            ManagedService service =
                    (ManagedService) context.getService(reference);
            try {
                if (service == null) {
                    // Must have deregistered so nothing to do.
                    return;
                }

                try {
                    CaseInsensitiveDictionary temp;
                    if (dictionary == null) {
                        temp = null;
                    } else {
                        temp = new CaseInsensitiveDictionary(dictionary);
                    }
                    service.updated(temp);
                } catch (ConfigurationException e) {
                    log.log(reference, LogService.LOG_ERROR,
                            "updated(" + dictionary + ") method failed:",
                            e);
                } catch (Throwable throwable) {
                    log.log(reference, LogService.LOG_ERROR,
                            "updated(" + dictionary + ") method failed:",
                            throwable);
                }
            } finally {
                context.ungetService(reference);
            }
        }
    }
}
