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

import com.volantis.osgi.cm.factory.ManagedServiceFactoryHandler;
import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import mock.org.osgi.framework.ServiceReferenceMock;
import org.osgi.service.cm.Configuration;
import org.osgi.service.log.LogService;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Test cases for {@link ConfigurationManager}.
 */
public class ConfigurationAdminTestCase
        extends AdminStoreMockBasedTestAbstract {

    private static final String TARGET_LOCATION = "target location";
    private static final String OTHER_TARGET_LOCATION = "other target location";

    /**
     * Ensure that the createFactoryConfiguration() method creates new instance
     * every time that it is called.
     */
    public void testCreateFactoryConfiguration() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", AUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID1", "FACTORY-PID", null,
                        AUTHORIZED_BUNDLE_LOCATION, null));

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", AUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID2", "FACTORY-PID", null,
                        AUTHORIZED_BUNDLE_LOCATION, null));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Getting a configuration should create a new one with the correct
        // pid, not factory pid, and null properties.
        Configuration configuration1 =
                authorizedAdmin.createFactoryConfiguration("FACTORY-PID");
        assertEquals("PID1", configuration1.getPid());
        assertEquals("FACTORY-PID", configuration1.getFactoryPid());
        assertNull(configuration1.getProperties());
        assertEquals(AUTHORIZED_BUNDLE_LOCATION,
                configuration1.getBundleLocation());

        // Getting a configuration should create a new one with the correct
        // pid, not factory pid, and null properties.
        Configuration configuration2 =
                authorizedAdmin.createFactoryConfiguration("FACTORY-PID");
        assertEquals("PID2", configuration2.getPid());
        assertEquals("FACTORY-PID", configuration2.getFactoryPid());
        assertNull(configuration2.getProperties());
        assertEquals(AUTHORIZED_BUNDLE_LOCATION,
                configuration2.getBundleLocation());

        // Two configurations must not be the same.
        assertNotSame(configuration1, configuration2);
    }

    /**
     * Ensure that the createFactoryConfiguration() method checks security
     * correctly.
     */
    public void testCreateFactoryConfigurationChecksSecurity()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", UNAUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID1", "FACTORY-PID", null,
                        UNAUTHORIZED_BUNDLE_LOCATION, null));

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", ATTACKING_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID2", "FACTORY-PID", null,
                        ATTACKING_BUNDLE_LOCATION, null));

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", UNAUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID3", "FACTORY-PID", null,
                        UNAUTHORIZED_BUNDLE_LOCATION, null));

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", AUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID4", "FACTORY-PID", null,
                        AUTHORIZED_BUNDLE_LOCATION, null));

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", ATTACKING_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID5", "FACTORY-PID", null,
                        ATTACKING_BUNDLE_LOCATION, null));

        // Create a handler for managing information about the registration
        // of
        ManagedServiceFactoryHandler handler =
                new ManagedServiceFactoryHandler(manager);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Creating a new configuration may check for configure permission but
        // shouldn't care if it does not have it.
        unauthorizedAdmin.createFactoryConfiguration("FACTORY-PID");

        // Creating a new configuration for a different bundle may check for
        // configure permission but shouldn't care if it does not have it.
        attackingAdmin.createFactoryConfiguration("FACTORY-PID");

        // Bind the factory PID to the unauthorized bundle.
        final ServiceReferenceMock managedServiceFactoryReferenceMock =
                new ServiceReferenceMock("managedServiceFactoryReferenceMock",
                        expectations);

        managedServiceFactoryReferenceMock.expects.getBundle()
                .returns(unauthorizedBundleMock).any();
        managedServiceFactoryReferenceMock.expects
                .getProperty(FrameworkConstants.SERVICE_PID)
                .returns("FACTORY-PID").any();

        // The unauthorized bundle should not see any configurations created
        // by the attacking bundle.
        dispatcherMock.expects.managedServiceFactoryRegistered(
                "FACTORY-PID", managedServiceFactoryReferenceMock,
                new ConfigurationSnapshot[]{
                        new ConfigurationSnapshot("PID1", new Hashtable())
                });

        // A message must be logged to the log service for the configuration
        // that was created by the attacking bundle.
        logServiceMock.expects.log(managedServiceFactoryReferenceMock,
                LogService.LOG_WARNING,
                "Configuration with pid 'PID2' for factory 'FACTORY-PID' is " +
                        "bound to '" + ATTACKING_BUNDLE_LOCATION +
                        "' instead of '" + UNAUTHORIZED_BUNDLE_LOCATION +
                        "' and so will be ignored");

        handler.serviceRegistered(managedServiceFactoryReferenceMock);

        // Creating a configuration for a factory PID that has been bound to
        // a specific bundle (the bundle that registered the
        // ManagedServiceFactory with the matching factory PID) should work
        // for the owning bundle and an authorized admin.
        unauthorizedAdmin.createFactoryConfiguration("FACTORY-PID");
        authorizedAdmin.createFactoryConfiguration("FACTORY-PID");

        // But not for an unauthorized bundle that is different.
        try {
            attackingAdmin.createFactoryConfiguration("FACTORY-PID");
            fail("Did not perform correct security check");
        } catch (SecurityException expected) {
            assertEquals("Bundle '" + ATTACKING_BUNDLE_LOCATION +
                    "' does not have permission to create configuration for " +
                    "service.factoryPid 'FACTORY-PID' which is bound to bundle '" +
                    UNAUTHORIZED_BUNDLE_LOCATION + "'",
                    expected.getMessage());
        }
    }

    /**
     * Ensure that the getConfiguration() method creates new instance every time
     * that it is called.
     */
    public void testGetConfiguration() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        storeMock.expects.createConfiguration(
                "PID1", AUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID1", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null));

        storeMock.expects.createConfiguration(
                "PID2", AUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID2", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Getting a configuration should create a new one with the correct
        // pid, not factory pid, and null properties.
        Configuration configuration1 = authorizedAdmin.getConfiguration("PID1");
        assertEquals("PID1", configuration1.getPid());
        assertNull(configuration1.getFactoryPid());
        assertNull(configuration1.getProperties());
        assertEquals(AUTHORIZED_BUNDLE_LOCATION,
                configuration1.getBundleLocation());

        // Getting a configuration should create a new one with the correct
        // pid, not factory pid, and null properties.
        Configuration configuration2 = authorizedAdmin.getConfiguration("PID2");
        assertEquals("PID2", configuration2.getPid());
        assertNull(configuration2.getFactoryPid());
        assertNull(configuration2.getProperties());
        assertEquals(AUTHORIZED_BUNDLE_LOCATION,
                configuration2.getBundleLocation());

        // Two configurations must not be the same.
        assertNotSame(configuration1, configuration2);

        // Getting a configuration that has already been retrieved should
        // return an equal but not necessarily same.
        Configuration existing = authorizedAdmin.getConfiguration("PID2");
        assertEquals(configuration2, existing);
    }

    /**
     * Ensure that the getConfiguration() method checks security correctly.
     */
    public void testGetConfigurationChecksSecurity() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        storeMock.expects.createConfiguration(
                "PID1", UNAUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID1", null, null,
                        UNAUTHORIZED_BUNDLE_LOCATION, null));

        storeMock.expects.createConfiguration("PID3", null)
                .returns(new ConfigurationImpl("PID1", null, null, null, null));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Getting a new configuration may check for configure permission but
        // shouldn't care if it does not have it.
        unauthorizedAdmin.getConfiguration("PID1");

        // Getting a configuration that already exists but is bound to the same
        // location shouldn't care about security either.
        unauthorizedAdmin.getConfiguration("PID1");

        // Getting a configuration that already exists and is bound to a
        // different location should fail if the calling bundle doesn't have
        // permission.
        try {
            attackingAdmin.getConfiguration("PID1");
            fail("Did not perform correct security check");
        } catch (SecurityException expected) {
            assertEquals("Bundle '" +
                    ATTACKING_BUNDLE_LOCATION +
                    "' does not have " +
                    "permission to create configuration for service.pid " +
                    "'PID1' which is bound to bundle '" +
                    UNAUTHORIZED_BUNDLE_LOCATION +
                    "'",
                    expected.getMessage());
        }

        // Getting a configuration that already exists and is not bound to a
        // location should work even if the calling bundle doesn't have
        // permission.
        authorizedAdmin.getConfiguration("PID3", null);
        unauthorizedAdmin.getConfiguration("PID3");

        // The bundle location should be bound to the location of the bundle
        // that requested it.
        Configuration configuration =
                authorizedAdmin.getConfiguration("PID3", null);
        assertEquals(UNAUTHORIZED_BUNDLE_LOCATION,
                configuration.getBundleLocation());
    }

    /**
     * Ensure that the getConfiguration() admin method creates new instance
     * every time that it is called.
     */
    public void testGetConfigurationAdmin() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        storeMock.expects.createConfiguration("PID1", null)
                .returns(new ConfigurationImpl("PID1", null, null, null, null));

        storeMock.expects.createConfiguration("PID2", TARGET_LOCATION)
                .returns(new ConfigurationImpl("PID2", null, null,
                        TARGET_LOCATION, null));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Getting a configuration should create a new one with the correct
        // pid, not factory pid, and null properties.
        Configuration configuration1 =
                authorizedAdmin.getConfiguration("PID1", null);
        assertEquals("PID1", configuration1.getPid());
        assertNull(configuration1.getFactoryPid());
        assertNull(configuration1.getProperties());
        assertNull(null, configuration1.getBundleLocation());

        // Getting a configuration should create a new one with the correct
        // pid, not factory pid, and null properties.
        Configuration configuration2 = authorizedAdmin.getConfiguration("PID2",
                TARGET_LOCATION);
        assertEquals("PID2", configuration2.getPid());
        assertNull(configuration2.getFactoryPid());
        assertNull(configuration2.getProperties());
        assertEquals(TARGET_LOCATION, configuration2.getBundleLocation());

        // Two configurations must not be the same.
        assertNotSame(configuration1, configuration2);

        // Getting a configuration that has already been retrieved should
        // return an equal but not necessarily same. However, it must not
        // change the bundle location for the configuration.
        Configuration existing = authorizedAdmin.getConfiguration(
                "PID2", OTHER_TARGET_LOCATION);
        assertEquals(configuration2, existing);
        assertEquals(TARGET_LOCATION, configuration2.getBundleLocation());
    }

    /**
     * Ensure that the getConfiguration() admin method checks security
     * correctly.
     */
    public void testGetConfigurationAdminChecksSecurity() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Getting a new configuration must ensure that caller has configure
        // permission, otherwise it fails.
        try {
            attackingAdmin.getConfiguration("PID1", "calling location");
            fail("Did not perform correct security check");
        } catch (SecurityException expected) {
            assertEquals("Bundle '" + ATTACKING_BUNDLE_LOCATION + "' does " +
                    "not have permission to create configuration for other " +
                    "bundles",
                    expected.getMessage());
        }
    }

    /**
     * Ensure that the listConfigurations() method behaves correctly.
     */
    public void testListConfigurations() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        InternalConfiguration authorizedInternalConfiguration =
                new ConfigurationImpl("PID1", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null);
        storeMock.expects
                .createConfiguration("PID1", AUTHORIZED_BUNDLE_LOCATION)
                .returns(authorizedInternalConfiguration);

        InternalConfiguration attackingInternalConfiguration =
                new ConfigurationImpl("PID2", null, null,
                        ATTACKING_BUNDLE_LOCATION, null);
        storeMock.expects
                .createConfiguration("PID2", ATTACKING_BUNDLE_LOCATION)
                .returns(attackingInternalConfiguration);

        InternalConfiguration unassignedInternalConfiguration =
                new ConfigurationImpl("PID3", null, null, null, null);
        storeMock.expects.createConfiguration("PID3", null)
                .returns(unassignedInternalConfiguration);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Configuration[] configurations;

        // If there are no configurations then should return null.
        configurations = authorizedAdmin.listConfigurations(null);
        assertCorrectConfigurations(null, configurations);

        Configuration authorizedConfiguration =
                authorizedAdmin.getConfiguration("PID1");
        Configuration attackingConfiguration =
                attackingAdmin.getConfiguration("PID2");
        Configuration unassignedConfiguration =
                authorizedAdmin.getConfiguration("PID3", null);

        // The authorized admin can list all the configurations for all the
        // bundles but shouldn't see bundles that haven't got properties set.

        configurations = authorizedAdmin.listConfigurations(null);
        assertCorrectConfigurations(null, configurations);

        // Update the configurations so that they can be seen.
        Dictionary properties = new CaseInsensitiveDictionary();

        storeMock.expects.update(authorizedInternalConfiguration);

        dispatcherMock.expects
                .managedServiceConfigurationUpdated(EMPTY_REFERENCES,
                        new ConfigurationSnapshot("PID1", properties));

        properties.put("a", "b");
        authorizedConfiguration.update(properties);

        storeMock.expects.update(attackingInternalConfiguration);

        dispatcherMock.expects
                .managedServiceConfigurationUpdated(EMPTY_REFERENCES,
                        new ConfigurationSnapshot("PID2", properties));

        properties.put("a", "d");
        attackingConfiguration.update(properties);

        storeMock.expects.update(unassignedInternalConfiguration);

        dispatcherMock.expects
                .managedServiceConfigurationUpdated(EMPTY_REFERENCES,
                        new ConfigurationSnapshot("PID3", properties));

        properties.put("a", "e");
        unassignedConfiguration.update(properties);

        // The authorized admin can list all the configurations for all the
        // bundles.
        configurations = authorizedAdmin.listConfigurations(null);
        assertCorrectConfigurations(new Configuration[]{
                authorizedConfiguration, attackingConfiguration,
                unassignedConfiguration
        }, configurations);

        // The unauthorized admin can only list its own configurations.
        configurations = attackingAdmin.listConfigurations(null);

        assertCorrectConfigurations(new Configuration[]{
                attackingConfiguration
        }, configurations);

        // Make sure that the filter that selects nothing returns null.
        contextMock.expects.createFilter("(a=c)")
                .returns(new FilterMatchesProperty("a", "c"));
        configurations = authorizedAdmin.listConfigurations("(a=c)");
        assertCorrectConfigurations(null, configurations);

        // Make sure that the filter that selects something returns correct.
        contextMock.expects.createFilter("(a=b)")
                .returns(new FilterMatchesProperty("a", "b"));
        configurations = authorizedAdmin.listConfigurations("(a=b)");
        assertCorrectConfigurations(new Configuration[]{
                authorizedConfiguration
        }, configurations);
    }
}
