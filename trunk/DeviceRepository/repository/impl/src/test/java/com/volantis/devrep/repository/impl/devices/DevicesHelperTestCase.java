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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.devices;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DeviceIdentificationResult;
import com.volantis.devrep.repository.api.devices.DevicesHelper;
import com.volantis.devrep.repository.api.devices.DefaultDeviceMock;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorMock;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.http.HttpFactory;
import com.volantis.mcs.http.MutableHttpHeaders;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.LocalRepositoryMock;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.xml.XMLRepositoryConnectionMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;

/**
 * Tests {@link DevicesHelper}.
 */
public class DevicesHelperTestCase extends TestCaseAbstract {

    private MutableHttpHeaders headers;
    private MockDeviceRepositoryAccessor accessor;
    private XMLRepositoryConnectionMock connectionMock;

    public void setUp() throws Exception {
        super.setUp();
        connectionMock = new XMLRepositoryConnectionMock(
                "connectionMock", expectations);
        final LocalRepository localRepositoryMock =
            new LocalRepositoryMock("localRepositoryMock", expectations);
        connectionMock.expects.getLocalRepository().returns(localRepositoryMock).any();
        headers = HttpFactory.getDefaultInstance().createHTTPHeaders();
        accessor = new MockDeviceRepositoryAccessor(connectionMock.getLocalRepository()) {
            /**
             * Helper method. If the deviceName supplied is equal to one of the
             * default device names (WAP-Handset, PC, Custom), then it wraps them in an
             * InternalDevice, otherwise it returns null.
             *
             * @param connection Not used in this helper method
             * @param deviceName String
             * @return InternalDevice or null if deviceName did not match either
             *         default device name
             */
            public DefaultDevice getDeviceFallbackChain(
                    RepositoryConnection connection,
                    String deviceName) {
                DefaultDevice device = null;
                if ("PC".equalsIgnoreCase(deviceName) ||
                        "WAP-Handset".equalsIgnoreCase(deviceName) ||
                        "Custom".equalsIgnoreCase(deviceName)) {
                    device = new DefaultDevice(deviceName, new HashMap(),
                        new PolicyValueFactory() {
                            public PolicyValue createPolicyValue(
                                DefaultDevice device, String policyName) {
                                return null;
                            }
                        });
                }
                return device;
            }
        };
    }

    public void tearDown() {
        connectionMock = null;
        headers = null;
        accessor = null;
    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there is one accept header with one value which
     * matches the required mimeType, then the default device type of
     * WAP-Handset is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithMatchingSingleAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "wap.wml");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("WAP-Handset", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there is one accept header with one value which
     * doesn't match the required mimeType, then the default device type of
     * PC is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithNonMatchingSingleAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "text/html");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("PC", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there is one accept header with one value which
     * is a superstring of the required mimeType, then the default device type
     * of WAP-Handset is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithNearMatchingSingleAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "application/vnd.wap.wml");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("WAP-Handset", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there are multiple accept headers, one of which
     * matches the required mimeType, then the default device type of
     * WAP-Handset is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithMatchingMultipleAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "wap.wml");
        headers.addHeader("accept", "text/html");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("WAP-Handset", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there are multiple accept headers, none of which
     * match the required mimeType, then the default device type of PC is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithNonMatchingMultipleAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "text/html");
        headers.addHeader("accept", "application/xml");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("PC", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there are multiple accept headers, one of which
     * is a superstring of the required mimeType, then the default device type
     * of WAP-Handset is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithNearMatchingMultipleAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "application/vnd.wap.wml");
        headers.addHeader("accept", "text/html");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("WAP-Handset", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there is one accept header with comma separated
     * values, one of which matches the required mimeType, then the default
     * device type of WAP-Handset is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithMatchingCombinedAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "wap.wml, text/html");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("WAP-Handset", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there is one accept header with comma separated
     * values, none of which matches the required mimeType, then the default
     * device type of PC is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithNonMatchingCombinedAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "text/html, application/xml");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("PC", device.getName());

    }

    /**
     * Tests that if the device name cannot be found from the user agent or
     * profile headers, and there is one accept header with comma separated
     * values, one of which is a superstring of the required mimeType, then
     * the default device type of WAP-Handset is used.
     *
     * @throws RepositoryException if the device cannot be found
     */
    public void testGetDeviceWithNearMatchingCombinedAcceptHeader()
            throws RepositoryException {

        headers.addHeader("accept", "application/vnd.wap.wml, application/xml");

        Device device = DevicesHelper.getDevice(
            connectionMock, headers, accessor, null).getDevice();

        assertEquals("WAP-Handset", device.getName());

    }

    public void testGetDeviceWithSuppliedDefault() throws RepositoryException {

        String defaultDeviceName = "Custom";
        headers.addHeader("accept", "text/html, application/xml");

        Device device = DevicesHelper.getDevice(
             connectionMock, headers, accessor, null, defaultDeviceName)
                .getDevice();

         assertEquals(defaultDeviceName, device.getName());
    }

    /**
     * Tests {@link DevicesHelper#getDeviceName}.
     */
    public void testGetDeviceName() throws Exception {
        // A fake accessor that allows this test to operate
        accessor = new MockDeviceRepositoryAccessor(connectionMock.getLocalRepository()) {
            // javadoc inherited
            public String retrieveMatchingDeviceName(String userAgent) {
                return userAgent;
            }
        };

        assertEquals("Failed pattern generation (1)",
                     DevicesHelper.UAPROF_PREFIX.toLowerCase() + ": value MyDev",
                     DevicesHelper.getDeviceName(accessor,
                                                 DevicesHelper.UAPROF_PREFIX,
                                                 "value",
                                                 "MyDev"));
        assertEquals("Failed pattern generation (2)",
                     DevicesHelper.UAPROF_PREFIX.toLowerCase() + ": value",
                     DevicesHelper.getDeviceName(accessor,
                                                 DevicesHelper.X_WAP_PROFILE,
                                                 "value",
                                                 null));
        assertEquals("Failed pattern generation (3)",
                     "some header: value",
                     DevicesHelper.getDeviceName(accessor,
                                                 "SoMe HeaDer",
                                                 "value",
                                                 null));
    }

    /**
     * Tests if NN-profile secondary headers are handled correctly.
     */
    public void testNNProfile() throws RepositoryException {
        final DeviceRepositoryAccessorMock accessorMock =
            new DeviceRepositoryAccessorMock("accessorMock", expectations);
        final DefaultDeviceMock deviceMock1 =
            new DefaultDeviceMock("deviceMock1", expectations);
        final DefaultDeviceMock deviceMock2 =
            new DefaultDeviceMock("deviceMock2", expectations);

        // expectations to return the main device
        accessorMock.expects.
            retrieveMatchingDeviceName("ua string").returns("device name").any();
        accessorMock.expects.getDeviceFallbackChain(
            connectionMock, "device name").returns(deviceMock1).any();

        // expectation to use NN-profile as a secondary header name
        deviceMock1.expects.getSecondaryIDHeaderName().returns("NN-profile").any();
        deviceMock1.expects.getSecondaryDevice("http://test.com").returns(null).any();
        deviceMock1.expects.getName().returns("device name").any();

        // expectation to return the secondary device for the profile URL
        accessorMock.expects.
            retrieveMatchingDeviceName("profile: http://test.com device name").
                returns("secondary device name").any();
        accessorMock.expects.getDeviceFallbackChain(
            connectionMock, "secondary device name").returns(deviceMock2).any();

        // the secondary device doesn't have secondary header
        deviceMock2.expects.getSecondaryIDHeaderName().returns(null).any();
        deviceMock2.expects.getName().returns("secondary device name").any();

        // adding deviceMock2 to deviceMock1 as cached secondary device
        deviceMock1.expects.addSecondaryDevice("http://test.com", deviceMock2);

        // for unknown devices logging
        deviceMock2.expects.getComputedPolicyValue("entrytype").returns("real_device");

        // headers
        // initial user agent string
        headers.addHeader("User-Agent", "ua string");
        // NN-profile header
        headers.addHeader("Opt",
            "http://www.w3.org/1999/06/24-CCPPexchange;ns=13");
        headers.addHeader("13-profile", "http://test.com");

        final DeviceIdentificationResult identificationResult =
            DevicesHelper.getDevice(connectionMock, headers, accessorMock, null);
        final Device device = identificationResult.getDevice();

        // right device is returned
        assertEquals("secondary device name", device.getName());
        // identificationResult.getHeaderNamesUsed() contains the headers used
        // for NN-profile check
        assertTrue(
            identificationResult.getHeaderNamesUsed().contains("13-profile"));
        assertTrue(
            identificationResult.getHeaderNamesUsed().contains("Opt"));
    }

    /**
     * Tests if NN-profile secondary headers are handled correctly when there is
     * no Opt header in the request.
     */
    public void testNNProfileNoOpt() throws RepositoryException {
        final DeviceRepositoryAccessorMock accessorMock =
            new DeviceRepositoryAccessorMock("accessorMock", expectations);
        final DefaultDeviceMock deviceMock1 =
            new DefaultDeviceMock("deviceMock1", expectations);
        final DefaultDeviceMock deviceMock2 =
            new DefaultDeviceMock("deviceMock2", expectations);

        // expectations to return the main device
        accessorMock.expects.
            retrieveMatchingDeviceName("ua string").returns("device name").any();
        accessorMock.expects.getDeviceFallbackChain(
            connectionMock, "device name").returns(deviceMock1).any();

        // expectation to use NN-profile as a secondary header name
        deviceMock1.expects.getSecondaryIDHeaderName().returns("NN-profile").any();
        deviceMock1.expects.getSecondaryDevice("http://test.com").returns(null).any();
        deviceMock1.expects.getName().returns("device name").any();

        // expectation to return the secondary device for the profile URL
        accessorMock.expects.
            retrieveMatchingDeviceName("profile: http://test.com device name").
                returns("secondary device name").any();
        accessorMock.expects.getDeviceFallbackChain(
            connectionMock, "secondary device name").returns(deviceMock2).any();

        // the secondary device doesn't have secondary header
        deviceMock2.expects.getSecondaryIDHeaderName().returns(null).any();
        deviceMock2.expects.getName().returns("secondary device name").any();

        // adding deviceMock2 to deviceMock1 as cached secondary device
        deviceMock1.expects.addSecondaryDevice("http://test.com", deviceMock2);

        // for unknown devices logging
        deviceMock2.expects.getComputedPolicyValue("entrytype").returns("real_device");

        // headers
        // initial user agent string
        headers.addHeader("User-Agent", "ua string");
        // NN-profile header
        headers.addHeader("13-profile", "http://test.com");

        final DeviceIdentificationResult identificationResult =
            DevicesHelper.getDevice(connectionMock, headers, accessorMock, null);
        final Device device = identificationResult.getDevice();

        // right device is returned
        assertEquals("secondary device name", device.getName());
        // identificationResult.getHeaderNamesUsed() contains the headers used
        // for NN-profile check
        assertTrue(
            identificationResult.getHeaderNamesUsed().contains("13-profile"));
        assertTrue(
            identificationResult.getHeaderNamesUsed().contains("Opt"));
    }

    /**
     * Tests if NN-profile secondary headers are handled correctly when the
     * request contains more than one Opt header. (The list of headers used must
     * not depend on the order of the headers in the request.)
     */
    public void testNNProfileMultipleOpt() throws RepositoryException {
        final DeviceRepositoryAccessorMock accessorMock =
            new DeviceRepositoryAccessorMock("accessorMock", expectations);
        final DefaultDeviceMock deviceMock1 =
            new DefaultDeviceMock("deviceMock1", expectations);
        final DefaultDeviceMock deviceMock2a =
            new DefaultDeviceMock("deviceMock2a", expectations);
        final DefaultDeviceMock deviceMock2b =
            new DefaultDeviceMock("deviceMock2b", expectations);

        // expectations to return the main device
        accessorMock.expects.
            retrieveMatchingDeviceName("ua string").returns("device name").any();
        accessorMock.expects.getDeviceFallbackChain(
            connectionMock, "device name").returns(deviceMock1).any();

        // expectation to use NN-profile as a secondary header name
        deviceMock1.expects.
            getSecondaryIDHeaderName().returns("NN-profile").any();
        deviceMock1.expects.getName().returns("device name").any();

        // expectation to return the secondary device for the profile URL
        accessorMock.expects.
            retrieveMatchingDeviceName("profile: http://test13.com device name").
                returns("secondary device name 1").any();
        accessorMock.expects.getDeviceFallbackChain(
            connectionMock, "secondary device name 1").returns(deviceMock2a).any();
        accessorMock.expects.
            retrieveMatchingDeviceName("profile: http://test19.com device name").
                returns("secondary device name 2").any();
        accessorMock.expects.getDeviceFallbackChain(
            connectionMock, "secondary device name 2").returns(deviceMock2b).any();

        // expectation to return the cached secondary device
        deviceMock1.expects.
            getSecondaryDevice("http://test13.com").returns(deviceMock2a).any();
        deviceMock1.expects.
            getSecondaryDevice("http://test19.com").returns(deviceMock2b).any();

        // the secondary device doesn't have secondary header
        deviceMock2a.expects.getSecondaryIDHeaderName().returns(null).any();
        deviceMock2a.expects.getName().returns("secondary device name 1").any();
        deviceMock2b.expects.getSecondaryIDHeaderName().returns(null).any();
        deviceMock2b.expects.getName().returns("secondary device name 2").any();

        // for unknown devices logging
        deviceMock2a.expects.
            getComputedPolicyValue("entrytype").returns("real_device").any();
        deviceMock2b.expects.
            getComputedPolicyValue("entrytype").returns("real_device").any();

        // headers
        // initial user agent string
        headers.addHeader("User-Agent", "ua string");
        // NN-profile header
        headers.addHeader("Opt",
            "http://www.w3.org/1999/06/24-CCPPexchange;ns=13");
        headers.addHeader("Opt",
            "http://www.w3.org/1999/06/24-CCPPexchange;ns=19");
        headers.addHeader("13-profile", "http://test13.com");
        headers.addHeader("19-profile", "http://test19.com");

        final DeviceIdentificationResult identificationResult =
            DevicesHelper.getDevice(connectionMock, headers, accessorMock, null);
        final Device device = identificationResult.getDevice();

        // it doesn't matter which device is returned
        assertTrue(device == deviceMock2a || device == deviceMock2b);
        // identificationResult.getHeaderNamesUsed() contains the headers used
        // for NN-profile check
        final Collection headerNamesUsed =
            identificationResult.getHeaderNamesUsed();
        assertTrue(headerNamesUsed.contains("Opt"));
        // must contain at least one *-profile header
        String profileHeaderName = null;
        for (Iterator iter = headerNamesUsed.iterator(); iter.hasNext(); ) {
            final String headerName = (String) iter.next();
            if (headerName.endsWith("-profile")) {
                profileHeaderName = headerName;
            }
        }
        assertNotNull(profileHeaderName);
        assertTrue(profileHeaderName.equals("13-profile") ||
            profileHeaderName.equals("19-profile"));

        //now try it again with different Opt header order
        headers = HttpFactory.getDefaultInstance().createHTTPHeaders();
        // initial user agent string
        headers.addHeader("User-Agent", "ua string");
        // NN-profile header
        headers.addHeader("Opt",
            "http://www.w3.org/1999/06/24-CCPPexchange;ns=19");
        headers.addHeader("Opt",
            "http://www.w3.org/1999/06/24-CCPPexchange;ns=13");
        headers.addHeader("13-profile", "http://test13.com");
        headers.addHeader("19-profile", "http://test19.com");

        final DeviceIdentificationResult identificationResult2 =
            DevicesHelper.getDevice(connectionMock, headers, accessorMock, null);
        // identified the same device
        assertTrue(device.getName().equals(
            identificationResult2.getDevice().getName()));
        // used the same headers to identify the device
        assertEquals(
            headerNamesUsed, identificationResult2.getHeaderNamesUsed());
    }

    /**
     * No *-profile header.
     */
    public void testGetProfileHeaderNamesNoOpt1() {
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(0, names.size());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * *-profile header, but no namespace declaration.
     * If there is no Opt header, we have to use the NN-profile headers as maybe
     * the device doesn't follow the specification.
     */
    public void testGetProfileHeaderNamesNoOpt2() {
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("134-profile", "http://www.profile.url.com/");
        headers.addHeader("AB-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(1, names.size());
        assertEquals("13-profile", names.first());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * *-profile and Opt header, but wrong namespace in the Opt header
     * If there is no valid CC/PP Opt header, we have to use the NN-profile
     * headers as maybe the device doesn't follow the specification correctly.
     */
    public void testGetProfileHeaderNamesInvalidNamespace() {
        headers.addHeader("Opt", "\"http://www.example.com\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("134-profile", "http://www.profile.url.com/");
        headers.addHeader("AB-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(1, names.size());
        assertEquals("13-profile", names.first());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Invalid namespace ID, namespace ID must consist of two digits.
     */
    public void testGetProfileHeaderNamesInvalidNamespaceID1() {
        headers.addHeader("Opt", "\"http://www.example.com\";ns=AB");
        headers.addHeader("AB-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(0, names.size());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * TWO digits...
     */
    public void testGetProfileHeaderNamesInvalidNamespaceID2() {
        headers.addHeader("Opt", "\"http://www.example.com\";ns=123");
        headers.addHeader("123-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(0, names.size());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Namespace ID is missing from the declaration.
     * If there is no valid CC/PP Opt header, we have to use the NN-profile
     * headers as maybe the device doesn't follow the specification correctly.
     */
    public void testGetProfileHeaderNamesMissingNamespaceID() {
        headers.addHeader("Opt", "\"http://www.example.com\"");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("134-profile", "http://www.profile.url.com/");
        headers.addHeader("AB-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(1, names.size());
        assertEquals("13-profile", names.first());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Normal case, one matching *-profile header.
     */
    public void testGetProfileHeaderNamesOK1() {
        headers.addHeader("Opt",
            "http://www.w3.org/1999/06/24-CCPPexchange;ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(1, names.size());
        assertEquals("13-profile", names.first());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Normal case, CC/PP Exchange namespace between quotes.
     */
    public void testGetProfileHeaderNamesOK2() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(1, names.size());
        assertEquals("13-profile", names.first());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Normal case, 2 matching *-profile headers.
     */
    public void testGetProfileHeaderNamesDuplicateProfileHeaders() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("13-profile", "http://www.other.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(1, names.size());
        assertEquals("13-profile", names.first());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Multiple *-profile headers, but not all of them have a matching namespace
     * declaration.
     */
    public void testGetProfileHeaderNamesMultipleProfileHeaders() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("42-profile", "http://www.other.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(1, names.size());
        assertEquals("13-profile", names.first());
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Multiple *-profile headers, multiple namespace declaration.
     */
    public void testGetProfileHeaderNamesMultipleNamespacesOK() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=42");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("42-profile", "http://www.other.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(2, names.size());
        assertTrue(names.contains("13-profile"));
        assertTrue(names.contains("42-profile"));
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Multiple namespace declarations, but not all of them have matching
     * *-profile headers.
     */
    public void testGetProfileHeaderNamesMultipleNamespacesMissing() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=42");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        final Set headersUsed = new HashSet();
        final SortedSet names =
            DevicesHelper.getProfileHeaderNames(headers, headersUsed);
        assertEquals(2, names.size());
        assertTrue(names.contains("13-profile"));
        assertTrue(names.contains("42-profile"));
        assertEquals(1, headersUsed.size());
        assertEquals("Opt", headersUsed.iterator().next());
    }

    /**
     * Namespace declaration, but no *-profile header.
     */
    public void testGetProfileUrlsMissingOpt() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        final List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(0, urls.size());
    }

    /**
     * *- profile header without namespace declaration (namespace declaration is
     * checked in getProfileHeaderNames, so it is OK not to have a namespace
     * declaration here.
     */
    public void testGetProfileUrlsMissingProfileHeader() {
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        final List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(1, urls.size());
        assertEquals("http://www.profile.url.com/", urls.get(0));
    }

    /**
     * Normal case.
     */
    public void testGetProfileUrlsOK() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        final List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(1, urls.size());
        assertEquals("http://www.profile.url.com/", urls.get(0));
    }

    /**
     * No *-profile header.
     */
    public void testGetProfileUrlsMissingProfile() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        final List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(0, urls.size());
    }

    public void testGetProfileUrlsDuplicateProfileHeaders() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("13-profile", "http://www.other.profile.url.com/");
        final List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(2, urls.size());
        assertEquals("http://www.other.profile.url.com/", urls.get(0));
        assertEquals("http://www.profile.url.com/", urls.get(1));
    }

    /**
     * Duplicate *-profile headers, URL's should be returned in reverse order.
     */
    public void testGetProfileUrlsDuplicateProfileHeadersWithOrderCheck1() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("13-profile", "http://www.other.profile.url.com/");
        final List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(2, urls.size());
        assertEquals("http://www.other.profile.url.com/", urls.get(0));
        assertEquals("http://www.profile.url.com/", urls.get(1));
    }

    /**
     * Multiple URL's in header value, URL's should be returned in reverse
     * order.
     */
    public void testGetProfileUrlsDuplicateProfileHeadersWithOrderCheck2() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "\"http://www.profile.url.com/\"," +
            "\"http://www.other.profile.url.com/\"");
        final List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(2, urls.size());
        assertEquals("http://www.other.profile.url.com/", urls.get(0));
        assertEquals("http://www.profile.url.com/", urls.get(1));
    }

    /**
     * Multiple *-profile headers, some without namespace declaration, but here
     * we don't care about it.
     */
    public void testGetProfileUrlsMultipleProfileHeaders() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("13-profile", "http://www.profile.url.com/");
        headers.addHeader("13-profile", "http://www.other.profile.url.com/");
        headers.addHeader("42-profile", "http://www.third.profile.url.com/");

        List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(2, urls.size());
        assertEquals("http://www.other.profile.url.com/", urls.get(0));
        assertEquals("http://www.profile.url.com/", urls.get(1));

        urls = DevicesHelper.getProfileUrls(headers, "42-profile");
        assertEquals(1, urls.size());
        assertEquals("http://www.third.profile.url.com/", urls.get(0));
    }

    /**
     * Multiple *-profile headers, one with profile-diff-name value.
     */
    public void testGetProfileUrlsMultipleProfileHeaderValues() {
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=13");
        headers.addHeader("Opt",
            "\"http://www.w3.org/1999/06/24-CCPPexchange\";ns=42");
        headers.addHeader("13-profile", "\"http://www.profile.url.com/\"," +
            "\"http://www.other.profile.url.com/\"");
        headers.addHeader("42-profile", "\"http://www.abc.com/\", " +
            "\"1-P1GRkSjKK50aTWXXndFcSQ==\", \"http://www.def.com/\"");

        List urls = DevicesHelper.getProfileUrls(headers, "13-profile");
        assertEquals(2, urls.size());
        assertEquals("http://www.other.profile.url.com/", urls.get(0));
        assertEquals("http://www.profile.url.com/", urls.get(1));

        urls = DevicesHelper.getProfileUrls(headers, "42-profile");
        assertEquals(2, urls.size());
        assertEquals("http://www.def.com/", urls.get(0));
        assertEquals("http://www.abc.com/", urls.get(1));
    }

    // A fake accessor that allows this test to operate
    class MockDeviceRepositoryAccessor extends AbstractDeviceRepositoryAccessor {

        public MockDeviceRepositoryAccessor(final LocalRepository repository) {
            super(repository);
        }

        // javadoc inherited
        public String retrieveMatchingDeviceName(String userAgent) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public RepositoryEnumeration enumerateDevicePatterns(
                RepositoryConnection connection) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        protected void addDeviceImpl(
                RepositoryConnection connection,
                DefaultDevice device)
                throws RepositoryException {
        }

        // javadoc inherited
        protected void removeDeviceImpl(RepositoryConnection connection,
                                        String deviceName)
                throws RepositoryException {
        }

        // javadoc inherited
        protected DefaultDevice retrieveDeviceImpl(
                RepositoryConnection connection,
                String deviceName) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        protected RepositoryEnumeration enumerateDevicesChildrenImpl(
                RepositoryConnection connection,
                String deviceName) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        protected void renameDeviceImpl(RepositoryConnection connection,
                                        String deviceName,
                                        String newName)
                throws RepositoryException {
        }

        // javadoc inherited
        public void removePolicy(RepositoryConnection connection,
                                 String policyName) throws RepositoryException {
        }

        // javadoc inherited
        public RepositoryEnumeration enumerateDeviceNames(
                RepositoryConnection connection) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public RepositoryEnumeration enumerateDeviceFallbacks(
                RepositoryConnection connection) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public RepositoryEnumeration enumerateDeviceTACs(
                RepositoryConnection connection) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public void updatePolicyName(RepositoryConnection connection,
                                     String oldPolicyName,
                                     String newPolicyName)
                throws RepositoryException {
        }

        // javadoc inherited
        public RepositoryEnumeration enumeratePolicyNames(
                RepositoryConnection connection) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public RepositoryEnumeration enumeratePolicyNames(
                RepositoryConnection connection,
                String categoryName) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public PolicyDescriptor retrievePolicyDescriptor(
                RepositoryConnection connection, String policyName,
                Locale locale) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public CategoryDescriptor retrieveCategoryDescriptor(
                RepositoryConnection connection, String categoryName,
                Locale locale) throws RepositoryException {
            return null;
        }

        // javadoc inherited
        public void addPolicyDescriptor(
                RepositoryConnection connection,
                String policyName,
                PolicyDescriptor descriptor)
                throws RepositoryException {
        }

        // javadoc inherited
        public void addCategoryDescriptor(
                RepositoryConnection connection,
                String categoryName,
                CategoryDescriptor descriptor)
                throws RepositoryException {
        }

        // javadoc inherited
        public void removePolicyDescriptor(
                RepositoryConnection connection,
                String policyName)
                throws RepositoryException {
        }

        public void removeAllPolicyDescriptors(RepositoryConnection connection)
                throws RepositoryException {
        }

        // javadoc inherited
        public void removeCategoryDescriptor(
                RepositoryConnection connection,
                String categoryName)
                throws RepositoryException {
        }

        public void removeAllCategoryDescriptors(RepositoryConnection connection)
                throws RepositoryException {
        }

        // javadoc inherited
        public RepositoryEnumeration enumerateCategoryNames(
                RepositoryConnection connection) throws RepositoryException {
            return null;
        }

        public List retrievePolicyDescriptors(RepositoryConnection connection,
                                              String policyName)
                throws RepositoryException {
            return null;
        }

        public List retrieveCategoryDescriptors(RepositoryConnection connection,
                                                String categoryName)
                throws RepositoryException {
            return null;
        }
    }
}
