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

import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.AbstractMarkupPlugin;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.integration.iapi.InvokeAttributes;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.configuration.ArgumentConfiguration;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfigurationContainer;
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;
import com.volantis.mcs.runtime.plugin.IntegrationPluginFactory;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;

/**
 * This class tests MarkupPluginManager
 */
public class MarkupPluginManagerTestCase extends TestCaseAbstract {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    private MarkupPluginContainerImpl applicationScopeContainer;
    private MarkupPluginContainerImpl canvasScopeContainer;
    private MarkupPluginContainerImpl sessionScopeContainer;

    /**
     * Contstruct a new instance of MarkupPluginManagerTestCase
     * @param name The name of the testcase.
     */
    public MarkupPluginManagerTestCase(String name) {
        super(name);
    }

    /**
     * Create a manager containing one single plugin.
     * @param config The plugin configuration.
     * @return The manager.
     */
    private MarkupPluginManagerImpl createManagerForOnePlugin(
            MarkupPluginConfiguration config) {

        IntegrationPluginConfigurationContainer plugins
                = new IntegrationPluginConfigurationContainer();
        plugins.addPlugin(config);

        applicationScopeContainer = new MarkupPluginContainerImpl();
        canvasScopeContainer = new MarkupPluginContainerImpl();
        sessionScopeContainer = new MarkupPluginContainerImpl();

        // Create a test locator.
        Scope2ContainerLocator scope2ContainerLocator = new Scope2ContainerLocator() {

            private ContainerLocator application = new TestContainerLocator(
                    applicationScopeContainer, true);

            private ContainerLocator canvas = new TestContainerLocator(
                    canvasScopeContainer, false);

            private ContainerLocator session = new TestContainerLocator(
                    sessionScopeContainer, false);

            public ContainerLocator getLocator(MarkupPluginScope scope) {

                if (scope == MarkupPluginScope.APPLICATION) {
                    return application;
                } else if (scope == MarkupPluginScope.CANVAS) {
                    return canvas;
                } else if (scope == MarkupPluginScope.SESSION) {
                    return session;
                } else {
                    throw new IllegalArgumentException(
                            "Unknown scope: " + scope);
                }
            }
        };

        MarkupPluginManagerImpl manager
                = new MarkupPluginManagerImpl(
                        plugins, applicationScopeContainer, null,
                        scope2ContainerLocator);
        return manager;
    }

    /**
     * Test the method addMarkupPlugin
     */
    public void testAddMarkupPlugin() throws Exception {
        MarkupPluginConfiguration config = new MarkupPluginConfiguration();

        final String pluginName = "myPlugin";
        final String pluginClassName = ManagerTestMarkupPlugin.class.getName();
        final String pluginScope = "application";

        config.setName(pluginName);
        config.setClassName(pluginClassName);
        config.setScope(pluginScope);

        final ArgumentConfiguration ac = new ArgumentConfiguration();
        final String argName = "argName";
        final String argValue = "argValue";
        ac.setName(argName);
        ac.setValue(argValue);
        config.addArgument(ac);

        MarkupPluginManagerImpl manager = createManagerForOnePlugin(config);

        IntegrationPluginFactory factory = manager.getFactory(pluginName);

        assertNotNull("Expected as a factory to be stored under the name key",
                      factory);
    }

    protected static MarinerRequestContext createMarinerRequestContext() {

        MarinerRequestContext requestContext =
                new MarinerRequestContext() {
                    public MarinerRequestContext createNestedContext()
                            throws IOException,
                            RepositoryException,
                            MarinerContextException {
                        return null;
                    }
                };
        return requestContext;
    }

    /**
     * Test getMarkupPlugin with an application scope plugin.
     */
    public void testGetMarkupPluginApplicationScope() throws Exception {
        MarkupPluginConfiguration config = new MarkupPluginConfiguration();

        final String pluginName = "myPlugin";
        final String pluginClassName = ManagerTestMarkupPlugin.class.getName();
        final String pluginScope = "application";

        config.setName(pluginName);
        config.setClassName(pluginClassName);
        config.setScope(pluginScope);

        final ArgumentConfiguration ac = new ArgumentConfiguration();
        final String argName = "argName";
        final String argValue = "argValue";
        ac.setName(argName);
        ac.setValue(argValue);
        config.addArgument(ac);

        MarkupPluginManagerImpl manager = createManagerForOnePlugin(config);

        InvokeAttributes attrs = new InvokeAttributes();
        attrs.setName(pluginName);

        MarinerRequestContext requestContext = createMarinerRequestContext();

        MarkupPlugin plugin
                = manager.getMarkupPlugin(requestContext, attrs.getName());
        assertTrue("Expected an instance of ManagerTestMarkupPlugin.",
                   plugin instanceof ManagerTestMarkupPlugin);
    }

    /**
     * Test the method getSessionScope
     */
    public void testGetSessionScopePlugin() throws Exception {
        MarkupPluginConfiguration config = new MarkupPluginConfiguration();

        final String pluginName = "myPlugin";
        final String pluginClassName = ManagerTestMarkupPlugin.class.getName();
        final String pluginScope = "session";

        config.setName(pluginName);
        config.setClassName(pluginClassName);
        config.setScope(pluginScope);

        final ArgumentConfiguration ac = new ArgumentConfiguration();
        final String argName = "argName";
        final String argValue = "argValue";
        ac.setName(argName);
        ac.setValue(argValue);
        config.addArgument(ac);

        MarkupPluginManagerImpl manager = createManagerForOnePlugin(config);

        InvokeAttributes attrs = new InvokeAttributes();
        attrs.setName(pluginName);

        MarinerRequestContext requestContext = createMarinerRequestContext();

        MarkupPlugin fromManager =
                manager.getMarkupPlugin(requestContext, attrs.getName());
        assertNotNull("Unexpected null value for plugin", fromManager);
        assertTrue("Expected an instance of ManagerTestMarkupPlugin.",
                   fromManager instanceof ManagerTestMarkupPlugin);

        //MarkupPluginContainerImpl container = (MarkupPluginContainerImpl)
        //session.getMarkupPluginContainer();
        MarkupPlugin fromContext = (MarkupPlugin)
                sessionScopeContainer.getPlugin(pluginName);

        assertSame("Expected the plugin to be stored in session.",
                   fromManager, fromContext);
    }

    public void testGetCanvasScopePlugin() throws Exception {
        MarkupPluginConfiguration config = new MarkupPluginConfiguration();

        final String pluginName = "myPlugin";
        final String pluginClassName = ManagerTestMarkupPlugin.class.getName();
        final String pluginScope = "canvas";

        config.setName(pluginName);
        config.setClassName(pluginClassName);
        config.setScope(pluginScope);

        final ArgumentConfiguration ac = new ArgumentConfiguration();
        final String argName = "argName";
        final String argValue = "argValue";
        ac.setName(argName);
        ac.setValue(argValue);
        config.addArgument(ac);

        MarkupPluginManagerImpl manager = createManagerForOnePlugin(config);

        InvokeAttributes attrs = new InvokeAttributes();
        attrs.setName(pluginName);

        MarinerRequestContext requestContext = createMarinerRequestContext();

        MarkupPlugin fromManager =
                manager.getMarkupPlugin(requestContext, attrs.getName());
        assertNotNull("Unexpected null value for plugin", fromManager);
        assertTrue("Expected an instance of ManagerTestMarkupPlugin.",
                   fromManager instanceof ManagerTestMarkupPlugin);

        MarkupPlugin fromContext
                = (MarkupPlugin) canvasScopeContainer.getPlugin(pluginName);

        assertSame("Expected the plugin to be stored in canvas.",
                   fromManager, fromContext);
    }

    /**
     * Test implementation of MarkupPlugin.
     */
    public static class ManagerTestMarkupPlugin extends AbstractMarkupPlugin {
        /**
         * Public no-arg constructor.
         */
        public ManagerTestMarkupPlugin() {
        }
    }

    private static class TestContainerLocator
            implements ContainerLocator {

        private final boolean application;
        private final MarkupPluginContainer container;

        public TestContainerLocator(MarkupPluginContainer container,
                                    boolean application) {
            this.container = container;
            this.application = application;
        }

        public boolean isApplication() {
            return application;
        }

        public MarkupPluginContainer getContainer(
                MarinerRequestContext requestContext) {
            return container;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/7	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
