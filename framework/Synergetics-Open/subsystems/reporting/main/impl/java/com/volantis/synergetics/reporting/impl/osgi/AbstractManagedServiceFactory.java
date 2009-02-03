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
package com.volantis.synergetics.reporting.impl.osgi;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.reporting.impl.DefaultMetrics;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Hashtable;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.event.EventHandler;
import org.osgi.service.event.EventConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Base functionality for ReportEventHandler ManagedServiceFactories
 */
public abstract class AbstractManagedServiceFactory
    implements ManagedServiceFactory {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory
            .createExceptionLocalizer(AbstractManagedServiceFactory.class);

    /**
     * This holds the registered listeners
     */
    private final Map reportHandlers = new HashMap();

    /**
     * The bundle context.
     */
    private final BundleContext context;

    /**
     * Map from PIDs to {@link org.osgi.framework.ServiceRegistration}s.
     */
    private final Map registrations = new HashMap();

    /**
     * Creates a new instance bound to the specified bundle context.
     *
     * @param context the bundle context.
     */
    protected AbstractManagedServiceFactory(final BundleContext context) {
        this.context = context;
    }

    // Javadoc inherited
    public void updated(String pid, Dictionary configuration)
            throws ConfigurationException {

        if (null == pid) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format(
                    "argument-must-not-be",
                    new Object[]{"pid", "null"}));
        }

        if (null == configuration) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format(
                    "argument-must-not-be",
                    new Object[]{"configuration", "null"}));
        }

        synchronized (reportHandlers) {
            if (reportHandlers.containsKey(pid)) {
                ReportEventHandler handler =
                    (ReportEventHandler) reportHandlers.get(pid);
                handler.setConfiguration(configuration);
            } else {
                // create the handler and register it
                ReportEventHandler handler =
                    createEventHandler(configuration);
                final Dictionary properties = new Hashtable();
                properties.put(EventConstants.EVENT_TOPIC,
                    new String[]{DefaultMetrics.TOPIC});
                final ServiceRegistration registration =
                    context.registerService(
                        EventHandler.class.getName(), handler, properties);
                registrations.put(pid, registration);
                reportHandlers.put(pid, handler);
            }
        }
    }

    /**
     * Create a new fully configured event handler instance to store in this
     * abstract cache.
     *
     * @param configuration the configuration object used to configure the
     *                      created EventHandler
     * @return a fully configured EventHandler instance.
     *
     * @throws ConfigurationException if there is an error during the
     *                                configuration
     */
    protected abstract ReportEventHandler createEventHandler(
        Dictionary configuration)
        throws ConfigurationException;

    // Javadoc inherited
    public void deleted(String pid) {
        if (null == pid) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format(
                    "argument-must-not-be",
                    new Object[]{"configuration", "null"}));
        }
        // just remove anything associated with that pid from the map
        synchronized (reportHandlers) {
            reportHandlers.remove(pid);
            final ServiceRegistration registration =
                (ServiceRegistration) registrations.remove(pid);
            if (registration != null) {
                registration.unregister();
            }
        }
    }
}
