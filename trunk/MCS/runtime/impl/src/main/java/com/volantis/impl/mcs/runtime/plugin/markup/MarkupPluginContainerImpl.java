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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.impl.mcs.runtime.plugin.markup;

import com.volantis.impl.mcs.runtime.plugin.IntegrationPluginContainerImpl;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;

import java.util.Iterator;

/**
 * The default implementation of {@link MarkupPluginContainer}.  This is
 * typically used by other implementors of {@link MarkupPluginContainer} as
 * a delegate.
 */
public class MarkupPluginContainerImpl
        extends IntegrationPluginContainerImpl
        implements MarkupPluginContainer {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    public MarkupPluginContainerImpl() {
    }

    // javadoc inherited from MarkupPluginContainer interface
    public void releasePlugins() {
        Iterator iterator = plugins.values().iterator();
        while (iterator.hasNext()) {
            MarkupPlugin plugin = (MarkupPlugin) iterator.next();
            plugin.release();
        }
        plugins.clear();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/8	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 ===========================================================================
*/
