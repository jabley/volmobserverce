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

package com.volantis.osgi.cm.tracker;

import com.volantis.osgi.cm.ConfigurationManagerMock;
import com.volantis.osgi.cm.plugin.PluginManagerMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import mock.org.osgi.framework.BundleContextMock;
import mock.org.osgi.framework.FilterMock;
import mock.org.osgi.framework.ServiceReferenceMock;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;

/**
 * Test cases for {@link ConfigurationServiceTracker}.
 */
public class ConfigurationServiceTrackerTestCase
        extends TestCaseAbstract {

    private ServiceListener trackerListener;
    private BundleContextMock contextMock;
    private ConfigurationManagerMock managerMock;
    private PluginManagerMock pluginManagerMock;
    private ServiceReferenceMock referenceMock;


    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        contextMock = new BundleContextMock("contextMock", expectations);
        managerMock = new ConfigurationManagerMock("managerMock",
                expectations);
        pluginManagerMock = new PluginManagerMock(
                "pluginManagerMock", expectations);

        FilterMock filterMock = new FilterMock("filterMock", expectations);

        referenceMock = new ServiceReferenceMock("referenceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects
                .createFilter(
                        "(|(objectClass=org.osgi.service.cm.ManagedService)" +
                                "(objectClass=org.osgi.service.cm.ManagedServiceFactory)" +
                                "(objectClass=org.osgi.service.cm.ConfigurationPlugin))")
                .returns(filterMock);

        contextMock.fuzzy.addServiceListener(mockFactory.expectsInstanceOf(
                ServiceListener.class), null).does(new MethodAction() {
            public Object perform(MethodActionEvent event) throws Throwable {
                trackerListener = (ServiceListener)
                        event.getArgument(ServiceListener.class);
                return null;
            }
        });

        contextMock.expects.getServiceReferences(null, filterMock.toString())
                .returns(null);

        filterMock.expects.match(referenceMock).returns(true).any();

        ConfigurationServiceTracker tracker = new ConfigurationServiceTracker(
                contextMock, managerMock, pluginManagerMock);
        tracker.start();
    }

    /**
     * Ensure that registration of {@link ManagedServiceFactory} services is
     * handled correctly.
     */
    public void testManagedServiceFactory() throws Exception {

        referenceMock.expects.getProperty(Constants.OBJECTCLASS)
                .returns(new String[]{ManagedServiceFactory.class.getName()})
                .any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // ---------------------------------------------------------------------
        // Register Service
        // ---------------------------------------------------------------------

        // Register the service, this should succeed and cause the
        // use count of the service to be incremented.

        expectations.add(new OrderedExpectations() {
            public void add() {

                managerMock.expects.factoryRegistered(referenceMock)
                        .description("Register Successfully Bound");

                contextMock.expects.getService(referenceMock)
                        .description("Register Increment Use Count")
                        .returns(null);
            }
        });

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.REGISTERED, referenceMock));

        // ---------------------------------------------------------------------
        // Modify Service
        // ---------------------------------------------------------------------

        // Modifying the service does not require any changes to the use count
        // of the service.

        managerMock.expects.factoryModified(referenceMock)
                .description("Modify Service");

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.MODIFIED, referenceMock));

        // ---------------------------------------------------------------------
        // Unregister Service
        // ---------------------------------------------------------------------

        // Unregistering the service must decrement the use count of the
        // service.

        expectations.add(new OrderedExpectations() {
            public void add() {

                managerMock.expects.factoryUnregistering(referenceMock)
                        .description("Unregistering Service");

                contextMock.expects.ungetService(referenceMock)
                        .description("Unregistering Decrement Use Count")
                        .returns(false);
            }
        });

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.UNREGISTERING, referenceMock));
    }

    /**
     * Ensure that registration of {@link ManagedService} services is handled
     * correctly.
     */
    public void testManagedService() throws Exception {

        referenceMock.expects.getProperty(Constants.OBJECTCLASS)
                .returns(new String[]{ManagedService.class.getName()})
                .any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // ---------------------------------------------------------------------
        // Register Service
        // ---------------------------------------------------------------------

        // Register the service, this should succeed and cause the
        // use count of the service to be incremented.

        expectations.add(new OrderedExpectations() {
            public void add() {

                managerMock.expects.serviceRegistered(referenceMock)
                        .description("Register Successfully Bound");

                contextMock.expects.getService(referenceMock)
                        .description("Register Increment Use Count")
                        .returns(null);
            }
        });

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.REGISTERED, referenceMock));

        // ---------------------------------------------------------------------
        // Modify Service
        // ---------------------------------------------------------------------

        // Modifying the service does not require any changes to the use count
        // of the service.

        managerMock.expects.serviceModified(referenceMock)
                .description("Modify Service");

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.MODIFIED, referenceMock));

        // ---------------------------------------------------------------------
        // Unregister Service
        // ---------------------------------------------------------------------

        // Unregistering the service must decrement the use count of the
        // service.

        expectations.add(new OrderedExpectations() {
            public void add() {

                managerMock.expects.serviceUnregistering(referenceMock)
                        .description("Unregistering Service");

                contextMock.expects.ungetService(referenceMock)
                        .description("Unregistering Decrement Use Count")
                        .returns(false);
            }
        });

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.UNREGISTERING, referenceMock));
    }

    /**
     * Ensure that registration of {@link ConfigurationPlugin} services is
     * handled correctly.
     */
    public void testConfigurationPlugin() throws Exception {

        referenceMock.expects.getProperty(Constants.OBJECTCLASS)
                .returns(new String[]{ConfigurationPlugin.class.getName()})
                .any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // ---------------------------------------------------------------------
        // Register Service
        // ---------------------------------------------------------------------

        // Register the service, this should succeed and cause the
        // use count of the service to be incremented.

        expectations.add(new OrderedExpectations() {
            public void add() {

                pluginManagerMock.expects.pluginRegistered(referenceMock)
                        .description("Register Successfully Bound");

                contextMock.expects.getService(referenceMock)
                        .description("Register Increment Use Count")
                        .returns(null);
            }
        });

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.REGISTERED, referenceMock));

        // ---------------------------------------------------------------------
        // Modify Service
        // ---------------------------------------------------------------------

        // Modifying the service does not require any changes to the use count
        // of the service.

        pluginManagerMock.expects.pluginModified(referenceMock)
                .description("Modify Service");

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.MODIFIED, referenceMock));

        // ---------------------------------------------------------------------
        // Unregister Service
        // ---------------------------------------------------------------------

        // Unregistering the service must decrement the use count of the
        // service.

        expectations.add(new OrderedExpectations() {
            public void add() {

                pluginManagerMock.expects.pluginUnregistering(referenceMock)
                        .description("Unregistering Service");

                contextMock.expects.ungetService(referenceMock)
                        .description("Unregistering Decrement Use Count")
                        .returns(false);
            }
        });

        trackerListener.serviceChanged(
                new ServiceEvent(ServiceEvent.UNREGISTERING, referenceMock));
    }
}
