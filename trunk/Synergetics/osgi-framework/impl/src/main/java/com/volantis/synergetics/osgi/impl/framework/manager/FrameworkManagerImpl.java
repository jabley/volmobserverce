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

package com.volantis.synergetics.osgi.impl.framework.manager;

import com.volantis.synergetics.osgi.framework.FrameworkManager;
import com.volantis.synergetics.osgi.impl.framework.bridge.BridgeServiceHelper;
import com.volantis.synergetics.osgi.impl.framework.bridge.BridgeServiceRegistration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;

public class FrameworkManagerImpl
        implements FrameworkManager {

    private final ClassLoader contextClassLoader;
    private final BundleContext bundleContext;

    public FrameworkManagerImpl(
            BundleContext bundleContext, ClassLoader contextClassLoader) {
        this.bundleContext = bundleContext;
        this.contextClassLoader = contextClassLoader;
    }

    // Javadoc inherited.
    public ServiceRegistration registerExportedBridgeService(
            Class clazz, Object service,
            Dictionary properties) {

        // Copy the properties from the dictionary into a new hash table. 
        Dictionary extended = BridgeServiceHelper.addBridgeServiceProperty(
                properties, BridgeServiceHelper.VOLANTIS_EXPORT_SERVICE);

        Object proxy = createContextSwitchingProxy(clazz, service);

        final ServiceRegistration registration =
                bundleContext.registerService(clazz.getName(), proxy, extended);

        // Wrap the registration to ensure that if the properties are updated
        // the additional property is not lost.
        return new BridgeServiceRegistration(
                registration
        );
    }

    // Javadoc inherited.
    public Object getImportedBridgeService(Class clazz) {

        ServiceReference[] references;
        try {
            references = bundleContext.getServiceReferences(
                    clazz.getName(),
                    BridgeServiceHelper.VOLANTIS_IMPORTED_SERVICE_FILTER);
        } catch (InvalidSyntaxException e) {
            IllegalStateException ise = new IllegalStateException();
            ise.initCause(e);
            throw ise;
        }

        Object service = null;
        if (references != null) {
            ServiceReference reference = references[0];
            service = bundleContext.getService(reference);
        }
        return service;
    }

    // Javadoc inherited.
    public Object createContextSwitchingProxy(Class clazz, Object object) {
        return BridgeServiceHelper.createContextSwitchingProxy(
                contextClassLoader, clazz, object);
    }
}
