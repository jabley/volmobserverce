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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl.accessors;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.impl.accessors.AbstractDeviceRepositoryAccessor;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractDeviceRepositoryAccessorTestAbstract
        extends TestCaseAbstract {

    /**
     * A class for executing tests that require access to a device repository
     * accessor and a repository connection.
     */
    protected abstract class DeviceRepositoryAccessorTest {
        /**
         * Run a test against the provided accessor and connection.
         *
         * @param accessor The accessor for the test repository
         * @param connection The connection for the test repository
         * @throws Exception if an error occurs
         */
        public abstract void runTest(AbstractDeviceRepositoryAccessor accessor,
                                     RepositoryConnection connection)
                throws Exception;
    }

    /**
     * Runs a test against a suitably defined repository accessor and
     * connection.
     *
     * <P>In order to allow consistent testing to take place, the repository
     * tested against should contain a mapping of TAC 350612 to the device
     * name Nokia-6210. In addition, the repository should <EM>not</EM>
     * contain a mapping for TAC 12345678 (including the fallback value of
     * TAC 123456).

     * @param test The test to run.
     */
    protected abstract void
            runDeviceRepositoryAccessorTest(DeviceRepositoryAccessorTest test)
            throws Exception;

    /**
     * Test that retrieved InternalDevice instances contain the same
     * instances of the Strings used to represent their policies.
     */
    public void testRetrieveInternalDeviceInternedPolicies() throws Exception {
        runDeviceRepositoryAccessorTest(
                new DeviceRepositoryAccessorTest() {
                    public void runTest(
                            AbstractDeviceRepositoryAccessor accessor,
                            RepositoryConnection connection)
                            throws Exception {
                        DefaultDevice device1 =
                            accessor.retrieveDevice(connection,"PC");

                        DefaultDevice device2 =
                            accessor.retrieveDevice(connection, "PC");

                        Map policies1 = new HashMap();
                        for (Iterator iter = device1.getPolicyNames();
                                iter.hasNext(); ) {
                            final String name = (String) iter.next();
                            policies1.put(name, device1.getPolicyValue(name));
                        }
                        Map policies2 = new HashMap();
                        for (Iterator iter = device2.getPolicyNames();
                                iter.hasNext(); ) {
                            final String name = (String) iter.next();
                            policies2.put(name, device2.getPolicyValue(name));
                        }

                        List keys1 = new ArrayList(policies1.keySet());
                        List keys2 = new ArrayList(policies2.keySet());
                        Collections.sort(keys1);
                        Collections.sort(keys2);
                        Iterator iter1 = keys1.iterator();
                        Iterator iter2 = keys2.iterator();

                        while (iter1.hasNext()) {
                            String key1 = (String) iter1.next();
                            String key2 = (String) iter2.next();

                            assertSame("Expected policy " + key1 +
                                    " to be the" +
                                    " same as policy " + key2, key1, key2);

                            String value1 = (String) policies1.get(key1);
                            String value2 = (String) policies2.get(key1);
                            assertSame("Expected value " + value1 +
                                    " to be the" +
                                    " same as value " + value2, value1,
                                    value2);
                        }
                    }
                }
        );
    }

    /**
     * Tests retrieval of a device by TAC from the DeviceRepositoryAccessor.
     *
     * @throws Exception if an error occurs
     */
    public void testRetrieveDeviceByTAC() throws Exception {
        runDeviceRepositoryAccessorTest(
                new DeviceRepositoryAccessorTest() {
                    public void runTest(
                            AbstractDeviceRepositoryAccessor accessor,
                            RepositoryConnection connection)
                            throws Exception {
                        assertEquals("Device should be retrieved for TAC 350612",
                                "Nokia-6210",
                                accessor.retrieveDeviceName(connection, 350612)
                        );

                        assertNull("No device should be retrieved for TAC 12345678",
                                accessor.retrieveDeviceName(connection, 12345678));
                    }
                }
        );
    }

    public void testRefreshTACCache() throws Exception {
        runDeviceRepositoryAccessorTest(
                new DeviceRepositoryAccessorTest() {
                    public void runTest(
                            AbstractDeviceRepositoryAccessor accessor,
                            RepositoryConnection connection)
                            throws Exception {
                        // Get a device to ensure cache is populated
                        accessor.retrieveDeviceName(connection, 350612);

                        // Flush the cache and ensure it has been removed
                        assertNotNull("TAC Cache should exist",
                                PrivateAccessor.getField(accessor, "tacCache"));
                        accessor.refreshDeviceCache();
                        assertNull("TAC Cache should be null",
                                PrivateAccessor.getField(accessor, "tacCache"));

                        // Repopulate the cache by requesting a value. Carry out
                        // timing at this point so we can get an idea of
                        // performance
                        long start = System.currentTimeMillis();
                        accessor.retrieveDeviceName(connection, 350612);
                        long end = System.currentTimeMillis();
                        System.out.println("\nTime for refresh and query: " +
                                (end - start));
                        assertNotNull("TAC Cache should exist",
                                PrivateAccessor.getField(accessor, "tacCache"));
                    }
                }
        );
    }

    /**
     * Test {@link AbstractDeviceRepositoryAccessor#normalizePattern}.
     */
    public void testNormalizePattern() throws Exception {
        // Actually don't care what the configuration is as we're testing
        // a protected method that doesn't use the accessor's configuration
        runDeviceRepositoryAccessorTest(new DeviceRepositoryAccessorTest() {
            public void runTest(AbstractDeviceRepositoryAccessor accessor,
                                RepositoryConnection connection)
                    throws Exception {
                assertEquals("Failed pass-through",
                             "this Should not normalize",
                             accessor.normalizePattern(
                                     "this Should not normalize"));
                assertEquals("Failed non-header pass-through (1)",
                             "this Should: not normalize",
                             accessor.normalizePattern(
                                     "this Should: not normalize"));
                assertEquals("Failed non-header pass-through (2)",
                             "this_Should1: not normalize",
                             accessor.normalizePattern(
                                     "this_Should1: not normalize"));
                assertEquals("Failed non-header pass-through (3)",
                             "this/Should: not normalize",
                             accessor.normalizePattern(
                                     "this/Should: not normalize"));
                assertEquals("Failed header normalize (1)",
                             "this-should: normalize",
                             accessor.normalizePattern(
                                     "this-Should: normalize"));
                assertEquals("Failed header normalize (2)",
                             "this-should_definately_: normalize",
                             accessor.normalizePattern(
                                     "this-Should_DefinateLy_: normalize"));
                assertEquals("Failed header normalize (3)",
                             "profile: Mozilla 4.0:Junk",
                             accessor.normalizePattern(
                                     "Profile: Mozilla 4.0:Junk"));
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Mar-05	6623/1	philws	VBM:2005010602 Port of correction for migration and other handling of secondary ID headers from 3.3

 01-Mar-05	7167/1	philws	VBM:2005010602 Correct migration and other handling of secondary ID headers

 23-Dec-04	6472/6	allan	VBM:2004121003 Intern device names and device policies

 23-Dec-04	6472/4	allan	VBM:2004121003 Intern device names and device policies

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-04	5315/2	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 13-Aug-04	5187/1	adrianj	VBM:2004080302 UAProf URI caching mechanism

 06-Aug-04	5121/2	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC

 04-Aug-04	5065/3	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 ===========================================================================
*/
