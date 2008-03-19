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

package com.volantis.osgi.cm;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.log.LogService;

import java.util.HashSet;
import java.util.Set;

/**
 * Maintains association between {@link Configuration}s and their targets.
 */
public abstract class Association {

    /**
     * The log service.
     */
    protected final LogService log;

    /**
     * The pid of the service to which this is bound / will bind.
     */
    protected final String servicePid;

    /**
     * The list of targets.
     *
     * <p>Typically there should only be one target but the specification does
     * allow more as long as they all belong to the same bundle.</p>
     */
    private final Set targets;

    /**
     * The location to which this is bound.
     */
    protected String bundleLocation;

    /**
     * Initialise.
     *
     * @param log        The log service.
     * @param servicePid The pid of the service to which this is bound / will
     *                   bind.
     */
    protected Association(LogService log, String servicePid) {
        this.log = log;
        targets = new HashSet();
        this.servicePid = servicePid;
    }

    /**
     * Get the targets to which this owner has been bound.
     *
     * @return The collection of targets.
     */
    public ServiceReference[] getConfigurationTargets() {
        ServiceReference[] targets = new ServiceReference[this.targets.size()];
        this.targets.toArray(targets);
        return targets;
    }

    /**
     * Check to see whether this has any targets.
     *
     * @return True if it has targets, false otherwise.
     */
    public boolean hasTargets() {
        return !targets.isEmpty();
    }

    /**
     * Get the pid of the service to which this is bound / will bind.
     *
     * @return The pid.
     */
    protected String getServicePid() {
        return servicePid;
    }

    /**
     * Get the location to which this is bound.
     *
     * @return The bound location, null if it is not yet bound.
     */
    public String getBundleLocation() {
        return bundleLocation;
    }

    /**
     * Bind this association to the specified target.
     *
     * @param reference The target.
     * @return True if it was bound, false otherwise.
     */
    public boolean bindToLocation(ServiceReference reference) {

        Bundle bundle = reference.getBundle();
        String bundleLocation = bundle.getLocation();

        if (this.bundleLocation == null) {
            // This is the first service that has registered with this pid so
            // bind the association to this service.
            this.bundleLocation = bundleLocation;
        } else if (!this.bundleLocation.equals(bundleLocation)) {
            // A service from a different bundle has already registered with
            // this pid so ignore this.
            log.log(reference, LogService.LOG_WARNING,
                    "Pid '" + getServicePid() +
                            "' cannot be bound to '" + bundleLocation +
                            "' as it has already been bound to '" +
                            this.bundleLocation + "'");
            return false;
        }

        if (targets.size() > 0) {
            // This is the second service from the same bundle to register
            // which is wrong but still allowed.
            log.log(reference, LogService.LOG_WARNING,
                    "Multiple services registered with same pid '" +
                            getServicePid() + "'");
        }

        targets.add(reference);

        return true;
    }

    /**
     * Unbind the association from the target.
     *
     * @param reference The target.
     * @return True if it leaves the association unbound (i.e. no targets left),
     *         false otherwise.
     */
    public boolean unbindFromLocation(ServiceReference reference) {
        targets.remove(reference);
        if (targets.isEmpty()) {
            this.bundleLocation = null;
            return true;
        }

        return false;
    }

    /**
     * Check to see if this has any configuration information.
     *
     * @return True if it has false otherwise.
     */
    public abstract boolean hasConfiguration();
}
