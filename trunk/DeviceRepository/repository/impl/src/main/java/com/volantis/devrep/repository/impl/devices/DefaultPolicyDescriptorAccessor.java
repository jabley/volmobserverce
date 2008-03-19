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
package com.volantis.devrep.repository.impl.devices;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.devices.PolicyDescriptorAccessor;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 */
public class DefaultPolicyDescriptorAccessor implements PolicyDescriptorAccessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultPolicyDescriptorAccessor.class);

    private final LocalRepository repository;

    private final DeviceRepositoryAccessor accessor;

    private final Map cachedPolicyDescriptorRequests;

    private Set cachedPolicyNames;

    public DefaultPolicyDescriptorAccessor(final LocalRepository repository,
                                           final DeviceRepositoryAccessor accessor) {

        this.repository = repository;
        this.accessor = accessor;
        cachedPolicyDescriptorRequests =
            Collections.synchronizedMap(new HashMap());
    }

    // javadoc inherited
    public PolicyDescriptor getPolicyDescriptor(String policyName, Locale locale)
            throws DeviceRepositoryException {

        PolicyDescriptor descriptor = null;

        if (policyName != null &&
                getCachedPolicyNames().contains(policyName)) {
            // get the right cache
            Map nameToDescriptor =
                (Map) cachedPolicyDescriptorRequests.get(locale);
            if (nameToDescriptor == null) {
                nameToDescriptor =
                    Collections.synchronizedMap(new HashMap());
                cachedPolicyDescriptorRequests.put(locale, nameToDescriptor);
            }
            // check if it's been cached
            descriptor = (PolicyDescriptor) nameToDescriptor.get(policyName);
            if (descriptor == null) {
                // if not go to the repository and load it
                try {
                    final RepositoryConnection connection =
                        repository.connect();
                    try {
                        descriptor = accessor.retrievePolicyDescriptor(
                                connection, policyName, locale);
                        nameToDescriptor.put(policyName, descriptor);
                    } finally {
                        repository.disconnect(connection);
                    }
                } catch (RepositoryException e) {
                    throw new DeviceRepositoryException(e);
                }
            }
        }

        return descriptor;
    }
    /**
     * Return the value of {@link #cachedPolicyNames}, lazy loading it if
     * required.
     *
     * @return the set of all accessible policy names.
     * @throws DeviceRepositoryException
     */
    private Set getCachedPolicyNames() throws DeviceRepositoryException {
        // Lazy load the cache of accessible policy names if necessary.
        if (cachedPolicyNames == null) {
            try {
                cachedPolicyNames = calculatePolicyNames();
            } catch (RepositoryException e) {
                throw new DeviceRepositoryException(e);
            }
        }
        return cachedPolicyNames;
    }

    /**
     * Return a set containing all "accessible" policy names.
     * <p>
     * This is used to create a cache.
     *
     * @return a Set which may be empty but not null.
     */
    private Set calculatePolicyNames() throws RepositoryException {

        Set result = new HashSet();

        RepositoryConnection connection = repository.connect();
        try {
            RepositoryEnumeration policyNames =
                    accessor.enumeratePolicyNames(connection);
            try {
                while (policyNames.hasNext()) {
                    String policyName = (String) policyNames.next();
                    result.add(policyName);
                }
            } finally {
                policyNames.close();
            }
        } finally {
            repository.disconnect(connection);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Finished copying PolicyNames to " +
                    "PolicyDescriptor cache, total number =" +
                    result.size());
        }

        return result;
    }
}
