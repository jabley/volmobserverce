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

package com.volantis.osgi.j2ee.bridge.http.service;

import com.volantis.synergetics.osgi.framework.FrameworkManager;
import com.volantis.synergetics.osgi.j2ee.bridge.http.HttpBridge;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

import javax.servlet.ServletContext;

/**
 * Registers two services, the bridge service that is used by a servlet in the
 * outermost container to dispatch requests to the {@link HttpService}, and the
 * {@link HttpService} itself.
 */
public class Activator
        implements BundleActivator {

    /**
     * The registration of the bridge service.
     */
    private ServiceRegistration bridgeRegistration;

    /**
     * The registration of the http service.
     */
    private ServiceRegistration serviceRegistration;

    // Javadoc inherited.
    public void start(BundleContext bundleContext) throws Exception {

        ServletContext servletContext = (ServletContext)
                getService(bundleContext, ServletContext.class);

        FrameworkManager manager = (FrameworkManager)
                getService(bundleContext, FrameworkManager.class);

        ServletRegistry registry = new ServletRegistry(servletContext);

        // Create the bridge.
        HttpBridgeService bridge = new HttpBridgeService(registry);

        // Register the bridge using the manager so that it can
        // be accessed from outside the OSGi framework.
        bridgeRegistration = manager.registerExportedBridgeService(
                HttpBridge.class,
                bridge, null);

        serviceRegistration = bundleContext.registerService(
                HttpService.class.getName(),
                new HttpServiceFactory(registry), null);
    }

    /**
     * Get the service for the specified class.
     *
     * @param bundleContext The current bundle context.
     * @param clazz         The clazz that the service must implement.
     * @return The service.
     * @throws IllegalArgumentException If the bundle could not be found.
     */
    private Object getService(BundleContext bundleContext, Class clazz) {
        String className = clazz.getName();

        ServiceReference contextReference =
                bundleContext.getServiceReference(className);
        if (contextReference == null) {
            throw new IllegalStateException(
                    "Could not find " + className +
                            " service");
        }
        Object service = bundleContext.getService(contextReference);
        if (service == null) {
            throw new IllegalStateException(
                    "Found reference to " + className +
                            " service but could not retrieve it");
        }
        return service;
    }

    // Javadoc inherited.
    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.ungetService(serviceRegistration.getReference());
        bundleContext.ungetService(bridgeRegistration.getReference());
    }
}
