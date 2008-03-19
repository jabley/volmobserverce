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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.time.Period;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfigurationImpl;
import com.volantis.xml.pipeline.sax.drivers.web.httpcache.CachingPluggableHTTPManager;

/**
 * This tests the {@link WebDriverConfigurationImpl} class.
 *
 * @todo Later Finish implementing these tests for the whole class.
 */
public class WebDriverConfigurationImplTestCase extends TestCaseAbstract {

    /**
     * Initialise a new instance of this test case.
     */
    public WebDriverConfigurationImplTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public WebDriverConfigurationImplTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Ensure that if no HTTPCacheConfiguration has been set that a
     * CachingPluggableHTTPManager is not created.
     * @throws Exception
     */
    public void testGetPluggableHTTPManagerWithoutConfiguration()
            throws Exception {
        // Create test instance
        WebDriverConfigurationImpl webdConfig = new WebDriverConfigurationImpl();

        // do not set the HTTPCacheConfiguration on the WebDriverConfiguration

        // Test null protocol string
        PluggableHTTPManager manager =
            webdConfig.getPluggableHTTPManager(null, null);
        manager.initialize(webdConfig, Period.INDEFINITELY);
        assertNotNull("Manager should exist (1)", manager);
        assertTrue("Manager should be an instance of caching http client (1)",
                   manager instanceof HTTPClientPluggableHTTPManager);
    }
    /**
     * This tests the {@link WebDriverConfigurationImpl#getPluggableHTTPManager}
     * method.
     */
    public void testGetPluggableHTTPManager() throws Exception {
        // Create test instance
        WebDriverConfigurationImpl webdConfig = new WebDriverConfigurationImpl();
        // Set a cache configuration. Without this a
        // CachingPluggableHTTPManager will not be created.
        webdConfig.setHTTPCacheConfiguration(
                new MockHTTPCacheConfiguration(null, 50, 4000));

        final XMLPipelineConfiguration pipelineConfiguration =
            createPipelineConfiguration();

        // Test null protocol string
        PluggableHTTPManager manager =
            webdConfig.getPluggableHTTPManager(null, pipelineConfiguration);
        manager.initialize(webdConfig, Period.INDEFINITELY);
        assertNotNull("Manager should exist (1)", manager);
        assertTrue("Manager should be an instance of caching http client (1)",
                   manager instanceof CachingPluggableHTTPManager);

        // Create test instance
        webdConfig = new WebDriverConfigurationImpl();

        // Set a cache configuration. Without this a
        // CachingPluggableHTTPManager will not be created.
        webdConfig.setHTTPCacheConfiguration(
                new MockHTTPCacheConfiguration(null, 50, 4000));

        // Test http client
        manager = webdConfig.getPluggableHTTPManager(
            "https", pipelineConfiguration);
        assertNotNull("Manager should exist (2)", manager);
        assertTrue("Manager should be an instance of http client (1)",
                   manager instanceof HTTPClientPluggableHTTPManager);

        PluggableHTTPManager managerCopy =
            webdConfig.getPluggableHTTPManager("https", pipelineConfiguration);
        assertNotNull("Manager should exist (3)", managerCopy);
        assertTrue("Manager should be an instance of http client (2)",
                   managerCopy instanceof HTTPClientPluggableHTTPManager);
        assertSame("Same manager instance should have been returned (1)",
                   manager,
                   managerCopy);

        // Create test instance (so existing value is not used)
        webdConfig = new WebDriverConfigurationImpl();
        webdConfig.setHTTPCacheConfiguration(
                new MockHTTPCacheConfiguration(null, 50,4000));

        // Test jigsaw client
        manager = webdConfig.getPluggableHTTPManager("http",
            pipelineConfiguration);
        manager.initialize(webdConfig, Period.INDEFINITELY);
        assertNotNull("Manager should exist (4)", manager);
        assertTrue("Manager should be an instance of caching http client (2)",
                   manager instanceof CachingPluggableHTTPManager);

        managerCopy = webdConfig.getPluggableHTTPManager("http",
            pipelineConfiguration);
        assertNotNull("Manager should exist (5)", managerCopy);
        assertTrue("Manager should be an instance of caching http client (3)",
                   managerCopy instanceof CachingPluggableHTTPManager);
        assertSame("Same manager instance should have been returned (2)",
                   manager,
                   managerCopy);
    }

    // JavaDoc inherited
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        final XMLPipelineConfiguration config = new IntegrationTestHelper().
            getPipelineFactory().createPipelineConfiguration();
        final ConnectionConfiguration connectionConfiguration =
            new ConnectionConfigurationImpl();
        connectionConfiguration.setCachingEnabled(true);
        connectionConfiguration.setMaxCacheEntries(1000);
        config.storeConfiguration(
            ConnectionConfiguration.class, connectionConfiguration);
        return config;
    }

    /**
     * Mock configuration object.
     */
    public static class MockHTTPCacheConfiguration
            implements HTTPCacheConfiguration {

        private String location;

        private int maxEntries;

        private int maxSize;

        public MockHTTPCacheConfiguration(String location, int maxEntries, int maxSize) {
            this.location = location;
            this.maxEntries = maxEntries;
            this.maxSize = maxSize;
        }

        public String getLocation() {
            return location;
        }

        public int getMaxEntries() {
            return maxEntries;
        }

        public int getPersistentCacheMaxSize() {
            return maxSize;
        }
    }
}
