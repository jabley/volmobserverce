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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains configurations for a set of integration plugins.
 */
public class IntegrationPluginConfigurationContainer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(IntegrationPluginConfigurationContainer.class);

    /**
     * A mutable map from plugin name to configuration.
     */
    private Map plugins = new HashMap();

    /**
     * An immutable collation of the {@link Map#values()} of {@link #plugins}.
     */
    private Collection immutablePlugins
            = Collections.unmodifiableCollection(plugins.values());

    /**
     * Add a plugin.
     *
     * <p>This will log a warning if the name has not been set (should already
     * have been picked up by schema validation), or if it is not unique.</p>
     *
     * @param plugin The plugin to add.
     */
    public void addPlugin(IntegrationPluginConfiguration plugin) {
        final String name = plugin.getName();
        if (name == null) {
            logger.warn("markup-plugin-name-missing");
        } else {
            final IntegrationPluginConfiguration existing =
                    (IntegrationPluginConfiguration) plugins.get(name);
            if (existing != null) {
                logger.warn("plugin-overwritten", new Object[]{name});
            }
            plugins.put(name, plugin);
        }
    }

    /**
     * Get the named {@link IntegrationPluginConfiguration}.
     * @param name The name of the plugin.
     * @return The configuration for the plugin, or null.
     */
    public IntegrationPluginConfiguration getPluginConfiguration(String name) {
        final IntegrationPluginConfiguration plugin =
                (IntegrationPluginConfiguration) plugins.get(name);
        return plugin;
    }

    /**
     * Get an unmodifiable {@link Collection} of
     * {@link IntegrationPluginConfiguration}.
     *
     * @return The unmodifiable {@link Collection} of
     * {@link IntegrationPluginConfiguration}.
     */
    public Collection getPlugins() {
        return immutablePlugins;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
