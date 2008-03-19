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

import com.volantis.impl.mcs.runtime.plugin.IntegrationPluginFactoryImpl;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.IntegrationPlugin;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Map;

/**
 * This factory creates instances of {@link MarkupPlugin} from configuration
 * information provided on construction.  If the configuration states that the
 * plugin has application level scope then the same instance is always returned
 * by the factory.  Otherwise a new instance is returned each time.
 *
 * If an error occurs in attempting to create an instance of
 * {@link MarkupPlugin} then a error handling instance is returned instead.
 */
public class MarkupPluginFactoryImpl
        extends IntegrationPluginFactoryImpl {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MarkupPluginFactoryImpl.class);

    /**
     * Create a new instance of MarkupPluginConfiguration.
     * @param config The configuration information required to create an
     * instance of a {@link MarkupPlugin}
     */
    public MarkupPluginFactoryImpl(MarkupPluginConfiguration config,
                                   MarinerApplication application) {
        super(config, application, MarkupPlugin.class);
    }

    public MarkupPluginFactoryImpl(IntegrationPlugin errorPlugin) {
        super(errorPlugin);
    }

    protected IntegrationPlugin createErrorPluginImpl(String errorMsg,
                                                      Throwable throwable) {

        return createErrorMarkupPlugin(pluginInterfaceClass, pluginName,
                                       errorMsg, throwable);
    }

    public static MarkupPlugin createErrorMarkupPlugin(
            Class pluginInterfaceClass,
            String pluginName, String errorMsg, Throwable throwable) {

        return new ErrorMarkupPlugin(pluginInterfaceClass, pluginName,
                                     errorMsg, throwable);
    }

    /**
     * This class allows us to gracefully fail those MarkupPlugins which failed
     * to initialize.  This MarkupPlugin is returned instead of the failed
     * plugin and logs warnings when the interface methods are called.
     */
    protected static class ErrorMarkupPlugin
            extends ErrorIntegrationPlugin
            implements MarkupPlugin {

        /**
         * Initialise new instance.
         */
        private ErrorMarkupPlugin(Class pluginInterfaceClass,
                                  String pluginName,
                                  String errorMsg,
                                  Throwable throwable) {

            super(pluginInterfaceClass, pluginName, errorMsg, throwable);
        }

        // Javadoc inherited.
        public void process(MarinerRequestContext context, Map arguments) {
            logger.warn("plugin-process-problem",
                        new Object[]{pluginName,
                                     errorMsg,
                                     pluginInterfaceClass.getName()});
        }

        // Javadoc inherited.
        public void release() {
            // do nothing.
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 25-Jan-05	6712/8	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
