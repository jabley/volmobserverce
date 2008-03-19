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

package com.volantis.synergetics.osgi.framework;

import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;

/**
 * A service available inside the framework for use by services.
 *
 * @see com.volantis.synergetics.osgi.OSGiManager for details of bridge service.
 */
public interface FrameworkManager {

    /**
     * Exports a bridge service so that it can only be accessed externally,
     * i.e. outside the OSGi framework.
     *
     * <p>This invokes {@link #createContextSwitchingProxy(Class,Object)} to
     * wrap the service before it registers it. It also adds a special property
     * that ensures it can only be found from outside the framework.</p>
     *
     * @param clazz      The class name under which the service can be located.
     * @param service    The service object or a <code>ServiceFactory</code>
     *                   object.
     * @param properties The properties for this service. The keys in the
     *                   properties object must all be <code>String</code>
     *                   objects.. The set of properties may be
     *                   <code>null</code> if the service has no properties.
     * @throws java.lang.IllegalStateException
     *          If this BundleContext is no longer valid.
     */
    ServiceRegistration registerExportedBridgeService(
            Class clazz, Object service,
            Dictionary properties);

    /**
     * Get the bridge service imported from within the framework.
     *
     * <p>This will only retrieve services that have been explicitly exported
     * from within the OSGi framework.</p>
     *
     * @param clazz The class of the service interface.
     * @return The service, or null if it could not be found.
     */
    Object getImportedBridgeService(Class clazz);
    
    /**
     * Create a context switching proxy for the object.
     *
     * <p>The specified clazz must be an interface as this creates a
     * {@link java.lang.reflect.Proxy} object that ensures that every call
     * through the service interface uses the context class loader for the
     * OSGi framework. This will protect the framework from inadvertently
     * accessing external classes through the context class loader.</p>
     *
     * @param clazz  The interface that the proxy must implement.
     * @param object The object to proxy.
     * @return The proxy object.
     */
    Object createContextSwitchingProxy(Class clazz, Object object);
}
