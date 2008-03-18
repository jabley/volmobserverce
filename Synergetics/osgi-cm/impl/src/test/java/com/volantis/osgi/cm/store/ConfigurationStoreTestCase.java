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

package com.volantis.osgi.cm.store;

import com.volantis.osgi.cm.InternalConfiguration;
import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import mock.org.osgi.framework.BundleContextMock;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Test cases for {@link ConfigurationStore}.
 */
public class ConfigurationStoreTestCase
        extends TestCaseAbstract {

    private File bundleRootDir;

    protected void setUp() throws Exception {
        super.setUp();

        bundleRootDir = createTempDir("cmstore");
    }

    /**
     * Ensure that a service configuration can be round tripped.
     */
    public void testRoundTripServiceConfiguration() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final BundleContextMock bundleContextMock =
                new BundleContextMock("bundleContextMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        bundleContextMock.expects.getDataFile("")
                .returns(bundleRootDir).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FileManager fileManager = new FileManagerImpl(bundleRootDir, 10, 10);
        ConfigurationStore store = new ConfigurationStoreImpl(fileManager);

        InternalConfiguration written1 = storeServiceConfiguration(
                store, "pid1", null);
        InternalConfiguration written2 = storeServiceConfiguration(
                store, "pid2", "location");

        // Load the objects back in.
        Collection loaded = new HashSet(Arrays.asList(store.load()));
        Collection expected =
                new HashSet(Arrays.asList(new InternalConfiguration[]{
                        written1, written2
                }));
        assertEquals(expected, loaded);

        // Delete a configuration.
        store.remove(written1);

        // Load the objects back in.
        loaded = new HashSet(Arrays.asList(store.load()));
        expected = new HashSet(Arrays.asList(new InternalConfiguration[]{
                written2
        }));
        assertEquals(expected, loaded);

    }

    /**
     * Ensure that a number of factory configurations can be round tripped.
     */
    public void testRoundTripFactoryConfigurations() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final BundleContextMock bundleContextMock =
                new BundleContextMock("bundleContextMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        bundleContextMock.expects.getDataFile("")
                .returns(bundleRootDir).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FileManager fileManager = new FileManagerImpl(bundleRootDir, 10, 10);
        ConfigurationStore store = new ConfigurationStoreImpl(fileManager);

        InternalConfiguration written1 = storeServiceConfiguration(
                store, "pid1", null);
        InternalConfiguration written2 = storeServiceConfiguration(
                store, "pid2", "location");

        // Load the objects back in.
        Collection loaded = new HashSet(Arrays.asList(store.load()));
        Collection expected =
                new HashSet(Arrays.asList(new InternalConfiguration[]{
                        written1, written2
                }));
        assertEquals(expected, loaded);
    }

    private InternalConfiguration storeServiceConfiguration(
            ConfigurationStore store, String pid,
            String bundleLocation)
            throws IOException {

        // Store an object.
        CaseInsensitiveDictionary properties = new CaseInsensitiveDictionary();
        properties.put("a", "1");
        InternalConfiguration configuration = store.createConfiguration(
                pid, bundleLocation);
        configuration.replaceProperties(properties);

        store.update(configuration);
        return configuration;
    }

    public void testCreateConfiguration() throws IOException {

        final FileManagerMock fileManagerMock =
                new FileManagerMock("fileManagerMock", expectations);

        ConfigurationStore store = new ConfigurationStoreImpl(fileManagerMock);

        File file = new File("/0/0/a");
        fileManagerMock.expects.allocateFile().returns(file);

        InternalConfiguration configuration =
                store.createConfiguration("pid", null);

        assertEquals(file, configuration.getPersistentFile());
        assertEquals("pid", configuration.getPid());
    }

    public void testCreateFactoryConfiguration() throws IOException {

        final FileManagerMock fileManagerMock =
                new FileManagerMock("fileManagerMock", expectations);

        File file = new File("/0/0/a");
        fileManagerMock.expects.allocateFile().returns(file);
        fileManagerMock.expects.getRelativePath(file).returns("0/0/a");

        ConfigurationStore store = new ConfigurationStoreImpl(fileManagerMock);

        InternalConfiguration configuration =
                store.createFactoryConfiguration("factoryPid", null);

        assertEquals(file, configuration.getPersistentFile());
        assertEquals("factoryPid", configuration.getFactoryPid());
        assertTrue(configuration.getPid().endsWith(".0.0.a"));
    }
}
