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

package com.volantis.impl.mcs.runtime.plugin;

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.integration.IntegrationPlugin;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfiguration;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfigurationContainer;
import com.volantis.mcs.runtime.plugin.IntegrationPluginContainer;
import com.volantis.mcs.runtime.plugin.IntegrationPluginFactory;
import com.volantis.mcs.runtime.plugin.IntegrationPluginManager;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Base for those classes that manage plugins.
 */
public abstract class IntegrationPluginManagerImpl
        implements IntegrationPluginManager {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(IntegrationPluginManagerImpl.class);

    //private final Class pluginInterfaceClass;

    /**
     * The container for application plugins.
     */
    private final IntegrationPluginContainer applicationContainer;

    /**
     * A Map of plugin factories which are used to create instances of
     * {@link IntegrationPlugin}.
     */
    protected final Map pluginEntries;

    /**
     * Initialise.
     * @param applicationContainer
     */
    public IntegrationPluginManagerImpl(
            IntegrationPluginContainer applicationContainer) {

        this.applicationContainer = applicationContainer;
        pluginEntries = new HashMap();
    }

    /**
     * Must be called by any derived classes.
     */
    protected void initialise(IntegrationPluginConfigurationContainer plugins,
                              MarinerApplication application) {
        for (Iterator i = plugins.getPlugins().iterator(); i.hasNext();) {
            IntegrationPluginConfiguration cfg
                    = (IntegrationPluginConfiguration) i.next();
            addPlugin(cfg, application);
        }
    }

    /**
     * Get an entry for a plugin.
     *
     * @param pluginName The name.
     *
     * @return The entry, or null if no such plugin has been configured.
     */
    protected Entry getEntry(String pluginName) {
        return (Entry) pluginEntries.get(pluginName);
    }

    /*
    protected abstract IntegrationPlugin createErrorPlugin(
            Class pluginInterfaceClass,
            String pluginName, String errorMsg, Throwable throwable);

    protected abstract IntegrationPluginFactory createErrorPluginFactory(
            IntegrationPlugin errorPlugin);
            */

    /**
     * Add a plugin using the specified configuration information.
     * @param config The configuration which contains the
     * information required to create the plugin.
     */
    private void addPlugin(IntegrationPluginConfiguration config,
                           MarinerApplication application) {
        String name = config.getName();
        if (name != null) {
            if (pluginEntries.get(name) != null) {
                logger.warn("plugin-overwritten", new Object[]{name});
            }
            Entry entry = createEntry(config, application);
            pluginEntries.put(name, entry);
        } else {
            logger.warn("markup-plugin-name-missing");
        }
    }

    /**
     * Create a populated entry.
     *
     * @param config The configuration.
     * @param application The application, passed to the plugin when it is
     * initialized.
     *
     * @return The entry for the plugin.
     */
    protected abstract Entry createEntry(IntegrationPluginConfiguration config,
                                         MarinerApplication application);

    // Javadoc inherited.
    public void createApplicationPlugins() {

        for (Iterator i = pluginEntries.values().iterator(); i.hasNext();) {
            Entry entry = (Entry) i.next();
            IntegrationPluginFactory factory = entry.getFactory();

            if (isApplicationPlugin(entry)) {
                applicationContainer.getPlugin(factory);
            }
        }
    }

    /**
     * Check to see whether the entry is for an application plugin.
     *
     * @param entry The entry.
     *
     * @return True if it is an application plugin and false otherwise.
     */
    protected boolean isApplicationPlugin(Entry entry) {
        return true;
    }

    /**
     * Container for information about a plugin that is not part of the
     * factory.
     */
    protected static class Entry {

        /**
         * The factory.
         */
        private final IntegrationPluginFactory factory;

        /**
         * Initialise.
         * @param factory The factory.
         */
        public Entry(IntegrationPluginFactory factory) {
            this.factory = factory;
        }

        /**
         * Get the factory.
         */
        public IntegrationPluginFactory getFactory() {
            return factory;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/2	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
