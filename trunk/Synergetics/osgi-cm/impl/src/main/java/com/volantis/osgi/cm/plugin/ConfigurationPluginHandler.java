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

package com.volantis.osgi.cm.plugin;

import com.volantis.osgi.cm.ServiceHandler;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationPlugin;

/**
 * Forwards service events for {@link ConfigurationPlugin} instances through to
 * the {@link PluginManager}.
 */
public class ConfigurationPluginHandler
        implements ServiceHandler {

    /**
     * The name of the class whose events this will receive.
     */
    private static final String CONFIGURATION_PLUGIN_CLASS =
            ConfigurationPlugin.class.getName();

    /**
     * The manager.
     */
    private final PluginManager manager;

    /**
     * Initialise.
     *
     * @param manager The manager.
     */
    public ConfigurationPluginHandler(PluginManager manager) {
        this.manager = manager;
    }

    // Javadoc inherited.
    public String getRegisteredClass() {
        return CONFIGURATION_PLUGIN_CLASS;
    }

    // Javadoc inherited.
    public void serviceRegistered(ServiceReference reference) {
        manager.pluginRegistered(reference);
    }

    // Javadoc inherited.
    public void serviceModified(ServiceReference reference) {
        manager.pluginModified(reference);
    }

    public void serviceUnregistering(ServiceReference reference) {
        manager.pluginUnregistering(reference);
    }
}
