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

import com.volantis.impl.mcs.runtime.plugin.IntegrationPluginManagerImpl;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.IntegrationPlugin;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfiguration;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfigurationContainer;
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;
import com.volantis.mcs.runtime.plugin.IntegrationPluginContainer;
import com.volantis.mcs.runtime.plugin.IntegrationPluginFactory;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginManager;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.MessageLocalizer;

/**
 * Implementation of {@link MarkupPluginManager}.
 */
class MarkupPluginManagerImpl
        extends IntegrationPluginManagerImpl
        implements MarkupPluginManager {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for generating localized messages.
     */
    private static final MessageLocalizer messageLocalizer =
            LocalizationFactory.createMessageLocalizer(
                    MarkupPluginManagerImpl.class);

    /**
     * Map from scope to container locator.
     */
    private final Scope2ContainerLocator scope2ContainerLocator;

    /**
     * Initialise.
     */
    protected MarkupPluginManagerImpl(
            IntegrationPluginConfigurationContainer plugins,
            IntegrationPluginContainer applicationContainer,
            MarinerApplication application,
            Scope2ContainerLocator scope2ContainerLocator) {

        super(applicationContainer);

        this.scope2ContainerLocator = scope2ContainerLocator;

        initialise(plugins, application);
    }

    /**
     * Get the factory given the name.
     *
     * <p>This is currently only use by tests.</p>
     */
    protected IntegrationPluginFactory getFactory(String pluginName) {
        Entry entry = (Entry) pluginEntries.get(pluginName);
        return entry.getFactory();
    }

    // Javadoc inherited.
    public MarkupPlugin getMarkupPlugin(MarinerRequestContext context,
                                        String pluginName) {

        MarkupPlugin result = null;

        MarkupEntry entry = (MarkupEntry) getEntry(pluginName);

        if (entry != null) {
            IntegrationPluginFactory factory = entry.getFactory();
            ContainerLocator locator = entry.getLocator();
            MarkupPluginContainer container = locator.getContainer(context);
            result = (MarkupPlugin) container.getPlugin(factory);
        } else {
            // if there was not a plugin then create an error plugin.
            result = (MarkupPlugin) createErrorPlugin(
                    MarkupPlugin.class,
                    pluginName,
                    messageLocalizer.format("plugin-not-configured",
                                            pluginName),
                    null);
        }

        return result;
    }

    // Javadoc inherited.
    protected Entry createEntry(IntegrationPluginConfiguration config,
                                MarinerApplication application) {

        MarkupPluginConfiguration cfg = (MarkupPluginConfiguration) config;
        String pluginName = cfg.getName();
        IntegrationPluginFactory factory;
        MarkupPluginScope scope = MarkupPluginScope.literal(cfg.getScope());
        if (scope == null) {
            IntegrationPlugin errorPlugin = createErrorPlugin(
                    MarkupPlugin.class,
                    pluginName,
                    messageLocalizer.format("plugin-scope-invalid",
                                            new Object[]{pluginName,
                                                         scope}),
                    null);

            factory = createErrorPluginFactory(errorPlugin);

            // Default the scope to APPLICATION.
            scope = MarkupPluginScope.APPLICATION;
        } else {
            factory = new MarkupPluginFactoryImpl((MarkupPluginConfiguration) config,
                                                  application);
        }

        ContainerLocator locator = scope2ContainerLocator.getLocator(scope);

        return new MarkupEntry(factory, locator);
    }

    // Javadoc inherited.
    protected boolean isApplicationPlugin(Entry entry) {
        MarkupEntry markupEntry = (MarkupEntry) entry;
        return markupEntry.getLocator().isApplication();
    }

    /**
     * Create an error plugin.
     *
     * @see MarkupPluginFactoryImpl#createErrorMarkupPlugin
     */
    protected IntegrationPlugin createErrorPlugin(
            Class pluginInterfaceClass, String pluginName,
            String errorMsg, Throwable throwable) {

        return MarkupPluginFactoryImpl.createErrorMarkupPlugin(
                pluginInterfaceClass, pluginName, errorMsg, throwable);
    }

    /**
     * Create a factory that wraps an error plugin.
     * @param errorPlugin The plugin to wrap.
     * @return An error plugin.
     */
    protected IntegrationPluginFactory createErrorPluginFactory(
            IntegrationPlugin errorPlugin) {

        return new MarkupPluginFactoryImpl(errorPlugin);
    }

    /**
     * Extend to associate a {@link ContainerLocator} with each entry.
     */
    private static class MarkupEntry
            extends Entry {

        /**
         * The locator to use to find the container given a
         * {@link MarinerRequestContext}.
         */
        private final ContainerLocator locator;

        /**
         * Initialise.
         * @param factory The factory.
         * @param locator The container locator.
         */
        public MarkupEntry(IntegrationPluginFactory factory,
                           ContainerLocator locator) {
            super(factory);

            this.locator = locator;
        }

        /**
         * Get the locator.
         */
        public ContainerLocator getLocator() {
            return locator;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 25-Jan-05	6712/6	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
