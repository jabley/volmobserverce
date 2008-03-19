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
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

import java.net.URL;

/**
 * This class is used to test that SAX events are successfully forwarded
 * through the process on the first parse of the input xml document.
 * The expected xml is equal to the input xml without the caching pipeline
 * markup.
 *
 * It then goes on to test that the recorded sax events correctly result in
 * the same output.  We provide a different input source that references the
 * same cache but has different content.  If we see that the result matches
 * the original expected result then we know two things:
 * <ol>
 * <li>We played back from the cache.
 * <li>Playback from the cache successfully resulted in the correct xml.
 * </ol>
 *
 * <b> NB. THIS TESTCASE MAY FAIL WITH INSUFFICIENT MEMORY ALLOCATION...
 * This testcase relies on the synergetics cache model.  On those jvm that
 * support it we use soft references.  This means that if we run short of
 * memory in the course of running the test the garbage collector may
 * clean up our cache entry.  If that happens we would see the content of the
 * second input file being forwarded down the pipeline as it would appear that
 * we never had an entry cached.
 * </b>
 */
public class SimpleReplayEventsTestCase extends PipelineTestAbstract {

    /**
     * The configuration used by this test case.
     */
    private CacheProcessConfiguration cpc;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        cpc = new CacheProcessConfiguration(Integer.MAX_VALUE);
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
        cpc.createCache("myCache", "1000", "100");

        XMLPipelineFactory pipelineFactory =
                new IntegrationTestHelper().getPipelineFactory();

        String path = getClass().getName().replace('.', '/');

        URL inputURL = getResourceURL(path + ".input-A.xml");
        URL expectedURL = getResourceURL(path + ".expected.xml");

        doTest(pipelineFactory, inputURL, expectedURL);

        inputURL = getResourceURL(path + ".input-B.xml");
        doTest(pipelineFactory, inputURL, expectedURL);
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
