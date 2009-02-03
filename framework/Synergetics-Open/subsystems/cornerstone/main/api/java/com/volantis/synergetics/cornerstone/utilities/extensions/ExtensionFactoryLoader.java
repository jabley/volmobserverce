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
 * Copyright (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cornerstone.utilities.extensions;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is the base class for loading extensions. Because of code creep over
 * the years this seems the cleanest way of allowing arbitrary extensions to
 * be added to some bits of MCS. If you need a new extension point then
 * add a method to the Factories that use this helper. This is ugly.
 *
 * @volantis-api-include-in InternalAPI
 */
public class ExtensionFactoryLoader {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ExtensionFactoryLoader.class);

    /**
     * Initialize the ExtensionFactoryLoader. This method can only be called once it
     * will throw a RuntimeException if called more then once.
     *
     * @param className the name of the class to load
     * @param classloader the classloader to load it with (may be null)
     * @return the specified extension factory
     */
    public static Extension createExtensionFactory(
            String className, ClassLoader classloader) {

        Extension defaultFactory = null;

        if (className != null) {
            try {
                defaultFactory = loadFactory(className, classloader);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.info("loaded-extension:",
                            className);
                }
            } catch (ExtensionException e) {
                LOGGER.debug("Extension factory not found: " +className);
            }
        }
        return defaultFactory;
    }

    /**
     * Instantiate the class specified by <code>factoryClassName</code> using
     * the specified classloader
     *
     * @param factoryClassName the class name of the factory to load
     * @param classLoader the classloader to load the factory (may be null)
     * @return an instance of the specified factory
     * @throws ExtensionException if the factory class could not be loaded
     */
    private static Extension loadFactory(
            final String factoryClassName,
            ClassLoader classLoader)
            throws ExtensionException {

        Extension factoryInstance;
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        try {
            Class implClass =
                    classLoader.loadClass(factoryClassName);
            factoryInstance = (Extension) implClass.newInstance();
        } catch (Exception e) {
            throw new ExtensionException(e);
        }

        return  factoryInstance;
    }
}
