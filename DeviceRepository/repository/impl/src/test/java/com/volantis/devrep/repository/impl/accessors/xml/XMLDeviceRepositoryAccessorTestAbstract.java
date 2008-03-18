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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.accessors.xml;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor;
import com.volantis.devrep.repository.impl.DeviceTACPair;
import com.volantis.devrep.repository.impl.TACValue;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessorTestAbstract;
import com.volantis.devrep.repository.impl.devices.policy.DefaultPolicyDescriptor;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.BooleanPolicyType;
import com.volantis.mcs.devices.policy.types.IntPolicyType;
import com.volantis.mcs.devices.policy.types.OrderedSetPolicyType;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.RangePolicyType;
import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.mcs.devices.policy.types.StructurePolicyType;
import com.volantis.mcs.devices.policy.types.TextPolicyType;
import com.volantis.mcs.devices.policy.types.UnorderedSetPolicyType;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.testtools.io.ResourceTemporaryFileCreator;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Tests XML {@link AbstractDeviceRepositoryAccessor}s. This test assumes that
 * the data contained in the <code>runtime_repository.zip</code> file accessed
 * by a number of the tests matches the data defined in the various protected
 * supporting <code>getExpected</code>... methods.
 */
public abstract class XMLDeviceRepositoryAccessorTestAbstract
    extends AbstractDeviceRepositoryAccessorTestAbstract {
    /**
     * The name of the root device.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     */
    protected static final String ROOT_DEVICE_NAME = "Master";

    /**
     * The name of a non-root device.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     */
    protected static final String RICOH_DEVICE_NAME = "Ricoh-RDC-i700";

    /**
     * A temporary file creator for the runtime repository file associated with
     * (some of) these tests.
     */
    private static final ResourceTemporaryFileCreator
            RUNTIME_REPOSITORY_CREATOR = new ResourceTemporaryFileCreator(
                    XMLDeviceRepositoryAccessorTestAbstract.class,
                            "runtime_repository.zip");

    protected abstract AbstractDeviceRepositoryAccessor createAccessor(
        File file);

    /**
     * Factory method used to create the repository connection used when
     * calling the tested accessor's methods.
     *
     * @return a repository connection appropriate to the testing
     */
    protected RepositoryConnection createConnection() {
        // The tested implementation of the runtime device repository
        // accessor doesn't need a connection to perform its duties.
        return null;
    }

    // javadoc unnecessary
    public void testEnumerateDevicePatterns() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                Set expectedPatterns = getExpectedDevicePatterns();

                RepositoryEnumeration actualEnum =
                    accessor.enumerateDevicePatterns(createConnection());

                Set actualPatterns = new HashSet();

                while (actualEnum.hasNext()) {
                    String[] pair = (String[])actualEnum.next();
                    actualPatterns.add(new StringPair(pair[0], pair[1]));
                }

                assertEquals("the pattern enumeration is not as",
                             expectedPatterns,
                             actualPatterns);
            }
        });
    }

    /**
     * Test enumating policy names.
     */
    public void testEnumeratePolicyNames() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                // Create a test connection
                RepositoryConnection connection = createConnection();

                RepositoryEnumeration enumeration = accessor.enumeratePolicyNames(connection);

                // Check successful operation of the enumeration
                assertNotNull("Enumeration over retrieved items should exist", enumeration);
                assertTrue("Should be items in the enumeration", enumeration.hasNext());

                // Create a list of returned results
                List results = new ArrayList();
                while (enumeration.hasNext()) {
                    results.add(enumeration.next());
                }

                // Expected results
                String[] expected = new String[] {
                    "java",
                    "UAProf.CcppAccept",
                    "protocol",
                    "fallback", // this one is added at runtime.
                };

                // And now confirm that the expected and actual results match
                List expectedList = Arrays.asList(expected);
                assertEquals("Expected and actual should match", results, expectedList);
            }
        });
    }

    /**
     * Test enumating policy names.
     */
    public void testEnumerateCategoryNames() throws Exception {

        final TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                final AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                // Create a test connection
                final RepositoryConnection connection = createConnection();

                final RepositoryEnumeration enumeration =
                    accessor.enumerateCategoryNames(connection);

                // Check successful operation of the enumeration
                assertNotNull("Enumeration over retrieved items should exist", enumeration);
                assertTrue("Should be items in the enumeration", enumeration.hasNext());

                // Create a list of returned results
                final List results = new ArrayList();
                while (enumeration.hasNext()) {
                    results.add(enumeration.next());
                }

                // Expected results
                final String[] expected =
                    new String[] {"system", "browser", "protocol", "custom"};

                // And now confirm that the expected and actual results match
                final List expectedList = Arrays.asList(expected);
                assertEquals("Expected and actual should match", results, expectedList);
            }
        });
    }

    /**
     * Test enumeration of device/TAC pairings.
     *
     * @throws Exception if an error occurs
     */
    public void testEnumerateDeviceTACs() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                RepositoryConnection connection = createConnection();

                RepositoryEnumeration enumeration = accessor.enumerateDeviceTACs(connection);
                assertTrue("Enumeration should contain elements", enumeration.hasNext());
                DeviceTACPair dtp = (DeviceTACPair) enumeration.next();
                assertEquals("First device name should match 'Nokia-6210'",
                        "Nokia-6210", dtp.getDeviceName());
                assertEquals("First device TAC should match '350612'",
                        350612, dtp.getTAC());
                int deviceTacCount = 1;
                while (enumeration.hasNext()) {
                    enumeration.next();
                    deviceTacCount += 1;
                }
                assertEquals("Repository should contain expected number of TACs", 3,
                        deviceTacCount);
            }
        });
    }

    /**
     * Test enumeration of device/TAC pairings where there is no TAC data.
     *
     * @throws Exception if an error occurs
     */
    public void testEnumerateDeviceTACsWithoutTACs() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                RepositoryConnection connection = createConnection();

                RepositoryEnumeration enumeration = accessor.enumerateDeviceTACs(connection);
                assertFalse("With no TAC data, enumeration should be empty",
                        enumeration.hasNext());
            }
        });
    }

    /**
     * Test enumating policy names for a given category.
     */
    public void testEnumeratePolicyNamesWithCategory() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                // Create a test connection
                RepositoryConnection connection = createConnection();

                RepositoryEnumeration enumeration = accessor.enumeratePolicyNames(connection,
                                                                           "system");

                // Check successful operation of the enumeration
                assertNotNull("Enumeration over retrieved items should exist", enumeration);
                assertTrue("Should be items in the enumeration", enumeration.hasNext());

                // Create a list of returned results
                List results = new ArrayList();
                while (enumeration.hasNext()) {
                    results.add(enumeration.next());
                }

                // Expected results
                String[] expected = new String[] {
                    "java"
                };

                // And now confirm that the expected and actual results match
                List expectedList = Arrays.asList(expected);
                assertEquals("Expected and actual should match", results, expectedList);
            }
        });
    }

    /**
     * Test retrieving a category descriptor.
     */
    public void testRetrieveCategoryDescriptor() throws Exception {
        final TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(final File deviceRepositoryFile) throws Exception {

                final AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                final Locale locale = Locale.getDefault();

                // Create a test connection
                final RepositoryConnection connection = createConnection();

                CategoryDescriptor descriptor =
                        accessor.retrieveCategoryDescriptor(
                                connection, "protocol", locale);
                // Ensure the name matches as expected
                assertEquals("Descriptive name should match (boolean)",
                        descriptor.getCategoryDescriptiveName(),
                        "Protocol");

                descriptor = accessor.retrieveCategoryDescriptor(
                                connection, "audio", new Locale("de"));
                // Ensure the name matches as expected
                assertEquals("Descriptive name should match",
                        descriptor.getCategoryDescriptiveName(),
                        "Audiodaten");

                descriptor = accessor.retrieveCategoryDescriptor(
                                connection, "audio", new Locale("de", "CH"));
                // Ensure the name matches as expected
                assertEquals("Descriptive name should match",
                        descriptor.getCategoryDescriptiveName(),
                        "Audiodaten");
            }
        });
    }

    /**
     * Test retrieving category descriptors.
     */
    public void testRetrieveCategoryDescriptors() throws Exception {
        final TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(final File deviceRepositoryFile) throws Exception {

                final AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                // Create a test connection
                final RepositoryConnection connection = createConnection();

                final List descriptors =
                    accessor.retrieveCategoryDescriptors(connection, "audio");
                assertEquals(2, descriptors.size());
                final CategoryDescriptor descriptor1 =
                    (CategoryDescriptor) descriptors.get(0);
                checkLanguage(descriptor1);
                final CategoryDescriptor descriptor2 =
                    (CategoryDescriptor) descriptors.get(1);
                checkLanguage(descriptor2);
                assertNotEquals(descriptor1.getLanguage(),
                    descriptor2.getLanguage());
            }

            private void checkLanguage(final CategoryDescriptor descriptor) {
                final String language = descriptor.getLanguage();
                if (language.equals("__")) {
                    assertEquals("Audio",
                        descriptor.getCategoryDescriptiveName());
                } else if (language.equals("de__")) {
                    assertEquals("Audiodaten",
                        descriptor.getCategoryDescriptiveName());
                } else {
                    fail("Unexpected language: " + language);
                }
            }
        });
    }

    /**
     * Test retrieving category descriptors.
     */
    public void testRetrievePolicyDescriptors() throws Exception {
        final TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(final File deviceRepositoryFile) throws Exception {

                final AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                // Create a test connection
                final RepositoryConnection connection = createConnection();

                final List descriptors =
                    accessor.retrievePolicyDescriptors(connection, "J2MEconf");
                assertEquals(2, descriptors.size());
                final PolicyDescriptor descriptor1 =
                    (PolicyDescriptor) descriptors.get(0);
                checkLanguage(descriptor1);
                final PolicyDescriptor descriptor2 =
                    (PolicyDescriptor) descriptors.get(1);
                checkLanguage(descriptor2);
                assertNotEquals(descriptor1.getLanguage(),
                    descriptor2.getLanguage());
            }

            private void checkLanguage(final PolicyDescriptor descriptor) {
                final String language = descriptor.getLanguage();
                if (language.equals("__")) {
                    assertEquals("J2ME configuration",
                        descriptor.getPolicyDescriptiveName());
                    assertEquals("Describes the specific J2ME configuration " +
                            "implemented on the device",
                        descriptor.getPolicyHelp());
                } else if (language.equals("de__")) {
                    assertEquals("J2ME-Konfiguration",
                        descriptor.getPolicyDescriptiveName());
                    assertEquals("Beschreibt die spezifische J2ME-Konfiguration" +
                            ", die auf der Einheit implementiert ist",
                        descriptor.getPolicyHelp());
                } else {
                    fail("Unexpected language: " + language);
                }
            }
        });
    }

    /**
     * Test retrieving a policy descriptor.
     */
    public void testRetrievePolicyDescriptor() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                final Locale locale = Locale.getDefault();

                // Create a test connection
                RepositoryConnection connection = createConnection();

                // ------------------------------------------------------------
                // boolean
                // ------------------------------------------------------------
                DefaultPolicyDescriptor descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(
                                connection, "mmssupp", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (boolean)",
                        descriptor.getPolicyDescriptiveName(),
                        "Supports MMS");
                assertEquals("Help should match (boolean)",
                        descriptor.getPolicyHelp(),
                        "Indicates whether the device supports the Multimedia" +
                        " Messaging Service (MMS)");
                assertEquals("Category should match (boolean)",
                        descriptor.getCategoryName(),
                        "system");
                // Ensure the type is of the correct type (try a cast!)
                BooleanPolicyType booleanType =
                        (BooleanPolicyType) descriptor.getPolicyType();

                // ------------------------------------------------------------
                // int
                // ------------------------------------------------------------
                descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(
                                connection, "cpuclock", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (int)",
                        descriptor.getPolicyDescriptiveName(),
                        "Processor clock rate");
                assertEquals("Help should match (int)",
                        descriptor.getPolicyHelp(),
                        "Processor speed in MHz. A value -1 indicates that the" +
                        " speed is unknown");
                assertEquals("Category should match (int)",
                        descriptor.getCategoryName(),
                        "system");
                // Ensure the type is of the correct type (try a cast!)
                IntPolicyType intType = (IntPolicyType) descriptor.getPolicyType();

                // ------------------------------------------------------------
                // range
                // ------------------------------------------------------------
                descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(
                                connection, "localsec", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (range)",
                        descriptor.getPolicyDescriptiveName(),
                        "Local data security");
                assertEquals("Help should match (range)",
                        descriptor.getPolicyHelp(),
                        "An indication of the level of security built into the" +
                        " device for securing user data");
                assertEquals("Category should match (range)",
                        descriptor.getCategoryName(),
                        "security");
                // Ensure the type is of the correct type (try a cast!)
                RangePolicyType rangeType = (RangePolicyType) descriptor.getPolicyType();
                assertEquals("Max should match", 100, rangeType.getMaxInclusive());
                assertEquals("Min should match", -1, rangeType.getMinInclusive());

                // ------------------------------------------------------------
                // selection
                // ------------------------------------------------------------
                descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(
                                connection, "J2MEconf", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (selection)",
                        descriptor.getPolicyDescriptiveName(),
                        "J2ME configuration");
                assertEquals("Help should match (selection)",
                        descriptor.getPolicyHelp(),
                        "Describes the specific J2ME configuration implemented" +
                        " on the device");
                assertEquals("Category should match (selection)",
                        descriptor.getCategoryName(),
                        "system");
                // Ensure the type is of the correct type (try a cast!)
                SelectionPolicyType selectionType =
                        (SelectionPolicyType) descriptor.getPolicyType();
                String[] expected = new String[]{"none", "CLDC-1.0", "CDC-1.0"};
                List keywords = selectionType.getKeywords();
                int size = keywords.size();
                for (int i = 0; i < size; i++) {
                    assertEquals("Keyword " + i + " should match",
                            (String) keywords.get(i),
                            expected[i]);
                }

                // ------------------------------------------------------------
                // text
                // ------------------------------------------------------------
                descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(
                                connection, "cpumfgr", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (text)",
                        descriptor.getPolicyDescriptiveName(),
                        "Processor manufacturer");
                assertEquals("Help should match (text)",
                        descriptor.getPolicyHelp(),
                        "The manufacturer of the device processor");
                assertEquals("Category should match (text)",
                        descriptor.getCategoryName(),
                        "system");
                // Ensure the type is of the correct type (try a cast!)
                TextPolicyType textType = (TextPolicyType) descriptor.getPolicyType();

                // ------------------------------------------------------------
                // structure
                // ------------------------------------------------------------
                descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(connection,
                                "protocol.wml.emulate.bigTag", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (structure)",
                        descriptor.getPolicyDescriptiveName(),
                        "Emulate WML big tag");
                assertEquals("Help should match (structure)",
                        descriptor.getPolicyHelp(),
                        "Controls markup or textual substitution for the WML" +
                        " <big> element");
                assertEquals("Category should match (structure)",
                        descriptor.getCategoryName(),
                        "protocol");
                // Ensure the type is of the correct type (try a cast!)
                StructurePolicyType structureType =
                        (StructurePolicyType) descriptor.getPolicyType();

                Map fields = structureType.getFieldTypes();
                Set keys = fields.keySet();
                String[] expectedKeys = new String[]{
                    "enable", "prefix", "suffix", "altTag"
                };
                size = expectedKeys.length;
                for (int i = 0; i < size; i++) {
                    assertTrue("Key " + i + " should be in the key set",
                            keys.contains(expectedKeys[i]));
                    assertNotNull("Key " + i + " should map to a non-null value",
                            fields.get(expectedKeys[i]));
                }

                // ------------------------------------------------------------
                // ordered set
                // ------------------------------------------------------------
                descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(
                                connection, "protocol", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (ordered set)",
                        descriptor.getPolicyDescriptiveName(),
                        "Protocol");
                assertEquals("Help should match (ordered set)",
                        descriptor.getPolicyHelp(),
                        "The protocol (markup language) to be used to" +
                        " communicate with the browser");
                assertEquals("Category should match (ordered set)",
                        descriptor.getCategoryName(),
                        "browser");
                // Ensure the type is of the correct type (try a cast!)
                OrderedSetPolicyType orderedSetType =
                        (OrderedSetPolicyType) descriptor.getPolicyType();
                PolicyType memberType = orderedSetType.getMemberPolicyType();
                assertTrue("Type of set values should match",
                        memberType instanceof SelectionPolicyType);

                // ------------------------------------------------------------
                // unordered set
                // ------------------------------------------------------------
                descriptor = (DefaultPolicyDescriptor)
                        accessor.retrievePolicyDescriptor(connection,
                                "UAProf.Push-Accept", locale);
                // Ensure the names and help match as expected
                assertEquals("Descriptive name should match (unordered set)",
                        descriptor.getPolicyDescriptiveName(),
                        "Push content types supported");
                assertEquals("Help should match (unordered set)",
                        descriptor.getPolicyHelp(),
                        "List of content types the device supports for push");
                assertEquals("Category should match (unordered set)",
                        descriptor.getCategoryName(),
                        "message");
                // Ensure the type is of the correct type (try a cast!)
                UnorderedSetPolicyType unorderedSetType =
                        (UnorderedSetPolicyType) descriptor.getPolicyType();
                memberType = unorderedSetType.getMemberPolicyType();
                assertTrue("Type of set values should match",
                        memberType instanceof TextPolicyType);

                {
                    // -------------------------------------------------------
                    // fallback (fake)
                    // -------------------------------------------------------
                    descriptor = (DefaultPolicyDescriptor)
                            accessor.retrievePolicyDescriptor(connection,
                                    "fallback", locale);
                    // Ensure the names and help match as expected
                    assertEquals("Descriptive name should match (fallback)",
                            descriptor.getPolicyDescriptiveName(), "fallback");
                    assertEquals("Help should match (fallback)",
                            descriptor.getPolicyHelp(), "fallback");
                    assertEquals("Category should match (fallback)",
                            descriptor.getCategoryName(),
                            "identification");
                    // Ensure the type is of the correct type
                    assertTrue(descriptor.getPolicyType() instanceof
                            TextPolicyType);
                }
            }
        });

    }

    // javadoc unnecessary
    public void testAddDeviceImpl() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                try {
                    accessor.addDevice(createConnection(),
                            new DefaultDevice("PC-MacOS", new HashMap(), null));

                    fail("Should have had a repository exception");
                } catch (RepositoryException e) {
                    // Expected condition
                }
            }
        });
    }

    // javadoc unnecessary
    public void testRemoveDeviceImpl() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                try {
                    accessor.removeDevice(createConnection(), "PC");

                    fail("Should have had a repository exception");
                } catch (RepositoryException e) {
                    // Expected condition
                }
            }
        });
    }

    // javadoc unnecessary
    public void testRetrieveDeviceImplRoot() throws Exception {
        doRetrieveDeviceImplTest(ROOT_DEVICE_NAME);
    }

    // javadoc unnecessary
    public void testRetrieveDeviceImplRicoh() throws Exception {
        doRetrieveDeviceImplTest(RICOH_DEVICE_NAME);
    }

    // javadoc unnecessary
    public void testEnumerateDevicesChildrenImplRoot() throws Exception {
        doEnumerateDevicesChildrenImplTest(ROOT_DEVICE_NAME);
    }

    // javadoc unnecessary
    public void testEnumerateDevicesChildrenImplRicoh() throws Exception {
        doEnumerateDevicesChildrenImplTest(ROOT_DEVICE_NAME);
    }

    // javadoc unnessary
    public void testRetrieveDevice() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                DefaultDevice master = accessor.retrieveDevice(createConnection(),
                        "Master");
                assertEquals("", "Master", master.getName());

                assertNull("No TACs should be defined for Master",
                        master.getTACValues());

                int count = 0;
                for (Iterator iter = master.getPolicyNames(); iter.hasNext(); iter.next()) {
//                    final String name = (String) iter.next();
//                    System.out.println(
//                        name + " -> " + master.getPolicyValue(name));
                    count++;
                }
                assertEquals(289, count);
                assertEquals("Unexpected download value",
                        "false", master.getPolicyValue("download"));
                assertEquals("Unexpected UAProf.CcppAccept value",
                        "text/html,image/jpeg,image/gif",
                        master.getPolicyValue("UAProf.CcppAccept"));
                assertEquals("Unexpected protocol.wml.emulate.cardTitle.enable",
                        "false",
                        master.getPolicyValue("protocol.wml.emulate.cardTitle.enable"));

                // Check a device which is expected to have TACs
                DefaultDevice nokia = accessor.retrieveDevice(createConnection(),
                        "Nokia-6210");
                assertEquals("Name should be as expected",
                        "Nokia-6210", nokia.getName());
                Set tacs = nokia.getTACValues();
                assertEquals("Nokia-6210 should have two TACs",
                        2, tacs.size());
                assertTrue("Nokia-6210 TACs should include 35061220",
                        tacs.contains(new TACValue(35061220)));
                assertTrue("Nokia-6210 TACs should include 350612",
                        tacs.contains(new TACValue(350612)));
            }
        });
    }

    // javadoc unnecessary
    public void testRenameDeviceImpl() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                try {
                    accessor.renameDevice(createConnection(),
                            ROOT_DEVICE_NAME,
                            "NewName");

                    fail("Should have had a repository exception");
                } catch (RepositoryException e) {
                    // Expected condition
                }
            }
        });
    }

    // javadoc unnecessary
    public void testRemovePolicy() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                try {
                    accessor.removePolicy(createConnection(),
                            ROOT_DEVICE_NAME);

                    fail("Should have had a repository exception");
                } catch (RepositoryException e) {
                    // Expected condition
                }
            }
        });
    }

    // javadoc unnecessary
    public void testEnumerateDeviceNames() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                Set expectedDeviceNames = new HashSet();
                Set actualDeviceNames = new HashSet();
                RepositoryEnumeration nameEnum =
                    accessor.enumerateDeviceNames(createConnection());

                String[] deviceNames = getExpectedDeviceNames();

                for (int i = 0; i < deviceNames.length; i++) {
                    expectedDeviceNames.add(deviceNames[i]);
                }

                while (nameEnum.hasNext()) {
                    actualDeviceNames.add(nameEnum.next());
                }

                assertEquals("device names not as",
                             expectedDeviceNames,
                             actualDeviceNames);
            }
        });
    }

    // javadoc unnecessary
    public void testEnumerateDeviceFallbacks() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                Map expectedFallbacks = getExpectedDeviceFallbacks();
                Map actualFallbacks = new HashMap();

                RepositoryEnumeration fallbackEnum =
                    accessor.enumerateDeviceFallbacks(createConnection());

                while (fallbackEnum.hasNext()) {
                    String[] fallback = (String[])fallbackEnum.next();

                    actualFallbacks.put(fallback[0], fallback[1]);
                }

                assertEquals("device fallbacks not as",
                             expectedFallbacks.entrySet(),
                             actualFallbacks.entrySet());
            }
        });
    }

    // javadoc unnecessary
    public void testUpdatePolicyName() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                try {
                    accessor.updatePolicyName(createConnection(),
                            "java",
                            "beanies");

                    fail("Should have had a repository exception");
                } catch (RepositoryException e) {
                    // Expected condition
                }
            }
        });
    }

    /**
     * Test {@link AbstractDeviceRepositoryAccessor#retrieveDeviceImpl}
     * for the named device.
     *
     * @param deviceName the device to be checked against. Must not be null
     */
    protected void doRetrieveDeviceImplTest(final String deviceName)
        throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                if (deviceName == null) {
                    throw new IllegalArgumentException("test case fault");
                }

                DefaultDevice expectedDevice =
                    getExpectedDevice(deviceName);
                DefaultDevice actualDevice =
                    accessor.retrieveDevice(createConnection(), deviceName);

                assertEquals(expectedDevice, actualDevice);
            }
        });
    }

    /**
     * Test {@link
     * XMLDeviceRepositoryAccessor #enumerateDevicesChildrenImpl}
     * for the named device.
     *
     * @param deviceName the device to be checked against. Must not be null
     */
    protected void doEnumerateDevicesChildrenImplTest(final String deviceName)
        throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                RUNTIME_REPOSITORY_CREATOR);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                Set expectedChildren = new HashSet();
                Set actualChildren = new HashSet();
                Map fallbacks = getExpectedDeviceFallbacks();
                Iterator i = fallbacks.keySet().iterator();

                if (deviceName == null) {
                    throw new IllegalArgumentException("test case fault");
                }

                RepositoryEnumeration childrenEnum =
                    accessor.enumerateDevicesChildren(createConnection(),
                            deviceName);

                while (i.hasNext()) {
                    String name = (String)i.next();
                    String fallback = (String)fallbacks.get(name);

                    if (deviceName.equals(fallback)) {
                        expectedChildren.add(name);
                    }
                }

                while (childrenEnum.hasNext()) {
                    actualChildren.add(childrenEnum.next());
                }

                assertEquals(deviceName + " children not as",
                             expectedChildren,
                             actualChildren);
            }
        });
    }

    /**
     * Supporting method that uses JUnit assertions to verify that the two
     * given devices have the same definitions. Neither device may be null.
     *
     * @param expectedDevice
     *         the expected definition. Must not be null
     * @param actualDevice
     *         the actual definition. Must not be null
     */
    protected void assertEquals(DefaultDevice expectedDevice,
                                DefaultDevice actualDevice)
        throws Exception {
        Map expectedPatterns = expectedDevice.getPatterns();
        Map actualPatterns = actualDevice.getPatterns();
        String[] policies = getExpectedPolicyNames();

        assertEquals("name not as",
                     expectedDevice.getName(),
                     actualDevice.getName());

        // Verify the patterns
        if (expectedPatterns == null) {
            assertNull(expectedDevice.getName() +
                       " patterns should be null",
                       actualPatterns);
        } else {
            assertNotNull(expectedDevice.getName() +
                          " patterns should not be null",
                          actualPatterns);

            assertEquals(expectedDevice.getName() +
                         " number of patterns not as",
                         expectedPatterns.size(),
                         actualPatterns.size());

            assertEquals(expectedDevice.getName() +
                         " patterns not as",
                         expectedPatterns.entrySet(),
                         actualPatterns.entrySet());
        }

        // Verify the policies
        for (int i = 0; i < policies.length; i++) {
            assertEquals(expectedDevice.getName() +
                         " policy " + policies[i] + " not as",
                         expectedDevice.getPolicyValue(policies[i]),
                         actualDevice.getPolicyValue(policies[i]));
        }
    }

    /**
     * Returns the device patterns expected as a set of {@link StringPair}s.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     *
     * @return a set of (device/pattern) {@link StringPair}s
     */
    protected Set getExpectedDevicePatterns() {
        Set expectedPatterns = new HashSet();

        String[] devicePatterns;
        String[] deviceNames = getExpectedDeviceNames();

        for (int i = 0; i < deviceNames.length; i++) {
            devicePatterns = getExpectedDevicePatterns(deviceNames[i]);

            if (devicePatterns != null) {
                for (int j = 0; j < devicePatterns.length; j++) {
                    expectedPatterns.add(new StringPair(deviceNames[i],
                                                        devicePatterns[j]));
                }
            }
        }

        return expectedPatterns;
    }

    /**
     * Returns the <code>InternalDevice</code> appropriate for the specified
     * device name.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     *
     * @param deviceName the name of the device to be retrieved. Must not be
     *                   null
     * @return an appropriately initialized {@link com.volantis.devrep.repository.api.devices.DefaultDevice}
     */
    protected DefaultDevice getExpectedDevice(String deviceName) {

        if (deviceName == null) {
            throw new IllegalArgumentException("test case fault");
        }

        Map policies = new HashMap();
        if (ROOT_DEVICE_NAME.equals(deviceName)) {
            policies.put("java",
                         "none");
            policies.put("UAProf.CcppAccept",
                         "text/html,image/jpeg,image/gif");
            policies.put("protocol", "HTMLVersion3_2");
        } else if ("PC".equals("deviceName")) {
            policies.put("java", "J2SE");
        } else if ("PC-UNIX-Opera".equals(deviceName)) {
            policies.put("protocol", "HTMLVersion4_0");
        } else if ("PC-Win32-Netscape".equals(deviceName)) {
            policies.put("protocol", "HTMLVersion4_0");
        } else if ("Mobile".equals(deviceName)) {
            policies.put("protocol", "XHTMLMobile1_0");
        } else if ("Digital-Video-Camera".equals(deviceName)) {
            policies.put("UAProf.CcppAccept",
                         "text/html,image/jpeg,image/gif,video/mp4");
        }

        policies.put("fallback", getExpectedDeviceFallback(deviceName));

        DefaultDevice device = new DefaultDevice(deviceName, policies, null);

        // Finally handle the device patterns
        String[] patternStrings = getExpectedDevicePatterns(deviceName);

        if ((patternStrings != null) &&
            (patternStrings.length != 0)) {
            Map patterns = new HashMap();

            for (int j = 0; j < patternStrings.length; j++) {
                patterns.put(patternStrings[j], null);
            }

            device.setPatterns(patterns);
        }

        return device;
    }

    /**
     * Returns the fallback device name appropriate for the specified device
     * name.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     *
     * @param deviceName the name of the device for which the fallback is to be
     *                   retrieved. Must not be null
     * @return an appropriate device name
     */
    protected String getExpectedDeviceFallback(String deviceName) {
        if (deviceName == null) {
            throw new IllegalArgumentException("test case fault");
        }

        return (String)getExpectedDeviceFallbacks().get(deviceName);
    }

    /**
     * Returns the device fallbacks as a map, keyed on device name and
     * associating to the fallback device name. Note that this explicitly
     * includes the Master device since the underlying accessor does so.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     *
     * @return the device fallbacks as a map
     */
    protected Map getExpectedDeviceFallbacks() {
        Map fallbacks = new HashMap();

        fallbacks.put(ROOT_DEVICE_NAME, null);
        fallbacks.put("PC", ROOT_DEVICE_NAME);
        fallbacks.put("PC-UNIX", "PC");
        fallbacks.put("PC-UNIX-Opera", "PC-UNIX");
        fallbacks.put("PC-UNIX-Opera7", "PC-UNIX-Opera");
        fallbacks.put("PC-Win32", "PC");
        fallbacks.put("PC-Win32-Netscape", "PC-Win32");
        fallbacks.put("PC-Win32-Netscape7", "PC-Win32-Netscape");
        fallbacks.put("Mobile", ROOT_DEVICE_NAME);
        fallbacks.put("Portable-AV-Device", "Mobile");
        fallbacks.put("Digital-Still-Camera", "Portable-AV-Device");
        fallbacks.put(RICOH_DEVICE_NAME, "Digital-Still-Camera");
        fallbacks.put("Digital-Video-Camera", "Portable-AV-Device");

        return fallbacks;
    }

    /**
     * Returns an array of device names covering all devices available in the
     * repository.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     *
     * @return an array of all device names
     */
    protected String[] getExpectedDeviceNames() {
        return new String[]{
            ROOT_DEVICE_NAME,
            "PC",
            "PC-UNIX",
            "PC-UNIX-Opera",
            "PC-UNIX-Opera7",
            "PC-Win32",
            "PC-Win32-Netscape",
            "PC-Win32-Netscape7",
            "Mobile",
            "Portable-AV-Device",
            "Digital-Still-Camera",
            RICOH_DEVICE_NAME,
            "Digital-Video-Camera"
        };
    }

    /**
     * Returns the identification patterns for the specified device name.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     *
     * @param deviceName the name of the device for which the patterns are to
     *                   be retrieved. Must not be null
     * @return an array of pattern strings for the named device
     */
    protected String[] getExpectedDevicePatterns(String deviceName) {
        String[] patterns = null;

        if (deviceName == null) {
            throw new IllegalArgumentException("test case fault");
        }

        if ("PC".equals(deviceName)) {
            patterns = new String[]{"Mozilla/1.*",
                                    "Mozilla/2.*",
                                    "Mozilla/3.*",
                                    "Mozilla/4.*",
                                    "Mozilla/5.*"};
        } else if ("PC-UNIX-Opera7".equals(deviceName)) {
            patterns = new String[]{"Mozilla/4\\.0.*Linux.*Opera 7.*",
                                    "Mozilla/5\\.0.*Linux.*Opera 7.*",
                                    "Opera/7\\..*Linux .*"};
        } else if ("PC-Win32-Netscape7".equals(deviceName)) {
            patterns = new String[]{"Mozilla/5\\.0 .* Netscape/7.*"};
        } else if (RICOH_DEVICE_NAME.equals(deviceName)) {
            patterns = new String[]{"Ricoh-RDC.*",
                                    "FAKE: Fake Ricoh-RDC-.*00",
                                    "ARTIFICIAL: Pretend.*"};
        }

        return patterns;
    }

    /**
     * Returns all policy names that are defined.
     *
     * <p><strong>NOTE:</strong> this must match the content of the
     * runtime_repository.zip.</p>
     *
     * @return an array of policy names
     */
    protected String[] getExpectedPolicyNames() {
        return new String[]{"java",
                            "UAProf.CcppAccept",
                            "protocol"};
    }

    // Javadoc inherited
    protected void runDeviceRepositoryAccessorTest(
            final AbstractDeviceRepositoryAccessorTestAbstract.
                    DeviceRepositoryAccessorTest test) throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File deviceRepositoryFile) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(deviceRepositoryFile);

                test.runTest(accessor, null);
            }
        });

    }

    /**
     * Checks that the device pattern cache is correctly returning
     * devices looked up by UAProf URI.
     *
     * @throws Exception if an error occurs
     */
    public void testUAProfCaching() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(final File repository) throws Exception {

                AbstractDeviceRepositoryAccessor accessor =
                        createAccessor(repository);

                accessor.initializeDevicePatternCache(null);

                // Access a device for which an initial KeyInfo was set up
                String devName = accessor.retrieveMatchingDeviceName("profile: http://nds.nokia.com/uaprof/N6210r100.xml");
                assertEquals("Nokia 6210 should be retrieved by UAProf URI",
                        "Nokia-6210", devName);

                // Access a device which will be in the default profile bucket
                devName = accessor.retrieveMatchingDeviceName("profile: http://device.sprintpcs.com/Handspring/HSTR300HK");
                assertEquals("Handspring should be retrieved by UAProf URI",
                        "SprintPCS-HSTR-300", devName);

                // Attempt to access a non-existent device
                devName = accessor.retrieveMatchingDeviceName("profile: http://www.volantis.com/uaprof/NoSuchPhone.xml");
                assertNull("No device should be retrieved for unknown UAProf URI",
                        devName);
            }
        });
    }

    /**
     * Used to simplify testing of equality for string pairs (such as
     * device pattern enumeration pairings etc.).
     */
    protected class StringPair {
        /**
         * The left-hand part of the pair.
         */
        public final String left;

        /**
         * The right-hand part of the pair.
         */
        public final String right;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param left the left-hand part of the pair
         * @param right the right-hand part of the pair
         */
        public StringPair(String left, String right) {
            this.left = left;
            this.right = right;
        }

        // javadoc inherited
        public boolean equals(Object o) {
            boolean equals = false;

            if (this == o) {
                equals = true;
            } else if (o.getClass() == getClass()) {
                final StringPair stringPair = (StringPair)o;

                equals = true;

                if (left != null ?
                    !left.equals(stringPair.left) :
                    stringPair.left != null) {
                    equals = false;
                } else if (right != null ?
                    !right.equals(stringPair.right) :
                    stringPair.right != null) {
                    equals = false;
                }
            }

            return equals;
        }

        // javadoc inherited
        public int hashCode() {
            int result;

            result = (left != null ? left.hashCode() : 0);
            result = 29 * result + (right != null ? right.hashCode() : 0);

            return result;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/4	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 28-Apr-05	7908/2	pduffin	VBM:2005042712 Removing Revision object, added UNKNOWN_REVISION constant to JDBCAccessorHelper. This will be removed when revisions are removed from the database tables

 01-Mar-05	6623/1	philws	VBM:2005010602 Port of correction for migration and other handling of secondary ID headers from 3.3

 01-Mar-05	7167/1	philws	VBM:2005010602 Correct migration and other handling of secondary ID headers

 23-Dec-04	6472/1	allan	VBM:2004121003 Intern device names and device policies

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Dec-04	6377/1	geoff	VBM:2004112307 Device.getRealPolicyValue(fallback) fails with NullPointerException

 22-Sep-04	5567/3	allan	VBM:2004092010 Rework issues.

 21-Sep-04	5567/1	allan	VBM:2004092010 Handle multi-valued device policy selection.

 27-Aug-04	5315/3	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 10-Aug-04	5147/1	adrianj	VBM:2004080318 Added support for TAC values to importer

 06-Aug-04	5121/1	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 27-Jul-04	4961/1	claire	VBM:2004072601 Public API for Device Repository: Implement XML metadata read support

 22-Apr-04	3975/1	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 18-Feb-04	3060/1	philws	VBM:2004021701 Implement runtime device repository accessor

 ===========================================================================
*/
