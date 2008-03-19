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

import com.volantis.mcs.integration.IntegrationPlugin;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.plugin.IntegrationPluginContainer;
import com.volantis.mcs.runtime.plugin.IntegrationPluginFactory;

import java.util.HashMap;

public class IntegrationPluginContainerImpl
        implements IntegrationPluginContainer {
    /**
     * The Map of {@link MarkupPlugin}s
     */
    protected HashMap plugins;

    public IntegrationPluginContainerImpl() {
        plugins = new HashMap();
    }

    // Javadoc inherited.
    public IntegrationPlugin getPlugin(IntegrationPluginFactory factory) {

        String pluginName = factory.getName();
        IntegrationPlugin plugin;

        synchronized (this) {
            plugin = (IntegrationPlugin) plugins.get(pluginName);
            if (plugin == null) {
                plugin = factory.createPluginInstance();
                plugins.put(pluginName, plugin);
            }
        }

        return plugin;
    }

    /**
     * Testing only.
     */
    public IntegrationPlugin getPlugin(String pluginName) {
        synchronized (this) {
            return (IntegrationPlugin) plugins.get(pluginName);
        }
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
