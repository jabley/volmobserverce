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

package com.volantis.osgi.cm.factory;

import com.volantis.osgi.cm.Association;
import com.volantis.osgi.cm.ConfigurationSnapshot;
import com.volantis.osgi.cm.InternalConfiguration;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.log.LogService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the association between a number (usually one) of {@link
 * ManagedServiceFactory} instances and a number of configurations.
 *
 * <p>The association is bound to a particular bundle only when a {@link
 * ManagedServiceFactory} with a matching factory PID is registered. Until then
 * it remains unbound. The configurations however, may be bound either when they
 * are created through the admin service, or when the association is bound. The
 * configurations can be bound to different locations to the association. In
 * this situation only those configurations that are bound to the correct
 * location are passed to the factory. The others are just ignored.</p>
 */
public final class FactoryAssociation
        extends Association {

    /**
     * A list of the configurations.
     */
    private final List/*<InternalConfiguration>*/ configurations;

    /**
     * Initialise.
     *
     * @param log        The log service.
     * @param servicePid The pid of the service to which this is bound.
     */
    public FactoryAssociation(LogService log, String servicePid) {
        super(log, servicePid);

        this.configurations = new ArrayList();
    }

    /**
     * Add a configuration.
     *
     * @param configuration The configuration to add.
     */
    public void addConfiguration(InternalConfiguration configuration) {
        /// todo ensure that pid is unique.
        configurations.add(configuration);
    }

    /**
     * Remove the configuration with the specified pid.
     *
     * @param pid The pid of the configuration to remove.
     */
    public void removeConfiguration(String pid) {
        for (int i = 0; i < configurations.size(); i++) {
            InternalConfiguration configuration =
                    (InternalConfiguration) configurations.get(i);
            if (configuration.getPid().equals(pid)) {
                configurations.remove(i);
                return;
            }
        }

        // todo deal with the situation when the configuration could not be
        // todo found.
    }

    /**
     * Create snapshots of all those configurations that are bound to the same
     * location as the service.
     *
     * @param reference The service reference for whom the snapshots are
     *                  needed.
     * @return The snapshots.
     */
    public ConfigurationSnapshot[] createSnapshots(ServiceReference reference) {
        Collection snapshots = new ArrayList();
        for (int i = 0; i < configurations.size(); i++) {
            InternalConfiguration configuration =
                    (InternalConfiguration) configurations.get(i);

            String configurationLocation =
                    configuration.getBundleLocation();
            if (bundleLocation.equals(configurationLocation)) {
                snapshots.add(configuration.createSnapshot());
            }
        }

        ConfigurationSnapshot[] array =
                new ConfigurationSnapshot[snapshots.size()];
        snapshots.toArray(array);
        return array;
    }

    // Javadoc inherited.
    public boolean bindToLocation(ServiceReference reference) {
        if (!super.bindToLocation(reference)) {
            return false;
        }

        // Bind all the configurations that have not been explicitly bound.
        for (int i = 0; i < configurations.size(); i++) {
            InternalConfiguration configuration =
                    (InternalConfiguration) configurations.get(i);

            String configurationLocation =
                    configuration.getBundleLocation();
            if (configurationLocation == null) {
                // The configuration should be bound to the bundle of the
                // first service that registers.
                configuration.setSpecifiedLocation(bundleLocation);
            } else if (!bundleLocation.equals(configurationLocation)) {
                log.log(reference, LogService.LOG_WARNING,
                        "Configuration with pid '" + configuration.getPid() +
                                "' for factory '" + servicePid +
                                "' is bound to '" + configurationLocation +
                                "' instead of '" + bundleLocation +
                                "' and so will be ignored");
            }
        }

        return true;
    }

    // Javadoc inherited.
    public boolean unbindFromLocation(ServiceReference reference) {
        if (!super.unbindFromLocation(reference)) {
            return false;
        }

        // Unbind the configurations, which means revert to the specified
        // bundle location.
        for (int i = 0; i < configurations.size(); i++) {
            InternalConfiguration configuration =
                    (InternalConfiguration) configurations.get(i);

            configuration.unbindFromLocation();
        }

        return true;
    }

    // Javadoc inherited.
    public boolean hasConfiguration() {
        return !configurations.isEmpty();
    }
}
