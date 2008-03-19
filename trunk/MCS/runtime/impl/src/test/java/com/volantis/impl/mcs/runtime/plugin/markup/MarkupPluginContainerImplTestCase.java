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

import com.volantis.impl.mcs.runtime.plugin.IntegrationPluginContainerImplTestCase;
import com.volantis.mcs.integration.IntegrationPluginMock;
import com.volantis.mcs.integration.MarkupPluginMock;
import com.volantis.mcs.runtime.plugin.IntegrationPluginContainer;
import com.volantis.mcs.runtime.plugin.IntegrationPluginFactoryMock;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;

/**
 * This class tests DefaultMarkupPluginContainer
 */
public class MarkupPluginContainerImplTestCase
        extends IntegrationPluginContainerImplTestCase {

    protected IntegrationPluginMock createMockPluginInstance(
            final String identifier) {
        return new MarkupPluginMock(identifier, expectations);
    }

    protected IntegrationPluginContainer createContainer() {
        return new MarkupPluginContainerImpl();
    }

    /**
     * Test the method releasePlugins()
     */
    public void testReleasePlugins() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a dynamic mock MarkupPlugin with a sequence of expectations.
        MarkupPluginMock plugin = (MarkupPluginMock) createMockPluginInstance(
                "pluginMock");

        // Create a mock factory.
        IntegrationPluginFactoryMock factory = createMockPluginFactory(
                "pluginFactoryMock");

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // 1) When getting a plugin from the container the factory will be
        //    used to create a plugin.
        factory.expects.getName().returns("myPlugin");
        factory.expects.createPluginInstance().returns(plugin);

        // 2) When releasing the plugins the plugin will be released.
        plugin.expects.release();

        // 3) When getting a plugin from the container after releasing the
        //    factory should be reinvoked to get the plugin again.
        factory.expects.getName().returns("myPlugin");
        factory.expects.createPluginInstance().returns(plugin);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MarkupPluginContainer container = new MarkupPluginContainerImpl();
        container.getPlugin(factory);

        // Release the plugins, this should cause the plugins release method
        // to be called and then the plugin should be removed from the
        // container.
        container.releasePlugins();

        // Get them again.
        container.getPlugin(factory);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/3	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 25-Jan-05	6712/6	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 ===========================================================================
*/
