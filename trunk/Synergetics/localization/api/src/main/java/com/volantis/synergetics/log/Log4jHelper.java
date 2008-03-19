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
package com.volantis.synergetics.log;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggerRepository;

/**
 * This is a helper class for the Log4j logger. It contains methods to
 * initialise and shutdown Log4j.
 */
public class Log4jHelper {

    /**
     * The localizer used to retrieve localized messages for exceptions.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(Log4jHelper.class);

    /**
     * Class only contains helper methods.
     */
    private Log4jHelper() {
    }

    /**
     * Initialise the log4j logger using the specified configuration file
     * locations. The search for the log4j configuration file proceeds as
     * follows:
     *
     * <ol> <li> If <code>log4jConfigLocation</code> is a valid configuration
     * file then use it. </li> <li> Otherwise, if <code>defaultLog4jLocation</code>
     * is a valid configuration file then use it. </li> <li> If a valid
     * configuration file has not been found, use a predefined logging
     * configuration which is shipped as a resource with the product using
     * logging. This is specified with <code>predefinedLog4jLocation</code>. A
     * warning is logged. </li> <li> If a configuration file has still not been
     * found then log this as an error. </li> </ol>
     *
     * @param log4jConfigLocation     this location is defined in terms of a
     *                                ServletContext resource or an absolute
     *                                filename
     * @param defaultLog4jLocation    this location is defined in terms of a
     *                                ServletContext resource or an absolute
     *                                filename
     * @param predefinedLog4jLocation this location is defined in terms of a
     *                                Java ClassLoader resource
     */
    public static void initializeLogging(String log4jConfigLocation,
                                         ConfigurationResolver resolver,
                                         String defaultLog4jLocation,
                                         String predefinedLog4jLocation) {
        // Keep track of a successful configuration.
        boolean isConfigured = true;

        if (log4jConfigLocation != null &&
            log4jConfigLocation.length() > 0) {
            // The log4j config file was specified as a context parameter in
            // the web.xml. Attempt to configure log4j using this.
            isConfigured = configureLog4j(log4jConfigLocation,
                                          resolver);

        } else if (defaultLog4jLocation != null &&
            defaultLog4jLocation.length() > 0) {
            // Try the default location for the log4j config file.
            isConfigured = configureLog4j(defaultLog4jLocation,
                                          resolver);
        }

        // If the config file has still not been found, fallback to the
        // config file shipped as a resource.
        if (!isConfigured) {
            // Attempt to configure log4j using this file.
            isConfigured = configureLog4j(predefinedLog4jLocation,
                                          new ClassLoaderConfigResolver());
        }

        // If the fallback has failed (it shouldn't) then log as an error.
        if (!isConfigured) {
            LogLog.error(exceptionLocalizer.
                         format("unable-to-configure-logging"));
        }
    }

    /**
     * Initialise the log4j logger using an appender.
     *
     * <strong>This method should only be called from CLI and GUI environments
     * where there won't be class loader clashes with respect to the static
     * Log4J hierarchy definitions.</strong>
     *
     * @param logToConsole true if the logger should log to the console; false
     *                     otherwise
     * @param appender     the appender to use. Can be null.
     */
    public static void initializeLogging(boolean logToConsole,
                                         AppenderSkeleton appender) {
        LoggerRepository repos = LogManager.getLoggerRepository();
        if (!logToConsole) {
            String params;
            if (appender == null) {
                params = "OFF";
            } else {
                // Set the hierarchy's threshold to match the one and
                // only appender
                params = appender.getThreshold().toString();
            }
                
            repos.setThreshold(params);
        }
    }

    /**
     * Helper method that attempts to configure log4j with the given log4j
     * configuration file location.
     *
     * @param log4jConfigLocation the log4j configuration location
     * @param resolver            the resolver to use for resolving the given
     *                            configuration location
     * @return true if configuration was successful; false otherwise
     */
    private static boolean configureLog4j(String log4jConfigLocation,
                                          ConfigurationResolver resolver) {
        final VolantisDOMConfigurator configurator =
            new VolantisDOMConfigurator();

        // Attempt to configure log4j.
        configurator.doConfigure(resolver,
                                 log4jConfigLocation,
                                 LogManager.getLoggerRepository());

        return configurator.isConfigured();
    }

    /**
     * Shuts down Log4J.
     */
    public static void shutdown() {

        LogManager.shutdown();
    }

    /**
     * An implementation of ConfigurationResolver that provides an InputStream
     * to a resource loaded by a ClassLoader. This is used to provide an
     * InputStream to the predefined Log4J configuration file shipped as part
     * of the product using the logging.
     */
    private static final class ClassLoaderConfigResolver
        implements ConfigurationResolver {

        /**
         * Creates and returns an InputStream to the given resource location.
         * This thread's context ClassLoader is tried first, followed by this
         * class's ClassLoader, and finally the system ClassLoader.
         *
         * @param resourceLocation the resource location
         * @return an InputStream for the resource
         *
         * @throws LogException if an InputStream could not be created
         */
        public InputStream createConfigInputStream(String resourceLocation)
            throws LogException {

            InputStream is = null;

            // Use the context class loader first if any.
            ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                is = classLoader.getResourceAsStream(resourceLocation);
            }

            // If that did not work then use the class loader of this class.
            if (is == null) {
                classLoader =
                    ClassLoaderConfigResolver.class.getClassLoader();
                is = classLoader.getResourceAsStream(resourceLocation);
            }

            // If that did not work then use the system class loader.
            if (is == null) {
                classLoader = ClassLoader.getSystemClassLoader();
                is = classLoader.getResourceAsStream(resourceLocation);
            }

            if (is == null) {
                throw new LogException(exceptionLocalizer.
                                       format("unable-to-read-resource",
                                              new Object[]{resourceLocation}));
            }

            return is;
        }

        /**
         * Overidden to do nothing as this resolver only needs to return an
         * InputStream for a class-loadable Java resource.
         */
        public File createConfigFile(String path) throws LogException {
            return null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-May-05	435/5	pcameron	VBM:2005040505 Fixed localisation of logging messages

 21-Apr-05	435/1	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	428/53	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	428/50	pcameron	VBM:2005040505 Logging initialisation changed

 ===========================================================================
*/
