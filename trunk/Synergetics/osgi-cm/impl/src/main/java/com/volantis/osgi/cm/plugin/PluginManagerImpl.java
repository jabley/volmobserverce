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

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import java.util.Dictionary;

/**
 * Implementation of {@link PluginManager}.
 *
 * todo Implement the rest of this.
 */
public class PluginManagerImpl
        implements PluginManager {

    /**
     * The log service.
     */
    private final LogService log;

    /**
     * Initialise.
     *
     * @param log The log service.
     */
    public PluginManagerImpl(LogService log) {
        this.log = log;
    }

    // Javadoc inherited.
    public Dictionary invokePlugins(Dictionary dictionary) {
        return dictionary;
    }

    // Javadoc inherited.
    public void pluginRegistered(ServiceReference reference) {
        log.log(reference, LogService.LOG_WARNING,
                "ConfigurationPlugin not supported");
    }

    // Javadoc inherited.
    public void pluginModified(ServiceReference reference) {
        log.log(reference, LogService.LOG_WARNING,
                "ConfigurationPlugin not supported");
    }

    // Javadoc inherited.
    public void pluginUnregistering(ServiceReference reference) {
        log.log(reference, LogService.LOG_WARNING,
                "ConfigurationPlugin not supported");
    }
}
