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
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;

import java.io.IOException;
import java.util.Dictionary;

/**
 * Interface for internal implementation of {@link ConfigurationAdmin} service.
 *
 * @mock.generate
 */
public interface ConfigurationManager {

    /**
     * Bundle specific equivalent to {@link ConfigurationAdmin#createFactoryConfiguration(String)}.
     */
    InternalConfiguration createFactoryConfiguration(
            Bundle bundle, String factoryPid)
            throws IOException;

    /**
     * Bundle specific equivalent to {@link ConfigurationAdmin#createFactoryConfiguration(String,String)}.
     */
    InternalConfiguration createFactoryConfiguration(
            Bundle bundle, String factoryPid, String location)
            throws IOException;

    /**
     * Bundle specific equivalent to {@link ConfigurationAdmin#getConfiguration(String)}.
     */
    InternalConfiguration getConfiguration(Bundle bundle, String pid)
            throws IOException;

    /**
     * Bundle specific equivalent to {@link ConfigurationAdmin#getConfiguration(String,String)}.
     */
    InternalConfiguration getConfiguration(
            Bundle bundle, String pid, String location)
            throws IOException;

    /**
     * Bundle specific equivalent to {@link ConfigurationAdmin#listConfigurations(String)}.
     */
    InternalConfiguration[] listConfigurations(Bundle bundle, String filter)
            throws IOException, InvalidSyntaxException;

    /**
     * Deletes the configuration.
     *
     * <p>Called by implementation of {@link Configuration#delete()}.</p>
     *
     * @param configuration The internal configuration to delete.
     * @throws IOException If there was a problem access the persistent file
     *                     store.
     */
    void deleteConfiguration(InternalConfiguration configuration)
            throws IOException;

    /**
     * Updates the configuration.
     *
     * @param configuration The configuration to be updated.
     * @param properties    The new properties, or null if this should just send
     *                      notifications.
     * @throws IOException If there was a problem accessing the persistent file
     *                     store.
     */
    void updateConfiguration(
            InternalConfiguration configuration, Dictionary properties) throws
            IOException;

    /**
     * Gets the configuration properties
     *
     * <p>Called by implementation of {@link Configuration#getProperties()}.</p>
     *
     * @param configuration The internal configuration whose properties are
     *                      needed.
     * @return The properties, may be null.
     * @throws IOException If there was a problem accessing the persistent file
     *                     store.
     */
    Dictionary getConfigurationProperties(InternalConfiguration configuration);

    /**
     * Sets the configuration bundle location.
     *
     * <p>Called by implementation of {@link Configuration#setBundleLocation(String)}.</p>
     *
     * @param configuration  The internal configuration whose bundle location is
     *                       to be set.
     * @param bundleLocation The new bundle location, may be null.
     * @throws IOException If there was a problem accessing the persistent file
     *                     store.
     */
    void setConfigurationBundleLocation(
            InternalConfiguration configuration, String bundleLocation);

    /**
     * Gets the configuration bundle location.
     *
     * <p>Called by implementation of {@link Configuration#setBundleLocation(String)}.</p>
     *
     * @param configuration The internal configuration whose bundle location is
     *                      to be returned.
     * @return The bundle location, may be null.
     * @throws IOException If there was a problem accessing the persistent file
     *                     store.
     */
    String getConfigurationBundleLocation(InternalConfiguration configuration);

    /**
     * Handler for event triggered by registration of {@link
     * ManagedServiceFactory} service.
     *
     * @param reference The service reference.
     */
    void factoryRegistered(ServiceReference reference);

    /**
     * Handler for event triggered by change of {@link ManagedServiceFactory}
     * service.
     *
     * @param reference The service reference.
     */
    void factoryModified(ServiceReference reference);

    /**
     * Handler for event triggered by unregistering of {@link
     * ManagedServiceFactory} service.
     *
     * @param reference The service reference.
     */
    void factoryUnregistering(ServiceReference reference);

    /**
     * Handler for event triggered by registration of {@link ManagedService}
     * service.
     *
     * @param reference The service reference.
     */
    void serviceRegistered(ServiceReference reference);

    /**
     * Handler for event triggered by change of {@link ManagedService} service.
     *
     * @param reference The service reference.
     */
    void serviceModified(ServiceReference reference);

    /**
     * Handler for event triggered by unregistering of {@link ManagedService}
     * service.
     *
     * @param reference The service reference.
     */
    void serviceUnregistering(ServiceReference reference);
}
