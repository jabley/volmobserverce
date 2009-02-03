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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi.impl.framework.bridge;

import com.volantis.synergetics.osgi.framework.boot.ContextSwitchingProxyHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Provides helper methods for use by bridge services.
 */
public class BridgeServiceHelper {

    /**
     * The property that is associated with a service that bridges between the
     * outside of the framework and the inside.
     */
    public static final String VOLANTIS_BRIDGE_SERVICE =
            "volantis.bridge.service";

    /**
     * Indicates that a service is to be exported from the framework.
     */
    public static final String VOLANTIS_EXPORT_SERVICE = "export";

    /**
     * Indicates that a service is to be imported intp the framework.
     */
    public static final String VOLANTIS_IMPORT_SERVICE = "import";

    /**
     * The filter to use to check for imported bridge services.
     */
    public static final String VOLANTIS_IMPORTED_SERVICE_FILTER =
            "(" + VOLANTIS_BRIDGE_SERVICE + "=" +
                    VOLANTIS_IMPORT_SERVICE + ")";

    /**
     * The filter to use to check for exported bridge services.
     */
    public static final String VOLANTIS_EXPORTED_SERVICE_FILTER =
            "(" + VOLANTIS_BRIDGE_SERVICE + "=" +
                    VOLANTIS_EXPORT_SERVICE + ")";

    /**
     * Create a proxy wrapper around the specified object implementing the
     * specified interface and switching to the specified class loader before
     * invoking the method on the specified object.
     *
     * @param contextClassLoader The class loader to switch to.
     * @param clazz The interface.
     * @param object The object to wrap.
     * @return The proxy object.
     */
    public static Object createContextSwitchingProxy(
            ClassLoader contextClassLoader, Class clazz, Object object) {

        InvocationHandler contextSwitchingHandler =
                new ContextSwitchingProxyHandler(
                        object, contextClassLoader);
        return Proxy.newProxyInstance(contextClassLoader,
                new Class[]{clazz}, contextSwitchingHandler);

    }

    /**
     * Add an additional property to the properties to indicate that the
     * service is visible externally.
     *
     * @param properties The properties.
     * @param bridgeType
     * @return The new dictionary with the new property in.
     */
    public static Dictionary addBridgeServiceProperty(
            Dictionary properties, String bridgeType) {
        Dictionary extended = new Hashtable();
        if (properties != null) {
            Enumeration enumeration = properties.keys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                Object value = properties.get(key);
                extended.put(key, value);
            }
        }
        extended.put(VOLANTIS_BRIDGE_SERVICE, bridgeType);
        return extended;
    }
}
