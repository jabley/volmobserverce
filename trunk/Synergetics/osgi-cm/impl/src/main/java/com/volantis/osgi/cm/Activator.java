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

import com.volantis.osgi.cm.async.AsynchronousDispatcher;
import com.volantis.osgi.cm.dispatcher.DelayedReferenceContainer;
import com.volantis.osgi.cm.dispatcher.Dispatcher;
import com.volantis.osgi.cm.dispatcher.DispatcherImpl;
import com.volantis.osgi.cm.plugin.PluginManager;
import com.volantis.osgi.cm.plugin.PluginManagerImpl;
import com.volantis.osgi.cm.store.ConfigurationStore;
import com.volantis.osgi.cm.store.ConfigurationStoreImpl;
import com.volantis.osgi.cm.store.FileManager;
import com.volantis.osgi.cm.store.FileManagerImpl;
import com.volantis.osgi.cm.tracker.ConfigurationServiceTracker;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Bundle activator for the {@link ConfigurationAdmin} service.
 *
 * <p>I would have preferred to use a Declarative Services component for this
 * but unfortunately that is not appropriate because that would create an
 * entirely separate component for each bundle rather than a single component
 * with bundle specific delegating proxies.</p>
 */
public class Activator
        implements BundleActivator {

    /**
     * The maximum number of files to create per directory in the file store.
     */
    private static final int MAX_FILES_PER_DIR = 26;

    /**
     * The maximum number of directories to create per directory in the file
     * store.
     */
    private static final int MAX_DIRS_PER_DIR = 10;

    /**
     * The configuration tracker.
     */
    private ConfigurationServiceTracker tracker;

    /**
     * The asynchronous dispatcher.
     */
    private AsynchronousDispatcher asynchronousDispatcher;

    /**
     * The tracker for the {@link LogService}.
     */
    private ServiceTracker logTracker;

    // Javadoc inherited.
    public void start(final BundleContext bundleContext) throws Exception {

        // Track the log service, when it is available then start up the
        // ConfigurationAdmin service and when it goes away stop the
        // ConfigurationAdmin service.
        this.logTracker = new ServiceTracker(bundleContext,
                LogService.class.getName(), null) {

            // Javadoc inherited.
            public Object addingService(ServiceReference reference) {
                LogService log = (LogService) super.addingService(reference);

                try {
                    startService(bundleContext, log);
                } catch (IOException e) {
                    throw new UndeclaredThrowableException(e);
                }

                return log;
            }

            // Javadoc inherited.
            public void removedService(
                    ServiceReference reference, Object service) {
                super.removedService(reference, service);
                stopService();
            }
        };

        // Begin tracking the log service.
        this.logTracker.open();
    }

    /**
     * Start this bundle with the log service.
     *
     * @param bundleContext The bundle context.
     * @param log           The log service.
     * @throws IOException If there was a problem accessing the persistent file
     *                     store.
     */
    private void startService(BundleContext bundleContext, LogService log)
            throws IOException {

        // Create a background thread to process tasks added to the queue.
        asynchronousDispatcher = new AsynchronousDispatcher();

        // Create a container to the admin reference.
        DelayedReferenceContainer delayedReferenceContainer =
                new DelayedReferenceContainer();

        PluginManager pluginManager =
                new PluginManagerImpl(log);

        Dispatcher dispatcher =
                new DispatcherImpl(bundleContext, log, asynchronousDispatcher,
                        delayedReferenceContainer, pluginManager);

        File rootDir = bundleContext.getDataFile("");
        if (rootDir == null) {
            throw new IOException(
                    "Cannot persist configuration as cannot access file" +
                            " system");
        }

        FileManager fileManager = new FileManagerImpl(
                rootDir, MAX_FILES_PER_DIR, MAX_DIRS_PER_DIR);

        ConfigurationStore store = new ConfigurationStoreImpl(fileManager);

        ConfigurationManager manager =
                new ConfigurationAdminManager(bundleContext,
                        log, dispatcher, store);

        tracker = new ConfigurationServiceTracker(
                bundleContext, manager,
                pluginManager);

        ServiceRegistration registration = bundleContext
                .registerService(ConfigurationAdmin.class.getName(),
                        manager, null);
        ServiceReference adminServiceReference = registration.getReference();
        delayedReferenceContainer.setReference(adminServiceReference);

        tracker.start();
    }

    // Javadoc inherited.
    private void stopService() {
        tracker.stop();
        tracker = null;

        asynchronousDispatcher.stop();
        asynchronousDispatcher = null;
    }

    // Javadoc inherited.
    public void stop(BundleContext bundleContext) {
        // Close the log service tracker, this should stop the service.
        logTracker.close();
    }
}
