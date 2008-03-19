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
import com.volantis.mcs.integration.IntegrationPluginMock;
import com.volantis.mcs.runtime.plugin.IntegrationPluginContainer;
import com.volantis.mcs.runtime.plugin.IntegrationPluginFactoryMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class IntegrationPluginContainerImplTestCase
        extends TestCaseAbstract {

    protected ExpectationBuilder expectations;

    protected void setUp() throws Exception {
        super.setUp();

        expectations = mockFactory.createOrderedBuilder();
    }

    protected IntegrationPluginContainer createContainer() {
        return new IntegrationPluginContainerImpl();
    }

    /**
     * Expect a usage of the factory.
     * 1) When getting a plugin from the container the factory will be
     *    called to get the plugin name.
     * 2) Then the factory will be called to create a plugin, it will
     *    return the mock plugin.
     *
     * @param factory The factory to which the expectations should be added.
     * @param pluginName The name of the plugin to return.
     * @param plugin The plugin to return.
     */
    public static void expectFactoryUsage(IntegrationPluginFactoryMock factory,
                                          String pluginName,
                                          IntegrationPluginMock plugin) {

        factory.expects.getName().returns(pluginName);
        factory.expects.createPluginInstance().returns(plugin);
    }

    /**
     * Get the class to use for the mock plugins stored in the container.
     * @param identifier
     */
    protected IntegrationPluginMock createMockPluginInstance(
            final String identifier) {
        return new IntegrationPluginMock(identifier, expectations);
    }

    /**
     * Get the class to use for the mock plugins stored in the container.
     * @param identifier
     */
    protected IntegrationPluginFactoryMock createMockPluginFactory(
            final String identifier) {
        return new IntegrationPluginFactoryMock(identifier, expectations);
    }

    /**
     * Test the methods addMarkupPlugin and retrieveMarkupPlugin
     */
    public void testAddAndRetrieveMarkupPlugin()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a dynamic mock MarkupPlugin with a sequence of expectations.
        IntegrationPluginMock plugin = createMockPluginInstance("pluginMock");

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
        
        // 2) When getting the plugin again the factory will be called to get
        //    the name but will not be asked to create a plugin.
        factory.expects.getName().returns("myPlugin");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Get the plugin.
        IntegrationPluginContainer container = createContainer();
        IntegrationPlugin firstPlugin = container.getPlugin(factory);
        assertSame("Unexpected value retrieved from container.",
                   plugin, firstPlugin);

        // Get the plugin again.
        IntegrationPlugin secondPlugin = container.getPlugin(factory);
        assertSame("Expected same object as the first time",
                   firstPlugin, secondPlugin);
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

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
