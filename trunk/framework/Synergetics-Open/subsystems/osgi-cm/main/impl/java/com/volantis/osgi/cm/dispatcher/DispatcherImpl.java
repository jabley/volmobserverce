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
import com.volantis.osgi.cm.async.AsynchronousDispatcher;
import com.volantis.osgi.cm.plugin.PluginManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.log.LogService;

/**
 * Implementation of {@link Dispatcher}.
 */
public class DispatcherImpl
        implements Dispatcher {

    /**
     * The bundle context within which this is running.
     */
    private final BundleContext context;

    /**
     * The log service.
     */
    private final LogService log;

    /**
     * The underlying asynchronous dispatcher.
     */
    private final AsynchronousDispatcher dispatcher;

    /**
     * The container for the {@link ServiceReference} for the {@link
     * ConfigurationAdmin} service.
     */
    private final ServiceReferenceContainer adminReferenceContainer;

    /**
     * The {@link ConfigurationPlugin} manager.
     */
    private final PluginManager pluginManager;

    /**
     * Initialise.
     *
     * @param context                 The bundle context within which this is
     *                                running.
     * @param log                     The log service.
     * @param dispatcher              The underlying asynchronous dispatcher.
     * @param adminReferenceContainer The container for the {@link ServiceReference}
     *                                for the {@link ConfigurationAdmin}
     *                                service.
     * @param pluginManager           The {@link ConfigurationPlugin} manager.
     */
    public DispatcherImpl(
            BundleContext context,
            LogService log, AsynchronousDispatcher dispatcher,
            ServiceReferenceContainer adminReferenceContainer,
            PluginManager pluginManager) {
        this.context = context;
        this.dispatcher = dispatcher;
        this.adminReferenceContainer = adminReferenceContainer;
        this.pluginManager = pluginManager;
        this.log = log;
    }

    /**
     * Queue an event.
     *
     * @param factoryPid The factory pid, may be null.
     * @param pid        The pid.
     * @param type       The event type.
     */
    private void queueEvent(String factoryPid, String pid, final int type) {

        ServiceReference[] references;
        try {
            references = context.getServiceReferences(
                    ConfigurationListener.class.getName(), null);

        } catch (InvalidSyntaxException e) {
            // Should never happen.
            IllegalStateException ise = new IllegalStateException();
            ise.initCause(e);
            throw ise;
        }

        if (references != null && references.length > 0) {
            queueTask(new InvokeConfigurationEventTask(
                    references, adminReferenceContainer,
                    factoryPid, pid,
                    type));
        }
    }

    /**
     * Queue the task.
     *
     * @param task The task to queue.
     */
    private void queueTask(BaseTask task) {
        dispatcher.queueAsynchronousAction(
                new RunnableTaskAdapter(context, log, task));
    }

    // Javadoc inherited.
    public void managedServiceFactoryRegistered(
            String factoryPid, ServiceReference reference,
            ConfigurationSnapshot[] snapshots) {

        ServiceReference[] references = new ServiceReference[]{
                reference
        };

        if (snapshots.length > 0) {
            queueTask(new InvokeFactoryUpdatedTask(
                    factoryPid, references, snapshots,
                    pluginManager));
        }
    }

    // Javadoc inherited.
    public void managedServiceFactoryConfigurationUpdated(
            final String factoryPid,
            ServiceReference[] references,
            ConfigurationSnapshot snapshot) {

        ConfigurationSnapshot[] snapshots = new ConfigurationSnapshot[]{
                snapshot
        };

        if (references.length > 0) {
            queueTask(new InvokeFactoryUpdatedTask(
                    factoryPid, references, snapshots,
                    pluginManager));
        }

        queueEvent(factoryPid, snapshot.getPid(),
                ConfigurationEvent.CM_UPDATED);
    }

    // Javadoc inherited.
    public void managedServiceFactoryConfigurationDeleted(
            String factoryPid, String pid, ServiceReference[] references) {

        queueTask(new InvokeFactoryDeletedTask(
                references, pid));

        queueEvent(factoryPid, pid, ConfigurationEvent.CM_DELETED);
    }

    // Javadoc inherited.
    public void managedServiceRegistered(
            ServiceReference reference, ConfigurationSnapshot snapshot) {

        ServiceReference[] references = new ServiceReference[]{
                reference
        };

        queueTask(new InvokeServiceUpdatedTask(
                references, snapshot, pluginManager));
    }

    // Javadoc inherited.
    public void managedServiceConfigurationUpdated(
            ServiceReference[] references, ConfigurationSnapshot snapshot) {

        if (references.length > 0) {
            queueTask(new InvokeServiceUpdatedTask(
                    references, snapshot, pluginManager));
        }

        queueEvent(null, snapshot.getPid(), ConfigurationEvent.CM_UPDATED);
    }

    // Javadoc inherited.
    public void managedServiceConfigurationDeleted(
            String pid, ServiceReference[] references) {

        queueTask(new InvokeServiceUpdatedTask(
                references, null, pluginManager));

        queueEvent(null, pid, ConfigurationEvent.CM_DELETED);
    }
}
