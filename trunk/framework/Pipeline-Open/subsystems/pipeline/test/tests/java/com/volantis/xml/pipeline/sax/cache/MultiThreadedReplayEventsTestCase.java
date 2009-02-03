/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Â  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.Â  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;
import com.volantis.xml.pipeline.sax.cache.body.CacheBodyOperationProcess;
import com.volantis.xml.pipeline.sax.cache.body.CacheBodyOperationProcessState;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import org.xml.sax.SAXException;

import java.net.URL;

/**
 * This class is used to test that in a multi-threaded environment we only
 * ever record SAX events into the same cache once and that thereafter we
 * playback.
 *
 * <b> NB. THIS TESTCASE MAY FAIL WITH INSUFFICIENT MEMORY ALLOCATION...
 * This testcase relies on the synergetics cache model.  On those jvm that
 * support it we use soft references.  This means that if we run short of
 * memory in the course of running the test the garbage collector may
 * clean up our cache entry.  If that happens we would see the events being
 * recorded again.
 * </b>
 */
public class MultiThreadedReplayEventsTestCase extends PipelineTestAbstract {

    /**
     * Used to feedback errorMsg
     */
    String errorMsg;


    public MultiThreadedReplayEventsTestCase(String name) {
        super(name);
    }

    /**
     * Test that pipeline caching works with multi-threading.
     */
    public void testConcurrency() throws Exception {
        final XMLPipelineConfiguration config =
                super.createPipelineConfiguration();
        final CacheProcessConfiguration cpc = new CacheProcessConfiguration();
        config.storeConfiguration(CacheProcessConfiguration.class, cpc);
        cpc.createCache("myCache", "1000", "0");

        String path = getClass().getName().replace('.', '/');
        URL inputURL = getResourceURL(path + ".input.xml");
        URL expectedURL = getResourceURL(path + ".expected.xml");

        for (int i = 0; i < 50 && errorMsg == null; i++) {
            Thread thread = getThread(inputURL, expectedURL);
            thread.start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (errorMsg != null) {
            fail(errorMsg);
        }
    }

    /**
     * Get a thread that runs the pipeline process
     *
     * @param inputURL    The path to the input file
     * @param expectedURL The path to the expected file
     * @return a thread that runs the pipeline process
     */
    protected Thread getThread(final URL inputURL, final URL expectedURL) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    doTest(new TestPipelineFactory(),
                            inputURL,
                            expectedURL);

                } catch (Exception exc) {
                    exc.printStackTrace();
                    errorMsg = "Unexpected Exception encountered: " + exc;
                }
            }
        };
        return thread;
    }

    /**
     * The flag helps us determine whether the operation process has
     * recorded more that once.
     */
    static boolean hasRecorded = false;

    /**
     * We use this MockCacheBodyOperationProcess to determine whether we ever
     * get into a state where we try to record into the same cache in
     * multiple threads.
     */
    public class MockCacheBodyOperationProcess extends
            CacheBodyOperationProcess {

        // Javadoc inherited from superclass.
        public CacheBodyOperationProcessState initializeProcessState()
                throws SAXException {
            super.initializeProcessState();

            CacheBodyOperationProcessState state =
                    getCacheBodyOperationProcessState();

            if (state == CacheBodyOperationProcessState.RECORD_AND_FORWARD) {
                if (hasRecorded) {
                    errorMsg = "CacheBodyOperationProcess has already " +
                            "recorded. It should only record once.";
                }
                hasRecorded = true;
            } else if (state !=
                    CacheBodyOperationProcessState.PLAYBACK_AND_SUPPRESS) {
                errorMsg = "CacheBodyOperationProcess should " +
                        "be in playback state.";
            }

            return state;
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/2	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 04-Aug-03	285/1	doug	VBM:2003080402 Renamed XMLProcessConfiguration interface to Configuration

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 09-Jun-03	49/5	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 09-Jun-03	49/3	adrian	VBM:2003060505 Updated xml caching process to include cacheBody element

 06-Jun-03	26/2	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
