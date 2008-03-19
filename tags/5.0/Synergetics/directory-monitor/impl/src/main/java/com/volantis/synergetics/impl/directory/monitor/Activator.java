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

import com.volantis.synergetics.directory.monitor.DirectoryMonitor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Registers the DirectoryMonitor Service
 */
public class Activator
    implements BundleActivator {

    /**
     * The registration of the DirectoryMonitor Service
     */
    private ServiceRegistration serviceRegistration = null;

    // Javadoc inherited.
    public void start(BundleContext bundleContext) throws Exception {
        serviceRegistration = bundleContext.registerService(
                DirectoryMonitor.class.getName(),
                new DirectoryMonitorFactory(), null);
    }

    // Javadoc inherited.
    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.ungetService(serviceRegistration.getReference());
    }
}
