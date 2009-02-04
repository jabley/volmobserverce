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

package com.volantis.mcs.runtime.plugin;

import com.volantis.mcs.integration.IntegrationPlugin;

/**
 * A container of {@link IntegrationPlugin} instances.
 *
 * @mock.generate
 */
public interface IntegrationPluginContainer {

    /**
     * Get a plugin that was stored within the container.
     *
     * <p>If no plugin with the specified name currently exists then use the
     * factory to create one and add it into the container. This test and set
     * is thread safe.</p>
     *
     * @param factory The object to use to create the plugin if necessary.
     *
     * @return The plugin stored within the container, or null if there was
     * none.
     */
    public IntegrationPlugin getPlugin(IntegrationPluginFactory factory);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
