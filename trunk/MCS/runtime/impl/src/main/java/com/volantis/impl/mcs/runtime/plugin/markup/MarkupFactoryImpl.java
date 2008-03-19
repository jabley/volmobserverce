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

package com.volantis.impl.mcs.runtime.plugin.markup;

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfigurationContainer;
import com.volantis.mcs.runtime.plugin.markup.MarkupFactory;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginManager;

/**
 * Implementation of the factory.
 */
public class MarkupFactoryImpl
        extends MarkupFactory {

    // Javadoc inherited.
    public MarkupPluginManager createMarkupPluginManager(
            IntegrationPluginConfigurationContainer plugins,
            MarkupPluginContainer applicationContainer,
            MarinerApplication application) {

        return new MarkupPluginManagerImpl(
                plugins, applicationContainer, application,
                Scope2ContainerLocator.DEFAULT_INSTANCE);
    }

    // Javadoc inherited.
    public MarkupPluginContainer createMarkupPluginContainer() {
        return new MarkupPluginContainerImpl();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/6	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
