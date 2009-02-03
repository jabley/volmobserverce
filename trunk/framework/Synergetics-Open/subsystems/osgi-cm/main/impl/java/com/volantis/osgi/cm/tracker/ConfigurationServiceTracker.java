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

package com.volantis.osgi.cm.tracker;

import com.volantis.osgi.cm.ConfigurationManager;
import com.volantis.osgi.cm.ServiceHandler;
import com.volantis.osgi.cm.factory.ManagedServiceFactoryHandler;
import com.volantis.osgi.cm.plugin.ConfigurationPluginHandler;
import com.volantis.osgi.cm.plugin.PluginManager;
import com.volantis.osgi.cm.service.ManagedServiceHandler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Tracks the services that are of interest to the {@link ConfigurationManager}
 * and {@link PluginManager} and informs them of changes in their state.
 */
public class ConfigurationServiceTracker {

    /**
     * The handlers for the different types of service.
     */
    private final ServiceHandler[] serviceHandlers;

    /**
     * The underlying service tracker.
     */
    private final ServiceTracker tracker;

    /**
     * Initialise.
     *
     * @param bundleContext The {@link ConfigurationManager} bundle context.
     * @param manager       The {@link ConfigurationManager}.
     * @param pluginManager The {@link PluginManager}.
     */
    public ConfigurationServiceTracker(
            BundleContext bundleContext,
            ConfigurationManager manager,
            PluginManager pluginManager) {

        serviceHandlers = new ServiceHandler[]{
                new ManagedServiceHandler(manager),
                new ManagedServiceFactoryHandler(manager),
                new ConfigurationPluginHandler(pluginManager)
        };

        // Create a filter that will only select those services that are of the
        // same type as we care about.
        StringBuffer buffer = new StringBuffer();
        buffer.append("(|");
        for (int i = 0; i < serviceHandlers.length; i++) {
            ServiceHandler serviceHandler = serviceHandlers[i];
            String registeredClass = serviceHandler.getRegisteredClass();
            buffer.append("(objectClass=").append(registeredClass).append(")");
        }
        buffer.append(")");

        Filter filter;
        try {
            filter = bundleContext.createFilter(buffer.toString());
        } catch (InvalidSyntaxException e) {
            // Should never happen but throw an exception anyway just to be on
            // the safe side.
            IllegalStateException ise = new IllegalStateException();
            ise.initCause(e);
            throw ise;
        }

        // Create a tracker for tracking all bundles which this is interested
        // in.
        tracker = new ServiceTracker(bundleContext, filter,
                new Customizer(bundleContext));
    }

    /**
     * Start tracking services.
     */
    public void start() {
        tracker.open(false);
    }

    /**
     * Stop tracking services.
     */
    public void stop() {
        tracker.close();
    }

    /**
     * The {@link ServiceTrackerCustomizer} that is invoked by the {@link
     * ServiceTracker}.
     */
    private class Customizer
            implements ServiceTrackerCustomizer {

        /**
         * The bundle context.
         */
        private final BundleContext bundleContext;

        /**
         * Initialise.
         *
         * @param bundleContext The bundle context.
         */
        public Customizer(BundleContext bundleContext) {
            this.bundleContext = bundleContext;
        }

        /**
         * Get the appropriate {@link ServiceHandler} for the reference.
         *
         * @param reference The reference of the service being tracked.
         * @return The {@link ServiceHandler}, or null if no suitable one could
         *         be found.
         */
        private ServiceHandler getServiceHandler(ServiceReference reference) {

            String[] registeredClasses = (String[])
                    reference.getProperty(Constants.OBJECTCLASS);

            for (int i = 0; i < registeredClasses.length; i++) {
                String registeredClass = registeredClasses[i];

                // Determine if the service that was registered is one of the
                // special ones that we care about. If not ignore it and carry on.
                for (int j = 0; j < serviceHandlers.length; j++) {
                    ServiceHandler handler = serviceHandlers[j];
                    if (registeredClass
                            .equals(handler.getRegisteredClass())) {
                        return handler;
                    }
                }
            }

            return null;
        }

        // Javadoc inherited.
        public Object addingService(ServiceReference reference) {
            ServiceHandler handler = getServiceHandler(reference);
            if (handler == null) {
                // Not one that we wanted, probably an error somewhere but
                // just ignore it.
                return null;
            }

            handler.serviceRegistered(reference);

            // Increment the use count on the service so that it doesn't go
            // away. Otherwise, delayed declarative services components
            // could be deactivated.
            bundleContext.getService(reference);

            return handler;
        }

        // Javadoc inherited.
        public void modifiedService(
                ServiceReference reference, Object service) {

            ServiceHandler handler = (ServiceHandler) service;

            handler.serviceModified(reference);
        }

        // Javadoc inherited.
        public void removedService(
                ServiceReference reference, Object service) {

            ServiceHandler handler = (ServiceHandler) service;

            handler.serviceUnregistering(reference);

            // Decrement the use count on the service as we are not interested
            // in it anymore.
            bundleContext.ungetService(reference);
        }
    }
}
