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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.impl.directory.monitor;

import org.osgi.framework.ServiceFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;

/**
 * Factory used by OSGi to create a Service instance especially tailored for
 * a particular bundle.
 */
public class DirectoryMonitorFactory implements ServiceFactory {

    private final DirectoryMonitorRegistry registry =
        new DirectoryMonitorRegistry(true);

    /**
     * Called by OSGi when a Component-Service is activates to return the
     * implementation of that service.
     *
     * @param bundle the bundle this Component-Service is being activated for
     * @param serviceRegistration
     * @return the implementation of the Service.
     */
    public Object getService(Bundle bundle,
                             ServiceRegistration serviceRegistration) {
        return new DefaultDirectoryMonitor(bundle.getBundleId(), registry);
    }

    /**
     * Called by OSGi when a Component-Service is no longer needed by a
     * particular bundle.
     *
     * @param bundle the bundle that no longer needs the service
     * @param serviceRegistration
     * @param object the implementation that was being used
     */
    public void ungetService(Bundle bundle,
                             ServiceRegistration serviceRegistration,
                             Object object) {
        registry.unregister(bundle.getBundleId());
    }
}
