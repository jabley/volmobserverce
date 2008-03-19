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
 * Copyright Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.api;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.LocalizationFactory;


/**
 * This class is designed to more easily handle attepmts at loading class files
 * that have alternate defaults.
 */
public class ExtensionClassLoader {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ExtensionClassLoader.class);

    private ExtensionClassLoader() {
        // this exists to make the class non-instantiable
    }

    /**
     * Load an extension class if it exists. If it does not exist then return
     * the default class in its place. This method only produces logs if in
     * debug mode. It is up to the loaded classes to notify the user via
     * logging, if necessary, that it has loaded.
     * <p>
     * This class returns an instance of the specified class or returns the
     * object passed to it. The specified extension class must have a zero
     * argument constructor.
     *
     * </p>
     *
     * @param extensionClassName the fully qualified class name of the class to
     * load
     * @param defaultObject the object to return if the extension can not be
     * found
     * @param classLoader The class loader to use to load the classes. This may
     * be null in which case the context classloader will be used. If the
     * Context classloader is null then the classloader of this class will be
     * used
     * @return the object that was requested to be loaded.
     * @throws RuntimeException if the extension and default classes could not
     * be loaded.
     */
    public static Object loadExtension(String extensionClassName,
                                       Object defaultObject,
                                       ClassLoader classLoader) {
        Object result = defaultObject;
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        // try to load the extension class
        try {
            Class clazz = null;
            if (classLoader == null) {
                clazz = Class.forName(extensionClassName);
            } else {
                clazz = classLoader.loadClass(extensionClassName);
            }
            result = clazz.newInstance();
        } catch (Exception e) {
            // catch all exceptions and hide them as we should now return the
            // default class
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("failed to load extension class", e);
            }
        }

        return result;
    }
}
