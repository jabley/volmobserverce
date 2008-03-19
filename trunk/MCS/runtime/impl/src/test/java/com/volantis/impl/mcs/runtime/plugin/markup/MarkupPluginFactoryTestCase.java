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

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.integration.AbstractMarkupPlugin;
import com.volantis.mcs.integration.IntegrationPlugin;
import com.volantis.mcs.runtime.configuration.ArgumentConfiguration;
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class tests MarkupPluginFactory.
 */
public class MarkupPluginFactoryTestCase extends TestCaseAbstract {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create an new instance of MarkupPluginFactory testcase.
     * @param name The name of the testcase.
     */
    public MarkupPluginFactoryTestCase(String name) {
        super(name);
    }

    public void testGetPluginInstance() throws Exception {
        MarkupPluginConfiguration config = new MarkupPluginConfiguration();

        final String pluginName = "myPlugin";
        final String pluginClassName = FactoryTestMarkupPlugin.class.getName();
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

        MarinerApplication application = null;
        MarkupPluginFactoryImpl factory
                = new MarkupPluginFactoryImpl(config, application);

        assertEquals("Unexpected name.", pluginName, factory.getName());

        IntegrationPlugin plugin = factory.createPluginInstance();

        assertEquals("Unexpected object class",
                     FactoryTestMarkupPlugin.class, plugin.getClass());
    }

    public void testGetPluginInstanceWithError() throws Exception {
        MarkupPluginConfiguration config = new MarkupPluginConfiguration();

        final String pluginName = "myPlugin";
        final String pluginClassName = "not.markup.plugin";
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

        MarinerApplication application = null;
        MarkupPluginFactoryImpl factory
                = new MarkupPluginFactoryImpl(config, application);

        IntegrationPlugin plugin = factory.createPluginInstance();

        assertTrue("Plugin should be an instance of ErrorMarkupPlugin but"
                   + " is: " + plugin,
                   plugin instanceof MarkupPluginFactoryImpl.ErrorMarkupPlugin);
    }

    /**
     * Test implementation of MarkupPlugin.
     */
    public static class FactoryTestMarkupPlugin extends AbstractMarkupPlugin {
        /**
         * Public no-arg constructor.
         */
        public FactoryTestMarkupPlugin() {
        }
    }

    /**
     * Test class which doesn't implement MarkupPlugin.  Included to cause an
     * error plugin to be generated.
     */
    public static class NotMarkupPlugin {
        /**
         * Public no-arg constructor.
         */
        public NotMarkupPlugin() {
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 25-Jan-05	6712/5	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
