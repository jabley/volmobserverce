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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfigurationImpl;
import com.volantis.xml.pipeline.sax.drivers.web.httpcache.CachingPluggableHTTPManager;
import com.volantis.xml.pipeline.sax.url.URLContentCacheConfiguration;
import com.volantis.shared.net.http.WaitTransaction;
import com.volantis.shared.throwable.ExtendedRuntimeException;

/**
 * Test the caching varient of the web driver
 */
public class CachingWebDriverTestCase extends WebDriverTestAbstract {

    // JavaDoc inherited
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        final XMLPipelineConfiguration config =
            super.createPipelineConfiguration();
        final ConnectionConfiguration connectionConfiguration =
            new ConnectionConfigurationImpl();
        connectionConfiguration.setCachingEnabled(true);
        connectionConfiguration.setMaxCacheEntries(1000);
        config.storeConfiguration(
            ConnectionConfiguration.class, connectionConfiguration);
        final URLContentCacheConfiguration urlConfig =
            new URLContentCacheConfiguration(config);
        WebDriverConfigurationImpl webdConf = new WebDriverConfigurationImpl() {
            // JavaDoc inherited
            PluggableHTTPManager getPluggableHTTPManager(String protocol) {
                return new CachingPluggableHTTPManager(
                    new HTTPClientPluggableHTTPManager(), urlConfig.getCache());
            }
        };
        config.storeConfiguration(WebDriverConfiguration.class, webdConf);
        return config;
    }

    /**
     * Test a get request that times out.
     *
     * @throws Exception if an error occurs.
     */
    public void testTimeoutGet() throws Exception {
        serverMock.addTransaction(new WaitTransaction(2000));

        try {
            doTest(pipelineFactory,
                    "TimeoutGetTestCase.input.xml",
                    "TimeoutGetTestCase.expected.xml");
            fail("Did not time out");
        } catch (ExtendedRuntimeException e) {
            HTTPException cause = (HTTPException) e.getCause();
            assertEquals("Request to '" +
                    serverMock.getURLAsString("/mantis/Mantis_Login.jsp") +
                    "' timed out after 1000ms",
                    cause.getMessage());
        }
    }
}
