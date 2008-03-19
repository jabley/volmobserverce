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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi.boot;

import com.volantis.synergetics.osgi.OSGiManager;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Factory for creating objects relating to booting up an OSGi framework.
 */
public abstract class OSGiBootFactory {

    /**
     * The name of the implementation class.
     */
    private static final String FACTORY_IMPL =
            "com.volantis.synergetics.osgi.impl.framework.boot.OSGiBootFactoryImpl";

    /**
     * Get the default instance of this factory.
     *
     * @param properties The set of properties that specify where
     * @return The default instance of this factory.
     */
    public static OSGiBootFactory createInstance(Map properties) {

        // Get the list of URLs that form the class path needed to boot OSGi,
        // this must include the OSGi interface and implementation JARs as well
        // as the JAR containing the implementation of the Volantis OSGi boot
        // interface.
        List urls = (List) properties.remove(BootConstants.BOOT_CLASSPATH);
        URL[] array = new URL[urls.size()];
        urls.toArray(array);

        // Create the class loader within which the OSGi framework will live.
        // This is needed in order to protect the whole OSGi framework from its
        // environment and vice versa.
        ClassLoader loader = new OSGiClassLoader(
                array, OSGiBootFactory.class.getClassLoader());

        OSGiBootFactory factory = null;
        try {
            Class clazz = loader.loadClass(FACTORY_IMPL);

            factory = (OSGiBootFactory) clazz.newInstance();

        } catch (ClassNotFoundException e) {
            IllegalStateException exception = new IllegalStateException(
                    "Could not load " + FACTORY_IMPL + " from " + urls);
            exception.initCause(e);
            throw exception;
        } catch (IllegalAccessException e) {
            IllegalStateException exception = new IllegalStateException("Could not construct " +
                    FACTORY_IMPL +
                    " from " + urls);
            exception.initCause(e);
            throw exception;
        } catch (InstantiationException e) {
            IllegalStateException exception = new IllegalStateException("Could not construct " +
                    FACTORY_IMPL +
                    " from " + urls);
            exception.initCause(e);
            throw exception;
        }

        return factory;
    }

    /**
     * Create the {@link OSGiManager} for an OSGi framework.
     *
     * @param properties The set of properties to pass to the manager.
     * @return The newly created manager.
     */
    public abstract OSGiManager createManager(Map properties)
            throws Exception;
}
