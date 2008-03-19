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

import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import org.osgi.service.cm.Configuration;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Test cases for {@link Configuration}.
 *
 * <p>This tests the external interface as defined by OSGi specification, not an
 * individual class. Therefore, as some of the specification is performed by the
 * bundle specific instance and some by the internal instance this uses
 * both.</p>
 */
public class ConfigurationTestCase
        extends AdminStoreMockBasedTestAbstract {

    /**
     * Ensure that equals() and hashCode() are defined in terms of PID.
     */
    public void testEqualsHashCode() throws Exception {

        String pid1 = "PID1";
        String pid2 = "PID2";

        storeMock.expects.createConfiguration(
                "PID1", AUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID1", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null));

        storeMock.expects.createConfiguration(
                "PID2", AUTHORIZED_BUNDLE_LOCATION)
                .returns(new ConfigurationImpl("PID2", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null));

        Configuration configuration1 = authorizedAdmin.getConfiguration(pid1);
        Configuration configuration2 = authorizedAdmin.getConfiguration(pid2);
        Configuration configuration3 = authorizedAdmin.getConfiguration(pid1);

        assertEquals(configuration1, configuration3);
        assertNotEquals(configuration1, configuration2);
        assertTrue(configuration1.hashCode() == configuration3.hashCode());
        assertTrue(pid1.hashCode() != pid2.hashCode());
        assertTrue(configuration1.hashCode() != configuration2.hashCode());
    }

    /**
     * Ensure that delete() behaves correctly according to the specification.
     */
    public void testDelete() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        Dictionary dictionary = new CaseInsensitiveDictionary();
        dictionary.put("a", "b");

        InternalConfiguration internalConfiguration =
                new ConfigurationImpl("pid", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null);

        storeMock.expects.createConfiguration(
                "pid", AUTHORIZED_BUNDLE_LOCATION)
                .returns(internalConfiguration);

        storeMock.expects.update(internalConfiguration);

        dispatcherMock.expects
                .managedServiceConfigurationUpdated(EMPTY_REFERENCES,
                        new ConfigurationSnapshot("pid", dictionary));

        storeMock.expects.remove(internalConfiguration);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Configuration configuration = authorizedAdmin.getConfiguration("pid");

        // Make sure that the configuration can be retrieved before deletion.
        configuration.update(dictionary);

        Configuration[] configurations;
        configurations = authorizedAdmin.listConfigurations(null);
        assertCorrectConfigurations(new Configuration[]{
                configuration
        }, configurations);

        // Delete the configuration.
        configuration.delete();

        // Make sure that the configuration cannot be retrieved after
        // deletion.
        configurations = authorizedAdmin.listConfigurations(null);
        assertCorrectConfigurations(null, configurations);

        try {
            configuration.delete();
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }

        try {
            configuration.getBundleLocation();
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }

        try {
            configuration.getFactoryPid();
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }

        try {
            configuration.getPid();
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }

        try {
            configuration.getProperties();
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }

        try {
            configuration.setBundleLocation("blah");
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }

        try {
            configuration.update();
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }

        try {
            configuration.update(new CaseInsensitiveDictionary());
            fail("Failed to check delete state");
        } catch (IllegalStateException expected) {
            assertEquals("Configuration 'pid' has been deleted",
                    expected.getMessage());
        }
    }

    private Configuration createConfiguration(String pid)
            throws IOException {

        return authorizedAdmin.getConfiguration(pid);
    }

    /**
     * Ensure that getPid() behaves correctly according to the specification.
     */
    public void testGetPid() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        InternalConfiguration internalConfiguration =
                new ConfigurationImpl("PID", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null);

        storeMock.expects.createConfiguration(
                "PID", AUTHORIZED_BUNDLE_LOCATION)
                .returns(internalConfiguration);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Configuration configuration = authorizedAdmin.getConfiguration("PID");
        assertEquals("PID", configuration.getPid());
        assertNull(configuration.getFactoryPid());
    }

    /**
     * Ensure that getFactoryPid() behaves correctly according to the
     * specification.
     */
    public void testGetFactoryPid() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        InternalConfiguration internalConfiguration =
                new ConfigurationImpl("PID", "FACTORY-PID", null,
                        AUTHORIZED_BUNDLE_LOCATION, null);

        storeMock.expects.createFactoryConfiguration(
                "FACTORY-PID", AUTHORIZED_BUNDLE_LOCATION)
                .returns(internalConfiguration);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Configuration configuration =
                authorizedAdmin.createFactoryConfiguration("FACTORY-PID");
        assertEquals("PID", configuration.getPid());
        assertEquals("FACTORY-PID", configuration.getFactoryPid());
    }


    /**
     * Ensure that getProperties() behaves correctly according to the
     * specification.
     */
    public void testGetProperties() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        InternalConfiguration internalConfiguration =
                new ConfigurationImpl("PID", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null);

        storeMock.expects.createConfiguration(
                "PID", AUTHORIZED_BUNDLE_LOCATION)
                .returns(internalConfiguration);

        storeMock.expects.update(internalConfiguration);

        Dictionary properties = new Hashtable();
        properties.put("a", "b");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Configuration configuration = createConfiguration("PID");
        assertNull(configuration.getProperties());

        dispatcherMock.expects
                .managedServiceConfigurationUpdated(EMPTY_REFERENCES,
                        new ConfigurationSnapshot("PID", properties));

        configuration.update(properties);

        Dictionary out = configuration.getProperties();
        assertNotSame(properties, out);
        assertEquals("b", out.get("a"));
        assertEquals("b", out.get("A"));
    }

    /**
     * Ensure that update() methods behave correctly according to the
     * specification.
     */
    public void testUpdateMethods() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        InternalConfiguration internalConfiguration =
                new ConfigurationImpl("PID", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null);

        storeMock.expects.createConfiguration(
                "PID", AUTHORIZED_BUNDLE_LOCATION)
                .returns(internalConfiguration);

        Dictionary properties = new Hashtable();
        properties.put("a", "b");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Configuration configuration = createConfiguration("PID");

        dispatcherMock.expects
                .managedServiceConfigurationUpdated(EMPTY_REFERENCES,
                        new ConfigurationSnapshot("PID",
                                new Hashtable()));

        configuration.update();

        storeMock.expects.update(internalConfiguration);

        dispatcherMock.expects
                .managedServiceConfigurationUpdated(EMPTY_REFERENCES,
                        new ConfigurationSnapshot("PID", properties));

        configuration.update(properties);
    }

    /**
     * Ensure that bound location methods behave correctly according to the
     * specification.
     */
    public void testBoundLocationMethods() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        InternalConfiguration internalConfiguration1 =
                new ConfigurationImpl("PID1", null, null,
                        AUTHORIZED_BUNDLE_LOCATION, null);

        storeMock.expects.createConfiguration(
                "PID1", AUTHORIZED_BUNDLE_LOCATION)
                .returns(internalConfiguration1);

        InternalConfiguration internalConfiguration2 =
                new ConfigurationImpl("PID2", null, null,
                        ATTACKING_BUNDLE_LOCATION, null);

        storeMock.expects.createConfiguration(
                "PID2", ATTACKING_BUNDLE_LOCATION)
                .returns(internalConfiguration2);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Configuration configuration = authorizedAdmin.getConfiguration("PID1");

        // Make sure that when the bundle has permission that the accessors
        // work.
        configuration.setBundleLocation("fred");

        assertEquals("fred", configuration.getBundleLocation());

        // Make sure that when the bundle does not have permission that the
        // accessors fail.
        configuration = attackingAdmin.getConfiguration("PID2");
        try {
            configuration.setBundleLocation("fred");
            fail("Did not detect security problem");
        } catch (SecurityException expected) {
            // Expected.
        }

        try {
            configuration.getBundleLocation();
            fail("Did not detect security problem");
        } catch (SecurityException expected) {
            // Expected.
        }
    }
}
