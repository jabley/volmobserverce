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

package com.volantis.osgi.cm.dispatcher;

import com.volantis.osgi.cm.ConfigurationSnapshot;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;

/**
 * Dispatches tasks to be run asynchronously.
 *
 * @mock.generate
 */
public interface Dispatcher {

    /**
     * A {@link ManagedServiceFactory} has registered so inform it of all the
     * existing configurations.
     *
     * @param factoryPid The pid of the factory.
     * @param reference  The reference of the service that just registered.
     * @param snapshots  The snapshots of the configuration.
     */
    void managedServiceFactoryRegistered(
            String factoryPid, ServiceReference reference,
            ConfigurationSnapshot[] snapshots);

    /**
     * A configuration for a {@link ManagedServiceFactory} has just been updated
     * so inform all interested services of the new properties and generate a
     * {@link ConfigurationEvent#CM_UPDATED} event for any listeners.
     *
     * @param factoryPid The pid of the factory.
     * @param references The references of the services that registered an
     *                   interest.
     * @param snapshot   The snapshot of the configuration.
     */
    void managedServiceFactoryConfigurationUpdated(
            final String factoryPid,
            ServiceReference[] references,
            ConfigurationSnapshot snapshot);

    /**
     * A configuration for a {@link ManagedServiceFactory} has just been deleted
     * so inform all interested services of that and generate a {@link
     * ConfigurationEvent#CM_DELETED} event for any listeners.
     *
     * @param factoryPid The pid of the factory.
     * @param pid        The pid of the configuration that was deleted.
     * @param references The references of the services that registered an
     *                   interest.
     */
    void managedServiceFactoryConfigurationDeleted(
            String factoryPid, String pid, ServiceReference[] references);

    /**
     * A {@link ManagedService} has registered so inform it of the existing
     * configuration, if any.
     *
     * @param reference The reference of the service that just registered.
     * @param snapshot  The snapshot of the configuration, is null if no
     *                  configurations have been created yet.
     */
    void managedServiceRegistered(
            ServiceReference reference, ConfigurationSnapshot snapshot);

    /**
     * The configuration for a {@link ManagedService} has just been updated so
     * inform all interested services of the new properties and generate a
     * {@link ConfigurationEvent#CM_UPDATED} event for any listeners.
     *
     * @param references The references of the services that registered an
     *                   interest.
     * @param snapshot   The snapshot of the configuration.
     */
    void managedServiceConfigurationUpdated(
            ServiceReference[] references,
            ConfigurationSnapshot snapshot);

    /**
     * The configuration for a {@link ManagedService} has just been deleted so
     * inform all interested services of that and generate a {@link
     * ConfigurationEvent#CM_DELETED} event for any listeners.
     *
     * @param pid        The pid of the configuration that was deleted.
     * @param references The references of the services that registered an
     *                   interest.
     */
    void managedServiceConfigurationDeleted(
            String pid, ServiceReference[] references);
}
