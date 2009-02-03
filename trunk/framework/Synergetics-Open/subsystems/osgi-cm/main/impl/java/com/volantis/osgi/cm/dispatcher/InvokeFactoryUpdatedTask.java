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
import com.volantis.osgi.cm.FrameworkConstants;
import com.volantis.osgi.cm.plugin.PluginManager;
import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.log.LogService;

import java.util.Dictionary;

/**
 * Task to invoke the {@link ManagedServiceFactory#updated(String,Dictionary)}
 * method asynchronously.
 *
 * <p>Used when a {@link Configuration} is updated, or when a {@link
 * ManagedServiceFactory} first registers.</p>
 */
class InvokeFactoryUpdatedTask
        extends BaseUpdatedTask {

    /**
     * The pid of the factory.
     */
    private final String factoryPid;

    /**
     * The snapshots to pass to the factory.
     */
    private final ConfigurationSnapshot[] snapshots;

    /**
     * Initialise.
     *
     * @param factoryPid    The pid of the factory.
     * @param references    The service references.
     * @param snapshots     The {@link ConfigurationSnapshot}s.
     * @param pluginManager The {@link ConfigurationPlugin} manager.
     */
    public InvokeFactoryUpdatedTask(
            final String factoryPid,
            ServiceReference[] references,
            ConfigurationSnapshot[] snapshots,
            PluginManager pluginManager) {
        super(references, pluginManager);

        this.factoryPid = factoryPid;
        this.snapshots = snapshots;
    }

    // Javadoc inherited.
    protected void doTask(BundleContext context, LogService log) {

        Dictionary[] allProperties = new Dictionary[snapshots.length];

        for (int i = 0, p = 0; i < snapshots.length; i++) {
            ConfigurationSnapshot snapshot = snapshots[i];

            // Store them away.
            allProperties[p++] =
                    getDictionaryFromSnapshot(snapshot, factoryPid);
        }

        for (int t = 0; t < references.length; t++) {
            ServiceReference reference = references[t];

            // Get the service to update.
            ManagedServiceFactory factory =
                    (ManagedServiceFactory) context.getService(reference);
            try {
                if (factory == null) {
                    // Must have deregistered so nothing to do.
                    return;
                }

                for (int i = 0; i < allProperties.length; i++) {
                    Dictionary dictionary = allProperties[i];

                    String pid = (String) dictionary.get(
                            FrameworkConstants.SERVICE_PID);

                    try {
                        factory.updated(pid,
                                new CaseInsensitiveDictionary(dictionary));
                    } catch (ConfigurationException e) {
                        log.log(reference, LogService.LOG_ERROR,
                                "updated(" + pid + ", " + dictionary +
                                        ") method failed:",
                                e);
                    } catch (Throwable throwable) {
                        log.log(reference, LogService.LOG_ERROR,
                                "updated(" + pid + ", " + dictionary +
                                        ") method failed:",
                                throwable);
                    }
                }
            } finally {
                context.ungetService(reference);
            }
        }
    }
}
