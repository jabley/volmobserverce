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

package com.volantis.osgi.cm.service;

import com.volantis.osgi.cm.Association;
import com.volantis.osgi.cm.InternalConfiguration;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

/**
 * Represents the association between a number (usually one) of {@link
 * ManagedService} instances and a single configuration.
 *
 * <p>The association is bound to a particular bundle only when a {@link
 * ManagedService} with a matching PID is registered. Until then it remains
 * unbound. The configuration however, may be bound either when it is created
 * through the admin service, or when the association is bound. The
 * configuration can be bound to a different location to the association. In
 * this situation the configuration is ignored.</p>
 */
public class ServiceAssociation
        extends Association {

    /**
     * The single configuration.
     */
    private InternalConfiguration configuration;

    /**
     * Initialise.
     *
     * @param configuration The configuration, may be null.
     */
    public ServiceAssociation(
            LogService log,
            String servicePid,
            InternalConfiguration configuration) {
        super(log, servicePid);

        this.configuration = configuration;
    }

    /**
     * Get the configuration that is currently bound.
     *
     * @param configuration The configuration, may be null.
     */
    public void setConfiguration(InternalConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Get the configuration.
     *
     * @return The configuration, may be null.
     */
    public InternalConfiguration getConfiguration() {
        return configuration;
    }

    // Javadoc inherited.
    public boolean unbindFromLocation(ServiceReference reference) {
        if (!super.unbindFromLocation(reference)) {
            return false;
        }

        if (configuration != null) {
            configuration.unbindFromLocation();
        }

        return true;
    }

    // Javadoc inherited.
    public boolean hasConfiguration() {
        return configuration != null;
    }
}
