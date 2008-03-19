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

import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

/**
 * Test case for the http varient of the web driver.
 */
public class HTTPWebDriverTestCase extends WebDriverTestAbstract {

    // JavaDoc inherited
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        XMLPipelineConfiguration config = super.createPipelineConfiguration();
        WebDriverConfigurationImpl webdConf = new WebDriverConfigurationImpl() {
            // javadoc inherited
            PluggableHTTPManager getPluggableHTTPManager(String protocol) {
                return new HTTPClientPluggableHTTPManager();
            }
            // javadoc inherited
            public void setTimeout(long timeout) {
            }
            // javadoc inherited
            public long getTimeoutInMillis() {
                return -1;
            }
        };
        config.storeConfiguration(WebDriverConfiguration.class, webdConf);
        return config;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Mar-05	7474/1	pcameron	VBM:2005031018 Added timeout to web driver configuration

 22-Mar-05	7472/1	pcameron	VBM:2005031018 Added timeout to web driver configuration

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jul-04	786/1	claire	VBM:2004071304 Refactor web driver to support HTTPS

 16-Jul-04	767/1	claire	VBM:2004070101 Provide Jigsaw implemention of PluggableHTTPManager

 ===========================================================================
*/
