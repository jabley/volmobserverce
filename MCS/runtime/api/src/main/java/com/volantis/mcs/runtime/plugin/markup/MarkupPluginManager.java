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

package com.volantis.mcs.runtime.plugin.markup;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.plugin.IntegrationPluginManager;

/**
 * MarkupPluginManager is provided to ensure that the IAPI invoke element does
 * not need to be aware of where application, session and "page" scope plugins
 * are stored and how they are managed.
 */
public interface MarkupPluginManager
        extends IntegrationPluginManager {

    /**
     * Get the {@link MarkupPlugin} defined by the specified attributes.
     *
     * <p>If the named plugin was either not configured, or was configured but
     * an error occurred when instantiating it then an error handling plugin
     * is returned.</p>
     *
     * @param context The MarinerRequestContext from which we will retrieve
     * the appropriate scope to retrieve (or store) the MarkupPlugin.
     * @param pluginName The name of the {@link MarkupPlugin} to return.
     *
     * @return the {@link MarkupPlugin} defined by the specified attributes.
     */
    MarkupPlugin getMarkupPlugin(MarinerRequestContext context,
                                 String pluginName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/5	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
