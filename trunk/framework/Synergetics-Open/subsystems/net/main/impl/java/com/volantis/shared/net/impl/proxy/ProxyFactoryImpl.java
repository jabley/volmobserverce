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

package com.volantis.shared.net.impl.proxy;

import com.volantis.shared.net.proxy.ProxyFactory;
import com.volantis.shared.net.proxy.ProxyBuilder;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.system.SystemProperties;

/**
 * Default implementation of {@link ProxyFactory}.
 */
public class ProxyFactoryImpl
        extends ProxyFactory {

    /**
     * The system proxy manager, will be null if no proxy host has been
     * specified.
     */
    private final ProxyManager systemProxyManager;

    /**
     * Initialise.
     */
    public ProxyFactoryImpl() {
        this(SystemProperties.getDefaultInstance());
    }

    /**
     * Initialise.
     */
    public ProxyFactoryImpl(SystemProperties systemProperties) {
        // Assume that there is a system proxy and create a manager for it.
        // If the manager does not have a system proxy then discard the
        // manager.
        SystemProxyManager manager = new SystemProxyManager(this,
                systemProperties);
        if (manager.getSystemProxy() == null) {
            manager = null;
        }
        systemProxyManager = manager;
    }

    // Javadoc inherited.
    public ProxyBuilder createProxyBuilder() {
        return new ProxyBuilderImpl();
    }

    // Javadoc inherited.
    public ProxyManager getSystemProxyManager() {
        return systemProxyManager;
    }
}
