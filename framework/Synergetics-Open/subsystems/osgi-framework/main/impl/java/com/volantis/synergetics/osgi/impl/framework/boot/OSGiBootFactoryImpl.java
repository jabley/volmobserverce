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

package com.volantis.synergetics.osgi.impl.framework.boot;

import com.volantis.synergetics.osgi.OSGiManager;
import com.volantis.synergetics.osgi.boot.OSGiBootFactory;
import com.volantis.synergetics.osgi.framework.boot.ContextSwitchingProxyHandler;
import com.volantis.synergetics.osgi.impl.framework.equinox.EquinoxManagerImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Implementation of the {@link OSGiBootFactory}.
 *
 * <p>Creates an Equinox based OSGi framework.</p>
 */
public class OSGiBootFactoryImpl
        extends OSGiBootFactory {

    // Javadoc inherited.
    public OSGiManager createManager(Map properties)
            throws Exception {

        OSGiManager equinox;

        // Equinox installs a special context class loader that will redirect
        // any requests to it back to the bundle or framework class loader for
        // the calling bundle. Unfortunately, if this class loader is left
        // then it can cause some errors in the application container. Save
        // the application container class loader away to be restored later
        // and create the Equinox OSGi framework. Once that has been done get
        // the context class loader that it has set and save it away so that it
        // can be set before entering the OSGi framework.
        Thread current = Thread.currentThread();
        ClassLoader applicationCCL = current.getContextClassLoader();
        ClassLoader osgiCCL;
        try {
            equinox = new EquinoxManagerImpl(properties);

            // Get the ContextFinder class loader. This must not be wrapped
            // by any classes loaded by the current class loader as otherwise
            // it will prevent classes from being loaded by a bundle class
            // loader if necessary and they will always be loaded by the
            // framework class loader.
            osgiCCL = current.getContextClassLoader();
        } finally {
            current.setContextClassLoader(applicationCCL);
        }

        // Wrap the manager in a context switching one that will ensure that
        // the context class loader is correctly set up on any call to the
        // manager.
        InvocationHandler contextSwitchingHandler =
                new ContextSwitchingProxyHandler(
                        equinox, osgiCCL);
        return (OSGiManager) Proxy.newProxyInstance(osgiCCL,
                new Class[]{OSGiManager.class}, contextSwitchingHandler);
    }
}
