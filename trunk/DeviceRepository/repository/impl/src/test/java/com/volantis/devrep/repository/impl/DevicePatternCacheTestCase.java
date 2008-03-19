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
package com.volantis.devrep.repository.impl;

import com.volantis.devrep.repository.impl.accessors.DevicePatternCache;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * TestCase for DevicePatternCache.
 */
public class DevicePatternCacheTestCase extends TestCaseAbstract {

    /**
     * Constant for the name used to classify a generic i-mode device.
     */
    private static final String GENERAL_I_MODE_DEVICE = "i-mode-Handset";

    /**
     * The device pattern cache being tested.
     */
    private DevicePatternCache dpc;

    public void setUp() {
        dpc = new DevicePatternCache();
    }

    public void tearDown() {
        dpc = null;
    }

    public void testMapPatternWith10CharsGreaterThanDeviceNameLength() {

        String devicePattern = "Nokia8210821020";
        String deviceName = "Nokia";
        dpc.mapPatternAndDevice(devicePattern, deviceName);
        assertUserAgentMatchesDevice(devicePattern, deviceName);
    }

    public void testMapPatternWith4CharsGreaterThanDeviceNameLength() {

       String devicePattern = "Nokia8210";
       String deviceName = "Nokia";
       dpc.mapPatternAndDevice(devicePattern, deviceName);

       assertUserAgentMatchesDevice(devicePattern, deviceName);
    }

    public void testMapPatternWith3CharsGreaterThanDeviceNameLength() {

       String devicePattern = "Nokia821";
       String deviceName = "Nokia";
       dpc.mapPatternAndDevice(devicePattern, deviceName);

       assertUserAgentMatchesDevice(devicePattern,
                                                     deviceName);
    }

    public void testMapPatternWith2CharsGreaterThanDeviceNameLength() {

       String devicePattern = "Nokia82";
       String deviceName = "Nokia";
       dpc.mapPatternAndDevice(devicePattern, deviceName);

       assertUserAgentMatchesDevice(devicePattern, deviceName);
    }

    /**
     * This test case was added to simulate the bug
     * (index out of bounds Exception) reported in VBM 2005011201.
     */
    public void testMapPatternWith1CharGreaterThanDeviceNameLength() {

       String devicePattern = "Nokia8";
       String deviceName = "Nokia";
       dpc.mapPatternAndDevice(devicePattern, deviceName);

       assertUserAgentMatchesDevice(devicePattern, deviceName);
    }

    public void testMappingAndRetrievalForDoCoMoN901iC() {

        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-N901iC";
        String userAgentPattern = "DoCoMo/2\\.0 N901iC.*";
        String userAgentString = "DoCoMo/2.0 N901iCxxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    public void testMappingAndRetrievalForDoCoMo900() {

        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-F900i";
        String userAgentPattern = "DoCoMo/2.0 F900i.*";
        String userAgentString = "DoCoMo/2.0 F900ixxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    public void testMappingAndRetrievalForDoCoMoN253i() {

        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-N253i";
        String userAgentPattern = "DoCoMo/1\\.0/N253i.*";
        String userAgentString = "DoCoMo/1.0/N253ixxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    public void testMappingAndRetrievalForDoCoMoF880iES() {
        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-F880iES";
        String userAgentPattern = "DoCoMo/2\\.0 F880iES.*";
        String userAgentString = "DoCoMo/2.0 F880iESxxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    public void testMappingAndRetrievalOfDoCoMoP700i() {
        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-P700i";
        String userAgentPattern = "DoCoMo/2\\.0 P700i.*";
        String userAgentString = "DoCoMo/2.0 P700ixxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    /**
     * Ensure that mapping and retrieval works properly when
     * all of the characters between the start of the useragent string, e.g.
     * DoCoMo/ and the start of device specific information, e.g. P700i,
     * are escaped full stops.
     */
    public void testMappingAndRetrievalEdgeCase() {
        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-P700i";
        String userAgentPattern = "DoCoMo/\\.\\.\\.\\.P700i.*";
        String userAgentString = "DoCoMo/....P700ixxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    /**
     * Ensure that mapping and retrieval works properly when
     * all of the characters between the start of the useragent string, e.g.
     * DoCoMo/ and the start of device specific information, e.g. P700i,
     * are escaped characters (mixture of * and .).
     */
    public void testMappingAndRetrievalEdgeCase2() {
        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-P700i";
        String userAgentPattern = "DoCoMo/\\*\\*\\.\\.P700i.*";
        String userAgentString = "DoCoMo/**..P700ixxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    /**
     * Ensure that mapping and retrieval works properly when
     * all of the characters between the start of the useragent string, e.g.
     * DoCoMo/ and the start of device specific information, e.g. P700i,
     * are escaped characters (mixture of * and .).
     */
    public void testMappingAndRetrievalEdgeCase3() {
        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-P700i";
        String userAgentPattern = "DoCoMo/\\*\\*\\*\\.P700i.*";
        String userAgentString = "DoCoMo/***.P700ixxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    /**
     * Ensure that mapping and retrieval works properly when
     * all of the characters between the start of the useragent string, e.g.
     * DoCoMo/ and the start of device specific information, e.g. P700i,
     * are escaped *.
     */
    public void testMappingAndRetrievalEdgeCase4() {
        initialiseDoCoMoDeviceCache();
        String deviceName = "DoCoMo-P700i";
        String userAgentPattern = "DoCoMo/\\*\\*\\*\\*P700i.*";
        String userAgentString = "DoCoMo/****P700ixxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    public void testMappingAndRetrievalForNokia7210() {
        String deviceName = "Nokia-7210";
        String userAgentPattern = "Nokia7210.*";
        String userAgentString = "Nokia7210xxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    public void testMappingAndRetrievalForNokia7110() {
        String userAgentPattern = "Nokia7110/1\\.0.*";

        String deviceName = "Nokia7110";
        String userAgentString = "Nokia7110/1.0xxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    public void testMappingAndRetrievalForMozillaDevice() {
        String userAgentPattern =
                "Mozilla/4\\.0 (compatible; MSIE 6\\.0; " +
                "Windows 95; PalmSource; Blazer 3\\.0.*";
        String deviceName = "Blazer-4.0";
        String userAgentString =
                "Mozilla/4.0 (compatible; MSIE 6.0; " +
                "Windows 95; PalmSource; Blazer 3.0xxx";

        mapPatternAndDeviceAndFindDeviceByUserAgent(deviceName,
                                                    userAgentPattern,
                                                    userAgentString);
    }

    /**
     * Ensure that a device is identified as the next best general
     * class of device if it does not exist in the device repository.
     * <p>
     * e.g If the device matched by, DoCoMo/2.0 P901iSxxx, is not in the
     * device cache then it should be identified as an
     * {@link #GENERAL_I_MODE_DEVICE}.
     */
    public void testIdentificationOfUnknownDevice() {

        doTestIdentificationOfUnknownDoCoMoDevice();
    }

    /**
     * Ensure that a device is identified as the next best general class
     * of device if it does not exist in the cache when devices
     * with similar names exist.
     *
     * e.g If the device matched by, DoCoMo/2.0 P901iSxxx, not in the
     * device cache then it should be identified as an
     * {@link #GENERAL_I_MODE_DEVICE}. This should be the case even if the
     * device repository contains devices with similar names, e.g:
     * <p>
     * DoCoMo/2.0 P900iC <br/>
     * DoCoMo/2.0 P901iC <br/>
     */
    public void testIdentificationOfUnkownDeviceWhenSimiliarDevicesExistsInCache() {

        dpc.mapPatternAndDevice("DoCoMo/2\\.0 P901iC.*", "DoCoMo-P901iC");
        dpc.mapPatternAndDevice("DoCoMo/2\\.0 P900iC.*", "DoCoMo-P900iC");

        doTestIdentificationOfUnknownDoCoMoDevice();
    }

    /**
     * Helper method to test the identification of an unknown device
     * in the cache.
     */
    private void doTestIdentificationOfUnknownDoCoMoDevice() {
        initialiseDoCoMoDeviceCache();

        String userAgentNotInDeviceCache = "DoCoMo/2.0 P901iSxxx";
        assertUserAgentMatchesDevice(userAgentNotInDeviceCache,
                                     GENERAL_I_MODE_DEVICE);
    }

    /**
     * Initialise the cache so that any device with a pattern that
     * matches DoCoMo/* will be identified as an
     * {@link #GENERAL_I_MODE_DEVICE} if we fail to match against
     * more specific patterns that are available.
     */
    private void initialiseDoCoMoDeviceCache() {
        dpc.mapPatternAndDevice("DoCoMo/*", GENERAL_I_MODE_DEVICE);
    }

    /**
     * Maps the supplied deviceName to the supplied userAgentPattern and
     * attempts to match the supplied userAgentString to the specified device.
     *
     * @param deviceName device to be mapped to the userAgentPattern.
     * @param userAgentPattern UA pattern associated with deviceName.
     * @param userAgentString the UA string used to locate the specified
     * deviceName.
     */
    private void mapPatternAndDeviceAndFindDeviceByUserAgent(
            String deviceName,
            String userAgentPattern,
            String userAgentString) {

        // map the pattern to the device name in the cache
        dpc.mapPatternAndDevice(userAgentPattern, deviceName);
        // lets see if we can retrieve the name of the device by
        // supplying the user agent pattern.
        assertUserAgentMatchesDevice(userAgentString, deviceName);
    }

    /**
     * Asserts that the supplied userAgent is matched to the supplied
     * deviceName.
     *
     * @param userAgent the user agent string to be tested for matching
     * deviceName.
     *
     * @param deviceName the name of the device that the supplied userAgent
     * should match to.
     */
    private void assertUserAgentMatchesDevice(String userAgent,
                                              String deviceName) {
        String matchingDeviceName = "";
        matchingDeviceName = dpc.match(userAgent);
        assertEquals(deviceName, matchingDeviceName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	10055/1	rgreenall	VBM:2005092902 Fixed fallback identification of DoCoMo devices.

 26-Oct-05	9968/1	rgreenall	VBM:2005092902 Fixed fallback identification for DoCoMo devices.

 11-Oct-05	9712/5	rgreenall	VBM:2005092211 Fixed identification of DoCoMo devices (rework).

 10-Oct-05	9712/3	rgreenall	VBM:2005092211 Fixed identification of DoCoMo devices

 06-Oct-05	9712/1	rgreenall	VBM:2005092211 Fixed identification of DoCoMo devices.

 02-Mar-05	7130/5	rgreenall	VBM:2005011201 Further changes post review

 02-Mar-05	7130/3	rgreenall	VBM:2005011201 Post review corrections

 02-Mar-05	7130/1	rgreenall	VBM:2005011201 Fixed bug where the mapping of a user agent pattern to a device name would fail if the pattern was one character greater than the device name.

 ===========================================================================
*/
