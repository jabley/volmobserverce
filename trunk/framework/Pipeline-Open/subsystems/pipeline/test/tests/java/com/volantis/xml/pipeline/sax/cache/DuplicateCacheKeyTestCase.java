/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.� See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.� If not, see <http://www.gnu.org/licenses/>. 
*/


/* ----------------------------------------------------------------------------
 * Copyright Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

import java.net.URL;

/**
 * Ensure that two caches with different cache names but which have a common
 * cache key do not interact
 */
public class DuplicateCacheKeyTestCase  extends PipelineTestAbstract {

    /**
     * The configuration used by this test case.
     */
    private CacheProcessConfiguration cpc;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        cpc = new CacheProcessConfiguration();
    }

    // javadoc inherited
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        final XMLPipelineConfiguration config =
                super.createPipelineConfiguration();
        config.storeConfiguration(CacheProcessConfiguration.class, cpc);
        return config;
    }

    /**
     * Test that the SAX events are successfully forwarded through the process
     * on the first parse of the input xml document.
     */
    public void testCommonKey() throws Exception {
        cpc.createCache("cacheA", "1000", "0");
        cpc.createCache("cacheB", "1000", "0");
        XMLPipelineFactory pipelineFactory =
                new IntegrationTestHelper().getPipelineFactory();

        String path = getClass().getName().replace('.', '/');

        URL inputURL = getResourceURL(path + ".input-A.xml");
        URL expectedURL = getResourceURL(path + ".expected-A.xml");
        doTest(pipelineFactory, inputURL, expectedURL);

        inputURL = getResourceURL(path + ".input-B.xml");
        expectedURL = getResourceURL(path + ".expected-B.xml");
        doTest(pipelineFactory, inputURL, expectedURL);
    }
}