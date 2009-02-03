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

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Class loader for use when booting the OSGi framework.
 *
 * <p>Attempts to load classes from its class path first, if they could not be
 * found then looks for them in the parent class loader. Ditto for
 * resources.</p>
 *
 * <p>The purpose is to protect the OSGi framework from being exposed to any
 * conflicting packages, and also to protect the environment from being exposed
 * to any OSGi framework classes.</p>
 *
 * <p>This relies on the OSGi framework to ensure that it only makes requests
 * to this class loader for classes that need to be accessed either from the
 * OSGi Framework / Boot JARs, or from the parent class loader.</p>
 */
public class OSGiClassLoader
        extends URLClassLoader {

    private final ClassLoader parent;

    /**
     * Initialise.
     *
     * @param urls           The URLs that form the class path.
     * @param parent         The parent class loader.
     */
    public OSGiClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);

        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null");
        }

        this.parent = parent;
    }

    /**
     * Override to change the order in which various class loaders are checked
     * for the class.
     *
     * @param name    The name of the class.
     * @param resolve Indicates whether the class should be resolved after
     *                loading.
     * @return The class that was loaded.
     * @throws ClassNotFoundException If no class could be found.
     */
    protected synchronized Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class c = findLoadedClass(name);
        if (c == null) {

            // If still not found, then invoke findClass in order
            // to find the class.
            try {
//                System.out.println("OCL: Looking for class " + name);
                c = findClass(name);
//                System.out.println("OCL: Found class " + name);
            } catch (ClassNotFoundException e) {
                // If it could not be found then as a last resort look in the
                // parent class loader.
//                System.out.println("OCL: Defaulting to load " + name +
//                        " from parent class loader");
                c = parent.loadClass(name);
//                System.out.println("OCL: Loaded " + name +
//                        " from parent class loader");
            }
        }

        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    public URL getResource(String name) {

        URL url = findResource(name);
        if (url == null) {
            url = parent.getResource(name);
        }

        return url;
    }
}
