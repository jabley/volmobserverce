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

package com.volantis.synergetics.osgi;

import java.util.Dictionary;

/**
 * External manager of an OSGi framework.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * <h2>Bridge Services</h2>
 *
 * <p>Sometimes it is necessary for the code outside the OSGi framework and the
 * code inside to communicate. While this should be kept to an absolute minimum
 * in some cases it is unavoidable. This is where bridge services are used.
 * A bridge service is simply an OSGi service that is used to bridge the gap
 * created by the OSGi framework.</p>
 *
 * <p>Although the OSGi framework does protect itself against the environment
 * there is one place where that protection can break down and that is with
 * the context class loader. The OSGi framework needs to set its own context
 * class loader to make sure that bundles cannot use it to circumvent its
 * module layer. Similarly, the environment needs to use its own class loader
 * to ensure that it does not access classes from within the OSGi
 * framework.</p>
 *
 * <p>As bridge services cross the gap between the environment and the
 * framework they need to make sure that the context class loader is set up
 * correctly for the code that is about to be executed. Ideally, this would
 * mean that every method on every object that is passed between the two is
 * modified to set the context class loader before invoking any code and reset
 * it afterwards.</p>
 *
 * <p>At the moment only interfaces can be used as services because that allows
 * a {@link java.lang.reflect.Proxy} objects to be automatically created to
 * wrap the service before it is registered with OSGi. Unfortunately, this does
 * not solve all the problem as it does not deal with objects passed as
 * parameters and returned as values. These need to be explicitly handled by
 * the calling code and the returning code respectively.</p> 
 *
 * <p>Bridge services that only use methods that return primitive values (or
 * their autoboxed objects), {@link String}s, or arrays of the previous types
 * are safe but services that return user defined objects may not be as the
 * objects may make use of the context class loader. In this case the objects
 * must be wrapped either in a {@link java.lang.reflect.Proxy}, or in some
 * custom code.</p>
 *
 * <p>As special care has to be taken when providing and using them only those
 * services that have been explicitly exported should be accessible from
 * outside the framework and so exported services have an additional property
 * to control that and the method provided to access the services uses a
 * filter to ensure that service was exported. The same applies to imported
 * services but unfortunately, it is not possible to prevent access to those
 * services by the normal mechanism as there is no way to say that the service
 * must be retrieved by a filter. Care must therefore be taken to ensure that
 * imported bridge services are not misused.</p>
 */
public interface OSGiManager {

    /**
     * Start the OSGi framework.
     */
    void start();

    /**
     * Imports the specified bridge service so that it can only be accessed
     * internally, i.e. from within the OSGi framework.
     *
     * <p>This invokes {@link #createContextSwitchingProxy(Class,Object)} to
     * wrap the service before it registers it. It also adds a special property
     * that ensures it can only be found from inside the framework.</p>
     *
     * <p>Once a service has been registered with this it cannot be
     * unregistered.</p>
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
    void registerImportedBridgeService(
            Class clazz, Object service, Dictionary properties);

    /**
     * Get the bridge service exported from within the framework.
     *
     * <p>This will only retrieve services that have been explicitly exported
     * from within the OSGi framework.</p>
     *
     * @param clazz The class of the service interface.
     * @return The service, or null if it could not be found.
     */
    Object getExportedBridgeService(Class clazz);

    /**
     * Create a context switching proxy for the object.
     *
     * <p>This switches to the context class loader for the application
     * container before invoking a method and switches back afterwards.</p>
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

    /**
     * Stop the OSGi framework.
     */
    void stop() throws Exception;
}
