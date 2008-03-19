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
import com.volantis.osgi.cm.dispatcher.ServiceReferenceContainer;
import com.volantis.osgi.cm.factory.ManagedServiceFactoryHandler;
import com.volantis.osgi.cm.plugin.PluginManager;
import com.volantis.osgi.cm.plugin.PluginManagerImpl;
import com.volantis.osgi.cm.service.ManagedServiceHandler;
import com.volantis.osgi.cm.store.ConfigurationStore;
import com.volantis.osgi.cm.store.ConfigurationStoreImpl;
import com.volantis.osgi.cm.store.FileManager;
import com.volantis.osgi.cm.store.FileManagerImpl;
import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.concurrency.PerThreadExpectationBuilder;
import com.volantis.testtools.mock.concurrency.ThreadMatcher;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import mock.org.osgi.framework.BundleContextMock;
import mock.org.osgi.framework.ServiceReferenceMock;
import mock.org.osgi.service.cm.ConfigurationListenerMock;
import mock.org.osgi.service.cm.ManagedServiceFactoryMock;
import mock.org.osgi.service.cm.ManagedServiceMock;
import mock.org.osgi.service.log.LogServiceMock;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.log.LogService;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Integration level test cases that test everything except the OSGi specific
 * parts, i.e. activator and event tracker.
 */
public class ConfigurationAdminManagerTestCase
        extends AdminBasedTestAbstract {

    //    protected BundleContextMock contextMock;
    //    private BundleMock callingBundleMock;
    //    protected EventDispatcherMock dispatcherMock;
    //    private ServiceReferenceMock adminServiceReferenceMock;
    private File persistentDir;
    private FileManager fileManager;
    private ConfigurationListenerMock listenerMock;
    protected ServiceReferenceMock adminServiceReferenceMock;
    private ServiceReferenceMock listenerReferenceMock;
    private Dispatcher dispatcher;
    private AsynchronousDispatcher asynchronousDispatcher;
    private BundleContextMock contextMock;
    private PerThreadExpectationBuilder perThreadExpectations;
    private static final String PREFIX = "com.volantis.osgi.cm.factory-pid.";
    private static final String FACTORY_PID1 = "factoryPid1";
    private ThreadMatcher mainThread;
    private ThreadMatcher dispatcherThread;
    private ExpectationBuilder dispatcherExpectations;
    private static final String SERVICE_PID1 = "pid1";
    private LogServiceMock logServiceMock;

    protected ConfigurationManager createAdminManager() throws IOException {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a container that can be used across multiple threads.
        perThreadExpectations = mockFactory.createPerThreadBuilder(
                "Per Thread Expectations");

        logServiceMock =
                new LogServiceMock("logServiceMock", perThreadExpectations);

        PluginManager pluginManager =
                new PluginManagerImpl(logServiceMock);

        // Add the container for the current thread.
        mainThread = mockFactory.createKnownThreadMatcher("Main thread");
        perThreadExpectations.addThreadSpecificBuilder(
                mainThread, expectations);

        // The event dispatcher will dispatch events on a separate thread so
        // the listener will need its own expectations.
        dispatcherExpectations = mockFactory.createOrderedBuilder(
                "Dispatcher Expectations");
        dispatcherThread = mockFactory.createAnyThreadMatcher(
                "Dispatcher thread");

        // Add a container for use by the background event dispatcher whose
        // name we do not know.
        perThreadExpectations.addThreadSpecificBuilder(
                dispatcherThread, dispatcherExpectations);

        adminServiceReferenceMock =
                new ServiceReferenceMock("adminServiceReferenceMock");

        DelayedReferenceContainer referenceContainer =
                new DelayedReferenceContainer();
        referenceContainer.setReference(adminServiceReferenceMock);

        // Create a bundle context that can be used across thread. This can
        // also be used to add expectations for the current thread.
        contextMock = new BundleContextMock("contextMock",
                perThreadExpectations);

        listenerReferenceMock =
                new ServiceReferenceMock("listenerReferenceMock",
                        dispatcherExpectations);

        listenerMock = new ConfigurationListenerMock(
                "listenerMock", dispatcherExpectations);


        persistentDir = createTempDir("cmint");
        fileManager = new FileManagerImpl(persistentDir, 4, 10);
        ConfigurationStore store = new ConfigurationStoreImpl(fileManager);

        // Create the dispatcher and prepare it for using mocks.
        asynchronousDispatcher = new AsynchronousDispatcher();
//        asynchronousDispatcher.queueAsynchronousAction(new Runnable() {
//            public void run() {
//                MockTestHelper.begin();
//            }
//        });

        dispatcher = new DispatcherImpl(
                contextMock, logServiceMock, asynchronousDispatcher,
                new ServiceReferenceContainer() {
                    public ServiceReference getReference() {
                        return adminServiceReferenceMock;
                    }
                }, pluginManager);

        ConfigurationAdminManager manager = new ConfigurationAdminManager(
                contextMock, logServiceMock, dispatcher, store);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Context expects to be called on this thread to get the listeners
        // at the time that the event was queued, rather than when it was
        // dispatched. This is in accordance with the OSGi specification.
        contextMock.expects.getServiceReferences(
                "org.osgi.service.cm.ConfigurationListener", null)
                .returns(new ServiceReference[]{listenerReferenceMock}).any();

        return manager;
    }

    private void addFactoryUpdateExpectation(
            String description, ManagedServiceFactoryMock managedFactory1Mock,
            final String expectedPidSuffix, final String expectedFactoryPid) {
        Dictionary table = new Hashtable();
        table.put(FrameworkConstants.SERVICE_PID, PREFIX + expectedPidSuffix);
        table.put(FrameworkConstants.SERVICE_FACTORYPID, expectedFactoryPid);
        managedFactory1Mock.expects
                .updated(PREFIX + expectedPidSuffix, table)
                .description(description + ": updated");
    }

    private void addFactoryUpdateExpectation(
            String description, final ServiceReference reference,
            ManagedServiceFactoryMock managedFactory1Mock,
            final String expectedPidSuffix, final String expectedFactoryPid) {

        contextMock.expects.getService(reference)
                .description(description + ": getService")
                .returns(managedFactory1Mock);

        addFactoryUpdateExpectation(description, managedFactory1Mock,
                expectedPidSuffix, expectedFactoryPid);

        contextMock.expects.ungetService(reference)
                .description(description + ": ungetService")
                .returns(true);
    }

    private void addFactoryListenerExpectation(
            final String description, final String expectedFactoryPid,
            final String expectedPidSuffix,
            final int expectedEventType) {

        contextMock.expects.getService(listenerReferenceMock)
                .returns(listenerMock);

        String expectedPid = PREFIX + expectedPidSuffix;

        addListenerExpectation(description, expectedFactoryPid, expectedPid,
                expectedEventType);

        contextMock.expects.ungetService(listenerReferenceMock)
                .returns(true);
    }

    private void addFactoryDeletedExpectation(
            String description, ServiceReference reference,
            ManagedServiceFactoryMock managedFactory1Mock,
            String expectedPidSuffix) {

        contextMock.expects.getService(reference)
                .description(description + ": getService")
                .returns(managedFactory1Mock);

        managedFactory1Mock.expects
                .deleted(PREFIX + expectedPidSuffix)
                .description(description + ": updated");

        contextMock.expects.ungetService(reference)
                .description(description + ": ungetService")
                .returns(true);
    }

    /**
     * Ensure that factory configurations are processed correctly. <ol>
     * <li>Registers a factory</li> <li>Creates some configurations, some free,
     * some bound.</li> <li>Deletes a configuration.</li> <li>Changes factory
     * properties (but not PID).</li> <li>Unregisters factory.</li>
     * <li>Reregisters factory from different bundle.</li> <li>Checks
     * persistence by loading configurations from the store.</li> </ol>
     */
    public void testFactoryConfiguration()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ServiceReferenceMock unauthorizedFactoryReferenceMock =
                new ServiceReferenceMock("unauthorizedFactoryReferenceMock",
                        expectations);

        // Create a mock ManagedServiceFactory that is invoked from the
        // dispatcher thread.
        final ManagedServiceFactoryMock unauthorizedManagedFactoryMock =
                new ManagedServiceFactoryMock("unauthorizedManagedFactoryMock",
                        dispatcherExpectations);

        final ServiceReferenceMock attackingFactoryReferenceMock =
                new ServiceReferenceMock("attackingFactoryReferenceMock",
                        expectations);

        // Create a mock ManagedServiceFactory that is invoked from the
        // dispatcher thread.
        final ManagedServiceFactoryMock attackingManagedFactoryMock =
                new ManagedServiceFactoryMock("attackingManagedFactoryMock",
                        dispatcherExpectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        unauthorizedFactoryReferenceMock.expects
                .getProperty(FrameworkConstants.SERVICE_PID)
                .returns(FACTORY_PID1).any();
        unauthorizedFactoryReferenceMock.expects.getBundle()
                .returns(unauthorizedBundleMock).any();

        attackingFactoryReferenceMock.expects
                .getProperty(FrameworkConstants.SERVICE_PID)
                .returns(FACTORY_PID1).any();
        attackingFactoryReferenceMock.expects.getBundle()
                .returns(attackingBundleMock).any();

        perThreadExpectations.add(dispatcherThread, new OrderedExpectations() {
            public void add() {

                // ----------------------------------------------------
                // Registered Factory
                // ----------------------------------------------------

                // ----------------------------------------------------
                // Create First Configuration
                // ----------------------------------------------------

                // Factory is informed when first configuration
                // is updated.
                addFactoryUpdateExpectation(
                        "First Configuration Updated",
                        unauthorizedFactoryReferenceMock,
                        unauthorizedManagedFactoryMock, "a",
                        FACTORY_PID1);

                // Listeners are invoked.
                addFactoryListenerExpectation(
                        "First Configuration Updated Event",
                        FACTORY_PID1, "a",
                        ConfigurationEvent.CM_UPDATED);

                // ----------------------------------------------------
                // Create Second Configuration
                // ----------------------------------------------------

                // Factory is informed when second configuration
                // is updated.
                addFactoryUpdateExpectation(
                        "Second Configuration Updated",
                        unauthorizedFactoryReferenceMock,
                        unauthorizedManagedFactoryMock, "b",
                        FACTORY_PID1);

                // Listeners are invoked.
                addFactoryListenerExpectation(
                        "Second Configuration Updated Event",
                        FACTORY_PID1, "b",
                        ConfigurationEvent.CM_UPDATED);

                // ----------------------------------------------------
                // Create Third Configuration
                // ----------------------------------------------------

                // Factory is informed when third configuration
                // is updated.
                addFactoryUpdateExpectation(
                        "Third Configuration Updated",
                        unauthorizedFactoryReferenceMock,
                        unauthorizedManagedFactoryMock, "c",
                        FACTORY_PID1);

                // Listeners are invoked.
                addFactoryListenerExpectation(
                        "Third Configuration Updated Event",
                        FACTORY_PID1, "c",
                        ConfigurationEvent.CM_UPDATED);

                // ----------------------------------------------------
                // Delete Second Configuration
                // ----------------------------------------------------

                // Factory is informed when second configuration
                // is deleted.
                addFactoryDeletedExpectation(
                        "Second Configuration Deleted",
                        unauthorizedFactoryReferenceMock,
                        unauthorizedManagedFactoryMock, "b");

                // Listeners are invoked.
                addFactoryListenerExpectation(
                        "Second Configuration Deleted Event",
                        FACTORY_PID1, "b",
                        ConfigurationEvent.CM_DELETED);

                // ----------------------------------------------------
                // Reregister Factory
                // ----------------------------------------------------

                contextMock.expects
                        .getService(attackingFactoryReferenceMock)
                        .returns(attackingManagedFactoryMock);

                addFactoryUpdateExpectation(
                        "Reregister Pass First Configuration",
                        attackingManagedFactoryMock, "a",
                        FACTORY_PID1);

                contextMock.expects
                        .ungetService(attackingFactoryReferenceMock)
                        .returns(true);
            }
        });

        logServiceMock.expects.log(attackingFactoryReferenceMock,
                LogService.LOG_WARNING,
                "Pid '" + FACTORY_PID1 + "' cannot be bound to '" +
                        ATTACKING_BUNDLE_LOCATION +
                        "' as it has already been bound to '" +
                        UNAUTHORIZED_BUNDLE_LOCATION + "'");

        logServiceMock.expects.log(attackingFactoryReferenceMock,
                LogService.LOG_WARNING,
                "Configuration with pid '" + PREFIX + "c" +
                        "' for factory '" + FACTORY_PID1 + "' is bound to '" +
                        UNAUTHORIZED_BUNDLE_LOCATION + "' instead of '" +
                        ATTACKING_BUNDLE_LOCATION + "' and so will be ignored");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ServiceHandler factoryHandler =
                new ManagedServiceFactoryHandler(manager);

        // Register a ManagedServiceFactory before a suitable configuration
        // has been created.
        factoryHandler.serviceRegistered(unauthorizedFactoryReferenceMock);

        // Attempt to register another factory for the same pid from a
        // different bundle. This should fail and log a warning. Then make sure
        // that the other service listener events do not fail.
        factoryHandler.serviceRegistered(attackingFactoryReferenceMock);
        factoryHandler.serviceModified(attackingFactoryReferenceMock);
        factoryHandler.serviceUnregistering(attackingFactoryReferenceMock);

        // Create a couple of factory configurations.
        Configuration fc1_1 =
                authorizedAdmin.createFactoryConfiguration(FACTORY_PID1, null);
        fc1_1.update(new Hashtable());
        checkLocationBinding(PREFIX + "a", UNAUTHORIZED_BUNDLE_LOCATION);

        Configuration fc1_2 =
                authorizedAdmin.createFactoryConfiguration(FACTORY_PID1, null);
        fc1_2.update(new Hashtable());
        checkLocationBinding(PREFIX + "b", UNAUTHORIZED_BUNDLE_LOCATION);

        Configuration fc1_3 =
                authorizedAdmin.createFactoryConfiguration(FACTORY_PID1,
                        UNAUTHORIZED_BUNDLE_LOCATION);
        fc1_3.update(new Hashtable());
        checkLocationBinding(PREFIX + "c", UNAUTHORIZED_BUNDLE_LOCATION);

        // Check to see if the information has been correctly persisted.
        checkPersistedConfigurations(
                new InternalConfiguration[]{
                        new ConfigurationImpl(PREFIX + "a", FACTORY_PID1, null,
                                null, new CaseInsensitiveDictionary()),
                        new ConfigurationImpl(PREFIX + "b", FACTORY_PID1, null,
                                null, new CaseInsensitiveDictionary()),
                        new ConfigurationImpl(PREFIX + "c", FACTORY_PID1, null,
                                UNAUTHORIZED_BUNDLE_LOCATION,
                                new CaseInsensitiveDictionary()),
                });

        // Delete a factory configuration.
        fc1_2.delete();

        // The ManagedServiceFactory service has changed.
        factoryHandler.serviceModified(unauthorizedFactoryReferenceMock);

        // The ManagedServiceFactory service has unregistered.
        factoryHandler.serviceUnregistering(unauthorizedFactoryReferenceMock);

        // Make sure that the configurations that were created without any
        // specific bundle location and have not since been deleted have
        // reverted to null.
        checkLocationBinding(PREFIX + "a", null);

        // Reregister another ManagedServiceFactory with the same PID but from
        // a different bundle.
        factoryHandler.serviceRegistered(attackingFactoryReferenceMock);

        // Check to see if the information has been correctly persisted.
        checkPersistedConfigurations(
                new InternalConfiguration[]{
                        new ConfigurationImpl(PREFIX + "a", FACTORY_PID1, null,
                                null, new CaseInsensitiveDictionary()),
                        new ConfigurationImpl(PREFIX + "c", FACTORY_PID1, null,
                                UNAUTHORIZED_BUNDLE_LOCATION,
                                new CaseInsensitiveDictionary()),
                });

        asynchronousDispatcher.stop();
    }

    private void assertMatchingConfigurations(
            InternalConfiguration[] expectedConfigurations,
            InternalConfiguration[] actualConfigurations) {

        Map actualMap = new HashMap();
        for (int i = 0; i < actualConfigurations.length; i++) {
            InternalConfiguration configuration = actualConfigurations[i];
            actualMap.put(configuration.getPid(), configuration);
        }

        assertEquals(expectedConfigurations.length, actualMap.size());

        for (int i = 0; i < expectedConfigurations.length; i++) {
            InternalConfiguration expected = expectedConfigurations[i];
            String pid = expected.getPid();
            InternalConfiguration actual =
                    (InternalConfiguration) actualMap.get(pid);
            if (actual == null) {
                fail("Expected configuration with pid '" + pid +
                        "' but did not find it");
            }

            // Check each property separately because Configuration.equals()
            // only checks the pid.
            assertEquals(expected.getPid(), actual.getPid());
            assertEquals(expected.getFactoryPid(),
                    actual.getFactoryPid());
            assertEquals(expected.getProperties(),
                    actual.getProperties());
            assertEquals(expected.getBundleLocation(),
                    actual.getBundleLocation());
        }
    }

    private void checkLocationBinding(
            final String pid, final String expectedLocation)
            throws IOException, InvalidSyntaxException {

        // Make sure that it is bound to the bundle.
        Configuration[] configurations =
                authorizedAdmin.listConfigurations(null);
        for (int i = 0; i < configurations.length; i++) {
            Configuration configuration = configurations[i];
            if (configuration.getPid().equals(pid)) {
                assertEquals(expectedLocation,
                        configurations[0].getBundleLocation());
            }
        }
    }

    private void addServiceUpdateExpectation(
            String description, ManagedServiceMock managedService1Mock,
            final Dictionary expectedDictionary) {
        managedService1Mock.expects.updated(expectedDictionary)
                .description(description + ": updated");
    }

    private Dictionary createServiceDictionary(String expectedPid) {
        Hashtable table = new Hashtable();
        table.put(FrameworkConstants.SERVICE_PID, expectedPid);
        return table;
    }

    private void addServiceUpdateExpectation(
            String description, final ServiceReference reference,
            ManagedServiceMock managedService1Mock,
            final Dictionary expectedDictionary) {

        contextMock.expects.getService(reference)
                .description(description + ": getService")
                .returns(managedService1Mock);

        addServiceUpdateExpectation(description, managedService1Mock,
                expectedDictionary);

        contextMock.expects.ungetService(reference)
                .description(description + ": ungetService")
                .returns(true);
    }

    private void addServiceListenerExpectation(
            String description, final String expectedPid,
            final int expectedEventType) {

        contextMock.expects.getService(listenerReferenceMock)
                .description(description + ": getService")
                .returns(listenerMock);

        addListenerExpectation(description, null, expectedPid,
                expectedEventType);

        contextMock.expects.ungetService(listenerReferenceMock)
                .description(description + ": ungetService")
                .returns(true);
    }

    private void addListenerExpectation(
            String description, final Object expectedFactoryPid,
            final String expectedPid,
            final int expectedEventType) {
        listenerMock.fuzzy
                .configurationEvent(mockFactory.expectsInstanceOf(
                        ConfigurationEvent.class))
                .description(description + ": configurationEvent")
                .does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        ConfigurationEvent ce =
                                (ConfigurationEvent) event.getArgument(
                                        ConfigurationEvent.class);

                        assertEquals(expectedPid, ce.getPid());
                        assertEquals(expectedFactoryPid, ce.getFactoryPid());
                        assertEquals(adminServiceReferenceMock,
                                ce.getReference());
                        assertEquals(expectedEventType, ce.getType());
                        return null;
                    }
                });
    }

    private void addServiceDeleteExpectation(
            String description, final ServiceReference reference,
            ManagedServiceMock managedService1Mock) {

        contextMock.expects.getService(reference)
                .description(description + ": getService")
                .returns(managedService1Mock);

        managedService1Mock.expects.updated(null)
                .description(description + ": updated");

        contextMock.expects.ungetService(reference)
                .description(description + ": ungetService")
                .returns(true);
    }

    /**
     * Ensure that service configurations are processed correctly. <ol>
     * <li>Registers a service</li> <li>Creates a bound configuration.</li>
     * <li>Deletes the configuration.</li> <li>Creates a free
     * configuration.</li> <li>Changes service properties (but not PID).</li>
     * <li>Unregisters service.</li> <li>Reregisters service from different
     * bundle.</li> <li>Checks persistence by loading configurations from the
     * store.</li> </ol>
     */
    public void testConfiguration()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ServiceReferenceMock unauthorizedServiceReferenceMock =
                new ServiceReferenceMock("unauthorizedServiceReferenceMock",
                        expectations);

        // Create a mock ManagedService that is invoked from the
        // dispatcher thread.
        final ManagedServiceMock unauthorizedManagedServiceMock =
                new ManagedServiceMock("unauthorizedManagedServiceMock",
                        dispatcherExpectations);

        final ServiceReferenceMock attackingServiceReferenceMock =
                new ServiceReferenceMock("attackingServiceReferenceMock",
                        expectations);

        // Create a mock ManagedService that is invoked from the
        // dispatcher thread.
        final ManagedServiceMock attackingManagedServiceMock =
                new ManagedServiceMock("attackingManagedServiceMock",
                        dispatcherExpectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        unauthorizedServiceReferenceMock.expects
                .getProperty(FrameworkConstants.SERVICE_PID)
                .returns(SERVICE_PID1).any();
        unauthorizedServiceReferenceMock.expects.getBundle()
                .returns(unauthorizedBundleMock).any();

        attackingServiceReferenceMock.expects
                .getProperty(FrameworkConstants.SERVICE_PID)
                .returns(SERVICE_PID1).any();
        attackingServiceReferenceMock.expects.getBundle()
                .returns(attackingBundleMock).any();

        perThreadExpectations.add(dispatcherThread, new OrderedExpectations() {
            public void add() {

                // ----------------------------------------------------
                // Registered Service
                // ----------------------------------------------------

                // When the service is first registered and no
                // configuration is available then its update method
                // is invoked with null.
                addServiceUpdateExpectation("Registered Service",
                        unauthorizedServiceReferenceMock,
                        unauthorizedManagedServiceMock, null);

                // ----------------------------------------------------
                // Create Bound Configuration
                // ----------------------------------------------------

                // After the configuration has been created then its
                // update method is invoked with the properties.
                addServiceUpdateExpectation(
                        "Update Bound Configuration",
                        unauthorizedServiceReferenceMock,
                        unauthorizedManagedServiceMock,
                        createServiceDictionary(SERVICE_PID1));

                // And an updated event is sent to any listeners.
                addServiceListenerExpectation(
                        "Update Bound Configuration Event",
                        SERVICE_PID1,
                        ConfigurationEvent.CM_UPDATED);

                // ----------------------------------------------------
                // Delete Bound Configuration
                // ----------------------------------------------------

                addServiceDeleteExpectation(
                        "Delete Bound Configuration",
                        unauthorizedServiceReferenceMock,
                        unauthorizedManagedServiceMock);

                addServiceListenerExpectation(
                        "Delete Bound Configuration Event",
                        SERVICE_PID1,
                        ConfigurationEvent.CM_DELETED);

                // ----------------------------------------------------
                // Create Free Configuration
                // ----------------------------------------------------

                // After the configuration has been created then its
                // update method is invoked with the properties.
                addServiceUpdateExpectation(
                        "Update Free Configuration",
                        unauthorizedServiceReferenceMock,
                        unauthorizedManagedServiceMock,
                        createServiceDictionary(SERVICE_PID1));

                // And an updated event is sent to any listeners.
                addServiceListenerExpectation(
                        "Update Free Configuration Event",
                        SERVICE_PID1,
                        ConfigurationEvent.CM_UPDATED);

                // ----------------------------------------------------
                // Reregister Free Configuration
                // ----------------------------------------------------

                addServiceUpdateExpectation(
                        "Reregister Free Configuration",
                        attackingServiceReferenceMock,
                        attackingManagedServiceMock,
                        createServiceDictionary(SERVICE_PID1));
            }
        });

        logServiceMock.expects.log(attackingServiceReferenceMock,
                LogService.LOG_WARNING,
                "Pid '" + SERVICE_PID1 + "' cannot be bound to '" +
                        ATTACKING_BUNDLE_LOCATION +
                        "' as it has already been bound to '" +
                        UNAUTHORIZED_BUNDLE_LOCATION + "'").fixed(2);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ServiceHandler serviceHandler = new ManagedServiceHandler(manager);

        // Register a ManagedService before a suitable configuration
        // has been created.
        serviceHandler.serviceRegistered(unauthorizedServiceReferenceMock);

        // Attempt to register another factory for the same pid from a
        // different bundle. This should fail and log a warning. Then make sure
        // that the other service listener events do not fail.
        serviceHandler.serviceRegistered(attackingServiceReferenceMock);
        serviceHandler.serviceModified(attackingServiceReferenceMock);
        serviceHandler.serviceUnregistering(attackingServiceReferenceMock);

        // Create the configuration.
        Configuration c1 =
                unauthorizedAdmin.getConfiguration(SERVICE_PID1);
        c1.update(new Hashtable());

        // Check to see if the information has been correctly persisted.
        checkPersistedConfigurations(
                new InternalConfiguration[]{
                        new ConfigurationImpl(SERVICE_PID1, null, null,
                                UNAUTHORIZED_BUNDLE_LOCATION,
                                new CaseInsensitiveDictionary()),
                });

        // Delete the configuration.
        c1.delete();

        // Create a free configuration.
        c1 = authorizedAdmin.getConfiguration(SERVICE_PID1, null);
        c1.update(new Hashtable());
        checkLocationBinding(SERVICE_PID1, UNAUTHORIZED_BUNDLE_LOCATION);

        // The ManagedService service has changed.
        serviceHandler.serviceModified(unauthorizedServiceReferenceMock);

        // The ManagedService service has unregistered.
        serviceHandler.serviceUnregistering(unauthorizedServiceReferenceMock);

        // Make sure that the configurations that were created without any
        // specific bundle location and have not since been deleted have
        // reverted to null.
        checkLocationBinding(SERVICE_PID1, null);

        // Reregister another ManagedServiceFactory with the same PID but from
        // a different bundle.
        serviceHandler.serviceRegistered(attackingServiceReferenceMock);

        // Check to see if the information has been correctly persisted.
        checkPersistedConfigurations(
                new InternalConfiguration[]{
                        new ConfigurationImpl(SERVICE_PID1, null, null,
                                null, new CaseInsensitiveDictionary()),
                });

        asynchronousDispatcher.stop();
    }

    private void checkPersistedConfigurations(InternalConfiguration[] expected)
            throws IOException {
        ConfigurationStore newStore = new ConfigurationStoreImpl(fileManager);
        InternalConfiguration[] persisted = newStore.load();


        assertMatchingConfigurations(expected, persisted);
    }
}
