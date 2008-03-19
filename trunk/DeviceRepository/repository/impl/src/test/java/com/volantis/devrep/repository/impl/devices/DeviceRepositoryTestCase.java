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
package com.volantis.devrep.repository.impl.devices;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorMock;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DefaultDeviceMock;
import com.volantis.devrep.repository.impl.DeviceRepositoryLocationImpl;
import com.volantis.devrep.repository.impl.devices.policy.DefaultPolicyDescriptor;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.DeviceRepositoryFactory;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.mcs.http.HttpFactory;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.MutableHttpHeaders;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Test the device repository by using an XML device repository.
 */
public class DeviceRepositoryTestCase extends TestCaseAbstract {
    private static final DeviceRepositoryAccessorFactory REPOSITORY_ACCESSOR_FACTORY =
        DeviceRepositoryAccessorFactory.getDefaultInstance();
    private static final HttpFactory HTTP_HEADERS_FACTORY = HttpFactory.getDefaultInstance();

    /**
     * This tests that it is possible to retrieve a named device from an
     * XML device repository.
     */
    public void testGetDeviceUsingDeviceName() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                String deviceName = "Master";

                Device device = repository.getDevice(deviceName);
                assertEquals("Name should match: ", deviceName, device.getName());

                device = repository.getDevice("Not Found");
                assertNull("No device should be found", device);

                deviceName = "Nokia-6210";
                device = repository.getDevice(deviceName);
                assertEquals("Name should match: ", deviceName, device.getName());
                DefaultDevice fallbackDevice =
                    ((DefaultDevice)device).getFallbackDevice();
                assertNotNull(fallbackDevice);
                assertEquals("Fallback name should match: ", "Nokia-Series7110",
                        fallbackDevice.getName());
            }
        });
    }

    /**
     * Tests retrieval of devices by IMEI number.
     *
     * @throws Exception if an error occurs.
     */
    public void testGetDeviceByIMEI() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                // Test retrieval of an IMEI that exists as an eight-digit TAC
                String devName = repository.getDeviceNameByIMEI("350612190161323");
                assertEquals("Device 3506121901613238 should be Nokia 6210 Special",
                        "Nokia-6210-Special", devName);

                // Test retrieval of an IMEI that falls back to a six-digit TAC
                devName = repository.getDeviceNameByIMEI("350612350161323");
                assertEquals("Device 3506123501613238 should be Nokia 6210",
                        "Nokia-6210", devName);

                // Test retrieval of a IMEI that does not map to a TAC of eight or six
                // digits
                devName = repository.getDeviceNameByIMEI("123456789012345");
                assertNull("Device 123456789012345 should not exist", devName);

                // Test retrieval of an invalid IMEI
                try {
                    repository.getDeviceNameByIMEI("cheddarsmonkeys");
                    fail("Attempt to find non-numeric IMEI should " +
                            "throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    // We expect an IllegalArgumentException - 'cheddarsmonkeys'
                    // is not a valid IMEI combination.
                }
                try {
                    repository.getDeviceNameByIMEI("0123456789abcde");
                    fail("Attempt to find non-numeric IMEI should " +
                            "throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    // We expect an IllegalArgumentException - '0123456789abcde'
                    // is not a valid IMEI combination.
                }

                // Test invalid length
                try {
                    repository.getDeviceNameByIMEI("0123456789");
                    fail("Attempt to find too short IMEI should " +
                            "throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    // We expect an IllegalArgumentException - '0123456789' is
                    // not a valid IMEI combination.
                }
                try {
                    repository.getDeviceNameByIMEI("01234567890123456789");
                    fail("Attempt to find too long IMEI should " +
                            "throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    // We expect an IllegalArgumentException
                    // '01234567890123456789' is not a valid IMEI combination.
                }
            }
        });
    }

    /**
     * Tests retrieval of devices by TAC/FAC combination.
     *
     * @throws Exception if an error occurs.
     */
    public void testGetDeviceByTACFAC() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                // Test retrieval of a TAC/FAC that exists
                String devName = repository.getDeviceNameByTACFAC("35061219");
                assertEquals("Device 35061219 should be Nokia 6210 Special",
                        "Nokia-6210-Special", devName);

                // Test retrieval of a TAC/FAC that falls back to a six-digit TAC
                devName = repository.getDeviceNameByTACFAC("35061235");
                assertEquals("Device 35061235 should be Nokia 6210",
                        "Nokia-6210", devName);

                // Test retrieval of a TAC/FAC that does not exist as eight or six
                // digits
                devName = repository.getDeviceNameByTACFAC("12345678");
                assertNull("Device 12345678 should not exist", devName);

                // Test retrieval of an invalid TAC/FAC
                try {
                    repository.getDeviceNameByTACFAC("cheddars");
                    fail("Attempt to find non-numeric TAC should " +
                            "throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    // We expect an IllegalArgumentException - 'cheddars' is not a
                    // valid TAC/FAC combination.
                }
            }
        });
    }

    /**
     * Tests retrieval of devices by TAC code.
     *
     * @throws Exception if an error occurs.
     */
    public void testGetDeviceByTAC() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                // Test retrieval of an eight-digit TAC that exists
                String devName = repository.getDeviceNameByTAC("35061220");
                assertEquals("Device 35061220 should be Nokia 6210",
                        "Nokia-6210", devName);

                // Test retrieval of an eight-digit TAC that does not exist
                devName = repository.getDeviceNameByTAC("12345678");
                assertNull("Device 12345678 should not exist.", devName);

                // Test retrieval of a six-digit TAC that does not exist
                devName = repository.getDeviceNameByTAC("123456");
                assertNull("Device 123456 should not exist.", devName);

                // Test retrieval of a six-digit TAC that exists
                devName = repository.getDeviceNameByTAC("350612");
                assertEquals("Device 350612 should be Nokia 6210",
                        "Nokia-6210", devName);

                // Test retrieval of an invalid TAC
                try {
                    repository.getDeviceNameByTAC("monkey");
                    fail("Attempt to find non-numeric TAC should " +
                            "throw IllegalArgumentException");
                } catch (IllegalArgumentException iae) {
                    // We expect an IllegalArgumentException - 'monkey' is not a
                    // valid TAC.
                }
            }
        });
    }

    /**
     * Test getting a device using HttpHeaders.
     */
    public void testGetDeviceUsingServletHttpHeaders() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                final DeviceRepositoryFactory factory =
                    DeviceRepositoryFactory.getDefaultInstance();
                final URL deviceRepositoryUrl = file.toURL();
                final DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);


                Device device = repository.getDevice((HttpHeaders) null);
                assertNull("No device should be found", device);

                MutableHttpHeaders headers =
                    HTTP_HEADERS_FACTORY.createHTTPHeaders();
                device = repository.getDevice(headers);
                assertNotNull("Valid device should be found", device);
                assertEquals("Device name should match", "PC",
                    device.getName());

                headers.addHeader("User-Agent", "Nokia3330/");
                device = repository.getDevice(headers);
                assertNotNull("Valid device should be found", device);
                assertEquals("Device name should match", "Nokia-3330",
                    device.getName());
             }
        });

    }

    /**
     * Test getting a device using HttpHeaders.
     */
    public void testGetDeviceUsingServletHttpHeadersWithSuppliedDefault() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                final DeviceRepositoryFactory factory =
                    DeviceRepositoryFactory.getDefaultInstance();
                final URL deviceRepositoryUrl = file.toURL();
                final DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                final String defaultDeviceName = "Mobile";

                // Null headers mean we return null device, not the default device
                // (that's how it worked before introducing configurable default device,
                // so I'm preserving this behaviuor)
                Device device = repository.getDevice((HttpHeaders) null, defaultDeviceName);
                assertNull("No device should be found", device);

                // Empty headers cause default device to be used
                MutableHttpHeaders headers =
                    HTTP_HEADERS_FACTORY.createHTTPHeaders();
                device = repository.getDevice(headers, defaultDeviceName);
                assertNotNull("Valid device should be found", device);
                assertEquals("Device name should match", defaultDeviceName,
                    device.getName());

                // Default device should be ignoired if a valid device can be extracted from headers
                headers.addHeader("User-Agent", "Nokia3330/");
                device = repository.getDevice(headers, defaultDeviceName);
                assertNotNull("Valid device should be found", device);
                assertEquals("Device name should match", "Nokia-3330",
                    device.getName());
             }
        });

    }

    /**
     * Test getting a device using the UAProf.
     */
    public void testGetDeviceNameUsingUAProfURL() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                URL uaprofURL = null;
                String deviceName =
                    repository.getDeviceNameByUAProfURL(uaprofURL);
                assertNull("Device name should not have been found",
                        deviceName);

                uaprofURL = new URL("http", "host-not-defined-anywhere.com",
                        "/Handspring/HSTR300HK");
                deviceName = repository.getDeviceNameByUAProfURL(uaprofURL);
                assertNull("Device name should not have been found",
                        deviceName);

                uaprofURL = new URL("http", "device.sprintpcs.com", "/Handspring/HSTR300HK");
                deviceName = repository.getDeviceNameByUAProfURL(uaprofURL);

                assertEquals("Device name should match",
                        "SprintPCS-HSTR-300", deviceName);
            }
        });

    }

    /**
     * Test getting a device using HttpHeaders.
     */
    public void testGetDeviceUsingMutableHttpHeaders() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                MutableHttpHeaders mutableHeaders = HTTP_HEADERS_FACTORY.
                        createHTTPHeaders();

                Device device = repository.getDevice((HttpHeaders)null);
                assertNull("No device should be found", device);

                device = repository.getDevice(mutableHeaders);
                assertNotNull("Valid device should be found", device);
                assertEquals("Device name should match", "PC", device.getName());

                mutableHeaders.addHeader("user-agent", "Nokia3330/");
                device = repository.getDevice(mutableHeaders);
                assertNotNull("Valid device should be found", device);
                assertEquals("Name should match: ", "Nokia-3330", device.getName());
            }
        });

    }



    /**
     * Test that ensures that a cyclic secondary header reference does not 
     * occur. The WAP-Handset device in the test zip file points to itself.
     */
    public void testGetDeviceUsingMutableHttpHeadersDoesNotRecurse()
                throws Exception {

	
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {
		 DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

		MutableHttpHeaders mutableHeaders =
            HTTP_HEADERS_FACTORY.createHTTPHeaders();

		Device device = repository.getDevice((HttpHeaders)null);
		assertNull("No device should be found", device);
		
		device = repository.getDevice(mutableHeaders);
		assertNotNull("Valid device should be found", device);
		assertEquals("Device name should match", 
			     "PC", 
			     device.getName());
		
		mutableHeaders.addHeader("accept", "text/vnd.wap.wml");
		device = repository.getDevice(mutableHeaders);
		
		assertNotNull("Valid device should be found", device);
		
		assertEquals("Name should match: ", 
			     "WAP-Handset", 
			     device.getName());
	    }
        });
    }


    /**
     * Test the getting of the devices using a pattern name.
     */
    public void testGetDevices() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                List devices = repository.getDevices(null);
                assertNull("No devices should be found", devices);

                devices = repository.getDevices("*");
                assertNotNull("Valid device should be found", devices);
                assertEquals("Number of devices found", 51, devices.size());
            }
        });

    }

    /**
     * Test the getting of the device policy names.
     */
    public void testGetDevicePolicyNames() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();

                {
                    DeviceRepository repository = factory.getDeviceRepository(
                            deviceRepositoryUrl, null);

                    List list = repository.getDevicePolicyNames();
                    assertNotNull("Valid list should be found", list);
                    assertEquals("Expected 262 policy names", 262, list.size());
                }

                // Retest with access to experimental policies
                {
                    DeviceRepository repository =
                            getDeviceRepository(deviceRepositoryUrl, true);

                    List list = repository.getDevicePolicyNames();
                    assertNotNull("Valid list should be found", list);
                    assertEquals("Expected 263 policy names", 263, list.size());
                }
            }
        });

    }

    /**
     * Test the getting of the policy category names.
     */
    public void testGetPolicyCategoryNames() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();

                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                List list = repository.getPolicyCategoryNames();
                assertEquals(17, list.size());
                assertTrue(list.contains("system"));
                assertTrue(list.contains("misc"));
                assertTrue(list.contains("output"));
                assertTrue(list.contains("network"));
                assertTrue(list.contains("image"));
                assertTrue(list.contains("audio"));
                assertTrue(list.contains("browser"));
                assertTrue(list.contains("identification"));
                assertTrue(list.contains("ergonomics"));
                assertTrue(list.contains("dynvis"));
                assertTrue(list.contains("input"));
                assertTrue(list.contains("security"));
                assertTrue(list.contains("location"));
                assertTrue(list.contains("rules"));
                assertTrue(list.contains("protocol"));
                assertTrue(list.contains("message"));
                assertTrue(list.contains("custom"));
            }
        });
    }

    /**
     * Test the getting of the fallback device name.
     */
    public void testGetFallbackDeviceName() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();

                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                String deviceName = repository.getFallbackDeviceName("Master");
                assertNull(deviceName);

                deviceName = repository.getFallbackDeviceName("Voice");
                assertEquals("Master", deviceName);
            }
        });
    }

    /**
     * Test the getting of the children device names.
     */
    public void testGetChildrenDeviceNames() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();

                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                List children = repository.getChildrenDeviceNames(("Master"));
                assertEquals(6, children.size());
                assertTrue(children.contains("Voice"));
                assertTrue(children.contains("Internet-Appliance"));
                assertTrue(children.contains("Kiosk"));
                assertTrue(children.contains("Mobile"));
                assertTrue(children.contains("TV"));
                assertTrue(children.contains("PC"));

                children = repository.getChildrenDeviceNames("Nokia-6210");
                assertEquals(0, children.size());
            }
        });
    }

    /**
     * Test the getting of the device policy names by category.
     */
    public void testGetDevicePolicyNamesByCategory() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {
                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();

                {
                    DeviceRepository repository = factory.getDeviceRepository(
                            deviceRepositoryUrl, null);

                    List list = repository.getDevicePolicyNamesByCategory(null);
                    assertNull("No policy names should not be found", list);

                    list = repository.getDevicePolicyNamesByCategory("Not Found");
                    assertNotNull("Valid list should be found", list);
                    assertEquals("Expected no elements", 0, list.size());

                    list = repository.getDevicePolicyNamesByCategory("system");
                    assertNotNull("Valid list should be found", list);
                    assertEquals("Expected 25 policy names", 25, list.size());
                }

                // Re-test with access to experimental policies
                {
                    DeviceRepository repository =
                            getDeviceRepository(deviceRepositoryUrl, true);

                    List list = repository.getDevicePolicyNamesByCategory("system");
                    assertNotNull("Valid list should be found", list);
                    assertEquals("Expected 26 policy names", 26, list.size());
                }
            }
        });

    }

    /**
     * Test the getting of the device policy descriptors.
     */
    public void testGetPolicyDescriptor() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                PolicyDescriptor descriptor =
                        repository.getPolicyDescriptor(null, Locale.getDefault());
                assertNull("Policy descriptor should not be found", descriptor);

                // todo this code is commented cause the expected value should be null (currently a DeviceRepositoryException is thrown which is wrong).
//        descriptor = repository.getPolicyDescriptor("not here");
//        assertNull("Policy descriptor should not be found", descriptor);

                descriptor = repository.getPolicyDescriptor(
                        "output.charset.default", Locale.getDefault());
                assertNotNull("Policy descriptor should be found", descriptor);
                assertTrue("Type should match", descriptor instanceof DefaultPolicyDescriptor);
                assertEquals("Type should match", "Output charset (default)",
                        descriptor.getPolicyDescriptiveName());
                assertEquals("Category should match", "output",
                        descriptor.getCategoryName());
            }
        });
    }

    /**
     * Test the getting of the device policy descriptors.
     */
    public void testGetCategoryDescriptor() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                CategoryDescriptor descriptor =
                    repository.getCategoryDescriptor(null, Locale.getDefault());
                assertNull("Category descriptor should not be found", descriptor);

                // todo this code is commented cause the expected value should be null (currently a DeviceRepositoryException is thrown which is wrong).
//                descriptor = repository.getCategoryDescriptor(
//                        "xxxxxxxxx", Locale.getDefault());
//                assertNull("Category descriptor should not be found", descriptor);

                descriptor = repository.getCategoryDescriptor(
                        "protocol", Locale.getDefault());
                assertNotNull("Category descriptor should be found", descriptor);
            }
        });
    }

    /**
     * Test the getting of experimental device policy descriptors.
     */
    public void testGetExperimentalPolicyDescriptor() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                URL deviceRepositoryUrl = file.toURL();

                // Try getting the value from a device repository with the
                // experimental policies non-accessible
                {
                    DeviceRepository repository = getDeviceRepository(deviceRepositoryUrl, false);

                    PolicyDescriptor descriptor = repository.getPolicyDescriptor(
                            "x-experimental-policy", Locale.getDefault());
                    assertNull("Policy descriptor should not be found", descriptor);
                }

                // Try getting the value from a device repository with the
                // experimental policies accessible
                {
                    DeviceRepository repository = getDeviceRepository(deviceRepositoryUrl, true);

                    PolicyDescriptor descriptor = repository.getPolicyDescriptor(
                            "x-experimental-policy", Locale.getDefault());
                    assertNotNull("Policy descriptor should be found", descriptor);
                }
            }
        });

    }

    /**
     * Gets an XML device repository for a specified repository URL and
     * with specified visibility of experimental policies
     *
     * @param repositoryUrl The URL of the device repository
     * @param accessExperimental True if experimental policies should be visible
     * @return A device repository as specified
     * @throws DeviceRepositoryException if an error occurs creating the repository
     */
    public DeviceRepository getDeviceRepository(URL repositoryUrl,
                                                boolean accessExperimental)
            throws DeviceRepositoryException {
        try {
            // Create the XML repository
            // Note that we can only handle file: urls.
            XMLRepositoryFactory factory =
                    XMLRepositoryFactory.getDefaultInstance();
            LocalRepository repository = factory.createXMLRepository(null);

            DeviceRepositoryLocation location =
                    new DeviceRepositoryLocationImpl(repositoryUrl.getFile());

            // Create the XML accessor.
            DeviceRepositoryAccessor accessor =
                REPOSITORY_ACCESSOR_FACTORY.createDeviceRepositoryAccessor(
                    repository,  location, null);

            // Return the default device repository impl using the XML
            // repository and accessor we just created.
            return new DefaultDeviceRepository(repository, accessor,
                    accessExperimental);
        } catch (RepositoryException e) {
            throw new DeviceRepositoryException(e);
        }
    }

    /**
     * This tests the enumerate device names method based on a pattern
     * match.  This currently just uses an xml repository to test this.
     * The implementation of this method is entirely within
     * {@link com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor} and
     * uses the {@link com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor#enumerateDeviceNames}
     * to obtain the list against which the pattern is then matched.  This
     * test fully tests that method as JDBC failures would come from problems
     * with the method mentioned above.
     */
    public void testEnumerateDeviceNames() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                // Test match pattern*
                String pattern = "Nokia-3*";
                List actual = repository.getDevices(pattern);

                // Create expected values based on the pattern
                String[] expected = new String[] {
//                    "Nokia-3280",
//                    "Nokia-3589i",
                    "Nokia-3350",
                    "Nokia-3410",
//                    "Nokia-3510",
//                    "Nokia-3510_m",
//                    "Nokia-3510i",
//                    "Nokia-3510i_m",
//                    "Nokia-3530",
//                    "Nokia-3530_m",
//                    "Nokia-3585i",
//                    "Nokia-3610",
                    "Nokia-3330",
                    "Nokia-3360",
//                    "Nokia-3620",
//                    "Nokia-3620_m",
//                    "Nokia-3650",
//                    "Nokia-3650-Opera6",
//                    "Nokia-3650_m",
//                    "Nokia-3660",
//                    "Nokia-3660_m",
//                    "Nokia-3600",
//                    "Nokia-3600_m",
//                    "Nokia-3100",
//                    "Nokia-3100_m",
//                    "Nokia-3108",
//                    "Nokia-3108_m",
//                    "Nokia-3200",
//                    "Nokia-3200_m",
//                    "Nokia-3300",
//                    "Nokia-3300-1",
//                    "Nokia-3300-2",
//                    "Nokia-3300_m",
//                    "Nokia-3560",
//                    "Nokia-3590",
//                    "Nokia-3590_m",
//                    "Nokia-3595",
//                    "Nokia-3595_m",
//                    "Nokia-3520"
                };

                // Check success of test
                assertNotNull("Should be some devices (1)", actual);
                // NOTE: convert to set so order is not relied on.
                assertEquals("Actual and expected arrays should match (1)",
                            Arrays.asList(expected), actual);

                // Test match *pattern
                pattern = "*400";
                actual = repository.getDevices(pattern);

                // Create expected values based on the pattern
                expected = new String[] {
//                    "LG-G5400",
                    "Samsung-SPH-A400",
//                    "Samsung-SGH-T400",
//                    "Samsung-SGH-P400",
                    "Samsung-SGH-A400",
                    "Samsung-SGH-N400",
//                    "Sanyo-SCP-5400",
//                    "Sanyo-SCP-6400",
//                    "Motorola-V400",
//                    "Samsung-SPH-N400"
                };

                // Check success of test
                assertNotNull("Should be some devices (2)", actual);
                // NOTE: convert to set so order is not relied on.
                assertEquals("Actual and expected arrays should match (2)",
                           Arrays.asList(expected), actual);

                // Test match pattern (no wildcards)
                pattern = "Master";
                actual = repository.getDevices(pattern);

                // Create expected values based on the pattern
                expected = new String[] {
                    "Master"
                };

                // Check success of test
                assertNotNull("Should be some devices (3)", actual);
                assertEquals("Actual and expected arrays should match (3)",
                        Arrays.asList(expected), actual);
            }
        });
    }

    /**
     * Test the getting of the device policy descriptors.
     */
    public void testGetRealPolicyValue() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                DeviceRepositoryFactory factory =
                            DeviceRepositoryFactory.getDefaultInstance();
                URL deviceRepositoryUrl = file.toURL();
                DeviceRepository repository = factory.getDeviceRepository(
                        deviceRepositoryUrl, null);

                String deviceName = "Master";

                Device device = repository.getDevice(deviceName);
                assertEquals("Name should match: ", deviceName, device.getName());

                PolicyValue value = device.getRealPolicyValue("realvidinpage");
                assertNotNull(value);
            }
        });
    }
}
