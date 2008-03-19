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

package com.volantis.mcs.runtime.plugin.markup;

import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.plugin.IntegrationPluginContainer;

/**
 * This interface is implemented by those classes which store instances of
 * {@link MarkupPlugin}.  It provides an additional method to release the
 * plugins.
 *
 * @mock.generate base="IntegrationPluginContainer"
 */
public interface MarkupPluginContainer
        extends IntegrationPluginContainer {

    /**
     * The Volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Calls the {@link MarkupPlugin#release} on each of the stored plugins
     * before emptying the store of all plugins.
     */
    public void releasePlugins();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 25-Jan-05	6712/6	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 ===========================================================================
*/
