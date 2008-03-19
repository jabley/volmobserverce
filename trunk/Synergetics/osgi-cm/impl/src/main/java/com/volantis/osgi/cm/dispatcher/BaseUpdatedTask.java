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
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;

import java.util.Dictionary;

/**
 * Base for all tasks that update a {@link ManagedService} / {@link
 * ManagedServiceFactory}.
 */
abstract class BaseUpdatedTask
        extends BaseTask {

    /**
     * The manager for {@link ConfigurationPlugin}s.
     */
    private final PluginManager pluginManager;

    /**
     * Initialise.
     *
     * @param references    The service references.
     * @param pluginManager The manager for {@link ConfigurationPlugin}s.
     */
    protected BaseUpdatedTask(
            ServiceReference[] references,
            PluginManager pluginManager) {
        super(references);

        if (pluginManager == null) {
            throw new IllegalArgumentException("pluginManager cannot be null");
        }

        this.pluginManager = pluginManager;
    }

    /**
     * Get the dictionary from the snapshot.
     *
     * @param snapshot   The snapshot.
     * @param factoryPid The factory PID, may be null.
     * @return The dictionary that may have been modified by {@link
     *         ConfigurationPlugin}s.
     */
    protected Dictionary getDictionaryFromSnapshot(
            ConfigurationSnapshot snapshot,
            final String factoryPid) {

        if (snapshot == null) {
            return null;
        }

        String pid = snapshot.getPid();
        Dictionary properties = snapshot.getProperties();

        Dictionary dictionary = pluginManager.invokePlugins(properties);

        // Make sure that the automatic properties are added as per the
        // specification.
        dictionary.put(FrameworkConstants.SERVICE_PID, pid);
        if (factoryPid != null) {
            dictionary.put(FrameworkConstants.SERVICE_FACTORYPID,
                    factoryPid);
        }

        return dictionary;
    }
}
