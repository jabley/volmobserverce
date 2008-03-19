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

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.integration.IntegrationPlugin;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfiguration;
import com.volantis.mcs.runtime.plugin.IntegrationPluginFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation for all classes that create implementations of
 * interfaces derived from {@link IntegrationPlugin}.
 */
public abstract class IntegrationPluginFactoryImpl
        implements IntegrationPluginFactory {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(IntegrationPluginFactoryImpl.class);

    /**
     * Used for generating localized messages.
     */
    private static final MessageLocalizer messageLocalizer =
            LocalizationFactory.createMessageLocalizer(
                    IntegrationPluginFactoryImpl.class);

    /**
     * The name of the plugin.
     */
    protected final String pluginName;

    /**
     * A map of name-value pair String arguments used to initialize the plugin
     */
    private final Map pluginArguments;

    /**
     * An unmodiable wrapper around the plugin arguments. This is the map that
     * is passed to the plugin.
     */
    private final Map immutablePluginArguments;

    /**
     * The type of plugin.
     */
    protected final Class pluginInterfaceClass;

    /**
     * The Class of the {@link IntegrationPlugin}
     */
    protected final Class pluginImplementationClass;

    /**
     * This field is only populated if we have an application scope
     * {@link MarkupPlugin} which we therefore need to reuse.  Or, if there was
     * an error instantiating the plugin this field will be populated with an
     * error handling instance of MarkupPlugin.  If we have a session or canvas
     * scope plugin then this field will remain null.
     */
    protected IntegrationPlugin errorPlugin;

    /**
     * Create a new instance of MarkupPluginConfiguration.
     * @param config The configuration information required to create an
     * instance of a {@link MarkupPlugin}
     */
    public IntegrationPluginFactoryImpl(IntegrationPluginConfiguration config,
                                        MarinerApplication application,
                                        Class pluginInterfaceClass) {

        this.pluginInterfaceClass = pluginInterfaceClass;
        pluginName = config.getName();

        // Copy the arguments so that an additional parameter can be added.
        pluginArguments = new HashMap(config.getArguments());
        pluginArguments.put(IntegrationPlugin.APPLICATION, application);

        // Wrap them in an unmodifiable collection.
        immutablePluginArguments = Collections.unmodifiableMap(pluginArguments);

        String pluginImplementationClassName = config.getClassName();
        Class pluginClass = null;
        try {
            pluginClass = Class.forName(pluginImplementationClassName);
        } catch (ClassNotFoundException e) {
            errorPlugin = createErrorPlugin(
                    messageLocalizer.format(
                            "plugin-class-missing",
                            new Object[]{pluginImplementationClassName,
                                         pluginName,
                                         pluginInterfaceClass.getName()}),
                    e);
        }

        // Make sure that the supplied class is an instance of the plugin
        // class.
        if (pluginClass != null) {
            if (!(pluginInterfaceClass.isAssignableFrom(pluginClass))) {
                errorPlugin = createErrorPlugin(
                        messageLocalizer.format(
                                "plugin-class-bad",
                                new Object[]{pluginImplementationClassName,
                                             pluginName,
                                             pluginInterfaceClass.getName()}),
                        null);
            }
        }

        // Remember the class.
        this.pluginImplementationClass = pluginClass;
    }

    /**
     * Initialise a factory that always returns the specified plugin.
     *
     * <p>This is normally used when a plugin cannot be created because of an
     * error that was detected outside the factory.</p>
     *
     * @param errorPlugin The plugin that is always returned by this factory.
     */
    public IntegrationPluginFactoryImpl(IntegrationPlugin errorPlugin) {
        this.errorPlugin = errorPlugin;
        this.immutablePluginArguments = null;
        this.pluginArguments = null;
        this.pluginName = null;
        this.pluginImplementationClass = null;
        this.pluginInterfaceClass = null;
    }

    // Javadoc inherited.
    public final IntegrationPlugin createPluginInstance() {
        IntegrationPlugin result = null;
        if (errorPlugin == null) {
            result = createInstance();
        } else {
            result = errorPlugin;
        }
        return result;
    }

    // Javadoc inherited.
    public String getName() {
        return pluginName;
    }

    /**
     * Create an instance of {@link IntegrationPlugin} of the class determined
     * on construction based upon the configuration information provided.
     * @return an instance of {@link IntegrationPlugin}
     */
    protected IntegrationPlugin createInstance() {
        IntegrationPlugin result;

        try {
            result = (IntegrationPlugin) pluginImplementationClass.newInstance();
            try {
                result.initialize(immutablePluginArguments);
            } catch (Exception e) {
                // Return an error plugin but don't save it as this may not
                // happen every time.
                result = createErrorPlugin(
                        messageLocalizer.format(
                                "plugin-class-exception",
                                new Object[]{pluginImplementationClass.
                                             getName(),
                                             pluginName}),
                        e);
            }

        } catch (InstantiationException e) {
            // Save the error plugin as this error will happen every time.
            errorPlugin = createErrorPlugin(
                    messageLocalizer.format(
                            "plugin-class-cannot-instantiate",
                            new Object[]{pluginImplementationClass.getName(),
                                         pluginName}),
                    e);

            // Return the plugin.
            result = errorPlugin;

        } catch (IllegalAccessException e) {
            // Save the error plugin as this error will happen every time.
            errorPlugin = createErrorPlugin(
                    messageLocalizer.format(
                            "plugin-class-cannot-access",
                            new Object[]{pluginImplementationClass.getName(),
                                         pluginName}),
                    e);

            // Return the plugin.
            result = errorPlugin;
        }

        return result;
    }

    /**
     * Create an error plugin.
     *
     * <p>This first logs a warning and then delegates to {@link
     * #createErrorPluginImpl} to create an error plugin that will log a
     * warning every time one of its methods are called.</p>
     *
     * @param errorMsg  The (already localized) error message that indicates
     *                  why the real plugin could not be used.
     * @param throwable The exception or error that prevented the real plugin
     *                  from being created, may be null.
     * @return A plugin that logs an error.
     */
    protected final IntegrationPlugin createErrorPlugin(
            String errorMsg,
            Throwable throwable) {
        logger.warn("localized-message", errorMsg, throwable);

        return createErrorPluginImpl(errorMsg, throwable);
    }

    /**
     * Create an error plugin.
     *
     * @see #createErrorPlugin
     */
    protected abstract IntegrationPlugin createErrorPluginImpl(
            String errorMsg,
            Throwable throwable);

    /**
     * Base for those integration plugin classes.
     */
    protected static abstract class ErrorIntegrationPlugin
            implements IntegrationPlugin {

        /**
         * The class for the plugin interface.
         */
        protected final Class pluginInterfaceClass;

        /**
         * The name of the plugin.
         */
        protected final String pluginName;

        /**
         * The error message that indicates why the real plugin could not be
         * used.
         */
        protected final String errorMsg;

        /**
         * An optional error or exception that prevented the real plugin
         * from being created.
         */
        protected final Throwable throwable;

        /**
         * Create a new Error plugin to substitute for an plugin that failed to
         * initialize.
         *
         * @param pluginName The name of the plugin that has failed to
         *        initialize.
         * @param errorMsg The error that caused the plugin class to fail to
         *        initialize.
         */
        protected ErrorIntegrationPlugin(Class pluginInterfaceClass,
                                         String pluginName,
                                         String errorMsg,
                                         Throwable throwable) {
            this.pluginInterfaceClass = pluginInterfaceClass;
            this.pluginName = pluginName;
            this.errorMsg = errorMsg;
            this.throwable = throwable;
        }

        // Javadoc inherited.
        public void initialize(Map arguments) {
            logger.warn("plugin-initialization-problem",
                        new Object[]{pluginName,
                                     errorMsg,
                                     pluginInterfaceClass.getName()},
                        throwable);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
