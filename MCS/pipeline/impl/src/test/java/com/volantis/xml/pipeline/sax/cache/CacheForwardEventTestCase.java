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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

/**
 * This class is used to test that SAX events are successfully forwarded
 * through the process on the first parse of the input xml document.
 * The expected xml is equal to the input xml without the caching pipeline
 * markup.
 */
public class CacheForwardEventTestCase extends PipelineTestAbstract {

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
    public void testPipeline() throws Exception {
        cpc.createCache("myCache", "1000", "0");

        doTest(new IntegrationTestHelper().getPipelineFactory());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 09-Jun-03	49/1	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 06-Jun-03	26/2	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
