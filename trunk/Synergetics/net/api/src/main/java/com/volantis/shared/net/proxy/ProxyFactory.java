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

package com.volantis.shared.net.proxy;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * A factory for {@link Proxy} related classes.
 */
public abstract class ProxyFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.shared.net.impl.proxy.ProxyFactoryImpl",
                        ProxyFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ProxyFactory getDefaultInstance() {
        return (ProxyFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create a {@link ProxyBuilder}.
     *
     * @return A {@link ProxyBuilder}.
     */
    public abstract ProxyBuilder createProxyBuilder();

    /**
     * Get the {@link ProxyManager} created by the system, may be null.
     *
     * @return The {@link ProxyManager} for the system, or null if none was
     *         specified.
     */
    public abstract ProxyManager getSystemProxyManager();
}
